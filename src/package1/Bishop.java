package package1;


import java.util.List;
import java.util.ArrayList;
import java.util.Collection;

public class Bishop extends Piece {

	//All possible moves that a Bishop can perform.
	private final static int[] POSSIBLE_MOVE_COORDINATES = {9, 7, -7, -9};
	
	/************************************
	 * Creates a new Bishop object.
	 * @param piecePosition
	 * @param pieceTeam
	 ************************************/
	Bishop(int piecePosition, Team pieceTeam) {
		super(piecePosition, pieceTeam, PieceType.BISHOP, true);
	}
	
	Bishop(int piecePosition, Team pieceTeam,  boolean isFirst) {
		super(piecePosition, pieceTeam, PieceType.BISHOP, isFirst);
	}

	@Override
	public Collection<Move> calcLegalMove(Board board) {

		List<Move> legalMoves = new ArrayList<>();
		
		for(int i = 0; i < 4; i++){
			int possibleEndCoordinate = this.piecePosition;
			
			//Calculates all possible legal moves for the Bishop
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
						Piece pieceAtEnd = possibleEndSpace.getPiece();
						//Saves the team of the piece occupying the end space to pieceTeam
						Team pieceTeam = pieceAtEnd.getTeam();
						
						//Checks if the current Bishop and the piece at the end space aren't part of the same Team
						if(this.pieceTeam != pieceTeam) {
							/*****************************************************************
							If they aren't the same it means the Bishop can occupy the space.
							And the move is added to the legal moves list.
							******************************************************************/
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
		return PieceType.BISHOP.toString();
	}
	
	/****************************************************************
	 * Checks if the Bishop's moves breaks down in the first column
	 ***************************************************************/
	private static boolean firstColumn(final int currentPosition, final int endPosition){
		return Board.FIRST_COLUMN.get(currentPosition) && (endPosition == 7 || endPosition == -9);
	}
	
	/*****************************************************************
	 * Checks if the Bishop's moves breaks down in the eighth column.
	 *****************************************************************/
	private static boolean eighthColumn(final int currentPosition, final int endPosition){
		return Board.EIGHTH_COLUMN.get(currentPosition) && (endPosition == -7 || endPosition == 9);		
	}

	/**************************************************************
	 * Moves the Bishop to a new space after it has been moved
	 * @param Move move
	 * @return A new Bishop object at the position it was moved to.
	 **************************************************************/
	@Override
	public Piece movePiece(Move move) {
		//Creates a new Bishop object
		return new Bishop(move.getEndCoordinate(), move.getMovedPiece().getTeam());
	}
}
