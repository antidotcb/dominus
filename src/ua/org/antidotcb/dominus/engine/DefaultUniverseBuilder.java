
package ua.org.antidotcb.dominus.engine;


import java.util.ArrayList;
import java.util.Random;

import android.util.Log;


public class DefaultUniverseBuilder
	implements AbstractUniverseBuilder {

	public ArrayList<Race> CreateRaces() {
		ArrayList<Race> result = new ArrayList<Race>();
		result.add(this.getPlayer());
		return result;
	}

	public Universe CreateUniverse() {
		Universe result = new Universe();
		int starSystemsCount = 20 + rand.nextInt(10);
		Log.i(Engine.TAG, String.format("Builder decided to create %d", starSystemsCount));
		result.AddStarSystems(result, starSystemsCount);
		result.AddLinks(result);
		return result;
	}

	public Race getPlayer() {
		return new Race();
	}

	private Random	rand	= new Random();

}
