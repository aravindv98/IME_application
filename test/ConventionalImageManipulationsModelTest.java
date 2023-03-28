import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.OutputStream;
import java.nio.file.Path;
import java.nio.file.Paths;

import model.ConventionalImageManipulationsModel;
import model.IImageManipulationsModelFactory;
import model.ImageManipulationsModelFactory;
import model.NewImageManipulationsModel;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

/**
 * This class tests the main working ConventionalImageManipulation Model.
 */
public class ConventionalImageManipulationsModelTest {

  NewImageManipulationsModel obj;
  OutputStream out;

  ImageManipulationsModelFactory factory;

  @Before
  public void setup() {
    out = new ByteArrayOutputStream();
    obj = ConventionalImageManipulationsModel.getInstance();
    obj.loadImage(getImagePath("/test/testData/manhattan-small.png"),
            "manhattan-small", out);
    factory = new ImageManipulationsModelFactory();
  }

  @After
  public void cleanUp() {
    Path currentRelativePath = Paths.get("");
    String filePath = currentRelativePath.toAbsolutePath()
            + "/test/testData/manhattan-blur.png";
    File f = new File(filePath);
    f.delete();
  }

  private String getImagePath(String relPath) {
    Path currentRelativePath = Paths.get("");
    return currentRelativePath.toAbsolutePath() + relPath;
  }

  @Test
  public void loadImage() {
    assertNotEquals("", obj.getImageProperties("manhattan-small"));
  }

  @Test
  public void saveImage() {
    Path currentRelativePath = Paths.get("");
    StringBuilder s = new StringBuilder(currentRelativePath.toAbsolutePath().toString());
    obj.blur("manhattan-small", "manhattan-blur");
    String str = s.append("/test/testData/manhattan-blur.png").toString();
    obj.saveImage(str, "manhattan-blur", out);
    File f = new File(str);
    assertTrue(f.exists() && !f.isDirectory());
  }

  @Test
  public void sepia() {
    obj.loadImage(getImagePath("/test/testData/manhattan-small-sepia-expected.png"),
            "manhattan-small-sepia-expected", out);
    String expected = obj.getImageProperties("manhattan-small-sepia-expected");
    obj.sepia("manhattan-small", "manhattan-small-sepia-result");
    String result = obj.getImageProperties("manhattan-small-sepia-result");
    assertEquals(expected, result);
  }

  @Test
  public void dither() {
    obj.loadImage(getImagePath("/test/testData/manhattan-small-dither-expected.png"),
            "manhattan-small-dither-expected", out);
    String expected = obj.getImageProperties("manhattan-small-dither-expected");
    obj.dither("manhattan-small", "manhattan-small-dither-result");
    String result = obj.getImageProperties("manhattan-small-dither-result");
    assertEquals(expected, result);
  }

  @Test
  public void blur() {
    obj.loadImage(getImagePath("/test/testData/manhattan-small-blur-expected.png"),
            "manhattan-small-blur-expected", out);
    String expected = obj.getImageProperties("manhattan-small-blur-expected");
    obj.blur("manhattan-small", "manhattan-small-blur-result");
    String result = obj.getImageProperties("manhattan-small-blur-result");
    assertEquals(expected, result);
  }

  @Test
  public void sharpen() {
    obj.loadImage(getImagePath("/test/testData/manhattan-small-sharpen-expected.png"),
            "manhattan-small-sharpen-expected", out);
    String expected = obj.getImageProperties("manhattan-small-sharpen-expected");
    obj.sharpen("manhattan-small", "manhattan-small-sharpen-result");
    String result = obj.getImageProperties("manhattan-small-sharpen-result");
    assertEquals(expected, result);
  }

  @Test
  public void greyscale() {
    obj.loadImage(getImagePath("/test/testData/manhattan-small-greyscale-expected.png"),
            "manhattan-small-greyscale-expected", out);
    String expected = obj.getImageProperties("manhattan-small-greyscale-expected");
    obj.createGreyScale("manhattan-small",
            "manhattan-small-greyscale-result");
    String result = obj.getImageProperties("manhattan-small-greyscale-result");
    assertEquals(expected, result);
  }

  @Test
  public void pngToBmp() {
    obj.loadImage(getImagePath("/res/png_to_bmp.bmp"),
            "png_to_bmp_expected", out);
    String expected = obj.getImageProperties("png_to_bmp_expected");
    obj.loadImage(getImagePath("/test/testData/manhattan-small.png"),
            "manhattan-png", out);
    Path currentRelativePath = Paths.get("");
    StringBuilder s = new StringBuilder(currentRelativePath.toAbsolutePath().toString());
    String str = s.append("/res/png_to_bmp_testcase.bmp").toString();
    obj.saveImage(str,"manhattan-png",out);
    obj.loadImage(getImagePath("/res/png_to_bmp_testcase.bmp"),"result",out);
    String result = obj.getImageProperties("result");
    assertEquals(expected, result);
  }
  @Test
  public void pngToJpg() {
    obj.loadImage(getImagePath("/res/png_to_jpg.jpg"),
            "png_to_jpg_expected", out);
    String expected = obj.getImageProperties("png_to_jpg_expected");
    obj.loadImage(getImagePath("/test/testData/manhattan-small.png"),
            "manhattan-png", out);
    Path currentRelativePath = Paths.get("");
    StringBuilder s = new StringBuilder(currentRelativePath.toAbsolutePath().toString());
    String str = s.append("/res/png_to_jpg_testcase.jpg").toString();
    obj.saveImage(str,"manhattan-png",out);
    obj.loadImage(getImagePath("/res/png_to_jpg_testcase.jpg"),"result",out);
    String result = obj.getImageProperties("result");
    assertEquals(expected, result);
  }
  @Test
  public void pngToPpm() {
    obj = factory.getModel("png_to_ppm.ppm");
    obj.loadImage(getImagePath("/res/png_to_ppm.ppm"),
            "png_to_ppm_expected", out);
    String expected = obj.getImageProperties("png_to_ppm_expected");
    obj = factory.getModel("manhattan-small.png");
    obj.loadImage(getImagePath("/test/testData/manhattan-small.png"),
            "manhattan-png", out);
    Path currentRelativePath = Paths.get("");
    StringBuilder s = new StringBuilder(currentRelativePath.toAbsolutePath().toString());
    String str = s.append("/res/png_to_ppm_testcase.ppm").toString();
    obj = factory.getModel("png_to_ppm_testcase.ppm");
    obj.saveImage(str,"manhattan-png",out);
    obj.loadImage(getImagePath("/res/png_to_ppm_testcase.ppm"),"result",out);
    String result = obj.getImageProperties("result");
    assertEquals(expected, result);
  }
  @Test
  public void ppmToJpg(){
    obj = factory.getModel("ppm_to_jpg.jpg");
    obj.loadImage(getImagePath("/res/ppm_to_jpg.jpg"),
            "ppm_to_jpg_expected", out);
    String expected = obj.getImageProperties("ppm_to_jpg_expected");
    obj = factory.getModel("duck.ppm");
    obj.loadImage(getImagePath("/src/images/duck.ppm"),
            "duck-ppm", out);
    Path currentRelativePath = Paths.get("");
    StringBuilder s = new StringBuilder(currentRelativePath.toAbsolutePath().toString());
    String str = s.append("/res/ppm_to_jpg_testcase.jpg").toString();
    obj = factory.getModel("ppm_to_jpg_testcase.jpg");
    obj.saveImage(str,"duck-ppm",out);
    obj.loadImage(getImagePath("/res/ppm_to_jpg_testcase.jpg"),"result",out);
    String result = obj.getImageProperties("result");
    assertEquals(expected, result);
  }
  @Test
  public void jpgToBmp(){
    obj.loadImage(getImagePath("/res/jpg_to_bmp.bmp"),
            "jpg_to_bmp_expected", out);
    String expected = obj.getImageProperties("jpg_to_bmp_expected");
    obj.loadImage(getImagePath("/src/images/sample_jpg.jpeg"),
            "sample-jpeg", out);
    Path currentRelativePath = Paths.get("");
    StringBuilder s = new StringBuilder(currentRelativePath.toAbsolutePath().toString());
    String str = s.append("/res/jpg_to_bmp_testcase.bmp").toString();
    obj.saveImage(str,"sample-jpeg",out);
    obj.loadImage(getImagePath("/res/jpg_to_bmp_testcase.bmp"),"result",out);
    String result = obj.getImageProperties("result");
    assertEquals(expected, result);
  }
  @Test
  public void bmpTojpg(){
    obj.loadImage(getImagePath("/res/bmp_to_jpg.jpg"),
            "bmp_to_jpg_expected", out);
    String expected = obj.getImageProperties("bmp_to_jpg_expected");
    obj.loadImage(getImagePath("/src/images/blackbuck.bmp"),
            "sample-bmp", out);
    Path currentRelativePath = Paths.get("");
    StringBuilder s = new StringBuilder(currentRelativePath.toAbsolutePath().toString());
    String str = s.append("/res/bmp_to_jpg_testcase.jpg").toString();
    obj.saveImage(str,"sample-bmp",out);
    obj.loadImage(getImagePath("/res/bmp_to_jpg_testcase.jpg"),"result",out);
    String result = obj.getImageProperties("result");
    assertEquals(expected, result);
  }
}