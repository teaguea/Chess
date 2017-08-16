package package1;
/*******************************************************************************
 * This is the abstract move class. This class form the basic
 * nearly all moves follow. The class contains
 * the board that move is played on. The actual piece that is
 * being moved. The coordinate that the move ends on and finally
 * if the move is a first move.
 * @author Aaron Teague
 * I was helped by stackoverflow.com and chessprogramming.wikispaces.com/
 *******************************************************************************/
public abstract class Move {
	
		 
	
	protected Board board;
	protected Piece movedPiece;
	protected int endCoordinate;
	protected boolean isFirst; 
	
    public static final Move InvalidMove = new InvalidMove();
	
	/******************************************************
	 * Initializes all of the attributes of the Move class to
	 * create Move object. And determines whether or not the
	 * move is a first move.
	 * @param board
	 * @param movedPiece
	 * @param endCoordinate
	 *******************************************************/
	Move(Board board, Piece movedPiece, int endCoordinate){
			
			this.board = board;
			this.movedPiece = movedPiece;
			this.endCoordinate = endCoordinate;
			this.isFirst = movedPiece.getFirst();
	}

	/****************************************************************
	 * This Move Constructor only takes a board and end coordinate
	 * as parameters. This constructor initializes the move as not
	 * first and the piece that was moved as null.
	 * @param board
	 * @param endCoordinate
	 ****************************************************************/
	Move(Board board, int endCoordinate){
		this.board = board;
		this.movedPiece = null;
		this.endCoordinate = endCoordinate;
		this.isFirst = false;
	}
 
	/***********************************
	 * @return The board from the Move
	 ***********************************/
	public Board getBoard(){
		return this.board;
	}

	/************************************************
	 * @return the coordinate of the end of the move
	 ************************************************/
	public Integer getEndCoordinate() {
		
		return this.endCoordinate;
	}
	
	/****************************************
	 * @return the piece that has been moved
	 ****************************************/
	public Piece getMovedPiece(){
		return this.movedPiece;
	}

	/*********************************************************
	 * @return the the coordinate of the piece that was moved
	 *********************************************************/
	public int getCurrentCoordinate(){
		return this.getMovedPiece().getPiecePosition();
	}
	
	/****************************************************************
	 * Executes the move by setting the piece to the end coordinate
	 * of the move. Resets all pieces and creates a new board 
	 * @return
	 ****************************************************************/
	public Board execute(){
		
		Board.Builder builder = new Board.Builder();
		
		//Sets all the none moving pieces to the new board
		for(Piece piece: this.board.getCurrentPlayer().getAlivePieces()){
			
			//checks if the piece is the moving piece
			if(!this.movedPiece.equals(piece)){
				builder.setPiece(piece);
			}
		}
		
		//Sets all the opposing pieces on the new board
		for(Piece piece : this.board.getCurrentPlayer().getOpponent().getAlivePieces()){
			
			//No check is required because none of the opposing pieces are being moved at this point
			builder.setPiece(piece);
		}
		
		builder.setPiece(this.movedPiece.movePiece(this));
		//The newPlayer is set to opponent's team
		builder.setNewPlayer(this.board.getCurrentPlayer().getOpponent().getTeam());
		
		return builder.build();
	}
	
	/***********************************************************
	 * Inner class that utilizes the Factory Design pattern
	 * to create moves. I was helped a lot by stackoverflow.com
	 * in creating this inner class and stackoverflow.com
	 * provided and introduced the Factory Design.
	 **********************************************************/
	public static class MoveCreate {
		
		public MoveCreate(){
			throw new RuntimeException("Sorry can't create");
		}

		public static Move createMove(Board board, int currentCoordinate, int endCoordinate){
			for(Move move : board.getAllLegalMoves()){
				if(move.getCurrentCoordinate() == currentCoordinate &&
				   move.getEndCoordinate() == endCoordinate){
					return move;
				}
			}
			
			return InvalidMove;
		}
		
	}
	
	public boolean isAttack(){
		return false;
	}
	
	public boolean isCastling(){
		return false;
	}
	
	public Piece getAttackedPiece(){
		return null;
	}


	@Override
	public int hashCode() {
		int result = 1;
        result = 31 * result + this.endCoordinate;
        result = 31 * result + this.movedPiece.hashCode();
        result = 31 * result + this.movedPiece.getPiecePosition();
        result = result + (isFirst ? 1 : 0);
          return result;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
            return true;
        }
        if (!(obj instanceof Move)) {
            return false;
        }
        final Move otherMove = (Move) obj;
        return getCurrentCoordinate() == otherMove.getCurrentCoordinate() &&
        		getEndCoordinate() == otherMove.getEndCoordinate() &&
        		getMovedPiece().equals(otherMove.getMovedPiece());
	}
	
	

}
