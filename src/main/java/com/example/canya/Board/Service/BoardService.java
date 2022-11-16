package com.example.canya.Board.Service;

import com.example.canya.Board.Dto.*;
import com.example.canya.Board.Entity.Board;
import com.example.canya.Board.Repository.BoardRepository;
import com.example.canya.Heart.Repository.HeartRepository;
import com.example.canya.Image.Entity.Image;
import com.example.canya.Image.Repository.ImageRepository;
import com.example.canya.Member.Entity.Member;
import com.example.canya.Member.Repository.MemberRepository;
import com.example.canya.Member.Service.MemberDetailsImpl;
import com.example.canya.Rating.Dto.RatingRequestDto;
import com.example.canya.Rating.Dto.RatingResponseDto;
import com.example.canya.Rating.Entity.Rating;
import com.example.canya.Rating.Repository.RatingRepository;
import com.example.canya.S3.S3Uploader;
import lombok.RequiredArgsConstructor;
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
    private final MemberRepository memberRepository;
    private final ImageRepository imageRepository;
    private final RatingRepository ratingRepository;
    private final HeartRepository heartRepository;

    private final S3Uploader s3Uploader;

    @Transactional
    public ResponseEntity<?> saveBoard(MemberDetailsImpl memberDetails) {
        Board board = boardRepository.save(new Board(memberDetails.getMember()));
        return new ResponseEntity<>(board.getBoardId(), HttpStatus.OK);
    }

    @Transactional
    public ResponseEntity<?> editBoard(BoardRequestDto dto, List<MultipartFile> images,
                                       Long boardId, Member member) throws IOException {

        Board board = boardRepository.findById(boardId).orElse(null);
        List<Image> imageList = imageRepository.findAllByBoard(board);
        Rating rating = ratingRepository.findRatingByBoardAndMemberId(board, member.getMemberId());

        assert board != null;
        if (!Objects.equals(board.getMember().getMemberId(), member.getMemberId())) {
            return new ResponseEntity<>("작성자가 다릅니다.", HttpStatus.BAD_REQUEST);
        }

        if (member.getMemberId().equals(board.getMember().getMemberId())) {
            board.update(dto);

            RatingRequestDto ratingDto = new RatingRequestDto(
                    dto.getRatings()[0], dto.getRatings()[1], dto.getRatings()[2],
                    dto.getRatings()[3], dto.getRatings()[4], dto.getRatings()[5]);

            rating.update(ratingDto);

            List<Map.Entry<String, Double>> twoHighestRatings = rating.getTwoHighestRatings(rating);
            board.update(dto, twoHighestRatings.get(0).getKey(), twoHighestRatings.get(1).getKey());

            imageRepository.deleteAll(imageList);
            for (MultipartFile image : images) {
                imageRepository.save(new Image(board, s3Uploader.upload(image, "boardImage"), board.getMember()));
            }
        }

        return new ResponseEntity<>("수정이 완료되었습니다.", HttpStatus.OK);
    }


    public ResponseEntity<?> cancelBoard(Long boardId, Member member) {
        Optional<Board> board = boardRepository.findById(boardId);
        if (board.isEmpty()) {
            return new ResponseEntity<>("게시글이 존재 하지 않습니다", HttpStatus.BAD_REQUEST);
        }
        if (board.get().getMember() != member) {
            return new ResponseEntity<>("작성자가 일치하지 않습니다", HttpStatus.BAD_REQUEST);
        }
        boardRepository.deleteById(boardId);
        return new ResponseEntity<>("작성이 취소 되었습니다.", HttpStatus.OK);
    }

    public ResponseEntity<?> getBoardDetail(Long boardId) {
        Board board = boardRepository.findById(boardId).orElse(null);
        if (board == null) {
            return new ResponseEntity<>("존재 하지 않는 게시물입니다", HttpStatus.BAD_REQUEST);
        }
        boolean isLiked = heartRepository.existsByBoardAndMember_MemberId(board, board.getMember().getMemberId());
        List<Image> imageList = board.getImageList();
        Rating rating = ratingRepository.findRatingByBoard(board);

        BoardResponseDto responseDto = BoardResponseDto.builder()
                .board(board)
                .imageList(imageList)
                .rating(rating)
                .isLiked(isLiked)
                .build();

        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    public ResponseEntity<?> getBoards() {

        List<Board> createdAtBoards = boardRepository.findAllByOrderByCreatedAtDesc();
        List<Board> bestBoards = boardRepository.findBoardsByOrderByTotalHeartCountDesc();
        List<Board> canyaCoffeeBoards = boardRepository.findBoardsByHighestRatingContainingOrderByTotalHeartCountDesc("coffee");
        List<Board> canyaMoodBoards = boardRepository.findBoardsByHighestRatingContainingOrderByTotalHeartCountDesc("mood");
        List<Board> canyaDessertBoards = boardRepository.findBoardsByHighestRatingContainingOrderByTotalHeartCountDesc("dessert");

        List<CanyaPickDto> canyaDto = new ArrayList<>();
        List<BestDto> bestDto = new ArrayList<>();
        List<CoffeePick> coffeePickList = new ArrayList<>();
        List<MoodPick> moodPickList = new ArrayList<>();
        List<DessertPick> dessertList = new ArrayList<>();
        List<NewDto> newDto = new ArrayList<>();
        List<AllDto> allDto = new ArrayList<>();

        for (Board bestBoard : bestBoards) {
            bestDto.add(new BestDto(bestBoard));
        }

        for (Board boards : canyaCoffeeBoards) {
            Rating ratingList = ratingRepository.findRatingByBoardAndMemberId(boards, boards.getMember().getMemberId());

            List<Map.Entry<String, Double>> ratings = ratingList.getTwoHighestRatings(ratingList);

            RatingResponseDto ratingDto = new RatingResponseDto(ratings.get(0), ratings.get(1), ratingList);

            coffeePickList.add(new CoffeePick(boards, ratingDto));
            canyaDto.add(new CanyaPickDto(coffeePickList, null, null));
        }
        for (Board boards : canyaMoodBoards) {
            Rating ratingList = ratingRepository.findRatingByBoardAndMemberId(boards, boards.getMember().getMemberId());

            List<Map.Entry<String, Double>> ratings = ratingList.getTwoHighestRatings(ratingList);

            RatingResponseDto ratingDto = new RatingResponseDto(ratings.get(0), ratings.get(1), ratingList);

            moodPickList.add(new MoodPick(boards, ratingDto));
            canyaDto.add(new CanyaPickDto(null, null, moodPickList));
        }
        for (Board boards : canyaDessertBoards) {
            Rating ratingList = ratingRepository.findRatingByBoardAndMemberId(boards, boards.getMember().getMemberId());

            List<Map.Entry<String, Double>> ratings = ratingList.getTwoHighestRatings(ratingList);

            RatingResponseDto ratingDto = new RatingResponseDto(ratings.get(0), ratings.get(1), ratingList);

            dessertList.add(new DessertPick(boards, ratingDto));
            canyaDto.add(new CanyaPickDto(null, dessertList, null));
        }

        for (Board boards : createdAtBoards) {

            Rating ratingList = ratingRepository.findRatingByBoardAndMemberId(boards, boards.getMember().getMemberId());

            List<Map.Entry<String, Double>> ratings = ratingList.getTwoHighestRatings(ratingList);

            RatingResponseDto ratingDto = new RatingResponseDto(ratings.get(0), ratings.get(1), ratingList);

            newDto.add(new NewDto(ratingDto, boards));
            allDto.add(new AllDto(boards, ratingDto));
        }
        MainPageDto mainPageDto = new MainPageDto(canyaDto, newDto, allDto, bestDto);

        return new ResponseEntity<>(mainPageDto, HttpStatus.OK);
    }


    @Transactional
    public ResponseEntity<?> confirmBoard(BoardRequestDto dto, List<MultipartFile> images,
                                          Long boardId) throws IOException {
        Board board = boardRepository.findById(boardId).orElse(null);

        if (images != null) {
            for (MultipartFile image : images) {
                assert board != null;
                imageRepository.save(new Image(board, s3Uploader.upload(image, "boardImage"), board.getMember()));
            }
        }

        RatingRequestDto ratingDto = new RatingRequestDto(
                dto.getRatings()[0], dto.getRatings()[1], dto.getRatings()[2],
                dto.getRatings()[3], dto.getRatings()[4], dto.getRatings()[5]);
        assert board != null;
        Rating rating = new Rating(ratingDto, board, board.getMember());
        System.out.println(rating);
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
        return new ResponseEntity<>("삭제가 완료 되었습니다", HttpStatus.OK);
    }
}
