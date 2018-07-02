package pwcg.mission.briefing;

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
        
        SquadronMembers squadronMembers = mission.getCampaign().getPersonnelManager().getSquadronPersonnel(mission.getCampaign().getSquadronId()).getActiveSquadronMembersWithAces();
        for (SquadronMember squadronMember : squadronMembers.getSquadronMembers().values())
        {
            briefingAssignmentData.addPilot(squadronMember);
        }
        
        Equipment squadronPlanes = mission.getCampaign().getEquipmentManager().getEquipmentForSquadron(mission.getCampaign().getSquadronId());
        for (EquippedPlane squadronPlane : squadronPlanes.getEquippedPlanes().values())
        {
            briefingAssignmentData.addPlane(squadronPlane);
        }
	    
        Flight playerFlight = mission.getMissionFlightBuilder().getPlayerFlight();
	    for (PlaneMCU plane : playerFlight.getPlanes())
	    {
	        briefingAssignmentData.assignPilot(plane.getPilot().getSerialNumber(), plane.getSerialNumber());
	    }
	}
}
