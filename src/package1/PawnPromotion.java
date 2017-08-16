package package1;
import package1.Board.Builder;

/****************************************************************************
 * Handles exchanging a Pawn, once it reaches the opposite of
 * the board, for a Queen
 * @author Aaron Teague
 * Helped by stackoverflow.com and chessprogramming.wikispaces.com/
 ****************************************************************************/
public class PawnPromotion extends PawnMove{
	Move move;
	Pawn promotedPawn;
	
	/**********************************
	 * Creates a PawnPromotion object
	 * @param move 
	 **********************************/
	PawnPromotion(Move move) {
		super(move.getBoard(), move.getMovedPiece(), move.getEndCoordinate());
		this.move = move;
		this.promotedPawn = (Pawn) move.getMovedPiece();
	}
	
	/***************************************
	 * Executes the actual pawn promotion.
	 ***************************************/
	@Override
	public Board execute(){
		
		//Initializes the new board created by using the execute from the move that was passed in
		Board pawnMoved = this.move.execute();
		Board.Builder builder = new Builder();
		
		//set the current's player pieces on the board
		for(Piece piece : pawnMoved.getCurrentPlayer().getAlivePieces()){
			//check to make sure the promoted piece isn't set on the board
			if(!this.promotedPawn.equals(piece)){
				builder.setPiece(piece);
			}
		}
		
		//set the opponent's pieces on the board
		for(Piece piece : pawnMoved.getCurrentPlayer().getOpponent().getAlivePieces()){
			
			builder.setPiece(piece);
			
		}
		//sets the new piece that was promoted by the pawn on the board
		builder.setPiece(this.promotedPawn.getPromotedPiece().movePiece(this));
		//sets the new player
		builder.setNewPlayer(pawnMoved.getCurrentPlayer().getTeam());
		//return the new board
		return builder.build();
	}
	
	
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((move == null) ? 0 : move.hashCode());
		result = prime * result
				+ ((promotedPawn == null) ? 0 : promotedPawn.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		return this == obj || obj instanceof PawnPromotion && (super.equals(obj));
	}

	/*************************************************************
	 * @return True if the move is an attack, false if it's not
	 *************************************************************/
	@Override
	public boolean isAttack(){
		return this.move.isAttack();
	}
	
	/***************************************
	 * @return The piece that was attacked.
	 ***************************************/
	@Override
	public Piece getAttackedPiece(){
		return this.move.getAttackedPiece();
	}
	
	

}
