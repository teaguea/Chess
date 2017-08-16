package package1;

public class AttackMove extends Move{
	
	//The piece that will be attacked
	final Piece attackedPiece;
	
	/*******************************************
	 * Constructor Creates an AttackMove Object
	 * @param board
	 * @param movedPiece
	 * @param endCoordinate
	 * @param attackedPiece
	 ********************************************/
	AttackMove(Board board, Piece movedPiece, int endCoordinate, Piece attackedPiece) {
		super(board, movedPiece, endCoordinate);
		this.attackedPiece = attackedPiece;
	}
	
	/*******************************************
	 * @return True that this is an attack move
	 *******************************************/
	@Override
	public boolean isAttack(){
		return true;
	}
	
	@Override
	public Piece getAttackedPiece(){
		return this.attackedPiece;
	}
	
	@Override
	public int hashCode(){
		return this.attackedPiece.hashCode() + super.hashCode();
	}

	@Override
	public boolean equals(Object x){
		if(this == x){
			return true;
		}
		
		if(!(x instanceof AttackMove)){
			return false;
		}
		
		AttackMove y = (AttackMove) x;
		return super.equals(y) && getAttackedPiece().equals(y.getAttackedPiece());
			
	}

}
