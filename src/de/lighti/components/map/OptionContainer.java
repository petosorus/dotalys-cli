package de.lighti.components.map;

import java.awt.FlowLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileFilter;

import de.lighti.io.DataExporter;
import de.lighti.model.AppState;
import de.lighti.model.Statics;

public class OptionContainer extends JComponent {
    /**
     *
     */
    private static final long serialVersionUID = -7333865570478624736L;
    private final MapComponent mapComponent;
    private final AppState appState;

    private JButton exportButton;
    private JCheckBox allButton;
    private JSlider stepSlider;
    private JButton playButton;
    private JButton stopButton;
    private TimerTask animation;

    public OptionContainer( final MapComponent mapComponent, AppState appState ) {
        super();
        this.mapComponent = mapComponent;
        this.appState = appState;

        setLayout( new FlowLayout( FlowLayout.LEFT ) );

        add( getAllButton() );
        add( getExportButton() );
        add( getStepSlider() );
        add( getPlayButton() );
        add( getStopButton() );

        final JButton toggleButton = new JButton( Statics.MAP_ZONES );
        toggleButton.addActionListener( new ActionListener() {

            @Override
            public void actionPerformed( ActionEvent e ) {
                mapComponent.getMapCanvas().setPaintMapModel( !mapComponent.getMapCanvas().isPaintMapModel() );
                mapComponent.getMapCanvas().repaint();
            }
        } );
        add( toggleButton );

    }

    private ActionListener createExportButtonActionListener() {
        return new ActionListener() {
            @Override
            public void actionPerformed( ActionEvent e ) {
//                final DefaultMutableTreeNode node = (DefaultMutableTreeNode) mapComponent.getAttributeTree().getLastSelectedPathComponent();
//                if (node != null && node.isLeaf()) {
//                    final String selection = (String) node.getUserObject();
//                    final Player p = appState.getPlayerByName( selection );
//                    if (p != null) {
//                        final DefaultMutableTreeNode category = (DefaultMutableTreeNode) node.getParent();
//                        final String catName = (String) category.getUserObject();
//                        switch (catName) {
//                            case MapComponent.CAT_MOVEMENT:
//                                final String[][] log = ChartCreator.createMoveLog( p.getName(), appState );
//                                doSaveDialog( catName, log );
//                                break;
//                            default:
//                                JOptionPane.showMessageDialog( OptionContainer.this, "Exporting " + catName + " is not implemented", "We're terribly sorry",
//                                                JOptionPane.ERROR_MESSAGE );
//                        }
//                    }
//                }
                JOptionPane.showMessageDialog( OptionContainer.this,
                                "This button is disabled until I have time to fix it. Blease use the batch export instead." );
            }
        };
    }

    void doSaveDialog( String category, String[][] data ) {
        // Create a file chooser
        final JFileChooser fc = new JFileChooser( "." );
        fc.setFileFilter( new FileFilter() {

            @Override
            public boolean accept( File f ) {
                return f.isDirectory() || f.getName().endsWith( ".csv" );
            }

            @Override
            public String getDescription() {
                return "comma-separated values (*.csv)";
            }
        } );

        final int returnVal = fc.showSaveDialog( this );
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            try {
                String header = "# <unknown data>";
                switch (category) {
                    case MapComponent.CAT_MOVEMENT:
                        header = "#tickms, x , y";
                        break;
                    default:
                        break;
                }
                DataExporter.exportCSV( fc.getSelectedFile(), header, data );
            }
            catch (final IOException e) {
                JOptionPane.showMessageDialog( this, e.getLocalizedMessage(), "We're terribly sorry", JOptionPane.ERROR_MESSAGE );
            }
        }
    }

    public JCheckBox getAllButton() {
        if (allButton == null) {
            allButton = new JCheckBox( Statics.ALL );
            allButton.setEnabled( false );
            allButton.addChangeListener( new ChangeListener() {

                @Override
                public void stateChanged( ChangeEvent e ) {
                    final boolean allValue = allButton.isSelected();
                    getStepSlider().setEnabled( !allValue );
                    if (allValue) {
                        mapComponent.getMapCanvas().resetDotSize();

                        mapComponent.getMapCanvas().resetTimeMarker();
                        getStopButton().setEnabled( false );
                        if (animation != null) {
                            animation.cancel();
                        }
                    }
                    getPlayButton().setEnabled( !allValue );

                }
            } );
        }
        return allButton;
    }

    public JButton getExportButton() {
        if (exportButton == null) {
            exportButton = new JButton( Statics.EXPORT );
            exportButton.addActionListener( createExportButtonActionListener() );
            exportButton.setEnabled( false );
        }
        return exportButton;
    }

    public JButton getPlayButton() {
        if (playButton == null) {

            try {
                final ImageIcon icon = new ImageIcon( ImageIO.read( getClass().getResource( "Play.gif" ) ) );
                playButton = new JButton( icon );
                // to remote the spacing between the image and button's borders
                playButton.setMargin( new Insets( 0, 0, 0, 0 ) );

                // to remove the border
                playButton.setBorder( null );
            }
            catch (final IOException e1) {
                playButton = new JButton( Statics.PLAY );
            }

            playButton.setEnabled( false );
            playButton.addActionListener( new ActionListener() {

                @Override
                public void actionPerformed( ActionEvent e ) {
                    animation = new TimerTask() {

                        @Override
                        public void run() {
                            final JSlider slider = getStepSlider();
                            slider.setValue( slider.getValue() + 1 );
                            if (slider.getValue() >= slider.getMaximum()) {
                                cancel();
                                getPlayButton().setEnabled( true );
                                animation = null;
                            }

                        }
                    };
                    final Timer timer = new Timer();
                    timer.schedule( animation, 0, 33 );
                    getPlayButton().setEnabled( false );
                    getStopButton().setEnabled( true );
                }
            } );

        }
        return playButton;
    }

    public JSlider getStepSlider() {
        if (stepSlider == null) {
            stepSlider = new JSlider();
            stepSlider.setEnabled( false );
            stepSlider.setMinimum( 0 );
            stepSlider.setValue( 0 );
            stepSlider.addChangeListener( new ChangeListener() {

                @Override
                public void stateChanged( ChangeEvent e ) {
                    if (!getAllButton().isSelected()) {
                        mapComponent.getMapCanvas().setDotSize( 5 );
                        mapComponent.getMapCanvas().setTimeMarker( stepSlider.getValue() );
                    }

                }
            } );
        }
        return stepSlider;
    }

    public JButton getStopButton() {
        if (stopButton == null) {
            try {
                final ImageIcon icon = new ImageIcon( ImageIO.read( getClass().getResource( "Stop.gif" ) ) );
                stopButton = new JButton( icon );
                // to remote the spacing between the image and button's borders
                stopButton.setMargin( new Insets( 0, 0, 0, 0 ) );

                // to remove the border
                stopButton.setBorder( null );
            }
            catch (final IOException e1) {
                stopButton = new JButton( Statics.STOP );
            }
            stopButton.setEnabled( false );
            stopButton.addActionListener( new ActionListener() {

                @Override
                public void actionPerformed( ActionEvent e ) {
                    animation.cancel();
                    getPlayButton().setEnabled( true );
                    getStopButton().setEnabled( false );
                    animation = null;
                }
            } );
        }
        return stopButton;
    }

    @Override
    public void setEnabled( boolean enabled ) {
        super.setEnabled( enabled );

        getExportButton().setEnabled( enabled );
        getStepSlider().setEnabled( enabled );
        mapComponent.getMapCanvas().resetTimeMarker();
        getStepSlider().setValue( 0 );
        getAllButton().setEnabled( enabled );
        getAllButton().setSelected( enabled );
    }
}
