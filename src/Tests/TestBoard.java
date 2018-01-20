package Tests;


import chess.engine.board.Board;
import chess.engine.board.Board.Builder;
import chess.engine.board.BoardUtils;
import chess.engine.board.Move;
import chess.engine.board.Move.MoveFactory;
import chess.engine.board.Tile;
import chess.engine.pieces.Bishop;
import chess.engine.pieces.King;
import chess.engine.pieces.Knight;
import chess.engine.pieces.Pawn;
import chess.engine.pieces.Piece;


import chess.engine.pieces.Queen;
import chess.engine.pieces.Rook;
import chess.engine.pieces.Team;
import chess.engine.player.MoveTransition;

import com.google.common.collect.Iterables;

import org.junit.Test;

import static junit.framework.Assert.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class TestBoard {

	
	@Test
	    public void initialBoard() {

	        final Board board = Board.createStandardBoard();
	        assertEquals(board.currentPlayer().getLegalMoves().size(), 20);
	        assertEquals(board.currentPlayer().getOpponent().getLegalMoves().size(), 20);
	        assertFalse(board.currentPlayer().isInCheck());
	        assertFalse(board.currentPlayer().isInCheckMate());
	        assertFalse(board.currentPlayer().isCastled());
	        assertTrue(board.currentPlayer().isKingSideCastleCapable());
	        assertTrue(board.currentPlayer().isQueenSideCastleCapable());
	        assertEquals(board.currentPlayer(), board.getWhitePlayer());
	        assertEquals(board.currentPlayer().getOpponent(), board.getBlackPlayer());
	        assertFalse(board.currentPlayer().getOpponent().isInCheck());
	        assertFalse(board.currentPlayer().getOpponent().isInCheckMate());
	        assertFalse(board.currentPlayer().getOpponent().isCastled());
	        assertTrue(board.currentPlayer().getOpponent().isKingSideCastleCapable());
	        assertTrue(board.currentPlayer().getOpponent().isQueenSideCastleCapable());
	        assertTrue(board.getWhitePlayer().toString().equals("White"));
	        assertTrue(board.getBlackPlayer().toString().equals("Black"));

	        final Iterable<Piece> allPieces = board.getAllPieces();
	        final Iterable<Move> allMoves = Iterables.concat(board.getWhitePlayer().getLegalMoves(), board.getBlackPlayer().getLegalMoves());
	        for(final Move move : allMoves) {
	            assertFalse(move.isAttack());
	            assertFalse(move.isCastlingMove());
	            
	        }

	        assertEquals(Iterables.size(allMoves), 40);
	        assertEquals(Iterables.size(allPieces), 32);
	        assertEquals(board.getTile(35).getPiece(), null);
	        assertEquals(board.getTile(35).getTileCoordinate(), 35);

	    }

	   
		@Test
	    public void testPlainKingMove() {

	        final Builder builder = new Builder();
	        // Black Layout
	        builder.setPiece(new King(Team.BLACK, 4, false, false));
	        builder.setPiece(new Pawn(Team.BLACK, 12));
	        // White Layout
	        builder.setPiece(new Pawn(Team.WHITE, 52));
	        builder.setPiece(new King(Team.WHITE, 60, false, false));
	        builder.setMoveMaker(Team.WHITE);
	        // Set the current player
	        final Board board = builder.build();
	        System.out.println(board);

	        assertEquals(board.getWhitePlayer().getLegalMoves().size(), 6);
	        assertEquals(board.getBlackPlayer().getLegalMoves().size(), 6);
	        assertFalse(board.currentPlayer().isInCheck());
	        assertFalse(board.currentPlayer().isInCheckMate());
	        assertFalse(board.currentPlayer().getOpponent().isInCheck());
	        assertFalse(board.currentPlayer().getOpponent().isInCheckMate());
	        assertEquals(board.currentPlayer(), board.getWhitePlayer());
	        assertEquals(board.currentPlayer().getOpponent(), board.getBlackPlayer());
	        

	        final Move move = MoveFactory.createMove(board, BoardUtils.getCoordinateAtPosition("e1"),
	                BoardUtils.getCoordinateAtPosition("f1"));

	        final MoveTransition moveTransition = board.currentPlayer()
	                .makeMove(move);

	        assertEquals(moveTransition.getTransitionMove(), move);
	        assertEquals(moveTransition.getFromBoard(), board);
	        assertEquals(moveTransition.getToBoard().currentPlayer(), moveTransition.getToBoard().getBlackPlayer());

	        assertTrue(moveTransition.getMoveStatus().isDone());
	        assertEquals(moveTransition.getToBoard().getWhitePlayer().getPlayerKing().getPiecePosition(), 61);
	        System.out.println(moveTransition.getToBoard());

	    }

	    
		@Test
	    public void testBoardConsistency() {
	        final Board board = Board.createStandardBoard();
	        assertEquals(board.currentPlayer(), board.getWhitePlayer());

	        final MoveTransition t1 = board.currentPlayer()
	                .makeMove(MoveFactory.createMove(board, BoardUtils.getCoordinateAtPosition("e2"),
	                        BoardUtils.getCoordinateAtPosition("e4")));
	        final MoveTransition t2 = t1.getToBoard()
	                .currentPlayer()
	                .makeMove(MoveFactory.createMove(t1.getToBoard(), BoardUtils.getCoordinateAtPosition("e7"),
	                        BoardUtils.getCoordinateAtPosition("e5")));

	        final MoveTransition t3 = t2.getToBoard()
	                .currentPlayer()
	                .makeMove(MoveFactory.createMove(t2.getToBoard(), BoardUtils.getCoordinateAtPosition("g1"),
	                        BoardUtils.getCoordinateAtPosition("f3")));
	        final MoveTransition t4 = t3.getToBoard()
	                .currentPlayer()
	                .makeMove(MoveFactory.createMove(t3.getToBoard(), BoardUtils.getCoordinateAtPosition("d7"),
	                        BoardUtils.getCoordinateAtPosition("d5")));

	        final MoveTransition t5 = t4.getToBoard()
	                .currentPlayer()
	                .makeMove(MoveFactory.createMove(t4.getToBoard(), BoardUtils.getCoordinateAtPosition("e4"),
	                        BoardUtils.getCoordinateAtPosition("d5")));
	        final MoveTransition t6 = t5.getToBoard()
	                .currentPlayer()
	                .makeMove(MoveFactory.createMove(t5.getToBoard(), BoardUtils.getCoordinateAtPosition("d8"),
	                        BoardUtils.getCoordinateAtPosition("d5")));

	        final MoveTransition t7 = t6.getToBoard()
	                .currentPlayer()
	                .makeMove(MoveFactory.createMove(t6.getToBoard(), BoardUtils.getCoordinateAtPosition("f3"),
	                        BoardUtils.getCoordinateAtPosition("g5")));
	        final MoveTransition t8 = t7.getToBoard()
	                .currentPlayer()
	                .makeMove(MoveFactory.createMove(t7.getToBoard(), BoardUtils.getCoordinateAtPosition("f7"),
	                        BoardUtils.getCoordinateAtPosition("f6")));

	        final MoveTransition t9 = t8.getToBoard()
	                .currentPlayer()
	                .makeMove(MoveFactory.createMove(t8.getToBoard(), BoardUtils.getCoordinateAtPosition("d1"),
	                        BoardUtils.getCoordinateAtPosition("h5")));
	        final MoveTransition t10 = t9.getToBoard()
	                .currentPlayer()
	                .makeMove(MoveFactory.createMove(t9.getToBoard(), BoardUtils.getCoordinateAtPosition("g7"),
	                        BoardUtils.getCoordinateAtPosition("g6")));

	        final MoveTransition t11 = t10.getToBoard()
	                .currentPlayer()
	                .makeMove(MoveFactory.createMove(t10.getToBoard(), BoardUtils.getCoordinateAtPosition("h5"),
	                        BoardUtils.getCoordinateAtPosition("h4")));
	        final MoveTransition t12 = t11.getToBoard()
	                .currentPlayer()
	                .makeMove(MoveFactory.createMove(t11.getToBoard(), BoardUtils.getCoordinateAtPosition("f6"),
	                        BoardUtils.getCoordinateAtPosition("g5")));

	        final MoveTransition t13 = t12.getToBoard()
	                .currentPlayer()
	                .makeMove(MoveFactory.createMove(t12.getToBoard(), BoardUtils.getCoordinateAtPosition("h4"),
	                        BoardUtils.getCoordinateAtPosition("g5")));
	        final MoveTransition t14 = t13.getToBoard()
	                .currentPlayer()
	                .makeMove(MoveFactory.createMove(t13.getToBoard(), BoardUtils.getCoordinateAtPosition("d5"),
	                        BoardUtils.getCoordinateAtPosition("e4")));

	        assertTrue(t14.getToBoard().getWhitePlayer().getActivePieces().size() == calculatedActivesFor(t14.getToBoard(), Team.WHITE));
	        assertTrue(t14.getToBoard().getBlackPlayer().getActivePieces().size() == calculatedActivesFor(t14.getToBoard(), Team.BLACK));

	    }

	    @Test(expected=RuntimeException.class)
	    public void testInvalidBoard() {

	        final Builder builder = new Builder();
	        // Black Layout
	        builder.setPiece(new Rook(Team.BLACK, 0));
	        builder.setPiece(new Knight(Team.BLACK, 1));
	        builder.setPiece(new Bishop(Team.BLACK, 2));
	        builder.setPiece(new Queen(Team.BLACK, 3));
	        builder.setPiece(new Bishop(Team.BLACK, 5));
	        builder.setPiece(new Knight(Team.BLACK, 6));
	        builder.setPiece(new Rook(Team.BLACK, 7));
	        builder.setPiece(new Pawn(Team.BLACK, 8));
	        builder.setPiece(new Pawn(Team.BLACK, 9));
	        builder.setPiece(new Pawn(Team.BLACK, 10));
	        builder.setPiece(new Pawn(Team.BLACK, 11));
	        builder.setPiece(new Pawn(Team.BLACK, 12));
	        builder.setPiece(new Pawn(Team.BLACK, 13));
	        builder.setPiece(new Pawn(Team.BLACK, 14));
	        builder.setPiece(new Pawn(Team.BLACK, 15));
	        // White Layout
	        builder.setPiece(new Pawn(Team.WHITE, 48));
	        builder.setPiece(new Pawn(Team.WHITE, 49));
	        builder.setPiece(new Pawn(Team.WHITE, 50));
	        builder.setPiece(new Pawn(Team.WHITE, 51));
	        builder.setPiece(new Pawn(Team.WHITE, 52));
	        builder.setPiece(new Pawn(Team.WHITE, 53));
	        builder.setPiece(new Pawn(Team.WHITE, 54));
	        builder.setPiece(new Pawn(Team.WHITE, 55));
	        builder.setPiece(new Rook(Team.WHITE, 56));
	        builder.setPiece(new Knight(Team.WHITE, 57));
	        builder.setPiece(new Bishop(Team.WHITE, 58));
	        builder.setPiece(new Queen(Team.WHITE, 59));
	        builder.setPiece(new Bishop(Team.WHITE, 61));
	        builder.setPiece(new Knight(Team.WHITE, 62));
	        builder.setPiece(new Rook(Team.WHITE, 63));
	        //white to move
	        builder.setMoveMaker(Team.WHITE);
	        //build the board
	        builder.build();
	    }

	    @Test
	    public void testAlgebreicNotation() {
	        assertEquals(BoardUtils.getPositionAtCoordinate(0), "a8");
	        assertEquals(BoardUtils.getPositionAtCoordinate(1), "b8");
	        assertEquals(BoardUtils.getPositionAtCoordinate(2), "c8");
	        assertEquals(BoardUtils.getPositionAtCoordinate(3), "d8");
	        assertEquals(BoardUtils.getPositionAtCoordinate(4), "e8");
	        assertEquals(BoardUtils.getPositionAtCoordinate(5), "f8");
	        assertEquals(BoardUtils.getPositionAtCoordinate(6), "g8");
	        assertEquals(BoardUtils.getPositionAtCoordinate(7), "h8");
	    }

	    @Test
	    public void tt() {

	        final Board.Builder builder = new Board.Builder();
	        //BLACK LAYOUT
	        builder.setPiece(new King(Team.BLACK, 4, false, false));
	        //WHITE LAYOUT
	        builder.setPiece(new King(Team.WHITE, 60, false, false));
	        builder.setPiece(new Bishop(Team.WHITE, 61));
	        //white to move
	        builder.setMoveMaker(Team.WHITE);
	    }


	    private static int calculatedActivesFor(final Board board,
	                                            final Team Team) {
	        int count = 0;
	        for (final Tile t : board.getGameBoard()) {
	            if (t.isTileOccupied() && t.getPiece().getPieceTeam().equals(Team)) {
	                count++;
	            }
	        }
	        return count;
	}

}