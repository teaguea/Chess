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
 * Defines the behavior and values of a Queen Piece.
 * Most importantly this class will define how the Queen
 * will calculate it's moves.
 * @author Aaron Teague
 * I was helped by the sites www.stackoverflow.com, www.stackexchange.com,
 * and chessprogramming.wikispaces.com
 **********************************************************/
public class Queen extends Piece {

	//All possible vectors that the Bishop will be able to move on.
	private final static int[] CANDIDATE_MOVE_VECTOR_COORDINATES = {-9, -8, -7, -1, 1, 7, 8, 9};
	
	/****************************************************************
	 * Convenience Constructor for when it's the Queen's first move.
	 * first move is automatically set to true.
	 * @param pieceTeam
	 * @param piecePosition
	 ****************************************************************/
	public Queen(final Team pieceTeam, final int piecePosition) {
		super(PieceType.QUEEN, piecePosition, pieceTeam, true);
	}
	
	public Queen(final Team pieceTeam, final int piecePosition, final boolean isFirstMove) {
		super(PieceType.QUEEN, piecePosition, pieceTeam, isFirstMove);
	}

	/******************************************************************
	 * Calculates all the legal moves that a Bishop can perform by
	 * looping through CANDIDATE_MOVE_COORDINATES and returns a List of
	 * legal moves.
	 * @param Board board
	 * @return List<Move> legalMoves
	 *****************************************************************/
	@Override
	public Collection<Move> calculateLegalMoves(final Board board) {
		
		final List<Move> legalMoves = new ArrayList<>();
		
		//Loop through each possible vector coordinate
		for(final int candidateCoordinateOffset: CANDIDATE_MOVE_VECTOR_COORDINATES){
			
			//Possible end position of the Bishop
			int candidateDestinationCoordinate = this.piecePosition;
			
			//Continues to apply the vector offset to the position of the Queen to test for legal moves
			while(BoardUtils.isValidTileCoordinate(candidateDestinationCoordinate)){
				
				//Checks for edge cases in First and Eighth Columns
				if(isFirstColumnExclusion(candidateDestinationCoordinate, candidateCoordinateOffset) ||
				   isEighthColumnExclusion(candidateDestinationCoordinate, candidateCoordinateOffset))
					break;
					
				//Add the offset to the destination to get the new destination.
				candidateDestinationCoordinate += candidateCoordinateOffset;
				
				if(BoardUtils.isValidTileCoordinate(candidateDestinationCoordinate)){
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
						
						//Check if the piece belongs to the same team as the Queen.
						if(this.pieceTeam != pieceTeam)
							//The pieces are not on the same Team so we add an Attack Move to legal moves.
							legalMoves.add(new Move.MajorAttackMove(board, this, candidateDestinationCoordinate, pieceAtDestination));
						
						break;
					}
				}
			}
		}
		
		return ImmutableList.copyOf(legalMoves);
	}
	
	/**************************************
	 * Prints a 'q' to represents a Queen
	 *************************************/
	@Override
	public String toString(){
		return PieceType.QUEEN.toString();
	}
	
	/***************************************************************************************************
	 * Checks if the move is invalid edge case in the first column
	 * @param currentPosition
	 * @param candidateOffset
	 * @return boolean true or false
	 ***************************************************************************************************/
	private static boolean isFirstColumnExclusion(final int currentPosition, final int candidateOffset){
		
		return BoardUtils.FILE_A[currentPosition] && ((candidateOffset == -1) || (candidateOffset == -9) || (candidateOffset == 7));
	}
	
	/***************************************************************************************************
	 * Checks if the move is invalid edge case in the eighth column
	 * @param currentPosition
	 * @param candidateOffset
	 * @return boolean true or false
	 ***************************************************************************************************/
	private static boolean isEighthColumnExclusion(final int currentPosition, final int candidateOffset){
		return BoardUtils.FILE_H[currentPosition] && ((candidateOffset == 1) || (candidateOffset == -7) || (candidateOffset == 9));
		
	}

	/*****************************************************************
	 * Creates and returns a new Queen with an updated coordinate to
	 * reflect the move
	 * @return Queen
	 ******************************************************************/
	@Override
	public Queen movePiece(Move move) {
		return new Queen(move.getMovedPiece().getPieceTeam(), move.getDestinationCoordinate());
	}
}
