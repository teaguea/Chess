/*******************************************************************************
* The Pawn Class represents the Pawn piece in the chess game. The fields included in
* class are the Pawn's position, the team it is playing for, a special enum Type, and
* finally whether or not it's the King's first move. The class' two functions are to
* calculate the Pawn's legal moves and move the Pawn to it's new position if has been
* moved. The Pawn class also calculates if the Pawn is doing two space or one space 
* jump. The Pawn class calculates Pawn promotion, for simplicity purposes the pawn
* is automatically promoted to a Queen. Finally the Pawn class also calculates En Passant
* moves
* @author Aaron Teague
* I was helped by stackoverflow.com and chessprogramming.wikispaces.com/
*******************************************************************************/

package package1;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Pawn extends Piece{
	
	//Represents the number of spaces the Pawn can move
	private final static int[] POSSIBLE_MOVE_COORDINATES = {8, 7, 16, 9};

	/**************************************************************
	 * Creates a new Pawn object and designates it as a first move.
	 * @param int piecePosition
	 * @param Team pieceTeam
	 **************************************************************/
	Pawn(int piecePosition, Team pieceTeam) {
		super(piecePosition, pieceTeam, PieceType.PAWN, true);
		
	}
	
	/***********************************************************
	 * Creates a new Pawn object.
	 * @param piecePosition
	 * @param pieceTeam
	 * @param isFirst
	 ***********************************************************/
	Pawn(int piecePosition, Team pieceTeam, boolean isFirst) {
		super(piecePosition, pieceTeam, PieceType.PAWN, isFirst);
		
	}

	/***********************************************************************************************
	 * Loops through all possible moves the pawn can do. Then determines if the moves are possible.
	 * The function will also calculates En Passant moves as well as Pawn promotions,
	 * @param board
	 * @return legalMoves
	 ***********************************************************************************************/
	@Override
	public Collection<Move> calcLegalMove(Board board) {
	
		List<Move> legalMoves = new ArrayList<>();
		
		for(int i = 0; i < 4; i++){
			
			int possibleEndCoordinate = this.piecePosition + (this.getTeam().getDirection() * POSSIBLE_MOVE_COORDINATES[i]);
			
			if(!Board.isValidSpaceCoordinate(possibleEndCoordinate)){
				continue;
			}
			
			//Checks and handles if the Pawn is doing a one space jump to an empty space
			if(POSSIBLE_MOVE_COORDINATES[i] == 8 && !board.getSpace(possibleEndCoordinate).isSpaceUsed()){
				
				//checks if the pawn is on a promotion space
				if(this.pieceTeam.pawnPromotionSpace(possibleEndCoordinate)){
					legalMoves.add(new PawnPromotion(new PawnMove(board, this, possibleEndCoordinate)));
				}
				//otherwise the gets added to legal moves as just an ordinary pawn move.
				else{
					legalMoves.add(new PawnMove(board, this, possibleEndCoordinate));
				}
			
			
			//Checks and handles if the Pawn is doing a two space jump to an empty space
			} else if (POSSIBLE_MOVE_COORDINATES[i] == 16 && this.getFirst() &&
                    ((Board.SECOND_ROW.get(this.piecePosition) && this.getTeam().Black()) ||
                     (Board.SEVENTH_ROW.get(this.piecePosition) && this.getTeam().White()))) {
                int behindCandidateDestinationCoordinate =
                        this.piecePosition + (this.getTeam().getDirection() * 8);
                if (!board.getSpace(possibleEndCoordinate).isSpaceUsed() &&
                    !board.getSpace(behindCandidateDestinationCoordinate).isSpaceUsed()) {
                	//System.out.println("Move Added " + possibleEndCoordinate);
                    legalMoves.add(new PawnJump(board, this, possibleEndCoordinate));
               }
					
			
			
			/****************************************************************
			 * EXCEPTION CASES
			A White Pawn on the eight column can't move in the 7 direction.
			A Black Pawn on the first column can't move in the 7 direction.
			****************************************************************/
			} else if(POSSIBLE_MOVE_COORDINATES[i] == 7 &&
					!((Board.EIGHTH_COLUMN.get(this.piecePosition) && this.getTeam().White() || 
					(Board.FIRST_COLUMN.get(this.piecePosition) && this.getTeam().Black() )))){
				
				if(board.getSpace(possibleEndCoordinate).isSpaceUsed()){
					
					Piece possiblePiece = board.getSpace(possibleEndCoordinate).getPiece();
					
					if(this.getTeam() != possiblePiece.getTeam()){
						
						//checks if the pawn is now on a promotion space
						if(this.pieceTeam.pawnPromotionSpace(possibleEndCoordinate)){
							
							legalMoves.add(new PawnPromotion(new PawnAttackMove(board, this, possibleEndCoordinate, possiblePiece)));
							
						}
						else {
							legalMoves.add(new PawnAttackMove(board, this, possibleEndCoordinate, possiblePiece));
						}
					}
					
				/*******************************************
				 * Start Checking if En Passant is possible	
				 *******************************************/
				  //Checks if the board has an En Passant Pawn	
				} else if(board.getEnPassantPawn() != null) {
					
					//Checks if the opposing piece is direct right or left
					if(board.getEnPassantPawn().getPiecePosition() == (this.piecePosition + (this.pieceTeam.getOpposingDirection()))){
						
						Piece possiblePiece = board.getEnPassantPawn();
						
						//Checks if pieces are on opposing teams
						if(this.pieceTeam != possiblePiece.getTeam()) {
						
								legalMoves.add(new PawnEnPassan(board, this, possibleEndCoordinate, possiblePiece));
							
						}
						
					}
					
				}
				
			/****************************************************************
			 * EXCEPTION CASES
			A Black Pawn on the eight column can't move in the 9 direction.
			A White Pawn on the first column can't move in the 9 direction.
			****************************************************************/
			} else if(POSSIBLE_MOVE_COORDINATES[i] == 9 &&
					!((Board.FIRST_COLUMN.get(this.piecePosition) && this.getTeam().White() || 
							(Board.EIGHTH_COLUMN.get(this.piecePosition) && this.getTeam().Black() )))){
					if(board.getSpace(possibleEndCoordinate).isSpaceUsed()){
					
					Piece possiblePiece = board.getSpace(possibleEndCoordinate).getPiece();
					
					if(this.getTeam() != possiblePiece.getTeam()){
						//checks if the pawn is now on a promotion space
						if(this.pieceTeam.pawnPromotionSpace(possibleEndCoordinate)){
							
							legalMoves.add(new PawnPromotion(new PawnAttackMove(board, this, possibleEndCoordinate, possiblePiece)));	
						}
						else {
							
							legalMoves.add(new PawnAttackMove(board, this, possibleEndCoordinate, possiblePiece));
						}
					}
				  //checks if the board has an En Passant Pawn
				} else if(board.getEnPassantPawn() != null) {
						
					//Checks if the opposing piece is direct right or left
						if(board.getEnPassantPawn().getPiecePosition() == (this.piecePosition - (this.pieceTeam.getOpposingDirection()))){
							
							Piece possiblePiece = board.getEnPassantPawn();
							
							//Checks if pieces are on opposing teams
							if(this.pieceTeam != possiblePiece.getTeam()) {
								legalMoves.add(new PawnEnPassan(board, this, possibleEndCoordinate, possiblePiece));
							}
							
						}
						
					}
							
			}
		}
		
		//returns the calculated list of legal moves
		return legalMoves;
	}
	
	@Override
	public String toString() {
		return PieceType.PAWN.toString();
	}

	/**************************************************************
	 * Moves the Pawn to a new space after it has been moved
	 * @param Move move
	 * @return A new Pawn object at the position it was moved to.
	 **************************************************************/
	@Override
	public Piece movePiece(Move move) {
		return new Pawn(move.getEndCoordinate(), move.getMovedPiece().getTeam());
	}
	
	
	/***************************************************************************
	 * For simplicity sake the function automatically returns a new Queen piece
	 * @return A Queen piece
	 ***************************************************************************/
	public Piece getPromotedPiece(){
		return new Queen(this.piecePosition, this.getTeam(), false);
	}
}
