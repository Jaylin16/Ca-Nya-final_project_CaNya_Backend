package com.example.canya.Board.Service;

import com.example.canya.Board.Dto.*;
import com.example.canya.Board.Entity.Board;
import com.example.canya.Board.Repository.BoardRepository;
import com.example.canya.Heart.Repository.HeartRepository;
import com.example.canya.Image.Entity.Image;
import com.example.canya.Image.Repository.ImageRepository;
import com.example.canya.Member.Entity.Member;
import com.example.canya.Member.Service.MemberDetailsImpl;
import com.example.canya.Rating.Dto.RatingRequestDto;
import com.example.canya.Rating.Dto.RatingResponseDto;
import com.example.canya.Rating.Entity.Rating;
import com.example.canya.Rating.Repository.RatingRepository;
import com.example.canya.S3.S3Uploader;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.*;

@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;
    private final String DEFAULT_THUMBNAIL = "https://assets.website-files.com/5ee732bebd9839b494ff27cd/5ef0a6c746931523ace53017_Starbucks.jpg";
    private final ImageRepository imageRepository;
    private final RatingRepository ratingRepository;
    private final HeartRepository heartRepository;
    private final S3Uploader s3Uploader;

    public void addBoards(List<Board> boardList, List<BoardResponseDto> returningDto) {
        for (Board board : boardList) {
            Rating ratingList = ratingRepository.findRatingByBoardAndMemberId(board, board.getMember().getMemberId());

            List<Map.Entry<String, Double>> ratings = ratingList.getTwoHighestRatings(ratingList);

            RatingResponseDto ratingDto = new RatingResponseDto(ratings.get(0), ratings.get(1), ratingList);
            boolean isLiked = heartRepository.existsByBoardAndMember_MemberId(board, board.getMember().getMemberId());

            returningDto.add(new BoardResponseDto(board, ratingDto, isLiked));
        }
        new ResponseEntity<>(returningDto, HttpStatus.OK);
    }

    public ResponseEntity<?> getBoards() {

        List<Board> allBoards = boardRepository.findTop8ByOrderByCreatedAtDesc();
        List<Board> bestBoards = boardRepository.findTop4ByOrderByTotalHeartCountDesc();
        List<Board> newBoards = boardRepository.findTop6ByOrderByCreatedAtDesc();

        List<Board> canyaCoffeeBoards =
                boardRepository.findTop3ByHighestRatingContainingOrderByTotalHeartCountDesc("coffee");
        List<Board> canyaMoodBoards =
                boardRepository.findTop3ByHighestRatingContainingOrderByTotalHeartCountDesc("mood");
        List<Board> canyaDessertBoards =
                boardRepository.findTop3ByHighestRatingContainingOrderByTotalHeartCountDesc("dessert");

        List<BoardResponseDto> bestDto = new ArrayList<>();
        List<BoardResponseDto> dessertResponseDto = new ArrayList<>();
        List<BoardResponseDto> coffeeResponseDto = new ArrayList<>();
        List<BoardResponseDto> moodResponseDto = new ArrayList<>();
        List<BoardResponseDto> newDto = new ArrayList<>();
        List<BoardResponseDto> allDto = new ArrayList<>();

        for (Board board : bestBoards) {

            boolean isLiked = heartRepository.existsByBoardAndMember_MemberId(board, board.getMember().getMemberId());
            BoardResponseDto boardResponseDto = new BoardResponseDto(board,isLiked);
            bestDto.add(boardResponseDto);
        }

        addBoards(canyaCoffeeBoards, coffeeResponseDto);
        addBoards(canyaMoodBoards, moodResponseDto);
        addBoards(canyaDessertBoards, dessertResponseDto);
        addBoards(allBoards, allDto);
        addBoards(newBoards, newDto);
        return new ResponseEntity<>(new MainPageDto(coffeeResponseDto, moodResponseDto, dessertResponseDto, newDto, allDto, bestDto), HttpStatus.OK);
    }

    public ResponseEntity<?> getMainCategory(String keyword, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);

        List<BoardResponseDto> keywordPick = new ArrayList<>();

        int boardNum = boardRepository.findBoardsByHighestRatingContainingOrderByTotalHeartCountDesc(keyword).size();

        Slice<Board> boardList = boardRepository.findBoardsByHighestRatingContainingOrderByTotalHeartCountDesc(keyword, pageable);

        for (Board board : boardList) {
            Rating ratingList = ratingRepository.findRatingByBoardAndMemberId(board, board.getMember().getMemberId());

            List<Map.Entry<String, Double>> ratings = ratingList.getTwoHighestRatings(ratingList);

            boolean isLiked = heartRepository.existsByBoardAndMember_MemberId(board, board.getMember().getMemberId());
            RatingResponseDto ratingDto = new RatingResponseDto(ratings.get(0), ratings.get(1), ratingList);

            keywordPick.add(new BoardResponseDto(board, ratingDto, isLiked));
        }

        return new ResponseEntity<>(new BoardResponseDto(keywordPick, size, boardNum, page), HttpStatus.OK);
    }

    // 이 부분은 query DSL 로 바꾸기.
    public ResponseEntity<?> searchBoard(String category, String keyword, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        List<BoardResponseDto> boardResponseDtos = new ArrayList<>();
        int boardNum = 0;
        if (category.equals("all")) {
//            List<Board> boards = boardRepository.findBoardByMember_MemberNicknameBoardContentAndBoardTitleContaining(keyword);
//            Slice<Board> boardList = boardRepository.findBoardByMember_MemberNicknameBoardContentAndBoardTitleContaining(keyword, pageable);
//            List<BoardResponseDto> dtoList = new ArrayList<>();
//            for (Board board : boardList) {
//                dtoList.add(new BoardResponseDto(board));
//                return new ResponseEntity<>(new BoardResponseDto(boardResponseDtos, size, boards.size(), page), HttpStatus.OK);
//            }
            return new ResponseEntity<>("나중에 할꺼", HttpStatus.OK);

        }
        if (Objects.equals(keyword, "")) {
            List<Board> boards = boardRepository.findAll();
            List<BoardResponseDto> dtoList = new ArrayList<>();
            for (Board board : boards) {
                boolean isLiked = heartRepository.existsByBoardAndMember_MemberId(board, board.getMember().getMemberId());
                dtoList.add(new BoardResponseDto(board,isLiked));
                return new ResponseEntity<>(new BoardResponseDto(boardResponseDtos, size, boards.size(), page), HttpStatus.OK);
            }
        }

        if (category.equals("memberNickname")) {

            List<Board> boards = boardRepository.findBoardByMemberMemberNicknameContaining(keyword);
            Slice<Board> boardList = boardRepository.findBoardByMemberMemberNicknameContaining(keyword, pageable);

            if (boardList.isEmpty()) {
                return new ResponseEntity<>("해당 조회 결과가 없습니다.", HttpStatus.OK);
            }

            for (Board board : boardList) {
                boolean isLiked = heartRepository.existsByBoardAndMember_MemberId(board, board.getMember().getMemberId());
                boardResponseDtos.add(new BoardResponseDto(board,isLiked));
            }

            return new ResponseEntity<>(new BoardResponseDto(boardResponseDtos, size, boards.size(), page), HttpStatus.OK);
        }
        if (category.equals("boardTitle")) {
            boardNum = boardRepository.findBoardsByBoardTitleContaining(keyword).size();

            Slice<Board> boardList = boardRepository.findBoardsByBoardTitleContaining(keyword, pageable);
            if (boardList.isEmpty()) {
                return new ResponseEntity<>("해당 조회 결과가 없습니다.", HttpStatus.OK);
            }

            for (Board board : boardList) {
                boolean isLiked = heartRepository.existsByBoardAndMember_MemberId(board, board.getMember().getMemberId());
                boardResponseDtos.add(new BoardResponseDto(board,isLiked));
            }
            return new ResponseEntity<>(new BoardResponseDto(boardResponseDtos, size, boardNum, page), HttpStatus.OK);
        }
        if (category.equals("boardContent")) {

            boardNum = boardRepository.findBoardsByBoardContentContaining(keyword).size();

            Slice<Board> boardList = boardRepository.findBoardsByBoardContentContaining(keyword, pageable);

            if (boardList.isEmpty()) {

                return new ResponseEntity<>("해당 조회 결과가 없습니다.", HttpStatus.OK);
            }

            for (Board board : boardList) {
                boolean isLiked = heartRepository.existsByBoardAndMember_MemberId(board, board.getMember().getMemberId());
                boardResponseDtos.add(new BoardResponseDto(board,isLiked));

            }
            return new ResponseEntity<>(new BoardResponseDto(boardResponseDtos, size, boardNum, page), HttpStatus.OK);
        }

        return new ResponseEntity<>("해당되는 카테고리를 넣어주세요", HttpStatus.BAD_REQUEST);
    }

    @Transactional
    public ResponseEntity<?> saveBoard(MemberDetailsImpl memberDetails) {

        int lastBoardIndex = boardRepository.findBoardByMember(memberDetails.getMember()).size();
        List<Board> boardList = boardRepository.findBoardByMember(memberDetails.getMember());
        if (lastBoardIndex == 0) {
            Board board = boardRepository.save(new Board(memberDetails.getMember()));
            return new ResponseEntity<>(board.getBoardId(), HttpStatus.OK);
        }
        if (boardList.get(lastBoardIndex - 1).getImageList().size() == 0) {

            return new ResponseEntity<>("이미 만든 보드가 존재합니다.", HttpStatus.BAD_REQUEST);
        } else {
            Board board = boardRepository.save(new Board(memberDetails.getMember()));
            return new ResponseEntity<>(board.getBoardId(), HttpStatus.OK);
        }
    }


    // s3 uploader 에서 삭제 해주는 부분 추가하기.
    @Transactional
    public ResponseEntity<?> editBoard(BoardRequestDto dto, List<MultipartFile> images, Long boardId, Member member, String[] urls) throws IOException {

        Board board = boardRepository.findById(boardId).orElse(null);
        List<Image> imageList = imageRepository.findAllByBoard(board);
        Rating rating = ratingRepository.findRatingByBoardAndMemberId(board, member.getMemberId());

        assert board != null;
        if (!Objects.equals(board.getMember().getMemberId(), member.getMemberId())) {
            return new ResponseEntity<>("작성자가 다릅니다.", HttpStatus.BAD_REQUEST);
        }

        if (member.getMemberId().equals(board.getMember().getMemberId())) {
            board.update(dto);

            RatingRequestDto ratingDto = new RatingRequestDto(dto.getRatings()[0], dto.getRatings()[1], dto.getRatings()[2], dto.getRatings()[3], dto.getRatings()[4], dto.getRatings()[5]);

            rating.update(ratingDto);

            List<Map.Entry<String, Double>> twoHighestRatings = rating.getTwoHighestRatings(rating);
            board.update(dto, twoHighestRatings.get(0).getKey(), twoHighestRatings.get(1).getKey());

            imageRepository.deleteAll(imageList);
            if (urls != null) {
                for (String url : urls) {
                    imageRepository.save(new Image(board, url, board.getMember()));
                }
            }
            if (images != null) {
                for (MultipartFile image : images) {
                    imageRepository.save(new Image(board, s3Uploader.upload(image, "boardImage"), board.getMember()));
                }
            }
            for (Image deletingImage : imageList) {
                String imageUrl = deletingImage.getImageUrl();
                String target = "boardImage" + imageUrl.substring(imageUrl.lastIndexOf("/"));
                s3Uploader.deleteFile(target);
            }
        }
        return new ResponseEntity<>("수정이 완료되었습니다.", HttpStatus.OK);
    }

    public ResponseEntity<?> getBoardDetail(Long boardId) {
        Board board = boardRepository.findById(boardId).orElse(null);
        if (board == null) {
            return new ResponseEntity<>("존재 하지 않는 게시물입니다", HttpStatus.BAD_REQUEST);
        }
        boolean isLiked = heartRepository.existsByBoardAndMember_MemberId(board, board.getMember().getMemberId());
        List<Image> imageList = board.getImageList();
        Rating rating = ratingRepository.findRatingByBoard(board);

        BoardResponseDto responseDto = new BoardResponseDto(board, imageList, rating, isLiked);

        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    // try to divide the sections up. takes too long to get the result at the moment.
    @Transactional
    public ResponseEntity<?> confirmBoard(BoardRequestDto dto, List<MultipartFile> images, Long boardId) throws IOException {
        Board board = boardRepository.findById(boardId).orElse(null);
        assert board != null;
        Member member = board.getMember();
        if (images != null) {
            for (MultipartFile image : images) {
                imageRepository.save(new Image(board, s3Uploader.upload(image, "boardImage"), member));
            }
        } else {
            imageRepository.save(new Image(board, DEFAULT_THUMBNAIL, member));
        }
        RatingRequestDto ratingDto = new RatingRequestDto(dto.getRatings()[0], dto.getRatings()[1], dto.getRatings()[2], dto.getRatings()[3], dto.getRatings()[4], dto.getRatings()[5]);

        Rating rating = new Rating(ratingDto, board, board.getMember());
        ratingRepository.save(rating);

        List<Map.Entry<String, Double>> twoHighestRatings = rating.getTwoHighestRatings(rating);
        board.update(dto, twoHighestRatings.get(0).getKey(), twoHighestRatings.get(1).getKey());
        boardRepository.save(board);

        return new ResponseEntity<>("게시글 작성이 완료 되었습니다.", HttpStatus.CREATED);
    }

    @Transactional
    public ResponseEntity<?> deleteBoard(Long boardId, Member member) {

        Optional<Board> board = boardRepository.findById(boardId);
        if (board.isEmpty()) {
            return new ResponseEntity<>("게시글이 존재 하지 않습니다", HttpStatus.BAD_REQUEST);
        }
        if (!Objects.equals(board.get().getMember().getMemberId(), member.getMemberId())) {
            return new ResponseEntity<>("작성자가 일치 하지 않습니다", HttpStatus.BAD_REQUEST);
        }
        boardRepository.deleteById(boardId);

        List<Image> deletingImages = board.get().getImageList();

        for (Image deletingImage : deletingImages) {
            String imageUrl = deletingImage.getImageUrl();
            String target = "boardImage" + imageUrl.substring(imageUrl.lastIndexOf("/"));
            s3Uploader.deleteFile(target);
        }

        return new ResponseEntity<>("삭제가 완료 되었습니다", HttpStatus.OK);
    }
}