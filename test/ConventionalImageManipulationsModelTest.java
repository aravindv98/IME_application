import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.OutputStream;
import java.nio.file.Path;
import java.nio.file.Paths;

import model.ConventionalImageManipulationsModel;
import model.NewImageManipulationsModel;

import static org.junit.Assert.*;

public class ConventionalImageManipulationsModelTest {

  NewImageManipulationsModel obj;
  OutputStream out;

  @Before
  public void setup() {
    out = new ByteArrayOutputStream();
    obj = ConventionalImageManipulationsModel.getInstance();
    obj.loadImage(getImagePath("/test/testData/manhattan-small.png"),
            "manhattan-small", out);
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
    obj.createGreyScale("luma-component", "manhattan-small",
            "manhattan-small-greyscale-result");
    String result = obj.getImageProperties("manhattan-small-greyscale-result");
    assertEquals(expected, result);
  }
}