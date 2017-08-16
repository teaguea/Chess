package package1;

public class NormalAttackMove extends AttackMove{

	NormalAttackMove(Board board, Piece movedPiece, int endCoordinate,
			Piece attackedPiece) {
		super(board, movedPiece, endCoordinate, attackedPiece);
		
	}
	
	@Override
	public boolean equals(Object obj){
		return this == obj || obj instanceof NormalAttackMove && super.equals(obj);
	}
	

}
