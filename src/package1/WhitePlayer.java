/*************************************************************************************************
 * The WhitePlayer class inherits from the player class. It's main purpose is to calculate the
 * castle moves for the white player.
 * @author Aaron Teague
 *I was helped by StackOverflow and chessprogramming.wikispaces.com/
 *************************************************************************************************/
package package1;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class WhitePlayer extends Player{

	public WhitePlayer(Board board, Collection<Move> whiteLegalMoves,
			Collection<Move> blackLegalMoves) {
		
		/**********************************************************************
		In the case of the white player the legal moves are the whiteLegalMoves
		and the opponents moves are the blackLegalMoves
		***********************************************************************/
		super(board, whiteLegalMoves, blackLegalMoves);
	}

	/**************************************
	 *Gets the white pieces from the board 
	 *@return whitePieces
	 **************************************/
	@Override
	public Collection<Piece> getAlivePieces() {
		
		return this.board.getWhitePieces();
	}

	/*****************************
	 * Get the team of the player
	 * @return Team.WHITE
	 *****************************/
	@Override
	public Team getTeam() {
		
		return Team.WHITE;
	}

	@Override
	public Player getOpponent() {
		
		return this.board.getBlackPlayer();
	}

	/*****************************************
	 * Calculates all possible Castle Moves
	 *@param Collection<Move> playerLegal
	 *@param Collection<Move> opponetLegals
	 *****************************************/
	@Override
	protected Collection<Move> calcKingCastle(Collection<Move> playerLegal,
			Collection<Move> opponetLegals) {
		
		List<Move> kingCastles = new ArrayList<>();
		
		//King side castle, To do a castle move it must the King's first move and can't be in check
		if(this.king.getFirst() && !this.Check()){
			//Checks if squares 61 and 62 aren't being used
			if(!this.board.getSpace(61).isSpaceUsed() && !this.board.getSpace(62).isSpaceUsed()){
				Space rookSpace = this.board.getSpace(63);
				
				//Checks if it also the rook's first move
				if(rookSpace.isSpaceUsed() && rookSpace.getPiece().getFirst()){
					
					if(Player.determineAttacksOnSpace(61, opponetLegals).isEmpty() &&
					   Player.determineAttacksOnSpace(62, opponetLegals).isEmpty() &&
					   rookSpace.getPiece().getPieceType().isRook()){
						
						kingCastles.add(new KingCastle(this.board, this.king, 62, (Rook)rookSpace.getPiece(), 
								        rookSpace.getSpaceCoordinate(), 61));
						
					}
				}
			}
			
			//Checks if conditions are correct for a Queen side castle
			if(!this.board.getSpace(59).isSpaceUsed() &&
					!this.board.getSpace(58).isSpaceUsed() && 
					!this.board.getSpace(57).isSpaceUsed()){
			
				Space rookSpace = this.board.getSpace(56);
				if(rookSpace.isSpaceUsed() && rookSpace.getPiece().getFirst() && Player.determineAttacksOnSpace(58, opponetLegals).isEmpty() &&
						Player.determineAttacksOnSpace(59, opponetLegals).isEmpty() && rookSpace.getPiece().getPieceType().isRook()){
					kingCastles.add(new QueenCastle(this.board, this.king, 58, (Rook)rookSpace.getPiece(), 
							        rookSpace.getSpaceCoordinate(), 59));
				}
			}
		 
		}
		return kingCastles;
	}

}
