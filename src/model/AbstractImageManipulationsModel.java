package model;

import java.io.OutputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;

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

  // Gets the full image path from the relative path.
  protected String getFullImagePath(String path) {
    Path currentPath = Paths.get(path);
    if (!currentPath.isAbsolute()) {
      return currentPath.toAbsolutePath().toString();
    }

    return path;
  }

  @Override
  abstract public boolean loadImage(String imagePath, String imageName, OutputStream out);

  @Override
  abstract public boolean saveImage(String imagePath, String imageName, OutputStream out);

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

  private void applyFilter(int[][] filter, String imageName, String destinationImageName) {
    int size = filter[0].length;
    int sum = 0;
    for (int i=0; i<size; i++) {
      for (int j=0; j<size; j++) {
        sum += filter[i][j];
      }
    }

    checkIfImagePresentInMap(imageName);
    Pixels obj = imageNamePropertiesMap.get(imageName);
    Pixels newObj = new Pixels(obj);

    for (int y = 0; y < obj.height; y++) {
      for (int x = 0; x < obj.width; x++) {
        double sumRed = 0.0, sumGreen = 0.0, sumBlue = 0.0;
        String[] arr = obj.listOfPixels[x][y].split(" ");

        for (int i = 0; i < size; i++) {
          for (int j = 0; j < size; j++) {
            sumRed += Integer.parseInt(arr[0]) * filter[i][j];
            sumGreen += Integer.parseInt(arr[1]) * filter[i][j];
            sumBlue += Integer.parseInt(arr[2]) * filter[i][j];
          }
        }

        int newRed = (int)(sumRed / sum);
        int newGreen = (int)(sumGreen / sum);
        int newBlue = (int)(sumBlue / sum);
        newObj.listOfPixels[x][y] = newRed + " " + newGreen + " " + newBlue;
      }
    }

    imageNamePropertiesMap.put(destinationImageName, newObj);
  }

  @Override
  public void blur(String imageName, String destinationImageName) throws IllegalArgumentException {
    int[][] filter = {{1, 1, 1}, {1, 1, 1}, {1, 1, 1}};
    applyFilter(filter, imageName, destinationImageName);
  }


  @Override
  public void sepia(String imageName, String destinationImageName)
          throws IllegalArgumentException {
    checkIfImagePresentInMap(imageName);

    Pixels obj = imageNamePropertiesMap.get(imageName);
    Pixels newObj = new Pixels(obj);

    // Define the blur filter
    double[] filter = {1.014,1.989,0.488};

    for (int y = 0; y < obj.height; y++) {
      for (int x = 0; x < obj.width; x++) {
        int sumRed = 0, sumGreen = 0, sumBlue = 0;
        String[] arr = obj.listOfPixels[x][y].split(" ");

        sumRed += Double.parseDouble(arr[0]) * filter[0];
        sumGreen += Double.parseDouble(arr[1]) * filter[1];
        sumBlue += Double.parseDouble(arr[2]) * filter[2];

        newObj.listOfPixels[x][y] = Math.min(Math.max(sumRed, 0), 255) + " " +
                Math.min(Math.max(sumGreen, 0), 255) + " " +
                Math.min(Math.max(sumBlue, 0), 255);
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
    int[][] filter = {{-1, -1, -1, -1, -1}, {-1, 2, 2, 2, -1}, {-1, 2, 8, 2, -1},
            {-1, 2, 2, 2, -1}, {-1, -1, -1, -1, -1}};
    applyFilter(filter, imageName, destinationImageName);
  }
}
