package package1;
/*******************************************************************************
 * This class helps with the En Passan move.
 * @author Aaron Teague
 * I was helped by stackoverflow.com and chessprogramming.wikispaces.com/
 *******************************************************************************/
public class NormalMove extends Move {

	/********************************************
	 * Constructor creates a Normal Move object
	 * @param Board board
	 * @param Piece movedPiece
	 * @param int endCoordinate
	 ********************************************/
	NormalMove(Board board, Piece movedPiece, int endCoordinate) {
		
		super(board, movedPiece, endCoordinate);
		
	}

	@Override
	public boolean equals(Object obj){
		return this == obj || obj instanceof NormalMove && super.equals(obj);
	}
	

}
