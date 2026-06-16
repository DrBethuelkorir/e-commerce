package com.beviamy.dreamers.Repository;

import com.beviamy.dreamers.models.Image;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository<Image,Long> {
}
