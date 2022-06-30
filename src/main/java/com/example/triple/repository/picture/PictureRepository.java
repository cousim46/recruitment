package com.example.triple.repository.picture;

import com.example.triple.entity.picture.Picture;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PictureRepository extends JpaRepository<Picture, UUID> {

}
