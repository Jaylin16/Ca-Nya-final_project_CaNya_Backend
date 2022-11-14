package com.example.canya.Image.Entity;

import com.example.canya.Board.Entity.Board;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
public class Image {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String imageUrl;

    @ManyToOne(fetch= FetchType.LAZY)
    @JoinColumn(name = "board_id")
    private Board board;

    public Image(Board board , String imageUrl){
        this.board = board;
        this.imageUrl = imageUrl;


    }


}
