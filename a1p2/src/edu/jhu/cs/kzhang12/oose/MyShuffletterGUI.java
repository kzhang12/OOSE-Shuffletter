package edu.jhu.cs.kzhang12.oose;

import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

import edu.jhu.cs.oose.fall2014.shuffletter.iface.ShuffletterModel;
/**
 * GUI that sets model and gui
 * @author Kevin Zhang
 *
 */
public class MyShuffletterGUI {
	private static MyShuffletterFrame gui;
	private static ShuffletterModel model;
	private static BufferedImage image;
	/**
	 * Main method
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		model = new edu.jhu.cs.oose.fall2014.shuffletter.model.StandardShuffletterModel();
		//model = new MyShuffletterModelImpl();
		image = ImageIO.read(new File("Bananagrams_logo.jpg"));
		gui = new MyShuffletterFrame(model);
		gui.setIconImage(image);
		gui.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		gui.setLocationRelativeTo(null);
		gui.getPreferredSize();
		gui.setSize(gui.getPreferredSize());
		gui.setVisible(true);
	}
	
	/**
	 * restart the game
	 * @throws IOException 
	 */
	public static void restart() throws IOException
	{
		gui.dispose();
		model = new edu.jhu.cs.oose.fall2014.shuffletter.model.StandardShuffletterModel();
		image = ImageIO.read(new File("Bananagrams_logo.jpg"));
		gui = new MyShuffletterFrame(model);
		gui.setIconImage(image);
		gui.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		gui.setLocationRelativeTo(null);
		gui.getPreferredSize();
		gui.setSize(gui.getPreferredSize());
		gui.setVisible(true);
	}	
}