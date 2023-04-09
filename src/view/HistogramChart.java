package view;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.DefaultDrawingSupplier;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.StandardXYBarPainter;
import org.jfree.chart.renderer.xy.XYBarRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.statistics.HistogramDataset;
import org.jfree.data.statistics.SimpleHistogramBin;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;


import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import utility.Pixels;

public class HistogramChart implements IHistogram {

  private Pixels properties;

  private Map<Integer, Integer> redFrequencyMap = new HashMap<>();
  private Map<Integer, Integer> greenFrequencyMap = new HashMap<>();
  private Map<Integer, Integer> blueFrequencyMap = new HashMap<>();
  private static final int BINS = 256;

  protected HistogramChart(Pixels properties) {
    this.properties = properties;

    for (int i = 0; i <= 256; i++) {
      this.redFrequencyMap.put(i, 0);
      this.greenFrequencyMap.put(i, 0);
      this.blueFrequencyMap.put(i, 0);
    }
  }

  @Override
  public ChartPanel createRGBChart(Pixels properties) {
    int index = 0;
      int width = properties.width;
      int height = properties.height;
      for (int y = 0; y < height; y++) {
        for (int x = 0; x < width; x++) {
          String[] arr = properties.listOfPixels[x][y].split(" ");
          this.redFrequencyMap.put(Integer.parseInt(arr[0]),
                  this.redFrequencyMap.get(Integer.parseInt(arr[0]))+1);
          this.greenFrequencyMap.put(Integer.parseInt(arr[1]),
                  this.greenFrequencyMap.get(Integer.parseInt(arr[1]))+1);
          this.blueFrequencyMap.put(Integer.parseInt(arr[2]),
                  this.blueFrequencyMap.get(Integer.parseInt(arr[2]))+1);
        }
      }
    final XYSeriesCollection dataset = new XYSeriesCollection( );
    final XYSeries redSeries = new XYSeries( "Red" );
    final XYSeries greenSeries = new XYSeries( "Green" );
    final XYSeries blueSeries = new XYSeries( "Blue" );
    for(int i = 0;i<=256;i++){
      redSeries.add(i, this.redFrequencyMap.get(i));
      greenSeries.add(i, this.greenFrequencyMap.get(i));
      blueSeries.add(i, this.blueFrequencyMap.get(i));
    }
    dataset.addSeries(redSeries);
    dataset.addSeries(greenSeries);
    dataset.addSeries(blueSeries);

    JFreeChart chart = ChartFactory.createXYLineChart("Histogram", "Pixel Value",
            "Frequency", (XYDataset) dataset, PlotOrientation.VERTICAL, true, true, false);
    XYPlot plot = (XYPlot) chart.getPlot();
    XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer( );
    renderer.setSeriesPaint( 0 , Color.RED );
    renderer.setSeriesPaint( 1 , Color.GREEN );
    renderer.setSeriesPaint( 2 , Color.BLUE );
    plot.setRenderer(renderer);
    ChartPanel panel = new ChartPanel(chart);
    return panel;
  }
}
