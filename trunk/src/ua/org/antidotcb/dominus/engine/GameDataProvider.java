package ua.org.antidotcb.dominus.engine;

import java.util.ArrayList;

public interface GameDataProvider {
	public ArrayList<Race> getRaces();
	public Universe getUniverse();
	
	public void Save();
}
