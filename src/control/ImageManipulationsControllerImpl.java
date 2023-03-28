package control;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Scanner;
import java.util.function.BiFunction;

import control.cmd.BrightenImage;
import control.cmd.CreateGreyscale;
import control.cmd.CreateNewGreyscale;
import control.cmd.HorizontalFlip;
import control.cmd.LoadImage;
import control.cmd.RGBCombine;
import control.cmd.RGBSplit;
import control.cmd.SaveImage;
import control.cmd.VerticalFlip;
import model.IImageManipulationsModelFactory;
import model.ImageManipulationsModel;

/**
 * This class implements the ImageManipulationsController interface.
 * It reads the contents of the script file or user input and calls the appropriate model function.
 */
public class ImageManipulationsControllerImpl implements ImageManipulationsController {

  private final OutputStream out;
  private final InputStream in;
  ImageManipulationsModel model;

  IImageManipulationsModelFactory factory;

  String fileExtension;

  public ImageManipulationsControllerImpl(OutputStream out,
                                          InputStream in) {
    this.out = out;
    this.in = in;
  }

  public ImageManipulationsControllerImpl(ImageManipulationsModel model,
                                          OutputStream out,
                                          InputStream in) {
    this(out, in);
    this.model = model;
  }

  public ImageManipulationsControllerImpl(IImageManipulationsModelFactory factory,
                                          OutputStream out, InputStream in) {
    this(out, in);
    this.factory = factory;
  }

  /**
   * Reads the contents of the PPM file and aggregates it into a String.
   *
   * @param filePath path of the file passed as an argument.
   * @param out      this is used to get the exception message if file is not found.
   */
  private StringBuilder getFileContent(String filePath, PrintStream out) {
    StringBuilder builder = new StringBuilder();

    try {
      File fileObj = new File(filePath);
      Scanner reader = new Scanner(fileObj);
      while (reader.hasNextLine()) {
        String s = reader.nextLine();

        // Ignores the blank line
        if (s.length() == 0) {
          continue;
        }

        // Ignores the comments
        if (s.charAt(0) != '#') {
          builder.append(s).append(System.lineSeparator());
        }
      }
      reader.close();
    } catch (FileNotFoundException e) {
      out.print("File " + filePath + " not found!");
    }

    return builder;
  }

  /**
   * Contains the execution paths of the model.
   * Calls the appropriate model function based on the first keyword.
   *
   * @param arr          the file content stored in an array.
   * @param outputStream this is used to get the exception message if file is not found.
   */
  protected boolean executeModel(String[] arr, PrintStream outputStream)
          throws IllegalArgumentException {
    Map<String, BiFunction<String[], PrintStream, ImageManipulationsCmd>> knownCommands =
            new HashMap<>();

    knownCommands.put("brighten", (a, o) -> new BrightenImage(Integer.parseInt(a[1]), a[2], a[3]));
    knownCommands.put("vertical-flip", (a, o) -> new VerticalFlip(a[1], a[2]));
    knownCommands.put("horizontal-flip", (a, o) -> new HorizontalFlip(a[1], a[2]));
    knownCommands.put("rgb-split", (a, o) -> new RGBSplit(a[1], a[2], a[3], a[4]));
    knownCommands.put("rgb-combine", (a, o) -> new RGBCombine(a[1], a[2], a[3], a[4]));

    knownCommands.put("greyscale", (a, o) -> {
      if (a.length == 4)
        return new CreateGreyscale(a[1], a[2], a[3]);

      return null;
    });

    knownCommands.put("load", (a, o) -> {
      fileExtension = a[1];
      return new LoadImage(a[1], a[2], o);
    });

    knownCommands.put("save", (a, o) -> {
      fileExtension = a[1];
      return new SaveImage(a[1], a[2], o);
    });

    ImageManipulationsCmd c;
    BiFunction<String[], PrintStream, ImageManipulationsCmd> cmd =
            knownCommands.getOrDefault(arr[0], null);
    if (cmd == null) {
      if (this.getClass().getSimpleName().equals("ImageManipulationsControllerImpl")) {
        throw new IllegalArgumentException("Invalid command entered!");
      }
      return false;
    } else {
      c = cmd.apply(arr, outputStream);
      if (c==null) {
        if (this.getClass().getSimpleName().equals("ImageManipulationsControllerImpl")) {
          throw new IllegalArgumentException("Invalid command entered!");
        }
        return false;
      }
      String str = c.getClass().getSimpleName();
      if (factory != null)
        model = factory.getModel(fileExtension);
      boolean success = c.go(model);
      if (success)
        outputStream.print(str + " successful!\n");

      return true;
    }
  }

  @Override
  public void inputFromScriptFile(String filePath) throws IllegalArgumentException {
    PrintStream outputStream = new PrintStream(this.out);
    StringBuilder fileContent = getFileContent(filePath, outputStream);
    Scanner sc = new Scanner(fileContent.toString());
    while (sc.hasNextLine()) {
      String line = sc.nextLine();
      if (line.length() == 0) {
        continue;
      }

      String[] arr = line.split(" ");
      this.executeModel(arr, outputStream);
    }
  }

  @Override
  public void inputFromUserCommands() throws IllegalArgumentException {
    PrintStream outputStream = new PrintStream(this.out);
    this.welcomeMessage(outputStream);

    outputStream.println(System.lineSeparator() +
            "Enter user command for PPM image manipulation and Q to Quit:");
    Scanner sc = new Scanner(in);
    String line = sc.nextLine();

    while (!line.equals("Q")) {
      String[] arr = line.split(" ");
      this.executeModel(arr, outputStream);

      if (Objects.equals(arr[0], "run")) {
        inputFromScriptFile(arr[1]);
      }

      outputStream.println();
      outputStream.println("Enter next command:");
      line = sc.nextLine();
    }

    this.farewellMessage(outputStream);
  }

  protected void printMenu(PrintStream out) {
    out.print("Supported user instructions are: " + System.lineSeparator());
    out.print("load imagePath imageName"
            + System.lineSeparator());
    out.print("save imagePath imageName"
            + System.lineSeparator());
    out.print("brighten increment sourceImage destinationImageName" + System.lineSeparator());
    out.print("vertical-flip sourceImage destinationImageName" + System.lineSeparator());
    out.print("horizontal-flip sourceImage destinationImageName" + System.lineSeparator());
    out.print("greyscale component-type sourceImage destinationImageName"
            + System.lineSeparator());
    out.print("rgb-combine destinationImageName sourceRedComponentImage sourceGreenComponentImage "
            + "sourceBlueComponentImage"
            + System.lineSeparator());
    out.print("rgb-split sourceImageName destinationRedComponentImage "
            + "destinationGreenComponentImage "
            + "destinationBlueComponentImage"
            + System.lineSeparator());
  }

  protected void welcomeMessage(PrintStream out) {
    out.print(System.lineSeparator() + "Welcome to the image processing program!"
            + System.lineSeparator());
    printMenu(out);
  }

  private void farewellMessage(PrintStream out) {
    out.print("Thank you for using this program!");
  }
}