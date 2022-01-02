package pwcg.gui.rofmap.brief;

import pwcg.campaign.crewmember.CrewMember;
import pwcg.campaign.crewmember.CrewMembers;
import pwcg.campaign.personnel.CompanyPersonnel;
import pwcg.campaign.personnel.CrewMemberFilter;
import pwcg.campaign.plane.Equipment;
import pwcg.campaign.plane.EquippedPlane;
import pwcg.campaign.squadron.Company;
import pwcg.core.exception.PWCGException;
import pwcg.gui.rofmap.brief.model.BriefingCrewMemberAssignmentData;
import pwcg.mission.Mission;
import pwcg.mission.flight.IFlight;
import pwcg.mission.flight.plane.PlaneMcu;

public class BriefingDataInitializer
{
    private Mission mission;
    
	public BriefingDataInitializer(Mission mission)
	{
        this.mission = mission;
	}
	
	public BriefingCrewMemberAssignmentData initializeFromMission(Company squadron) throws PWCGException
	{	    
	    BriefingCrewMemberAssignmentData briefingAssignmentData = new BriefingCrewMemberAssignmentData();
	    briefingAssignmentData.setSquadron(squadron);
        
	    CompanyPersonnel playerPersonnel = mission.getCampaign().getPersonnelManager().getCompanyPersonnel(squadron.getSquadronId());
        CrewMembers squadronMembers = CrewMemberFilter.filterActiveAIAndPlayerAndAcesNoWounded(
        		playerPersonnel.getCrewMembersWithAces().getCrewMemberCollection(), (mission.getCampaign().getDate()));
        for (CrewMember crewMember : squadronMembers.getCrewMemberCollection().values())
        {
            briefingAssignmentData.addCrewMember(crewMember);
        }
        
        Equipment squadronPlanes = mission.getCampaign().getEquipmentManager().getEquipmentForSquadron(squadron.getSquadronId());
        for (EquippedPlane squadronPlane : squadronPlanes.getActiveEquippedPlanes().values())
        {
            briefingAssignmentData.addPlane(squadronPlane);
        }
	    
        IFlight playerFlight = mission.getFlights().getPlayerFlightForSquadron(squadron.getSquadronId());
	    for (PlaneMcu plane : playerFlight.getFlightPlanes().getPlanes())
	    {
	        briefingAssignmentData.assignCrewMember(plane.getCrewMember().getSerialNumber(), plane.getSerialNumber());
	    }
	    
	    return briefingAssignmentData;
	}
}
