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
import java.util.Arrays;
import java.util.HashMap;
import java.util.Scanner;
import javax.imageio.ImageIO;


public class ImageUtil {

  static HashMap<String, InputStream> inputMap = new HashMap<>();

  public static byte[] toByteArray(BufferedImage image, String format) throws IOException {
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    ImageIO.write(image, format, outputStream);
    return outputStream.toByteArray();
  }

  // Gets the full image path from the relative path.
  public static String getFullImagePath(String path) {
    Path currentPath = Paths.get(path);
    if (!currentPath.isAbsolute()) {
      return currentPath.toAbsolutePath().toString();
    }

    return path;
  }

  public static String getFileExtension(String fileName) {
    String[] tokens = fileName.split("\\.(?=[^\\.]+$)");
    return tokens[1];
  }

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
    }
    else {
      try {
        path = getFullImagePath(imagePath);
        File file = new File(path);
        BufferedImage image = ImageIO.read(file);
        byte[] bytes = toByteArray(image, fileExtension);

        // Convert byte array to InputStream
        inputStream = new ByteArrayInputStream(bytes);
      }
      catch (IOException e) {
        PrintStream outStream = new PrintStream(out);
        outStream.print("File " + path + " not found!");
        return false;
      }
    }

    inputMap.put(imagePath, inputStream);
    return true;
  }

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
    }
    else {
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

  public static void setInputData(String imagePath, InputStream os) {
    inputMap.put(imagePath, os);
  }

  public static InputStream getInputData(String imagePath) {
    return inputMap.get(imagePath);
  }
}
