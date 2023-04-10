package view;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;


import java.awt.Color;
import java.util.HashMap;
import java.util.Map;

import utility.Pixels;

/**
 * A class that implements IHistogram interface to generate the line
 * chart for a any given image.
 */
public class HistogramChart implements IHistogram {

  private final Map<Integer, Integer> redFrequencyMap = new HashMap<>();
  private final Map<Integer, Integer> greenFrequencyMap = new HashMap<>();
  private final Map<Integer, Integer> blueFrequencyMap = new HashMap<>();

  private final Map<Integer, Integer> intensityFrequencyMap = new HashMap<>();

  protected HistogramChart() {

    for (int i = 0; i <= 256; i++) {
      this.redFrequencyMap.put(i, 0);
      this.greenFrequencyMap.put(i, 0);
      this.blueFrequencyMap.put(i, 0);
      this.intensityFrequencyMap.put(i, 0);
    }
  }

  @Override
  public ChartPanel createRGBChart(Pixels properties) {
    final XYSeriesCollection dataset = getXySeriesCollection(properties);

    JFreeChart chart = ChartFactory.createXYLineChart("Histogram",
            "Pixel Value",
            "Frequency", (XYDataset) dataset,
            PlotOrientation.VERTICAL, true, true, false);
    XYPlot plot = (XYPlot) chart.getPlot();
    XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
    renderer.setSeriesPaint(0, Color.RED);
    renderer.setSeriesPaint(1, Color.GREEN);
    renderer.setSeriesPaint(2, Color.BLUE);
    renderer.setSeriesPaint(3, Color.GRAY);
    renderer.setSeriesShapesVisible(0, false);
    renderer.setSeriesShapesVisible(1, false);
    renderer.setSeriesShapesVisible(2, false);
    renderer.setSeriesShapesVisible(3, false);
    plot.setRenderer(renderer);
    ChartPanel panel = new ChartPanel(chart);
    return panel;
  }

  private XYSeriesCollection getXySeriesCollection(Pixels properties) {
    int width = properties.width;
    int height = properties.height;
    for (int y = 0; y < height; y++) {
      for (int x = 0; x < width; x++) {

        String[] arr = properties.listOfPixels[x][y].split(" ");
        int intensityValue = (Integer.parseInt(arr[0]) + Integer.parseInt(arr[1])
                + Integer.parseInt(arr[2])) / 3;
        this.redFrequencyMap.put(Integer.parseInt(arr[0]),
                this.redFrequencyMap.get(Integer.parseInt(arr[0])) + 1);
        this.greenFrequencyMap.put(Integer.parseInt(arr[1]),
                this.greenFrequencyMap.get(Integer.parseInt(arr[1])) + 1);
        this.blueFrequencyMap.put(Integer.parseInt(arr[2]),
                this.blueFrequencyMap.get(Integer.parseInt(arr[2])) + 1);
        this.intensityFrequencyMap.put(intensityValue,
                this.intensityFrequencyMap.get(intensityValue) + 1);
      }
    }
    final XYSeriesCollection dataset = new XYSeriesCollection();
    final XYSeries redSeries = new XYSeries("Red");
    final XYSeries greenSeries = new XYSeries("Green");
    final XYSeries blueSeries = new XYSeries("Blue");
    final XYSeries intensitySeries = new XYSeries("Intensity");
    for (int i = 0; i <= 256; i++) {
      redSeries.add(i, this.redFrequencyMap.get(i));
      greenSeries.add(i, this.greenFrequencyMap.get(i));
      blueSeries.add(i, this.blueFrequencyMap.get(i));
      intensitySeries.add(i, this.intensityFrequencyMap.get(i));
    }
    dataset.addSeries(redSeries);
    dataset.addSeries(greenSeries);
    dataset.addSeries(blueSeries);
    dataset.addSeries(intensitySeries);
    return dataset;
  }
}
