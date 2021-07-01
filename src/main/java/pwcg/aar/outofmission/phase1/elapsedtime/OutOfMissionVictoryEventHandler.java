package pwcg.aar.outofmission.phase1.elapsedtime;

import pwcg.aar.data.AARContext;
import pwcg.aar.prelim.CampaignMembersOutOfMissionFinder;
import pwcg.campaign.Campaign;
import pwcg.campaign.plane.Role;
import pwcg.campaign.squadmember.SquadronMember;
import pwcg.campaign.squadmember.SquadronMembers;
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
                if (missionRole == Role.ROLE_FIGHTER)
                {
                    OutOfMissionAirVictoryEventGenerator airVictoryEventGenerator = new OutOfMissionAirVictoryEventGenerator(campaign, aarContext, squadronMember);
                    OutOfMissionVictoryData airVictories = airVictoryEventGenerator.outOfMissionVictoriesForSquadronMember();
                    victoriesOutOMission.merge(airVictories);
                }
                else if (missionRole == Role.ROLE_ATTACK || missionRole == Role.ROLE_DIVE_BOMB)
                {
                    OutOfMissionGroundVictoryEventGenerator groundVictoryEventGenerator = new OutOfMissionGroundVictoryEventGenerator(campaign, squadronMember);
                    OutOfMissionVictoryData groundVictories = groundVictoryEventGenerator.outOfMissionVictoriesForSquadronMember();
                    victoriesOutOMission.merge(groundVictories);
                }
                else if (missionRole == Role.ROLE_BOMB || missionRole == Role.ROLE_STRAT_BOMB)
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
