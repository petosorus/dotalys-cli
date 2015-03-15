package de.lighti.model;

import java.awt.Color;

public interface Statics {
    String DIRE = "Dire";
    String GOLD_PER_MINUTE = "Gold per Minute";
    String HERO = "Hero";
    String MILISECONDS = "Miliseconds";
    String MOVEMENT = "Movement";
    String NAME = "Name";
    String PLAYER = "Player";
    String RADIANT = "Radiant";
    String SECONDS = "Seconds";
    String TEAM = "Team";
    String TOTAL_GOLD = "Total Gold";
    String TOTAL_XP = "Total XP";
    String UNKNOWN_HERO = "<unknown>";
    String XP_PER_MINUTE = "XP per Minute";
    String DEATHS = "Deaths";
    String MAP_ZONES = "Map/Zones";
    String EXPORT = "Export";
    String PLAY = "Play";
    String STOP = "Stop";
    String ALL = "All";

    Color[] PLAYER_COLOURS = { Color.BLUE, new Color( 0, 128, 128 ), // Teal
                    new Color( 128, 0, 128 ), //Purple
                    Color.YELLOW, Color.ORANGE, Color.PINK, Color.GRAY, new Color( 173, 216, 230 ), //Light blue
                    Color.GREEN, new Color( 165, 42, 42 ) //Brown
    };
    String UNKNOWN_ABILITY = "<unknown>";
    String PLAYER_HISTOGRAMS = "Player Histograms";
    String PLAYER_STATISTICS = "Player Statistics";
    String MAP_EVENTS = "Map Events";
    String MATCH_ANALYSIS = "Match Analysis";
    String APPLICATION_TITLE = "Dotalys2";
    String DIFFERENCE = "Difference";
    String EXPERIENCE = "Experience";
    String TIME = "Time";
    String GOLD = "Gold";
    String ZONES = "Zones";
    String ABILITIES = "Abilities";
    String BATCH_EXPORT = "Batch export";
    String OK = "Ok";
    String ITEMS = "Items";
    String UNKNOWN_ITEM = "<unknown>";
    String SAVE_TO = "Save to: ";
    String BROWSE = "Browse";
    int SUPPORTED_PROTOCOL_VERSION = 40; //Version used by Dota 6.8.1
    String PROTOCOL_WARNING = "The Dota2 version of the file is newer than what this version of Dotalys2 was tested with. Results may vary";
    String WARNING = "Warning";
}
