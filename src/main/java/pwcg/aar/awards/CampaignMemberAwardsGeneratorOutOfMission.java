package pwcg.aar.awards;

import java.util.Map;

import pwcg.aar.data.AARContext;
import pwcg.aar.data.AARPersonnelAwards;
import pwcg.campaign.Campaign;
import pwcg.campaign.squadmember.SquadronMember;
import pwcg.core.exception.PWCGException;

public class CampaignMemberAwardsGeneratorOutOfMission extends CampaignMemberAwardsGenerator
{
    public CampaignMemberAwardsGeneratorOutOfMission(
            Campaign campaign, 
            AARContext aarContext)
    {
        super(campaign, aarContext);
    }

    public AARPersonnelAwards createCampaignMemberAwards() throws PWCGException
    {
        Map<Integer, SquadronMember> campaignMembersNotInMission = aarContext.getPreliminaryData().getCampaignMembersOutOfMission().getSquadronMembers();
        for (SquadronMember campaignMember : campaignMembersNotInMission.values())
        {
            promotions(campaignMember);
            medals(campaignMember, 1);
            missionsFlown(campaignMember);
        }
        
        return personnelAwards;
    }
}
