
package ua.org.antidotcb.dominus.engine;


import java.util.ArrayList;


public abstract interface AbstractUniverseBuilder {

	public abstract Universe CreateUniverse();

	public abstract ArrayList<Race> CreateRaces();

	public abstract Race getPlayer();
}
