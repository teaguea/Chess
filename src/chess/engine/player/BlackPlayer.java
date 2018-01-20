package chess.engine.player;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.google.common.collect.ImmutableList;

import chess.engine.board.Board;
import chess.engine.board.Move;
import chess.engine.board.Tile;
import chess.engine.pieces.Piece;
import chess.engine.pieces.Rook;
import chess.engine.pieces.Team;

/******************************************************************************
 * The Main purpose of this class is to calculate all of the Castling
 * moves on the Black Player side.
 * @author Aaron Teague
 * I was helped by the sites www.stackoverflow.com, www.stackexchange.com,
 * and chessprogramming.wikispaces.com
 ******************************************************************************/
public class BlackPlayer extends Player{

	/******************************************
	 * Creates the BlackPlayer object
	 * @param board
	 * @param whiteStandardLegalMoves
	 * @param blackStandardLegalMoves
	 ******************************************/
	public BlackPlayer(final Board board,
					   final Collection<Move> whiteStandardLegalMoves,
					   final Collection<Move> blackStandardLegalMoves) {
		
		super(board, blackStandardLegalMoves, whiteStandardLegalMoves);
		
	}
	
	@Override
	public String toString(){
		return "Black";
	}

	/*******************************************
	 * Returns all of the active pieces on the
	 * black side.
	 *******************************************/
	@Override
	public Collection<Piece> getActivePieces() {
		
		return this.board.getBlackPieces();
	}

	/********************************
	 * Returns Black enum value
	 * @return Team.BLACK
	 ********************************/
	@Override
	public Team getTeam() {
		return Team.BLACK;
	}

	@Override
	public Player getOpponent() {
		return this.board.getWhitePlayer();
	}
	/**************************************************
	 * Calculates the King Castling Moves and returns
	 * the moves as a list
	 * @return List<Move> kingCastles
	 * @param Collection<Move> playerLegals
	 * @param Collection<Move> opponentsLegals
	 **************************************************/
	@Override
	protected Collection<Move> calculateKingCastles(final Collection<Move> playerLegals, final Collection<Move> opponentsLegals) {
		
		final List<Move> kingCastles = new ArrayList<>();
		
		if(this.playerKing.isFirstMove() && !this.isInCheck()){
			if(!this.board.getTile(5).isTileOccupied() && !this.board.getTile(6).isTileOccupied()){
				final Tile rookTile = this.board.getTile(7);
				
				if(rookTile.isTileOccupied() && rookTile.getPiece().isFirstMove()){
					
					if(Player.calculateAttacksOnTile(5, opponentsLegals).isEmpty() &&
					   Player.calculateAttacksOnTile(6, opponentsLegals).isEmpty() &&
					   rookTile.getPiece().getPieceType().isRook()){
						kingCastles.add(new Move.KingSideCastleMove(this.board,
															   this.playerKing,
															   6,
															   (Rook)rookTile.getPiece(),
															   rookTile.getTileCoordinate(),
															   5));
					}
				}
			}
			
			if(!this.board.getTile(1).isTileOccupied() &&
			   !this.board.getTile(2).isTileOccupied() &&
			   !this.board.getTile(3).isTileOccupied()){
				
				final Tile rookTile = this.board.getTile(0);
				
				if(rookTile.isTileOccupied() && rookTile.getPiece().isFirstMove() &&
					Player.calculateAttacksOnTile(2, opponentsLegals).isEmpty() &&
					Player.calculateAttacksOnTile(3, opponentsLegals).isEmpty() &&
					rookTile.getPiece().getPieceType().isRook())
					kingCastles.add(new Move.QueenSideCastleMove(this.board,
							   									 this.playerKing,
							   									 2,
							   									 (Rook)rookTile.getPiece(),
							   									 rookTile.getTileCoordinate(),
							   									 3));
				
				
			}
			
		}
		
		return ImmutableList.copyOf(kingCastles);

	}

}
