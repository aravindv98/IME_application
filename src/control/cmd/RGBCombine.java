package control.cmd;
import control.*;
import model.*;

public class RGBCombine implements ImageManipulationsCmd {
  String destinationImage;

  String redSourceImageName;
  String greenSourceImageName;
  String blueSourceImageName;

  public RGBCombine(String destinationImage,
                  String redSourceImageName,
                  String greenSourceImageName,
                  String blueSourceImageName) {
    this.destinationImage = destinationImage;
    this.redSourceImageName = redSourceImageName;
    this.greenSourceImageName = greenSourceImageName;
    this.blueSourceImageName = blueSourceImageName;
  }

  @Override
  public void go(NewImageManipulationsModel m) throws IllegalArgumentException {
    if (m == null){
      throw new IllegalArgumentException("Model not loaded!");
    }
    m.rgbCombine(destinationImage, redSourceImageName, greenSourceImageName,
            blueSourceImageName);
  }
}
