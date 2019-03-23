package pwcg.mission.briefing;

import pwcg.campaign.context.PWCGContextManager;
import pwcg.campaign.personnel.SquadronMemberFilter;
import pwcg.campaign.personnel.SquadronPersonnel;
import pwcg.campaign.plane.Equipment;
import pwcg.campaign.plane.EquippedPlane;
import pwcg.campaign.squadmember.SquadronMember;
import pwcg.campaign.squadmember.SquadronMembers;
import pwcg.core.exception.PWCGException;
import pwcg.mission.Mission;
import pwcg.mission.flight.Flight;
import pwcg.mission.flight.plane.PlaneMCU;

public class BriefingDataInitializer
{
    private Mission mission;
    private BriefingAssignmentData briefingAssignmentData = new BriefingAssignmentData();
    
	public BriefingDataInitializer(Mission mission, BriefingAssignmentData briefingAssignmentData)
	{
        this.mission = mission;
        this.briefingAssignmentData = briefingAssignmentData;
	}
	
	public void initializeFromMission() throws PWCGException
	{	    
	    briefingAssignmentData.reset();
        
	    SquadronMember referencePlayer = PWCGContextManager.getInstance().getReferencePlayer();
	    SquadronPersonnel playerPersonnel = mission.getCampaign().getPersonnelManager().getSquadronPersonnel(referencePlayer.getSquadronId());
        SquadronMembers squadronMembers = SquadronMemberFilter.filterActiveAIAndPlayerAndAcesNoWounded(playerPersonnel.getSquadronMembersWithAces().getSquadronMemberCollection(), (mission.getCampaign().getDate()));
        for (SquadronMember squadronMember : squadronMembers.getSquadronMemberCollection().values())
        {
            briefingAssignmentData.addPilot(squadronMember);
        }
        
        Equipment squadronPlanes = mission.getCampaign().getEquipmentManager().getEquipmentForSquadron(referencePlayer.getSquadronId());
        for (EquippedPlane squadronPlane : squadronPlanes.getActiveEquippedPlanes().values())
        {
            briefingAssignmentData.addPlane(squadronPlane);
        }
	    
        Flight playerFlight = mission.getMissionFlightBuilder().getPlayerFlight(referencePlayer);
	    for (PlaneMCU plane : playerFlight.getPlanes())
	    {
	        briefingAssignmentData.assignPilot(plane.getPilot().getSerialNumber(), plane.getSerialNumber());
	    }
	}
}
