package model;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

import javax.imageio.ImageIO;

public class ConventionalImageManipulationsModel extends AbstractImageManipulationsModel {

  private static ConventionalImageManipulationsModel instance = null;

  protected ConventionalImageManipulationsModel() {
    super();
  }

  public static ConventionalImageManipulationsModel getInstance() {
    if (instance == null) {
      instance = new ConventionalImageManipulationsModel();
    }

    return instance;
  }

  @Override
  public boolean loadImage(String imagePath, String imageName, OutputStream out)
          throws IllegalArgumentException {
    Pixels properties;
    try {
      String path = getFullImagePath(imagePath);
      File file = new File(path);
      BufferedImage image = ImageIO.read(file);

      int width = image.getWidth();
      int height = image.getHeight();
      properties = new Pixels(width, height);
      // Get the pixel values
      for (int x = 0; x < width; x++) {
        for (int y = 0; y < height; y++) {
          int rgb = image.getRGB(x, y);
          Color color = new Color(rgb);
          int r = color.getRed();
          int g = color.getGreen();
          int b = color.getBlue();
          properties.listOfPixels[x][y] = r + " " + g + " " + b;
        }
      }
    } catch (IOException e) {
      System.out.println("Error loading image: " + e.getMessage());
      return false;
    }

    imageNamePropertiesMap.put(imageName, properties);
    return true;
  }

  @Override
  public boolean saveImage(String imagePath, String imageName, OutputStream out)
          throws IllegalArgumentException {
    checkIfImagePresentInMap(imageName);

    // Create a new file at the specified path with the given file name
    Pixels obj = imageNamePropertiesMap.get(imageName);
    String[] tokens = imagePath.split("\\.(?=[^\\.]+$)");
    String extension = tokens[1];
    BufferedImage image = null;
    if (extension.equals("png")) {
      image = new BufferedImage(obj.width, obj.height, BufferedImage.TYPE_INT_ARGB);
    }
    else if (extension.equals("jpg") || extension.equals("jpeg") ||
    extension.equals("bmp")) {
      image = new BufferedImage(obj.width, obj.height, BufferedImage.TYPE_INT_RGB);
    }

    for (int y = 0; y < obj.height; y++) {
      for (int x = 0; x < obj.width; x++) {
        String[] arr = obj.listOfPixels[x][y].split(" ");
        Color temp = new Color(Integer.parseInt(arr[0]),
                Integer.parseInt(arr[1]),
                Integer.parseInt(arr[2]));
        image.setRGB(x, y, temp.getRGB());
      }
    }

    File output = new File(getFullImagePath(imagePath));
    try {
      ImageIO.write(image, extension, output);
    } catch (Exception e) {
      System.out.println("Error saving image: " + e.getMessage());
      return false;
     }

    return true;
  }


}
