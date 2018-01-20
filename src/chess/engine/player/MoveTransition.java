package chess.engine.player;

import chess.engine.board.Board;
import chess.engine.board.Move;


/****************************************************************************
 * When we make a move we'll transition from board to 
 * another. This class will help carry the information
 * from the old board to the new board
 * @author Aaron Teague
 * I was helped by the sites www.stackoverflow.com, www.stackexchange.com,
 * and chessprogramming.wikispaces.com
 ****************************************************************************/
public class MoveTransition {
	
	private final Board fromBoard;
    private final Board toBoard;
    private final Move transitionMove;
    private final MoveStatus moveStatus;

    public MoveTransition(final Board fromBoard,
                          final Board toBoard,
                          final Move transitionMove,
                          final MoveStatus moveStatus) {
        this.fromBoard = fromBoard;
        this.toBoard = toBoard;
        this.transitionMove = transitionMove;
        this.moveStatus = moveStatus;
    }

    public Board getFromBoard() {
        return this.fromBoard;
    }

    public Board getToBoard() {
         return this.toBoard;
    }

    public Move getTransitionMove() {
        return this.transitionMove;
    }

    public MoveStatus getMoveStatus() {
        return this.moveStatus;
}

	public Board getTransitionBoard() {
		// TODO Auto-generated method stub
		return null;
	}

}
