package de.lighti;

import de.lighti.components.batch.*;
import de.lighti.io.ChartCreator;
import de.lighti.io.DataExporter;
import de.lighti.io.DataImporter;
import de.lighti.model.Statics;
import de.lighti.model.game.Player;
import de.lighti.model.AppState;

import java.io.File;
import java.io.IOException;

public class MainJson{
    
    public static void main(String[] args){
		
		if(args.length == 0){
			System.out.println("Usage : java -jar Main.jar replay.dem");
			System.out.println("Résultats sur la sortie standard.");
			return;
		}
        
        int option = 0;
        if(args.length == 2){
            switch(args[1]){
                case "-movement" :
                    option = 0;
                    break;
                case "-ability" :
                    option = 1;
                    break;
                case "-item" :
                    option = 2;
                    break;
                case "-gold" :
                    option = 3;
                    break;
                case "-xp" :
                    option = 4;
                    break;
                case "-death":
					option = 5;
					break;
                default:
                    System.err.println("Option non existante");
                    System.err.println("Liste des options super cool");
                    return;
            }
        }
        
        
        File f = new File(args[0]);
        AppState state = new AppState();
        DataImporter.parseReplayFile( state, null, f );
        
        DataExporter.outJsonLog( state, option );
      
        
    }
}
        

