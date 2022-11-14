package com.example.canya.Image.Repository;

import com.example.canya.Image.Entity.Image;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository<Image, Long> {
}
