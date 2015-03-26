package de.lighti.log;

import de.lighti.model.AppState;
import de.lighti.model.game.Player;
import java.util.TreeMap;

public class XpLogJson extends LogJson{
	
	@Override
	protected String[][] getData(Player player){
		
		TreeMap<Long, Integer> xp = player.getXp();
		
		Long[] array = xp.keySet().toArray(new Long[xp.size()]);
        
		long[] ticks = new long[xp.keySet().size()];
        {
            int index = 0;
            for(Long elt : xp.keySet()){
                ticks[index++] = elt.longValue();
            }
        }
		
		
		String[][] data = new String[ticks.length][2];
		
		for(int i = 0; i < data.length; i++){
			data[i][0] = String.valueOf(ticks[i]);
			data[i][1] = String.valueOf(xp.get(ticks[i]));
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
						System.out.print("\"xp\" : ");
						System.out.print(data[j][i]);
						System.out.println(",");
						break;
				}
			}
			
			closingDataBlock(j, data.length);
		}
	}
	
    public XpLogJson(Player player, AppState state){
        super(player, state);
    }
}
