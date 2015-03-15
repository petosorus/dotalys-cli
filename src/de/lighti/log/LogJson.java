package de.lighti.log;

import de.lighti.model.game.Player;

public abstract class LogJson{
		
		static int id = 0;
        protected Player player;
		
		public void treatingPlayer(){
			openingPlayer(player);
			outData(player);
			closingPlayer();
			
			id++;
		}
		
		public static void reinitId(){ id = 0; }
		
		protected void openingPlayer(Player player){
			System.out.println("{");			//opening player block
			
			System.out.print("\"name\" : ");		//name label
			System.out.println(player.getName() + ", ");	//player name
			
			System.out.print("\"id\" : ");		//id label
			System.out.println(id + ", ");		//id
			
            System.out.print("\"team\" : ");
            System.out.println((player.isRadiant()?"0":"1") + ", ");
            
            System.out.print("\"hero\" : ");
            System.out.println("\"" + player.getHero().getName() + "\""
                                                                + ", ");
            
			System.out.println("\"data\" : ");
			System.out.println("[");			//opening data tab
		}
		
		protected void closingPlayer(){
			System.out.println("]");			//closing coords tab
			System.out.println("}");			//closing player block
		}
		
		protected void openingDataBlock(){
			System.out.println("{");
		}
		
		protected void closingDataBlock(int itemNumber, int dataSize){
			System.out.println("}");
			
			if(itemNumber != (dataSize - 1)){
				System.out.println(",");
			}
		}
		
        public LogJson(Player player){
            this.player = player;
        }
        
		protected abstract void outData(Player player);
		
		protected abstract String[][] getData(Player player);
	
	}
