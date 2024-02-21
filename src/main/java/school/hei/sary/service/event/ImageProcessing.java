package school.hei.sary.service.event;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import school.hei.sary.PojaGenerated;
import school.hei.sary.file.BucketComponent;
import school.hei.sary.repository.ImageRepository;
import school.hei.sary.repository.model.Image;

@PojaGenerated
@Service
@AllArgsConstructor
@Slf4j
public class ImageProcessing {
  @Autowired private final BucketComponent bucketComponent;
  @Autowired private ImageRepository imageRepository;

  public void imageProcessing(MultipartFile multipartFile, String bucketKey) {
    try {
      File file = convertMultipartFileToFile(multipartFile);
      BufferedImage originalImage = ImageIO.read(file);
      BufferedImage newImage = ConvertImage.BlackCouleur(originalImage);
      File newImageFile = createTempFile(newImage);
      bucketComponent.upload(file, bucketKey);
      bucketComponent.upload(newImageFile, bucketKey + "_bw");
      String id = bucketKey + "_bw";
      Image imageEntity = new Image();
      imageEntity.setId(id);
      imageRepository.save(imageEntity);
      newImageFile.delete();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private File convertMultipartFileToFile(MultipartFile multipartFile) throws IOException {
    File file = new File(multipartFile.getOriginalFilename());
    multipartFile.transferTo(file);
    return file;
  }

  private File createTempFile(BufferedImage image) throws IOException {
    File tempFile = File.createTempFile("temp_image", ".png");
    ImageIO.write(image, "png", tempFile);
    return tempFile;
  }
}
