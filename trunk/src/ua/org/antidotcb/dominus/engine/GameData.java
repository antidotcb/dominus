package ua.org.antidotcb.dominus.engine;

import java.util.ArrayList;

public class GameData implements GameDataProvider {
	private ArrayList<Race>	races;
	private Universe		universe;

	public GameData() {}

	public ArrayList<Race> getRaces() {
		return races;
	}

	public void setRaces(ArrayList<Race> races) {
		this.races = races;
	}

	public Universe getUniverse() {
		return universe;
	}

	public void setUniverse(Universe universe) {
		this.universe = universe;
	}

	public void Save() {
		// TODO Auto-generated method stub
	}
}