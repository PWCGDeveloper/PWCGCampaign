package pwcg.aar.prelim;

import java.util.Map;

import pwcg.campaign.Campaign;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.crewmember.CrewMember;
import pwcg.campaign.crewmember.CrewMembers;
import pwcg.campaign.squadron.Company;
import pwcg.campaign.squadron.SquadronViability;
import pwcg.core.exception.PWCGException;

public class CampaignMembersOutOfMissionFinder
{
    public static CrewMembers getAllCampaignMembersNotInMission(Campaign campaign, CrewMembers campaignMembersInMission) throws PWCGException
    {
        Map<Integer, CrewMember> allCampaignMembers = campaign.getPersonnelManager().getAllNonAceCampaignMembers();  
        CrewMembers campaignMembersOutOfMission = new CrewMembers();
        for (CrewMember crewMember : allCampaignMembers.values())
        {
            Company squadron = PWCGContext.getInstance().getSquadronManager().getSquadron(crewMember.getCompanyId());
            if (SquadronViability.isSquadronViable(squadron, campaign))
            {
                if (!campaignMembersInMission.getCrewMemberCollection().containsKey(crewMember.getSerialNumber()))
                {
                    campaignMembersOutOfMission.addToCrewMemberCollection(crewMember);
                }
            }
        }
        
        return campaignMembersOutOfMission;
    }
    
    public static CrewMembers getActiveCampaignMembersNotInMission(Campaign campaign, CrewMembers campaignMembersInMission) throws PWCGException
    {
        Map<Integer, CrewMember> allCampaignMembers = campaign.getPersonnelManager().getAllActiveNonAceCampaignMembers();  
        CrewMembers activeCampaignMembersOutOfMission = new CrewMembers();
        for (CrewMember crewMember : allCampaignMembers.values())
        {
            if (!campaignMembersInMission.getCrewMemberCollection().containsKey(crewMember.getSerialNumber()))
            {
                activeCampaignMembersOutOfMission.addToCrewMemberCollection(crewMember);
            }
        }
        
        return activeCampaignMembersOutOfMission;
    }
}
