package control.cmd;
import control.*;
import model.*;

public class VerticalFlip implements ImageManipulationsCmd {
  String sourceImageName;
  String destinationImageName;

  public VerticalFlip(String sourceImageName,
                        String destinationImageName) {
    this.sourceImageName = sourceImageName;
    this.destinationImageName = destinationImageName;
  }

  @Override
  public boolean go(NewImageManipulationsModel m) throws IllegalArgumentException {
    if (m == null){
      throw new IllegalArgumentException("Model not loaded!");
    }
    m.verticalFlip(sourceImageName, destinationImageName);
    return true;
  }
}
