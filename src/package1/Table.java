package package1;

import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EtchedBorder;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Table {
	
	private Color lightSpaceColor = Color.decode("#FFFFFF");
	private Color darkSpaceColor = Color.decode("#0000FF");
	private static String defaultPiecePicPath;
	
	private JFrame chessFrame;
	private BoardPanel boardPanel;
	private MoveLog moveLog;
	private Board chessBoard;
	private Space startSpace;
	private Space endSpace;
	private Piece playerMovedPiece;
	private GraveYardPanel GraveYard;
	
	
	
	public Table(){
		
		this.GraveYard = new GraveYardPanel(); 
		this.chessFrame = new JFrame("Chess");
		this.chessFrame.setSize(600, 600);
		this.chessFrame.setLayout(new BorderLayout());
		this.chessBoard = Board.createStartBoard();
		this.defaultPiecePicPath = "ChessImages/";
		this.boardPanel = new BoardPanel();
		this.moveLog = new MoveLog();
		this.chessFrame.add(this.GraveYard, BorderLayout.EAST);
		this.chessFrame.add(this.boardPanel, BorderLayout.CENTER);
		this.chessFrame.setVisible(true);
		
	}
	
	private class BoardPanel extends JPanel {
		List<SpacePanel> boardSpaces;
		
		BoardPanel(){
			super(new GridLayout(8,8));
			this.boardSpaces = new ArrayList<>();
			for(int i = 0; i < 64; i++){
				SpacePanel spacePanel = new SpacePanel(this, i);
				this.boardSpaces.add(spacePanel);
				add(spacePanel);
			}
			
			setPreferredSize(new Dimension(400, 350));
			validate();
		}
		
		public void drawBoard(Board board){
			removeAll();
			for(SpacePanel spacePanel : boardSpaces){
				
				spacePanel.drawSpace(board);
				add(spacePanel);
				
			}
			
			validate();
			repaint();
		}
	}
	
	public static class MoveLog {
		private final List<Move> moves;
		
		MoveLog() {
			this.moves = new ArrayList<>();
		}
		
		public List<Move> getMoves() {
			return this.moves;
		}
		
		public void addMove(Move move) {
			this.moves.add(move);
		}
		
		public int size() {
			return this.moves.size();
		}
		
		public void clear() {
			this.moves.clear();
		}
		
		public Move removeMove(int i){
			return this.moves.remove(i);
		}
		
		public boolean removeMove(Move move){
			return this.moves.remove(move);
		}
	}
	
	private class SpacePanel extends JPanel {
		
		private int spaceID;
		
		SpacePanel(BoardPanel board, final int spaceID){
			
			super(new GridBagLayout());
			this.spaceID = spaceID;
			setPreferredSize(new Dimension(10, 10));
			assignSpaceColor();
			assignSpacePiecePic(chessBoard);
			
			addMouseListener(new MouseListener(){

				@Override
				public void mouseClicked(MouseEvent e) {
					
					//A right mouse click indicates the move is canceled
					if(SwingUtilities.isRightMouseButton(e)){
						
						startSpace = null;
						endSpace = null;
						playerMovedPiece = null;
						
						//Left click indicates start of move
						} else if(SwingUtilities.isLeftMouseButton(e)){
							//Checks if the space is free
							if(startSpace == null) {
								
								startSpace = chessBoard.getSpace(spaceID);
								playerMovedPiece = startSpace.getPiece();
								
								if(playerMovedPiece == null){
									startSpace = null;
								}
							} else {
								
								endSpace = chessBoard.getSpace(spaceID);
								//Move object is created
								Move move = Move.MoveCreate.createMove(chessBoard, startSpace.getSpaceCoordinate(), 
										    endSpace.getSpaceCoordinate());
								
								//The Move is checked if it valid and executed if it is
								MoveChange change = chessBoard.getCurrentPlayer().makeMove(move);
								
								
								
								//Checks if the move was able to be finished and the current board is reassigned
								if(change.getMoveState().isFinished()){
									chessBoard = change.getChangeBoard();
									moveLog.addMove(move);
								}
								
								
								startSpace = null;
								endSpace = null;
								playerMovedPiece = null;
							}
							
							SwingUtilities.invokeLater(new Runnable() {

								@Override
								public void run() {
									GraveYard.redo(moveLog);
									boardPanel.drawBoard(chessBoard);
									
								}
							});
						
					    }
					
				}

				@Override
				public void mouseEntered(MouseEvent arg0) {
					// TODO Auto-generated method stub
					
				}

				@Override
				public void mouseExited(MouseEvent arg0) {
					// TODO Auto-generated method stub
					
				}

				@Override
				public void mousePressed(MouseEvent arg0) {
					// TODO Auto-generated method stub
					
				}

				@Override
				public void mouseReleased(MouseEvent arg0) {
					// TODO Auto-generated method stub
					
				}
				
			});
			
			validate();
			
		}
		
		
		
		public void drawSpace(Board board){
			assignSpaceColor();
			assignSpacePiecePic(board);
			validate();
			repaint();
		}
		
		/********************************************
		 * Assigns the correct color to each space
		 ********************************************/
		private void assignSpaceColor() {
			if (Board.FIRST_ROW.get(this.spaceID) ||
	                Board.THIRD_ROW.get(this.spaceID) ||
	                Board.FIFTH_ROW.get(this.spaceID) ||
	                Board.SEVENTH_ROW.get(this.spaceID)) {
	                setBackground(this.spaceID % 2 == 0 ? lightSpaceColor : darkSpaceColor);
	            } else if(Board.SECOND_ROW.get(this.spaceID) ||
	                      Board.FOURTH_ROW.get(this.spaceID) ||
	                      Board.SIXTH_ROW.get(this.spaceID)  ||
	                      Board.EIGHTH_ROW.get(this.spaceID)) {
	                setBackground(this.spaceID % 2 != 0 ? lightSpaceColor : darkSpaceColor);
	           }
			
		}
		
		/************************************************
		 * Assign the correct piece image to each space
		 * @param board
		 ************************************************/
		private void assignSpacePiecePic(Board board){
			this.removeAll();
			if(board.getSpace(this.spaceID).isSpaceUsed()){
				
				//Assigns the proper piece image to each space
				try {
					BufferedImage pic = 
							ImageIO.read(new File(defaultPiecePicPath +
		                            board.getSpace(this.spaceID).getPiece().getTeam().toString().substring(0, 1) + "" +
		                            board.getSpace(this.spaceID).getPiece().toString() + ".gif"));
					this.add(new JLabel(new ImageIcon(pic)));		
				} catch (IOException e) {
					e.printStackTrace();
				}
				
			}
			
		}
	
	}
	

}
