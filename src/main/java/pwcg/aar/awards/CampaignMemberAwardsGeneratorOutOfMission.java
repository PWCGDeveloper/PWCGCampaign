package pwcg.aar.awards;

import pwcg.aar.data.AARContext;
import pwcg.aar.data.AARPersonnelAwards;
import pwcg.aar.outofmission.phase1.elapsedtime.OutOfMissionPilotSelector;
import pwcg.aar.prelim.CampaignMembersOutOfMissionFinder;
import pwcg.campaign.Campaign;
import pwcg.campaign.squadmember.SquadronMember;
import pwcg.campaign.squadmember.SquadronMembers;
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
        SquadronMembers campaignMembersNotInMission = CampaignMembersOutOfMissionFinder.getAllCampaignMembersNotInMission(
                campaign, aarContext.getPreliminaryData().getCampaignMembersInMission());
        for (SquadronMember squadronMember : campaignMembersNotInMission.getSquadronMemberList())
        {
            promotions(squadronMember);
            medals(squadronMember, 1);
            if (OutOfMissionPilotSelector.shouldPilotBeEvaluated(campaign, squadronMember)) 
            {
                missionsFlown(squadronMember);
            }
        }
        
        return personnelAwards;
    }
}
