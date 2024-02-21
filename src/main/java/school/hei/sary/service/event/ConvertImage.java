package school.hei.sary.service.event;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import school.hei.sary.PojaGenerated;

import java.awt.*;
import java.awt.image.BufferedImage;
@PojaGenerated
@Service
@AllArgsConstructor
@Slf4j

public class ConvertImage {
    public static BufferedImage BlackCouleur(BufferedImage img ) {
        BufferedImage image=img;
        int w = image.getWidth();
        int h = image.getHeight();
        int  colB=new Color(255,255,255).getRGB();
        int  colN=new Color(1,1,1).getRGB();
        int  colMoyen=(colB + colN)/2;
        for (int x=0; x<w; x++) {
            for (int y=0;y<h; y++) {
                int k =image.getRGB(x, y);

                if (k <=colMoyen )
                    image.setRGB(x,y ,colN);
                if (k >colMoyen)
                    image.setRGB(x,y,colB);

            }
        }
        return  image;
    }
}
