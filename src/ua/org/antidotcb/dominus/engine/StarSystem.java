
package ua.org.antidotcb.dominus.engine;


import java.util.ArrayList;
import java.util.Random;

import android.graphics.Color;
import android.util.Log;


public class StarSystem
	implements IPosition {

	private static short	StarSystemID	= 0;

	public StarSystem() {
		Log.i(Engine.TAG, this.getClass() + " created.");
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

		Log.i(Engine.TAG, String.format("Coords generation: t=%f w=%f x=%f y=%f z=%f ", t, w, x, y, z));

		this.x = (float) x;
		this.y = (float) y;
		this.z = (float) z;

		color = Color.argb(255, rand.nextInt(128) + 128, rand.nextInt(128) + 128, rand.nextInt(128) + 128);
	}

	/** @return
	 * @uml.property name="star" */
	public Star getStar() {
		return star;
	}

	public final ArrayList<Planet> getPlanets() {
		return planets;
	}

	/** @return
	 * @uml.property name="x" */
	public float getX() {
		return x;
	}

	/** @return
	 * @uml.property name="y" */
	public float getY() {
		return y;
	}

	/** @return
	 * @uml.property name="z" */
	public float getZ() {
		return z;
	}

	/** @return
	 * @uml.property name="color" */
	public int getColor() {
		return color;
	}

	/** @return
	 * @uml.property name="id" */
	public short getId() {
		return id;
	}

	/** @uml.property name="star"
	 * @uml.associationEnd multiplicity="(1 1)" */
	private Star				star;
	/** @uml.property name="planets" */
	private ArrayList<Planet>	planets;
	/** @uml.property name="x" */
	private final float			x;
	/** @uml.property name="y" */
	private final float			y;
	/** @uml.property name="z" */
	private final float			z;
	/** @uml.property name="color" */
	private final int			color;
	/** @uml.property name="id" */
	private final short			id;

}
