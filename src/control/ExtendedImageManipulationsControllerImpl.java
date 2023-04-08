package control;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;

import control.cmd.BlurImage;
import control.cmd.CreateNewGreyscale;
import control.cmd.DitherImage;
import control.cmd.SepiaImage;
import control.cmd.SharpenImage;
import model.IImageManipulationsModelFactory;
import model.NewImageManipulationsModel;
import utility.ImageUtil;
import utility.Pixels;
import view.Features;
import view.IView;

/**
 * Extension of the ImageManipulationsControllerImpl that contains the new model object.
 * This supports both the old and new Image Manipulation operations.
 */
public class ExtendedImageManipulationsControllerImpl extends ImageManipulationsControllerImpl implements Features {

  NewImageManipulationsModel model;

  private IView view;

  /**
   * Constructor of ExtendedImageManipulationsControllerImpl.
   * It only role right now is to intialize the factory object.
   *
   * @param factory factory object used to get the model.
   * @param out     OutputStream object.
   * @param in      InputStream Object.
   */
  public ExtendedImageManipulationsControllerImpl(IImageManipulationsModelFactory factory,
                                                  OutputStream out, InputStream in) {
    super(factory, out, in);
    this.factory = factory;
  }

  public ExtendedImageManipulationsControllerImpl(IImageManipulationsModelFactory factory,
                                                  OutputStream out, InputStream in, IView v) {
    super(factory, out, in);
    this.factory = factory;
    this.view = v;
    view.addFeatures(this);
  }

  @Override
  protected boolean executeModel(String[] arr, PrintStream outputStream)
          throws IllegalArgumentException {
    boolean commandExecuted = super.executeModel(arr, outputStream);
    if (commandExecuted) {
      return true;
    }
    Map<String, BiFunction<String[], PrintStream, NewImageManipulationsCmd>> knownCommands =
            new HashMap<>();

    knownCommands.put("blur", (a, o) -> new BlurImage(a[1], a[2]));
    knownCommands.put("sharpen", (a, o) -> new SharpenImage(a[1], a[2]));
    knownCommands.put("sepia", (a, o) -> new SepiaImage(a[1], a[2]));
    knownCommands.put("dither", (a, o) -> new DitherImage(a[1], a[2]));
    knownCommands.put("greyscale", (a, o) -> new CreateNewGreyscale(a[1], a[2]));

    NewImageManipulationsCmd c;
    BiFunction<String[], PrintStream, NewImageManipulationsCmd> cmd =
            knownCommands.getOrDefault(arr[0], null);
    if (cmd == null) {
      throw new IllegalArgumentException("Invalid command entered!");
    } else {
      c = cmd.apply(arr, outputStream);
      String str = c.getClass().getSimpleName();
      if (factory != null) {
        model = factory.getModel(fileExtension);
      }
      if (model == null) {
        throw new IllegalArgumentException("Model not loaded!");
      }
      boolean success = c.execute(model);
      if (success) {
        outputStream.print(str + " successful!\n");
      }
      return true;
    }
  }

  protected void printMenu(PrintStream out) {
    super.printMenu(out);
    out.print("blur sourceImage destinationImageName" + System.lineSeparator());
    out.print("sharpen sourceImage destinationImageName" + System.lineSeparator());
    out.print("sepia sourceImage destinationImageName" + System.lineSeparator());
    out.print("dither sourceImage destinationImageName" + System.lineSeparator());
    out.print("greyscale sourceImage destinationImageName" + System.lineSeparator());
  }

  @Override
  public void loadImage() {
    File f = view.getChosenFile();
    if (f == null){
      return;
    }

    String fileExtension = ImageUtil.getFileExtension(f.getName());
    String fileName = ImageUtil.getFileName(f.getName());
    ImageUtil.readFile(out, f.getAbsolutePath(), fileExtension);
    if (factory != null) {
      model = factory.getModel(fileExtension);
    }
    model.loadImage(f.getAbsolutePath(), fileName, out);
    view.setImageInPanel(new String[]{fileName}, new Pixels[]{model.getImageNameProperties(fileName)});
    view.clearAllInputFields();
  }

  @Override
  public void saveImage(String[] sourceImages) {
    for (String sourceImage : sourceImages) {
      File f = view.getFilesToSave();
      if (f == null){
        return;
      }

      String path = f.getAbsolutePath();
      String fileName = ImageUtil.getFileName(sourceImage);
      String fileExtension = ImageUtil.getFileExtension(f.getName());
      model.saveImage(path.toString(),fileName, out);
      ImageUtil.writeFile(fileExtension, path.toString(), out);
    }
  }

  private void setImageOnViewHelper(String[] destinationImages) {
    Pixels[] properties = new Pixels[destinationImages.length];
    for (int i=0; i<destinationImages.length; i++) {
      properties[i] = model.getImageNameProperties(destinationImages[i]);
    }

    view.setImageInPanel(destinationImages, properties);
  }

  @Override
  public void sepia(String[] sourceImages) {
    if (sourceImages == null) {
      view.showLoadInfoMessage();
      return;
    }
    String[] destinationImages = new String[sourceImages.length];
    for (int i=0; i<sourceImages.length; i++) {
      destinationImages[i] = sourceImages[i] + "-sepia";
      model.sepia(sourceImages[i], destinationImages[i]);
    }
    setImageOnViewHelper(destinationImages);
  }

  @Override
  public void dither(String[] sourceImages) {
    if (sourceImages == null) {
      view.showLoadInfoMessage();
      return;
    }
    String[] destinationImages = new String[sourceImages.length];
    for (int i=0; i<sourceImages.length; i++) {
      destinationImages[i] = sourceImages[i] + "-dither";
      model.dither(sourceImages[i], destinationImages[i]);
    }
    setImageOnViewHelper(destinationImages);
  }

  @Override
  public void blur(String[] sourceImages) {
    if (sourceImages == null) {
      view.showLoadInfoMessage();
      return;
    }
    String[] destinationImages = new String[sourceImages.length];
    for (int i=0; i<sourceImages.length; i++) {
      destinationImages[i] = sourceImages[i] + "-blur";
      model.blur(sourceImages[i], destinationImages[i]);
    }
    setImageOnViewHelper(destinationImages);
  }

  @Override
  public void sharpen(String[] sourceImages) {
    if (sourceImages == null) {
      view.showLoadInfoMessage();
      return;
    }
    String[] destinationImages = new String[sourceImages.length];
    for (int i=0; i<sourceImages.length; i++) {
      destinationImages[i] = sourceImages[i] + "-sharpen";
      model.sharpen(sourceImages[i], destinationImages[i]);
    }
    setImageOnViewHelper(destinationImages);
  }

  @Override
  public void horizontalFlip(String[] sourceImages) {
    if (sourceImages == null) {
      view.showLoadInfoMessage();
      return;
    }
    String[] destinationImages = new String[sourceImages.length];
    for (int i=0; i<sourceImages.length; i++) {
      destinationImages[i] = sourceImages[i] + "-horizontal_flip";
      model.horizontalFlip(sourceImages[i], destinationImages[i]);
    }
    setImageOnViewHelper(destinationImages);
  }

  @Override
  public void verticalFlip(String[] sourceImages) {
    if (sourceImages == null) {
      view.showLoadInfoMessage();
      return;
    }
    String[] destinationImages = new String[sourceImages.length];
    for (int i=0; i<sourceImages.length; i++) {
      destinationImages[i] = sourceImages[i] + "-vertical_flip";
      model.verticalFlip(sourceImages[i], destinationImages[i]);
    }
    setImageOnViewHelper(destinationImages);
  }

  @Override
  public void rgbSplit(String[] sourceImages) {
    if (sourceImages == null) {
      view.showLoadInfoMessage();
      return;
    }
    String[] destinationImages = new String[3];
    destinationImages[0] = sourceImages[0] + "-red";
    destinationImages[1] = sourceImages[0] + "-green";
    destinationImages[2] = sourceImages[0] + "-blue";

    model.rgbSplit(sourceImages[0], destinationImages[0],
            destinationImages[1], destinationImages[2]);
    setImageOnViewHelper(destinationImages);
    view.disableRGBSplitRadioButton();
  }

  @Override
  public void rgbCombine(String[] sourceImages) {
    if (sourceImages == null) {
      view.showLoadInfoMessage();
      return;
    }
    String destinationImage = sourceImages[0] + "-rgbCombine";

    model.rgbCombine(destinationImage, sourceImages[0], sourceImages[1], sourceImages[2]);
    setImageOnViewHelper(new String[]{destinationImage});
    view.enableRGBSplitRadioButton();
  }

  @Override
  public void brightenImage(int brightnessFactor, String[] sourceImages) {
    if (sourceImages == null) {
      view.showLoadInfoMessage();
      return;
    }

    String[] destinationImages = new String[sourceImages.length];
    for (int i=0; i<sourceImages.length; i++) {
      destinationImages[i] = sourceImages[i] + "-brighten";
      model.brighten(brightnessFactor, sourceImages[i], destinationImages[i]);
    }
    setImageOnViewHelper(destinationImages);
  }

  @Override
  public void greyscaleImage(String componentType, String[] sourceImages) {
    if (sourceImages == null) {
      view.showLoadInfoMessage();
      return;
    }

    String[] destinationImages = new String[sourceImages.length];
    for (int i=0; i<sourceImages.length; i++) {
      destinationImages[i] = sourceImages[i] + "-newGreyscale";
      model.createGreyScale(componentType, sourceImages[i], destinationImages[i]);
    }
    setImageOnViewHelper(destinationImages);
  }

  @Override
  public void createGreyscale(String[] sourceImages) {
    if (sourceImages == null) {
      view.showLoadInfoMessage();
      return;
    }

    String[] destinationImages = new String[sourceImages.length];
    for (int i=0; i<sourceImages.length; i++) {
      destinationImages[i] = sourceImages[i] + "-greyscale";
      model.createGreyScale(sourceImages[i], destinationImages[i]);
    }
    setImageOnViewHelper(destinationImages);
  }
}