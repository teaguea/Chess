package chess.engine.pieces;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.google.common.collect.ImmutableList;

import chess.engine.board.Board;
import chess.engine.board.BoardUtils;
import chess.engine.board.Move;
import chess.engine.board.Tile;


/**********************************************************
 * Defines the behavior and values of a Knight Piece.
 * Most importantly this class will define how the Knight
 * will calculate it's moves.
 * @author Aaron Teague
 * I was helped by the sites www.stackoverflow.com, www.stackexchange.com,
 * and chessprogramming.wikispaces.com
 **********************************************************/
public class Knight extends Piece {

	//Array which holds the all the possible coordinate offsets that a Knight can move to. 
	private final static int[] CANDIDATE_MOVE_COORDINATES = {-17, -15, -10, -6, 6, 10, 15, 17};
	
	/****************************************************************
	 * Convenience Constructor for when it's the Bishop's first move.
	 * first move is automatically set to true.
	 * @param pieceTeam
	 * @param piecePosition
	 ****************************************************************/
	public Knight(final Team pieceTeam, final int piecePosition) {
		super(PieceType.KNIGHT, piecePosition, pieceTeam, true);
	}
	
	public Knight(final Team pieceTeam, final int piecePosition, final boolean isFirstMove) {
		super(PieceType.KNIGHT, piecePosition, pieceTeam, isFirstMove);
	}
	
	/******************************************************************
	 * Calculates all the legal moves that a Knight can perform by
	 * looping through CANDIDATE_MOVE_COORDINATES and returns a List of
	 * legal moves.
	 * @param Board board
	 * @return List<Move> legalMoves
	 *****************************************************************/
	@Override
	public Collection<Move> calculateLegalMoves(final Board board) {
		
		//Array list that will hold all of the legal moves a Knight can perform.
		final List<Move> legalMoves = new ArrayList<>();
		
		//Loop through all possible candidate offsets
		for(final int currentCandidateOffset : CANDIDATE_MOVE_COORDINATES){
			
			//Apply the offset to the piece's position
			final int candidateDestinationCoordinate = this.piecePosition + currentCandidateOffset;
			
			if(BoardUtils.isValidTileCoordinate(candidateDestinationCoordinate)){
				
				//Checks to see if the Offset is an edge case exclusion
				if(isFirstColumnExclusion(this.piecePosition, currentCandidateOffset) || 
				   isSecondColumnExclusion(this.piecePosition, currentCandidateOffset) ||
				   isSeventhColumnExclusion(this.piecePosition, currentCandidateOffset) ||
				   isEighthColumnExclusion(this.piecePosition, currentCandidateOffset))
					continue;
				
				
				final Tile candidateDestinationTile = board.getTile(candidateDestinationCoordinate);
				
				//If the tile is not occupied it is legal move and can be add to legalMoves.
				if(!candidateDestinationTile.isTileOccupied())
					//Add the move to legalMoves
					legalMoves.add(new Move.MajorMove(board, this, candidateDestinationCoordinate));
				
				else{
					//Retrieve the piece at that coordinate.
					final Piece pieceAtDestination = candidateDestinationTile.getPiece();
					//Retrieve what team the piece belongs to.
					final Team pieceTeam = pieceAtDestination.getPieceTeam();
					
					//Check if the piece belongs to the same team as the Knight.
					if(this.pieceTeam != pieceTeam)
						//The pieces are not on the same Team so we add an Attack Move to legal moves.
						legalMoves.add(new Move.MajorAttackMove(board, this, candidateDestinationCoordinate, pieceAtDestination));					
				}				
			}	
		}
		
		return ImmutableList.copyOf(legalMoves);
	}
	
	/**************************************
	 * Prints a 'n' to represents a Knight
	 *************************************/
	@Override
	public String toString(){
		return PieceType.KNIGHT.toString();
	}
	
	/***************************************************************************************************
	 * Checks if the move is invalid edge case in the first column
	 * @param currentPosition
	 * @param candidateOffset
	 * @return boolean true or false
	 ***************************************************************************************************/
	private static boolean isFirstColumnExclusion(final int currentPosition, final int candidateOffset){
		
		return BoardUtils.FILE_A[currentPosition] && ((candidateOffset == -17) || (candidateOffset == -10) ||
				(candidateOffset == 6) || (candidateOffset == 15));
	}
	
	/***************************************************************************************************
	 * Checks if the move is invalid edge case in the second column
	 * @param currentPosition
	 * @param candidateOffset
	 * @return boolean true or false
	 ***************************************************************************************************/
	private static boolean isSecondColumnExclusion(final int currentPosition, final int candidateOffset){
		return BoardUtils.FILE_B[currentPosition] && ((candidateOffset == -10) || (candidateOffset == 6));
		
	}
	
	/***************************************************************************************************
	 * Checks if the move is invalid edge case in the seventh column
	 * @param currentPosition
	 * @param candidateOffset
	 * @return boolean true or false
	 ***************************************************************************************************/
	private static boolean isSeventhColumnExclusion(final int currentPosition, final int candidateOffset){
		return BoardUtils.FILE_G[currentPosition] && ((candidateOffset == -6) || (candidateOffset == 10));
		
	}
	
	/***************************************************************************************************
	 * Checks if the move is invalid edge case in the eighth column
	 * @param currentPosition
	 * @param candidateOffset
	 * @return boolean true or false
	 ***************************************************************************************************/
	private static boolean isEighthColumnExclusion(final int currentPosition, final int candidateOffset){
		return BoardUtils.FILE_H[currentPosition] && ((candidateOffset == -15) || (candidateOffset == -6) || 
				(candidateOffset == 10) || (candidateOffset == 17));
		
	}

	/*****************************************************************
	 * Creates and returns a new Knight with an updated coordinate to
	 * reflect the move
	 * @return Knight
	 ******************************************************************/
	@Override
	public Knight movePiece(Move move) {
		return new Knight(move.getMovedPiece().getPieceTeam(), move.getDestinationCoordinate());
	}

}
