package school.hei.sary.service.event;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import school.hei.sary.PojaGenerated;
import school.hei.sary.file.BucketComponent;
import school.hei.sary.repository.ImageRepository;
import school.hei.sary.repository.model.Image;

@PojaGenerated
@Service
@AllArgsConstructor
@Slf4j
public class ImageService {
  @Autowired private ImageRepository imageRepository;
  @Autowired private final BucketComponent bucketComponent;

  public Optional<Image> findById(String id) {
    return imageRepository.findById(id);
  }

  public byte[] getBlackAndWhiteImageBytes(String id) {
    try {
      Path blackAndWhiteImagePath = bucketComponent.download(id + "_bw").toPath();
      return Files.readAllBytes(blackAndWhiteImagePath);
    } catch (IOException e) {
      log.error("Error fetching black and white image bytes", e);
      return null;
    }
  }

  public byte[] getOriginalImageBytes(String id) {
    try {
      Path originalImagePath = bucketComponent.download(id).toPath();
      return Files.readAllBytes(originalImagePath);
    } catch (IOException e) {
      log.error("Error fetching original image bytes", e);
      return null;
    }
  }
}
