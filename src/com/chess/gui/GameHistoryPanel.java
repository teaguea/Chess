package com.chess.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import chess.engine.board.Board;
import chess.engine.board.Move;


/***************************************************************************
 * Constructs and renders the Players Move using PNG notation
 * @author Aaron Teague
 * I was helped by the sites www.stackoverflow.com, www.stackexchange.com,
 * and chessprogramming.wikispaces.com
 ***************************************************************************/
public class GameHistoryPanel extends JPanel{
	
	
	private static final long serialVersionUID = 1L;
	private final DataModel model;
	private final JScrollPane scrollPane;
	private static final Dimension HISTORY_PANEL_DIMENSION = new Dimension(100, 400);
	
	GameHistoryPanel(){
		this.setLayout(new BorderLayout());
		this.model = new DataModel();
		final JTable table = new JTable(model);
		table.setRowHeight(15);
		this.scrollPane = new JScrollPane(table);
		scrollPane.setColumnHeaderView(table.getTableHeader());
		scrollPane.setPreferredSize(HISTORY_PANEL_DIMENSION);
		this.add(scrollPane, BorderLayout.CENTER);
		this.setVisible(true);
	}
	
	/*******************************************************************
	 * Updates/draws the Game History Panel
	 * @param board
	 * @param moveHistory
	 ********************************************************************/
	void redo(final Board board, final Table.MoveLog moveHistory){
		int currentRow = 0;
		this.model.clear();
		for(final Move move : moveHistory.getMoves()){
			final String moveText = move.toString();
			if(move.getMovedPiece().getPieceTeam().isWhite()){
				this.model.setValueAt(moveText, currentRow, 0);
			} else if(move.getMovedPiece().getPieceTeam().isBlack()){
				this.model.setValueAt(moveText, currentRow, 1);
				currentRow++;
			}
		}
		
		if(moveHistory.getMoves().size() > 0){
			final Move lastMove = moveHistory.getMoves().get(moveHistory.size() - 1);
			final String moveText = lastMove.toString();
			
			if(lastMove.getMovedPiece().getPieceTeam().isWhite()){
				this.model.setValueAt(moveText + calculateCheckAndCheckMateHash(board), currentRow, 0);
			}else if(lastMove.getMovedPiece().getPieceTeam().isBlack()){
				this.model.setValueAt(moveText + calculateCheckAndCheckMateHash(board), currentRow -1, 1);
				
			}
			
			final JScrollBar vertical = scrollPane.getVerticalScrollBar();
			vertical.setValue(vertical.getMaximum());
			
		}
	}
	
	/*******************************************************************
	 * Checks if the Current player is either check mate or check and
	 * returns the correct symbol
	 * @param board
	 * @return String
	 *******************************************************************/
	private String calculateCheckAndCheckMateHash(final Board board) {
		if(board.currentPlayer().isInCheckMate())
			return "#";
		else if(board.currentPlayer().isInCheck())
			return "+";
		return "";
	}

//<------------------------------------------------------------------------------------------------------------------------------------------------------------>
	
	private static class DataModel extends DefaultTableModel {
	
		private static final long serialVersionUID = 1L;
		private final List<Row> values;
		private static final String[] NAMES = {"White", "Black"};
		
		DataModel(){
			this.values = new ArrayList<>();
		}
		
		/*********************************
		 * Clears the Game History
		 *********************************/
		public void clear(){
			this.values.clear();
			setRowCount(0);
		}
		
		/********************************
		 * Returns the number of Rows
		 * @return int
		 ********************************/
		@Override
		public int getRowCount(){
			if(this.values == null)
				return 0;
			
			return this.values.size();
		}
		
		/*******************************
		 * Returns the number of Columns
		 * @return int
		 *******************************/
		@Override
		public int getColumnCount(){
			return NAMES.length;
		}
		
		
		/********************************************
		 * How to render a column on a given row
		 * @return Object
		 ********************************************/
		@Override
		public Object getValueAt(final int row, final int column){
			final Row currentRow = this.values.get(row);
			
			if(column == 0)
				return currentRow.getWhiteMove();
			else
				return currentRow.getBlackMove();
			
		}
		
		/*********************************************
		 * Sets the values in the rows
		 ********************************************/
		@Override
		public void setValueAt(final Object aValue, final int row, final int column){
			final Row currentRow;
			if(this.values.size() <= row){
				currentRow = new Row();
				this.values.add(currentRow);
			}
			else {
				currentRow = this.values.get(row);
			}
			
			if(column == 0){
				currentRow.setWhiteMove((String)aValue);
				fireTableRowsInserted(row, row);
			}
			else if(column == 1){
				currentRow.setBlackMove((String)aValue);
				fireTableCellUpdated(row, column);
			}
			
		}
		
		@Override
		public Class<?> getColumnClass(final int column){
			return Move.class;
			
		}
		
		@Override
		public String getColumnName(final int column){
			return NAMES[column];
		}
		
	}
	
//<------------------------------------------------------------------------------------------------------------------------------------------------------------>
	
//<------------------------------------------------------------------------------------------------------------------------------------------------------------>
	
	private static class Row{
		private String whiteMove;
		private String blackMove;
		
		Row(){
			
		}
		
		/***********************************
		 * Returns the White Move
		 * @return String whiteMove
		 ***********************************/
		public String getWhiteMove(){
			return this.whiteMove;
		}
		
		/**********************************
		 * Returns the Black Move
		 * @return String blackMove
		 **********************************/
		public String getBlackMove(){
			return this.blackMove;
		}
		
		/*******************************************
		 * Sets the White Move
		 * @param move
		 *******************************************/
		public void setWhiteMove(final String move){
			this.whiteMove = move;
		}
		
		/********************************************
		 * Sets the black move
		 * @param move
		 *******************************************/
		public void setBlackMove(final String move){
			this.blackMove = move;
		}
	}
	
//<------------------------------------------------------------------------------------------------------------------------------------------------------------>	


}
