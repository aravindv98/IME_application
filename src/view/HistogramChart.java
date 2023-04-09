package view;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.DefaultDrawingSupplier;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.StandardXYBarPainter;
import org.jfree.chart.renderer.xy.XYBarRenderer;
import org.jfree.data.statistics.HistogramDataset;

import java.awt.*;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.PropertyResourceBundle;

import utility.Pixels;

public class HistogramChart implements IHistogram {

  private Pixels properties;
  private double[] redValues;
  private double[] greenValues;
  private double[] blueValues;
  private static final int BINS = 256;

  protected HistogramChart(Pixels properties) {
    this.properties = properties;
  }

  @Override
  public ChartPanel createRGBChart(Pixels properties) {
    HistogramDataset dataset = new HistogramDataset();
    int index = 0;
      int width = properties.width;
      int height = properties.height;
      this.redValues = new double[width*height];
      this.greenValues = new double[width*height];
      this.blueValues = new double[width*height];
      for (int y = 0; y < height; y++) {
        for (int x = 0; x < width; x++) {
          String[] arr = properties.listOfPixels[x][y].split(" ");
          this.redValues[index] = Double.parseDouble(arr[0]);
          this.greenValues[index] = Double.parseDouble(arr[1]);
          this.blueValues[index] = Double.parseDouble(arr[2]);
          index+=1;
        }
      }
    dataset.addSeries("Red",this.redValues,BINS);
    dataset.addSeries("Green",this.greenValues,BINS);
    dataset.addSeries("Blue",this.blueValues,BINS);
    JFreeChart chart = ChartFactory.createHistogram("Histogram", "Value",
            "Count", dataset, PlotOrientation.VERTICAL, true, true, false);
    XYPlot plot = (XYPlot) chart.getPlot();
    XYBarRenderer renderer = (XYBarRenderer) plot.getRenderer();
    renderer.setBarPainter(new StandardXYBarPainter());
    // translucent red, green & blue
    Paint[] paintArray = {
            new Color(0x80ff0000, true),
            new Color(0x8000ff00, true),
            new Color(0x800000ff, true)
    };
    plot.setDrawingSupplier(new DefaultDrawingSupplier(
            paintArray,
            DefaultDrawingSupplier.DEFAULT_FILL_PAINT_SEQUENCE,
            DefaultDrawingSupplier.DEFAULT_OUTLINE_PAINT_SEQUENCE,
            DefaultDrawingSupplier.DEFAULT_STROKE_SEQUENCE,
            DefaultDrawingSupplier.DEFAULT_OUTLINE_STROKE_SEQUENCE,
            DefaultDrawingSupplier.DEFAULT_SHAPE_SEQUENCE));
    ChartPanel panel = new ChartPanel(chart);
    panel.setMouseWheelEnabled(true);
    return panel;
  }
}
