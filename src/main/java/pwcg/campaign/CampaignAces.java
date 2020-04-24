package pwcg.campaign;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pwcg.campaign.squadmember.Ace;
import pwcg.campaign.squadmember.SquadronMemberStatus;
import pwcg.campaign.squadron.Squadron;
import pwcg.core.exception.PWCGException;

public class CampaignAces
{
    private Map<Integer, Ace> acesInCampaign = new HashMap<>();

	public Map<Integer, Ace> getAllCampaignAces()
	{
	    Map<Integer, Ace> activeCampaignAces = new HashMap<>();
        for (Ace ace : acesInCampaign.values())
        {
            activeCampaignAces.put(ace.getSerialNumber(), ace);
        }
        
        return activeCampaignAces;
	}
	
    public Map<Integer, Ace> getActiveCampaignAces()
    {
        Map<Integer, Ace> activeCampaignAces = new HashMap<>();
        for (Ace ace : acesInCampaign.values())
        {
            if (ace.getPilotActiveStatus() != SquadronMemberStatus.STATUS_ACTIVE)
            {
                continue;
            }
            
            activeCampaignAces.put(ace.getSerialNumber(), ace);
        }
        
        return activeCampaignAces;
    }


    public List<Ace> getActiveCampaignAcesBySquadron(int squadronId) throws PWCGException
    {
        List<Ace> acesForSquadron = new ArrayList<>();
        for (Ace ace : acesInCampaign.values())
        {
            if (ace.getPilotActiveStatus() != SquadronMemberStatus.STATUS_ACTIVE)
            {
                continue;
            }
            
            Squadron aceSquadron = ace.determineSquadron();
            if (aceSquadron != null)
            {
                if (aceSquadron.getSquadronId() == squadronId)
                {
                    acesForSquadron.add(ace);
                }
            }
        }
        
        return acesForSquadron;
    }

	public void setCampaignAces(Map<Integer, Ace> acesInCampaign)
	{
		this.acesInCampaign = acesInCampaign;
	}
	
	public Ace retrieveAceBySerialNumber(int serialNumber)
	{
		return acesInCampaign.get(serialNumber);
	}

    public void mergeAddedAces(CampaignAces acesAvailable)
    {
        for (Ace ace : acesAvailable.getAllCampaignAces().values())
        {
            if (!acesInCampaign.containsKey(ace.getSerialNumber()))
            {
                acesInCampaign.put(ace.getSerialNumber(), ace);
            }
        }
        
    }
}
