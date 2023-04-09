package view;

import utility.Pixels;

import java.io.File;

/**
 * An interface that would contain methods for the view.
 */
public interface IView {

    /**
     * Adds features to the buttons.
     * @param features the features object.
     */
    void addFeatures(Features features);

    /**
     * Choose file to load.
     */
    File getChosenFile();

    /**
     * Choose file path and name to save.
     */
    File getFilesToSave();

    /**
     * Sets images in the panel.
     * @param images to be set to the panel.
     * @param properties of images as input.
     *
     */
    void setImageInPanel(String images[], Pixels[] properties);

    /**
     * Disables RGB split operation after once, as we want to do it only once.
     */
    void disableRGBSplitRadioButton();

    /**
     * Enables RGB split operation mainly after combine or load etc.
     */
    void enableRGBSplitRadioButton();

    /**
     * Shows message to user if we want to perform an operation before load.
     */
    void showLoadInfoMessage();

    /**
     * Shows message to user after successful save operation.
     */
    void showSaveSuccessMessage();

    /**
     * Clears all input fields.
     */
    void clearAllInputFields();

    /**
     * Reset the focus on the appropriate part of the view that has the keyboard listener attached to
     * it, so that keyboard events will still flow through.
     */
    void resetFocus();

    /**
     * Used by reload functionality, that allows users to get back the last loaded image.
     * @param f file that needs to be loaded.
     */
    void setOriginalImage(File f);

    /**
     * Shows message to users in case if they pass an invalid file extension.
     * @param extension file extension passed by user.
     */
    boolean showInvalidFileExtensionMessage(String extension);
}