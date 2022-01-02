package pwcg.aar.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pwcg.aar.ui.events.model.ClaimDeniedEvent;
import pwcg.campaign.crewmember.Victory;

public class AARPersonnelAcheivements
{
    private Map<Integer, Integer> missionsFlown = new HashMap<>();
    private Map<Integer, List<Victory>> victoryAwardByCrewMember = new HashMap<>();
    private List<ClaimDeniedEvent> playerClaimsDenied = new ArrayList<>();

    public void updateMissionsFlown(Integer serialNumber, Integer newMissionsFlown)
    {
        missionsFlown.put(serialNumber, newMissionsFlown);
    }

	public void merge(AARPersonnelAcheivements sourcePersonnelAwards)
	{
		mergeVictories(sourcePersonnelAwards.getVictoriesByCrewMember());
        missionsFlown.putAll(sourcePersonnelAwards.getMissionsFlown());
	}

	public void mergeVictories(Map<Integer, List<Victory>> sourceVictoryAwardByCrewMember)
	{
		for (Integer serialNumber : sourceVictoryAwardByCrewMember.keySet())
		{
		    List<Victory> victoriesForPlot = sourceVictoryAwardByCrewMember.get(serialNumber);
		    for (Victory victory : victoriesForPlot)
		    {
		        addVictoryAwardByCrewMember(serialNumber, victory);
		    }
		}
	}

    private void addVictoryAwardByCrewMember(Integer serialNumber, Victory victory)
    {
        if (!victoryAwardByCrewMember.containsKey(serialNumber))
        {
            victoryAwardByCrewMember.put(serialNumber, new ArrayList<Victory>());
        }
        
        List<Victory> victoriesForCrewMember = victoryAwardByCrewMember.get(serialNumber);
        victoriesForCrewMember.add(victory);
    }

    public Map<Integer, Integer> getMissionsFlown()
    {
        return missionsFlown;
    }
    
    public Map<Integer, List<Victory>> getVictoriesByCrewMember()
    {
        return victoryAwardByCrewMember;
    }
    
    public int getGroundVictoryCountForCrewMember(int serialNumber)
    {
        int numGroundVictoriesForCrewMember = 0;
        if (victoryAwardByCrewMember.containsKey(serialNumber))
        {
            for (Victory victoryForCrewMember : victoryAwardByCrewMember.get(serialNumber))
            {
                if (victoryForCrewMember.getVictim().getAirOrGround() == Victory.VEHICLE)
                {
                    ++numGroundVictoriesForCrewMember;
                }
            }
        }
        return numGroundVictoriesForCrewMember;
    }
    
    public int getAirVictoryCountForCrewMember(int serialNumber)
    {
        int numAirVictoriesForCrewMember = 0;
        if (victoryAwardByCrewMember.containsKey(serialNumber))
        {
            for (Victory victoryForCrewMember : victoryAwardByCrewMember.get(serialNumber))
            {
                if (victoryForCrewMember.getVictim().getAirOrGround() == Victory.AIRCRAFT)
                {
                    ++numAirVictoriesForCrewMember;
                }
            }
        }
        return numAirVictoriesForCrewMember;
    }
    
    public int getTotalAirToAirVictories()
    {
    	int totalAirToAirVictories = 0;
    	for (List<Victory> victoriesForCrewMember : victoryAwardByCrewMember.values())
    	{
    	    for (Victory victory : victoriesForCrewMember)
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
        for (List<Victory> victoriesForCrewMember : victoryAwardByCrewMember.values())
        {
            for (Victory victory : victoriesForCrewMember)
            {
                if (victory.getVictim().getAirOrGround() == Victory.VEHICLE)
                {
                    ++totalAirToAirVictories;
                }
            }
        }
        return totalAirToAirVictories;
    }

    public List<Victory> getVictoryAwardsForCrewMember(Integer serialNumber)
    {
        List<Victory> victoriesForCrewMember = new ArrayList<Victory>();
        if (victoryAwardByCrewMember.containsKey(serialNumber))
        {
            victoriesForCrewMember = victoryAwardByCrewMember.get(serialNumber);
        }
        return victoriesForCrewMember;
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
