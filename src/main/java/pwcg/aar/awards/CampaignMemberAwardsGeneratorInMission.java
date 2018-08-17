package pwcg.aar.awards;

import java.util.Map;

import pwcg.aar.data.AARContext;
import pwcg.aar.data.AARPersonnelAwards;
import pwcg.campaign.Campaign;
import pwcg.campaign.personnel.SquadronMemberFilter;
import pwcg.campaign.squadmember.SquadronMember;
import pwcg.campaign.squadmember.SquadronMembers;
import pwcg.core.exception.PWCGException;

public class CampaignMemberAwardsGeneratorInMission extends CampaignMemberAwardsGenerator
{
    public CampaignMemberAwardsGeneratorInMission(
            Campaign campaign, 
            AARContext aarContext)
    {
    	super(campaign, aarContext);
    }
    
    public AARPersonnelAwards createCampaignMemberAwards() throws PWCGException
    {
        generateWoundBadgesForCampaignMembersInMission();
        generateAwardsForCampaignMembersInMission();
        return personnelAwards;
    }

	private void generateWoundBadgesForCampaignMembersInMission() throws PWCGException
	{
		for (SquadronMember campaignMemberKilledInMission : aarContext.getCampaignUpdateData().getPersonnelLosses().getPersonnelKilled().values())
        {
            awardWoundMedal(campaignMemberKilledInMission);
        }
        
        for (SquadronMember campaignMemberWoundedInMission : aarContext.getCampaignUpdateData().getPersonnelLosses().getPersonnelMaimed().values())
        {
            awardWoundMedal(campaignMemberWoundedInMission);
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
        promotions(campaignMemberInMission);
        medals(campaignMemberInMission, victoriesInMissionForPilot);
        missionsFlown(campaignMemberInMission);
	}
}
