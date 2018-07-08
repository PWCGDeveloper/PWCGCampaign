package pwcg.aar.prelim;

import java.util.Map;

import pwcg.campaign.Campaign;
import pwcg.campaign.personnel.SquadronMemberFilter;
import pwcg.campaign.squadmember.SquadronMember;
import pwcg.campaign.squadmember.SquadronMembers;
import pwcg.core.exception.PWCGException;

public class CampaignMembersOutOfMissionFinder
{
    public SquadronMembers getCampaignMembersNotInMission(Campaign campaign, SquadronMembers campaignMembersInMission) throws PWCGException
    {
    	Map<Integer, SquadronMember> allCampaignMembers = campaign.getPersonnelManager().getAllCampaignMembers();  
        Map<Integer, SquadronMember> activeAiCampaignMembers = SquadronMemberFilter.filterActiveAI(allCampaignMembers, campaign.getDate());
        SquadronMembers campaignMembersOutOfMission = new SquadronMembers();
    	for (SquadronMember pilot : activeAiCampaignMembers.values())
    	{
    		if (!campaignMembersInMission.getSquadronMemberCollection().containsKey(pilot.getSerialNumber()))
    		{
    	        campaignMembersOutOfMission.addToSquadronMemberCollection(pilot);
    		}
    	}
    	
        return campaignMembersOutOfMission;
    }
}
