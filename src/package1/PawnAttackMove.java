package package1;

public class PawnAttackMove extends AttackMove{

	PawnAttackMove(Board board, Piece movedPiece, int endCoordinate, Piece attackedPiece) {
		super(board, movedPiece, endCoordinate, attackedPiece);
	}
	
	
	@Override
	public boolean equals(Object obj){
		return this == obj || obj instanceof PawnAttackMove && super.equals(obj);
		
	}
	
	
}
