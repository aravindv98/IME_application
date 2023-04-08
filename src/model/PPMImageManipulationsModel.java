package model;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Scanner;

import utility.ImageUtil;
import utility.Pixels;

/**
 * This class extends the AbstractImageManipulationsModel class.
 * It performs different unique kinds of supported operations on a PPM image.
 */
public class PPMImageManipulationsModel extends AbstractImageManipulationsModel {

  private static PPMImageManipulationsModel instance = null;

  /**
   * Constructor of ImageManipulationsModel.
   * It only role right now is to initialize the hash-map.
   */
  protected PPMImageManipulationsModel() {
    super();
  }

  /**
   * Single-ton design pattern.
   *
   * @return the class object.
   */
  public static PPMImageManipulationsModel getInstance() {
    if (instance == null) {
      instance = new PPMImageManipulationsModel();
    }

    return instance;
  }

  @Override
  public boolean loadImage(String imagePath, String imageName, OutputStream out)
          throws IllegalArgumentException {

    // Check if an image is already added into the map then remove it,
    // because if the controller is passing the same name the intention is to override the image.
    if (!imageNamePropertiesMap.isEmpty()) {
      imageNamePropertiesMap.remove(imageName);
    }

    InputStream in = ImageUtil.getInputData(imagePath);
    if (in == null) {
      throw new IllegalArgumentException("Invalid Input Stream!");
    }

    Scanner sc = new Scanner(in);
    String token = sc.next();
    if (!token.equals("P3")) {
      throw new IllegalArgumentException("Invalid PPM file: plain RAW file should begin with P3");
    }
    int width = sc.nextInt();
    int height = sc.nextInt();
    int maxValue = sc.nextInt();
    Pixels properties = new Pixels(width, height);

    for (int i = 0; i < height; i++) {
      for (int j = 0; j < width; j++) {
        int r = sc.nextInt();
        int g = sc.nextInt();
        int b = sc.nextInt();
        properties.listOfPixels[j][i] = r + " " + g + " " + b;
      }
    }

    imageNamePropertiesMap.put(imageName, properties);
    return true;
  }

  @Override
  public boolean saveImage(String imagePath, String imageName, OutputStream out)
          throws IllegalArgumentException {

    checkIfImagePresentInMap(imageName);

    Pixels obj = imageNamePropertiesMap.get(imageName);
    StringBuilder builder = new StringBuilder();
    builder.append("P3\n");
    builder.append(obj.width).append(" ").append(obj.height).append("\n");
    builder.append(255 + "\n");

    for (int y = 0; y < obj.height; y++) {
      for (int x = 0; x < obj.width; x++) {
        String[] arr = obj.listOfPixels[x][y].split(" ");
        builder.append(arr[0]).append(" ");
        builder.append(arr[1]).append(" ");
        builder.append(arr[2]).append(" ");
      }
      builder.append("\n");
    }

    InputStream inputStream = new ByteArrayInputStream(builder.toString().getBytes());
    ImageUtil.setInputData(imagePath, inputStream);

    return true;
  }
}