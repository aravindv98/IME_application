import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Path;
import java.nio.file.Paths;

import control.ImageManipulationsController;
import control.ImageManipulationsControllerImpl;
import model.IImageManipulationsModelFactory;
import model.ImageManipulationsModel;
import model.ImageManipulationsModelFactory;
import model.NewImageManipulationsModel;
import model.PPMImageManipulationsModel;

import static org.junit.Assert.assertEquals;

/**
 * This class tests the main working PPMImageManipulation Controller.
 */
public class PPMImageManipulationsControllerTest {

  OutputStream out;
  InputStream in;
  StringBuilder mockLog;

  IImageManipulationsModelFactory factory;

  @Before
  public void setup() {
    out = new ByteArrayOutputStream();
    in = null;
    mockLog = new StringBuilder();
    factory = new MockFactory();
  }

  @After
  public void cleanUp() {
    Path currentRelativePath = Paths.get("");
    String filePath = currentRelativePath.toAbsolutePath()
            + "/test/testData/Test_Image-brighten.ppm";
    File f = new File(filePath);
    f.delete();
  }

  /**
   * Test case to check if invalid path of file is not being accepted.
   *
   */
  @Test
  public void TestInvalidPath() {
    Path currentRelativePath = Paths.get("");
    String filePath = currentRelativePath.toAbsolutePath()
            + "/test/testDatas/Script_File.txt";
    ImageManipulationsController controller = new ImageManipulationsControllerImpl(
            new ImageManipulationsModelFactory(),
            out,
            in);
    controller.inputFromScriptFile(filePath);
    assertEquals("File " + filePath + " not found!", out.toString());
  }

  /**
   * Test case to check invalid file content.
   *
   */
  @Test(expected = IllegalArgumentException.class)
  public void TestInvalidFileContent() {
    Path currentRelativePath = Paths.get("");
    String filePath = currentRelativePath.toAbsolutePath()
            + "/test/testData/Script_Error_File.txt";
    ImageManipulationsController controller = new ImageManipulationsControllerImpl(
            new ImageManipulationsModelFactory(), out, in);
    controller.inputFromScriptFile(filePath);
  }

  /**
   * Test case to check the order of execution as per the script file in the test folder.
   *
   */
  @Test
  public void TestControllerOrderOfExecution() {
    String expected = "LoadImage successful!\n"
            + "BrightenImage successful!\n"
            + "SaveImage successful!\n"
            + "VerticalFlip successful!\n"
            + "HorizontalFlip successful!\n"
            + "CreateGreyscale successful!\n"
            + "RGBSplit successful!\n"
            + "RGBCombine successful!\n";

    Path currentRelativePath = Paths.get("");
    String filePath = currentRelativePath.toAbsolutePath()
            + "/test/testData/Script_File.txt";
    ImageManipulationsController controller = new ImageManipulationsControllerImpl(
            new ImageManipulationsModelFactory(), out, in);
    controller.inputFromScriptFile(filePath);
    assertEquals(expected, out.toString());
  }

  /**
   * Test case to check if user commands are being accepted
   * from controller to mock model.
   */
  @Test
  public void checkInputsForLoad() {
    String input = "load test/testData/Test_Image.ppm Test_Image\n"
            + "Q";
    in = new ByteArrayInputStream(input.getBytes());
    String imagePath = "test/testData/Test_Image.ppm";
    String imageName = "Test_Image";
    ImageManipulationsController controller = new ImageManipulationsControllerImpl(factory,
            out, in);
    controller.inputFromUserCommands();
    assertEquals("Received inputs " + imagePath + " and " + imageName + "\n",
            mockLog.toString());
  }

  /**
   * Test case to check invalid user command is throwing an exception
   * for invalid input of command from controller to mock model.
   */
  @Test(expected = IllegalArgumentException.class)
  public void checkInvalidInputsForLoad() {
    String input = "lad test/testData/Test_Image.ppm Test_Image\n"
            + "Q";
    in = new ByteArrayInputStream(input.getBytes());
    String imagePath = "test/testData/Test_Image.ppm";
    String imageName = "Test_Image";
    ImageManipulationsController controller = new ImageManipulationsControllerImpl(factory,
            out, in);
    controller.inputFromUserCommands();
    assertEquals("Received inputs " + imagePath + " and " + imageName, mockLog.toString());
  }

  /**
   * Test case to check user commands for saving a ppm image
   * from controller to mock model.
   */
  @Test
  public void checkInputsForSave() {
    String input = "load test/testData/Test_Image.ppm Test_Image\n"
            + "save test/testData/Test_Image.ppm Test_Image\n"
            + "Q";
    in = new ByteArrayInputStream(input.getBytes());
    String imagePath = "test/testData/Test_Image.ppm";
    String imageName = "Test_Image";
    ImageManipulationsController controller = new ImageManipulationsControllerImpl(factory,
            out, in);
    controller.inputFromUserCommands();
    assertEquals("Received inputs " + imagePath + " and " + imageName + "\n" +
            "Received inputs " + imagePath + " and " + imageName + "\n",
            mockLog.toString());
  }

  /**
   * Test case to check inputs for greyscale of a ppm image
   * from controller to mock model.
   */
  @Test
  public void checkInputsForGreyscale() {

    String input = "load test/testData/Test_Image.ppm Test_Image\n"
            + "greyscale value-component Test_Image Test_Image-greyscale\n"
            + "Q";
    String componentType = "value-component";
    String imageName = "Test_Image";
    String imagePath = "test/testData/Test_Image.ppm";
    String destinationImageName = "Test_Image-greyscale";
    in = new ByteArrayInputStream(input.getBytes());
    ImageManipulationsController controller = new ImageManipulationsControllerImpl(factory,
            out, in);
    controller.inputFromUserCommands();
    assertEquals( "Received inputs " + imagePath + " and " + imageName + "\n" +
            "Received inputs " + componentType + " , " + imageName
            + " and "
            + destinationImageName + "\n", mockLog.toString());
  }

  /**
   * Test case to check inputs for horizontal flip of a ppm image
   * from controller to mock model.
   */
  @Test
  public void checkInputsForHorizontalFlip() {
    String input = "load test/testData/Test_Image.ppm Test_Image\n"
            + "horizontal-flip Test_Image Test_Image-horizontal\n"
            + "Q";
    in = new ByteArrayInputStream(input.getBytes());
    String imageName = "Test_Image";
    String imagePath = "test/testData/Test_Image.ppm";
    String destinationImageName = "Test_Image-horizontal";
    ImageManipulationsController controller = new ImageManipulationsControllerImpl(
            factory, out, in);
    controller.inputFromUserCommands();
    assertEquals( "Received inputs " + imagePath + " and " + imageName + "\n" +
            "Received inputs " + imageName + " and " + destinationImageName + "\n",
            mockLog.toString());
  }

  /**
   * Test case to check inputs for a vertical flip of a ppm image
   * from controller to mock model.
   */
  @Test
  public void checkInputsForVerticalFlip() {
    String input = "load test/testData/Test_Image.ppm Test_Image\n"
            + "vertical-flip Test_Image Test_Image-vertical\n"
            + "Q";
    in = new ByteArrayInputStream(input.getBytes());
    String imageName = "Test_Image";
    String imagePath = "test/testData/Test_Image.ppm";
    String destinationImageName = "Test_Image-vertical";
    ImageManipulationsController controller = new ImageManipulationsControllerImpl(
            factory, out, in);
    controller.inputFromUserCommands();
    assertEquals("Received inputs " + imagePath + " and " + imageName + "\n" +
            "Received inputs " + imageName + " and " + destinationImageName + "\n",
            mockLog.toString());
  }

  /**
   * Test case to check inputs for brightening a ppm image
   * from controller to mock model.
   */
  @Test
  public void checkInputsForBrighten() {
    String input = "load test/testData/Test_Image.ppm Test_Image\n"
            + "brighten 10 Test_Image Test_Image-brighten\n"
            + "Q";
    in = new ByteArrayInputStream(input.getBytes());
    String increment = "10";
    String imageName = "Test_Image";
    String imagePath = "test/testData/Test_Image.ppm";
    String destinationImageName = "Test_Image-brighten";
    ImageManipulationsController controller = new ImageManipulationsControllerImpl(
            factory, out, in);
    controller.inputFromUserCommands();
    assertEquals("Received inputs " + imagePath + " and " + imageName + "\n" +
            "Received inputs " + increment + " , " + imageName
                    + " and "
                    + destinationImageName + "\n",
            mockLog.toString());
  }

  /**
   * Test case to check inputs for the rgb split of a ppm image
   * from controller to mock model.
   */
  @Test
  public void checkInputsForRGBSplit() {
    String input = "load test/testData/Test_Image.ppm Test_Image\n"
            + "rgb-split Test_Image Test_Image-red Test_Image-green Test_Image-blue\n"
            + "Q";
    in = new ByteArrayInputStream(input.getBytes());
    String imageName = "Test_Image";
    String imagePath = "test/testData/Test_Image.ppm";
    String redDestinationImageName = "Test_Image-red";
    String greenDestinationImageName = "Test_Image-green";
    String blueDestinationImageName = "Test_Image-blue";
    ImageManipulationsController controller = new ImageManipulationsControllerImpl(
            factory, out, in);
    controller.inputFromUserCommands();
    assertEquals("Received inputs " + imagePath + " and " + imageName + "\n" +
            "Received inputs " + imageName + " , " + redDestinationImageName
                    + " , " + greenDestinationImageName
                    + " and " + blueDestinationImageName + "\n",
            mockLog.toString());
  }

  /**
   * Test case to check inputs for rgb combine of a ppm image
   * from controller to mock model.
   */
  @Test
  public void checkInputsForRGBCombine() {
    String input = "load test/testData/Test_Image.ppm Test_Image\n"
            + "rgb-combine Test_Image-combine Test_Image-red "
            + "Test_Image-green Test_Image-blue\n"
            + "Q";
    in = new ByteArrayInputStream(input.getBytes());
    String destinationImageName = "Test_Image-combine";
    String redSourceImageName = "Test_Image-red";
    String greenSourceImageName = "Test_Image-green";
    String blueSourceImageName = "Test_Image-blue";
    String imagePath = "test/testData/Test_Image.ppm";
    String imageName = "Test_Image";
    ImageManipulationsController controller = new ImageManipulationsControllerImpl(
            factory, out, in);
    controller.inputFromUserCommands();
    assertEquals("Received inputs " + imagePath + " and " + imageName + "\n"
            + "Received inputs " + destinationImageName + " , " + redSourceImageName
                    + " , " + greenSourceImageName
                    + " and " + blueSourceImageName + "\n",
            mockLog.toString());
  }

  /**
   * Test case to check if image brightening before loading is
   * throwing an exception.
   */
  @Test(expected = IllegalArgumentException.class)
  public void ImageBrightenedBeforeLoading() {
    String input = "brighten 10 Test_Image Test_Image-brighten";
    in = new ByteArrayInputStream(input.getBytes());
    ImageManipulationsController controller = new ImageManipulationsControllerImpl(
            new ImageManipulationsModelFactory(), out, in);
    controller.inputFromUserCommands();
  }

  /**
   * Mock Model class used to test the ImageManipulationsControllerImpl.
   * We basically test if the inputs to the Model match with the inputs passed by the controller.
   */
  public class MockModel implements NewImageManipulationsModel {

    private final StringBuilder log;

    MockModel(StringBuilder log) {
      this.log = log;
    }

    @Override
    public boolean loadImage(String imagePath, String imageName, OutputStream out)
            throws IllegalArgumentException {
      log.append("Received inputs ").append(imagePath).append(" and ").append(imageName)
              .append("\n");
      return true;
    }

    @Override
    public boolean saveImage(String imagePath, String imageName, OutputStream out)
            throws IllegalArgumentException {
      log.append("Received inputs ").append(imagePath).append(" and ").append(imageName)
              .append("\n");
      return true;
    }

    @Override
    public void createGreyScale(String componentType, String imageName,
                                String destinationImageName) throws IllegalArgumentException {
      log.append("Received inputs ").append(componentType).append(" , ")
              .append(imageName).append(" and ").append(destinationImageName)
              .append("\n");
    }

    @Override
    public void horizontalFlip(String imageName, String destinationImageName)
            throws IllegalArgumentException {
      log.append("Received inputs ").append(imageName)
              .append(" and ").append(destinationImageName)
              .append("\n");
    }

    @Override
    public void verticalFlip(String imageName, String destinationImageName)
            throws IllegalArgumentException {
      log.append("Received inputs ").append(imageName)
              .append(" and ").append(destinationImageName)
              .append("\n");
    }

    @Override
    public void brighten(int increment, String imageName, String destinationImageName)
            throws IllegalArgumentException {
      log.append("Received inputs ").append(increment).append(" , ")
              .append(imageName).append(" and ").append(destinationImageName).append("\n");
    }

    @Override
    public void rgbSplit(String imageName,
                         String redDestinationImageName,
                         String greenDestinationImageName,
                         String blueDestinationImageName) throws IllegalArgumentException {
      log.append("Received inputs ").append(imageName)
              .append(" , ").append(redDestinationImageName)
              .append(" , ").append(greenDestinationImageName)
              .append(" and ").append(blueDestinationImageName)
              .append("\n");
    }

    @Override
    public void rgbCombine(String destinationImageName,
                           String redSourceImageName,
                           String greenSourceImageName,
                           String blueSourceImageName) throws IllegalArgumentException {
      log.append("Received inputs ").append(destinationImageName)
              .append(" , ").append(redSourceImageName)
              .append(" , ").append(greenSourceImageName)
              .append(" and ").append(blueSourceImageName)
              .append("\n");
    }

    @Override
    public String getImageProperties(String imageName) {
      return null;
    }

    @Override
    public void sepia(String imageName, String destinationImageName) throws IllegalArgumentException {

    }

    @Override
    public void dither(String imageName, String destinationImageName) throws IllegalArgumentException {

    }

    @Override
    public void blur(String imageName, String destinationImageName) throws IllegalArgumentException {

    }

    @Override
    public void sharpen(String imageName, String destinationImageName) throws IllegalArgumentException {

    }
  }

  public class MockFactory implements IImageManipulationsModelFactory {

    @Override
    public NewImageManipulationsModel getModel(String fileName) {
      return new MockModel(mockLog);
    }
  }
}