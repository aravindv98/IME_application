package view;

import java.io.File;

public interface Features {
    void loadImage();

    void load(File f);

    void saveImage(String[] sourceImages);

    void sepia(String[] sourceImages);

    void dither(String[] sourceImages);

    void blur(String[] sourceImages);

    void sharpen(String[] sourceImages);

    void horizontalFlip(String[] sourceImages);

    void verticalFlip(String[] sourceImages);

    void rgbSplit(String[] sourceImages);

    void rgbCombine(String[] sourceImages);

    void brightenImage(int brightnessFactor, String[] sourceImages);

    void greyscaleImage(String componentType, String[] sourceImages);

    void createGreyscale(String[] sourceImages);
}
