package chess.engine.board;

import chess.engine.board.Board.Builder;
import chess.engine.pieces.Pawn;
import chess.engine.pieces.Piece;
import chess.engine.pieces.Rook;

/**************************************************************************************
 * Defines the abstract class Move. A basic move is represented by the board that the
 * game is played on, the piece that was actually moved and the coordinate the piece
 * was moved to.
 * @author Aaron Teague
 * I was helped by the sites www.stackoverflow.com, www.stackexchange.com,
 * and chessprogramming.wikispaces.com
 *************************************************************************************/
public abstract class Move {
	
	//Represents the board the game is played on
	protected final Board board;
	//Represents the piece that was moved
	protected final Piece movedPiece;
	//Represents where the piece was moved
	protected final int destinationCoordinate;
	protected final boolean isFirstMove;
	
	public static final Move NULL_MOVE = new NullMove();
	
	/**********************************************************************
	 * The Move Constructor
	 * @param board
	 * @param movedPiece
	 * @param destinationCoordinate
	 ***********************************************************************/
	private Move(final Board board, final Piece movedPiece, final int destinationCoordinate){
		this.board = board;
		this.movedPiece = movedPiece;
		this.destinationCoordinate = destinationCoordinate;
		this.isFirstMove = movedPiece.isFirstMove();
	}
	
	private Move(final Board board, final int destinationCoordinate){
		this.board = board;
		this.destinationCoordinate = destinationCoordinate;
		this.movedPiece = null;
		this.isFirstMove = false;
	}
	
	
	@Override
	public int hashCode(){
		final int prime = 31;
		int result = 1;
		
		result = prime * result + this.destinationCoordinate;
		result = prime * result + this.movedPiece.hashCode();
		result = prime * result + this.movedPiece.getPiecePosition();
		return result;
		
	}
	
	@Override
	public boolean equals(final Object other){
		if(this == other)
			return true;
		
		if(!(other instanceof Move))
			return false;
		
		final Move otherMove = (Move) other;
		return getDestinationCoordinate()  == otherMove.getDestinationCoordinate() &&
			   getMovedPiece().equals(otherMove.getMovedPiece()) && getCurrentCoordinate() == otherMove.getCurrentCoordinate();
			   
	}
	
	/*********************************************
	 * Returns if the move is an attack, returns
	 * false on the parent class
	 * @return false
	 *********************************************/
	public boolean isAttack(){
		return false;
	}
	
	
	/***************************************************
	 * Returns if the move is a castling move, returns
	 * false on the parent class
	 * @return false
	 ***************************************************/
	public boolean isCastlingMove(){
		return false;
	}
	
	/*************************************************
	 * Returns the board the move is being played on
	 * @return Board board
	 *************************************************/
	public Board getBoard(){
		return board;
	}
	
	/*****************************************************
	 * Returns the piece that was attacked, returns null
	 * on the parent class
	 * @return null
	 ******************************************************/
	public Piece getAttackedPiece(){
		return null;
	}
	
	/*************************************************
	 * Returns the position of the piece
	 * @return int the coordinate of the piece moved
	 *************************************************/
	public int getCurrentCoordinate(){
		return this.movedPiece.getPiecePosition();
	}
	
	/****************************************
	 * Returns the piece that is being moved
	 * @return Piece movedPiece
	 ****************************************/
	public Piece getMovedPiece(){
		return this.movedPiece;
	}
	
	/**************************************************
	 * Returns this destination coordinate of the move.
	 * @return int destinationCoordinate
	 **************************************************/
	public int getDestinationCoordinate() {
		return this.destinationCoordinate;
	}
	
	/***********************************************************
	 * Will create new board to reflect the successful executed
	 * move.
	 * @return Board
	 *********************************************************/
	public Board execute() {
		
		//builder object will assist in creating new board
		final Builder builder = new Builder();
		
		//Loop through the incoming board's current player's pieces. All piece that aren't the moved
		//piece are simply set on to the new.
		for(final Piece piece : this.board.currentPlayer().getActivePieces())
			if(!this.movedPiece.equals(piece))
				builder.setPiece(piece);
		
		//The opponent's pieces are simply set with no check
		for(final Piece piece : this.board.currentPlayer().getOpponent().getActivePieces())
			builder.setPiece(piece);
			
		//The moved piece is set to it's new position
		builder.setPiece(this.movedPiece.movePiece(this));
		///The move maker will be set to the current's player opponent for the next turn
		builder.setMoveMaker(this.board.currentPlayer().getOpponent().getTeam());
		
		return builder.build();
	}
	
//<----------------------------------------------------------------------------------------------------------------------------------------------------------->	
	/***************************************************
	 * Defines a NonAttack move by a Major Piece
	 * @author Aaron Teague
	 * I was helped by the sites www.stackoverflow.com, www.stackexchange.com,
	 * and chessprogramming.wikispaces.com
	 ***************************************************/
	public static final class MajorMove extends Move {

		public MajorMove(final Board board, final Piece movedPiece, final int destinationCoordinate) {
			super(board, movedPiece, destinationCoordinate);
			
		}
		
		@Override
		public boolean equals(final Object other){
			return this == other || other instanceof MajorMove && super.equals(other);
		}
		
		@Override
		public String toString(){
			return movedPiece.getPieceType().toString() + BoardUtils.getPositionAtCoordinate(this.destinationCoordinate);
		}
		
	}
//<----------------------------------------------------------------------------------------------------------------------------------------------------------->

//<----------------------------------------------------------------------------------------------------------------------------------------------------------->
	public static class MajorAttackMove extends AttackMove {
		
		public MajorAttackMove(final Board board,
							   final Piece piece,
							   final int destinationCoordinate,
							   final Piece pieceAttacked){
			
			super(board, piece, destinationCoordinate, pieceAttacked);
			
		}
		
		@Override
		public boolean equals(final Object other){
			return this == other || other instanceof MajorAttackMove && super.equals(other);
		}
		
		@Override
		public String toString(){
			return movedPiece.getPieceType() + BoardUtils.getPositionAtCoordinate(this.destinationCoordinate);
		}
	}
//<----------------------------------------------------------------------------------------------------------------------------------------------------------->	

//<----------------------------------------------------------------------------------------------------------------------------------------------------------->	
	/**********************************************************************************
	 * Defines the attack move, the execute will create a new board object to apply
	 * the move.
	 * @author Aaron Teague
	 * I was helped by the sites www.stackoverflow.com, www.stackexchange.com,
	 * and chessprogramming.wikispaces.com
	 **********************************************************************************/
	public static class AttackMove extends Move {

		//Represents the Piece that the move is attacking.
		final Piece attackedPiece;
		
		/***********************************
		 * Creates a new attack move
		 * @param board
		 * @param movedPiece
		 * @param destinationCoordinate
		 * @param attackedPiece
		 ************************************/
		public AttackMove(final Board board, final Piece movedPiece, final int destinationCoordinate, final Piece attackedPiece) {
			super(board, movedPiece, destinationCoordinate);
			this.attackedPiece = attackedPiece;
		}
		
		@Override
		public int hashCode(){
			return this.attackedPiece.hashCode() + super.hashCode();
		}
		
		@Override
		public boolean equals(final Object other){
			if(this == other)
				return true;
			
			if(!(other instanceof AttackMove))
				return false;
			
			final AttackMove otherAttackMove = (AttackMove) other;
			return super.equals(otherAttackMove) && getAttackedPiece().equals(otherAttackMove.getAttackedPiece());
			
		}
		
		/******************************
		 * Returns for an attack move
		 * @return true
		 ******************************/
		@Override
		public boolean isAttack(){
			return true;
		}
		
		/********************************************
		 * Returns the piece that is being attacked
		 * @return this.getAttackedPiece()
		 ********************************************/
		@Override
		public Piece getAttackedPiece(){
			return this.attackedPiece;
		}
		
	}
//<----------------------------------------------------------------------------------------------------------------------------------------------------------->

//<----------------------------------------------------------------------------------------------------------------------------------------------------------->
	   public static class PawnPromotion extends Move{
		   
		   final Move decoratedMove;
		   final Pawn promotedPawn;
		   
		   public PawnPromotion(final Move decoratedMove){
			   super(decoratedMove.getBoard(), decoratedMove.getMovedPiece(), decoratedMove.getDestinationCoordinate());
			   this.decoratedMove = decoratedMove;
			   this.promotedPawn = (Pawn) decoratedMove.getMovedPiece();
			   
		   }
		   
		   
		   @Override
		   public Board execute(){
			   final Board pawnMovedBoard = this.decoratedMove.execute();
			   final Board.Builder builder = new Builder();
			   
			   for(final Piece piece : pawnMovedBoard.currentPlayer().getActivePieces()){
				   if(!this.promotedPawn.equals(piece)){
					   builder.setPiece(piece);
				   }
			   }
			   
			   for(final Piece piece : pawnMovedBoard.currentPlayer().getOpponent().getActivePieces()){
				   builder.setPiece(piece);
			   }
			   
			   builder.setPiece(this.promotedPawn.getPromotionPiece().movePiece(this));
			   builder.setMoveMaker(pawnMovedBoard.currentPlayer().getTeam());
			   return builder.build();
		   }
		   
		   @Override
		   public boolean isAttack(){
			   return this.decoratedMove.isAttack();
		   }
		   
		   @Override
		   public Piece getAttackedPiece() {
			   return this.decoratedMove.getAttackedPiece();
		   }
		   
		   @Override
		   public String toString(){
			   return "";
		   }
		   
		   @Override
		   public int hashCode(){
			   return decoratedMove.hashCode() + (31 * promotedPawn.hashCode());
		   }
		   
		   @Override
		   public boolean equals(final Object other){
			   return this == other || other instanceof PawnPromotion && (super.equals(other));
		   }
		   
		   
	   }
//<----------------------------------------------------------------------------------------------------------------------------------------------------------->
	

//<----------------------------------------------------------------------------------------------------------------------------------------------------------->	
		/**************************************************************************
		 * Defines a Pawn Move
		 * @author Aaron Teague
		 * I was helped by the sites www.stackoverflow.com, www.stackexchange.com,
		 * and chessprogramming.wikispaces.com
		 **************************************************************************/
		public static final class PawnMove extends Move {

			public PawnMove(final Board board, final Piece movedPiece, final int destinationCoordinate) {
				super(board, movedPiece, destinationCoordinate);
				
			}
			
			@Override
			public boolean equals(final Object other){
				return this == other || other instanceof PawnMove && super.equals(other);
			}
			
			
			
			@Override
			public String toString(){
				return BoardUtils.getPositionAtCoordinate(this.destinationCoordinate);
			}
			
		}
//<----------------------------------------------------------------------------------------------------------------------------------------------------------->
		
		
//<----------------------------------------------------------------------------------------------------------------------------------------------------------->	
		/**************************************************************************
		 * Defines a Pawn Attack Move
		 * @author Aaron Teague
		 * I was helped by the sites www.stackoverflow.com, www.stackexchange.com,
		 * and chessprogramming.wikispaces.com
		 ***************************************************************************/
		 public static class PawnAttackMove extends AttackMove {

			 /******************************************************
			  * Creates the PawnAttackMove object
			  * @param board
			  * @param movedPiece
			  * @param destinationCoordinate
			  * @param attackedPiece
			  *******************************************************/
		    public PawnAttackMove(final Board board, final Piece movedPiece, final int destinationCoordinate, final Piece attackedPiece) {
			  super(board, movedPiece, destinationCoordinate, attackedPiece);
						
			}
		    
		    @Override
		    public boolean equals(final Object other){
		    	return this == other || other instanceof PawnAttackMove && super.equals(other);
		    }
		    
		    @Override
		    public String toString(){
		    	return BoardUtils.getPositionAtCoordinate(this.movedPiece.getPiecePosition()).substring(0, 1) + "x" + 
		    											  BoardUtils.getPositionAtCoordinate(this.destinationCoordinate);
		    }
					
		 }
//<------------------------------------------------------------------------------------------------------------------------------------>

		 
//<------------------------------------------------------------------------------------------------------------------------------------>	
	     /**************************************************************************
	      * Defines a En Passant Attack Move
		  * @author Aaron Teague
		  * I was helped by the sites www.stackoverflow.com, www.stackexchange.com,
		  * and chessprogramming.wikispaces.com
		  **************************************************************************/
		  public static class PawnEnPassantAttackMove extends PawnAttackMove {

			/***************************************************************
			 * Creates the PawnEnPassantAttackMove object
			 * @param board
			 * @param movedPiece
			 * @param destinationCoordinate
			 * @param attackedPiece
			 ****************************************************************/
			public PawnEnPassantAttackMove(final Board board, final Piece movedPiece, final int destinationCoordinate, final Piece attackedPiece) {
			   super(board, movedPiece, destinationCoordinate, attackedPiece);
							
			}
			
			@Override
			public boolean equals(final Object other){
				return this == other || other instanceof PawnEnPassantAttackMove && super.equals(other); 
			}
			
			/**************************************************************
			 * Executes the En Passant and returns the resulting board
			 * @return Board board
			 **************************************************************/
			@Override
			public Board execute(){
				final Builder builder = new Builder();
				for(final Piece piece : this.board.currentPlayer().getActivePieces()){
					if(!this.movedPiece.equals(piece))
						builder.setPiece(piece);
				}
				
				for(final Piece piece : this.board.currentPlayer().getOpponent().getActivePieces()){
					if(!piece.equals(this.getAttackedPiece()))
						builder.setPiece(piece);
				}
				
				builder.setPiece(this.movedPiece.movePiece(this));
				builder.setMoveMaker(this.board.currentPlayer().getOpponent().getTeam());
				return builder.build();
				
			}
						
		  }
//<----------------------------------------------------------------------------------------------------------------------------------------------------------->

//<----------------------------------------------------------------------------------------------------------------------------------------------------------->	
		/***************************************************************************
		 * Defines a Pawn Jump
		 * @author Aaron Teague
		 * I was helped by the sites www.stackoverflow.com, www.stackexchange.com,
		 * and chessprogramming.wikispaces.com
		 ***************************************************************************/
		 public static final class PawnJump extends Move {

			/*************************************************************
			 * Creates the PawnJump Object
			 * @param board
			 * @param movedPiece
			 * @param destinationCoordinate
			 **************************************************************/
			public PawnJump(final Board board, final Piece movedPiece, final int destinationCoordinate) {
				super(board, movedPiece, destinationCoordinate);
					
			}
			
			/**************************************************************
			 * Exectues move by creating new board that reflects the move.
			 * @return Board
			 **************************************************************/
			@Override
			public Board execute(){
				//Builder object to create board
				final Builder builder = new Builder();
				
				
				/***************************************************************
				 * Loops through all the current player's piece and sets them
				 * on the board. But will not do this for the Pawn that was
				 * moved
				 ***************************************************************/
				for(final Piece piece : this.board.currentPlayer().getActivePieces())
					if(!this.movedPiece.equals(piece))
						builder.setPiece(piece);
				
				//Loops through all the opponent's pieces and sets them
				for(final Piece piece : this.board.currentPlayer().getOpponent().getActivePieces())
					builder.setPiece(piece);
				
				
				final Pawn movedPawn  = (Pawn)this.movedPiece.movePiece(this);
				//Set the moved pawn on the board
				builder.setPiece(movedPawn);
				//Set the candidate EnPassant Pawn as the moved pawn
				builder.setEnPassantPawn(movedPawn);
				//Set the next turn to the opponent
				builder.setMoveMaker(this.board.currentPlayer().getOpponent().getTeam());
				//Return the board with the executed pawn jump
				return builder.build();
			}
			
			@Override
			public String toString(){
				return BoardUtils.getPositionAtCoordinate(this.destinationCoordinate);
			}
				
		 }
//<----------------------------------------------------------------------------------------------------------------------------------------------------------->
		 
//<----------------------------------------------------------------------------------------------------------------------------------------------------------->	
		/*****************************************************************************
		 * Defines a Castling Move
		 * @author Aaron Teague
		 * I was helped by the sites www.stackoverflow.com, www.stackexchange.com,
		 * and chessprogramming.wikispaces.com
		 ****************************************************************************/
		 static abstract class CastleMove extends Move {
			 
			protected final Rook castleRook;
			protected final int castleRookStart;
			protected final int castleRookDestination;

			/******************************************
			 * Creates CastleMove object
			 * @param board
			 * @param movedPiece
			 * @param destinationCoordinate
			 * @param castleRook
			 * @param castleRookStart
			 * @param castleRookDestination
			 *******************************************/
		    public CastleMove(final Board board,
		    		final Piece movedPiece,
		    		final int destinationCoordinate,
		    		final Rook castleRook,
		    		final int castleRookStart,
		    		final int castleRookDestination) {
			  super(board, movedPiece, destinationCoordinate);
			  this.castleRook = castleRook;
			  this.castleRookStart = castleRookStart;
			  this.castleRookDestination = castleRookDestination; 
					
			}
		    
		    /****************************
		     * Returns the castled Rook
		     * @return Rook castleRook
		     ****************************/
		    public Rook getCastleRook(){
		    	return this.castleRook;
		    }
		    
		    /*********************************************
		     * Returns true that this is a castling move.
		     * @return True
		     *********************************************/
		    @Override
		    public boolean isCastlingMove(){
		    	return true;
		    }
		    
		    /**********************************************************
		     * Creates a new board object to reflect the castle move
		     * @return Board
		     **********************************************************/
		    @Override
		    public Board execute(){
		    	//Builder object to create board
				final Builder builder = new Builder();
				
				
				/***************************************************************
				 * Loops through all the current player's piece and sets them
				 * on the board. But will not do this for the piece that was
				 * moved.
				 ***************************************************************/
				for(final Piece piece : this.board.currentPlayer().getActivePieces())
					if(!this.movedPiece.equals(piece) && !this.castleRook.equals(piece))
						builder.setPiece(piece);
				
				//Loops through all the opponent's pieces and sets them
				for(final Piece piece : this.board.currentPlayer().getOpponent().getActivePieces())
					builder.setPiece(piece);
				
				
				//set the King piece to new position
				builder.setPiece(this.movedPiece.movePiece(this));
				//set the Rook piece to new position
				builder.setPiece(new Rook(this.castleRook.getPieceTeam(), this.castleRookDestination));
				//set the turn to the opponent
				builder.setMoveMaker(this.board.currentPlayer().getOpponent().getTeam());
				//creates and returns the new board
				return builder.build();
		    }
		    
		    @Override
		    public int hashCode(){
		    	final int prime = 31;
		    	int result = super.hashCode();
		    	result = prime * result + this.castleRook.hashCode();
		    	result = prime * result + this.castleRookDestination;
		    	return result;
		    }
		    
		    @Override
		    public boolean equals(final Object other){
		    	if(this == other)
		    		return true;
		    	
		    	if(!(other instanceof CastleMove))
		    		return false;
		    	
		    	final CastleMove otherCastleMove = (CastleMove)other;
		    	return super.equals(otherCastleMove) && this.castleRook.equals(otherCastleMove.getCastleRook());
		    }
				
		 }
//<----------------------------------------------------------------------------------------------------------------------------------------------------------->
		 
//<----------------------------------------------------------------------------------------------------------------------------------------------------------->	
		/*****************************************************************************
	     * Defines a King Side Castle Move
		 * @author Aaron Teague
		 * I was helped by the sites www.stackoverflow.com, www.stackexchange.com,
		 * and chessprogramming.wikispaces.com*
		 *****************************************************************************/
		 public static final class KingSideCastleMove extends CastleMove {

			public KingSideCastleMove(final Board board,
		    		final Piece movedPiece,
		    		final int destinationCoordinate,
		    		final Rook castleRook,
		    		final int castleRookStart,
		    		final int castleRookDestination) {
				   super(board, movedPiece, destinationCoordinate, castleRook, castleRookStart, castleRookDestination);
						
			}
			
			/****************************************************
			 * Prints the PGN notation for King Side Castle Move
			 * @Return "0-0"
			 ****************************************************/
			@Override
			public String toString() {
				return "0-0";
			}
			
			@Override
			public boolean equals(final Object other){
				return this == other || other instanceof KingSideCastleMove && super.equals(other);
			}
					
		 }
//<----------------------------------------------------------------------------------------------------------------------------------------------------------->

		 
//<----------------------------------------------------------------------------------------------------------------------------------------------------------->	
		/***************************************************
		 * Defines a Queen Side Castle Move
	     * @author Aaron Teague
		 * I was helped by the sites www.stackoverflow.com, www.stackexchange.com,
		 * and chessprogramming.wikispaces.com
		 ***************************************************/
		 public static final class QueenSideCastleMove extends CastleMove {

			public QueenSideCastleMove(final Board board,
		    		final Piece movedPiece,
		    		final int destinationCoordinate,
		    		final Rook castleRook,
		    		final int castleRookStart,
		    		final int castleRookDestination) {
			       super(board, movedPiece, destinationCoordinate, castleRook, castleRookStart, castleRookDestination);
							
			}
			
			/****************************************************
			 * Prints the PGN notation for Queen Side Castle Move
			 * @Return "0-0-0"
			 ****************************************************/
			@Override
			public String toString() {
				return "0-0-0";
			}
			
			@Override
			public boolean equals(final Object other){
				return this == other || other instanceof QueenSideCastleMove && super.equals(other);
			}
						
		}
//<----------------------------------------------------------------------------------------------------------------------------------------------------------->

//<----------------------------------------------------------------------------------------------------------------------------------------------------------->	
	   /***************************************************
	    * Defines a NullMove
		* @author Aaron Teague
	    * I was helped by the sites www.stackoverflow.com, www.stackexchange.com,
		* and chessprogramming.wikispaces.com
		***************************************************/
	    public static final class NullMove extends Move {

	      /**************************************************
	       * Will attempt to create a move with Null Values
	       *************************************************/
		  public NullMove() {
				 super(null, 65);
								
		   }
		  
		  /**************************************************************
		   * When an Invalid Move is attempted an exception is thrown
		   **************************************************************/
		  @Override
		  public Board execute(){
			  throw new RuntimeException("can't execute move!");
		  }
		  
		  @Override
		  public int getCurrentCoordinate(){
			  return -1;
		  }
							
		}
//<----------------------------------------------------------------------------------------------------------------------------------------------------------->

//<----------------------------------------------------------------------------------------------------------------------------------------------------------->
	    /*********************************************************
	     * Checks if the Move is a legal move
	     * @author Aaron Teague
	     * I was helped by the sites www.stackoverflow.com, www.stackexchange.com,
		 * and chessprogramming.wikispaces.com
	     *********************************************************/
	    public static class MoveFactory {
	    	
	    	private MoveFactory(){
	    		throw new RuntimeException("Not Valid!");
	    	}
	    	
	    	/********************************************************
	    	 * Iterates all through all legal to check if the move is
	    	 * in the board's legal moves
	    	 * @param board
	    	 * @param currentCoordinate
	    	 * @param destinationCoordinate
	    	 * @return Move move
	    	 **********************************************************/
	    	public static Move createMove(final Board board, final int currentCoordinate, final int destinationCoordinate){
	    		for(final Move move: board.getAllLegalMoves()){
	    			if(move.getCurrentCoordinate() == currentCoordinate && move.getDestinationCoordinate() == destinationCoordinate){
	    					return move;
	    			}
	    		}
	    			
	    		return NULL_MOVE;
	    	}
	    }
//<----------------------------------------------------------------------------------------------------------------------------------------------------------->


}
