package edu.jhu.cs.kzhang12.oose;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Random;
import java.util.Set;

import edu.jhu.cs.oose.fall2014.shuffletter.iface.Position;
import edu.jhu.cs.oose.fall2014.shuffletter.iface.ShuffletterIllegalMoveEvent;
import edu.jhu.cs.oose.fall2014.shuffletter.iface.ShuffletterModel;
import edu.jhu.cs.oose.fall2014.shuffletter.iface.ShuffletterModelListener;
import edu.jhu.cs.oose.fall2014.shuffletter.iface.ShuffletterTile;
import edu.jhu.cs.oose.fall2014.shuffletter.iface.ShuffletterTileMovedEvent;
import edu.jhu.cs.oose.fall2014.shuffletter.iface.ShuffletterTilePlayedEvent;

public class MyShuffletterModelImpl implements ShuffletterModel{

	private List<ShuffletterModelListener> listeners;
	private String authorName;
	private List<ShuffletterTile> bagTiles;
	private List<ShuffletterTile> supplyTiles;
	private Map<Position, ShuffletterTile> boardTiles;
	private Set<String> dictionary;
	private List<String> wildTiles;
	private boolean firstWild;

	public MyShuffletterModelImpl() throws IOException {
		super();
		this.listeners = new ArrayList<>();
		this.authorName = "Kevin Zhang";
		this.bagTiles = new ArrayList<>();
		this.supplyTiles = new ArrayList<>();
		this.boardTiles = new HashMap<Position, ShuffletterTile>();
		this.dictionary = new HashSet<String>();
		Random rand = new Random();
		this.wildTiles = new ArrayList<>();
		this.firstWild = true;
		
		BufferedReader br = new BufferedReader(new FileReader("wordlist.txt"));
		String line;
		while ((line = br.readLine()) != null)
		{
			dictionary.add(line);
		}
		br.close();
		/*
		for(int i = 0; i < 18; i++)
		{
			if(i < 1)
			{
				ShuffletterTile wild = new MyShuffletterTile(" ");
				bagTiles.add(wild);
			}
			if(i < 2)
			{
				ShuffletterTile J = new MyShuffletterTile("J");
				bagTiles.add(J);
				ShuffletterTile K = new MyShuffletterTile("K");
				bagTiles.add(K);
				ShuffletterTile Q = new MyShuffletterTile("Q");
				bagTiles.add(Q);
				ShuffletterTile X = new MyShuffletterTile("X");
				bagTiles.add(X);
				ShuffletterTile Z = new MyShuffletterTile("Z");
				bagTiles.add(Z);
			}
			if(i < 3)
			{
				ShuffletterTile B = new MyShuffletterTile("B");
				bagTiles.add(B);
				ShuffletterTile C = new MyShuffletterTile("C");
				bagTiles.add(C);
				ShuffletterTile F = new MyShuffletterTile("F");
				bagTiles.add(F);
				ShuffletterTile H = new MyShuffletterTile("H");
				bagTiles.add(H);
				ShuffletterTile M = new MyShuffletterTile("M");
				bagTiles.add(M);
				ShuffletterTile P = new MyShuffletterTile("P");
				bagTiles.add(P);
				ShuffletterTile V = new MyShuffletterTile("V");
				bagTiles.add(V);
				ShuffletterTile W = new MyShuffletterTile("W");
				bagTiles.add(W);
				ShuffletterTile Y = new MyShuffletterTile("Y");
				bagTiles.add(Y);
			}
			if(i < 4)
			{
				ShuffletterTile G = new MyShuffletterTile("G");
				bagTiles.add(G);
			}
			if(i < 5)
			{
				ShuffletterTile L = new MyShuffletterTile("L");
				bagTiles.add(L);
			}
			if(i < 6)
			{
				ShuffletterTile D = new MyShuffletterTile("D");
				bagTiles.add(D);
				ShuffletterTile S = new MyShuffletterTile("S");
				bagTiles.add(S);
				ShuffletterTile U = new MyShuffletterTile("U");
				bagTiles.add(U);
			}
			if(i < 8)
			{
				ShuffletterTile N = new MyShuffletterTile("N");
				bagTiles.add(N);
			}
			if(i < 9)
			{
				ShuffletterTile T = new MyShuffletterTile("T");
				bagTiles.add(T);
				ShuffletterTile R = new MyShuffletterTile("R");
				bagTiles.add(R);
			}
			if(i < 11)
			{
				ShuffletterTile O = new MyShuffletterTile("O");
				bagTiles.add(O);
			}
			if(i < 12)
			{
				ShuffletterTile I = new MyShuffletterTile("I");
				bagTiles.add(I);
			}
			if(i < 13)
			{
				ShuffletterTile A = new MyShuffletterTile("A");
				bagTiles.add(A);
			}
			if(i < 18)
			{
				ShuffletterTile E = new MyShuffletterTile("E");
				bagTiles.add(E);
			}
		}
		for (int i = 0; i < 21; i++)
		{
			int n = rand.nextInt(bagTiles.size());
			ShuffletterTile tile = bagTiles.get(n);
			bagTiles.remove(n);
			supplyTiles.add(tile);
		}
		*/
		ShuffletterTile wild1 = new MyShuffletterTile("M");
		supplyTiles.add(wild1);
		ShuffletterTile wild2 = new MyShuffletterTile("O");
		supplyTiles.add(wild2);
		ShuffletterTile wild3 = new MyShuffletterTile("N");
		supplyTiles.add(wild3);
		ShuffletterTile wild4 = new MyShuffletterTile("A");
		bagTiles.add(wild4);
	}

	@Override
	public void addListener(ShuffletterModelListener listener) {
		this.listeners.add(listener);		
	}

	@Override
	public void endRound() {
		//check there are no tiles remaining in the supply
		if (supplyTiles.isEmpty())
		{
			//check all tiles connected orthogonally
			Queue<Position> bfs = new LinkedList<Position>();
			Set<Position> countedTiles = new HashSet<Position>();
			Set<Position> keys = boardTiles.keySet();
			Iterator<Position> itr = keys.iterator();
			Position p = itr.next();
			countedTiles.add(p);
			bfs.add(p);
			while (!bfs.isEmpty())
			{
				Position temp = bfs.remove();
				int x = temp.getX();
				int y = temp.getY();
				Position left = new Position(x-1, y);
				Position top = new Position(x, y+1);
				Position right = new Position(x+1, y);
				Position bottom = new Position(x, y-1);
				if (boardTiles.containsKey(left) && !countedTiles.contains(left))
				{
					bfs.add(left);
					countedTiles.add(left);
				}
				if (boardTiles.containsKey(top) && !countedTiles.contains(top))
				{
					bfs.add(top);
					countedTiles.add(top);
				}
				if (boardTiles.containsKey(right) && !countedTiles.contains(right))
				{
					bfs.add(right);
					countedTiles.add(right);
				}
				if (boardTiles.containsKey(bottom) && !countedTiles.contains(bottom))
				{
					bfs.add(bottom);
					countedTiles.add(bottom);
				}
			}
			if (countedTiles.size() == boardTiles.size())
			{
				//check all words are valid
				boolean horizontal = checkHorizontalWords();
				boolean vertical = checkVerticalWords();
				if (horizontal && vertical)
				{
					//check game over
					if (!this.isGameOver())
					{
						Random rand = new Random();
						int n = rand.nextInt(bagTiles.size());
						ShuffletterTile tile = bagTiles.get(n);
						bagTiles.remove(n);
						supplyTiles.add(tile);
						for (ShuffletterModelListener listener : this.listeners)
						{
							listener.roundEnded();
						}						
					}
					else
					{
						for (ShuffletterModelListener listener : this.listeners)
						{
							listener.gameEnded();
						}						
					}
				}
				else
				{
					ShuffletterIllegalMoveEvent illMove= new ShuffletterIllegalMoveEvent("Not all words are spelled correctly.");
					for (ShuffletterModelListener listener : this.listeners)
					{
						listener.illegalMoveMade(illMove);
					}

				}
			}
			else
			{
				ShuffletterIllegalMoveEvent illMove= new ShuffletterIllegalMoveEvent("Not all tiles are connected.");
				for (ShuffletterModelListener listener : this.listeners)
				{
					listener.illegalMoveMade(illMove);
				}
			}
		}
		else
		{
			ShuffletterIllegalMoveEvent illMove= new ShuffletterIllegalMoveEvent("There are still tiles in the supply.");
			for (ShuffletterModelListener listener : this.listeners)
			{
				listener.illegalMoveMade(illMove);
			}
		}
	}

	@Override
	public String getAuthorName() {
		return authorName;
	}

	@Override
	public int getBagCount() {
		return bagTiles.size();
	}

	@Override
	public Set<String> getLegalWords() {
		return dictionary;
	}

	@Override
	public List<ShuffletterTile> getSupplyContents() {
		return supplyTiles;
	}

	@Override
	public ShuffletterTile getTile(Position p) {
		return boardTiles.get(p);
	}

	@Override
	public Collection<Position> getTilePositions() {
		return boardTiles.keySet();
	}

	@Override
	public boolean isGameOver() {
		if(bagTiles.isEmpty() && supplyTiles.isEmpty())
		{
			return true;
		}
		return false;
	}

	@Override
	public void move(Position source, Position target) throws IllegalArgumentException {
		if (boardTiles.containsKey(source))
		{
			ShuffletterTile tile = boardTiles.get(source);
			ShuffletterTileMovedEvent moved = new ShuffletterTileMovedEvent(source, target, tile);
			if (boardTiles.containsKey(target))
			{
				ShuffletterTile temp = boardTiles.get(source);
				boardTiles.remove(source);
				boardTiles.remove(target);
				boardTiles.put(source, temp);
				boardTiles.put(target, tile);
			}
			else
			{
				boardTiles.remove(source);
				boardTiles.put(target, tile);
			}			
			for (ShuffletterModelListener listener : this.listeners)
			{
				listener.tileMoved(moved);
			}
		}
		else
		{
			ShuffletterIllegalMoveEvent illMove= new ShuffletterIllegalMoveEvent("This position does not contain a tile.");
			for (ShuffletterModelListener listener : this.listeners)
			{
				listener.illegalMoveMade(illMove);
			}
		}
	}

	@Override
	public void play(ShuffletterTile tile, Position target) throws IllegalArgumentException {
		ShuffletterTilePlayedEvent played = new ShuffletterTilePlayedEvent(target, tile);
		if (boardTiles.containsKey(target))
		{			
			ShuffletterIllegalMoveEvent illPlay= new ShuffletterIllegalMoveEvent("This tile does not exist in supply.");
			for (ShuffletterModelListener listener : this.listeners)
			{
				listener.illegalMoveMade(illPlay);
			}
		}
		else
		{
			supplyTiles.remove(tile);
			boardTiles.put(target, tile);
			for (ShuffletterModelListener listener : this.listeners)
			{
				listener.tilePlayed(played);
			}
		}
	}

	@Override
	public void removeListener(ShuffletterModelListener listener) {
		this.listeners.remove(listener);
	}
	
	/**
	 * 
	 * @return whether or not all the horizontal words are valid.
	 */
	private boolean checkHorizontalWords()
	{
		int top = -100;
		int bottom = 100;
		int leftmost = 100;
		Set<Position> keys = boardTiles.keySet();
		Iterator<Position> itr = keys.iterator();
		//find upmost tile
		while (itr.hasNext())
		{
			Position temp = itr.next();
			int y = temp.getY();
			if (y > top)
			{
				top = y;
			}
			if (y < bottom)
			{
				bottom = y;
			}
		}
		//loop from highest tile to lowest tile
		Iterator<Position> itr2 = keys.iterator();
		for (int i = top; i >= bottom; i--)
		{
			//find leftmost tile
			while(itr2.hasNext())
			{
				Position temp = itr2.next();
				int x = temp.getX();
				if (temp.getY() == i && x < leftmost)
				{
					leftmost = x;
				}
			}
			Position check = new Position(leftmost, i);
			int xpos = check.getX();
			ArrayList<String> word = new ArrayList<>();
			Position test = new Position(xpos, i);
			boolean wild = false;
			while (boardTiles.containsKey(test))
			{
				ShuffletterTile tile = boardTiles.get(test);
				String letter = "" + tile.getLetter();
				if(letter.equals(" "))
				{
					wild = true;
				}
				word.add(letter);
				xpos++;
				test = new Position(xpos, i);
			}
			String listString = "";
			for (String s : word)
			{
				listString += s;
			}
			if(!wild)
			{
				boolean found = false;			
				Iterator<String> lib = dictionary.iterator();
				while (lib.hasNext())
				{
					String dic = lib.next();
					if (dic.equalsIgnoreCase(listString))
					{
						found = true;
					}
				}
				if (!found)
				{
					return false;
				}
			}
			else
			{
				if(!this.checkWildWords(listString))
				{
					return false;
				}
			}
		}
		return true;
	}
	
	/**
	 *
	 * @return whether or not all the vertical words are valid.
	 */
	private boolean checkVerticalWords()
	{
		int right = -100;
		int left = 100;
		int upmost = -100;
		Set<Position> keys = boardTiles.keySet();
		Iterator<Position> itr = keys.iterator();
		//find upmost tile
		while (itr.hasNext())
		{
			Position temp = itr.next();
			int x = temp.getX();
			if (x > right)
			{
				right = x;
			}
			if (x < left)
			{
				left = x;
			}
		}
		//loop from highest tile to lowest tile
		Iterator<Position> itr2 = keys.iterator();
		for (int i = right; i >= left; i--)
		{
			//find leftmost tile
			while(itr2.hasNext())
			{
				Position temp = itr2.next();
				int y = temp.getY();
				if (temp.getX() == i && y > upmost)
				{
					upmost = y;
				}
			}
			Position check = new Position(i, upmost);
			int ypos = check.getY();
			ArrayList<String> word = new ArrayList<>();
			Position test = new Position(i, ypos);
			boolean wild = false;
			while (boardTiles.containsKey(test))
			{
				ShuffletterTile tile = boardTiles.get(test);
				String letter = "" + tile.getLetter();
				if(letter.equals(" "))
				{
					wild = true;
				}
				word.add(letter);
				ypos++;
				test = new Position(i, ypos);
			}
			String listString = "";
			for (String s : word)
			{
				listString += s;
			}
			if(!wild)
			{
				boolean found = false;			
				Iterator<String> lib = dictionary.iterator();
				while (lib.hasNext())
				{
					String dic = lib.next();
					if (dic.equalsIgnoreCase(listString))
					{
						found = true;
					}
				}
				if (!found)
				{
					return false;
				}
			}
			else
			{
				if(!this.checkWildWords(listString))
				{
					return false;
				}
			}
		}
		return true;
	}
	
	/**
	 * 
	 * @param word to be tested
	 * @return whether or not a word with a wild tile is valid.
	 */
	private boolean checkWildWords(String word)
	{
		boolean valid = false;
		int wild = word.indexOf(" ");
		int wordLength = word.length();
		String front = word.substring(0,wild);
		String back = word.substring(wild+1);
		String mid = word.substring(wild, wild+1);
		Iterator<String> lib = dictionary.iterator();
		while (lib.hasNext())
		{
			String match = lib.next();
			if (match.length() == wordLength)
			{
				String matchFront = match.substring(0, wild);
				String matchBack = match.substring(wild+1);
				if (matchFront.equalsIgnoreCase(front) && matchBack.equalsIgnoreCase(back))
				{
					if (firstWild)
					{
						wildTiles.add(mid);
						System.out.println("got here");
						valid = true;
					}
					else
					{
						for (int i = 0; i < wildTiles.size(); i++)
						{
							String option = wildTiles.get(i);
							if (option.equalsIgnoreCase(mid))
							{
								valid = true;
							}
						}
					}
				}
			}
		}
		firstWild = false;
		return valid;
	}
}