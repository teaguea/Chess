package package1;
import package1.Board.Builder;
/*******************************************************************************
 * This PawnJump class main purpose is to indicate to the board
 * that potential En Passant move is possible.
 * @author Aaron Teague
 * I was helped by stackoverflow.com and chessprogramming.wikispaces.com/
 *******************************************************************************/
public class PawnJump extends Move{

	/***********************************************************************
	 * Creates a PawnJump move
	 * @param board, the board that is being played on
	 * @param movedPiece, the piece that was moved
	 * @param endCoordinate, represents the coordinate where the piece will
	 * 						 land after it's moved
	 ***********************************************************************/
	PawnJump(Board board, Piece movedPiece, int endCoordinate) {
		super(board, movedPiece, endCoordinate);
	}
	
	/************************************************
	 * Executes the pawn jump move
	 * @return build, the board after the pawn jump
	 ************************************************/
	@Override
	public Board execute() {
		//Creates a builder to a create a new board
		 Board.Builder builder = new Builder();
		 /*********************************************************************
		 loops through all the current player's piece that are still available
		 and sets the piece on the board
		 **********************************************************************/
         for (Piece piece : this.board.getCurrentPlayer().getAlivePieces()) {
        	 //checks if the piece is the piece that was moved by the player
             if (!this.movedPiece.equals(piece)) {
                 builder.setPiece(piece);
             }
         }
         //This loop sets the opponents pieces in the same way as the current player
         for (final Piece piece : this.board.getCurrentPlayer().getOpponent().getAlivePieces()) {
        	 //No check is need because no piece is moved
             builder.setPiece(piece);
         }
         //creates pawn object to represent the pawn that was moved
         Pawn movedPawn = (Pawn)this.movedPiece.movePiece(this);
         //set the moved pawn on the board
         builder.setPiece(movedPawn);
         //sets the moved pawn as possible to captured by En Passant
         builder.setEnPassantPawn(movedPawn);
         //switches the current player to the opponent
         builder.setNewPlayer(this.board.getCurrentPlayer().getOpponent().getTeam());
         return builder.build();
		
	}

}
