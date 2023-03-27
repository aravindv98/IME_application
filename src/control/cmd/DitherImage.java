package control.cmd;

import control.NewImageManipulationsCmd;
import model.NewImageManipulationsModel;

public class DitherImage implements NewImageManipulationsCmd {

  String sourceImageName;
  String destImageName;

  public DitherImage(String sourceImageName,
                   String destImageName) {
    this.sourceImageName = sourceImageName;
    this.destImageName = destImageName;
  }

  @Override
  public boolean go(NewImageManipulationsModel m) throws IllegalArgumentException {
    if (m == null){
      throw new IllegalArgumentException("Model not loaded!");
    }
    m.dither(sourceImageName, destImageName);
    return true;
  }
}
