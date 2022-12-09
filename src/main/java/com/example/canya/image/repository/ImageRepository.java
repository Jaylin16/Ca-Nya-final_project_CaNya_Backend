package com.example.canya.image.repository;

import com.example.canya.board.entity.Board;
import com.example.canya.image.entity.Image;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ImageRepository extends JpaRepository<Image, Long> {
    List<Image> findAllByBoard(Board board);
}