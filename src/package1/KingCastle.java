package package1;
/*******************************************************
 * 
 * @author Aaron Teague
 *
 *******************************************************/

public final class KingCastle extends Castle{

	KingCastle(Board board, Piece movedPiece, int endCoordinate, Rook castleRook, int castleRookStart, int castleRookEnd) {
		super(board, movedPiece, endCoordinate, castleRook, castleRookStart, castleRookEnd);
	}
	
	@Override
	public boolean equals(Object obj) {
	  		return this == obj || obj instanceof KingCastle && super.equals(obj);
	 }
	 
}
	
	


