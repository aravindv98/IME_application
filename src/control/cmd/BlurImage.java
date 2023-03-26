package control.cmd;
import control.*;
import model.*;

public class BlurImage implements ImageManipulationsCmd {

  String sourceImageName;
  String destImageName;

  public BlurImage(String sourceImageName,
                       String destImageName) {
    this.sourceImageName = sourceImageName;
    this.destImageName = destImageName;
  }

  @Override
  public boolean go(NewImageManipulationsModel m) throws IllegalArgumentException {
    if (m == null){
      throw new IllegalArgumentException("Model not loaded!");
    }
    m.blur(sourceImageName, destImageName);
    return true;
  }
}