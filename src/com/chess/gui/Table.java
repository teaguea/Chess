package com.chess.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.*;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;






import com.google.common.collect.Lists;

import chess.engine.board.Board;
import chess.engine.board.BoardUtils;
import chess.engine.board.Move;
import chess.engine.board.Tile;
import chess.engine.pieces.Piece;
import chess.engine.player.MoveTransition;

public class Table {
	private final Color lightTileColor = Color.decode("#FFFFFF"); 
	private final Color darkTileColor = Color.decode("#0000FF");
	
	private final JFrame gameFrame;
	private final GameHistoryPanel gameHistoryPanel;
	private final GraveyardPanel graveyardPanel;
	private final BoardPanel boardPanel;
	private final MoveLog moveLog;
	
	private Board chessBoard;
	
	private Tile sourceTile;
	private Tile destinationTile;
	private Piece humanMovedPiece;
	private BoardDirection boardDirection;
	
	private boolean highLightLegalMoves;
	
	private static final Dimension OUTER_FRAME_DIMENSION = new Dimension(600, 600);
	private static final Dimension BOARD_PANEL_DIMENSION = new Dimension(400, 350);
	private static final Dimension TILE_PANEL_DIMENSION = new Dimension(10, 10);
	private static String defaultPieceImagePath = "ChessImages/";
	
	public Table(){
		this.gameFrame = new JFrame("Chess");
		this.gameFrame.setLayout(new BorderLayout());
		final JMenuBar tableMenuBar = createMenuBar();
		this.gameFrame.setJMenuBar(tableMenuBar);
		this.gameFrame.setSize(OUTER_FRAME_DIMENSION);
		this.chessBoard = Board.createStandardBoard();
		this.gameHistoryPanel = new GameHistoryPanel();
		this.graveyardPanel = new GraveyardPanel(); 
		this.boardPanel = new BoardPanel();
		this.moveLog = new MoveLog();
		this.boardDirection = BoardDirection.NORMAL;
		this.highLightLegalMoves = false;
		this.gameFrame.add(this.graveyardPanel, BorderLayout.WEST);
		this.gameFrame.add(this.boardPanel, BorderLayout.CENTER);
		this.gameFrame.add(this.gameHistoryPanel, BorderLayout.EAST);
		this.gameFrame.setVisible(true);
	}

	/******************************************
	 * Create the Menu Bar 
	 * @param tableMenuBar
	 * @return JMenuBar tableMenuBar
	 ******************************************/
	private JMenuBar createMenuBar() {
		final JMenuBar tableMenuBar = new JMenuBar();
		tableMenuBar.add(createFileMenu());
		tableMenuBar.add(createPreferencesMenu());
		return tableMenuBar;
	}
	
	/********************************
	 * Creates options for the game
	 * @return JMenu fileMenu
	 ********************************/
	private JMenu createFileMenu(){
		final JMenu fileMenu = new JMenu("File");
		final JMenuItem openPGN = new JMenuItem("Load PGN File");
		openPGN.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e){
				System.out.println("Open PGN File");
			}
			
		});
		
		fileMenu.add(openPGN);
		
		final JMenuItem exitMenuItem = new JMenuItem("Exit");
		exitMenuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e){
				System.exit(0);
			}
		});
		fileMenu.add(exitMenuItem);
		
		return fileMenu;
	}
	
	private JMenu createPreferencesMenu(){
		
		final JMenu preferencesMenu = new JMenu("Preferences");
		final JMenuItem flipBoardMenuItem = new JMenuItem("Flip Board");
		flipBoardMenuItem.addActionListener(new ActionListener() {

			/******************************************************
			 * If the Flip Board option is invoked the board will
			 * be flipped
			 ******************************************************/
			@Override
			public void actionPerformed(final ActionEvent e) {
				//Reverse the board's direction
				boardDirection = boardDirection.opposite();
				boardPanel.drawBoard(chessBoard);
			}
		});
		
		preferencesMenu.add(flipBoardMenuItem);
		preferencesMenu.addSeparator();
		
		final JCheckBoxMenuItem legalMoveHighLightCheckbox = new JCheckBoxMenuItem("Highlight Lega Moves", false);
		
		legalMoveHighLightCheckbox.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				highLightLegalMoves = legalMoveHighLightCheckbox.isSelected();
				
			}
			
		});
		
		preferencesMenu.add(legalMoveHighLightCheckbox);
		
		return preferencesMenu;
	}
	
	/******************************************************************
	 * Indicates whether the board is in a normal orientation or a
	 * flipped orientation
	 * @author Aaron Teague
	 *****************************************************************/
	public enum BoardDirection {
		NORMAL {

			/****************************************************************
			 * Will traverse the in order in which the tiles were passed in
			 * @return List<TilePanel> boardTiles
			 ****************************************************************/
			@Override
			List<TilePanel> traverse(List<TilePanel> boardTiles) {
				return boardTiles;
			}

			/*************************************************
			 * Returns the opposite orientation of normal
			 * @return FLIPPED
			 *************************************************/
			@Override
			BoardDirection opposite() {
				return FLIPPED;
			}
		},
		
		FLIPPED{

			/*****************************************************
			 * Will traverse the tiles in reverse order
			 * @return Lists.reverse(boardTiles)
			 *****************************************************/
			@Override
			List<TilePanel> traverse(List<TilePanel> boardTiles) {
				return Lists.reverse(boardTiles);
			}

			/*************************************************
			 * Returns the opposite orientation of flipped
			 * @return NORMAL
			 *************************************************/
			@Override
			BoardDirection opposite() {
				return NORMAL;
			}
		};
		
		abstract List<TilePanel> traverse(final List<TilePanel> boardTiles);
		abstract BoardDirection opposite();
	}
	
	private class BoardPanel extends JPanel {
		
		private static final long serialVersionUID = 1L;
		final List<TilePanel> boardTiles;
		
		
		BoardPanel() {
			//Adds 64 tile panel to the boardTiles list and tiles are added to the board panel
			super(new GridLayout(8,8));
			this.boardTiles = new ArrayList<>();
			for(int i = 0;  i < BoardUtils.NUM_TILES; i++){
				final TilePanel tilePanel = new TilePanel(this, i); 
				this.boardTiles.add(tilePanel);
				this.add(tilePanel);
			}
			
			this.setPreferredSize(BOARD_PANEL_DIMENSION);
			this.validate();
		}
		
		/**********************************************************
		 * Redraws the board on the GUI after a move is executed
		 * @param Board board
		 ***********************************************************/
		public void drawBoard(final Board board){
			this.removeAll();
			//Loops in the order of traverse
			for(final TilePanel tilePanel : boardDirection.traverse(boardTiles)){
				tilePanel.drawTile(board);
				this.add(tilePanel);
			}
			
			this.validate();
			this.repaint();
		}
	}
	
	
	public static class MoveLog {
		private final List<Move> moves;
		
		MoveLog(){
			this.moves = new ArrayList<>();
		}
		
		public List<Move> getMoves(){
			return this.moves;
		}
		
		public void addMove(final Move move){
			this.moves.add(move);
		}
		
		public int size(){
			return this.moves.size();
		}
		
		public void clear(){
			this.moves.clear();
		}
		
		public Move removeMove(int index){
			return this.moves.remove(index);
		}
		
		public boolean removeMove(final Move move){
			return this.moves.remove(move);
		}
	}
	
	
	private class TilePanel extends JPanel {
		
		private static final long serialVersionUID = 1L;
		private final int tileId;
		
		TilePanel(final BoardPanel boardPanel, final int tileId){
			super(new GridBagLayout());
			this.tileId = tileId;
			this.setPreferredSize(TILE_PANEL_DIMENSION);
			this.assignTileColor();
			this.assignTilePieceIcon(chessBoard);
			
			this.addMouseListener(new MouseListener() {

				/********************************************************************
				 * Executes moves on the board according to the user's mouse clicks
				 * @param MouseEvent e
				 *******************************************************************/
				@Override
				public void mouseClicked(MouseEvent e) {
					
					//If the user right click on a tile, it will can any piece selection
					if(SwingUtilities.isRightMouseButton(e)){
						
						//A right click will cancel every thing out
						sourceTile = null;
						destinationTile = null;
						humanMovedPiece = null;
						
				
						}
						else if (SwingUtilities.isLeftMouseButton(e)){
							//If user left click and the sourceTile is null then we'll want to assign the the sourceTile
							if(sourceTile == null){
								sourceTile = chessBoard.getTile(tileId);
								//Assign the piece that the user is moving to the piece on the sourceTile
								humanMovedPiece = sourceTile.getPiece();
								//If there is no piece on the sourceTile then the event is canceled out
								if(humanMovedPiece == null) {
								   sourceTile = null;	
								}
							} else {
								destinationTile = chessBoard.getTile(tileId);
								//Get the move based on the sourceTile and destinationTile if it's in the legal moves, otherwise a null move will be returned
								final Move move = Move.MoveFactory.createMove(chessBoard, 
										                                      sourceTile.getTileCoordinate(), 
										                                      destinationTile.getTileCoordinate());
								//Make the move and see if it is possible to execute
								final MoveTransition transition = chessBoard.currentPlayer().makeMove(move);
								//If the transition's move status is done then we know we can execute the move
								if(transition.getMoveStatus().isDone()){
									chessBoard = transition.getToBoard();
									moveLog.addMove(move);
								}
								
								//Clear the state of the board
								sourceTile = null;
								destinationTile = null;
								humanMovedPiece = null;
							}
							
							SwingUtilities.invokeLater(new Runnable() {

								@Override
								public void run() {
									gameHistoryPanel.redo(chessBoard, moveLog);
									graveyardPanel.redo(moveLog);
									boardPanel.drawBoard(chessBoard);
								}
								
							});
						}			
				}

				@Override
				public void mouseEntered(MouseEvent e) {
				
					
				}

				@Override
				public void mouseExited(MouseEvent e) {
					
					
				}

				@Override
				public void mousePressed(MouseEvent e) {
					
					
				}

				@Override
				public void mouseReleased(MouseEvent e) {
					
					
				} 
				
			});			
			
			this.validate();
			
		}
		
		/****************************************
		 * Draws the Tile on the GUI board
		 * @param Board board
		 ****************************************/
		public void drawTile(final Board board){
			this.assignTileColor();
			this.assignTilePieceIcon(board);
			this.highLightLegals(board);
			this.validate();
			this.repaint();
			
		}
		
		/********************************************
		 * Assigns the correct gif to the TilePanel
		 ********************************************/
		private void assignTilePieceIcon(final Board board){
			this.removeAll();
			if(board.getTile(this.tileId).isTileOccupied()){
				
				try {
					final BufferedImage image =
						ImageIO.read(new File(defaultPieceImagePath + board.getTile(this.tileId).getPiece().getPieceTeam().toString().substring(0, 1) + 
								board.getTile(this.tileId).getPiece().toString() + ".gif") );
					
					this.add(new JLabel(new ImageIcon(image)));
					
				} catch (IOException e) {
					e.printStackTrace();
				}	
			}
		}
		
		/***********************************************************
		 * Marks the legal spaces the Piece can move to.
		 * @param board
		 ***********************************************************/
		private void highLightLegals(final Board board){
			if(highLightLegalMoves){
				//Loop through all the legal moves
				for(final Move move : pieceLegalMoves(board)){
					//If the move's destination tile matches this tile then it will be marked with a green dot
					if(move.getDestinationCoordinate() == this.tileId){
					   try {
						 add(new JLabel(new ImageIcon(ImageIO.read(new File("ChessImages/green_dot.png")))));
					   }
					   catch(Exception e){
						 e.printStackTrace();  
					   }
					}
				}
			}
		}

		/*************************************************************
		 * Returns the current player's legal moves
		 * @param board
		 * @return List of legalMoves
		 ***************************************************************/
		private Collection<Move> pieceLegalMoves(final Board board) {
			if(humanMovedPiece != null && humanMovedPiece.getPieceTeam() == board.currentPlayer().getTeam()){
				return humanMovedPiece.calculateLegalMoves(board);
			}
			
			return Collections.emptyList();
		}

		/**************************************************************
		 * Assigns the correct color to the tile by what row the tile
		 * is in and whether or not it's coordinate is even or odd
		 **************************************************************/
		private void assignTileColor() {
			if(BoardUtils.EIGHTH_RANK[this.tileId] ||
					BoardUtils.SIXTH_RANK[this.tileId] ||
					BoardUtils.FORTH_RANK[this.tileId] ||
					BoardUtils.SECOND_RANK[this.tileId]){
				setBackground(this.tileId % 2 == 0 ? lightTileColor : darkTileColor);
			} else if(BoardUtils.SEVENTH_RANK[this.tileId] ||
					  BoardUtils.FIFTH_RANK[this.tileId] ||
					  BoardUtils.THIRD_RANK[this.tileId] ||
					  BoardUtils.FIRST_RANK[this.tileId]){
				setBackground(this.tileId % 2 != 0 ? lightTileColor : darkTileColor);
			}	
		}	
	}
}
