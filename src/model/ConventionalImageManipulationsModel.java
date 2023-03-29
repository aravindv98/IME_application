package model;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;

import javax.imageio.ImageIO;

import utility.ImageUtil;

/**
 * This class extends the AbstractImageManipulationsModel class.
 * It performs different unique kinds of supported operations on a PNG, JPEG, BMP images.
 */
public class ConventionalImageManipulationsModel extends AbstractImageManipulationsModel {

  private static ConventionalImageManipulationsModel instance = null;

  protected ConventionalImageManipulationsModel() {
    super();
  }

  /**
   * Single-ton design pattern.
   *
   * @return the class object.
   */
  public static ConventionalImageManipulationsModel getInstance() {
    if (instance == null) {
      instance = new ConventionalImageManipulationsModel();
    }

    return instance;
  }

  @Override
  public boolean loadImage(String imagePath, String imageName, OutputStream out)
          throws IllegalArgumentException {

    InputStream in = ImageUtil.getInputData(imagePath);
    if (in == null) {
      throw new IllegalArgumentException("Invalid Input Stream!");
    }

    BufferedImage image = null;
    try {
      image = ImageIO.read(in);
    } catch (IOException e) {
      PrintStream putStream = new PrintStream(out);
      putStream.print("Error loading image: " + e.getMessage());
    }

    int width = image.getWidth();
    int height = image.getHeight();
    Pixels properties = new Pixels(width, height);
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
    imageNamePropertiesMap.put(imageName, properties);
    return true;
  }

  @Override
  public boolean saveImage(String imagePath, String imageName, OutputStream out)
          throws IllegalArgumentException {
    checkIfImagePresentInMap(imageName);

    // Create a new file at the specified path with the given file name
    Pixels obj = imageNamePropertiesMap.get(imageName);
    BufferedImage image = new BufferedImage(obj.width, obj.height, BufferedImage.TYPE_INT_RGB);

    for (int y = 0; y < obj.height; y++) {
      for (int x = 0; x < obj.width; x++) {
        String[] arr = obj.listOfPixels[x][y].split(" ");
        Color temp = new Color(Integer.parseInt(arr[0]),
                Integer.parseInt(arr[1]),
                Integer.parseInt(arr[2]));
        image.setRGB(x, y, temp.getRGB());
      }
    }

    try {
      String fileExtension = ImageUtil.getFileExtension(imagePath);
      byte[] bytes = ImageUtil.toByteArray(image, fileExtension);

      // Convert byte array to InputStream
      InputStream inputStream = new ByteArrayInputStream(bytes);
      ImageUtil.setInputData(imagePath, inputStream);
    } catch (IOException e) {
      PrintStream outStream = new PrintStream(out);
      outStream.print("Error saving image: " + e.getMessage());
      return false;
    }

    return true;
  }
}
