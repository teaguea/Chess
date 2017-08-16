package package1;

import package1.Board.Builder;

public class PawnEnPassan extends PawnAttackMove{

	PawnEnPassan(Board board, Piece movedPiece, int endCoordinate,
			Piece attackedPiece) {
		super(board, movedPiece, endCoordinate, attackedPiece);
	}
	
	@Override
	public Board execute(){
		Board.Builder builder = new Builder();
		
		for(Piece piece : this.board.getCurrentPlayer().getAlivePieces()){
			
			if(!this.movedPiece.equals(piece)){
				builder.setPiece(piece);
			}
		}
		
		for(Piece piece : this.board.getCurrentPlayer().getOpponent().getAlivePieces()){
			if(!piece.equals(this.getAttackedPiece())){
				builder.setPiece(piece);
			}
			
		}
		
		builder.setPiece(this.movedPiece.movePiece(this));
		builder.setNewPlayer(this.board.getCurrentPlayer().getOpponent().getTeam());
		return builder.build();
	}

}
