package package1;

public final class QueenCastle extends Castle{

	QueenCastle(Board board, Piece movedPiece, int endCoordinate, Rook castleRook, int castleRookStart, int castleRookEnd) {
		super(board, movedPiece, endCoordinate, castleRook, castleRookStart, castleRookEnd);
	}

	@Override
	public boolean equals(Object obj) {
	  		return this == obj || obj instanceof QueenCastle && super.equals(obj);
	 }

}
