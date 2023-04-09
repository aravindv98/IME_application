import control.ExtendedImageManipulationsControllerImpl;
import control.GUICommandCallbackController;
import control.ImageManipulationsController;
import model.ImageManipulationsModelFactory;
import view.IView;
import view.JFrameView;

/**
 * Contains the main method which is the entry point of our program.
 */
public class SimpleImageManipulations {

  /**
   * Staring point of our program execution, calls the controller.
   *
   * @param args arguments passed to the main function.
   */
  public static void main(String[] args) throws IllegalArgumentException {

    if (args.length > 2) {
      throw new IllegalArgumentException("Invalid number of arguments passed!");
    }

    // The good thing about our design is that the old client code
    // used for Assignment 4, can still be reused by just removing the comments
    // and commenting out the new client code.

    /*ImageManipulationsModel model = PPMImageManipulationsModel.getInstance();
    ImageManipulationsController controller = new ImageManipulationsControllerImpl(model,
            System.out,
            System.in);
    if (args.length == 0) {
      controller.inputFromUserCommands(); // Calls the main controller.
    } else {
      if (args[0].equals("-file")) {
        controller.inputFromScriptFile(args[1]); // Calls the main controller.
      } else {
        throw new IllegalArgumentException("Invalid argument entered for" +
                "running a script!");
      }
    }*/

    if (args.length >= 1) {
      // New Caller code
      ImageManipulationsController controller = new ExtendedImageManipulationsControllerImpl(
              new ImageManipulationsModelFactory(),
              System.out,
              System.in);
      if (args[0].equals("-text")) {
        controller.inputFromUserCommands(); // Calls the main controller.
      } else {
        if (args[0].equals("-file")) {
          controller.inputFromScriptFile(args[1]); // Calls the main controller.
        } else {
          throw new IllegalArgumentException("Invalid argument entered for" +
                  "running a script!");
        }
      }
    }
    else {
      IView view = new JFrameView();
      new GUICommandCallbackController(
              new ImageManipulationsModelFactory(),
              System.out,
              System.in,
              view);
    }
  }
}