package package1;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EtchedBorder;

import package1.Table.MoveLog;

/*******************************************************************************
* The GraveYardPanel main purpose is to keep track of pieces that been taken
* out of the game.
* @author Aaron Teague
* I was helped by stackoverflow.com and chessprogramming.wikispaces.com/
*******************************************************************************/
public class GraveYardPanel extends JPanel {
			
	
			
			private JPanel northPanel;
			private JPanel southPanel;
			
			private static EtchedBorder BORDER = new EtchedBorder(EtchedBorder.RAISED);   
			
			public GraveYardPanel(){
				
				super(new BorderLayout());
				this.setBackground(Color.decode("#7F7F7F"));
				this.setBorder(BORDER);
				this.northPanel = new JPanel(new GridLayout(8, 2));
				this.southPanel = new JPanel(new GridLayout(8, 2));
				this.northPanel.setBackground(Color.decode("#7F7F7F"));
				this.southPanel.setBackground(Color.decode("#7F7F7F"));
				this.add(this.northPanel, BorderLayout.NORTH);
				this.add(this.southPanel, BorderLayout.SOUTH);
				setPreferredSize(new Dimension(40, 80));
				
			}
			
			/************************************************************************
			 * Searches over the log all moves on the board and checks for attacks.
			 * If an attack is found the piece that was attacked is added to the
			 * list of dead pieces. Finally all dead pieces are assigned correct
			 * pictures.
			 * @param movelog List of all moves on the board
			 ************************************************************************/
			public void redo(MoveLog movelog){
				
				//sets up the panels for updates
				this.southPanel.removeAll();
				this.northPanel.removeAll();
				
				//creates a list to hold the dead white pieces
				List<Piece> whiteDeadPieces = new ArrayList<>();
				//creates a list to hold the dead black pieces
				List<Piece> blackDeadPieces = new ArrayList<>();
				
				//loops through the move log to find attack moves
				for(Move move : movelog.getMoves()){
					if(move.isAttack()) {
						Piece deadPiece = move.getAttackedPiece();
						
						if(deadPiece.getTeam().White()){
							whiteDeadPieces.add(deadPiece);
						}
						else {
							blackDeadPieces.add(deadPiece);
						}
					}	
				}
				
				//Assigns the correct picture to each dead white piece
				for(Piece deadPiece : whiteDeadPieces) {
					
						try {
							BufferedImage pic = ImageIO.read(new File("ChessImages/" + 
							                      deadPiece.getTeam().toString().substring(0, 1) + "" + deadPiece.toString() + ".gif"));
							ImageIcon icon = new ImageIcon(pic);
							JLabel picLabel = new JLabel(icon);
							this.southPanel.add(picLabel);
							
						} catch (IOException e) {
						
							e.printStackTrace();
						}
						
				}
				
				//Assigns the correct picture to each dead black piece
				for(Piece deadPiece : blackDeadPieces) {
					
					try {
						BufferedImage pic = ImageIO.read(new File("ChessImages/" + 
						                      deadPiece.getTeam().toString().substring(0, 1) + "" + deadPiece.toString() + ".gif"));
						ImageIcon icon = new ImageIcon(pic);
						JLabel picLabel = new JLabel(icon);
						this.southPanel.add(picLabel);
						
					}   catch (IOException e) {
					
						e.printStackTrace();
					}
					
			    }
				
				validate();
			}
		}
