package pwcg.aar.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pwcg.campaign.squadmember.Victory;

public class AARPersonnelAcheivements
{
    private Map<Integer, Integer> missionsFlown = new HashMap<>();
    private Map<Integer, List<Victory>> victoryAwardByPilot = new HashMap<>();

    public void addVictoryAwardByPilot(Integer serialNumber, Victory victory)
    {
        if (!victoryAwardByPilot.containsKey(serialNumber))
        {
            victoryAwardByPilot.put(serialNumber, new ArrayList<Victory>());
        }
        
        List<Victory> victoriesForPilot = victoryAwardByPilot.get(serialNumber);
        victoriesForPilot.add(victory);
    }

    public void updateMissionsFlown(Integer serialNumber, Integer newMissionsFlown)
    {
        missionsFlown.put(serialNumber, newMissionsFlown);
    }

	public void merge(AARPersonnelAcheivements sourcePersonnelAwards)
	{
		mergeVictories(sourcePersonnelAwards.getVictoriesByPilot());
        missionsFlown.putAll(sourcePersonnelAwards.getMissionsFlown());
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
}
