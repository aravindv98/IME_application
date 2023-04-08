package utility;

/**
 * Inner class used to store the properties of an image.
 * This acts like a structure and object of this inner class is used in the Hash Map.
 */
public class Pixels {
    public int width;
    public int height;
    public String[][] listOfPixels;

    public Pixels(int width, int height) {
        this.width = width;
        this.height = height;
        this.listOfPixels = new String[width][height];
    }

    public Pixels(Pixels another) {
        this(another.width, another.height);
    }
}