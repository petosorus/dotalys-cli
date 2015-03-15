package de.lighti.io;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import de.lighti.model.game.Player;
import de.lighti.model.AppState;
import java.util.Set;
import java.util.TreeMap;
import de.lighti.log.*;

public class DataExporter {
    public static void exportCSV( File file, String header, String[][] data ) throws IOException {
        final BufferedWriter fo = new BufferedWriter( new FileWriter( file ) );
        fo.write( header );
        fo.newLine();
        for (final String[] line : data) {
            for (int i = 0; i < line.length; i++) {
                fo.write( line[i] );
                if (i != line.length) {
                    fo.write( ", " );
                }
            }
            fo.newLine();

        }
        fo.close();
    }
    
    /**
     * Sortie en json de données
     * 
     * @param state 
     * @param option
     */
    public static void outJson( AppState state, int option ){
		
        int np = 0;
        Set<Player> players = state.getPlayers();
        int playerNumber = players.size();
        
        System.out.println("[");
		
		for(Player p : players){
            np++;
            
			System.out.println("{");
			System.out.println("\"name\" : " + "\"" + p.getName() + "\"" + ", ");
            System.out.println("\"id\" : " + "\"" + np + "\"" + ", ");
            System.out.println("\"team\" : " + (p.isRadiant()?0:1) + ", ");
            System.out.println("\"hero\" ; " + "\"" + p.getHero().getName() + "\"" + ", ");
			System.out.println("\"coords\" : [");
            
            String[][] data = new String[0][0];
            
            switch(option){
                case 0:
                    data = ChartCreator.createMoveLog( p.getName(), state );
                    break;
                case 1:
                    data = ChartCreator.createAbilityLog( p );
                    break;
                case 2:
					data = ChartCreator.createItemLog( p );
					break;
                case 3:
                    
            }
            
            for(String[] line : data){
				System.out.println("{");
				
				for(int i = 0; i < line.length; i++){
					switch(i){
						case 0:
							System.out.println("\"t\": " + line[i] + ",");
							break;
						case 1:
							System.out.println("\"x\": " + line[i] + ",");
							break;
						case 2:
                            if(line.length > 3){
                                System.out.println("\"y\" : " + line[i] + ",");
                            }
                            else{
                                System.out.println("\"y\" : " + line[i]);
                            }
							break;
                        case 3:
                            switch(option){
                                case 1:
                                    System.out.println("\"ability\" : " 
                                    + line[i]);
                                    break;
                                case 2:
                                    System.out.println("\"item\" : " 
                                    + line[i]);
                                    break;
                            }
					}
				}
                
                if(line != data[data.length - 1 ]){
                    System.out.println("},");
                }
                else{
                    System.out.println("}");
                }
			}
			System.out.println("]");
            if(np < playerNumber){
                System.out.println("}, ");
            }
            else{
                System.out.println("}");
            }
		}
        System.out.println("]");
	}
    
    public static void outJsonLog( AppState state, int option ){
        Set<Player> players = state.getPlayers();
        
        System.out.println("[");
		
		for(Player p : players){
            LogJson log;
            switch(option){
                case 0:
                    log = new MovementLogJson(p);
                    break;
                case 1:
                    log = new AbilityLogJson(p);
                    break;
                case 2:
                    log = new ItemLogJson(p);
                    break;
                case 3:
                    log = new GoldLogJson(p);
                    break;
                case 4:
                    log = new XpLogJson(p);
                    break;
                case 5:
					log = new DeathLogJson(p);
					break;
                default:
                    log = new MovementLogJson(p);
            }
            log.treatingPlayer();
        }    
        LogJson.reinitId();
        System.out.println("]");
    }
    
    
}
