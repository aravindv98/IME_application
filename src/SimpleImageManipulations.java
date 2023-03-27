import control.*;
import model.IImageManipulationsModelFactory;
import model.ImageManipulationsModel;
import model.ImageManipulationsModelFactory;
import model.PPMImageManipulationsModel;

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
    /*if (args.length == 0) {
      ImageManipulationsModel model = PPMImageManipulationsModel.getInstance();
      ImageManipulationsController controller = new ImageManipulationsControllerImpl(model,
              System.out,
              System.in);
      controller.inputFromUserCommands(); // Calls the main controller.
    } else if (args.length == 1) {
      ImageManipulationsModel model = PPMImageManipulationsModel.getInstance();
      ImageManipulationsController controller = new ImageManipulationsControllerImpl(model,
              System.out,
              System.in);
      controller.inputFromScriptFile(args[0]); // Calls the main controller.
    }*/

    // New Caller code
    if (args.length == 0) {
      ImageManipulationsController controller = new ExtendedImageManipulationsControllerImpl(
              new ImageManipulationsModelFactory(),
              System.out,
              System.in);
      controller.inputFromUserCommands(); // Calls the main controller.
    } else if (args.length == 1) {
      ImageManipulationsController controller = new ExtendedImageManipulationsControllerImpl(
              new ImageManipulationsModelFactory(),
              System.out,
              System.in);
      controller.inputFromScriptFile(args[0]); // Calls the main controller.
    }
  }
}