package pwcg.aar.outofmission.phase1.elapsedtime;

import pwcg.aar.data.AARContext;
import pwcg.aar.prelim.CampaignMembersOutOfMissionFinder;
import pwcg.campaign.Campaign;
import pwcg.campaign.plane.Role;
import pwcg.campaign.squadmember.AirVictimGenerator;
import pwcg.campaign.squadmember.GroundVictimGenerator;
import pwcg.campaign.squadmember.SquadronMember;
import pwcg.campaign.squadmember.SquadronMembers;
import pwcg.campaign.squadmember.StructureVictimGenerator;
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
        SquadronMembers campaignMembersNotInMission = CampaignMembersOutOfMissionFinder.getActiveCampaignMembersNotInMission(
                campaign, aarContext.getPreliminaryData().getCampaignMembersInMission());
        
        for (SquadronMember squadronMember : campaignMembersNotInMission.getSquadronMemberList())
        {
            if (OutOfMissionPilotSelector.shouldPilotBeEvaluated(campaign, squadronMember)) 
            {
                Role missionRole = squadronMember.determineSquadron().getSquadronRoles().selectRoleForMission(campaign.getDate());
                if (AirVictimGenerator.shouldUse(missionRole))
                {
                    OutOfMissionAirVictoryEventGenerator airVictoryEventGenerator = new OutOfMissionAirVictoryEventGenerator(campaign, aarContext, squadronMember);
                    OutOfMissionVictoryData airVictories = airVictoryEventGenerator.outOfMissionVictoriesForSquadronMember();
                    victoriesOutOMission.merge(airVictories);
                }
                else if (GroundVictimGenerator.shouldUse(missionRole))
                {
                    OutOfMissionGroundVictoryEventGenerator groundVictoryEventGenerator = new OutOfMissionGroundVictoryEventGenerator(campaign, squadronMember);
                    OutOfMissionVictoryData groundVictories = groundVictoryEventGenerator.outOfMissionVictoriesForSquadronMember();
                    victoriesOutOMission.merge(groundVictories);
                }
                else if (StructureVictimGenerator.shouldUse(missionRole))
                {
                    OutOfMissionStructureVictoryEventGenerator structureVictoryEventGenerator = new OutOfMissionStructureVictoryEventGenerator(campaign, squadronMember);
                    OutOfMissionVictoryData structureVictories = structureVictoryEventGenerator.outOfMissionVictoriesForSquadronMember();
                    victoriesOutOMission.merge(structureVictories);
                }
            }
        }

        return victoriesOutOMission;
    }
}
