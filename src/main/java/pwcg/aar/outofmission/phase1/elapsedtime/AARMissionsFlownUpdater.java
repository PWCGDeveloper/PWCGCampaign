package pwcg.aar.outofmission.phase1.elapsedtime;

import pwcg.aar.data.AARContext;
import pwcg.aar.outofmission.phase2.awards.MissionsFlownCalculator;
import pwcg.aar.prelim.CampaignMembersOutOfMissionFinder;
import pwcg.campaign.Campaign;
import pwcg.campaign.crewmember.CrewMember;
import pwcg.campaign.crewmember.CrewMembers;
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
        CrewMembers campaignMembersInMission = aarContext.getPreliminaryData().getCampaignMembersInMission();
        for (CrewMember crewMember : campaignMembersInMission.getCrewMemberList())
        {
            int updatedMissionsFlown = MissionsFlownCalculator.calculateMissionsFlown(campaign, crewMember);
            aarContext.getPersonnelAcheivements().updateMissionsFlown(crewMember.getSerialNumber(), updatedMissionsFlown);
        }
    }

    public void missionsFlownOutOfMission() throws PWCGException 
    {        
        CrewMembers campaignMembersInMission = aarContext.getPreliminaryData().getCampaignMembersInMission();
        CrewMembers campaignMembersNotInMission = CampaignMembersOutOfMissionFinder.getActiveCampaignMembersNotInMission(campaign, campaignMembersInMission);
        
        for (CrewMember crewMember : campaignMembersNotInMission.getCrewMemberList())
        {
            if (OutOfMissionCrewMemberSelector.shouldCrewMemberBeEvaluated(campaign, crewMember)) 
            {
                int updatedMissionsFlown = MissionsFlownCalculator.calculateMissionsFlown(campaign, crewMember);
                aarContext.getPersonnelAcheivements().updateMissionsFlown(crewMember.getSerialNumber(), updatedMissionsFlown);
            }
        }
    }
}
