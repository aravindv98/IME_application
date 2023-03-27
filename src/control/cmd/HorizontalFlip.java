package control.cmd;
import control.*;
import model.*;

public class HorizontalFlip implements ImageManipulationsCmd {
  String sourceImageName;
  String destinationImageName;

  public HorizontalFlip(String sourceImageName,
                         String destinationImageName) {
    this.sourceImageName = sourceImageName;
    this.destinationImageName = destinationImageName;
  }

  @Override
  public boolean go(ImageManipulationsModel m) throws IllegalArgumentException {
    if (m == null){
      throw new IllegalArgumentException("Model not loaded!");
    }
    m.horizontalFlip(sourceImageName, destinationImageName);
    return true;
  }
}
