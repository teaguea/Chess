package chess.engine.player;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;

import chess.engine.board.Board;
import chess.engine.board.Move;
import chess.engine.pieces.King;
import chess.engine.pieces.Piece;
import chess.engine.pieces.Team;

/*************************************************************
 * The Player class essentially determines if
 * the player is in check, check mate, or has
 * reached a stalemate.
 * @author Aaron
 * I was helped by the sites www.stackoverflow.com, www.stackexchange.com,
 * and chessprogramming.wikispaces.com
 **************************************************************/
public abstract class Player {
	
	//Represents the board the player is playing on
	protected final Board board;
	//Represents the player's king
	protected final King playerKing;
	//Represents the player's legal moves
	protected final Collection<Move> legalMoves;
	private final boolean isInCheck;
	
	
	/*********************************************************
	 * Creates the Player object
	 * @param board
	 * @param legalMoves
	 * @param opponetMoves
	 *********************************************************/
	Player(final Board board, final Collection<Move> legalMoves, final Collection<Move> opponetMoves){
		
		this.board = board;
		this.playerKing = establishKing();
		//Calculates the King Castle Moves and concatenates them with the player's legal moves and the opponet's moves
		this.legalMoves = ImmutableList.copyOf(Iterables.concat(legalMoves, calculateKingCastles(legalMoves, opponetMoves)));
		this.isInCheck = !Player.calculateAttacksOnTile(this.playerKing.getPiecePosition(), opponetMoves).isEmpty();
		
	}
	
	protected abstract Collection<Move> calculateKingCastles(Collection<Move> playerLegals, Collection<Move> opponentsLegals);
	
	/***********************************
	 * Returns the player's King piece
	 * @return King playerKing
	 ***********************************/
	public King getPlayerKing(){
		return this.playerKing;
	}
	
	public boolean isCastled() {
        return this.playerKing.isCastled();
    }

    public boolean isKingSideCastleCapable() {
        return this.playerKing.isKingSideCastleCapable();
    }

    public boolean isQueenSideCastleCapable() {
        return this.playerKing.isQueenSideCastleCapable();
}
	
	/******************************************
	 * Returns the player's legal moves
	 * @return Collection<Move> legalMoves
	 ******************************************/
	public Collection<Move> getLegalMoves(){
		return this.legalMoves;
	}

	/************************************************************************
	 * Calculates and then returns the moves that attack the piece position
	 * that is passed in.
	 * @param piecePostion
	 * @param moves
	 * @return List<Move> attackMoves
	 ************************************************************************/
	protected static Collection<Move> calculateAttacksOnTile(int piecePostion,
			Collection<Move> moves) {

		final List<Move> attackMoves = new ArrayList<>();
		for(final Move move: moves){
			if(piecePostion == move.getDestinationCoordinate())
				attackMoves.add(move);
		}
	    
	    return ImmutableList.copyOf(attackMoves);
	}

	/****************************************
	 * Makes certain that there is a King on
	 * the board.
	 * @return
	 ****************************************/
	private King establishKing() {
		for(final Piece piece : getActivePieces()){
			if(piece.getPieceType().isKing())
				return (King)piece;				
			
		}
		
		/**************************************************************************
		* Can't find King therefore we throw an exception because we have established
		* that the board is not valid board
		***************************************************************************/
		throw new RuntimeException("This is not a valid board!");
		
	}
	
	/*********************************************************
	 * Checks if the move is contained in the legal moves.
	 * @param move
	 * @return true or false
	 *********************************************************/
	public boolean isMoveLegal(final Move move){
		return this.legalMoves.contains(move);
	}
	
	/***************************************
	 * Return true if the player is in Check
	 * and false if the player is not in Check
	 * @return true or false
	 ***************************************/
	public boolean isInCheck(){
		return this.isInCheck;
	}
	
	/********************************************
	 * Determines if the player is in Check Mate
	 * @return true or false
	 ********************************************/
	public boolean isInCheckMate(){
		return this.isInCheck && !hasEscapeMoves();
	}
	
	/***********************************************
	 * Determines if the player is in a Stalemate
	 * @return true or false
	 ***********************************************/
	public boolean isInStaleMate(){
		return !this.isInCheck && !hasEscapeMoves();
	}
	
	
	/*************************************************************************************
	 * Determines if the player can escape by theoretically making the move on a pretend
	 * board. Should the move be successful we then know that an escape is possible.
	 * in check
	 * @return true or false
	 *************************************************************************************/
	protected boolean hasEscapeMoves(){
		for(final Move move: this.legalMoves){
			final MoveTransition transition = makeMove(move);
			
			if(transition.getMoveStatus().isDone())
				return true;
		}
			
		return false;
	}
	
	/********************************************************
	 * When we make a move we will return a move transition
	 * and this will relate to if the move was executed
	 * successfully
	 * @param move
	 * @return MoveTransition
	 *******************************************************/
	public MoveTransition makeMove(final Move move){
		
		/********************************************************
		* /If the move is illegal we send the original board to
		* create the MoveTransition object so the board will not
		* transition.
		**********************************************************/
		if (!isMoveLegal(move)) {
            return new MoveTransition(this.board, this.board, move, MoveStatus.ILLEGAL_MOVE);
        }
        final Board transitionedBoard = move.execute();
        final Collection<Move> kingAttacks = Player.calculateAttacksOnTile(
                transitionedBoard.currentPlayer().getOpponent().getPlayerKing().getPiecePosition(),
                transitionedBoard.currentPlayer().getLegalMoves());
        if (!kingAttacks.isEmpty()) {
            return new MoveTransition(this.board, this.board, move, MoveStatus.LEAVES_PLAYER_IN_CHECK);
        }
        return new MoveTransition(this.board, transitionedBoard, move, MoveStatus.DONE);
}
	
	/****************************************************
	 * Will retrieve all active Pieces
	 * @return
	 ****************************************************/
	public abstract Collection<Piece> getActivePieces();
	
	/***************************************************
	 * Will return what Team the player is playing on
	 * @return Team team
	 ***************************************************/
	public abstract Team getTeam();
	
	/************************************
	 * Returns the player's opponent
	 * @return Player opponent
	 ************************************/
	public abstract Player getOpponent();

}
