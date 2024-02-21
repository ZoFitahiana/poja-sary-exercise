package school.hei.sary.endpoint.rest.controller.health;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import school.hei.sary.PojaGenerated;
import school.hei.sary.service.event.ImageProcessing;
import school.hei.sary.service.event.ImageService;

@PojaGenerated
@RestController
@AllArgsConstructor
@RequestMapping("/images")
public class ImageController {
    private final ImageProcessing imageProcessing;
    private final ImageService imageService;

    @PutMapping("/blacks/{id}")
    public ResponseEntity<String> processImage(@RequestParam("file") MultipartFile file, @PathVariable String id) {
        try {
            imageProcessing.imageProcessing(file, id);
            return ResponseEntity.ok("Image processed and saved successfully!");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Error processing the image");
        }
    }

    @GetMapping("/blacks/{id}")
    public ResponseEntity<byte[]> getBlackAndWhiteImage(@PathVariable String id) {
        byte[] imageBytes = imageService.getBlackAndWhiteImageBytes(id);

        if (imageBytes != null && imageBytes.length > 0) {
            return ResponseEntity.ok().contentType(org.springframework.http.MediaType.IMAGE_PNG).body(imageBytes);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/originals/{id}")
    public ResponseEntity<byte[]> getOriginalImage(@PathVariable String id) {
        byte[] imageBytes = imageService.getOriginalImageBytes(id);

        if (imageBytes != null && imageBytes.length > 0) {
            return ResponseEntity.ok().contentType(org.springframework.http.MediaType.IMAGE_PNG).body(imageBytes);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
