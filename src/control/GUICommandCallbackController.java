package control;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;

import model.IImageManipulationsModelFactory;
import utility.ImageUtil;
import utility.Pixels;
import view.Features;
import view.IView;

/**
 * GUI Controller that extends from the text based controller and performs the GUI operations
 * making use of the model and view objects.
 */
public class GUICommandCallbackController extends ExtendedImageManipulationsControllerImpl
    implements Features {

  /**
   * Constructor.
   *
   * @param factory that decides the model object to return based on file extension.
   * @param v view that has to contains the feature object.
   * @param out Output stream object.
   * @param in Input stream object.
   */
  private final IView view;

  /**
   * Constructor.
   *
   * @param factory that decides the model object to return based on file extension.
   * @param out Output stream object.
   * @param in Input stream object.
   * @param v view object.
   */
  public GUICommandCallbackController(IImageManipulationsModelFactory factory,
                                      OutputStream out, InputStream in, IView v) {
    super(factory, out, in);
    this.factory = factory;
    this.view = v;
    view.addFeatures(this);
  }

  /**
   * Checks if alteast an image is loaded in panel before performing any operations.
   */
  private boolean isImageLoaded(String[] sourceImages) {
    if (sourceImages == null) {
      view.showLoadInfoMessage();
      return false;
    }

    return true;
  }

  @Override
  public void load(File f) {
    String fileExtension = ImageUtil.getFileExtension(f.getName());
    String fileName = ImageUtil.getFileName(f.getName());
    ImageUtil.readFile(out, f.getAbsolutePath(), fileExtension);
    if (factory != null) {
      model = factory.getModel(fileExtension);
    }
    model.loadImage(f.getAbsolutePath(), fileName, out);
    view.setImageInPanel(new String[]{fileName},
        new Pixels[]{model.getImageNameProperties(fileName)});
    view.clearAllInputFields();
    view.resetFocus();
    view.setOriginalImage(f);
    view.enableRGBSplitRadioButton();
  }

  @Override
  public void loadImage() {
    File f = view.getChosenFile();
    if (f == null) {
      return;
    }

    load(f);
  }

  @Override
  public void saveImage(String[] sourceImages) {
    if (!isImageLoaded(sourceImages)) {
      return;
    }

    for (String sourceImage : sourceImages) {
      File f = view.getFilesToSave();
      if (f == null) {
        return;
      }

      String path = f.getAbsolutePath();
      String fileName = ImageUtil.getFileName(sourceImage);
      String fileExtension = ImageUtil.getFileExtension(f.getName());

      // Requires user to enter a file name for save along with extension,
      // in case if he enters an invalid file extension name we display a message.
      if (view.showInvalidFileExtensionMessage(fileExtension)) {
        return;
      }

      if (factory != null) {
        model = factory.getModel(fileExtension);
      }
      model.saveImage(path, fileName, out);
      ImageUtil.writeFile(fileExtension, path, out);
    }

    view.resetFocus();
    view.showSaveSuccessMessage();
  }

  // Gets the image properties and sets them on the view panels for display.
  private void setImageOnViewHelper(String[] destinationImages) {
    Pixels[] properties = new Pixels[destinationImages.length];
    for (int i = 0; i < destinationImages.length; i++) {
      properties[i] = model.getImageNameProperties(destinationImages[i]);
    }

    view.setImageInPanel(destinationImages, properties);
  }

  @Override
  public void sepia(String[] sourceImages) {
    if (!isImageLoaded(sourceImages)) {
      return;
    }

    String[] destinationImages = new String[sourceImages.length];
    for (int i = 0; i < sourceImages.length; i++) {
      // We do not want user to enter a destination image name,
      // this is only for our reference and is required by the model.
      // Hence, we create a destination image name ourselves and pass it to the model.
      destinationImages[i] = sourceImages[i] + "-sepia";
      model.sepia(sourceImages[i], destinationImages[i]);
    }
    setImageOnViewHelper(destinationImages);
  }

  @Override
  public void dither(String[] sourceImages) {
    if (!isImageLoaded(sourceImages)) {
      return;
    }

    String[] destinationImages = new String[sourceImages.length];
    for (int i = 0; i < sourceImages.length; i++) {
      destinationImages[i] = sourceImages[i] + "-dither";
      model.dither(sourceImages[i], destinationImages[i]);
    }
    setImageOnViewHelper(destinationImages);
  }

  @Override
  public void blur(String[] sourceImages) {
    if (!isImageLoaded(sourceImages)) {
      return;
    }

    String[] destinationImages = new String[sourceImages.length];
    for (int i = 0; i < sourceImages.length; i++) {
      destinationImages[i] = sourceImages[i] + "-blur";
      model.blur(sourceImages[i], destinationImages[i]);
    }
    setImageOnViewHelper(destinationImages);
  }

  @Override
  public void sharpen(String[] sourceImages) {
    if (!isImageLoaded(sourceImages)) {
      return;
    }

    String[] destinationImages = new String[sourceImages.length];
    for (int i = 0; i < sourceImages.length; i++) {
      destinationImages[i] = sourceImages[i] + "-sharpen";
      model.sharpen(sourceImages[i], destinationImages[i]);
    }
    setImageOnViewHelper(destinationImages);
  }

  @Override
  public void horizontalFlip(String[] sourceImages) {
    if (!isImageLoaded(sourceImages)) {
      return;
    }

    String[] destinationImages = new String[sourceImages.length];
    for (int i = 0; i < sourceImages.length; i++) {
      destinationImages[i] = sourceImages[i] + "-horizontal_flip";
      model.horizontalFlip(sourceImages[i], destinationImages[i]);
    }
    setImageOnViewHelper(destinationImages);
  }

  @Override
  public void verticalFlip(String[] sourceImages) {
    if (!isImageLoaded(sourceImages)) {
      return;
    }

    String[] destinationImages = new String[sourceImages.length];
    for (int i = 0; i < sourceImages.length; i++) {
      destinationImages[i] = sourceImages[i] + "-vertical_flip";
      model.verticalFlip(sourceImages[i], destinationImages[i]);
    }
    setImageOnViewHelper(destinationImages);
  }

  @Override
  public void rgbSplit(String[] sourceImages) {
    if (!isImageLoaded(sourceImages)) {
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
    if (!isImageLoaded(sourceImages)) {
      return;
    }

    String destinationImage = sourceImages[0] + "-rgbCombine";

    model.rgbCombine(destinationImage, sourceImages[0], sourceImages[1], sourceImages[2]);
    setImageOnViewHelper(new String[]{destinationImage});
    view.enableRGBSplitRadioButton();
  }

  @Override
  public void brightenImage(int brightnessFactor, String[] sourceImages) {
    if (!isImageLoaded(sourceImages)) {
      return;
    }

    String[] destinationImages = new String[sourceImages.length];
    for (int i = 0; i < sourceImages.length; i++) {
      destinationImages[i] = sourceImages[i] + "-brighten";
      model.brighten(brightnessFactor, sourceImages[i], destinationImages[i]);
    }
    setImageOnViewHelper(destinationImages);
  }

  @Override
  public void greyscaleImage(String componentType, String[] sourceImages) {
    if (!isImageLoaded(sourceImages)) {
      return;
    }

    String[] destinationImages = new String[sourceImages.length];
    for (int i = 0; i < sourceImages.length; i++) {
      destinationImages[i] = sourceImages[i] + "-newGreyscale";
      model.createGreyScale(componentType, sourceImages[i], destinationImages[i]);
    }
    setImageOnViewHelper(destinationImages);
  }

  @Override
  public void createGreyscale(String[] sourceImages) {
    if (!isImageLoaded(sourceImages)) {
      return;
    }

    String[] destinationImages = new String[sourceImages.length];
    for (int i = 0; i < sourceImages.length; i++) {
      destinationImages[i] = sourceImages[i] + "-greyscale";
      model.createGreyScale(sourceImages[i], destinationImages[i]);
    }
    setImageOnViewHelper(destinationImages);
  }
}