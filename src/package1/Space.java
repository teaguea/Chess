/*************************************************************************
 * The Space class is an abstract class that sets
 * up basic variables and functions that the EmptySpace class
 * and UsedSpace class will use.  
 *@author Aaron Teague 
 *I was helped by stackoverflow.com and chessprogramming.wikispaces.com/
 **************************************************************************/

package package1;
import java.util.HashMap;
import java.util.Map;

public abstract class Space {
	
	//The coordinate of the space
    protected final int spaceCoordinate;
	
    
	private static Map<Integer, EmptySpace>EMPTY_SPACES = createAllEmptySpaces();
    
	/************************************************
	 * Creates the space and assigns the coordinate.
	*************************************************/
	protected Space(int spaceCoordinate){
		this.spaceCoordinate = spaceCoordinate; 
	}
	
	/************************************************************
	 * Returns the used space if the piece is not null, otherwise
	 * it will return the empty space
	 * @param spaceCoordinate
	 * @param piece
	 * @return
	 *************************************************************/
	public static Space createSpace(final int spaceCoordinate, final Piece piece) {
		if(piece != null)
			return new UsedSpace(spaceCoordinate, piece);
		else
			return EMPTY_SPACES.get(spaceCoordinate); 
	}
	
	/**********************************
	 * Get the coordinate of the space
	 * @return the space's coordinate
	 *********************************/
	public int getSpaceCoordinate(){
		return this.spaceCoordinate;
	}
	
	private static Map<Integer, EmptySpace> createAllEmptySpaces() {
		Map<Integer, EmptySpace> emptySpaceMap = new HashMap<>();
		
		//creates 64 empty spaces
		for(int i = 0; i < 64; i++)
			emptySpaceMap.put(i, new EmptySpace(i));
		
		//returns emptySpaceMap
		return emptySpaceMap;
	}

	/***************************************
	 * Checks if a piece is using this space.
	 * @return
	 ***************************************/
	public abstract boolean isSpaceUsed();
	
	/**********************************************
	 * Retrieves the space that is using the space.
	 **********************************************/
	public abstract Piece getPiece();
	
}
