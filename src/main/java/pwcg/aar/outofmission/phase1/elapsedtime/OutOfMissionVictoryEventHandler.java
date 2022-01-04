package pwcg.aar.outofmission.phase1.elapsedtime;

import pwcg.aar.data.AARContext;
import pwcg.aar.prelim.CampaignMembersOutOfMissionFinder;
import pwcg.campaign.Campaign;
import pwcg.campaign.crewmember.CrewMember;
import pwcg.campaign.crewmember.CrewMembers;
import pwcg.campaign.crewmember.GroundVictimGenerator;
import pwcg.campaign.tank.PwcgRole;
import pwcg.core.exception.PWCGException;

public class OutOfMissionVictoryEventHandler 
{    
	private Campaign campaign = null;
    private AARContext aarContext; 

	private OutOfMissionVictoryData victoriesOutOMission = new OutOfMissionVictoryData();

	public OutOfMissionVictoryEventHandler (Campaign campaign, AARContext aarContext) 
	{
        this.campaign = campaign;
        this.aarContext = aarContext;
	}
	
    
    public OutOfMissionVictoryData generateOutOfMissionVictories() throws PWCGException
    {
        CrewMembers campaignMembersNotInMission = CampaignMembersOutOfMissionFinder.getActiveCampaignMembersNotInMission(
                campaign, aarContext.getPreliminaryData().getCampaignMembersInMission());
        
        for (CrewMember crewMember : campaignMembersNotInMission.getCrewMemberList())
        {
            if (OutOfMissionCrewMemberSelector.shouldCrewMemberBeEvaluated(campaign, crewMember)) 
            {
                PwcgRole missionRole = crewMember.determineSquadron().getSquadronRoles().selectRoleForMission(campaign.getDate());
                if (GroundVictimGenerator.shouldUse(missionRole.getRoleCategory()))
                {
                    OutOfMissionGroundVictoryEventGenerator groundVictoryEventGenerator = new OutOfMissionGroundVictoryEventGenerator(campaign, crewMember);
                    OutOfMissionVictoryData groundVictories = groundVictoryEventGenerator.outOfMissionVictoriesForCrewMember();
                    victoriesOutOMission.merge(groundVictories);
                }
            }
        }

        return victoriesOutOMission;
    }
}
