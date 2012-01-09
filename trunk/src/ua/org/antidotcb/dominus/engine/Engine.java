package ua.org.antidotcb.dominus.engine;

import java.util.ArrayList;
import android.util.Log;

public final class Engine {
	public static final String eTag = "Dominus.Engine";
	
	private static final Engine uniqueInstance = new Engine();

	public static float getDistance(IPosition from, IPosition to) {
		float x1 = from.getX(),
			y1 = from.getY(),
			z1 = from.getZ(),
			x2 = to.getX(),
			y2 = to.getY(),
			z2 = to.getZ();
		
		float distance = (float) Math.sqrt (
			(x1 - x2) * (x1 - x2) +
			(y1 - y2) * (y1 - y2) +
			(z1 - z2) * (z1 - z2)
		);
		return distance;
	}

	static public Engine Instance() {
		return uniqueInstance;
	}

	private Engine() {
		Log.i(Engine.eTag, this.getClass().toString() + " created.");
	}
	
	public void CreateNew(AbstractUniverseBuilder builder) {
		Log.i(Engine.eTag, this.getClass() + " creating new universe using " + builder.getClass());
		data.setUniverse(builder.CreateUniverse());
		data.setRaces(builder.CreateRaces());
	}

	public ArrayList<Race> getRaces () {
		return data.getRaces();
	}

	public Universe getUniverse() {
		return data.getUniverse();
	}

	private GameData data = new GameData();

}
