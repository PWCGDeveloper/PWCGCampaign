package pwcg.aar.prelim;

import java.util.Map;

import pwcg.campaign.Campaign;
import pwcg.campaign.squadmember.SquadronMember;
import pwcg.campaign.squadmember.SquadronMembers;
import pwcg.core.exception.PWCGException;

public class CampaignMembersOutOfMissionFinder
{
    public SquadronMembers getCampaignMembersNotInMission(Campaign campaign, SquadronMembers campaignMembersInMission) throws PWCGException
    {
    	Map<Integer, SquadronMember> allCampaignMembers = campaign.getPersonnelManager().getAllNonAceCampaignMembers();  
        SquadronMembers campaignMembersOutOfMission = new SquadronMembers();
    	for (SquadronMember pilot : allCampaignMembers.values())
    	{
    		if (!campaignMembersInMission.getSquadronMembers().containsKey(pilot.getSerialNumber()))
    		{
    	        campaignMembersOutOfMission.addSquadronMember(pilot);
    		}
    	}
    	
        return campaignMembersOutOfMission;
    }
}
