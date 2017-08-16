package package1;
/********************************************************
 * When a move is made the board will change.
 * This class will insure the all the correct data from
 * the old board will be saved to the new board.
 * 
 * @author Aaron Teague
 *
 ********************************************************/
public class MoveChange {
		private Board changeBoard;
		private MoveState moveState;
		private Move move;
		
		public MoveChange(Move move, MoveState moveState, Board changeBoard){
			this.move = move;
			this.moveState = moveState;
			this.changeBoard = changeBoard; 
			
		}
		
		public MoveState getMoveState(){
			return this.moveState;
		}

		/*************************************************	
		 * Get the board that has been changed by a move
		 * @return altered board
		 *************************************************/
		public Board getChangeBoard() {
			return this.changeBoard;
		}
}
