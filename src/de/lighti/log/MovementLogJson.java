package de.lighti.log;

import de.lighti.model.AppState;
import de.lighti.model.game.Player;
import de.lighti.model.game.Unit;
import de.lighti.model.game.Hero;
import java.util.TreeMap;

public class MovementLogJson extends LogJson{

	@Override
	protected String[][] getData(Player player){
		
		Unit unit = player.getHero();		
        
		TreeMap<Long, Integer> x = (TreeMap)unit.getX();
		TreeMap<Long, Integer> y = (TreeMap)unit.getY();
		
		Long[] array = x.keySet().toArray(new Long[x.size()]);
        
		long[] ticks = new long[x.keySet().size()];
        {
            int index = 0;
            for(Long elt : x.keySet()){
                ticks[index++] = elt.longValue();
            }
        }
		
		String[][] data = new String[ticks.length][3];	
		
		for(int i = 0; i < data.length; i++){
			
			data[i][0] = String.valueOf(ticks[i]);
			data[i][1] = String.valueOf(x.get(ticks[i]));
			data[i][2] = String.valueOf(y.get(ticks[i]));
		}
		
		return data;
	}
	
	@Override
	protected void outData(Player player){
				
		String[][] data = getData(player);	
		
		for(int j = 0; j < data.length; j++){
			
			openingDataBlock();
			
			for(int i = 0; i < data[j].length; i++){
				switch(i){
					case 0:
						System.out.print("\"tick\" : ");
						System.out.print(data[j][i]);
						System.out.println(",");
						break;
					case 1:
						System.out.print("\"x\" : ");
						System.out.print(data[j][i]);
						System.out.println(",");
						break;
					case 2:
						System.out.print("\"y\" : ");
						System.out.println(data[j][i]);
						break;
				}
			}
			
			closingDataBlock(j, data.length);
		}
	}
    
    public MovementLogJson(Player player, AppState state){
        super(player, state);
    }

}
