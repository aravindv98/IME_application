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
  abstract public void createGreyScale(String componentType,
                                       String imageName,
                                       String destinationImageName);

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
    double[][] filter = {{0.393, 0.769, 0.189}, {0.349, 0.686, 0.168}, {0.272, 0.534, 0.131}};

    for (int y = 0; y < obj.height; y++) {
      for (int x = 0; x < obj.width; x++) {
        int sumRed = 0, sumGreen = 0, sumBlue = 0;
        String[] arr = obj.listOfPixels[x][y].split(" ");

        for (int j = 0; j <= 2; j++) {
          for (int i = 0; i <= 2; i++) {
            if (j == 0) {
              sumRed += Double.parseDouble(arr[0]) * filter[i][j];
            }
            if (j == 1) {
              sumGreen += Double.parseDouble(arr[1]) * filter[i][j];
            } else {
              sumBlue += Double.parseDouble(arr[2]) * filter[i][j];
            }
          }
        }
        newObj.listOfPixels[x][y] = sumRed + " " + sumGreen + " " + sumBlue;
      }
    }
    imageNamePropertiesMap.put(destinationImageName, newObj);

  }

  @Override
  public void dither(String imageName, String destinationImageName)
          throws IllegalArgumentException {
    return;
  }

  @Override
  public void sharpen(String imageName, String destinationImageName)
          throws IllegalArgumentException {
    int[][] filter = {{-1, -1, -1, -1, -1}, {-1, 2, 2, 2, -1}, {-1, 2, 8, 2, -1},
            {-1, 2, 2, 2, -1}, {-1, -1, -1, -1, -1}};
    applyFilter(filter, imageName, destinationImageName);
  }
}
