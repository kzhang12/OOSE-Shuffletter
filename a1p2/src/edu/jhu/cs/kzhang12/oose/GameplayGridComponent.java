package edu.jhu.cs.kzhang12.oose;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import javax.swing.JComponent;

import edu.jhu.cs.oose.fall2014.shuffletter.iface.ShuffletterIllegalMoveEvent;
import edu.jhu.cs.oose.fall2014.shuffletter.iface.ShuffletterModel;
import edu.jhu.cs.oose.fall2014.shuffletter.iface.ShuffletterModelListener;
import edu.jhu.cs.oose.fall2014.shuffletter.iface.ShuffletterTile;
import edu.jhu.cs.oose.fall2014.shuffletter.iface.ShuffletterTileMovedEvent;
import edu.jhu.cs.oose.fall2014.shuffletter.iface.ShuffletterTilePlayedEvent;
import edu.jhu.cs.oose.fall2014.shuffletter.iface.Position;
/**
 * Custom grid component
 * @author Kevin Zhang
 *
 */
public class GameplayGridComponent extends JComponent{
	public static final int RECT_SIZE = 50;
	public static final int FONT_SIZE = 30;
	public static final int X_ADJUST = 10;
	public static final int Y_ADJUST = 40;
	public int row = 10;
	public int col = 10;
	public int gridRowSize = 500;
	public int gridColSize = 500;
	private ShuffletterModel model;
	private Graphics g = null;
	
	/**
	 * Constructor for the gameplay grid
	 * @param model to be passed in
	 */
	public GameplayGridComponent(ShuffletterModel model) {
		super();
		this.model = model;
	}

	@Override
	public Dimension getPreferredSize() {
		//change grid size
		return new Dimension(gridRowSize, gridColSize);
	}

	@Override
	public void paint(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		this.g = g;
		for (int i = 0; i < row; i++)
		{
			for (int j = 0; j < col; j++)
			{
				g2.drawRect(RECT_SIZE*i, RECT_SIZE*j, RECT_SIZE, RECT_SIZE);
				g2.setColor(Color.BLACK);
			}
		}
		Collection<Position> tilePositions = model.getTilePositions();
		Iterator<Position> itr = tilePositions.iterator();
		
		while(itr.hasNext())
		{
			Position p = itr.next();
			int x = p.getX();
			int y = p.getY();
			Position p2 = new Position (x, -y);
			ShuffletterTile tile = model.getTile(p2);
			int x2 = p2.getX();
			int y2 = p2.getY();
			g2.setColor(Color.GREEN);
			g2.fillRect(x2*RECT_SIZE, y2*RECT_SIZE, RECT_SIZE, RECT_SIZE);
			g2.setColor(Color.BLACK);
			Font f = new Font("serif", Font.CENTER_BASELINE, FONT_SIZE);
			g2.setFont(f);
			g2.drawString(Character.toString(tile.getLetter()), x2*RECT_SIZE + X_ADJUST, y2*RECT_SIZE + Y_ADJUST);				
		}
	}
	
	/**
	 * Check if tile is played/moved to the edge of the grid and extend grid
	 * @param p of tile at the edge
	 */
	public void checkBorders(Position p)
	{
		if (p.getX() == (row - 1))
		{
			row += 1;
			gridRowSize += 50;
		}
		if (p.getY() == (col - 1))
		{
			col += 1;
			gridColSize += 50;
		}
		if (p.getX() == 0)
		{
			Collection<Position> tilePositions = model.getTilePositions();
			Iterator<Position> itr = tilePositions.iterator();
			Collection<Position> tempPositions = new ArrayList<Position>();
			Iterator<Position> itr2 = tempPositions.iterator();
			while (itr.hasNext())
			{
				Position p2 = itr.next();
				tempPositions.add(p2);
				int x = p.getX();
				int y = p.getY();
				//model.move(p2, new Position(x + 1, y));
			}
		}
		if (p.getY() == 0)
		{
			Collection<Position> tilePositions = model.getTilePositions();
			Iterator<Position> itr = tilePositions.iterator();
			Collection<Position> tempPositions = new ArrayList<Position>();
			Iterator<Position> itr2 = tempPositions.iterator();
			while (itr.hasNext())
			{
				Position p2 = itr.next();
				tempPositions.add(p2);
				int x = p.getX();
				int y = p.getY();
				Position p3 = new Position(x, y + 1);
				//model.move(p2, p3);
			}
		}
		revalidate();
		repaint();
	}
}
