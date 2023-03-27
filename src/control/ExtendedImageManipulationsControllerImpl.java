package control;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;

import control.cmd.BlurImage;
import control.cmd.DitherImage;
import control.cmd.SepiaImage;
import control.cmd.SharpenImage;
import model.IImageManipulationsModelFactory;
import model.NewImageManipulationsModel;

public class ExtendedImageManipulationsControllerImpl extends ImageManipulationsControllerImpl {

  NewImageManipulationsModel model;

  /**
   * Constructor of ImageManipulationsController.
   * It only role right now is to intialize the model object.
   *
   * @param factory
   * @param out
   * @param in
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
    if (commandExecuted)
      return true;

    Map<String, BiFunction<String[], PrintStream, NewImageManipulationsCmd>> knownCommands =
            new HashMap<>();

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
    knownCommands.put("dither", (a, o)-> {
      DitherImage obj = new DitherImage(a[1], a[2]);
      return obj;
    });

    NewImageManipulationsCmd c;
    BiFunction<String[], PrintStream, NewImageManipulationsCmd> cmd =
            knownCommands.getOrDefault(arr[0], null);
    if (cmd == null) {
      throw new IllegalArgumentException("Invalid command entered!");
    } else {
      c = cmd.apply(arr, outputStream);
      String str = c.getClass().getSimpleName();
      if (factory != null)
        model = factory.getModel(fileExtension);
      boolean success = c.go(model);
      if (success)
        outputStream.print(str + " successful!\n");

      return true;
    }
  }
}