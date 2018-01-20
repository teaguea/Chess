import com.chess.gui.Table;

import chess.engine.board.Board;


public class Chess {
	
	public static void main(String[] args){
		
		Board board = Board.createStandardBoard();
		
		System.out.println(board);
		
		Table table = new Table();
		
	}

}
