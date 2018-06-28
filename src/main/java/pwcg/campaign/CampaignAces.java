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
    private Map<Integer, Ace> campaignAces = new HashMap<>();

	public Map<Integer, Ace> getCampaignAces()
	{
	    Map<Integer, Ace> activeCampaignAces = new HashMap<>();
        for (Ace ace : campaignAces.values())
        {
            if (ace.getPilotActiveStatus() != SquadronMemberStatus.STATUS_ACTIVE)
            {
                continue;
            }
            
            activeCampaignAces.put(ace.getSerialNumber(), ace);
        }
        
        return activeCampaignAces;
	}

    public List<Ace> getCampaignAcesBySquadron(int squadronId) throws PWCGException
    {
        List<Ace> acesForSquadron = new ArrayList<>();
        for (Ace ace : campaignAces.values())
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

	public void setCampaignAces(Map<Integer, Ace> campaignAces)
	{
		this.campaignAces = campaignAces;
	}
	
	public Ace retrieveAceBySerialNumber(int serialNumber)
	{
		return campaignAces.get(serialNumber);
	}
}
