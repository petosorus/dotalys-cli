package de.lighti.log;

import de.lighti.model.game.Player;
import de.lighti.model.game.Unit;
import de.lighti.model.game.Hero;
import de.lighti.model.game.Ability;
import java.util.TreeMap;
import java.util.Collections;
import java.util.List;
import java.util.ArrayList;
import java.util.Comparator;

public class AbilityLogJson extends LogJson{

	@Override
	protected String[][] getData(Player player){
        final List<Object[]> log = new ArrayList();
        final Hero h = player.getHero();
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
		
		//~ Unit unit = player.getHero();		
        //~ 
		//~ TreeMap<Long, Integer> x = (TreeMap)unit.getX();
		//~ TreeMap<Long, Integer> y = (TreeMap)unit.getY();
		//~ 
		//~ Long[] array = x.keySet().toArray(new Long[x.size()]);
        //~ 
		//~ long[] ticks = new long[x.keySet().size()];
        //~ {
            //~ int index = 0;
            //~ for(Long elt : x.keySet()){
                //~ ticks[index++] = elt.longValue();
            //~ }
        //~ }
		//~ 
		//~ String[][] data = new String[ticks.length][3];	
		//~ 
		//~ for(int i = 0; i < data.length; i++){
			//~ 
			//~ data[i][0] = String.valueOf(ticks[i]);
			//~ data[i][1] = String.valueOf(x.get(ticks[i]));
			//~ data[i][2] = String.valueOf(y.get(ticks[i]));
		//~ }
		//~ 
		//~ return data;
	//~ }
	
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
						System.out.print(data[j][i]);
						System.out.println(",");
						break;
					case 3:
						System.out.print("\"ability\" : ");
						System.out.println(data[j][i]);
						break;
				}
			}
			
			closingDataBlock(j, data.length);
		}
	}
    
    public AbilityLogJson(Player player){
        super(player);
    }

}
