package chess.engine.pieces;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.google.common.collect.ImmutableList;

import chess.engine.board.Board;
import chess.engine.board.BoardUtils;
import chess.engine.board.Move;



/******************************************************************************
 * Defines the behavior and values of a Pawn Piece.
 * Most importantly this class will define how the Pawn
 * will calculate it's moves.
 * @author Aaron Teague
 * I was helped by the sites www.stackoverflow.com, www.stackexchange.com,
 * and chessprogramming.wikispaces.com
 ******************************************************************************/
public class Pawn extends Piece{

	//Array which holds the all the possible coordinate offsets that a Pawn can move to. 
	private final static int[] CANDIDATE_MOVE_COORDINATES = {7, 8, 9, 16};
	
	
	/****************************************************************
	 * Convenience Constructor for when it's the Pawn's first move.
	 * first move is automatically set to true.
	 * @param pieceTeam
	 * @param piecePosition
	 ****************************************************************/
	public Pawn(final Team pieceTeam, final int piecePosition) {
		super(PieceType.PAWN, piecePosition, pieceTeam, true);
	}
	
	public Pawn(final Team pieceTeam, final int piecePosition, final boolean isFirstMove) {
		super(PieceType.PAWN, piecePosition, pieceTeam, isFirstMove);
	}

	/********************************************************************
	 * Calculates by the legal moves of a Pawn by looping through all
	 * of the pawns possible CANDIDATE_MOVE_COORDINATES and adding them
	 * to the Pawn's current position.
	 * @return List legalMoves
	 ********************************************************************/
	@Override
	public Collection<Move> calculateLegalMoves(final Board board) {
		
		//Array that will hold of the legal moves for the Pawn
		final List<Move> legalMoves = new ArrayList<>();
		
		//Loop through all possible moves by applying of the offsets to the Pawn's current position
		for(final int currentCandidateOffset: CANDIDATE_MOVE_COORDINATES){
			
			//Apply the offset to the Pawn's current position
			final int candidateDestinationCoordinate = this.piecePosition + (this.getPieceTeam().getDirection() * currentCandidateOffset);
			
			//Checks if the Destination Coordinate is valid
			if(!BoardUtils.isValidTileCoordinate(candidateDestinationCoordinate)){
				continue;
			}
			//Checks if this tile is occupied
			if(currentCandidateOffset == 8 && !board.getTile(candidateDestinationCoordinate).isTileOccupied()){
				if(this.pieceTeam.isPawnPromotionSquare(candidateDestinationCoordinate))
					legalMoves.add(new Move.PawnPromotion(new Move.PawnMove(board, this, candidateDestinationCoordinate)));
				else
					legalMoves.add(new Move.PawnMove(board, this, candidateDestinationCoordinate));	
			}
			//Checks if the Pawn is on it's first move
			else if(currentCandidateOffset == 16 && this.isFirstMove() && ((BoardUtils.SEVENTH_RANK[this.piecePosition] && 
					this.getPieceTeam().isBlack()) || (BoardUtils.SECOND_RANK[this.piecePosition] && this.getPieceTeam().isWhite()))) {
				
				//Calculate coordinate of the tile behind candidate coordinate
				final int behindCandidateDestinationCoordinate = this.piecePosition + (this.pieceTeam.getDirection() * 8);
				
				//Checks if the tile behind the candidate destination is NOT occupied & if the Tile at the Candidate Destination is NOT occupied
				if(!board.getTile(behindCandidateDestinationCoordinate).isTileOccupied() && 
			       !board.getTile(candidateDestinationCoordinate).isTileOccupied())
						legalMoves.add(new Move.PawnJump(board, this, candidateDestinationCoordinate));
					
				
			
			//Checks for edge cases in the 7 direction	
			} else if(currentCandidateOffset == 7 &&
					!((BoardUtils.FILE_H[this.piecePosition] && this.getPieceTeam().isWhite() ||
					(BoardUtils.FILE_A[this.piecePosition] && this.getPieceTeam().isBlack())))){
				
				if(board.getTile(candidateDestinationCoordinate).isTileOccupied()){
					final Piece pieceOnCandidate = board.getTile(candidateDestinationCoordinate).getPiece();
					//If the pieces are not on the same team an Attack move is added to legal moves.
					if(this.getPieceTeam() != pieceOnCandidate.getPieceTeam())
						if(this.pieceTeam.isPawnPromotionSquare(candidateDestinationCoordinate))
							legalMoves.add(new Move.PawnPromotion(new Move.PawnAttackMove(board, this, candidateDestinationCoordinate, pieceOnCandidate)));
						else 
							legalMoves.add(new Move.PawnAttackMove(board, this, candidateDestinationCoordinate, pieceOnCandidate));
						
				//This basically checks if En Passant Pawn is next to Player's pawn	
				} else if(board.getEnPassantPawn() != null){
					if(board.getEnPassantPawn().getPiecePosition() == (this.piecePosition + (this.pieceTeam.getOppositeDirection()))){
						final Piece pieceOnCandidate = board.getEnPassantPawn();
						if(this.pieceTeam != pieceOnCandidate.getPieceTeam()){
							legalMoves.add(new Move.PawnEnPassantAttackMove(board, this, candidateDestinationCoordinate, pieceOnCandidate));
						}
					}
					
				}
				
			//Checks for edge cases in the 9 direction	
			} else if(currentCandidateOffset == 9 && 
					!((BoardUtils.FILE_A[this.piecePosition] && this.getPieceTeam().isWhite() ||
					(BoardUtils.FILE_H[this.piecePosition] && this.getPieceTeam().isBlack())))){
				
				if(board.getTile(candidateDestinationCoordinate).isTileOccupied()){
					final Piece pieceOnCandidate = board.getTile(candidateDestinationCoordinate).getPiece();
					//If the pieces are not on the same team an Attack move is added to legal moves.
					if(this.getPieceTeam() != pieceOnCandidate.getPieceTeam())
						if(this.pieceTeam.isPawnPromotionSquare(candidateDestinationCoordinate))
							legalMoves.add(new Move.PawnPromotion(new Move.PawnAttackMove(board, this, candidateDestinationCoordinate, pieceOnCandidate)));
						else
							legalMoves.add(new Move.PawnAttackMove(board, this, candidateDestinationCoordinate, pieceOnCandidate));
				} else if(board.getEnPassantPawn() != null){
					if(board.getEnPassantPawn().getPiecePosition() == (this.piecePosition - (this.pieceTeam.getOppositeDirection()))){
						final Piece pieceOnCandidate = board.getEnPassantPawn();
						if(this.pieceTeam != pieceOnCandidate.getPieceTeam()){
							legalMoves.add(new Move.PawnEnPassantAttackMove(board, this, candidateDestinationCoordinate, pieceOnCandidate));
						}
					}
					
				}
				
			}
							
		}
		
		return ImmutableList.copyOf(legalMoves);
	}
	
	/**************************************
	 * Prints a 'p' to represents a Pawn
	 *************************************/
	@Override
	public String toString(){
		return PieceType.PAWN.toString();
	}

	/*****************************************************************
	 * Creates and returns a new Pawn with an updated coordinate to
	 * reflect the move
	 * @return Pawn
	 ******************************************************************/
	@Override
	public Pawn movePiece(Move move) {
		return new Pawn(move.getMovedPiece().getPieceTeam(), move.getDestinationCoordinate());
	}
	
	/***********************************************************************************************************
	 * Creates a new queen in place of the pawn. I could have included the option to player to choose
	 * the piece. However for simplicity I decided not to do that. In reality 96% of the time in Grandmaster
	 * matches the Queen is the choosen as the promoted piece.
	 * @return Queen Piece
	 ************************************************************************************************************/
	public Piece getPromotionPiece(){
		return new Queen(this.pieceTeam, this.piecePosition, false);
	}

}
