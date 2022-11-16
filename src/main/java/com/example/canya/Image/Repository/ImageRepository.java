package com.example.canya.Image.Repository;

import com.example.canya.Board.Entity.Board;
import com.example.canya.Image.Entity.Image;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ImageRepository extends JpaRepository<Image, Long> {
    List<Image> findAllByBoard(Board board);
}
