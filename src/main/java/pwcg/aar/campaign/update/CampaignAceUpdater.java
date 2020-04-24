package pwcg.aar.campaign.update;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pwcg.campaign.Campaign;
import pwcg.campaign.squadmember.Ace;
import pwcg.campaign.squadmember.SquadronMemberStatus;
import pwcg.campaign.squadmember.Victory;

public class CampaignAceUpdater 
{
	private Campaign campaign = null;
	
    private Map<Integer, List<Victory>> aceVictories = new HashMap<>();
	
	public CampaignAceUpdater (Campaign campaign, Map<Integer, List<Victory>> aceVictories) 
	{
        this.campaign = campaign;
        this.aceVictories = aceVictories;
	}
	

	public void updatesCampaignAces() 
	{
		updateAceMissionsFlown();

		updateHistoricalVictories();
	}


    private void updateAceMissionsFlown()
    {
        for (Ace ace : campaign.getPersonnelManager().getCampaignAces().getActiveCampaignAces().values())
		{
			if (ace.getPilotActiveStatus() == SquadronMemberStatus.STATUS_ACTIVE)
			{
				int missionFlown = ace.getMissionFlown();
				missionFlown += 3;
				ace.setMissionFlown(missionFlown);
			}
		}
    }


    private void updateHistoricalVictories()
    {
        for (Integer serialNumber : aceVictories.keySet())
		{
			Ace ace = campaign.getPersonnelManager().getCampaignAces().retrieveAceBySerialNumber(serialNumber);
			if (ace != null)
			{
    			for (Victory victory : aceVictories.get(serialNumber)) 
    			{
    				ace.addVictory(victory);
    			}
			}
		}
    }
 }
