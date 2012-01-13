package ua.org.antidotcb.dominus.engine;

import android.graphics.Color;
import android.util.Log;

import java.util.ArrayList;
import java.util.Random;

public class StarSystem implements IPosition {

	private static short	StarSystemID	= 0;

	public StarSystem() {
		Log.i(Engine.eTag, this.getClass() + " created.");
		this.id = StarSystemID++;
		this.star = new Star();
		this.planets = new ArrayList<Planet>();
		Random rand = new Random();

		double t, w, z, y, x;

		z = (2.0 * rand.nextDouble()) - 1.0;
		t = (2.0 * Math.PI * rand.nextDouble());
		w = Math.sqrt(Math.abs(1.0 - (z * z)));
		x = w * Math.cos(t);
		y = w * Math.sin(t);

		Log.i(Engine.eTag, String.format("Coords generation: t=%f w=%f x=%f y=%f z=%f ", t, w, x, y, z));

		this.x = (float) x;
		this.y = (float) y;
		this.z = (float) z;

		color = Color.argb(255, rand.nextInt(128) + 128, rand.nextInt(128) + 128, rand.nextInt(128) + 128);
	}

	public Star getStar() {
		return star;
	}

	public final ArrayList<Planet> getPlanets() {
		return planets;
	}

	public float getX() {
		return x;
	}

	public float getY() {
		return y;
	}

	public float getZ() {
		return z;
	}

	public int getColor() {
		return color;
	}

	public short getId() {
		return id;
	}

	private Star				star;
	private ArrayList<Planet>	planets;
	private final float			x;
	private final float			y;
	private final float			z;
	private final int			color;
	private final short			id;

}
