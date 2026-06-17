package com.beviamy.dreamers.Controllers;

import com.beviamy.dreamers.APIResonse;
import com.beviamy.dreamers.Dto.ImageDto;
import com.beviamy.dreamers.exeption.ResourceNotFoundException;
import com.beviamy.dreamers.models.Image;
import com.beviamy.dreamers.service.Image.IImageService;
import org.springframework.core.io.Resource;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.sql.SQLException;
import java.util.List;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@RequiredArgsConstructor
@RestController
@RequestMapping("${api.prefix}/images")
public class ImageContoller {

    private final IImageService imageService;

        @PostMapping("/upload")
        public ResponseEntity<APIResonse> saveImages (
                @RequestParam("files")List <MultipartFile> files,
                  @RequestParam("productId") Long productID
        ) {
            try {
                List<ImageDto> imageDtos = imageService.addImage(files, productID);
                return ResponseEntity.ok(new APIResonse("uploaded succefully",imageDtos));
            }
            catch (Exception e) {
                return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new APIResonse("error",e.getMessage()));
            }
        }
    @GetMapping("/image/download/{imageId}")
    public ResponseEntity<Resource> downloadImage(@PathVariable Long imageId) throws SQLException {
        Image image = imageService.getImageById(imageId);

        ByteArrayResource resource = new ByteArrayResource(image.getImage().getBytes( 1, (int) image.getImage().length()));
        return ResponseEntity.ok().contentType(MediaType.parseMediaType(image.getFileType()))
                .header(HttpHeaders.CONTENT_DISPOSITION,  "attachment; filename=\"" +
                image.getFiliName() + "\"")
                .body(resource);
    }
    @PutMapping("image/{imageid}/update")
    public ResponseEntity<APIResonse> updateImage(@PathVariable Long imageid, @RequestBody MultipartFile image) {
            try {
            Image image1 = imageService.getImageById(imageid);
            if(image1 != null){
               imageService.updateImage(image, imageid);
               return ResponseEntity.ok(new APIResonse("updated",image1));
                }
            }
            catch (ResourceNotFoundException e) {
                return ResponseEntity.status(NOT_FOUND).body(new APIResonse("not found",e.getMessage()));
            }
            return ResponseEntity.status(INTERNAL_SERVER_ERROR)
                    .body(new APIResonse("update failed",INTERNAL_SERVER_ERROR));
        }
    @DeleteMapping("image/{imageid}/delete")
    public ResponseEntity<APIResonse> deleteImage(@PathVariable Long imageid) {
        try {
            Image image1 = imageService.getImageById(imageid);
            if(image1 != null){
                imageService.deleteImageById(imageid);
                return ResponseEntity.ok(new APIResonse("Deleted",image1));
            }
        }
        catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new APIResonse("not found",e.getMessage()));
        }
        return ResponseEntity.status(INTERNAL_SERVER_ERROR)
                .body(new APIResonse("Canot delete",INTERNAL_SERVER_ERROR));
    }
        }

