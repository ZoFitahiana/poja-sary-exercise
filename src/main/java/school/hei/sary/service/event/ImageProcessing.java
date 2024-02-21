package school.hei.sary.service.event;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import school.hei.sary.PojaGenerated;
import school.hei.sary.file.BucketComponent;
import school.hei.sary.repository.ImageRepository;
import school.hei.sary.repository.model.Image;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

@PojaGenerated
@Service
@AllArgsConstructor
@Slf4j
public class ImageProcessing {
    @Autowired
    private final BucketComponent bucketComponent;
    @Autowired
    private ImageRepository imageRepository;

    public void imageProcessing(MultipartFile multipartFile, String bucketKey) {
        try {
            // convert MultipartFile to File
            File file = convertMultipartFileToFile(multipartFile);

            // load the original image from the file
            BufferedImage originalImage = ImageIO.read(file);

            // convert image to black and white
            BufferedImage newImage = ConvertImage.BlackCouleur(originalImage);

            // create a temporary file for the new image
            File newImageFile = createTempFile(newImage);

            // upload the original image file to S3
            bucketComponent.upload(file, bucketKey);

            // upload the new black and white image file to S3
            bucketComponent.upload(newImageFile, bucketKey + "_bw");

            // save id image in the database
            String id = bucketKey + "_bw";
            Image imageEntity = new Image();
            imageEntity.setId(id);
            imageRepository.save(imageEntity);

            // delete the temporary file
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
        // create a temporary file
        File tempFile = File.createTempFile("temp_image", ".png");

        // write the BufferedImage to the temporary file
        ImageIO.write(image, "png", tempFile);

        return tempFile;
    }
}
