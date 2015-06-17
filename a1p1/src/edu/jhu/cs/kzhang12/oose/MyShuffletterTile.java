package edu.jhu.cs.kzhang12.oose;

import edu.jhu.cs.oose.fall2014.shuffletter.iface.ShuffletterTile;

public class MyShuffletterTile implements ShuffletterTile{
	private String letter;

	public MyShuffletterTile(String letter) {
		super();
		this.letter = letter;
	}

	@Override
	public char getLetter() {
		return letter.charAt(0);
	}

	@Override
	public boolean isWild() {
		if(letter.equals(" "))
		{
			return true;
		}
		return false;
	}
}
