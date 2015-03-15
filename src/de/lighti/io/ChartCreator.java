package de.lighti.io;

import java.awt.Color;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.logging.Logger;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.DateTickMarkPosition;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYDifferenceRenderer;
import org.jfree.data.time.FixedMillisecond;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.XYSeries;

import de.lighti.components.map.MapComponent;
import de.lighti.components.match.GameStatisticsComponent;
import de.lighti.model.AppState;
import de.lighti.model.Statics;
import de.lighti.model.game.Ability;
import de.lighti.model.game.Dota2Item;
import de.lighti.model.game.Hero;
import de.lighti.model.game.Player;
import de.lighti.model.game.Unit;
import de.lighti.model.game.Unit.Zone;

public final class ChartCreator {
    public static String[][] createAbilityLog( Player p ) {
        final List<Object[]> log = new ArrayList();
        final Hero h = p.getHero();
        for (final Ability a : h.getAbilities()) {
            for (final long l : a.getInvocations()) {
                final Object[] o = new Object[4];
                o[0] = l;
                o[1] = h.getX( l );
                o[2] = h.getY( l );
                o[3] = a.getKey();
                log.add( o );
            }
        }
        Collections.sort( log, new Comparator<Object[]>() {

            @Override
            public int compare( Object[] o1, Object[] o2 ) {
                final long l1 = (long) o1[0];
                final long l2 = (long) o2[0];
                return Long.compare( l1, l2 );
            }
        } );
        final String[][] ret = new String[log.size()][4];
        for (int i = 0; i < log.size(); i++) {
            final Object[] o = log.get( i );
            ret[i][0] = o[0].toString();
            ret[i][1] = o[1].toString();
            ret[i][2] = o[2].toString();
            ret[i][3] = o[3].toString();
        }
        return ret;
    }

    public static XYSeries createAbilityMap( Hero hero, String name ) {
        final Ability ability = hero.getAbilityByName( name );
        if (ability == null) {
            throw new IllegalArgumentException( "Hero " + hero.getName() + " has no ability " + name );
        }
        final XYSeries ret = new XYSeries( name + MapComponent.CAT_ABILITIES, false, true );

        for (final Long l : ability.getInvocations()) {
            ret.add( hero.getX( l ), hero.getY( l ) );
        }

        return ret;
    }

    /**
     * This method creates a data set with two series(one radiant, one dire) representing the average
     * distance between all members of that team. For each timestep, the symmetrical half of
     * and Euclidian distance matrix is calculated, and the entries for players of the same team are
     * added to the sum. Each sum is then divided by 5.
     * @param appState the current app state containing player data
     * @return a dat set containing two data series
     */
    private static TimeSeriesCollection createAverageTeamDistanceDataSet( AppState appState ) {
        final TimeSeriesCollection series = new TimeSeriesCollection();
        final TimeSeries goodGuys = new TimeSeries( Statics.RADIANT );
        final TimeSeries badGuys = new TimeSeries( Statics.DIRE );

        final List<Hero> radiant = new ArrayList<Hero>();
        final List<Hero> dire = new ArrayList<Hero>();

        for (final Player p : appState.getPlayers()) {
            if (p.isRadiant()) {
                radiant.add( p.getHero() );
            }
            else {
                dire.add( p.getHero() );
            }
        }

        for (long seconds = 0l; seconds < appState.getGameLength(); seconds += appState.getMsPerTick() * 1000) {
            long baddyDistance = 0l;
            long goodDistance = 0l;

            //Radiant
            outerLoop: for (final Hero h : radiant) {
                for (final Hero i : radiant) {
                    if (h == i) {
                        continue outerLoop;
                    }
                    final int xDiff = i.getX( seconds ) - h.getX( seconds );
                    final int yDiff = i.getY( seconds ) - h.getY( seconds );
                    goodDistance += Math.sqrt( Math.pow( xDiff, 2 ) + Math.pow( yDiff, 2 ) );
                }
            }

            //Dire
            outerLoop: for (final Hero h : dire) {
                for (final Hero i : dire) {
                    if (h == i) {
                        continue outerLoop;
                    }
                    final int xDiff = i.getX( seconds ) - h.getX( seconds );
                    final int yDiff = i.getY( seconds ) - h.getY( seconds );
                    baddyDistance += Math.sqrt( Math.pow( xDiff, 2 ) + Math.pow( yDiff, 2 ) );
                }
            }

            //Average
            goodDistance /= 5l;
            baddyDistance /= 5l;

            goodGuys.add( new FixedMillisecond( seconds ), goodDistance );
            badGuys.add( new FixedMillisecond( seconds ), baddyDistance );

        }
        series.addSeries( badGuys );
        series.addSeries( goodGuys );
        return series;
    }

    public static JFreeChart createAverageTeamDistanceGraph( AppState state ) {
        final JFreeChart chart = ChartFactory.createTimeSeriesChart( GameStatisticsComponent.AVERAGE_TEAM_DISTANCE, // chart title
                        Statics.MILISECONDS, // x axis label
                        "", // y axis label
                        createAverageTeamDistanceDataSet( state ), // data
                        true, // include legend
                        true, // tooltips
                        false // urls
                        );
        final XYPlot plot = chart.getXYPlot();

        final DateAxis domainAxis = new DateAxis( Statics.TIME );
        domainAxis.setTickMarkPosition( DateTickMarkPosition.MIDDLE );
        domainAxis.setLowerMargin( 0.0 );
        domainAxis.setUpperMargin( 0.0 );
        plot.setDomainAxis( domainAxis );
        plot.setForegroundAlpha( 0.5f );
        plot.setDomainPannable( false );
        plot.setRangePannable( false );

        final NumberAxis rangeAxis = new NumberAxis( GameStatisticsComponent.AVERAGE_TEAM_DISTANCE );
        rangeAxis.setLowerMargin( 0.15 );
        rangeAxis.setUpperMargin( 0.15 );
        plot.setRangeAxis( rangeAxis );

        return chart;
    }

    public static XYSeries createDeathMap( String name, AppState appState ) {
        final Player p = appState.getPlayerByName( name );

        final Hero hero = p.getHero();
        final Collection<int[]> coords = hero.getDeaths().values();

        final XYSeries ret = new XYSeries( name, false, true );
        for (final int[] e : coords) {
            ret.add( e[0], e[1] );
        }
        return ret;
    }

    public static String[][] createItemLog( Player p ) {
        final List<Object[]> log = new ArrayList();
        final Hero h = p.getHero();
        for (final Dota2Item a : h.getAllItems()) {
            for (final long l : a.getUsage()) {
                final Object[] o = new Object[4];
                o[0] = l;
                o[1] = h.getX( l );
                o[2] = h.getY( l );
                o[3] = a.getKey();
                log.add( o );
            }
        }
        Collections.sort( log, new Comparator<Object[]>() {

            @Override
            public int compare( Object[] o1, Object[] o2 ) {
                final long l1 = (long) o1[0];
                final long l2 = (long) o2[0];
                return Long.compare( l1, l2 );
            }
        } );
        final String[][] ret = new String[log.size()][4];
        for (int i = 0; i < log.size(); i++) {
            final Object[] o = log.get( i );
            ret[i][0] = o[0].toString();
            ret[i][1] = o[1].toString();
            ret[i][2] = o[2].toString();
            ret[i][3] = o[3].toString();
        }
        return ret;
    }

    public static XYSeries createItemMap( Hero hero, String itemKey ) {
        final Set<Dota2Item> items = hero.getItemsByName( itemKey );
        final XYSeries ret = new XYSeries( hero.getName() + itemKey + MapComponent.CAT_ABILITIES, false, true );

        for (final Dota2Item i : items) {
            for (final Long l : i.getUsage()) {
                ret.add( hero.getX( l ), hero.getY( l ) );
            }
        }

        return ret;
    }

    public static String[][] createMoveLog( String string, AppState appState ) {
        final Player p = appState.getPlayerByName( string );

        final Unit hero = p.getHero();
        final Map<Long, Integer> x = hero.getX();
        final Map<Long, Integer> y = hero.getY();
        final String[][] ret = new String[x.size()][];
        int i = 0;
        for (final Map.Entry<Long, Integer> e : x.entrySet()) {
            ret[i] = new String[] { e.getKey().toString(), e.getValue().toString(), y.get( e.getKey() ).toString() };
            i++;
        }

        return ret;
    }

    public static XYSeries createMoveMap( String string, AppState appState ) {

        final Player p = appState.getPlayerByName( string );

        final Unit hero = p.getHero();
        final Map<Long, Integer> x = hero.getX();
        final Map<Long, Integer> y = hero.getY();
        final XYSeries ret = new XYSeries( string, false, true );

        for (final Map.Entry<Long, Integer> e : x.entrySet()) {
            ret.add( e.getValue(), y.get( e.getKey() ) );

        }
        return ret;
    }

    private static TimeSeriesCollection createPlayerDataSet( String attribute, List<String> players, AppState appState ) {
        final TimeSeriesCollection series = new TimeSeriesCollection();

        try {
            for (final String player : players) {
                final Player p = appState.getPlayerByName( player );
                final TimeSeries series1 = new TimeSeries( player );

                switch (attribute) {
                    case Statics.EXPERIENCE:
                        for (long seconds = 0l; seconds < appState.getGameLength(); seconds += appState.getMsPerTick() * 1000) {
                            series1.add( new FixedMillisecond( seconds ), p.getXP( seconds ) );
                        }
                        break;
                    case Statics.GOLD:
                        for (long seconds = 0l; seconds < appState.getGameLength(); seconds += appState.getMsPerTick() * 1000) {
                            series1.add( new FixedMillisecond( seconds ), p.getEarnedGold( seconds ) );
                        }
                        break;

                    case Statics.DEATHS:
                        for (long seconds = 0l; seconds < appState.getGameLength(); seconds += appState.getMsPerTick() * 1000) {
                            series1.add( new FixedMillisecond( seconds ), p.getDeaths( seconds ).size() );
                        }
                        break;
                    default:

                        for (final Entry<Long, Map<String, Object>> e : appState.gameEventsPerMs.entrySet()) {
                            if (e.getValue().containsKey( attribute + "." + ID_TO_GAMEEVENT_FORMAT.format( p.getId() ) )) {
                                final Number v = (Number) e.getValue().get( attribute + "." + ID_TO_GAMEEVENT_FORMAT.format( p.getId() ) );
                                series1.add( new FixedMillisecond( e.getKey() ), v );
                            }
                        }
                        break;
                }
                series.addSeries( series1 );

            }
        }
        catch (final ClassCastException e) {
            LOGGER.warning( "Selected attribute contained alpha-numeric data" );
        }
        return series;
    }

    public static JFreeChart createPlayerHistogram( String selectedItem, List<String> selectedValuesList, AppState state ) {
        final JFreeChart chart = ChartFactory.createXYLineChart( selectedItem, // chart title
                        Statics.MILISECONDS, // x axis label
                        "", // y axis label
                        createPlayerDataSet( selectedItem, selectedValuesList, state ), // data
                        PlotOrientation.VERTICAL, true, // include legend
                        true, // tooltips
                        false // urls
                        );

        final XYPlot plot = chart.getXYPlot();

        final DateAxis domainAxis = new DateAxis( Statics.TIME );
        domainAxis.setTickMarkPosition( DateTickMarkPosition.MIDDLE );
        domainAxis.setLowerMargin( 0.0 );
        domainAxis.setUpperMargin( 0.0 );
        plot.setDomainAxis( domainAxis );
        plot.setForegroundAlpha( 0.5f );
        plot.setDomainPannable( false );
        plot.setRangePannable( false );

        final NumberAxis rangeAxis = new NumberAxis( selectedItem );
        rangeAxis.setLowerMargin( 0.15 );
        rangeAxis.setUpperMargin( 0.15 );
        plot.setRangeAxis( rangeAxis );

        return chart;
    }

    private static TimeSeriesCollection createTeamGoldDiffDataSet( AppState appState ) {

        final TimeSeriesCollection series = new TimeSeriesCollection();

        final TimeSeries goodGuys = new TimeSeries( Statics.RADIANT );
        final TimeSeries badGuys = new TimeSeries( Statics.DIRE );

        final List<Player> radiant = new ArrayList<Player>();
        final List<Player> dire = new ArrayList<Player>();

        for (final Player p : appState.getPlayers()) {
            if (p.isRadiant()) {
                radiant.add( p );
            }
            else {
                dire.add( p );
            }
        }

        for (long seconds = 0l; seconds < appState.getGameLength(); seconds += appState.getMsPerTick() * 1000) {
            long baddyGold = 0l;
            long goodGold = 0l;

            //Radiant
            for (final Player p : radiant) {
                goodGold += p.getEarnedGold( seconds );
            }

            //Dire
            for (final Player p : dire) {
                baddyGold += p.getEarnedGold( seconds );
            }

            goodGuys.add( new FixedMillisecond( seconds ), goodGold );
            badGuys.add( new FixedMillisecond( seconds ), baddyGold );

        }
        series.addSeries( badGuys );
        series.addSeries( goodGuys );
        return series;
    }

    public static JFreeChart createTeamGoldDifferenceGraph( AppState appState ) {
        final JFreeChart chart = ChartFactory.createTimeSeriesChart( GameStatisticsComponent.TEAM_XP, Statics.EXPERIENCE, "Time",
                        createTeamGoldDiffDataSet( appState ), true, // legend
                        true, // tool tips
                        false // URLs
                        );

        final XYDifferenceRenderer renderer = new XYDifferenceRenderer( Color.GREEN, Color.RED, false );

        renderer.setSeriesPaint( 0, Color.GREEN );
        renderer.setSeriesPaint( 1, Color.RED );
        final XYPlot plot = chart.getXYPlot();
        plot.setRenderer( renderer );

        final DateAxis domainAxis = new DateAxis( Statics.TIME );
        domainAxis.setTickMarkPosition( DateTickMarkPosition.MIDDLE );
        domainAxis.setLowerMargin( 0.0 );
        domainAxis.setUpperMargin( 0.0 );
        plot.setDomainAxis( domainAxis );
        plot.setForegroundAlpha( 0.5f );

        final NumberAxis rangeAxis = new NumberAxis( Statics.GOLD );
        rangeAxis.setLowerMargin( 0.15 );
        rangeAxis.setUpperMargin( 0.15 );
        plot.setRangeAxis( rangeAxis );
        return chart;
    }

    private static TimeSeriesCollection createTeamXPDiffDataSet( AppState appState ) {

        final TimeSeriesCollection series = new TimeSeriesCollection();

        final TimeSeries goodGuys = new TimeSeries( Statics.RADIANT );
        final TimeSeries badGuys = new TimeSeries( Statics.DIRE );

        final List<Player> radiant = new ArrayList<Player>();
        final List<Player> dire = new ArrayList<Player>();

        for (final Player p : appState.getPlayers()) {
            if (p.isRadiant()) {
                radiant.add( p );
            }
            else {
                dire.add( p );
            }
        }

        for (long seconds = 0l; seconds < appState.getGameLength(); seconds += appState.getMsPerTick() * 1000) {
            long baddyXP = 0l;
            long goodXP = 0l;

            //Radiant
            for (final Player p : radiant) {
                goodXP += p.getXP( seconds );
            }

            //Dire
            for (final Player p : dire) {
                baddyXP += p.getXP( seconds );
            }

            goodGuys.add( new FixedMillisecond( seconds ), goodXP );
            badGuys.add( new FixedMillisecond( seconds ), baddyXP );

        }
        series.addSeries( badGuys );
        series.addSeries( goodGuys );
        return series;
    }

    public static JFreeChart createTeamXpDifferenceGraph( AppState state ) {
        final JFreeChart chart = ChartFactory.createTimeSeriesChart( GameStatisticsComponent.TEAM_XP, Statics.EXPERIENCE, "Time",
                        createTeamXPDiffDataSet( state ), true, // legend
                        true, // tool tips
                        false // URLs
                        );

        final XYDifferenceRenderer renderer = new XYDifferenceRenderer( Color.GREEN, Color.RED, false );

        renderer.setSeriesPaint( 0, Color.GREEN );
        renderer.setSeriesPaint( 1, Color.RED );
        final XYPlot plot = chart.getXYPlot();
        plot.setRenderer( renderer );

        final DateAxis domainAxis = new DateAxis( Statics.TIME );
        domainAxis.setTickMarkPosition( DateTickMarkPosition.MIDDLE );
        domainAxis.setLowerMargin( 0.0 );
        domainAxis.setUpperMargin( 0.0 );
        plot.setDomainAxis( domainAxis );
        plot.setForegroundAlpha( 0.5f );

        final NumberAxis rangeAxis = new NumberAxis( Statics.EXPERIENCE );
        rangeAxis.setLowerMargin( 0.15 );
        rangeAxis.setUpperMargin( 0.15 );
        plot.setRangeAxis( rangeAxis );
        return chart;

    }

    public static String[][] createZoneLog( String name, AppState state ) {
        final Player p = state.getPlayerByName( name );

        final Unit hero = p.getHero();
        final Map<Long, Zone> zones = hero.getZones();

        final String[][] ret = new String[zones.size()][];
        int i = 0;
        for (final Entry<Long, Zone> e : zones.entrySet()) {
            ret[i] = new String[] { e.getKey().toString(), e.getValue().name() };
            i++;
        }

        return ret;
    }

    private final static Logger LOGGER = Logger.getLogger( ChartCreator.class.getName() );

    /**
     * TODO
     * We store player id as a real int, but unhandled game events are stored as name.XXXX.
     * We temporaily solve this by expanding the real id to four digits.
     */
    private final static DecimalFormat ID_TO_GAMEEVENT_FORMAT = new DecimalFormat( "0000" );

    /**
     * Default constructor to prevent instantiation.
     */
    private ChartCreator() {

    }
}
