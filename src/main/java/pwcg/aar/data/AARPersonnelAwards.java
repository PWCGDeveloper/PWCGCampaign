package pwcg.aar.data;

import java.util.HashMap;
import java.util.Map;

import pwcg.aar.outofmission.phase2.awards.HistoricalAceAwards;
import pwcg.campaign.medals.Medal;

public class AARPersonnelAwards
{
    private Map<Integer, String> promotions = new HashMap<>();
    private Map<Integer, Map<String, Medal>> medalsAwarded = new HashMap<>();
    private HistoricalAceAwards historicalAceAwards = new HistoricalAceAwards();

    public void addMedal(Integer serialNumber, Medal medal)
    {
        if (!medalsAwarded.containsKey(serialNumber))
        {
            medalsAwarded.put(serialNumber, new HashMap<String, Medal>());
        }

        Map<String, Medal> medalsForPilot = medalsAwarded.get(serialNumber);
        medalsForPilot.put(medal.getMedalName(), medal);
    }

    public void addPromotion(Integer serialNumber, String promotion)
    {
        promotions.put(serialNumber, promotion);
    }


	public void merge(AARPersonnelAwards sourcePersonnelAwards)
	{
		mergeMedals(sourcePersonnelAwards.getMedalsAwarded());
        promotions.putAll(sourcePersonnelAwards.getPromotions());
        historicalAceAwards.merge(sourcePersonnelAwards.getHistoricalAceAwards());
	}

	public void mergeMedals(Map<Integer, Map<String, Medal>> sourceMedalsAwarded)
	{
		for (Integer serialNumber : sourceMedalsAwarded.keySet())
		{
	        if (!medalsAwarded.containsKey(serialNumber))
	        {
	            medalsAwarded.put(serialNumber, new HashMap<String, Medal>());
	        }

            Map<String, Medal> sourceMedalsForPilot = sourceMedalsAwarded.get(serialNumber);
            Map<String, Medal> medalsForPilot = medalsAwarded.get(serialNumber);
            medalsForPilot.putAll(sourceMedalsForPilot);
		}
	}

    public Map<Integer, Map<String, Medal>> getCampaignMemberMedals()
    {
        return medalsAwarded;
    }
    
    public Map<Integer, String> getPromotions()
    {
        return promotions;
    }

    public Map<Integer, Map<String, Medal>> getMedalsAwarded()
    {
        return medalsAwarded;
    }


    public HistoricalAceAwards getHistoricalAceAwards()
    {
        return historicalAceAwards;
    }
}
