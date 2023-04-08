package view;

import utility.Pixels;

import java.io.File;
import java.io.IOException;

public interface IView {
    void addFeatures(Features features);

    File getChosenFile();

    File getFilesToSave();

    void setImageInPanel(String images[], Pixels[] properties);

    void disableRGBSplitRadioButton();

    void enableRGBSplitRadioButton();

    void showLoadInfoMessage();

    void showSaveSuccessMessage();

    void clearAllInputFields();

    /**
     * Reset the focus on the appropriate part of the view that has the keyboard listener attached to
     * it, so that keyboard events will still flow through.
     */
    void resetFocus();

    void setOriginalImage(File f);
}