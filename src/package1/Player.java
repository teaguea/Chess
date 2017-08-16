/************************************************************************************************
 * The Player Class sets up basic the operations and information a chess needs.
 * Each Player object will have boolean value inCheck to indicate if the player's
 * King is in check. The board that the player is currently playing on. The player's
 * king piece and finally a list of moves. The Player class can calculate potential moves on
 * his King, determines if his King is on the board, determines if a move is legal, check if
 * there is stalemate, check if his King is in check or in check mate. 
 * piece object contains it's position, what team it's playing for, is it on 
 * first move, and what type it is.
 * @author Aaron Teague
 *I was helped by StackOverflow and chessprogramming.wikispaces.com/
 *************************************************************************************************/

package package1;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;

public abstract class Player {
	
	private boolean inCheck;
	protected Board board;
	protected King king;
	protected Collection<Move> legalMoves;
	
	Player(Board board, Collection<Move> legalMoves, Collection<Move> oppoentMoves){
		
		
		
		this.board = board;
		this.king = createKing();
		this.legalMoves = ImmutableList.copyOf(Iterables.concat(legalMoves, calcKingCastle(legalMoves, oppoentMoves)));
		// Determines if any potential attacks are possible on the King.
		this.inCheck = !Player.determineAttacksOnSpace(this.king.getPiecePosition(), oppoentMoves).isEmpty();
	}

	/***************************************************************
	 * Calculates potential moves on the given space.
	 * @param piecePosition, the coordinate the piece is on
	 * @param opponetMoves, the opponet's moves
	 * @return attackMoves, list of attack moves on the the space
	 ****************************************************************/
	protected static Collection<Move> determineAttacksOnSpace(int piecePosition, Collection<Move> opponetMoves) {
		
		List<Move> attackMoves = new ArrayList<>();
		//Loops through all of the opponent's moves
		for(Move move : opponetMoves){
			//Checks if any of the moves have an end coordinate that is the same as the king
			if(piecePosition == move.getEndCoordinate()){
				//If indeed the end position of the move is on the King then the move gets added
				attackMoves.add(move);
				
			}
		}
		//If attack moves is not empty we know the king is in check
		return attackMoves;
	}

	/************************************
	 * Finds the King
	 * @return the player's king or an 
	 * error if the King isn't found
	 ************************************/
	private King createKing() {
		for(Piece piece : getAlivePieces()){
			if(piece.getPieceType().isKing()){
				return (King) piece;
			}
		}
		
		throw new RuntimeException("Not Valid!");
	}
	
	public abstract Player getOpponent();
	public abstract Collection<Piece> getAlivePieces();
	public abstract Team getTeam();
	protected abstract Collection<Move> calcKingCastle(Collection<Move> playerLegal, Collection<Move> opponetLegals);
	
	/******************************************
	 * Checks if the move is a legal move.
	 * @param move
	 * @return this.legalMoves.contains(move);
	 ******************************************/
	public boolean isMoveLegal(Move move){
		return this.legalMoves.contains(move);
		
	}
	
	/*************************
	 * Get the player's king
	 * @return the player's king
	 *************************/
	public King getKing(){
		
		return this.king;
	}
	
	/*********************************
	 * Gets the player's legal moves
	 * @return the player's legal moves
	 *********************************/
	public Collection<Move> getLegalMoves() {
		
		return this.legalMoves;
	}
	
	/********************************************
	 * Checks if the player is in a stalemate
	 * @return
	 ********************************************/
	public boolean Stalemate(){
		return !this.inCheck && !canEscape();
	}
	
	/********************************************
	 * Checks if the player has been check mated
	 * @return
	 ********************************************/
	public boolean CheckMate(){
		return this.inCheck && !canEscape();
	}
	
	/***************************************************
	 * Helper function that determines if the King can
	 * escape when checked
	 * @return true if an escape is possible, false when
	 * the King can't escape
	 ***************************************************/
	protected boolean canEscape() {
		
		for(Move move : this.legalMoves){
			//At this point we create and make theoretical moves for the King
			MoveChange change = makeMove(move);
			/****************************************************************
			At this point we check if the move could be finished.
			If this is possible then we know the King can escape the check.
			*****************************************************************/
			if(change.getMoveState().isFinished()){
				return true;
			}
		}
		return false;
	}

	/******************************************
	 * Checks if the player is in check
	 * @return check status
	 ******************************************/
	public boolean Check(){
		return this.inCheck;
	}
	
	/******************************************
	 * Checks if the player is in check
	 * @return false
	 ******************************************/
	public boolean Castled(){
		return false;
	}
	
	
	/*****************************************************************************************************
	 * Determines if the move is an illegal move, puts the player own king in check, or is legal.
	 * If the move can't be executed the board isn't changed. If the move was able to be successfully
	 * executed the board is changed and is ready for the next player's move. I was assisted in writing this
	 * method by the great people at stackoverflow.com
	 * @param move
	 * @return Returns the board after the move has been calculated
	 ******************************************************************************************************/
	public MoveChange makeMove(Move move){
		
		//Checks if the move is part of the collection of legal moves
		if(!isMoveLegal(move)){
			//if the move is illegal the same board is returned
			return new MoveChange(move, MoveState.ILLEGAL, this.board); 
		}
		
		//Execute the move and creates and changes to a new board
		Board changeBoard = move.execute();
		
		//Searches for any attacks on the king
		Collection<Move> kingAttacks = Player.determineAttacksOnSpace(changeBoard.getCurrentPlayer().getOpponent().getKing().getPiecePosition(),
				changeBoard.getCurrentPlayer().getLegalMoves());
		
		//If kingsAttack is not empty then we know the player has exposed his own King in check
		if(!kingAttacks.isEmpty()){
			//We then return the current board with the move state of player in check
			return new MoveChange(move, MoveState.PLAYER_CHECK, this.board);
		}
		
		//returns a board that has been changed due to the move
		return new MoveChange(move, MoveState.FINISHED, changeBoard);
	}

	

}
