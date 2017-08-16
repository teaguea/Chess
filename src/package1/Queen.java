/*******************************************************************************
* The Queen Class represents Queen piece in the chess game. The fields included in
* class are the Queen's position, the team it is playing for, a special enum Type, and
* finally whether or not it's the Queen's first move. The class' two function are to
* calculate the Queen's legal moves and move the Queen to it's new position if has been
* moved. 
* @author Aaron Teague
* I was helped by stackoverflow.com and chessprogramming.wikispaces.com/
*******************************************************************************/
package package1;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Queen extends Piece{
	

	/********************************************************************************
	 * All possible moves that a Queen can perform.
	 * Special Note: Since the queen is a cross between a Bishop and Rook, the Queen's possible 
	 * moves is therefore simply a Union and a Bishop.
	 *********************************************************************************/
	private final static int[] POSSIBLE_MOVE_COORDINATES = {9, 8, 7, 1, -1, -8, -7, -9};

	/************************************
	 * Creates a new Queen object.
	 * @param int piecePosition
	 * @param Team pieceTeam
	 ************************************/
	Queen(int piecePosition, Team pieceTeam) {
		super(piecePosition, pieceTeam, PieceType.QUEEN, true);
		
	}
	
	Queen(int piecePosition, Team pieceTeam, boolean isFirst) {
		super(piecePosition, pieceTeam, PieceType.QUEEN, isFirst);
		
	}

	/*******************************************************************
	 * The function will loop through all of the possible coordinates
	 * the Queen can move to. If the move is legal it will be added
	 * to a list of legal moves
	 * @return legalMoves
	 *******************************************************************/
	@Override
	public Collection<Move> calcLegalMove(final Board board) {

		final List<Move> legalMoves = new ArrayList<>();
		
		for(int i = 0; i < 8; i++){
			int possibleEndCoordinate = this.piecePosition;
			
			//Calculates all possible legal moves for the Queen
			while(Board.isValidSpaceCoordinate(possibleEndCoordinate)){
				
				//Checks to make sure that the move is legal from the First and Eight row
				if(firstColumn(possibleEndCoordinate, POSSIBLE_MOVE_COORDINATES[i]) || 
						eighthColumn(possibleEndCoordinate, POSSIBLE_MOVE_COORDINATES[i])){
					break;
				}
				
				
				possibleEndCoordinate += POSSIBLE_MOVE_COORDINATES[i];
				
				if(Board.isValidSpaceCoordinate(possibleEndCoordinate)){
					final Space possibleEndSpace = board.getSpace(possibleEndCoordinate);
					
					//Checks if the piece will land on an empty space
					if(!(possibleEndSpace.isSpaceUsed())){
						legalMoves.add(new NormalMove(board, this, possibleEndCoordinate));
					}
					
					else{
						//Assigns the piece at the end of the move to pieceAtEnd
						final Piece pieceAtEnd = possibleEndSpace.getPiece();
						//Saves the team of the piece occupying the end space to pieceTeam
						final Team pieceTeam = pieceAtEnd.getTeam();
						
						//Checks if the Queen and the piece at the end space aren't part of the same Team
						if(this.pieceTeam != pieceTeam) {
							//If they aren't the same it means the Queen can occupy the space.
							//And the move is added to the legal moves list.
							legalMoves.add(new NormalAttackMove(board, this, possibleEndCoordinate, pieceAtEnd));
						}
						//We know there is a Piece in front of the Queen's path we then need to stop regardless of the Team
						break;
				    }
			     }
			  }
	       }
		//returns the calculated list of legal moves.
		return legalMoves;
     }
	
	@Override
	public String toString() {
		return PieceType.QUEEN.toString();
	}
	
	/****************************************************************
	 * Checks if the Queen's moves breaks down in the first column
	 ***************************************************************/
	private static boolean firstColumn(final int currentPosition, final int endPosition){
		return Board.FIRST_COLUMN.get(currentPosition) && (endPosition == -1 || endPosition == 7 || endPosition == -9);
	}
	
	/*****************************************************************
	 * Checks if the Queen's moves breaks down in the eighth column.
	 *****************************************************************/
	private static boolean eighthColumn(final int currentPosition, final int endPosition){
		return Board.EIGHTH_COLUMN.get(currentPosition) && (endPosition == 1 || endPosition == -7 || endPosition == 9);		
	}

	/**************************************************************
	 * Moves the Queen to a new space after it has been moved
	 * @param Move move
	 * @return A new Queen object at the position it was moved to.
	 **************************************************************/
	@Override
	public Piece movePiece(Move move) {
		//Creates a new Queen object
		return new Queen(move.getEndCoordinate(), move.getMovedPiece().getTeam());
	}

}
