package control;

/**
 * This interface represents methods for consuming inputs for
 * image manipulation through a script file or by user commands.
 */
public interface ImageManipulationsController {

  /**
   * Starting point of the controller.
   * This method supports passing file as command line argument.
   *
   * @param filePath path of the file passed as an argument .
   */
  void inputFromScriptFile(String filePath) throws IllegalArgumentException;

  /**
   * Starting point of the controller.
   * This method supports passing user commands as inputs.
   */
  void inputFromUserCommands() throws IllegalArgumentException;
}