package pwcg.aar.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pwcg.aar.ui.events.model.ClaimDeniedEvent;
import pwcg.campaign.squadmember.Victory;

public class AARPersonnelAcheivements
{
    private Map<Integer, Integer> missionsFlown = new HashMap<>();
    private Map<Integer, List<Victory>> victoryAwardByPilot = new HashMap<>();
    private List<ClaimDeniedEvent> playerClaimsDenied = new ArrayList<>();

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
    
    public int getGroundVictoryCountForPilot(int serialNumber)
    {
        int numGroundVictoriesForPilot = 0;
        if (victoryAwardByPilot.containsKey(serialNumber))
        {
            for (Victory victoryForPilot : victoryAwardByPilot.get(serialNumber))
            {
                if (victoryForPilot.getVictim().getAirOrGround() == Victory.VEHICLE)
                {
                    ++numGroundVictoriesForPilot;
                }
            }
        }
        return numGroundVictoriesForPilot;
    }
    
    public int getAirVictoryCountForPilot(int serialNumber)
    {
        int numAirVictoriesForPilot = 0;
        if (victoryAwardByPilot.containsKey(serialNumber))
        {
            for (Victory victoryForPilot : victoryAwardByPilot.get(serialNumber))
            {
                if (victoryForPilot.getVictim().getAirOrGround() == Victory.AIRCRAFT)
                {
                    ++numAirVictoriesForPilot;
                }
            }
        }
        return numAirVictoriesForPilot;
    }
    
    public int getTotalAirToAirVictories()
    {
    	int totalAirToAirVictories = 0;
    	for (List<Victory> victoriesForPilot : victoryAwardByPilot.values())
    	{
    	    for (Victory victory : victoriesForPilot)
    	    {
    	        if (victory.getVictim().getAirOrGround() == Victory.AIRCRAFT)
    	        {
    	            ++totalAirToAirVictories;
    	        }
    	    }
    	}
        return totalAirToAirVictories;
    }
    
    public int getTotalAirToGroundVictories()
    {
        int totalAirToAirVictories = 0;
        for (List<Victory> victoriesForPilot : victoryAwardByPilot.values())
        {
            for (Victory victory : victoriesForPilot)
            {
                if (victory.getVictim().getAirOrGround() == Victory.VEHICLE)
                {
                    ++totalAirToAirVictories;
                }
            }
        }
        return totalAirToAirVictories;
    }

    public List<Victory> getVictoryAwardsForPilot(Integer serialNumber)
    {
        List<Victory> victoriesForPilot = new ArrayList<Victory>();
        if (victoryAwardByPilot.containsKey(serialNumber))
        {
            victoriesForPilot = victoryAwardByPilot.get(serialNumber);
        }
        return victoriesForPilot;
    }

    public List<ClaimDeniedEvent> getPlayerClaimsDenied()
    {
        return playerClaimsDenied;
    }

    public void setPlayerClaimsDenied(List<ClaimDeniedEvent> playerClaimsDenied)
    {
        this.playerClaimsDenied = playerClaimsDenied;
    }
}
