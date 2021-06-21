package pwcg.aar.outofmission.phase1.elapsedtime;

import pwcg.aar.data.AARContext;
import pwcg.aar.outofmission.phase2.awards.MissionsFlownCalculator;
import pwcg.aar.prelim.CampaignMembersOutOfMissionFinder;
import pwcg.campaign.Campaign;
import pwcg.campaign.squadmember.SquadronMember;
import pwcg.campaign.squadmember.SquadronMembers;
import pwcg.core.exception.PWCGException;

public class AARMissionsFlownUpdater
{
    private Campaign campaign;
    private AARContext aarContext;

    public AARMissionsFlownUpdater(Campaign campaign, AARContext aarContext)
    {
        this.campaign = campaign;
        this.aarContext = aarContext;
    }

    public void updateMissionsFlown() throws PWCGException 
    {        
        missionsFlownInMission();
        missionsFlownOutOfMission();
    }
    
    private void missionsFlownInMission() throws PWCGException
    {
        SquadronMembers campaignMembersInMission = aarContext.getPreliminaryData().getCampaignMembersInMission();
        for (SquadronMember squadronMember : campaignMembersInMission.getSquadronMemberList())
        {
            int updatedMissionsFlown = MissionsFlownCalculator.calculateMissionsFlown(campaign, squadronMember);
            aarContext.getPersonnelAcheivements().updateMissionsFlown(squadronMember.getSerialNumber(), updatedMissionsFlown);
        }
    }

    public void missionsFlownOutOfMission() throws PWCGException 
    {        
        SquadronMembers campaignMembersInMission = aarContext.getPreliminaryData().getCampaignMembersInMission();
        SquadronMembers campaignMembersNotInMission = CampaignMembersOutOfMissionFinder.getActiveCampaignMembersNotInMission(campaign, campaignMembersInMission);
        
        for (SquadronMember squadronMember : campaignMembersNotInMission.getSquadronMemberList())
        {
            if (OutOfMissionPilotSelector.shouldPilotBeEvaluated(campaign, squadronMember)) 
            {
                int updatedMissionsFlown = MissionsFlownCalculator.calculateMissionsFlown(campaign, squadronMember);
                aarContext.getPersonnelAcheivements().updateMissionsFlown(squadronMember.getSerialNumber(), updatedMissionsFlown);
            }
        }
    }
}
