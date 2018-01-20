package Tests;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import chess.engine.board.Board;
import chess.engine.board.Board.Builder;
import chess.engine.board.BoardUtils;
import chess.engine.board.Move.MoveFactory;
import chess.engine.pieces.Bishop;
import chess.engine.pieces.King;
import chess.engine.pieces.Pawn;
import chess.engine.pieces.Team;
import chess.engine.player.MoveTransition;
public class TestStaleMate {

	@Test
    public void testAnandKramnikStaleMate() {

        final Builder builder = new Builder();
        // Black Layout
        builder.setPiece(new Pawn(Team.BLACK, 14));
        builder.setPiece(new Pawn(Team.BLACK, 21));
        builder.setPiece(new King(Team.BLACK, 36, false, false));
        // White Layout
        builder.setPiece(new Pawn(Team.WHITE, 29));
        builder.setPiece(new King(Team.WHITE, 31, false, false));
        builder.setPiece(new Pawn(Team.WHITE, 39));
        // Set the current player
        builder.setMoveMaker(Team.BLACK);
        final Board board = builder.build();
        assertFalse(board.currentPlayer().isInStaleMate());
        final MoveTransition t1 = board.currentPlayer()
                .makeMove(MoveFactory.createMove(board, BoardUtils.getCoordinateAtPosition("e4"),
                        BoardUtils.getCoordinateAtPosition("f5")));
        assertTrue(t1.getMoveStatus().isDone());
        assertTrue(t1.getToBoard().currentPlayer().isInStaleMate());
        assertFalse(t1.getToBoard().currentPlayer().isInCheck());
        assertFalse(t1.getToBoard().currentPlayer().isInCheckMate());
    }

    @Test
    public void testAnonymousStaleMate() {
        final Builder builder = new Builder();
        // Black Layout
        builder.setPiece(new King(Team.BLACK, 2, false, false));
        // White Layout
        builder.setPiece(new Pawn(Team.WHITE, 10));
        builder.setPiece(new King(Team.WHITE, 26, false, false));
        // Set the current player
        builder.setMoveMaker(Team.WHITE);
        final Board board = builder.build();
        assertFalse(board.currentPlayer().isInStaleMate());
        final MoveTransition t1 = board.currentPlayer()
                .makeMove(MoveFactory.createMove(board, BoardUtils.getCoordinateAtPosition("c5"),
                        BoardUtils.getCoordinateAtPosition("c6")));
        assertTrue(t1.getMoveStatus().isDone());
        assertTrue(t1.getToBoard().currentPlayer().isInStaleMate());
        assertFalse(t1.getToBoard().currentPlayer().isInCheck());
        assertFalse(t1.getToBoard().currentPlayer().isInCheckMate());
    }

    @Test
    public void testAnonymousStaleMate2() {
        final Builder builder = new Builder();
        // Black Layout
        builder.setPiece(new King(Team.BLACK, 0, false, false));
        // White Layout
        builder.setPiece(new Pawn(Team.WHITE, 16));
        builder.setPiece(new King(Team.WHITE, 17, false, false));
        builder.setPiece(new Bishop(Team.WHITE, 19));
        // Set the current player
        builder.setMoveMaker(Team.WHITE);
        final Board board = builder.build();
        assertFalse(board.currentPlayer().isInStaleMate());
        final MoveTransition t1 = board.currentPlayer()
                .makeMove(MoveFactory.createMove(board, BoardUtils.getCoordinateAtPosition("a6"),
                        BoardUtils.getCoordinateAtPosition("a7")));
        assertTrue(t1.getMoveStatus().isDone());
        assertTrue(t1.getToBoard().currentPlayer().isInStaleMate());
        assertFalse(t1.getToBoard().currentPlayer().isInCheck());
        assertFalse(t1.getToBoard().currentPlayer().isInCheckMate());
}

}
