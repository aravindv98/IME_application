package control.cmd;

import control.ImageManipulationsCmd;
import model.NewImageManipulationsModel;

public class DitherImage implements ImageManipulationsCmd {

  String sourceImageName;
  String destImageName;

  public DitherImage(String sourceImageName,
                   String destImageName) {
    this.sourceImageName = sourceImageName;
    this.destImageName = destImageName;
  }

  @Override
  public void go(NewImageManipulationsModel m) throws IllegalArgumentException {
    if (m == null){
      throw new IllegalArgumentException("Model not loaded!");
    }
    m.dither(sourceImageName, destImageName);
  }
}
