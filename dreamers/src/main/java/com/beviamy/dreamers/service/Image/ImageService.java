package com.beviamy.dreamers.service.Image;

import com.beviamy.dreamers.Dto.ImageDto;
import com.beviamy.dreamers.Repository.ImageRepository;
import com.beviamy.dreamers.exeption.ResourceNotFoundException;
import com.beviamy.dreamers.models.Image;
import com.beviamy.dreamers.models.Product;
import com.beviamy.dreamers.service.products.ProductService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.sql.rowset.serial.SerialBlob;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ImageService implements IImageService {

    private final ImageRepository imageRepository;
    private final ProductService productService;

    @Override
    public List<ImageDto> addImage(List<MultipartFile> files, Long productId) {
        Product product = productService.getProductById(productId);
        List<ImageDto> imageDtos = new ArrayList<>();

        try {
            for (MultipartFile file : files) {
                Image image = new Image();
                image.setFiliName(file.getOriginalFilename());
                image.setFileType(file.getContentType());
                image.setImage(new SerialBlob(file.getBytes()));
                image.setProduct(product);

                // Save to get ID
                Image savedImage = imageRepository.save(image);

                // Set download URL with the ID
                String downloadUrl = "/api/v1/images/image/download/" + savedImage.getId();
                savedImage.setDownloadurl(downloadUrl);

                // Save again
                Image finalImage = imageRepository.save(savedImage);

                // Convert to DTO
                ImageDto dto = new ImageDto();
                dto.setImageId(finalImage.getId());
                dto.setImageName(finalImage.getFiliName());
                dto.setDownloadURL(finalImage.getDownloadurl());

                imageDtos.add(dto);
            }

            return imageDtos;

        } catch (IOException | SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public Image getImageById(Long id) {
        return this.imageRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Image not found"));
    }

    @Override
    public void deleteImageById(Long id) {
         this.imageRepository.findById(id).ifPresentOrElse(imageRepository :: delete,() ->{
            throw new ResourceNotFoundException("Image not found");
        });
    }

    @Override
    public void updateImage(MultipartFile file, Long ImageId) {
        Image image = getImageById(ImageId);
        try{
            image.setFiliName(file.getOriginalFilename());
            image.setFileType(file.getContentType());
            image.setDownloadurl(file.getOriginalFilename());
            image.setImage(new SerialBlob(file.getBytes()));
            imageRepository.save(image);
        }
        catch (IOException | SQLException e){
            throw new RuntimeException(e.getMessage());
        }
    }
}
