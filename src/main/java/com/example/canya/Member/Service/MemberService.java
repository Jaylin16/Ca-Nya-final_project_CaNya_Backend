package com.example.canya.Member.Service;

import com.example.canya.Board.Entity.Board;
import com.example.canya.Board.Repository.BoardRepository;
import com.example.canya.Comment.Entity.Comment;
import com.example.canya.Comment.Repository.CommentRepository;
import com.example.canya.Heart.Entity.Heart;
import com.example.canya.Heart.Repository.HeartRepository;
import com.example.canya.JWT.Dto.TokenDto;
import com.example.canya.JWT.JwtAuthFilter;
import com.example.canya.JWT.TokenProvider;
import com.example.canya.Member.Dto.MemberRequestDto;
import com.example.canya.Member.Dto.MemberResponseDto;
import com.example.canya.Member.Dto.MypageResponseDto;
import com.example.canya.Member.Entity.Authority;
import com.example.canya.Member.Entity.Member;
import com.example.canya.Member.Repository.MemberRepository;
import com.example.canya.RefreshToken.RefreshToken;
import com.example.canya.RefreshToken.RefreshTokenRepository;
import com.example.canya.S3.S3Uploader;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;
    private final HeartRepository heartRepository;
    private final BoardRepository boardRepository;
    private final CommentRepository commentRepository;
    private final S3Uploader s3Uploader;


    private final String DEFAULT_IMAGE = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRLxVwdzaxNLKosglrrPJRFG8ojryDuVby2yL8J4zwn&s";

    public ResponseEntity<?> nameCheck(String memberName){
        Optional<Member> memberOptional = memberRepository.findByMemberName(memberName);
        if(memberOptional.isEmpty()){
            return new ResponseEntity<>("사용 가능 한 아이디입니다.",HttpStatus.OK);
        }
        return new ResponseEntity<>("중복 된 아이디 입니다", HttpStatus.BAD_REQUEST);
    }

    public ResponseEntity<?> nicknameCheck(String memberNickname){
        Optional<Member> memberOptional = memberRepository.findByMemberNickname(memberNickname);
        if(memberOptional.isEmpty()){
            return new ResponseEntity<>("사용 가능 한 닉네임입니다.",HttpStatus.OK);
        }
        return new ResponseEntity<>("중복 된 닉네임 입니다", HttpStatus.BAD_REQUEST);
    }


    public ResponseEntity<?> signUp(MemberRequestDto requestDto){

        Member member = Member.builder()
                .memberName(requestDto.getMemberName())
                .password(passwordEncoder.encode(requestDto.getPassword()))
                .memberNickname(requestDto.getMemberNickname())
                .memberProfileImage(DEFAULT_IMAGE)
                .authority(Authority.ROLE_USER)
                .build();

        memberRepository.save(member);

        return new ResponseEntity<>("회원가입에 성공하셨습니다.", HttpStatus.CREATED);
    }


    public ResponseEntity<?> login(MemberRequestDto requestDto, HttpServletResponse response){
        UsernamePasswordAuthenticationToken authenticationToken = requestDto.toAuthentication();
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
//        Member member = memberRepository.findByMemberName(requestDto.getMemberName())
//                .orElseThrow(() -> new UsernameNotFoundException("존재하지 않는 유저입니다."));
        Optional<Member> member = memberRepository.findByMemberName(requestDto.getMemberName());
        if(member.isEmpty()){
            return new ResponseEntity<>("아이도 혹은 패스워드를 확인해주세요",HttpStatus.BAD_REQUEST);
        }

        if (!passwordEncoder.matches(requestDto.getPassword(), member.get().getPassword())){
            return new ResponseEntity<>("아이디 혹은 패스워드를 확인해주세요.", HttpStatus.BAD_REQUEST);
        }

        TokenDto tokenDto = tokenProvider.generateTokenDto(authentication);
        RefreshToken refreshToken = RefreshToken.builder()
                .key(authentication.getName())
                .value(tokenDto.getRefreshToken())
                .build();

        refreshTokenRepository.save(refreshToken);

        HttpHeaders httpHeaders= new HttpHeaders();
        httpHeaders.add(JwtAuthFilter.AUTHORIZATION_HEADER , JwtAuthFilter.BEARER_PREFIX + tokenDto.getAccessToken());
        httpHeaders.add("Refresh-Token" , tokenDto.getRefreshToken());
        httpHeaders.add("memberNickname",requestDto.getMemberNickname());


        return new ResponseEntity<>("로그인에 성공하셨습니다.", httpHeaders, HttpStatus.OK);
    }

    //마이페이지 내가 좋아요한 게시글 전체 조회
    @Transactional
    public ResponseEntity<?> getHeartBoards(Member member) {

        List<Heart> hearts = heartRepository.findAllByMember_MemberId(member.getMemberId());

        List<MemberResponseDto> myHeartBoardList = new ArrayList<>();
        for (Heart heartList : hearts) {

            Long boardId = heartList.getBoard().getBoardId();
            Optional<Board> board = boardRepository.findById(boardId);
            if(board.isEmpty()) {
                return new ResponseEntity<>("좋아요한 게시글이 없습니다.", HttpStatus.BAD_REQUEST);
            }

            MemberResponseDto memberResponseDto = new MemberResponseDto(board.get());

            myHeartBoardList.add(memberResponseDto);
        }
        return new ResponseEntity<>(myHeartBoardList, HttpStatus.OK);
    }

    //마이페이지 내가 작성한 댓글 전체 조회
    @Transactional
    public ResponseEntity<?> getMyComments(Member member) {

        List<Comment> comments = commentRepository.findAllByMember_MemberId(member.getMemberId());

        List<MemberResponseDto> myCommentList = new ArrayList<>();
        for(Comment commentList : comments) {

            MemberResponseDto memberResponseDto = new MemberResponseDto(commentList);

            myCommentList.add(memberResponseDto);

        }
        return new ResponseEntity<>(myCommentList, HttpStatus.OK);
    }

    //마이페이지 내가 작성한 게시글 전체 조회
    @Transactional
    public ResponseEntity<?> getMyBoards(Member member) {

        List<Board> boards = boardRepository.findBoardByMember(member);

        if(boards.isEmpty()) {
            return new ResponseEntity<>("작성한 게시물이 없습니다.", HttpStatus.BAD_REQUEST);
        }

        List<MemberResponseDto> myBoardList = new ArrayList<>();
        for(Board boardList : boards) {

            MemberResponseDto memberResponseDto = new MemberResponseDto(boardList);

            myBoardList.add(memberResponseDto);

        }
        return new ResponseEntity<>(myBoardList, HttpStatus.OK);
    }

    //마이페이지 메인 (모두 보기) 최신순 기준 3개씩만 보이도록 반환
    @Transactional
    public ResponseEntity<?> getAllMypage(Member member) {

        Pageable pageable = PageRequest.of(0, 3);

        List<Board> createdAtBoards = boardRepository.findByMember_MemberIdOrderByCreatedAtDesc(member.getMemberId(), pageable);
        List<Heart> hearts = heartRepository.findAllByMember_MemberIdOrderByCreatedAtDesc(member.getMemberId(), pageable);
        List<Comment> comments = commentRepository.findAllByMember_MemberIdOrderByCreatedAtDesc(member.getMemberId(), pageable);
        Optional<Member> memberInfo = memberRepository.findById(member.getMemberId());

        List<MemberResponseDto> recentlyMyBoardList = new ArrayList<>();
        List<MemberResponseDto> recentlyMyHeartBoardList = new ArrayList<>();
        List<MemberResponseDto> recentlyMyCommentList = new ArrayList<>();

        for (Board boardList : createdAtBoards) {

            MemberResponseDto memberResponseDto = new MemberResponseDto(boardList);

            recentlyMyBoardList.add(memberResponseDto);
        }

        for (Heart heartList : hearts) {
            Long boardId = heartList.getBoard().getBoardId();
            Optional<Board> board = boardRepository.findById(boardId);
            if(board.isEmpty()) {
                return new ResponseEntity<>("좋아요한 게시글이 없습니다.", HttpStatus.BAD_REQUEST);
            }

            MemberResponseDto memberResponseDto = new MemberResponseDto(board.get());

            recentlyMyHeartBoardList.add(memberResponseDto);
        }

        for (Comment commentList : comments) {
            MemberResponseDto memberResponseDto = new MemberResponseDto(commentList);
            recentlyMyCommentList.add(memberResponseDto);
        }

        MypageResponseDto mypageTotalResponseDto = new MypageResponseDto(recentlyMyBoardList, recentlyMyHeartBoardList, recentlyMyCommentList, memberInfo.get());

        return new ResponseEntity<>(mypageTotalResponseDto, HttpStatus.OK);
    }

    //마이페이지 내 프로필 사진 변경
    @Transactional
    public ResponseEntity<?> profileUpdate(Member member, MultipartFile image) throws IOException {

        Optional<Member> mypageMember = memberRepository.findById(member.getMemberId());
        if (mypageMember.isEmpty()) {
            return new ResponseEntity<>("로그인이 필요한 서비스 입니다.", HttpStatus.BAD_REQUEST);
        }

        String originProfile = mypageMember.get().getMemberProfileImage();
        String targetProfileName = "memberProfileImage" + originProfile.substring(originProfile.lastIndexOf("/"));
        s3Uploader.deleteFile(targetProfileName);

        String newProfileImage = s3Uploader.upload(image, "memberProfileImage");
        mypageMember.get().update(newProfileImage);

        return new ResponseEntity<>("프로필 사진 변경 완료!", HttpStatus.OK);
    }

}
