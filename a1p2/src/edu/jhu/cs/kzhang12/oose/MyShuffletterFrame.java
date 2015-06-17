package edu.jhu.cs.kzhang12.oose;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTextPane;

import edu.jhu.cs.oose.fall2014.shuffletter.iface.NoOpShuffletterModelListener;
import edu.jhu.cs.oose.fall2014.shuffletter.iface.Position;
import edu.jhu.cs.oose.fall2014.shuffletter.iface.ShuffletterIllegalMoveEvent;
import edu.jhu.cs.oose.fall2014.shuffletter.iface.ShuffletterModel;
import edu.jhu.cs.oose.fall2014.shuffletter.iface.ShuffletterTile;
import edu.jhu.cs.oose.fall2014.shuffletter.iface.ShuffletterModelListener;
import edu.jhu.cs.oose.fall2014.shuffletter.iface.ShuffletterTileMovedEvent;
import edu.jhu.cs.oose.fall2014.shuffletter.iface.ShuffletterTilePlayedEvent;
/**
 * Frame that contains grid and supply
 * @author Kevin Zhang
 *
 */
public class MyShuffletterFrame extends JFrame{
	public static final int SUPPLY_ROW_SIZE = 11;
	public static final int SUPPLY_COL_SIZE = 2;
	public static final int RECT_SIZE = 50;
	public static final int FONT_SIZE = 30;
	private ShuffletterModel model;
	private JTextArea area;
	public static boolean supplyCellSelected = false;
	public static boolean gridCellSelected = false;
	private ShuffletterTile currentLetter;
	private JLabel label;
	private ShuffletterTile[][] supplyTiles = new ShuffletterTile[11][2];
	private int supplyRow;
	private int supplyCol;
	private int gridRow;
	private int gridCol;

	/**
	 * Constructor for MyShuffletterFrame
	 * @param model to be passed in
	 */
	public MyShuffletterFrame(final ShuffletterModel model) {
		this.setTitle("Shuffletter!");
		
		this.model = model;

		final GameplayGridComponent gameplayGrid = new GameplayGridComponent(model);
		final JTable table = new JTable(SUPPLY_ROW_SIZE, SUPPLY_COL_SIZE){  
		       public boolean isCellEditable(int row,int column){  
		         return false;
		       }
		};
		table.setCellSelectionEnabled(true);
		table.setRowHeight(RECT_SIZE);
		table.setBackground(Color.GREEN);
		table.setForeground(Color.BLACK);
		table.setSelectionBackground(Color.RED);
		table.setFont(new Font("serif", Font.CENTER_BASELINE, FONT_SIZE));
		JButton button = new JButton("End Round");
		JButton button2 = new JButton("New Game");
		label = new JLabel();
		label.setText(model.getBagCount() + " tiles left");
		final JTextField field = new JTextField(FONT_SIZE);
		JScrollPane scrollPane = new JScrollPane(gameplayGrid);

		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new BorderLayout());
		buttonPanel.add(button, BorderLayout.CENTER);
		buttonPanel.add(button2, BorderLayout.EAST);

		JPanel rightPanel = new JPanel();
		rightPanel.setLayout(new BorderLayout());
		rightPanel.add(table, BorderLayout.CENTER);
		rightPanel.add(buttonPanel, BorderLayout.SOUTH);
		JScrollPane scrollPane2 = new JScrollPane(rightPanel);

		this.getContentPane().setLayout(new BorderLayout());
		this.getContentPane().add(scrollPane2, BorderLayout.EAST);
		this.getContentPane().add(scrollPane, BorderLayout.CENTER);
		this.getContentPane().add(label, BorderLayout.SOUTH);
		
		List<ShuffletterTile> SupplyContents = model.getSupplyContents();
		for (int i = 0; i < SUPPLY_ROW_SIZE; i++)
		{
			if(SupplyContents.size() > i)
			{
				ShuffletterTile letter = SupplyContents.get(i);
				table.setValueAt(letter.getLetter(), i, 0);
				supplyTiles[i][0] = letter;
			}
			else
			{
				table.setValueAt(" ", i, 0);
			}
		}
		for (int i = 0; i < 11; i++)
		{
			if(SupplyContents.size() > (i + SUPPLY_ROW_SIZE))
			{
				ShuffletterTile letter = SupplyContents.get(i + SUPPLY_ROW_SIZE);
				table.setValueAt(letter.getLetter(), i, 1);
				supplyTiles[i][1] = letter;
			}
			else
			{
				table.setValueAt(" ", i, 1);
			}
		}
		
		table.addMouseListener(new MouseAdapter() {

			@Override
			public void mousePressed(MouseEvent e) {
				supplyRow = table.rowAtPoint(e.getPoint());
				supplyCol = table.columnAtPoint(e.getPoint());
				Position p = new Position(supplyRow, -supplyCol);
				currentLetter = supplyTiles[supplyRow][supplyCol];
				if (!table.getValueAt(supplyRow, supplyCol).equals(" "))
				{
					supplyCellSelected = true;					
				}
			}
		});
		
		gameplayGrid.addMouseListener(new MouseAdapter(){

			@Override
			public void mousePressed(MouseEvent e) {
				int row = (e.getX() - e.getX()%RECT_SIZE)/RECT_SIZE;
				int col = (e.getY() - e.getY()%RECT_SIZE)/RECT_SIZE;
				Position p = new Position(row, -col);
				if (supplyCellSelected)
				{
					//table.setValueAt(" ", supplyRow, supplyCol);
					model.play(currentLetter, p);
					//gameplayGrid.checkBorders(p);
					//model.getSupplyContents();
					supplyCellSelected = false;
				}
				else if(!gridCellSelected)
				{
					gridRow = row;
					gridCol = col;
					if (model.getTilePositions().contains(new Position(row, col)))
					{
						gridCellSelected = true;						
					}
				}
				else
				{
					Position p2 = new Position(gridRow, -gridCol);
					model.move(p2, p);
					//gameplayGrid.checkBorders(p);
				}
			}
		});
		
		button.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				model.isGameOver();
				model.endRound();
			}
		});
		
		button2.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				try {
					MyShuffletterGUI.restart();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			
		});
		
		model.addListener(new NoOpShuffletterModelListener() {
			public void illegalMoveMade(ShuffletterIllegalMoveEvent e) {
				label.setText(e.getMessage());
			}
			
			public void tilePlayed(ShuffletterTilePlayedEvent e) {
				table.setValueAt(" ", supplyRow, supplyCol);
				gameplayGrid.checkBorders(e.getTarget());
				model.getSupplyContents();
				currentLetter = null;
				gameplayGrid.revalidate();
				gameplayGrid.repaint();
			}
			
			public void tileMoved(ShuffletterTileMovedEvent e) {
				gameplayGrid.checkBorders(e.getTarget());
				gridCellSelected = false;
				gameplayGrid.revalidate();
				gameplayGrid.repaint();
			}
			
			public void roundEnded() {
				List<ShuffletterTile> SupplyContents = model.getSupplyContents();
				ShuffletterTile letter = SupplyContents.get(0);
				table.setValueAt(letter.getLetter(), 0, 0);
				supplyTiles[0][0] = letter;
				label.setText(model.getBagCount() + " tiles left");
			}
			
			public void gameEnded() {
				JTextField finished = new JTextField("Congratulations, you're finished!");
				finished.setEditable(false);
				final JFrame parent = new JFrame();
				parent.add(finished);
				parent.pack();
				parent.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
				parent.setLocationRelativeTo(null);
		        parent.setVisible(true);
		        }
		});
		
		this.pack();
	}
}
