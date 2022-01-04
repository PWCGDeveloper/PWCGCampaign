package pwcg.campaign;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pwcg.campaign.company.Company;
import pwcg.campaign.crewmember.CrewMemberStatus;
import pwcg.campaign.crewmember.TankAce;
import pwcg.core.exception.PWCGException;

public class CampaignAces
{
    private Map<Integer, TankAce> acesInCampaign = new HashMap<>();

	public Map<Integer, TankAce> getAllCampaignAces()
	{
	    Map<Integer, TankAce> activeCampaignAces = new HashMap<>();
        for (TankAce ace : acesInCampaign.values())
        {
            activeCampaignAces.put(ace.getSerialNumber(), ace);
        }
        
        return activeCampaignAces;
	}
	
    public Map<Integer, TankAce> getActiveCampaignAces()
    {
        Map<Integer, TankAce> activeCampaignAces = new HashMap<>();
        for (TankAce ace : acesInCampaign.values())
        {
            if (ace.getCrewMemberActiveStatus() != CrewMemberStatus.STATUS_ACTIVE)
            {
                continue;
            }
            
            activeCampaignAces.put(ace.getSerialNumber(), ace);
        }
        
        return activeCampaignAces;
    }


    public List<TankAce> getActiveCampaignAcesBySquadron(int squadronId) throws PWCGException
    {
        List<TankAce> acesForSquadron = new ArrayList<>();
        for (TankAce ace : acesInCampaign.values())
        {
            if (ace.getCrewMemberActiveStatus() != CrewMemberStatus.STATUS_ACTIVE)
            {
                continue;
            }
            
            Company aceSquadron = ace.determineSquadron();
            if (aceSquadron != null)
            {
                if (aceSquadron.getCompanyId() == squadronId)
                {
                    acesForSquadron.add(ace);
                }
            }
        }
        
        return acesForSquadron;
    }

	public void setCampaignAces(Map<Integer, TankAce> acesInCampaign)
	{
		this.acesInCampaign = acesInCampaign;
	}
	
	public TankAce retrieveAceBySerialNumber(int serialNumber)
	{
		return acesInCampaign.get(serialNumber);
	}

    public void mergeAddedAces(CampaignAces acesAvailable)
    {
        for (TankAce ace : acesAvailable.getAllCampaignAces().values())
        {
            if (!acesInCampaign.containsKey(ace.getSerialNumber()))
            {
                acesInCampaign.put(ace.getSerialNumber(), ace);
            }
        }
        
    }
}
