
package ua.org.antidotcb.dominus.engine;


import java.util.ArrayList;
import java.util.Random;

import android.util.Log;
import android.util.Pair;


public class Universe {

	public Universe() {
		Log.i(Engine.TAG, this.getClass() + " created.");

		starSystems = new ArrayList<StarSystem>();
	}

	public void AddStarSystems(Universe result, int count) {
		starSystems.ensureCapacity(count);

		while (count-- > 0) {
			starSystems.add(new StarSystem());
		}
	}

	public void AddLinks(Universe result) {
		ArrayList<StarSystem> unlinked = new ArrayList<StarSystem>(starSystems);
		ArrayList<StarSystem> linked = new ArrayList<StarSystem>(starSystems.size());

		Random rand = new Random();

		int randomLinksCount;
		if (starSystems.size() > 4) {
			randomLinksCount = rand.nextInt(starSystems.size() / 4) + starSystems.size() / 10;
		} else randomLinksCount = 0;

		Log.i(Engine.TAG, String.format("Additional links to be created: %d", randomLinksCount));

		links = new ArrayList<Pair<StarSystem, StarSystem>>(unlinked.size() + 1 + randomLinksCount);

		do {
			StarSystem linkFirstStarSystem = null;
			StarSystem linkSecondStarSystem = null;

			if (unlinked.size() > 0) {
				int linkFirstId = rand.nextInt(unlinked.size());
				linkFirstStarSystem = unlinked.get(linkFirstId);
			} else {
				int linkFirstId = rand.nextInt(linked.size());
				linkFirstStarSystem = linked.get(linkFirstId);
				randomLinksCount--;
			}

			if (linked.size() > 0) {
				int linkSecondId = getClosestIndex(linked, linkFirstStarSystem);
				linkSecondStarSystem = linked.get(linkSecondId);
			} else {
				do {
					int linkSecondId = rand.nextInt(starSystems.size());
					linkSecondStarSystem = starSystems.get(linkSecondId);
				}
				while (linkSecondStarSystem.getId() == linkFirstStarSystem.getId());
			}

			Pair<StarSystem, StarSystem> link = new Pair<StarSystem, StarSystem>(linkFirstStarSystem, linkSecondStarSystem);
			if (unlinked.contains(linkFirstStarSystem)) {
				unlinked.remove(linkFirstStarSystem);
			}

			if (unlinked.contains(linkSecondStarSystem)) {
				unlinked.remove(linkSecondStarSystem);
			}

			if (!linked.contains(linkFirstStarSystem)) {
				linked.add(linkFirstStarSystem);
			}

			if (!linked.contains(linkSecondStarSystem)) {
				linked.add(linkSecondStarSystem);
			}

			links.add(link);
			Log.i(Engine.TAG, String.format("Link between %d and %d created", link.first.getId(), link.second.getId()));
		}
		while ((unlinked.size() > 0) || (randomLinksCount > 0));
	}

	private int getClosestIndex(ArrayList<StarSystem> starSystems, StarSystem from) {
		float minDistance = Float.MAX_VALUE;
		int index = 0, result = -1;
		for (StarSystem star : starSystems) {

			if (star == from) continue;

			float currentDistance = Engine.getDistance(from, star);

			if (currentDistance <= minDistance) {
				result = index;
				minDistance = currentDistance;
			}

			index++;
		}
		return result;
	}

	public final ArrayList<StarSystem> getStarSystems() {
		return (starSystems);
	}

	public final ArrayList<Pair<StarSystem, StarSystem>> getLinks() {
		return links;
	}

	private ArrayList<StarSystem>					starSystems;
	private ArrayList<Pair<StarSystem, StarSystem>>	links;

}
