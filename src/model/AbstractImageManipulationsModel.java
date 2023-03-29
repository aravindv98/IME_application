package model;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;

/**
 * This class contains all the common functions supported for both PPM and conventional images.
 */
public abstract class AbstractImageManipulationsModel implements NewImageManipulationsModel {

  // Maintains a map of the image name, and it's associated properties
  // This helps in using the image name at any point later in our program
  // after loading it only once.
  protected static HashMap<String, Pixels> imageNamePropertiesMap = null;

  /**
   * Inner class used to store the properties of an image.
   * This acts like a structure and object of this inner class is used in the Hash Map.
   */
  protected class Pixels {
    public int width;
    public int height;
    public String[][] listOfPixels;

    Pixels(int width, int height) {
      this.width = width;
      this.height = height;
      this.listOfPixels = new String[width][height];
    }

    Pixels(Pixels another) {
      this(another.width, another.height);
    }
  }

  protected AbstractImageManipulationsModel() {
    if (imageNamePropertiesMap == null) {
      imageNamePropertiesMap = new HashMap<>();
    }
  }

  // Helper function used to transfer the pixels from an array to the original list of pixels.
  // This is used for vertical and horizontal flip of images.
  protected void copyPixelValues(String[] newArr, Pixels newObj) {
    int count = 0;
    for (int i = 0; i < newObj.height; i++) {
      for (int j = 0; j < newObj.width; j++) {
        newObj.listOfPixels[j][i] = newArr[count++];
      }
    }
  }

  // Check if the image that suppose we want to flip etc. is already loaded or not.
  // If it is not already loaded we throw an exception.
  protected void checkIfImagePresentInMap(String imageName) throws IllegalArgumentException {
    if (!imageNamePropertiesMap.containsKey(imageName)) {
      throw new IllegalArgumentException("Invalid Image name passed!");
    }
  }

  @Override
  abstract public boolean loadImage(String imagePath, String imageName, OutputStream out)
          throws IllegalArgumentException;

  @Override
  abstract public boolean saveImage(String imagePath, String imageName, OutputStream out);

  @Override
  public void createGreyScale(String imageName,
                              String destinationImageName) throws IllegalArgumentException {
    createGreyScale("luma-component", imageName, destinationImageName);
  }

  @Override
  public void createGreyScale(String componentType,
                              String imageName,
                              String destinationImageName) throws IllegalArgumentException {

    checkIfImagePresentInMap(imageName);

    Pixels obj = imageNamePropertiesMap.get(imageName);
    Pixels newObj = new Pixels(obj);

    for (int y = 0; y < obj.height; y++) {
      for (int x = 0; x < obj.width; x++) {
        String[] arr = obj.listOfPixels[x][y].split(" ");
        int grey;

        // Grey scaling of only the red component
        switch (componentType) {
          case "red-component":
            grey = (int) (0.299 * Integer.parseInt(arr[0])
                    + 0.587 * Integer.parseInt(arr[0])
                    + 0.114 * Integer.parseInt(arr[0]));
            break;

          // Grey scaling of only the green component
          case "green-component":
            grey = (int) (0.299 * Integer.parseInt(arr[1])
                    + 0.587 * Integer.parseInt(arr[1])
                    + 0.114 * Integer.parseInt(arr[1]));
            break;

          // Grey scaling of only the blue component
          case "blue-component":
            grey = (int) (0.299 * Integer.parseInt(arr[2])
                    + 0.587 * Integer.parseInt(arr[2])
                    + 0.114 * Integer.parseInt(arr[2]));
            break;

          // The maximum value of the three components for each pixel
          case "value-component":
            grey = Math.max(
                    Math.max(Integer.parseInt(arr[0]), Integer.parseInt(arr[1])),
                    Integer.parseInt(arr[2]));
            break;

          // The weighted sum
          case "luma-component":
            grey = (int) (0.2126 * Integer.parseInt(arr[0])
                    + 0.7152 * Integer.parseInt(arr[1])
                    + 0.0722 * Integer.parseInt(arr[2]));
            break;

          // Average of three components for each pixel
          case "intensity-component":
            grey = (Integer.parseInt(arr[0])
                    + Integer.parseInt(arr[1])
                    + Integer.parseInt(arr[2])) / 3;
            break;

          default:
            throw new IllegalArgumentException("Invalid Component Type for create greyscale image");
        }

        newObj.listOfPixels[x][y] = grey + " " + grey + " " + grey;
      }
    }

    imageNamePropertiesMap.put(destinationImageName, newObj);
  }

  @Override
  public void horizontalFlip(String imageName, String destinationImageName)
          throws IllegalArgumentException {

    checkIfImagePresentInMap(imageName);

    Pixels obj = imageNamePropertiesMap.get(imageName);
    Pixels newObj = new Pixels(obj);

    String[] newArr = new String[newObj.width * newObj.height];
    int count = 0;
    for (int y = 0; y < obj.height; y++) {
      for (int x = obj.width - 1; x >= 0; x--) {
        String[] arr = obj.listOfPixels[x][y].split(" ");
        newArr[count++] = (arr[0] + " " + arr[1] + " " + arr[2]);
      }
    }

    copyPixelValues(newArr, newObj);
    imageNamePropertiesMap.put(destinationImageName, newObj);
  }

  @Override
  public void verticalFlip(String imageName, String destinationImageName)
          throws IllegalArgumentException {

    checkIfImagePresentInMap(imageName);

    Pixels obj = imageNamePropertiesMap.get(imageName);
    Pixels newObj = new Pixels(obj);

    String[] newArr = new String[newObj.width * newObj.height];
    int count = 0;
    for (int y = obj.height - 1; y >= 0; y--) {
      for (int x = 0; x < obj.width; x++) {
        String[] arr = obj.listOfPixels[x][y].split(" ");
        newArr[count++] = (arr[0] + " " + arr[1] + " " + arr[2]);
      }
    }

    copyPixelValues(newArr, newObj);
    imageNamePropertiesMap.put(destinationImageName, newObj);
  }

  @Override
  public void brighten(int increment, String imageName, String destinationImageName)
          throws IllegalArgumentException {

    checkIfImagePresentInMap(imageName);

    Pixels obj = imageNamePropertiesMap.get(imageName);
    Pixels newObj = new Pixels(obj);

    for (int y = 0; y < obj.height; y++) {
      for (int x = 0; x < obj.width; x++) {
        String[] arr = obj.listOfPixels[x][y].split(" ");

        // If the pixel value is at max, we should not increment it.
        if (Integer.parseInt(arr[0]) > Integer.MAX_VALUE - increment) {
          continue;
        }
        int r = Math.min(Math.max(Integer.parseInt(arr[0]) + increment, 0), 255);

        if (Integer.parseInt(arr[1]) > Integer.MAX_VALUE - increment) {
          continue;
        }
        int g = Math.min(Math.max(Integer.parseInt(arr[1]) + increment, 0), 255);

        if (Integer.parseInt(arr[2]) > Integer.MAX_VALUE - increment) {
          continue;
        }
        int b = Math.min(Math.max(Integer.parseInt(arr[2]) + increment, 0), 255);

        newObj.listOfPixels[x][y] = r + " " + g + " " + b;
      }
    }

    imageNamePropertiesMap.put(destinationImageName, newObj);
  }

  @Override
  public void rgbSplit(String imageName,
                       String redDestinationImageName,
                       String greenDestinationImageName,
                       String blueDestinationImageName) {

    checkIfImagePresentInMap(imageName);

    Pixels obj = imageNamePropertiesMap.get(imageName);
    Pixels obj_1 = new Pixels(obj);
    Pixels obj_2 = new Pixels(obj);
    Pixels obj_3 = new Pixels(obj);
    for (int i = 0; i < obj.height; i++) {
      for (int j = 0; j < obj.width; j++) {
        String[] arr = obj.listOfPixels[j][i].split(" ");
        int grey = (int) (0.299 * Integer.parseInt(arr[0])
                + 0.587 * Integer.parseInt(arr[1])
                + 0.114 * Integer.parseInt(arr[2]));
        obj_1.listOfPixels[j][i] = grey + " " + 0 + " " + 0;
        obj_2.listOfPixels[j][i] = 0 + " " + grey + " " + 0;
        obj_3.listOfPixels[j][i] = 0 + " " + 0 + " " + grey;
      }
    }

    imageNamePropertiesMap.put(redDestinationImageName, obj_1);
    imageNamePropertiesMap.put(greenDestinationImageName, obj_2);
    imageNamePropertiesMap.put(blueDestinationImageName, obj_3);
  }

  @Override
  public void rgbCombine(String destinationImageName, String redSourceImageName,
                         String greenSourceImageName, String blueSourceImageName)
          throws IllegalArgumentException {

    checkIfImagePresentInMap(redSourceImageName);
    checkIfImagePresentInMap(greenSourceImageName);
    checkIfImagePresentInMap(blueSourceImageName);

    Pixels obj_1 = imageNamePropertiesMap.get(redSourceImageName);
    Pixels obj_2 = imageNamePropertiesMap.get(greenSourceImageName);
    Pixels obj_3 = imageNamePropertiesMap.get(blueSourceImageName);

    // If suppose source images are of different dimensions we should not combine them.
    if ((obj_1.width != obj_2.width || obj_2.width != obj_3.width)
            || (obj_1.height != obj_2.height || obj_2.height != obj_3.height)) {
      throw new IllegalArgumentException("Wrong values");
    }

    Pixels newObj = new Pixels(obj_1);
    for (int i = 0; i < obj_1.height; i++) {
      for (int j = 0; j < obj_2.width; j++) {
        // Read the pixel values from the input files
        String[] arr_1 = obj_1.listOfPixels[j][i].split(" ");
        String[] arr_2 = obj_2.listOfPixels[j][i].split(" ");
        String[] arr_3 = obj_3.listOfPixels[j][i].split(" ");

        newObj.listOfPixels[j][i] = arr_1[0] + " " + arr_2[1] + " " + arr_3[2];
      }
    }
    imageNamePropertiesMap.put(destinationImageName, newObj);
  }

  @Override
  public String getImageProperties(String imageName) {
    Pixels obj = imageNamePropertiesMap.get(imageName);
    int width = obj.width;
    int height = obj.height;
     String[][] listPixels = obj.listOfPixels;
    StringBuilder pixels = new StringBuilder();

    for (int i = 0; i < height; i++) {
      for (int j = 0; j < width; j++) {
        pixels.append(listPixels[j][i] + "\n");
      }
    }

    String imageProperties = width + " " + height + "\n"
            + 255 + "\n"
            + pixels;
    return imageProperties;
  }

  private void applyFilter(double[][] kernel, String imageName,
                           String destinationImageName) {
    checkIfImagePresentInMap(imageName);

    Pixels obj = imageNamePropertiesMap.get(imageName);
    Pixels newObj = new Pixels(obj);

    // Create a new 2D array for the blurred image
    int[][][] outputPixels = new int[obj.width][obj.height][3];

    // Apply the filter to each pixel of every channel
    for (int i = 1; i < obj.width - 1; i++) {
      for (int j = 1; j < obj.height - 1; j++) {
        for (int k = 0; k < 3; k++) { // loop over RGB channels
          double sum = 0.0;
          for (int u = -1; u <= 1; u++) {
            for (int v = -1; v <= 1; v++) {
              String arr[] = obj.listOfPixels[i+u][j+v].split(" ");
              sum += kernel[u+1][v+1] * Integer.parseInt(arr[k]);
            }
          }
          outputPixels[i][j][k] = (int) sum;
        }
      }
    }

    for (int i = 0; i < obj.height; i++) {
      for (int j = 0; j < obj.width; j++) {
        int red = outputPixels[j][i][0];
        int green = outputPixels[j][i][1];
        int blue = outputPixels[j][i][2];

        newObj.listOfPixels[j][i] = Math.min(Math.max(red, 0), 255) + " " +
                Math.min(Math.max(green, 0), 255) + " " +
                Math.min(Math.max(blue, 0), 255);
      }
    }

    imageNamePropertiesMap.put(destinationImageName, newObj);
  }

  @Override
  public void blur(String imageName, String destinationImageName) throws IllegalArgumentException {
    // Define the blur filter kernel
    double[][] kernel = {{0.0625, 0.125, 0.0625},
            {0.125, 0.25, 0.125},
            {0.0625, 0.125, 0.0625}};
    applyFilter(kernel, imageName, destinationImageName);
  }


  @Override
  public void sepia(String imageName, String destinationImageName)
          throws IllegalArgumentException {
    checkIfImagePresentInMap(imageName);

    Pixels obj = imageNamePropertiesMap.get(imageName);
    Pixels newObj = new Pixels(obj);

    // Define the blur filter
    double[][] filter = {{0.393, 0.769, 0.189}, {0.349, 0.686, 0.168}, {0.272, 0.534, 0.131}};

    for (int y = 0; y < obj.height; y++) {
      for (int x = 0; x < obj.width; x++) {
        String[] arr = obj.listOfPixels[x][y].split(" ");
        int[] sum = {0, 0, 0};
        for (int i = 0; i <= 2; i++) {
          sum[i] += Double.parseDouble(arr[0]) * filter[i][0];
          sum[i] += Double.parseDouble(arr[1]) * filter[i][1];
          sum[i] += Double.parseDouble(arr[2]) * filter[i][2];
        }
        newObj.listOfPixels[x][y] = Math.min(Math.max(sum[0], 0), 255) + " " +
                Math.min(Math.max(sum[1], 0), 255) + " " +
                Math.min(Math.max(sum[2], 0), 255);
      }
    }

    imageNamePropertiesMap.put(destinationImageName, newObj);
  }

  @Override
  public void dither(String imageName, String destinationImageName)
          throws IllegalArgumentException {

    checkIfImagePresentInMap(imageName);
      this.createGreyScale("luma-component",imageName,
              "tempLumaForDither");
      checkIfImagePresentInMap("tempLumaForDither");
    Pixels luma = imageNamePropertiesMap.get("tempLumaForDither");
    Pixels dither = new Pixels(luma);
    for (int y = 0; y < luma.height; y++) {
        for (int x = 0; x < luma.width; x++) {
          String[] arr = luma.listOfPixels[x][y].split(" ");
          int oldRed = Integer.parseInt(arr[0]);
          int newRed = oldRed < 128 ? 0 : 255;
          dither.listOfPixels[x][y] = newRed + " " + newRed + " " + newRed;
          int error = oldRed - newRed;
          if(x < luma.width -1){
            String[] neighborRow = luma.listOfPixels[x+1][y].split(" ");
            int neighborRed = Integer.parseInt(neighborRow[0]);
            int calculatedRed = neighborRed + error * 7/16;
            luma.listOfPixels[x+1][y] = calculatedRed + " " + calculatedRed +
                    " " + calculatedRed;
          }
          if(x > 0 && y < luma.height - 1) {
            String[] neighborRow = luma.listOfPixels[x-1][y+1].split(" ");
            int neighborRed = Integer.parseInt(neighborRow[0]);
            int calculatedRed = neighborRed + error * 3/16;
            luma.listOfPixels[x-1][y+1] = calculatedRed + " " + calculatedRed +
                    " " + calculatedRed;
          }
          if(y < luma.height - 1){
            String[] neighborRow = luma.listOfPixels[x][y+1].split(" ");
            int neighborRed = Integer.parseInt(neighborRow[0]);
            int calculatedRed = neighborRed + error * 5/16;
            luma.listOfPixels[x][y+1] = calculatedRed + " " + calculatedRed +
                    " " + calculatedRed;
          }
          if(x < luma.width - 1 && y < luma.height - 1){
            String[] neighborRow = luma.listOfPixels[x+1][y+1].split(" ");
            int neighborRed = Integer.parseInt(neighborRow[0]);
            int calculatedRed = neighborRed + error * 1/16;
            luma.listOfPixels[x+1][y+1] = calculatedRed + " " + calculatedRed +
                    " " + calculatedRed;
          }
      }
    }
    imageNamePropertiesMap.put(destinationImageName, dither);
  }

  @Override
  public void sharpen(String imageName, String destinationImageName)
          throws IllegalArgumentException {
    double[][] kernel = {{-0.125, -0.125, -0.125},
            {-0.125,  2.0,   -0.125},
            {-0.125, -0.125, -0.125}};

    applyFilter(kernel, imageName, destinationImageName);
  }
}
