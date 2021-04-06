package pwcg.campaign.skirmish;

import java.util.ArrayList;
import java.util.List;

public class Skirmishes
{
	private List<Skirmish> skirmishes = new ArrayList<>();

	public List<Skirmish> getSkirmishes()
	{
		return skirmishes;
	}

	public void addSkirmish(Skirmish Skirmish)
	{
		skirmishes.add(Skirmish);
	}

    public void addSkirmish(Skirmishes skirmishesForBattle)
    {
        skirmishes.addAll(skirmishesForBattle.skirmishes);
    }
}
