/*******************************************************************************
* The Rook Class represents the Rook piece in the chess game. The fields included in
* class are the Rook's position, the team it is playing for, a special enum Type, and
* finally whether or not it's the Rook's first move. The class' two function are to
* calculate the Queen's legal moves and move the Queen to it's new position if has been
* moved. 
* @author Aaron Teague
* I was helped by stackoverflow.com and chessprogramming.wikispaces.com/
*******************************************************************************/

package package1;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Rook extends Piece {
	
	//All possible moves that a Rook can perform.
	private static final int[] POSSIBLE_MOVE_COORDINATES = {8, 1, -1, -8};

	public Rook(int piecePosition, Team pieceTeam) {
		super(piecePosition, pieceTeam, PieceType.ROOK, true);
		
	}
	
	public Rook(int piecePosition, Team pieceTeam, boolean isFirst) {
		super(piecePosition, pieceTeam, PieceType.ROOK, isFirst);
	}

	/****************************************************************************
	 * Loops through all of the Rook's possible coordinates it can be moved to.
	 * If the move is legal it's added to a list of legal moves.
	 * @return legalMoves, a list of legal moves the Rook can perform
	 ****************************************************************************/
	@Override
	public Collection<Move> calcLegalMove(final Board board) {

		List<Move> legalMoves = new ArrayList<>();
		
		for(int i = 0; i < 4; i++){
			int possibleEndCoordinate = this.piecePosition;
			
			//Calculates all possible legal moves for the Rook
			while(Board.isValidSpaceCoordinate(possibleEndCoordinate)){
				
				//Checks to make sure that the move is legal from the First and Eight column
				if(firstColumn(possibleEndCoordinate, POSSIBLE_MOVE_COORDINATES[i]) || 
						eighthColumn(possibleEndCoordinate, POSSIBLE_MOVE_COORDINATES[i])){
					break;
				}
				
				
				possibleEndCoordinate += POSSIBLE_MOVE_COORDINATES[i];
				
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
						
						//Checks if the current Rook and the piece at the end space aren't part of the same team
						if(this.pieceTeam != pieceTeam) {
							//If they aren't the same it means the Rook can occupy the space.
							//And the move is added to the legal moves list.
							legalMoves.add(new NormalAttackMove(board, this, possibleEndCoordinate, pieceAtEnd));
						}
						//We know there is a Piece in front of the Bishop's path we then need to stop regardless of the Team
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
		return PieceType.ROOK.toString();
	}
	
	
	/****************************************************************
	 * Checks if the Rook's moves breaks down in the first column
	 ***************************************************************/
	private static boolean firstColumn(final int currentPosition, final int endPosition){
		return Board.FIRST_COLUMN.get(currentPosition) && (endPosition == -1);
	}
	
	/*****************************************************************
	 * Checks if the Rook's moves breaks down in the eighth column.
	 *****************************************************************/
	private static boolean eighthColumn(final int currentPosition, final int endPosition){
		return Board.EIGHTH_COLUMN.get(currentPosition) && (endPosition == 1);		
	}

	/**************************************************************
	 * Moves the Rook to a new space after it has been moved
	 * @param Move move
	 * @return A new Rook object at the position it was moved to.
	 **************************************************************/
	@Override
	public Piece movePiece(Move move) {
		//Creates a new Rook object
		return new Rook(move.getEndCoordinate(), move.getMovedPiece().getTeam());
	}
}
