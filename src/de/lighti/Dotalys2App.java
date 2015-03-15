package de.lighti;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JTabbedPane;
import javax.swing.UIManager;

import de.lighti.components.DotalysMenuBar;
import de.lighti.components.map.MapComponent;
import de.lighti.components.match.GameStatisticsComponent;
import de.lighti.components.player.histogram.HistogramComponent;
import de.lighti.components.player.statistics.PlayerComponent;
import de.lighti.io.DataImporter;
import de.lighti.model.AppState;
import de.lighti.model.Statics;

public class Dotalys2App extends JFrame {
    private final AppState appState;

    /**
     *
     */
    private static final long serialVersionUID = -5920990846685808741L;
    static {
        try {
            UIManager.setLookAndFeel( UIManager.getSystemLookAndFeelClassName() );
            UIManager.put( "Panel.background", Color.WHITE );
            UIManager.put( "Slider.background", Color.WHITE );
            UIManager.put( "SplitPane.background", Color.WHITE );
            UIManager.put( "OptionPane.background", Color.WHITE );
        }
        catch (final Exception e) {
            // Don't care
        }
    }

    private JTabbedPane mainView;

    private HistogramComponent histogramComponent;

    private PlayerComponent playerComponent;

    private MapComponent mapComponent;

    public Dotalys2App() {
        super( Statics.APPLICATION_TITLE );

        appState = new AppState();
        parseLocalisedNames();

        setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );

        setSize( new Dimension( 900, 700 ) );
        setResizable( false );

        final JComponent com = getMainView();
        com.setPreferredSize( getContentPane().getPreferredSize() );

        getContentPane().add( com, BorderLayout.CENTER );
        // pack();
        setJMenuBar( new DotalysMenuBar( this ) );

    }

    public AppState getAppState() {
        return appState;
    }

    public HistogramComponent getHistogramComponent() {
        if (histogramComponent == null) {
            histogramComponent = new HistogramComponent( appState );
        }
        return histogramComponent;
    }

    public JComponent getMainView() {
        if (mainView == null) {
            mainView = new JTabbedPane() {
                @Override
                public void setEnabled( boolean enabled ) {
                    super.setEnabled( enabled );

                    //The player box which is visible at start is the only one we need to send an extra notification to
                    getHistogramComponent().setEnabled( enabled );
                }
            };
            mainView.addTab( Statics.PLAYER_HISTOGRAMS, getHistogramComponent() );

            mainView.addTab( Statics.PLAYER_STATISTICS, getPlayerComponent() );

            mainView.addTab( Statics.MAP_EVENTS, getMapComponent() );

            mainView.setEnabled( false );

            final GameStatisticsComponent gsc = new GameStatisticsComponent( getAppState() );
            mainView.addTab( Statics.MATCH_ANALYSIS, gsc );

            mainView.setBorder( BorderFactory.createEmptyBorder( 5, 5, 5, 5 ) );
        }

        return mainView;
    }

    public MapComponent getMapComponent() {
        if (mapComponent == null) {
            mapComponent = new MapComponent( appState );
        }
        return mapComponent;
    }

    public PlayerComponent getPlayerComponent() {
        if (playerComponent == null) {
            playerComponent = new PlayerComponent( appState );
        }
        return playerComponent;
    }

    private void parseLocalisedNames() {
        DataImporter.readLocalisedHeroNames( getClass().getResourceAsStream( "heroes.xml" ), appState );
        DataImporter.readLocalisedAbilityNames( getClass().getResourceAsStream( "abilities.xml" ), appState );

    }
}
