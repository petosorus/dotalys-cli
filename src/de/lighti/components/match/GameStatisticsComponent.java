package de.lighti.components.match;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JSplitPane;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;

import de.lighti.io.ChartCreator;
import de.lighti.model.AppState;

public class GameStatisticsComponent extends JSplitPane {
    /**
     * 
     */
    private static final long serialVersionUID = -3232988092086562528L;
    public static final String AVERAGE_TEAM_DISTANCE = "Average Team distance";
    public static final String TEAM_XP = "Difference in Team Experience";
    protected static final String TEAM_GOLD = "Difference in Team Gold";

    private JPanel selectionPanel;
    private ChartPanel chartPanel;
    private JComboBox<String> modeBox;
    private final AppState appState;

    public GameStatisticsComponent( AppState state ) {
        appState = state;
        setBorder( BorderFactory.createEmptyBorder( 5, 5, 5, 5 ) );
        setLeftComponent( getSelectionPanel() );
        setRightComponent( getChartPanel() );
    }

    public JComboBox<String> getAttributeBox() {
        if (modeBox == null) {
            modeBox = new JComboBox<String>() {

                /**
                 * 
                 */
                private static final long serialVersionUID = -3161821740467460702L;

                /** 
                 * @inherited <p>
                 */
                @Override
                public Dimension getMaximumSize() {
                    final Dimension max = super.getMaximumSize();
                    max.height = getPreferredSize().height;
                    return max;
                }

            };

            modeBox.setAlignmentX( Component.CENTER_ALIGNMENT );
            modeBox.addItem( AVERAGE_TEAM_DISTANCE );
            modeBox.addItem( TEAM_XP );
            modeBox.addItem( TEAM_GOLD );
            modeBox.addActionListener( new ActionListener() {

                @Override
                public void actionPerformed( ActionEvent e ) {
//                    ChartCreator.createPlayerHistogram( getChartPanel(), (String) attributeBox.getSelectedItem(), playerBox.getSelectedValuesList(), appState );
                    JFreeChart data = null;
                    switch ((String) modeBox.getSelectedItem()) {
                        case AVERAGE_TEAM_DISTANCE:
                            data = ChartCreator.createAverageTeamDistanceGraph( appState );
                            break;
                        case TEAM_XP:
                            data = ChartCreator.createTeamXpDifferenceGraph( appState );
                            break;
                        case TEAM_GOLD:
                            data = ChartCreator.createTeamGoldDifferenceGraph( appState );
                            break;
                        default:
                            break;
                    }

                    getChartPanel().setChart( data );
                }
            } );

        }
        return modeBox;
    }

    public ChartPanel getChartPanel() {
        if (chartPanel == null) {
            chartPanel = new ChartPanel( null );
            chartPanel.setBackground( Color.WHITE );
            chartPanel.setBorder( BorderFactory.createEtchedBorder() );
        }
        return chartPanel;
    }

    public JPanel getSelectionPanel() {
        if (selectionPanel == null) {
            selectionPanel = new JPanel();
            selectionPanel.setLayout( new BoxLayout( selectionPanel, BoxLayout.Y_AXIS ) );
            selectionPanel.add( getAttributeBox() );
            selectionPanel.add( Box.createVerticalGlue() );
            selectionPanel.setBorder( BorderFactory.createTitledBorder( BorderFactory.createEmptyBorder( 5, 5, 5, 5 ), "Mode" ) );
        }
        return selectionPanel;
    }
}
