package pwcg.aar.outofmission.phase2.awards;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pwcg.campaign.crewmember.Victory;
import pwcg.campaign.medals.Medal;

public class HistoricalAceAwards
{
    private Map<Integer, List<Victory>> aceVictories = new HashMap<>();
    private Map<Integer, List<Medal>> aceMedals = new HashMap<>();
    private Map<Integer, String> acePromotions = new HashMap<>();

    public HistoricalAceAwards()
    {
    }

    public Map<Integer, List<Victory>> getAceVictories()
    {
        return aceVictories;
    }

    public void addAceVictory(Integer serialNumber, Victory victory)
    {
        if (!aceVictories.containsKey(serialNumber))
        {
            List<Victory> victoriesForAce = new ArrayList<>();
            aceVictories.put(serialNumber, victoriesForAce);
        }
        
        List<Victory> victoriesForAce = aceVictories.get(serialNumber);
        victoriesForAce.add(victory);
    }

    public Map<Integer, List<Medal>> getAceMedals()
    {
        return aceMedals;
    }

    public void addAceMedal(Integer serialNumber, Medal medal)
    {
        if (!aceMedals.containsKey(serialNumber))
        {
            List<Medal> medalsForAce = new ArrayList<>();
            aceMedals.put(serialNumber, medalsForAce);
        }
        
        List<Medal> medalsForAce = aceMedals.get(serialNumber);
        medalsForAce.add(medal);
    }

    public Map<Integer, String> getAcePromotions()
    {
        return acePromotions;
    }

    public void addAcePromotions(Integer serialNumber, String promotion)
    {
        acePromotions.put(serialNumber, promotion);
    }

    public void addEvents(HistoricalAceAwards historicalAceEvents)
    {
        aceVictories.putAll(historicalAceEvents.getAceVictories());
        aceMedals.putAll(historicalAceEvents.getAceMedals());
        acePromotions.putAll(historicalAceEvents.getAcePromotions());
    }

	public void merge(HistoricalAceAwards sourceHistoricalAceAwards)
	{
		mergeMedals(sourceHistoricalAceAwards);
		mergeVictories(sourceHistoricalAceAwards);
		mergePromotions(sourceHistoricalAceAwards);
	}


	private void mergeMedals(HistoricalAceAwards sourceHistoricalAceAwards)
	{
		for (Integer serialNumber : sourceHistoricalAceAwards.getAceMedals().keySet())
		{
			List<Medal> sourceMedalsForAce = sourceHistoricalAceAwards.getAceMedals().get(serialNumber);
			if (aceMedals.containsKey(serialNumber))
			{
				List<Medal> medalsForAce = aceMedals.get(serialNumber);
				medalsForAce.addAll(sourceMedalsForAce);
			}
			else
			{
				aceMedals.put(serialNumber, sourceMedalsForAce);
			}
		}
	}

	private void mergeVictories(HistoricalAceAwards sourceHistoricalAceAwards)
	{
		for (Integer serialNumber : sourceHistoricalAceAwards.getAceVictories().keySet())
		{
			List<Victory> sourceVictoriesForAce = sourceHistoricalAceAwards.getAceVictories().get(serialNumber);
			if (aceVictories.containsKey(serialNumber))
			{
				List<Victory> victoriesForAce = aceVictories.get(serialNumber);
				victoriesForAce.addAll(sourceVictoriesForAce);
			}
			else
			{
				aceVictories.put(serialNumber, sourceVictoriesForAce);
			}
		}
	}

	private void mergePromotions(HistoricalAceAwards sourceHistoricalAceAwards)
	{
		acePromotions.putAll(sourceHistoricalAceAwards.getAcePromotions());
	}
}