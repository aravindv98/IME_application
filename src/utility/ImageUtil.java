package utility;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Scanner;

import javax.imageio.ImageIO;

/**
 * A class created to handle I/O operations of read and write
 * of a file.
 */
public class ImageUtil {

  static HashMap<String, InputStream> inputMap = new HashMap<>();

  /**
   * A method to convert the buffered image to byte array in order for it
   * to be processed by load/save methods.
   *
   * @param image  containing the image content.
   * @param format format of the image.
   * @return the byte array of the file content.
   * @throws IOException exception thrown in case of any IO issue.
   */
  public static byte[] toByteArray(BufferedImage image, String format) throws IOException {
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    ImageIO.write(image, format, outputStream);
    return outputStream.toByteArray();
  }

  // Gets the full image path from the relative path.

  /**
   * A method to obtain the full path of a file.
   *
   * @param path path of the file.
   * @return the absolute path of the file.
   */
  public static String getFullImagePath(String path) {
    Path currentPath = Paths.get(path);
    if (!currentPath.isAbsolute()) {
      return currentPath.toAbsolutePath().toString();
    }

    return path;
  }

  /**
   * A method to return the file extension of a file.
   *
   * @param fileName name of the file.
   * @return the file extension of the file.
   */
  public static String getFileExtension(String fileName) {
    String[] tokens = fileName.split("\\.(?=[^\\.]+$)");
    if (tokens.length == 2) {
      return tokens[1];
    }

    return "";
  }

  /**
   * A method to read the file.
   *
   * @param out           containing the outputstream object.
   * @param imagePath     path of the image.
   * @param fileExtension extension of the file.
   * @return if the file has been successfully read or not.
   */
  public static boolean readFile(OutputStream out, String imagePath, String fileExtension) {
    InputStream inputStream = null;
    String path = "";
    if (fileExtension.equals("ppm")) {
      Scanner sc = null;
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

      inputStream = new ByteArrayInputStream(builder.toString().getBytes());
    } else {
      try {
        path = getFullImagePath(imagePath);
        File file = new File(path);
        BufferedImage image = ImageIO.read(file);
        byte[] bytes = toByteArray(image, fileExtension);

        // Convert byte array to InputStream
        inputStream = new ByteArrayInputStream(bytes);
      } catch (IOException e) {
        PrintStream outStream = new PrintStream(out);
        outStream.print("File " + path + " not found!");
        return false;
      }
    }

    inputMap.put(imagePath, inputStream);
    return true;
  }

  /**
   * A method to write a file.
   *
   * @param fileExtension extension of the file.
   * @param imagePath     path of the image.
   * @param out           object of outputstream.
   * @return if the file has been successfully written or not.
   */
  public static boolean writeFile(String fileExtension, String imagePath, OutputStream out) {
    String path = "";
    if (fileExtension.equals("ppm")) {
      path = getFullImagePath(imagePath);
      File file = new File(path);
      FileWriter writer;
      try {
        writer = new FileWriter(file);
        InputStream is = getInputData(imagePath);
        ByteArrayInputStream bais = (ByteArrayInputStream) is;
        byte[] buffer = new byte[1024];
        int bytesRead;
        StringBuilder stringBuilder = new StringBuilder();

        while ((bytesRead = bais.read(buffer)) != -1) {
          stringBuilder.append(new String(buffer, 0, bytesRead));
        }

        writer.write(stringBuilder.toString());
        writer.close();
      } catch (IOException e) {
        PrintStream outStream = new PrintStream(out);
        outStream.print("File " + path + " not found!");
        return false;
      }
    } else {
      File output = new File(getFullImagePath(imagePath));
      try {
        // Create a ByteArrayInputStream from the byte array
        InputStream is = getInputData(imagePath);
        ByteArrayInputStream bais = (ByteArrayInputStream) is;

        // Use ImageIO to read the image from the input stream into a BufferedImage
        BufferedImage image = ImageIO.read(bais);
        ImageIO.write(image, fileExtension, output);
      } catch (Exception e) {
        PrintStream outStream = new PrintStream(out);
        outStream.print("File " + path + " not found!");
        return false;
      }
    }

    return true;
  }

  /**
   * A method to store the file content in a map for it to be
   * accessible by the load method.
   *
   * @param imagePath path of the image.
   * @param os        object of inputstream.
   */
  public static void setInputData(String imagePath, InputStream os) {
    inputMap.put(imagePath, os);
  }

  /**
   * A method to get the file content by providing the path of the file
   * as argument.
   *
   * @param imagePath path of the image.
   * @return the inputstream object from the map.
   */
  public static InputStream getInputData(String imagePath) {
    return inputMap.get(imagePath);
  }

  public static String getFileName(String name) { return name.replaceFirst("[.][^.]+$", "");}
}
