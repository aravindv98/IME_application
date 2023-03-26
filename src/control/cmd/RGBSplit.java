package control.cmd;
import control.*;
import model.*;

public class RGBSplit implements ImageManipulationsCmd {
  String imageName;

  String redDestinationImageName;
  String greenDestinationImageName;
  String blueDestinationImageName;

  public RGBSplit(String imageName,
                         String redDestinationImageName,
                         String greenDestinationImageName,
                         String blueDestinationImageName) {
    this.imageName = imageName;
    this.redDestinationImageName = redDestinationImageName;
    this.greenDestinationImageName = greenDestinationImageName;
    this.blueDestinationImageName = blueDestinationImageName;
  }

  @Override
  public boolean go(NewImageManipulationsModel m) throws IllegalArgumentException {
    if (m == null){
      throw new IllegalArgumentException("Model not loaded!");
    }
    m.rgbSplit(imageName, redDestinationImageName, greenDestinationImageName,
            blueDestinationImageName);
    return true;
  }
}
