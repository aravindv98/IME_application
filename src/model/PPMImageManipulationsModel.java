package model;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Scanner;

/**
 * This class implements the ImageManipulationsModel interface.
 * It performs different kinds of supported operations on a PPM image.
 */
public class PPMImageManipulationsModel extends AbstractImageManipulationsModel {

  private static PPMImageManipulationsModel instance = null;

  /**
   * Constructor of ImageManipulationsModel.
   * It only role right now is to intialize the hash-map.
   */
  protected PPMImageManipulationsModel() {
    super();
  }

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

    Scanner sc;
    String path = "";
    try {
      path = getFullImagePath(imagePath);
      sc = new Scanner(new FileInputStream(path));
    } catch (FileNotFoundException e) {
      PrintStream outStream = new PrintStream(out);
      outStream.print("File " + path + " not found!");
      return false;
    }
    StringBuilder builder = new StringBuilder();
    //read the file line by line, and populate a string. This will throw away any comment lines
    while (sc.hasNextLine()) {
      String s = sc.nextLine();
      if (s.charAt(0) != '#') {
        builder.append(s).append(System.lineSeparator());
      }
    }

    //now set up the scanner to read from the string we just built
    sc = new Scanner(builder.toString());

    String token;

    token = sc.next();
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

    // Create a new file at the specified path with the given file name
    File file = new File(getFullImagePath(imagePath));
    FileWriter writer;
    try {
      writer = new FileWriter(file);

      // Write the PPM header information to the file
      Pixels obj = imageNamePropertiesMap.get(imageName);
      writer.write("P3\n");
      writer.write(obj.width + " " + obj.height + "\n");
      writer.write(255 + "\n");

      // Write the color information for each pixel to the file
      for (int y = 0; y < obj.height; y++) {
        for (int x = 0; x < obj.width; x++) {
          String[] arr = obj.listOfPixels[x][y].split(" ");
          writer.write(arr[0] + " ");
          writer.write(arr[1] + " ");
          writer.write(arr[2] + " ");
        }
        writer.write("\n");
      }

      // Close the file writer
      writer.close();
    } catch (IOException e) {
      PrintStream outStream = new PrintStream(out);
      outStream.print("File " + imagePath + " not found!");
      return false;
    }

    return true;
  }


}