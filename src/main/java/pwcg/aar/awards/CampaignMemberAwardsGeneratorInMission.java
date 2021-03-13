package pwcg.aar.awards;

import java.util.Map;

import pwcg.aar.AARFactory;
import pwcg.aar.data.AARContext;
import pwcg.aar.data.AARPersonnelAwards;
import pwcg.campaign.Campaign;
import pwcg.campaign.personnel.SquadronMemberFilter;
import pwcg.campaign.squadmember.SquadronMember;
import pwcg.campaign.squadmember.SquadronMembers;
import pwcg.core.exception.PWCGException;

public class CampaignMemberAwardsGeneratorInMission
{
    private Campaign campaign;
    private AARContext aarContext;
    private CampaignMemberAwardsGeneratorCommon awardsGeneratorCommon;
    
    public CampaignMemberAwardsGeneratorInMission(
            Campaign campaign, 
            AARContext aarContext)
    {
        this.campaign = campaign;
        this.aarContext = aarContext;
    	this.awardsGeneratorCommon =  AARFactory.makeCampaignMemberAwardsGeneratorCommon(campaign);
    }
    
    public AARPersonnelAwards createCampaignMemberAwards() throws PWCGException
    {
        generateWoundBadgesForCampaignMembersInMission();
        generateAwardsForCampaignMembersInMission();
        return awardsGeneratorCommon.getPersonnelAwards();
    }

	private void generateWoundBadgesForCampaignMembersInMission() throws PWCGException
	{
		for (SquadronMember campaignMemberKilledInMission : aarContext.getCampaignUpdateData().getPersonnelLosses().getPersonnelKilled().values())
        {
		    awardsGeneratorCommon.awardWoundMedal(campaignMemberKilledInMission);
        }
        
        for (SquadronMember campaignMemberWoundedInMission : aarContext.getCampaignUpdateData().getPersonnelLosses().getPersonnelMaimed().values())
        {
            awardsGeneratorCommon.awardWoundMedal(campaignMemberWoundedInMission);
        }
        
        for (SquadronMember campaignMemberWoundedInMission : aarContext.getCampaignUpdateData().getPersonnelLosses().getPersonnelWounded().values())
        {
            awardsGeneratorCommon.awardWoundMedal(campaignMemberWoundedInMission);
        }
	}

	private void generateAwardsForCampaignMembersInMission() throws PWCGException
	{
        Map<Integer, SquadronMember> campaignMembersInMission = aarContext.getPreliminaryData().getCampaignMembersInMission().getSquadronMemberCollection();
        SquadronMembers nonAceCampaignMembersInMission = SquadronMemberFilter.filterActiveAIAndPlayer(campaignMembersInMission, campaign.getDate());
		for (SquadronMember nonAceCampaignMember : nonAceCampaignMembersInMission.getSquadronMemberCollection().values())
		{
		    SquadronMember campaignMemberInMission = campaign.getPersonnelManager().getAnyCampaignMember(nonAceCampaignMember.getSerialNumber());
			generateEvents(campaignMemberInMission);
		}
	}

	private void generateEvents(SquadronMember campaignMemberInMission) throws PWCGException
	{
        int victoriesInMissionForPilot = aarContext.getReconciledInMissionData().getReconciledVictoryData().getVictoryAwardsForPilot(campaignMemberInMission.getSerialNumber()).size();
        awardsGeneratorCommon.promotions(campaignMemberInMission);
        awardsGeneratorCommon.medals(campaignMemberInMission, victoriesInMissionForPilot);
        awardsGeneratorCommon.missionsFlown(campaignMemberInMission);
	}
}
