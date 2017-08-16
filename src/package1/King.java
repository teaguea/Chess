/*******************************************************************************
* The King Class represents King piece in the chess game. The fields included in
* class are the King's position, the team it is playing for, a special enum Type, and
* finally whether or not it's the King's first move. The class' two functions are to
* calculate the King's legal moves and move King to it's new position if has been
* moved. 
* @author Aaron Teague
* I was helped by stackoverflow.com and chessprogramming.wikispaces.com/
*******************************************************************************/


package package1;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


public class King extends Piece{

	/**********************************************************************
	 * Creates a new King object and designates the King is in first move.
	 * @param int piecePosition
	 * @param Team pieceTeam
	 * @param Team pieceTeam
	 **********************************************************************/
	King(int piecePosition, Team pieceTeam) {
		super(piecePosition, pieceTeam, PieceType.KING, true);
		
	}
	
	King(int piecePosition, Team pieceTeam, boolean isFirst) {
		super(piecePosition, pieceTeam, PieceType.KING, isFirst);
		
	}
	
	//Represents the number of spaces the King can move
	private final static int[] POSSIBLE_MOVE_COORDINATES = {9, 8, 7, 1, -1, -7, -8, -9};
	
	/**************************************************
	 * Calculates the legal moves the King can move
	 * @param board
	 * @return legalMoves, all the moves the King can do.
	 ****************************************************/
	@Override
	public Collection<Move> calcLegalMove(Board board) {
		
		List<Move> legalMoves = new ArrayList<>();
		
		
		//Loops through each possible move and checks if the space is occupied or not
		for(int i = 0; i < 8; i++){
			
			int possibleEndCoordinate = this.piecePosition + POSSIBLE_MOVE_COORDINATES[i];
			
			//Checks to make sure that the move is legal from the First and Eighth column
			if(firstColumn(this.piecePosition, POSSIBLE_MOVE_COORDINATES[i]) || 
			   eightColumn(this.piecePosition, POSSIBLE_MOVE_COORDINATES[i])){
				continue;
			}
			
			
			if(Board.isValidSpaceCoordinate(possibleEndCoordinate)){
				Space possibleEndSpace = board.getSpace(possibleEndCoordinate);
				
				//Checks if the piece will land on an empty space
				if(!(possibleEndSpace.isSpaceUsed())){
					legalMoves.add(new NormalMove(board, this, possibleEndCoordinate));
				}
				
				
				else{
					//Assigns the piece at the end of the move to pieceAtEnd
					Piece pieceAtEnd = possibleEndSpace.getPiece();
					//Saves the team of the piece occupying the end space to pieceTeam
					Team pieceTeam = pieceAtEnd.getTeam();
					
					//Checks if the King and the piece at the end space aren't part of the same Team
					if(this.pieceTeam != pieceTeam) {
						/****************************************************************
						If they aren't the same it means the knight can occupy the space.
						And the move is added to the legal moves list.
						****************************************************************/
						legalMoves.add(new NormalAttackMove(board, this, possibleEndCoordinate, pieceAtEnd));
					}
					
				}
				
			}
			
		}
		
		return legalMoves;
	}
	
	@Override
	public String toString() {
		return PieceType.KING.toString();
	}
	
	/****************************************************************
	 * Checks if the King's moves breaks down in the first column
	 ***************************************************************/
	private static boolean firstColumn(final int currentPosition, final int endPosition){
		return Board.FIRST_COLUMN.get(currentPosition) && ((endPosition == 7) || (endPosition == -9) || 
				(endPosition == -1));
	}
	
	/*****************************************************************
	 * Checks if the King's moves breaks down in the second column.
	 *****************************************************************/
	private static boolean eightColumn(final int currentPosition, final int endPosition){
		return Board.EIGHTH_COLUMN.get(currentPosition) && ((endPosition == -7) || (endPosition == 9) || 
				(endPosition == 1));		
	}

	/**************************************************************
	 * Moves the King to a new space after it has been moved
	 * @param Move move
	 * @return A new King object at the position it was moved to.
	 **************************************************************/
	@Override
	public Piece movePiece(Move move) {
		//Creates a new King object
		return new King(move.getEndCoordinate(), move.getMovedPiece().getTeam());
	}
}


