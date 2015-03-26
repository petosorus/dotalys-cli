package de.lighti.log;

import de.lighti.model.AppState;
import de.lighti.model.game.Player;
import de.lighti.Dotalys2App;

public abstract class LogJson{
		
		static int id = 0;
        protected Player player;
        protected AppState state;
		
		public void treatingPlayer(int nb){
            {
                Dotalys2App app = new Dotalys2App();
            }
			openingPlayer(player);
			outData(player);
			closingPlayer(nb);
			
			id++;
		}
		
		public static void reinitId(){ id = 0; }
		
		protected void openingPlayer(Player player){
			System.out.println("{");			//opening player block
			
			System.out.print("\"name\" : ");		//name label
			System.out.println("\"" + player.getName() + "\", ");	//player name
			
			System.out.print("\"id\" : ");		//id label
			System.out.println(id + ", ");		//id
			
            System.out.print("\"team\" : ");
            System.out.println((player.isRadiant()?"0":"1") + ", ");

            System.out.print("\"hero_name\" : ");
            System.out.println("\"" + player.getHero().getName() + "\"" + ", ");
            
            System.out.print("\"hero_localised_name\" : ");
            System.out.println("\"" + state.getHeroName(player.getHero().getName()) + "\""
                                                                + ", ");

            System.out.print("\"hero_id\" : ");
            System.out.println(state.getHeroId(player.getHero().getName()) + ", ");
            
			System.out.println("\"data\" : ");
			System.out.println("[");			//opening data tab
		}
		
		protected void closingPlayer(int nb){
			System.out.println("]");			//closing coords tab
			System.out.println("}");			//closing player block
			if (id < nb - 1) {
				System.out.println(",");
			}
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
		
        public LogJson(Player player, AppState state){
            this.player = player;
            this.state = state;
        }
        
		protected abstract void outData(Player player);
		
		protected abstract String[][] getData(Player player);
	
	}
