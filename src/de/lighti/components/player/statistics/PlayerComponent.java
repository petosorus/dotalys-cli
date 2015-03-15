package de.lighti.components.player.statistics;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import de.lighti.model.AppState;
import de.lighti.model.Statics;
import de.lighti.model.game.Ability;
import de.lighti.model.game.Dota2Item;
import de.lighti.model.game.Hero;
import de.lighti.model.game.Player;

public class PlayerComponent extends JSplitPane {
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private final AppState appState;
    private JComboBox<String> playerBox;
    private final static NumberFormat TWO_DIGITS = new DecimalFormat( "#####.##" );

    public PlayerComponent( AppState appState ) {
        super( JSplitPane.HORIZONTAL_SPLIT, null, null );

        this.appState = appState;

        setOneTouchExpandable( false );
        setDividerLocation( 150 );
        setDividerSize( 0 );
        setBorder( BorderFactory.createEmptyBorder( 5, 5, 5, 5 ) );
        setLeftComponent( createLeftComponent() );
        setRightComponent( createRightComponent() );
    }

    private Component createLeftComponent() {

        final JPanel leftPane = new JPanel();
        leftPane.setLayout( new BoxLayout( leftPane, BoxLayout.Y_AXIS ) );

        leftPane.add( getPlayerBox() );
        leftPane.add( Box.createVerticalGlue() );

        leftPane.setBorder( BorderFactory.createTitledBorder( BorderFactory.createEmptyBorder( 5, 5, 5, 5 ), Statics.PLAYER ) );
        return leftPane;
    }

    private JComponent createPlayerBuildOrderTab() {
        final BuildOrderComponent bc = new BuildOrderComponent();
        getPlayerBox().addItemListener( new java.awt.event.ItemListener() {

            @Override
            public void itemStateChanged( ItemEvent e ) {
                final String id = (String) playerBox.getSelectedItem();
                if (id == null) {
                    return;
                }

                final Player p = appState.getPlayerByName( id );
                final Queue<Dota2Item> buildOrder = new LinkedBlockingQueue<Dota2Item>();

                final Hero hero = p.getHero();
                if (hero != null) {
                    final Queue<Hero.ItemEvent> completeLog = hero.getItemLog();

                    for (final Hero.ItemEvent n : completeLog) {

                        //Only the bag, and only added items
                        if (n.slot <= 5 && n.added) {
                            buildOrder.add( n.item );
                        }
//                        if (n.startsWith( "+" )) {
//                            buildOrder.add( appState.getItemByName( n.substring( 1 ) ) );
//                        }
                    }
                    bc.setItems( buildOrder );
                }

            }
        } );
        return bc;
    }

    private JComponent createPlayerSkillTreeTab() {
        final SkillTreecomponent c = new SkillTreecomponent();
        getPlayerBox().addItemListener( new java.awt.event.ItemListener() {

            @Override
            public void itemStateChanged( ItemEvent e ) {
                final String id = (String) playerBox.getSelectedItem();
                if (id == null) {
                    return;
                }
                final Player p = appState.getPlayerByName( id );
                final Hero hero = p.getHero();

                final List<Ability> abilities = new ArrayList<Ability>();
                for (final Ability a : hero.getAbilities()) {
                    abilities.add( a );
                }
                c.setAbilities( abilities );
                c.repaint();

            }
        } );
        return c;
    }

    private JComponent createPlayerStatisticsTab() {

        final JTable table = new JTable();
        final TableModel model = new DefaultTableModel( 8, 2 ) {

            /**
             *
             */
            private static final long serialVersionUID = -5417710077260844257L;

            @Override
            public boolean isCellEditable( int row, int column ) {
                //all cells false
                return false;
            }
        };
        table.setModel( model );

        model.setValueAt( Statics.NAME, 0, 0 );
        model.setValueAt( Statics.TEAM, 1, 0 );
        model.setValueAt( Statics.HERO, 2, 0 );
        model.setValueAt( Statics.TOTAL_GOLD, 3, 0 );
        model.setValueAt( Statics.GOLD_PER_MINUTE, 4, 0 );
        model.setValueAt( Statics.TOTAL_XP, 5, 0 );
        model.setValueAt( Statics.XP_PER_MINUTE, 6, 0 );
        model.setValueAt( Statics.DEATHS, 7, 0 );

        getPlayerBox().addActionListener( new ActionListener() {

            @Override
            public void actionPerformed( ActionEvent e ) {
                final String id = (String) getPlayerBox().getSelectedItem();
                final Player p = appState.getPlayerByName( id );
                if (p != null) {
                    final long ms = appState.getGameLength();

                    final double minutes = ms / 60000.0;

                    final String team = p.isRadiant() ? Statics.RADIANT : Statics.DIRE;
                    final Hero hero = p.getHero();
                    final String name = hero != null ? hero.getName() : Statics.UNKNOWN_HERO;
                    final int gold = p.getTotalEarnedGold();
                    final double gpm = gold / minutes;
                    final int toalXp = p.getTotalXP();
                    final double xpm = toalXp / minutes;
                    final int deaths = p.getDeaths().size();

                    model.setValueAt( p.getName(), 0, 1 );
                    model.setValueAt( team, 1, 1 );
                    model.setValueAt( name, 2, 1 );
                    model.setValueAt( TWO_DIGITS.format( gold ), 3, 1 );

                    model.setValueAt( TWO_DIGITS.format( gpm ), 4, 1 );
                    model.setValueAt( TWO_DIGITS.format( toalXp ), 5, 1 );
                    model.setValueAt( TWO_DIGITS.format( xpm ), 6, 1 );
                    model.setValueAt( TWO_DIGITS.format( deaths ), 7, 1 );
                }
            }
        } );

        table.setBorder( BorderFactory.createEmptyBorder( 5, 5, 5, 5 ) );
        return table;
    }

    private Component createRightComponent() {
        final JTabbedPane rightPane = new JTabbedPane();
        rightPane.addTab( "Statistics", createPlayerStatisticsTab() );
        rightPane.addTab( "Build Order", createPlayerBuildOrderTab() );
        rightPane.addTab( "Skill Tree", createPlayerSkillTreeTab() );
        rightPane.setBorder( BorderFactory.createEmptyBorder( 5, 5, 5, 5 ) );
        return rightPane;
    }

    public JComboBox<String> getPlayerBox() {
        if (playerBox == null) {
            playerBox = new JComboBox<String>() {

                /**
                 *
                 */
                private static final long serialVersionUID = 1L;

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

            playerBox.setAlignmentX( Component.CENTER_ALIGNMENT );
        }
        return playerBox;
    }

}
