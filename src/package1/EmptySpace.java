package package1;
/*******************************************************************************
* The EmptySpace class handles various function to distinguish it from a spaced
* that is used by a piece.
* @author Aaron Teague
* I was helped by stackoverflow.com and chessprogramming.wikispaces.com/
*******************************************************************************/
public final class EmptySpace extends Space {
	
	/***************************************************
	 * Creates a empty space at the given coordinate.
	 * @param spaceCoordinate
	 ***************************************************/
	
	EmptySpace(final int spaceCoordinate){
		super(spaceCoordinate);
	}
	
	@Override
	/******************************************************
	The space is not being used it will always return false
	*******************************************************/
	public boolean isSpaceUsed() {
		return false;
	}
	
	@Override
	/******************************************************
	The space has no piece using it therefore it
	will always return null.
	*******************************************************/
	public Piece getPiece(){
		return null;
	}
	
	/*********************************************
	 * A to string method for debugging purposes
	 *********************************************/
	@Override
	public String toString(){
		return "-";
	}

}