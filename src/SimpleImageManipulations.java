import control.ExtendedImageManipulationsControllerImpl;
import control.ImageManipulationsController;
import model.ImageManipulationsModelFactory;

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

    if (args.length > 1) {
      throw new IllegalArgumentException("Invalid number of arguments passed!");
    }

    // The old client code can still be reused by just removing the comments.
    /*ImageManipulationsModel model = PPMImageManipulationsModel.getInstance();
    ImageManipulationsController controller = new ImageManipulationsControllerImpl(model,
            System.out,
            System.in);
    if (args.length == 0) {
      controller.inputFromUserCommands(); // Calls the main controller.
    } else {
      controller.inputFromScriptFile(args[0]); // Calls the main controller.
    }*/

    // New Caller code
    ImageManipulationsController controller = new ExtendedImageManipulationsControllerImpl(
            new ImageManipulationsModelFactory(),
            System.out,
            System.in);
    if (args.length == 0) {
      controller.inputFromUserCommands(); // Calls the main controller.
    } else {
      controller.inputFromScriptFile(args[0]); // Calls the main controller.
    }
  }
}