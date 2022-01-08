package pwcg.gui.rofmap.brief;

import pwcg.campaign.company.Company;
import pwcg.campaign.crewmember.CrewMember;
import pwcg.campaign.crewmember.CrewMembers;
import pwcg.campaign.personnel.CompanyPersonnel;
import pwcg.campaign.personnel.CrewMemberFilter;
import pwcg.campaign.tank.Equipment;
import pwcg.campaign.tank.EquippedTank;
import pwcg.core.exception.PWCGException;
import pwcg.gui.rofmap.brief.model.BriefingCrewMemberAssignmentData;
import pwcg.mission.Mission;
import pwcg.mission.playerunit.PlayerUnit;
import pwcg.mission.playerunit.TankMcu;

public class BriefingDataInitializer
{
    private Mission mission;
    
	public BriefingDataInitializer(Mission mission)
	{
        this.mission = mission;
	}
	
	public BriefingCrewMemberAssignmentData initializeFromMission(Company company) throws PWCGException
	{	    
	    BriefingCrewMemberAssignmentData briefingAssignmentData = new BriefingCrewMemberAssignmentData();
	    briefingAssignmentData.setSquadron(company);
        
	    CompanyPersonnel playerPersonnel = mission.getCampaign().getPersonnelManager().getCompanyPersonnel(company.getCompanyId());
        CrewMembers companyMembers = CrewMemberFilter.filterActiveAIAndPlayerAndAcesNoWounded(
        		playerPersonnel.getCrewMembersWithAces().getCrewMemberCollection(), (mission.getCampaign().getDate()));
        for (CrewMember crewMember : companyMembers.getCrewMemberCollection().values())
        {
            briefingAssignmentData.addCrewMember(crewMember);
        }
        
        Equipment companyPlanes = mission.getCampaign().getEquipmentManager().getEquipmentForCompany(company.getCompanyId());
        for (EquippedTank companyPlane : companyPlanes.getActiveEquippedTanks().values())
        {
            briefingAssignmentData.addPlane(companyPlane);
        }
	    
        PlayerUnit playerUnit = mission.getUnits().getPlayerUnitForCompany(company.getCompanyId());
	    for (TankMcu tank : playerUnit.getUnitTanks().getTanks())
	    {
	        briefingAssignmentData.assignCrewMember(tank.getCrewMember().getSerialNumber(), tank.getSerialNumber());
	    }
	    
	    return briefingAssignmentData;
	}
}
