package de.lighti.components.map;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.CrosshairState;
import org.jfree.chart.plot.PlotRenderingInfo;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYDotRenderer;
import org.jfree.chart.renderer.xy.XYItemRendererState;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import de.lighti.model.Statics;

/**
 * The MapCanvasComponent renders different variations of the Dota2 minimap
 * along with a series of markers. The component has a fixed size of 512x512.
 *
 * @author TobiasMahlmann
 *
 */
public class MapCanvasComponent extends ChartPanel {
    private static class CustomXYDotRenderer extends XYDotRenderer {
        private Integer itemIndex = null;

        @Override
        public void drawItem( Graphics2D arg0, XYItemRendererState arg1, Rectangle2D arg2, PlotRenderingInfo arg3, XYPlot arg4, ValueAxis arg5, ValueAxis arg6,
                        XYDataset arg7, int arg8, int arg9, CrosshairState arg10, int arg11 ) {
            if (itemIndex == null || itemIndex == arg9) {
                super.drawItem( arg0, arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11 );
            }
        }

    }

    /**
     *
     */
    private static final long serialVersionUID = 2077175805479363567L;
    private static final int DEFAULT_DOT_SIZE = 3;
    private BufferedImage minimap;

    private BufferedImage minimapModel;
    private final String MINIMAP_FILE = "Minimap.jpg";

    private final String MINIMAP_MODEL_FILE = "Mapmodel.png";

    private boolean paintMapModel;

    /**
     * Default constructor.
     */
    public MapCanvasComponent() {
        super( new JFreeChart( new XYPlot( new XYSeriesCollection(), new NumberAxis( "X" ), new NumberAxis( "Y" ), new CustomXYDotRenderer() ) ) );

        final Dimension size = new Dimension( 512, 512 );
        setMinimumSize( size );
        setMaximumSize( size );
        setPreferredSize( size );
        setPopupMenu( null );

        final XYPlot plot = (XYPlot) getChart().getPlot();
        resetDotSize();
        plot.getRangeAxis().setVisible( false );
        plot.getDomainAxis().setVisible( false );
        getChart().getLegend().setVisible( false );
        plot.setDomainGridlinesVisible( false );
        plot.setRangeGridlinesVisible( false );
        plot.setDomainPannable( false );
        plot.setRangePannable( false );
        plot.setBackgroundImageAlpha( 1f );
        ((NumberAxis) plot.getDomainAxis()).setRange( 64, 64 + 128 );
        ((NumberAxis) plot.getRangeAxis()).setRange( 64, 64 + 128 );

        paintMapModel = false;

        try {
            URL url = MapCanvasComponent.class.getResource( MINIMAP_FILE );
            minimap = ImageIO.read( url );
            getChart().getPlot().setBackgroundImage( minimap );

            url = MapCanvasComponent.class.getResource( MINIMAP_MODEL_FILE );
            minimapModel = ImageIO.read( url );
        }
        catch (final IOException e) {
            JOptionPane.showMessageDialog( this, e.getLocalizedMessage(), "Error loading minimap", JOptionPane.ERROR_MESSAGE );
        }
    }

    public void addSeries( XYSeries series ) {
        addSeries( series, null );
    }

    public void addSeries( XYSeries series, Integer index ) {
        final XYSeriesCollection s = (XYSeriesCollection) ((XYPlot) getChart().getPlot()).getDataset();
        s.addSeries( series );

        //If we have a colour for a player. Otherwise let the component handle it
        if (index != null) {
            ((XYPlot) getChart().getPlot()).getRenderer().setSeriesPaint( s.getSeriesIndex( series.getKey() ), Statics.PLAYER_COLOURS[index] );
        }
    }

    /**
     * @return the pencil size for painting the markers
     */
    public int getDotSize() {
        return ((XYDotRenderer) ((XYPlot) getChart().getPlot()).getRenderer()).getDotHeight();
    }

    public int getItemCount() {
        final XYSeriesCollection s = (XYSeriesCollection) ((XYPlot) getChart().getPlot()).getDataset();
        if (s.getSeriesCount() == 0) {
            return 0;
        }
        else {
            return s.getSeries( 0 ).getItemCount();
        }
    }

    public XYSeriesCollection getMarkers() {
        return (XYSeriesCollection) ((XYPlot) getChart().getPlot()).getDataset();
    }

    /**
     * @return true if the zone map is currently displayed
     */
    public boolean isPaintMapModel() {
        return paintMapModel;
    }

    public void removeSeries( String series ) {
        final XYSeriesCollection s = (XYSeriesCollection) ((XYPlot) getChart().getPlot()).getDataset();
        final XYSeries xys = s.getSeries( series );

        //If we remove a series, the series paints don't seem to get updated
        final XYDotRenderer renderer = (XYDotRenderer) ((XYPlot) getChart().getPlot()).getRenderer();
        final int index = s.getSeriesIndex( xys.getKey() );
        for (int i = index; i < s.getSeriesCount() - 1; i++) {
            renderer.setSeriesPaint( i, renderer.getSeriesPaint( i + 1 ) );
        }

        s.removeSeries( xys );

    }

    /**
     * Sets the marker size to the default value
     */
    public void resetDotSize() {
        setDotSize( DEFAULT_DOT_SIZE );
    }

    public void resetTimeMarker() {
        setTimeMarker( -1 );
    }

    /**
     * Sets the pencil size for each marker. If set to a value > 1,
     * the MapCanvasComponent will colour more pixels around the the actual
     * marker, depending on the pencil size, to highlight the marker.
     * @param dotSize
     */
    public void setDotSize( int dotSize ) {
        ((XYDotRenderer) ((XYPlot) getChart().getPlot()).getRenderer()).setDotHeight( dotSize );
        ((XYDotRenderer) ((XYPlot) getChart().getPlot()).getRenderer()).setDotWidth( dotSize );
    }

    /**
     * Set this to true if the component should render the annotated zone map
     * instead of the schematic representation of the game map.
     * @param paintMapModel
     */
    public void setPaintMapModel( boolean paintMapModel ) {
        this.paintMapModel = paintMapModel;
        if (paintMapModel) {
            getChart().getPlot().setBackgroundImage( minimapModel );
        }
        else {
            getChart().getPlot().setBackgroundImage( minimap );
        }
    }

    public void setTimeMarker( int x ) {
        if (x >= 0) {
            ((CustomXYDotRenderer) ((XYPlot) getChart().getPlot()).getRenderer()).itemIndex = x;
        }
        else {
            ((CustomXYDotRenderer) ((XYPlot) getChart().getPlot()).getRenderer()).itemIndex = null;
        }
        repaint();
    }

}
