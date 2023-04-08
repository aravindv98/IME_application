package control;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;

import control.cmd.BlurImage;
import control.cmd.CreateNewGreyscale;
import control.cmd.DitherImage;
import control.cmd.SepiaImage;
import control.cmd.SharpenImage;
import model.IImageManipulationsModelFactory;
import model.NewImageManipulationsModel;

/**
 * Extension of the ImageManipulationsControllerImpl that contains the new model object.
 * This supports both the old and new Image Manipulation operations.
 */
public class ExtendedImageManipulationsControllerImpl extends ImageManipulationsControllerImpl {

  NewImageManipulationsModel model;

  /**
   * Constructor of ExtendedImageManipulationsControllerImpl.
   * It only role right now is to intialize the factory object.
   *
   * @param factory factory object used to get the model.
   * @param out     OutputStream object.
   * @param in      InputStream Object.
   */
  public ExtendedImageManipulationsControllerImpl(IImageManipulationsModelFactory factory,
                                                  OutputStream out, InputStream in) {
    super(factory, out, in);
    this.factory = factory;
  }

  @Override
  protected boolean executeModel(String[] arr, PrintStream outputStream)
          throws IllegalArgumentException {
    boolean commandExecuted = super.executeModel(arr, outputStream);
    if (commandExecuted) {
      return true;
    }
    Map<String, BiFunction<String[], PrintStream, NewImageManipulationsCmd>> knownCommands =
            new HashMap<>();

    knownCommands.put("blur", (a, o) -> new BlurImage(a[1], a[2]));
    knownCommands.put("sharpen", (a, o) -> new SharpenImage(a[1], a[2]));
    knownCommands.put("sepia", (a, o) -> new SepiaImage(a[1], a[2]));
    knownCommands.put("dither", (a, o) -> new DitherImage(a[1], a[2]));
    knownCommands.put("greyscale", (a, o) -> new CreateNewGreyscale(a[1], a[2]));

    NewImageManipulationsCmd c;
    BiFunction<String[], PrintStream, NewImageManipulationsCmd> cmd =
            knownCommands.getOrDefault(arr[0], null);
    if (cmd == null) {
      throw new IllegalArgumentException("Invalid command entered!");
    } else {
      c = cmd.apply(arr, outputStream);
      String str = c.getClass().getSimpleName();
      if (factory != null) {
        model = factory.getModel(fileExtension);
      }
      if (model == null) {
        throw new IllegalArgumentException("Model not loaded!");
      }
      boolean success = c.execute(model);
      if (success) {
        outputStream.print(str + " successful!\n");
      }
      return true;
    }
  }

  protected void printMenu(PrintStream out) {
    super.printMenu(out);
    out.print("blur sourceImage destinationImageName" + System.lineSeparator());
    out.print("sharpen sourceImage destinationImageName" + System.lineSeparator());
    out.print("sepia sourceImage destinationImageName" + System.lineSeparator());
    out.print("dither sourceImage destinationImageName" + System.lineSeparator());
    out.print("greyscale sourceImage destinationImageName" + System.lineSeparator());
  }
}