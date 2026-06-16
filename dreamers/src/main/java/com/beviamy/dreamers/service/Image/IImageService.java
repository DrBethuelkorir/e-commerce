package com.beviamy.dreamers.service.Image;

import com.beviamy.dreamers.Dto.ImageDto;
import com.beviamy.dreamers.models.Image;
import com.beviamy.dreamers.models.Product;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface IImageService {
    List<ImageDto> addImage(List<MultipartFile> file, Long productId);
    Image  getImageById(Long id);
    void deleteImageById(Long id);
    void updateImage(MultipartFile file, Long productId);
}
