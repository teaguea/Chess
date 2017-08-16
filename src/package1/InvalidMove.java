package package1;
/*******************************************************************************
* The InvalidMove represents a move that is not possible to execute in the game
* @author Aaron Teague
* I was helped by stackoverflow.com and chessprogramming.wikispaces.com/
*******************************************************************************/

public class InvalidMove extends Move{

	/*********************************************
	 * Creates a move with a null board and a 65
	 * end coordinate. Thus an invalid move.
	 *********************************************/
	public InvalidMove() {
		super(null, 65);
		
	}
	
	/*****************************************************************
	 * Throws an exception because it's not possible to execute move.
	 *****************************************************************/
	@Override
	public Board execute(){
		throw new RuntimeException("Can't execute move");
	}
	
	/********************************************
	 * Return a -1, an invalid end coordinate.
	 ********************************************/
	@Override
	public int getCurrentCoordinate(){
		
		return -1;
		
	}

}
