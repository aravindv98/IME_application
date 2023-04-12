package utility;

/**
 * This class is used to store the properties of an image. This acts like a structure and object of
 * this class is used in the Hash Map of the model.
 */
public class Pixels {

  public int width;
  public int height;
  public String[][] listOfPixels;

  /**
   * Constructor.
   * @param width of image.
   * @param height of image.
   *
   */
  public Pixels(int width, int height) {
    this.width = width;
    this.height = height;
    this.listOfPixels = new String[width][height];
  }

  public Pixels(Pixels another) {
    this(another.width, another.height);
  }
}