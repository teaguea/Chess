package package1;

public class PawnMove extends Move{
	
		
		/*******************************************
		 * Constructor Creates a PawnMove Object
		 * @param board
		 * @param movedPiece
		 * @param endCoordinate
		 ********************************************/
		PawnMove(Board board, Piece movedPiece, int endCoordinate) {
			super(board, movedPiece, endCoordinate);
			
		}
		
		
		  public boolean equals(Object obj) {
		  		return this == obj || obj instanceof PawnMove && super.equals(obj);
		  }
		 
		
		
}
