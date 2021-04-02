package pwcg.campaign;

import java.util.ArrayList;
import java.util.List;

public class Battles
{
	private List<Battle> battles = new ArrayList<>();

	public List<Battle> getBattles()
	{
		return battles;
	}

	public void addBattle(Battle battle)
	{
		battles.add(battle);
	}
}
