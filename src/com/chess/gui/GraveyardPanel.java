package com.chess.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EtchedBorder;

import chess.engine.board.Move;
import chess.engine.pieces.Piece;

import com.chess.gui.Table.MoveLog;
import com.google.common.primitives.Ints;


/******************************************************************
 * Constructs the Panel for the game that will display the pieces
 * that have been taken out of the game
 * @author Aaron Teague
 * I was helped by the sites www.stackoverflow.com, www.stackexchange.com,
 * and chessprogramming.wikispaces.com
 ********************************************************************/
public class GraveyardPanel extends JPanel{
	
	private static final long serialVersionUID = 1L;
	private final JPanel northPanel;
	private final JPanel southPanel;
	
	private static final Color PANEL_COLOR = Color.decode("0xFDF5E6");
	private static final Dimension TAKEN_PIECES_DIMENSION = new Dimension(40, 80);
	private static final EtchedBorder PANEL_BORDER = new EtchedBorder(EtchedBorder.RAISED); 
	
	public GraveyardPanel(){
		super(new BorderLayout());
		this.setBackground(PANEL_COLOR);
		this.setBorder(PANEL_BORDER);
		this.northPanel = new JPanel(new GridLayout(8, 2));
		this.southPanel = new JPanel(new GridLayout(8, 2));
		this.northPanel.setBackground(PANEL_COLOR);
		this.southPanel.setBackground(PANEL_COLOR);
		this.add(this.northPanel, BorderLayout.NORTH);
		this.add(this.southPanel, BorderLayout.SOUTH);
		setPreferredSize(TAKEN_PIECES_DIMENSION);
	}
	
	/*************************************************
	 * Prints the correct Piece images on the panel
	 * @param moveLog
	 ************************************************/
	public void redo(final MoveLog moveLog){
		
		this.southPanel.removeAll();
		this.northPanel.removeAll();
		
		final List<Piece> whiteTakenPieces = new ArrayList<>();
		final List<Piece> blackTakenPieces  = new ArrayList<>();
		
	
		//Loop through the moveLog to find all the taken pieces
		for(final Move move : moveLog.getMoves()){
			if(move.isAttack()){
				final Piece takenPiece = move.getAttackedPiece();
				if(takenPiece.getPieceTeam().isWhite()){
					whiteTakenPieces.add(takenPiece);
				}
				else{
					blackTakenPieces.add(takenPiece);
				}
			}
		}

		
		
		//Sort the white pieces by how powerful they are
		Collections.sort(whiteTakenPieces, new Comparator<Piece>(){

			@Override
			public int compare(Piece arg0, Piece arg1) {
			
				return Ints.compare(arg0.getPieceValue(), arg1.getPieceValue());
			}
			
		});
		
		//Sort the black pieces by how powerful they are
		Collections.sort(blackTakenPieces, new Comparator<Piece>(){

			@Override
			public int compare(Piece arg0, Piece arg1) {
			
				return Ints.compare(arg0.getPieceValue(), arg1.getPieceValue());
			}
			
		});
		
		//Prints the white pieces to the Panel
		for(final Piece takenPiece : whiteTakenPieces){
			try {
					final BufferedImage image = ImageIO.read(new File("ChessImages/" + 
							takenPiece.getPieceTeam().toString().substring(0, 1) + "" + takenPiece.toString() + ".gif"));
					final ImageIcon icon = new ImageIcon(image);
					final JLabel imageLabel = new JLabel(icon);
					this.southPanel.add(imageLabel);
			}
			catch(final IOException e){
				e.printStackTrace();
			}
			
		}

		//Prints the black pieces to the Panel
		for(final Piece takenPiece : blackTakenPieces){
			try {
				final BufferedImage image = ImageIO.read(new File("ChessImages/" + 
						takenPiece.getPieceTeam().toString().substring(0, 1) + "" + takenPiece.toString() + ".gif"));
				final ImageIcon icon = new ImageIcon(image);
				final JLabel imageLabel = new JLabel(icon);
				this.southPanel.add(imageLabel);
			}
			catch(final IOException e){
				e.printStackTrace();
			}
					
		}
		
		this.validate();
	}

}
