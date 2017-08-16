/*************************************************************************************************
 * The BlackPlayer class inherits from the player class. It's main purpose is to calculate the
 * castle moves for the black player.
 * @author Aaron Teague
 *I was helped by StackOverflow and chessprogramming.wikispaces.com/
 *************************************************************************************************/
package package1;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class BlackPlayer extends Player{

	public BlackPlayer(Board board, Collection<Move> whiteLegalMoves,
			Collection<Move> blackLegalMoves) {
		
			/**********************************************************************
			 In the case of the black player the legal moves are the blackLegalMoves
			 and the opponents moves are the whiteLegalMoves
			***********************************************************************/
			super(board, blackLegalMoves, whiteLegalMoves);
	}

	/***************************************
	 * Gets the black pieces from the board
	 * @return blackPieces
	 ***************************************/
	@Override
	public Collection<Piece> getAlivePieces() {
		
		return this.board.getBlackPieces();
	}

	/*****************************
	 * Get the team of the player
	 * @return Team.BLACK
	 *****************************/
	@Override
	public Team getTeam() {
		
		return Team.BLACK;
	}

	@Override
	public Player getOpponent() {
		
		return this.board.getWhitePlayer();
	}

	@Override
	protected Collection<Move> calcKingCastle(Collection<Move> playerLegal,
			Collection<Move> opponetLegals) {
		List<Move> kingCastles = new ArrayList<>();
		
		//King side castle, To do a castle move it must the King's first move and can't be in check
		if(this.king.getFirst() && !this.Check()){
			//Black's king side castle, checks if squares 5 and 6 aren't being used
			if(!this.board.getSpace(5).isSpaceUsed() && !this.board.getSpace(6).isSpaceUsed()){
				Space rookSpace = this.board.getSpace(7);
				
				//Checks if it also the rook's first move
				if(rookSpace.isSpaceUsed() && rookSpace.getPiece().getFirst()){
					
					if(Player.determineAttacksOnSpace(5, opponetLegals).isEmpty() &&
					   Player.determineAttacksOnSpace(6, opponetLegals).isEmpty() &&
					   rookSpace.getPiece().getPieceType().isRook()){
						
						kingCastles.add(new KingCastle(this.board, this.king, 6, (Rook)rookSpace.getPiece(), rookSpace.getSpaceCoordinate(), 5));
						
					}
				}
			}
			
			//Checks conditions for a Queen's side castle
			if(!this.board.getSpace(1).isSpaceUsed() &&
					!this.board.getSpace(2).isSpaceUsed() && 
					!this.board.getSpace(3).isSpaceUsed()){
			
				Space rookSpace = this.board.getSpace(0);
				if(rookSpace.isSpaceUsed() && rookSpace.getPiece().getFirst() && Player.determineAttacksOnSpace(2, opponetLegals).isEmpty() &&
					Player.determineAttacksOnSpace(2, opponetLegals).isEmpty() && rookSpace.getPiece().getPieceType().isRook()){
					kingCastles.add(new KingCastle(this.board, this.king, 2, (Rook)rookSpace.getPiece(), rookSpace.getSpaceCoordinate(), 3));
				}
			}
		 
		}
		return kingCastles;
	}

}
