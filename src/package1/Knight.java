package package1;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
/*******************************************************************************
* The Knight Class represents Knight piece in the chess game. The fields included in
* class are the Knight's position, the team it is playing for, a special enum Type, and
* finally whether or not it's the Knight's first move. The class' two function are to
* calculate the Knight's legal moves and move Knight to it's new position if has been
* moved. 
* @author Aaron Teague
* I was helped by stackoverflow.com and chessprogramming.wikispaces.com/
*******************************************************************************/
public class Knight extends Piece {
	
	//All possible moves that a knight can perform.
	private final static int[] MOVE_COORDINATES = {17, 15, 10, 6, -6, -10, -15, -17};

	/*********************************************************************
	 * Creates a new knight object and automatically designates it moved
	 * it's first move.
	 * @param int piecePosition
	 * @param Team pieceTeam
	 ********************************************************************/
	Knight(int piecePosition, Team pieceTeam) {
		super(piecePosition, pieceTeam, PieceType.KNIGHT, true);
		
	}
	
	/**************************************
	 * Creates a new knight object.
	 * @param piecePosition
	 * @param pieceTeam
	 * @param isFirst
	 **************************************/
	Knight(int piecePosition, Team pieceTeam, boolean isFirst) {
		super(piecePosition, pieceTeam, PieceType.KNIGHT, isFirst);
		
	}

	/***********************************************************************
	 * Will loop through all of the moves that are possible.
	 * Test if each move is legal. If the move is legal the move
	 * is then added to a list in the method. When the loop is finished
	 * the list is then returned. 
	 * @param board
	 * @return legalMoves, a list of all possible moves the Knight can move.
	 ***********************************************************************/
	@Override
	public Collection<Move> calcLegalMove(Board board) {
		
		//The coordinate where the knight might move to
		int possibleEndCoordinate;
		
		//An array list that stores all moves that are possible for the knight
		List<Move> legalMoves = new ArrayList<>();
		
		//Beginning of loop 
		for(int i = 0; i < 8; i++){
			//Calculates the destination of the piece by adding to the piece's actual position to the move 
			possibleEndCoordinate = this.piecePosition + MOVE_COORDINATES[i];
			
			//Checks if the move is valid, if the will move off the board.
			if(Board.isValidSpaceCoordinate(possibleEndCoordinate)){
				
				//Checks if the move is legal from first, second, seventh, and eight columns
				if(firstColumn(this.piecePosition, MOVE_COORDINATES[i]) || 
				   secondColumn(this.piecePosition, MOVE_COORDINATES[i]) ||
				   seventhColumn(this.piecePosition, MOVE_COORDINATES[i]) ||
				   eighthColumn(this.piecePosition, MOVE_COORDINATES[i])){
					continue;
				}
				
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
					
					//Checks if the current knight and the piece at the end space aren't part of the same Team
					if(this.pieceTeam != pieceTeam) {
						//If they aren't the same it means the knight can occupy the space.
						//And the move is added to the legal moves list.
						legalMoves.add(new NormalAttackMove(board, this, possibleEndCoordinate, pieceAtEnd));
					}
					
				}
			}
		}
		
		//returns the calculated list of legal moves.
		return legalMoves;
	}

	@Override
	public String toString() {
		return PieceType.KNIGHT.toString();
	}
	
	/****************************************************************
	 * Checks if the knight's moves breaks down in the first column
	 ***************************************************************/
	private static boolean firstColumn(final int currentPosition, final int endPosition){
		return Board.FIRST_COLUMN.get(currentPosition) && ((endPosition == -17) || (endPosition == -10) || 
				(endPosition == 6) || (endPosition == 15));
	}
	
	/*****************************************************************
	 * Checks if the knight's moves breaks down in the second column.
	 *****************************************************************/
	private static boolean secondColumn(final int currentPosition, final int endPosition){
		return Board.SECOND_COLUMN.get(currentPosition) && ((endPosition == -10) || (endPosition == 6));		
	}
	
	/*****************************************************************
	 * Checks if the knight's moves breaks down in the seventh column.
	 *****************************************************************/
	private static boolean seventhColumn(final int currentPosition, final int endPosition){
		return Board.SEVENTH_COLUMN.get(currentPosition) && ((endPosition == -6) || (endPosition == 10));		
	}
	
	/*****************************************************************
	 * Checks if the knight's moves breaks down in the eighth column.
	 *****************************************************************/
	private static boolean eighthColumn(final int currentPosition, final int endPosition){
		return Board.EIGHTH_COLUMN.get(currentPosition) && ((endPosition == -15) || (endPosition == -6) || 
				(endPosition == 10) || (endPosition == 17));		
	}

	/**************************************************************
	 * Moves the Knight to a new space after it has been moved
	 * @param Move move
	 * @return A new Knight object at the position it was moved to.
	 **************************************************************/
	@Override
	public Piece movePiece(Move move) {
		//Creates a new Knight object
		return new Knight(move.getEndCoordinate(), move.getMovedPiece().getTeam());
	}

}
