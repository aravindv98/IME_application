import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import model.*;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.OutputStream;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * This class tests the main working PPMImageManipulation Model.
 */
public class PPMImageManipulationsModelTest {

  ImageManipulationsModel obj;
  OutputStream out;

  @Before
  public void setup() {
    out = new ByteArrayOutputStream();
    obj = PPMImageManipulationsModel.getInstance();
    obj.loadImage(getImagePath(), "Test_Image", out);
  }

  @After
  public void cleanUp() {
    Path currentRelativePath = Paths.get("");
    String filePath = currentRelativePath.toAbsolutePath()
            + "/test/testData/Test_Image-brighten.ppm";
    File f = new File(filePath);
    f.delete();
  }

  private String getImagePath() {
    Path currentRelativePath = Paths.get("");
    return currentRelativePath.toAbsolutePath() + "/test/testData/Test_Image.ppm";
  }

  /**
   * Test case to check if the test image is loaded as expected.
   */
  @Test
  public void TestLoadImage() {

    String expected = "2 2\n"
            + "255\n"
            + "255 0 0\n"
            + "0 255 0\n"
            + "0 0 255\n"
            + "255 255 255\n";

    String result = obj.getImageProperties("Test_Image");
    assertEquals(expected, result);
  }

  /**
   * Test case to check if the test image is saved as expected.
   */
  @Test
  public void TestSaveImage() {
    Path currentRelativePath = Paths.get("");
    StringBuilder s = new StringBuilder(currentRelativePath.toAbsolutePath().toString());
    obj.brighten(10, "Test_Image", "Test_Image-brighten");
    String str = s.append("/test/testData/Test_Image-brighten.ppm").toString();
    obj.saveImage(str, "Test_Image-brighten", out);
    File f = new File(str);
    assertTrue(f.exists() && !f.isDirectory());
  }

  /**
   * Test case to check if a file is being loaded from an invalid path or not.
   */
  @Test
  public void InvalidFilePathLoad() {
    Path currentRelativePath = Paths.get("");
    String filePath = currentRelativePath.toAbsolutePath()
            + "/test/testData/Test_Erro_Image.ppm";
    obj.loadImage(filePath, "Test_Image", out);
    assertEquals("File " + filePath + " not found!", out.toString());
  }

  /**
   * Test case to check if the test image is being saved for an invalid path.
   */
  @Test
  public void InvalidFilePathSave() {
    Path currentRelativePath = Paths.get("");
    StringBuilder s = new StringBuilder(currentRelativePath.toAbsolutePath().toString());
    obj.brighten(10, "Test_Image", "Test_Image-brighten");
    String str = s.append("/test/image/Test_Image-brighten.ppm").toString();
    obj.saveImage(str, "Test_Image-brighten", out);
    assertEquals("File " + str + " not found!", out.toString());
  }

  /**
   * Test case to check if an invalid file format is accepted.
   */
  @Test(expected = IllegalArgumentException.class)
  public void TestInvalidFileFormat() {
    Path currentRelativePath = Paths.get("");
    obj.loadImage(currentRelativePath.toAbsolutePath()
            + "/test/testData/Test_Error_Image.ppm", "Test_Image", out);
    obj.getImageProperties("Test_Image");
  }

  /**
   * Test case to check if the test image is brightened as expected.
   */
  @Test
  public void TestBrightenImage() {
    String expected = "2 2\n"
            + "255\n"
            + "255 10 10\n"
            + "10 255 10\n"
            + "10 10 255\n"
            + "255 255 255\n";

    obj.brighten(10, "Test_Image", "Test_Image-brighten");
    String result = obj.getImageProperties("Test_Image-brighten");
    assertEquals(expected, result);
  }

  /**
   * Test case to check if the red component of the greyscale image is
   * as expected.
   */
  @Test
  public void TestCreateGreyscaleRedImage() {
    String expected = "2 2\n"
            + "255\n"
            + "255 255 255\n"
            + "0 0 0\n"
            + "0 0 0\n"
            + "255 255 255\n";

    obj.createGreyScale("red-component", "Test_Image",
            "Test_Image-red");
    String result = obj.getImageProperties("Test_Image-red");
    assertEquals(expected, result);
  }

  /**
   * Test case to create a greyscale image.
   */
  @Test
  public void TestCreateGreyscaleValueImage() {
    String expected = "2 2\n"
            + "255\n"
            + "255 255 255\n"
            + "255 255 255\n"
            + "255 255 255\n"
            + "255 255 255\n";

    obj.createGreyScale("value-component", "Test_Image",
            "Test_Image-value");
    String result = obj.getImageProperties("Test_Image-value");
    assertEquals(expected, result);
  }

  /**
   * Test case to create greyscale intensity image.
   */
  @Test
  public void TestCreateGreyscaleIntensityImage() {
    String expected = "2 2\n"
            + "255\n"
            + "85 85 85\n"
            + "85 85 85\n"
            + "85 85 85\n"
            + "255 255 255\n";

    obj.createGreyScale("intensity-component", "Test_Image",
            "Test_Image-intensity");
    String result = obj.getImageProperties("Test_Image-intensity");
    assertEquals(expected, result);
  }

  /**
   * Test case to create greyscale luma image.
   */
  @Test
  public void TestCreateGreyscaleLumaImage() {
    String expected = "2 2\n"
            + "255\n"
            + "54 54 54\n"
            + "182 182 182\n"
            + "18 18 18\n"
            + "254 254 254\n";

    obj.createGreyScale("luma-component", "Test_Image",
            "Test_Image-luma");
    String result = obj.getImageProperties("Test_Image-luma");
    assertEquals(expected, result);
  }

  /**
   * Test case to create greyscale green image.
   */
  @Test
  public void TestCreateGreyscaleGreenImage() {
    String expected = "2 2\n"
            + "255\n"
            + "0 0 0\n"
            + "255 255 255\n"
            + "0 0 0\n"
            + "255 255 255\n";

    obj.createGreyScale("green-component", "Test_Image",
            "Test_Image-green");
    String result = obj.getImageProperties("Test_Image-green");
    assertEquals(expected, result);
  }

  /**
   * Test case to create greyscale blue image.
   */
  @Test
  public void TestCreateGreyscaleBlueImage() {
    String expected = "2 2\n"
            + "255\n"
            + "0 0 0\n"
            + "0 0 0\n"
            + "255 255 255\n"
            + "255 255 255\n";

    obj.createGreyScale("blue-component", "Test_Image",
            "Test_Image-blue");
    String result = obj.getImageProperties("Test_Image-blue");
    assertEquals(expected, result);
  }

  /**
   * Test case to check vertical flip of the ppm image.
   */
  @Test
  public void TestVerticalImage() {
    String expected = "2 2\n"
            + "255\n"
            + "0 0 255\n"
            + "255 255 255\n"
            + "255 0 0\n"
            + "0 255 0\n";

    obj.verticalFlip("Test_Image",
            "Test_Image-vertical");
    String result = obj.getImageProperties("Test_Image-vertical");
    assertEquals(expected, result);
  }

  /**
   * Test case to check horizontal flip of a ppm image.
   */
  @Test
  public void TestHorizontalImage() {
    String expected = "2 2\n"
            + "255\n"
            + "0 255 0\n"
            + "255 0 0\n"
            + "255 255 255\n"
            + "0 0 255\n";

    obj.horizontalFlip("Test_Image",
            "Test_Image-horizontal");
    String result = obj.getImageProperties("Test_Image-horizontal");
    assertEquals(expected, result);
  }

  /**
   * Test case to check wrong argument for horizontal flip of an image.
   */
  @Test(expected = IllegalArgumentException.class)
  public void TestImageNameNotPresent() {
    String expected = "2 2\n"
            + "255\n"
            + "0 255 0\n"
            + "255 0 0\n"
            + "255 255 255\n"
            + "0 0 255\n";

    obj.horizontalFlip("Test_Images",
            "Test_Image-horizontal");
  }

  /**
   * Test case to check vertical and horizontal flip one after the other.
   */
  @Test
  public void TestVerticalAndHorizontalImage() {
    String expected = "2 2\n"
            + "255\n"
            + "255 255 255\n"
            + "0 0 255\n"
            + "0 255 0\n"
            + "255 0 0\n";

    obj.verticalFlip("Test_Image",
            "Test_Image-vertical");
    obj.horizontalFlip("Test_Image-vertical",
            "Test_Image-vertical-horizontal");
    String result = obj.getImageProperties("Test_Image-vertical-horizontal");
    assertEquals(expected, result);
  }

  /**
   * Test case to test rgb split and combine.
   */
  @Test
  public void TestRGBSplitAndCombine() {
    String expected_1 = "2 2\n"
            + "255\n"
            + "76 0 0\n"
            + "149 0 0\n"
            + "29 0 0\n"
            + "255 0 0\n";

    String expected_2 = "2 2\n"
            + "255\n"
            + "0 76 0\n"
            + "0 149 0\n"
            + "0 29 0\n"
            + "0 255 0\n";

    String expected_3 = "2 2\n"
            + "255\n"
            + "0 0 76\n"
            + "0 0 149\n"
            + "0 0 29\n"
            + "0 0 255\n";

    String expected_4 = "2 2\n"
            + "255\n"
            + "76 76 76\n"
            + "149 149 149\n"
            + "29 29 29\n"
            + "255 255 255\n";

    obj.rgbSplit("Test_Image", "Test_Image-redDest",
            "Test_Image-greenDest",
            "Test_Image-blueDest");
    String result = obj.getImageProperties("Test_Image-redDest");
    assertEquals(expected_1, result);

    String result_2 = obj.getImageProperties("Test_Image-greenDest");
    assertEquals(expected_2, result_2);

    String result_3 = obj.getImageProperties("Test_Image-blueDest");
    assertEquals(expected_3, result_3);

    obj.rgbCombine("Test_Dest_Image", "Test_Image-redDest",
            "Test_Image-greenDest",
            "Test_Image-blueDest");
    String result_4 = obj.getImageProperties("Test_Dest_Image");
    assertEquals(expected_4, result_4);
  }
}