package com.example.canya.member.service;

import com.example.canya.board.entity.Board;
import com.example.canya.board.repository.BoardRepository;
import com.example.canya.comment.entity.Comment;
import com.example.canya.comment.repository.CommentRepository;
import com.example.canya.community.entity.Community;
import com.example.canya.community.repository.CommunityRepository;
import com.example.canya.communityComment.entity.CommunityComment;
import com.example.canya.communityComment.repository.CommunityCommentRepository;
import com.example.canya.heart.entity.Heart;
import com.example.canya.heart.repository.HeartRepository;
import com.example.canya.jwt.JwtAuthFilter;
import com.example.canya.jwt.TokenProvider;
import com.example.canya.jwt.dto.TokenDto;
import com.example.canya.member.dto.MemberRequestDto;
import com.example.canya.member.dto.MemberResponseDto;
import com.example.canya.member.dto.MypageResponseDto;
import com.example.canya.member.entity.Authority;
import com.example.canya.member.entity.Member;
import com.example.canya.member.repository.MemberRepository;
import com.example.canya.refreshToken.RefreshToken;
import com.example.canya.refreshToken.RefreshTokenRepository;
import com.example.canya.s3.S3Uploader;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
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
    private final CommunityRepository communityRepository;
    private final CommunityCommentRepository communityCommentRepository;
    private final S3Uploader s3Uploader;
    private final String DEFAULT_IMAGE = "http://doograpys.com/common/img/default_profile.png";

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
                .status("tall")
                .build();

        memberRepository.save(member);

        return new ResponseEntity<>("회원가입에 성공하셨습니다.", HttpStatus.CREATED);
    }


    public ResponseEntity<?> login(MemberRequestDto requestDto, HttpServletResponse response){
        UsernamePasswordAuthenticationToken authenticationToken = requestDto.toAuthentication();
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        Optional<Member> member = memberRepository.findByMemberName(requestDto.getMemberName());
        if(member.isEmpty()){
            return new ResponseEntity<>("아이디 혹은 패스워드를 확인해주세요",HttpStatus.BAD_REQUEST);
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


        return new ResponseEntity<>("로그인에 성공하셨습니다.", httpHeaders, HttpStatus.OK);
    }

    @Transactional(readOnly = true)
    public ResponseEntity<?> getHeartBoards(Member member, Integer page, Integer size) {

        Pageable pageable = PageRequest.of(page, size);
        Slice<Heart> hearts = heartRepository.findAllByMember_MemberIdOrderByCreatedAtDesc(member.getMemberId(), pageable);

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

        MypageResponseDto mypageResponseDto = new MypageResponseDto(myHeartBoardList, hearts.isLast());

        return new ResponseEntity<>(mypageResponseDto, HttpStatus.OK);
    }

    @Transactional(readOnly = true)
    public ResponseEntity<?> getMyComments(Member member, Integer page, Integer size) {

        Pageable pageable = PageRequest.of(page, size);
        Slice<Comment> comments = commentRepository.findAllByMember_MemberIdOrderByCreatedAtDesc(member.getMemberId(), pageable);

        if(comments.isEmpty()) {
            return new ResponseEntity<>("작성한 댓글이 없습니다.", HttpStatus.BAD_REQUEST);
        }

        List<MemberResponseDto> myCommentList = new ArrayList<>();
        for(Comment commentList : comments) {

            MemberResponseDto memberResponseDto = new MemberResponseDto(commentList);

            myCommentList.add(memberResponseDto);
        }

        MypageResponseDto mypageResponseDto = new MypageResponseDto(myCommentList, comments.isLast());

        return new ResponseEntity<>(mypageResponseDto, HttpStatus.OK);
    }

    @Transactional(readOnly = true)
    public ResponseEntity<?> getMyBoards(Member member, Integer page, Integer size) {

        Pageable pageable = PageRequest.of(page, size);
        Slice<Board> boards = boardRepository.findBoardByMemberOrderByCreatedAtDesc(member, pageable);

        if(boards.isEmpty()) {
            return new ResponseEntity<>("작성한 게시물이 없습니다.", HttpStatus.BAD_REQUEST);
        }

        List<MemberResponseDto> myBoardList = new ArrayList<>();
        for(Board boardList : boards) {

            MemberResponseDto memberResponseDto = new MemberResponseDto(boardList);

            myBoardList.add(memberResponseDto);
        }

        MypageResponseDto mypageResponseDto = new MypageResponseDto(myBoardList, boards.isLast());

        return new ResponseEntity<>(mypageResponseDto, HttpStatus.OK);
    }

    @Transactional(readOnly = true)
    public ResponseEntity<?> getMyCommunities(Member member, Integer page, Integer size) {

        Pageable pageable = PageRequest.of(page, size);
        Slice<Community> communities = communityRepository.findCommunityByMemberOrderByCreatedAtDesc(member, pageable);

        if(communities.isEmpty()) {
            return new ResponseEntity<>("작성한 커뮤니티 글이 없습니다.", HttpStatus.BAD_REQUEST);
        }

        List<MemberResponseDto> myCommunityList = new ArrayList<>();
        for(Community communityList : communities) {

            MemberResponseDto memberResponseDto = new MemberResponseDto(communityList);

            myCommunityList.add(memberResponseDto);
        }

        MypageResponseDto mypageResponseDto = new MypageResponseDto(myCommunityList, communities.isLast());

        return new ResponseEntity<>(mypageResponseDto, HttpStatus.OK);
    }

    @Transactional(readOnly = true)
    public ResponseEntity<?> getMycommunityComments(Member member, Integer page, Integer size) {

        Pageable pageable = PageRequest.of(page, size);
        Slice<CommunityComment> communityComments = communityCommentRepository.findCommunityCommentByMember_MemberIdOrderByCreatedAtDesc(member.getMemberId(), pageable);

        if(communityComments.isEmpty()) {
            return new ResponseEntity<>("작성한 커뮤니티 댓글이 없습니다.", HttpStatus.BAD_REQUEST);
        }

        List<MemberResponseDto> myCommunityCommentList = new ArrayList<>();
        for(CommunityComment communityCommentList : communityComments) {

            MemberResponseDto memberResponseDto = new MemberResponseDto(communityCommentList);

            myCommunityCommentList.add(memberResponseDto);
        }

        MypageResponseDto mypageResponseDto = new MypageResponseDto(myCommunityCommentList, communityComments.isLast());

        return new ResponseEntity<>(mypageResponseDto, HttpStatus.OK);
    }

    @Transactional(readOnly = true)
    public ResponseEntity<?> getAllMypage(Member member) {

        List<Board> createdAtBoards = boardRepository.findTop3ByMember_MemberIdOrderByCreatedAtDesc(member.getMemberId());
        List<Heart> hearts = heartRepository.findTop3ByMember_MemberIdOrderByCreatedAtDesc(member.getMemberId());
        List<Comment> comments = commentRepository.findTop3ByMember_MemberIdOrderByCreatedAtDesc(member.getMemberId());
        List<Community> communities = communityRepository.findTop3ByMember_MemberIdOrderByCreatedAtDesc(member.getMemberId());
        List<CommunityComment> communityComments = communityCommentRepository.findTop3ByMember_MemberIdOrderByCreatedAtDesc(member.getMemberId());
        Optional<Member> memberInfo = memberRepository.findById(member.getMemberId());

        List<MemberResponseDto> recentlyMyBoardList = new ArrayList<>();
        List<MemberResponseDto> recentlyMyHeartBoardList = new ArrayList<>();
        List<MemberResponseDto> recentlyMyCommentList = new ArrayList<>();
        List<MemberResponseDto> recentlyMyCommunityList = new ArrayList<>();
        List<MemberResponseDto> recentlyMyCommunityCommentList = new ArrayList<>();

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

        for (Community communityList : communities) {
            MemberResponseDto memberResponseDto = new MemberResponseDto(communityList);

            recentlyMyCommunityList.add(memberResponseDto);
        }

        for (CommunityComment communityCommentList : communityComments) {
            MemberResponseDto memberResponseDto = new MemberResponseDto(communityCommentList);

            recentlyMyCommunityCommentList.add(memberResponseDto);
        }

        MypageResponseDto mypageTotalResponseDto = new MypageResponseDto(recentlyMyBoardList, recentlyMyHeartBoardList, recentlyMyCommentList, recentlyMyCommunityList, recentlyMyCommunityCommentList, memberInfo.get());

        return new ResponseEntity<>(mypageTotalResponseDto, HttpStatus.OK);
    }


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
