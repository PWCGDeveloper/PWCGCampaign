package pwcg.aar.awards;

import pwcg.aar.AARFactory;
import pwcg.aar.data.AARContext;
import pwcg.aar.data.AARPersonnelAwards;
import pwcg.aar.outofmission.phase1.elapsedtime.OutOfMissionPilotSelector;
import pwcg.aar.prelim.CampaignMembersOutOfMissionFinder;
import pwcg.campaign.Campaign;
import pwcg.campaign.squadmember.SquadronMember;
import pwcg.campaign.squadmember.SquadronMembers;
import pwcg.core.exception.PWCGException;

public class CampaignMemberAwardsGeneratorOutOfMission
{
    private Campaign campaign;
    private AARContext aarContext;
    private CampaignMemberAwardsGeneratorCommon awardsGeneratorCommon;

    public CampaignMemberAwardsGeneratorOutOfMission(
            Campaign campaign, 
            AARContext aarContext)
    {
        this.campaign = campaign;
        this.aarContext = aarContext;
        this.awardsGeneratorCommon =  AARFactory.makeCampaignMemberAwardsGeneratorCommon(campaign);
    }

    public AARPersonnelAwards createCampaignMemberAwards() throws PWCGException
    {
        SquadronMembers campaignMembersNotInMission = CampaignMembersOutOfMissionFinder.getAllCampaignMembersNotInMission(
                campaign, aarContext.getPreliminaryData().getCampaignMembersInMission());
        for (SquadronMember squadronMember : campaignMembersNotInMission.getSquadronMemberList())
        {
            awardsGeneratorCommon.promotions(squadronMember);
            int pseudoVictories = 1;
            awardsGeneratorCommon.medals(squadronMember, pseudoVictories);
            if (OutOfMissionPilotSelector.shouldPilotBeEvaluated(campaign, squadronMember)) 
            {
                awardsGeneratorCommon.missionsFlown(squadronMember);
            }
        }
        
        return awardsGeneratorCommon.getPersonnelAwards();
    }
}
