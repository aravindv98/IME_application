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
import java.util.function.Function;

import model.IImageManipulationsModelFactory;
import model.ImageManipulationsModel;
import control.cmd.*;
import model.ImageManipulationsModelFactory;
import model.NewImageManipulationsModel;

/**
 * This class implements the ImageManipulationsController interface.
 * It reads the contents of the script file or user input and calls the appropriate model function.
 */
public class ImageManipulationsControllerImpl implements ImageManipulationsController {

  private final OutputStream out;

  private final InputStream in;

  NewImageManipulationsModel model;

  IImageManipulationsModelFactory factory;

  /**
   * Constructor of ImageManipulationsController.
   * It only role right now is to intialize the model object.
   */
  public ImageManipulationsControllerImpl(IImageManipulationsModelFactory factory,
                                          OutputStream out,
                                         InputStream in) {
    this.factory = factory;
    this.out = out;
    this.in = in;
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

  private void create( Map<String, BiFunction<String[], PrintStream, ImageManipulationsCmd>>
                               knownCommands) {
    knownCommands.put("brighten", (a, o)-> {
      BrightenImage obj = new BrightenImage(Integer.parseInt(a[1]), a[2], a[3]);
      return obj;
    });

    knownCommands.put("vertical-flip", (a, o)-> {
      VerticalFlip obj = new VerticalFlip(a[1], a[2]);
      return obj;
    });

    knownCommands.put("horizontal-flip", (a, o)-> {
      HorizontalFlip obj = new HorizontalFlip(a[1], a[2]);
      return obj;
    });

    knownCommands.put("load", (a, o)-> {
      model = factory.getModel(a[1]);
      LoadImage obj = new LoadImage(a[1], a[2], o);
      return obj;
    });

    knownCommands.put("save", (a, o)-> {
      model = factory.getModel(a[1]);
      SaveImage obj = new SaveImage(a[1], a[2], o);
      return obj;
    });

    knownCommands.put("greyscale", (a, o)-> {
      CreateGreyscale obj = new CreateGreyscale(a[1], a[2], a[3]);
      return obj;
    });

    knownCommands.put("rgb-split", (a, o)-> {
      RGBSplit obj = new RGBSplit(a[1], a[2], a[3], a[4]);
      return obj;
    });

    knownCommands.put("rgb-combine", (a, o)-> {
      RGBCombine obj = new RGBCombine(a[1], a[2], a[3], a[4]);
      return obj;
    });

    knownCommands.put("blur", (a, o)-> {
      BlurImage obj = new BlurImage(a[1], a[2]);
      return obj;
    });

    knownCommands.put("sharpen", (a, o)-> {
      SharpenImage obj = new SharpenImage(a[1], a[2]);
      return obj;
    });

    knownCommands.put("sepia", (a, o)-> {
      SepiaImage obj = new SepiaImage(a[1], a[2]);
      return obj;
    });
  }

  /**
   * Contains the execution paths of the model.
   * Calls the appropriate model function based on the first keyword.
   *
   * @param arr          the file content stored in an array.
   * @param outputStream this is used to get the exception message if file is not found.
   */
  private void executeModel(String[] arr, PrintStream outputStream)
          throws IllegalArgumentException {
    Map<String, BiFunction<String[], PrintStream, ImageManipulationsCmd>> knownCommands =
            new HashMap<>();
    create(knownCommands);

    ImageManipulationsCmd c;
    BiFunction<String[], PrintStream, ImageManipulationsCmd> cmd =
            knownCommands.getOrDefault(arr[0], null);
    if (cmd == null) {
      throw new IllegalArgumentException("Invalid command entered!");
    } else {
      c = cmd.apply(arr, outputStream);
      String str = c.getClass().getSimpleName();
      c.go(model);
      outputStream.print(str + " successful!\n");
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
      executeModel(arr, outputStream);
    }
  }

  @Override
  public void inputFromUserCommands() throws IllegalArgumentException {
    PrintStream outputStream = new PrintStream(this.out);
    outputStream.println("Enter user command for PPM image manipulation and Q to Quit:");
    Scanner sc = new Scanner(in);
    String line = sc.nextLine();

    while (!line.equals("Q")) {
      String[] arr = line.split(" ");
      executeModel(arr, outputStream);

      if (Objects.equals(arr[0], "run")) {
        inputFromScriptFile(arr[1]);
      }

      outputStream.println();
      outputStream.println("Enter next command:");
      line = sc.nextLine();
    }

    outputStream.println("Execution of commands stopped!");
  }
}