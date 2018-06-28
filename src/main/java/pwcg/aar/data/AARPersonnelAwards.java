package pwcg.aar.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pwcg.aar.outofmission.phase1.elapsedtime.HistoricalAceAwards;
import pwcg.campaign.medals.Medal;
import pwcg.campaign.squadmember.Victory;

public class AARPersonnelAwards
{
    private Map<Integer, Map<String, Medal>> medalsAwarded = new HashMap<>();
    private Map<Integer, String> promotions = new HashMap<>();
    private Map<Integer, Integer> missionsFlown = new HashMap<>();
    private Map<Integer, List<Victory>> victoryAwardByPilot = new HashMap<>();
    private HistoricalAceAwards historicalAceAwards = new HistoricalAceAwards();

    public void addVictoryAwardByPilot(Integer serialNumber, Victory victory)
    {
        if (!victoryAwardByPilot.containsKey(serialNumber))
        {
            victoryAwardByPilot.put(serialNumber, new ArrayList<Victory>());
        }
        
        List<Victory> victoriesForPilot = victoryAwardByPilot.get(serialNumber);
        victoriesForPilot.add(victory);
    }
    
    
    public void addMedal(Integer serialNumber, Medal medal)
    {
        if (!medalsAwarded.containsKey(serialNumber))
        {
            medalsAwarded.put(serialNumber, new HashMap<String, Medal>());
        }

        Map<String, Medal> medalsForPilot = medalsAwarded.get(serialNumber);
        medalsForPilot.put(medal.getMedalName(), medal);
    }

    public void addMissionsFlown(Integer serialNumber, Integer newRank)
    {
        missionsFlown.put(serialNumber, newRank);
    }

    public void addPromotion(Integer serialNumber, String pilotMissionsFlown)
    {
        promotions.put(serialNumber, pilotMissionsFlown);
    }


	public void merge(AARPersonnelAwards sourcePersonnelAwards)
	{
		mergeMedals(sourcePersonnelAwards.getMedalsAwarded());
		mergeVictories(sourcePersonnelAwards.getVictoriesByPilot());
        missionsFlown.putAll(sourcePersonnelAwards.getMissionsFlown());
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

	public void mergeVictories(Map<Integer, List<Victory>> sourceVictoryAwardByPilot)
	{
		for (Integer serialNumber : sourceVictoryAwardByPilot.keySet())
		{
            if (!victoryAwardByPilot.containsKey(serialNumber))
            {
                victoryAwardByPilot.put(serialNumber, new ArrayList<Victory>());
            }

            List<Victory> sourceVictoriesForPilot = sourceVictoryAwardByPilot.get(serialNumber);
            List<Victory> victoriesForPilot = victoryAwardByPilot.get(serialNumber);
            victoriesForPilot.addAll(sourceVictoriesForPilot);
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
    
    public Map<Integer, Integer> getMissionsFlown()
    {
        return missionsFlown;
    }
    
    public Map<Integer, List<Victory>> getVictoriesByPilot()
    {
        return victoryAwardByPilot;
    }
    
    public int getTotalAirToAirVictories()
    {
    	int totalAirToAirVictories = 0;
    	for (List<Victory> victoriesForPilot : victoryAwardByPilot.values())
    	{
    		totalAirToAirVictories += victoriesForPilot.size();
    	}
        return totalAirToAirVictories;
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
