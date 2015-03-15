package de.lighti.log;

import de.lighti.model.game.Player;
import de.lighti.model.game.Hero;
import de.lighti.model.game.Dota2Item;

import java.util.TreeMap;
import java.util.Collections;
import java.util.List;
import java.util.ArrayList;
import java.util.Comparator;

public class ItemLogJson extends LogJson{
    
    @Override
    protected String[][] getData(Player player){
        //code du mec
        final List<Object[]> log = new ArrayList();
        final Hero h = player.getHero();
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
                    case 2:
                        System.out.print("\"y\" : ");
                        System.out.print(data[j][i]);
                        System.out.println(",");
                        break;
                    case 3:
                        System.out.print("\"item\" : ");
                        System.out.println(data[j][i]);
                        break;
				}
			}
			
			closingDataBlock(j, data.length);
		}
	}
    
    public ItemLogJson(Player player){
        super(player);
    }
    
}
