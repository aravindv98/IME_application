package view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Enumeration;
import java.util.Objects;
import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;
import org.jfree.chart.ChartPanel;
import utility.Pixels;

/**
 * Main view class used to implement the view functionalities.
 */
public class JFrameView extends JFrame implements IView {

  private final JPanel imagePanel;
  private final JTextField textField;
  private final JComboBox<String> dropdown;
  private final JButton brightnessButton;
  private JButton fileSaveButton;
  private JButton fileOpenButton;
  private JButton fileReloadButton;
  private JPanel radioPanel;
  private ButtonGroup buttonGroup;
  private String[] currentImages;
  private File originalImage;
  private IHistogram histogram;

  /**
   * Constructor.
   */
  public JFrameView() {
    super();
    setTitle("ImageManipulationApplication");
    setSize(400, 400);

    JPanel mainPanel = new JPanel();
    //for elements to be arranged vertically within this panel
    mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.PAGE_AXIS));
    //scroll bars around this main panel
    JScrollPane mainScrollPane = new JScrollPane(mainPanel);
    add(mainScrollPane);

    JPanel dialogBoxesPanel = new JPanel();

    //file open
    addFileOpenButton(dialogBoxesPanel);

    //file save
    addFileSaveButton(dialogBoxesPanel);

    // Reload to original
    addReloadButton(dialogBoxesPanel);

    mainPanel.add(dialogBoxesPanel);

    //Radio-button
    addRadioButtons();

    mainPanel.add(radioPanel);

    // Contains the text field for brightness and combobox for greyscale.
    JPanel customPanel = new JPanel();
    customPanel.setBorder(BorderFactory.createTitledBorder("Custom Image Operations"));
    customPanel.setLayout(new BoxLayout(customPanel, BoxLayout.Y_AXIS));

    JPanel brightnessPanel = new JPanel();
    brightnessPanel.setLayout(new FlowLayout());
    JLabel brightnessText = new JLabel("Enter the brightness factor:");
    textField = new JTextField(20);
    ((AbstractDocument) textField.getDocument()).setDocumentFilter(new SignedNumberOnlyFilter());
    textField.setText(String.valueOf(0));
    brightnessButton = new JButton("Set brightness");
    brightnessPanel.add(brightnessText);
    brightnessPanel.add(textField);
    brightnessPanel.add(brightnessButton);
    customPanel.add(brightnessPanel);

    JPanel greyscalePanel = new JPanel();
    greyscalePanel.setLayout(new FlowLayout());
    JLabel greyscaleText = new JLabel("Select the greyscale component type:");
    String[] options = {"none", "red-component", "green-component", "blue-component",
        "value-component",
        "intensity-component", "luma-component"};
    dropdown = new JComboBox<>(options);
    greyscalePanel.add(greyscaleText);
    greyscalePanel.add(dropdown);
    customPanel.add(greyscalePanel);
    mainPanel.add(customPanel);

    //show an image with a scrollbar
    imagePanel = new JPanel();
    //a border around the panel with a caption
    imagePanel.setBorder(BorderFactory.createTitledBorder("Showing images"));
    imagePanel.setLayout(new GridLayout(1, 0, 10, 10));
    mainPanel.add(imagePanel);

    // Create an initial blank panel
    addInitialBlankPanel();
    this.histogram = null;
    resetFocus();
    setVisible(true);
  }

  private void addFileOpenButton(JPanel dialogBoxesPanel) {
    JPanel fileopenPanel = new JPanel();
    fileopenPanel.setLayout(new FlowLayout());
    dialogBoxesPanel.add(fileopenPanel);
    fileOpenButton = new JButton("Open a file");
    fileOpenButton.setActionCommand("Open file");
    fileopenPanel.add(fileOpenButton);
  }

  private void addFileSaveButton(JPanel dialogBoxesPanel) {
    JPanel filesavePanel = new JPanel();
    filesavePanel.setLayout(new FlowLayout());
    dialogBoxesPanel.add(filesavePanel);
    fileSaveButton = new JButton("Save a file");
    fileSaveButton.setActionCommand("Save file");
    filesavePanel.add(fileSaveButton);
  }

  private void addReloadButton(JPanel dialogBoxesPanel) {
    JPanel fileReloadPanel = new JPanel();
    fileReloadPanel.setLayout(new FlowLayout());
    dialogBoxesPanel.add(fileReloadPanel);
    fileReloadButton = new JButton("Reload to original image");
    fileReloadButton.setActionCommand("Reload");
    fileReloadPanel.add(fileReloadButton);
  }

  private void addRadioButtons() {
    radioPanel = new JPanel(new GridLayout(0, 4));
    radioPanel.setBorder(BorderFactory.createTitledBorder("Image Operations"));
    buttonGroup = new ButtonGroup();

    addRadioButton("sepia");
    addRadioButton("dither");
    addRadioButton("blur");
    addRadioButton("sharpen");
    addRadioButton("horizontal-flip");
    addRadioButton("vertical-flip");
    addRadioButton("rgb-split");
    addRadioButton("rgb-combine");
    addRadioButton("create-greyscale");
  }

  private void addInitialBlankPanel() {
    JLabel[] imageLabel = new JLabel[1];
    JScrollPane[] imageScrollPane = new JScrollPane[1];

    imageLabel[0] = new JLabel();
    imageScrollPane[0] = new JScrollPane(imageLabel[0]);
    imageLabel[0].setText("Please load an image using the 'Open a file' button!");
    imageLabel[0].setHorizontalAlignment(SwingConstants.CENTER); // Center the text
    imageLabel[0].setFont(new Font("Arial", Font.PLAIN, 24)); // Increase the font size
    imageScrollPane[0].setPreferredSize(new Dimension(100, 450));
    imagePanel.add(imageScrollPane[0]);
  }

  @Override
  public void clearAllInputFields() {
    buttonGroup.clearSelection();
    textField.setText(String.valueOf(0));
    if (!Objects.equals(dropdown.getItemAt(0), "none")) {
      dropdown.insertItemAt("none", 0);
    }

    dropdown.setSelectedIndex(0);
  }

  @Override
  public void resetFocus() {
    this.setFocusable(true);
    this.requestFocus();
  }

  @Override
  public void setOriginalImage(File image) {
    this.originalImage = image;
  }

  @Override
  public boolean showInvalidFileExtensionMessage(String fileExtension) {
    if (!fileExtension.isEmpty() && (fileExtension.equals("jpg") || fileExtension.equals("jpeg")
        || fileExtension.equals("ppm") || fileExtension.equals("bmp") || fileExtension.equals(
        "png"))) {
      return false;
    }

    JOptionPane.showMessageDialog(null, "Please provide a valid file extension, "
            + "supported extensions are (.png), (.jpg), (.bmp), (.jpeg), (.ppm)",
        "Message", JOptionPane.INFORMATION_MESSAGE);
    return true;
  }

  private void addRadioButton(String optionName) {
    JRadioButton radioButton = new JRadioButton(optionName);
    radioButton.setActionCommand(optionName);
    buttonGroup.add(radioButton);
    radioPanel.add(radioButton);
  }

  @Override
  public void addFeatures(Features features) throws IllegalArgumentException {

    addKeyPressListeners(features);

    fileOpenButton.addActionListener(evt -> features.loadImage());
    fileReloadButton.addActionListener(evt -> features.load(originalImage));
    fileSaveButton.addActionListener(evt -> features.saveImage(this.currentImages));
    brightnessButton.addActionListener(evt -> features.brightenImage(
        Integer.parseInt(textField.getText()),
        this.currentImages));

    dropdown.addActionListener(e -> {
      if (dropdown.getSelectedIndex() != 0) {
        features.greyscaleImage(
            dropdown.getSelectedItem().toString(),
            currentImages);
        if (Objects.equals(dropdown.getItemAt(0), "none")) {
          dropdown.removeItemAt(0);
        }
      }
    });

    Enumeration<AbstractButton> buttons = buttonGroup.getElements();
    while (buttons.hasMoreElements()) {
      AbstractButton radioButton = buttons.nextElement();
      switch (radioButton.getActionCommand()) {
        case "sepia":
          radioButton.addActionListener(evt -> features.sepia(this.currentImages));
          break;
        case "dither":
          radioButton.addActionListener(evt -> features.dither(this.currentImages));
          break;
        case "blur":
          radioButton.addActionListener(evt -> features.blur(this.currentImages));
          break;
        case "sharpen":
          radioButton.addActionListener(evt -> features.sharpen(this.currentImages));
          break;
        case "horizontal-flip":
          radioButton.addActionListener(evt -> features.horizontalFlip(this.currentImages));
          break;
        case "vertical-flip":
          radioButton.addActionListener(evt -> features.verticalFlip(this.currentImages));
          break;
        case "rgb-split":
          radioButton.addActionListener(evt -> features.rgbSplit(this.currentImages));
          break;
        case "rgb-combine":
          radioButton.addActionListener(evt -> features.rgbCombine(this.currentImages));
          break;
        case "create-greyscale":
          radioButton.addActionListener(evt -> features.createGreyscale(this.currentImages));
          break;
        default:
          throw new IllegalArgumentException("Invalid feature added!");
      }
    }
  }

  /**
   * We are also supporting key events such as what happens if load button is on focus, and we press
   * enter. Or if we press enter after entering values in text field.
   */
  private void addKeyPressListeners(Features features) {
    fileOpenButton.addKeyListener(new KeyAdapter() {
      public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
          features.loadImage();
        }
      }
    });

    fileReloadButton.addKeyListener(new KeyAdapter() {
      public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
          features.load(originalImage);
        }
      }
    });

    fileSaveButton.addKeyListener(new KeyAdapter() {
      public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
          features.saveImage(currentImages);
        }
      }
    });

    textField.addKeyListener(new KeyAdapter() {
      public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
          brightnessButton.doClick(); // Simulate a click on the button
        }
      }
    });
    brightnessButton.addKeyListener(new KeyAdapter() {
      public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
          features.brightenImage(
              Integer.parseInt(textField.getText()),
              currentImages);
        }
      }
    });
  }

  @Override
  public File getChosenFile() {
    File f = null;
    final JFileChooser fchooser = new JFileChooser(".");
    FileNameExtensionFilter filter = new FileNameExtensionFilter(
        "JPG, JPEG, PNG, PPM, BMP Images", "jpg", "jpeg", "png", "ppm", "bmp");
    fchooser.setFileFilter(filter);
    int retvalue = fchooser.showOpenDialog(JFrameView.this);
    if (retvalue == JFileChooser.APPROVE_OPTION) {
      f = fchooser.getSelectedFile();
    }
    return f;
  }

  @Override
  public File getFilesToSave() {
    File selectedFile = null;
    final JFileChooser fchooser = new JFileChooser(".");
    int retvalue = fchooser.showSaveDialog(JFrameView.this);
    if (retvalue == JFileChooser.APPROVE_OPTION) {
      selectedFile = fchooser.getSelectedFile();
    }

    return selectedFile;
  }

  @Override
  public void setImageInPanel(String[] imageNames, Pixels[] properties) {
    this.currentImages = imageNames;
    JLabel[] imageLabel = new JLabel[properties.length];
    JScrollPane[] imageScrollPane = new JScrollPane[properties.length];

    imagePanel.removeAll();
    for (int i = 0; i < imageLabel.length; i++) {
      int width = properties[i].width;
      int height = properties[i].height;

      imageLabel[i] = new JLabel();

      // Create a new label to hold the image
      BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
      for (int y = 0; y < height; y++) {
        for (int x = 0; x < width; x++) {
          String[] arr = properties[i].listOfPixels[x][y].split(" ");
          Color temp = new Color(Integer.parseInt(arr[0]),
              Integer.parseInt(arr[1]),
              Integer.parseInt(arr[2]));
          image.setRGB(x, y, temp.getRGB());
        }
      }

      imageLabel[i] = new JLabel(new ImageIcon(image));
      imageScrollPane[i] = new JScrollPane(imageLabel[i]);
      imageScrollPane[i].setPreferredSize(new Dimension(100, 600));
      imagePanel.add(imageScrollPane[i]);

      this.histogram = new HistogramChart();
      ChartPanel chart = this.histogram.createRGBChart(properties[i]);
      imagePanel.add(chart);

      // Tell the panel to redraw itself
      imagePanel.revalidate();
      imagePanel.repaint();
    }
  }

  @Override
  public void disableRGBSplitRadioButton() {
    Enumeration<AbstractButton> buttons = buttonGroup.getElements();
    while (buttons.hasMoreElements()) {
      AbstractButton radioButton = buttons.nextElement();
      if (Objects.equals(radioButton.getActionCommand(), "rgb-split")) {
        radioButton.setEnabled(false);
      }
    }
  }

  @Override
  public void enableRGBSplitRadioButton() {
    Enumeration<AbstractButton> buttons = buttonGroup.getElements();
    while (buttons.hasMoreElements()) {
      AbstractButton radioButton = buttons.nextElement();
      if (Objects.equals(radioButton.getActionCommand(), "rgb-split")) {
        radioButton.setEnabled(true);
      }
    }
  }

  @Override
  public void showLoadInfoMessage() {
    clearAllInputFields();
    JOptionPane.showMessageDialog(null,
        "Please load an image using the 'Open a file' button!",
        "Message", JOptionPane.INFORMATION_MESSAGE);
  }

  @Override
  public void showSaveSuccessMessage() {
    JOptionPane.showMessageDialog(null, "Image saved successfully",
        "Message", JOptionPane.INFORMATION_MESSAGE);
  }

  /**
   * Applies filter on the text field for brightness so that it only allows entering signed
   * numbers.
   */
  public class SignedNumberOnlyFilter extends DocumentFilter {

    @Override
    public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr)
        throws BadLocationException {
      StringBuilder builder = new StringBuilder(string);
      for (int i = builder.length() - 1; i >= 0; i--) {
        int cp = builder.codePointAt(i);
        if (!Character.isDigit(cp) && cp != '-' || i == 0 && cp == '-') {
          builder.deleteCharAt(i);
          if (cp == '-') {
            builder.insert(0, '-');
          }
        }
      }
      super.insertString(fb, offset, builder.toString(), attr);
    }

    @Override
    public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs)
        throws BadLocationException {
      if (text != null) {
        StringBuilder builder = new StringBuilder(text);
        for (int i = builder.length() - 1; i >= 0; i--) {
          int cp = builder.codePointAt(i);
          if (!Character.isDigit(cp) && cp != '-' || i == 0 && cp == '-') {
            builder.deleteCharAt(i);
            if (cp == '-') {
              builder.insert(0, '-');
            }
          }
        }
        text = builder.toString();
      }
      super.replace(fb, offset, length, text, attrs);
    }
  }
}