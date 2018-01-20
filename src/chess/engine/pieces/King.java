package chess.engine.pieces;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.google.common.collect.ImmutableList;

import chess.engine.board.Board;
import chess.engine.board.BoardUtils;
import chess.engine.board.Move;
import chess.engine.board.Tile;
/****************************************************************************
 * Defines the behavior and values of a King Piece.
 * Most importantly this class will define how the King
 * will calculate it's moves.
 * @author Aaron Teague
 * I was helped by the sites www.stackoverflow.com, www.stackexchange.com,
 * and chessprogramming.wikispaces.com
 *****************************************************************************/
public class King extends Piece{

	//Array which holds the all the possible coordinate offsets that a King can move to. 
	private final static int[] CANDIDATE_MOVE_COORDINATES = {-9, -8, -7, -1, 1, 7, 8, 9};
	private final boolean isCastled;
    private final boolean kingSideCastleCapable;
    private final boolean queenSideCastleCapable;

	
	/****************************************************************
	 * Convenience Constructor for when it's the King's first move.
	 * first move is automatically set to true.
	 * @param pieceTeam
	 * @param piecePosition
	 ****************************************************************/
	public King(final Team pieceTeam, final int piecePosition, final boolean kingSideCastleCapable, final boolean queenSideCastleCapable) {
		super(PieceType.KING, piecePosition, pieceTeam, true);
		this.isCastled = false;
        this.kingSideCastleCapable = kingSideCastleCapable;
        this.queenSideCastleCapable = queenSideCastleCapable;
	}
	
	public King(final Team pieceTeam, 
			    final int piecePosition,
			    final boolean isFirstMove,
			    final boolean isCastled,
			    final boolean kingSideCastleCapable,
			    final boolean queenSideCastleCapable) {
		super(PieceType.KING, piecePosition, pieceTeam, isFirstMove);
		this.isCastled = isCastled;
        this.kingSideCastleCapable = kingSideCastleCapable;
        this.queenSideCastleCapable = queenSideCastleCapable;
	}
	

	/******************************************************************
	 * Calculates all the legal moves that a King can perform by
	 * looping through CANDIDATE_MOVE_COORDINATES and returns a List of
	 * legal moves.
	 * @param Board board
	 * @return List<Move> legalMoves
	 *****************************************************************/
	@Override
	public Collection<Move> calculateLegalMoves(Board board) {
		
		final List<Move> legalMoves = new ArrayList<>();
		
		
		for(final int currentCandidateOffset : CANDIDATE_MOVE_COORDINATES){
			
			final int candidateDestinationCoordinate = this.piecePosition + currentCandidateOffset;
			
			//Checks if it's an edge exception on the First and Eighth columns
			if(isFirstColumnExclusion(this.piecePosition, currentCandidateOffset) ||  
			   isEighthColumnExclusion(this.piecePosition, currentCandidateOffset))
						continue;
			
			
			if(BoardUtils.isValidTileCoordinate(candidateDestinationCoordinate)) {
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
					
					//Check if the piece belongs to the same team as the King.
					if(this.pieceTeam != pieceTeam)
						//The pieces are not on the same Team so we add an Attack Move to legal moves.
						legalMoves.add(new Move.MajorAttackMove(board, this, candidateDestinationCoordinate, pieceAtDestination));	
					
				}
				
			}
			
		}
		
		return ImmutableList.copyOf(legalMoves);
	}
	
	
	/**************************************
	 * Prints a 'k' to represents a King
	 *************************************/
	@Override
	public String toString(){
		return PieceType.KING.toString();
	}
	
	/***************************************************************************************************
	 * Checks if the move is invalid edge case in the first column
	 * @param currentPosition
	 * @param candidateOffset
	 * @return boolean true or false
	 ***************************************************************************************************/
	private static boolean isFirstColumnExclusion(final int currentPosition, final int candidateOffset){
		
		return BoardUtils.FILE_A[currentPosition] && ((candidateOffset == -9) || (candidateOffset == -1) ||
				(candidateOffset == 7));
	}
	
	 public boolean isCastled() {
	        return this.isCastled;
	    }

	    public boolean isKingSideCastleCapable() {
	        return this.kingSideCastleCapable;
	    }

	    public boolean isQueenSideCastleCapable() {
	        return this.queenSideCastleCapable;
	}
	
	/***************************************************************************************************
	 * Checks if the move is invalid edge case in the second column
	 * @param currentPosition
	 * @param candidateOffset
	 * @return boolean true or false
	 ***************************************************************************************************/
	private static boolean isEighthColumnExclusion(final int currentPosition, final int candidateOffset){
		return BoardUtils.FILE_H[currentPosition] && ((candidateOffset == -7) || (candidateOffset == 1) || 
				(candidateOffset == 9));
		
	}

	/*****************************************************************
	 * Creates and returns a new King with an updated coordinate to
	 * reflect the move
	 * @return King
	 ******************************************************************/
	@Override
	public King movePiece(Move move) {
		return new King(move.getMovedPiece().getPieceTeam(), move.getDestinationCoordinate(), false, move.isCastlingMove(), false, false );
	}

}
