package de.lighti.parsing;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.JOptionPane;

import de.lighti.DefaultGameEventListener;
import de.lighti.Dotalys2App;
import de.lighti.model.Statics;
import de.lighti.model.game.Player;
import de.lighti.model.state.ParseState;

public class GeneralGameStateTracker extends DefaultGameEventListener {
    private final Dotalys2App app;

    public GeneralGameStateTracker( Dotalys2App app ) {
        this.app = app;
    }

    @Override
    public void parseComplete( long tickMs, ParseState state ) {
        if (state.getProtocolVersion() > Statics.SUPPORTED_PROTOCOL_VERSION) {
            JOptionPane.showMessageDialog( app, Statics.PROTOCOL_WARNING, Statics.WARNING, JOptionPane.WARNING_MESSAGE );
        }
        app.getAppState().setMsPerTick( (int) (state.getTickInterval() * 1000) );

        //Unhandled PlayerVariables
        final DefaultComboBoxModel<String> model = (DefaultComboBoxModel<String>) app.getHistogramComponent().getAttributeBox().getModel();
        for (final String s : app.getAppState().getUnhandledPlayerVariableNames()) {
            model.addElement( s );
        }

        //Players
        final DefaultListModel<String> playerHistogramModel = (DefaultListModel<String>) app.getHistogramComponent().getPlayerBox().getModel();
        final DefaultComboBoxModel<String> playerModel = (DefaultComboBoxModel<String>) app.getPlayerComponent().getPlayerBox().getModel();
        final List<Player> sortedPlayers = new ArrayList<Player>( app.getAppState().getPlayers() );
        Collections.sort( sortedPlayers, new Comparator<Player>() {

            @Override
            public int compare( Player o1, Player o2 ) {
                return Integer.compare( o1.getId(), o2.getId() );
            }
        } );
        for (final Player p : sortedPlayers) {
            playerHistogramModel.addElement( p.getName() );
            playerModel.addElement( p.getName() );
        }
        app.getMapComponent().buildTreeNodes( sortedPlayers );

        //Now fire up the application
        app.getMainView().setEnabled( true );

    }

}
