package de.lighti.log;

import de.lighti.model.AppState;
import de.lighti.model.game.Player;
import java.util.TreeMap;

public class DeathLogJson extends LogJson{
	
	@Override
	protected String[][] getData(Player player){
		
		TreeMap<Long, int[]> deaths = player.getHero().getDeaths();
		
		Long[] array = deaths.keySet().toArray(new Long[deaths.size()]);
        
		long[] ticks = new long[deaths.keySet().size()];
        {
            int index = 0;
            for(Long elt : deaths.keySet()){
                ticks[index++] = elt.longValue();
            }
        }
		
		
		String[][] data = new String[ticks.length][3];
		
		for(int i = 0; i < data.length; i++){
			data[i][0] = String.valueOf(ticks[i]);
			data[i][1] = String.valueOf(deaths.get(ticks[i])[0]);
			data[i][2] = String.valueOf(deaths.get(ticks[i])[1]);
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
	
    public DeathLogJson(Player player, AppState state){
        super(player, state);
    }
}
