import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.OutputStream;
import java.nio.file.Path;
import java.nio.file.Paths;

import model.ConventionalImageManipulationsModel;
import model.ImageManipulationsModelFactory;
import model.NewImageManipulationsModel;
import utility.ImageUtil;

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
    ImageUtil.readFile(out, getImagePath("/test/testData/manhattan-small.png"),
            "png");
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
    ImageUtil.writeFile("png", str, out);
    File f = new File(str);
    assertTrue(f.exists() && !f.isDirectory());
  }

  @Test
  public void sepia() {
    ImageUtil.readFile(out, getImagePath("/test/testData/manhattan-small-sepia-expected.png"),
            "png");
    obj.loadImage(getImagePath("/test/testData/manhattan-small-sepia-expected.png"),
            "manhattan-small-sepia-expected", out);
    String expected = obj.getImageProperties("manhattan-small-sepia-expected");
    obj.sepia("manhattan-small", "manhattan-small-sepia-result");
    String result = obj.getImageProperties("manhattan-small-sepia-result");
    assertEquals(expected, result);
  }

  @Test
  public void dither() {
    ImageUtil.readFile(out, getImagePath("/test/testData/manhattan-small-dither-expected.png"),
            "png");
    obj.loadImage(getImagePath("/test/testData/manhattan-small-dither-expected.png"),
            "manhattan-small-dither-expected", out);
    String expected = obj.getImageProperties("manhattan-small-dither-expected");
    obj.dither("manhattan-small", "manhattan-small-dither-result");
    String result = obj.getImageProperties("manhattan-small-dither-result");
    assertEquals(expected, result);
  }

  @Test
  public void blur() {
    ImageUtil.readFile(out, getImagePath("/test/testData/manhattan-small-blur-expected.png"),
            "png");
    obj.loadImage(getImagePath("/test/testData/manhattan-small-blur-expected.png"),
            "manhattan-small-blur-expected", out);
    String expected = obj.getImageProperties("manhattan-small-blur-expected");
    obj.blur("manhattan-small", "manhattan-small-blur-result");
    String result = obj.getImageProperties("manhattan-small-blur-result");
    assertEquals(expected, result);
  }

  @Test
  public void sharpen() {
    ImageUtil.readFile(out, getImagePath("/test/testData/manhattan-small-sharpen-expected.png"),
            "png");
    obj.loadImage(getImagePath("/test/testData/manhattan-small-sharpen-expected.png"),
            "manhattan-small-sharpen-expected", out);
    String expected = obj.getImageProperties("manhattan-small-sharpen-expected");
    obj.sharpen("manhattan-small", "manhattan-small-sharpen-result");
    String result = obj.getImageProperties("manhattan-small-sharpen-result");
    assertEquals(expected, result);
  }

  @Test
  public void greyscale() {
    ImageUtil.readFile(out,
            getImagePath("/test/testData/manhattan-small-greyscale-expected.png"),
            "png");
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
    ImageUtil.readFile(out,
            getImagePath("/res/png_to_bmp.bmp"),
            "bmp");
    obj.loadImage(getImagePath("/res/png_to_bmp.bmp"),
            "png_to_bmp_expected", out);
    String expected = obj.getImageProperties("png_to_bmp_expected");
    ImageUtil.readFile(out,
            getImagePath("/test/testData/manhattan-small.png"),
            "png");
    obj.loadImage(getImagePath("/test/testData/manhattan-small.png"),
            "manhattan-png", out);
    Path currentRelativePath = Paths.get("");
    StringBuilder s = new StringBuilder(currentRelativePath.toAbsolutePath().toString());
    String str = s.append("/res/png_to_bmp_testcase.bmp").toString();
    obj.saveImage(str, "manhattan-png", out);
    ImageUtil.readFile(out,
            getImagePath("/res/png_to_bmp_testcase.bmp"),
            "bmp");
    obj.loadImage(getImagePath("/res/png_to_bmp_testcase.bmp"), "result", out);
    String result = obj.getImageProperties("result");
    assertEquals(expected, result);
  }

  // Commenting out these two tests as they require a big dataset,
  // this was causing an issue while submitting in handin.
  /*@Test
  public void pngToPpm() {
    ImageUtil.readFile(out,
            getImagePath("/res/png_to_ppm.ppm"),
            "ppm");
    obj = factory.getModel("ppm");
    obj.loadImage(getImagePath("/res/png_to_ppm.ppm"),
            "png_to_ppm_expected", out);
    String expected = obj.getImageProperties("png_to_ppm_expected");
    ImageUtil.readFile(out,
            getImagePath("/test/testData/manhattan-small.png"),
            "png");
    obj = factory.getModel("png");
    obj.loadImage(getImagePath("/test/testData/manhattan-small.png"),
            "manhattan-png", out);
    Path currentRelativePath = Paths.get("");
    StringBuilder s = new StringBuilder(currentRelativePath.toAbsolutePath().toString());
    String str = s.append("/res/png_to_ppm_testcase.ppm").toString();
    obj = factory.getModel("ppm");
    obj.saveImage(str, "manhattan-png", out);
    ImageUtil.readFile(out,
            getImagePath("/res/png_to_ppm_testcase.ppm"),
            "ppm");
    obj.loadImage(getImagePath("/res/png_to_ppm_testcase.ppm"), "result", out);
    String result = obj.getImageProperties("result");
    assertEquals(expected, result);
  }

  @Test
  public void jpgToBmp() {
    ImageUtil.readFile(out,
            getImagePath("/res/jpg_to_bmp.bmp"),
            "bmp");
    obj.loadImage(getImagePath("/res/jpg_to_bmp.bmp"),
            "jpg_to_bmp_expected", out);
    String expected = obj.getImageProperties("jpg_to_bmp_expected");
    ImageUtil.readFile(out,
            getImagePath("/src/images/sample_jpg.jpeg"),
            "jpeg");
    obj.loadImage(getImagePath("/src/images/sample_jpg.jpeg"),
            "sample-jpeg", out);
    Path currentRelativePath = Paths.get("");
    StringBuilder s = new StringBuilder(currentRelativePath.toAbsolutePath().toString());
    String str = s.append("/res/jpg_to_bmp_testcase.bmp").toString();
    obj.saveImage(str, "sample-jpeg", out);
    ImageUtil.readFile(out,
            getImagePath("/res/jpg_to_bmp_testcase.bmp"),
            "bmp");
    obj.loadImage(getImagePath("/res/jpg_to_bmp_testcase.bmp"), "result", out);
    String result = obj.getImageProperties("result");
    assertEquals(expected, result);
  }*/

}