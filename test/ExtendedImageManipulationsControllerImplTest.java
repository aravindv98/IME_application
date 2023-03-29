import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

import control.ExtendedImageManipulationsControllerImpl;
import control.ImageManipulationsController;
import model.IImageManipulationsModelFactory;
import model.ImageManipulationsModelFactory;
import model.NewImageManipulationsModel;

import static org.junit.Assert.assertEquals;

/**
 * This class tests the main working ExtendedImageManipulationControllerImpl.
 */
public class ExtendedImageManipulationsControllerImplTest
        extends ImageManipulationsControllerImplTest {

  IImageManipulationsModelFactory factory;

  @Before
  public void setup() {
    super.setup();
    factory = new MockFactory();
  }

  @After
  public void cleanUp() {
    Path currentRelativePath = Paths.get("");
    String filePath = currentRelativePath.toAbsolutePath()
            + "/test/testData/manhattan-blur.ppm";
    File f = new File(filePath);
    f.delete();
  }

  @Test
  public void TestNewControllerOrderOfExecution() {
    String expected = "LoadImage successful!\n"
            + "BlurImage successful!\n"
            + "SaveImage successful!\n"
            + "DitherImage successful!\n"
            + "SepiaImage successful!\n"
            + "SharpenImage successful!\n";

    Path currentRelativePath = Paths.get("");
    String filePath = currentRelativePath.toAbsolutePath()
            + "/test/testData/New_Script_File.txt";
    ImageManipulationsController controller = new ExtendedImageManipulationsControllerImpl(
            new ImageManipulationsModelFactory(),
            out, in);
    controller.inputFromScriptFile(filePath);
    assertEquals(expected, out.toString());
  }

  @Test
  public void checkInputsForSepia() {
    String input = "sepia Test_Image Test_Image-sepia\n"
            + "Q";
    in = new ByteArrayInputStream(input.getBytes());
    String imageName = "Test_Image";
    String destinationImageName = "Test_Image-sepia";
    ImageManipulationsController controller = new ExtendedImageManipulationsControllerImpl(factory,
            out, in);
    controller.inputFromUserCommands();
    assertEquals("Received inputs " + imageName
                    + " and "
                    + destinationImageName,
            mockLog.toString());
  }

  @Test
  public void checkInputsForDither() {
    String input = "dither Test_Image Test_Image-dither\n"
            + "Q";
    in = new ByteArrayInputStream(input.getBytes());
    String imageName = "Test_Image";
    String destinationImageName = "Test_Image-dither";
    ImageManipulationsController controller = new ExtendedImageManipulationsControllerImpl(factory,
            out, in);
    controller.inputFromUserCommands();
    assertEquals("Received inputs " + imageName
                    + " and "
                    + destinationImageName,
            mockLog.toString());
  }

  @Test
  public void checkInputsForNewGreyscale() {
    String input = "greyscale Test_Image Test_Image-greyscale\n"
            + "Q";
    in = new ByteArrayInputStream(input.getBytes());
    String imageName = "Test_Image";
    String destinationImageName = "Test_Image-greyscale";
    ImageManipulationsController controller = new ExtendedImageManipulationsControllerImpl(factory,
            out, in);
    controller.inputFromUserCommands();
    assertEquals("Received inputs " + imageName
                    + " and "
                    + destinationImageName,
            mockLog.toString());
  }

  @Test
  public void checkInputsForSharpen() {
    String input = "sharpen Test_Image Test_Image-sharpen\n"
            + "Q";
    in = new ByteArrayInputStream(input.getBytes());
    String imageName = "Test_Image";
    String destinationImageName = "Test_Image-sharpen";
    ImageManipulationsController controller = new ExtendedImageManipulationsControllerImpl(factory,
            out, in);
    controller.inputFromUserCommands();
    assertEquals("Received inputs " + imageName
                    + " and "
                    + destinationImageName,
            mockLog.toString());
  }

  @Test
  public void checkInputsFor() {
    String input = "blur Test_Image Test_Image-blur\n"
            + "Q";
    in = new ByteArrayInputStream(input.getBytes());
    String imageName = "Test_Image";
    String destinationImageName = "Test_Image-blur";
    ImageManipulationsController controller = new ExtendedImageManipulationsControllerImpl(factory,
            out, in);
    controller.inputFromUserCommands();
    assertEquals("Received inputs " + imageName
                    + " and "
                    + destinationImageName,
            mockLog.toString());
  }

  /**
   * A mock model class created to test the flow of the controller.
   */
  public class NewMockModel extends ImageManipulationsControllerImplTest.MockModel
          implements NewImageManipulationsModel {

    NewMockModel(StringBuilder log) {
      super(log);
    }

    @Override
    public void sepia(String imageName,
                      String destinationImageName) throws IllegalArgumentException {
      log.append("Received inputs ").append(imageName).append(" and ").append(destinationImageName);
    }

    @Override
    public void dither(String imageName,
                       String destinationImageName) throws IllegalArgumentException {
      log.append("Received inputs ").append(imageName).append(" and ").append(destinationImageName);
    }

    @Override
    public void createGreyScale(String imageName, String destinationImageName)
            throws IllegalArgumentException {
      log.append("Received inputs ").append(imageName).append(" and ").append(destinationImageName);
    }

    @Override
    public void blur(String imageName,
                     String destinationImageName) throws IllegalArgumentException {
      log.append("Received inputs ").append(imageName).append(" and ").append(destinationImageName);
    }

    @Override
    public void sharpen(String imageName,
                        String destinationImageName) throws IllegalArgumentException {
      log.append("Received inputs ").append(imageName).append(" and ").append(destinationImageName);
    }
  }

  /**
   * A mock class created to implement the IImageManipulationsModelFactory
   * interface.
   */
  public class MockFactory implements IImageManipulationsModelFactory {

    @Override
    public NewImageManipulationsModel getModel(String fileName) {
      return new NewMockModel(mockLog);
    }
  }
}