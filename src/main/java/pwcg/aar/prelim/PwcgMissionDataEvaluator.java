package pwcg.aar.prelim;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import pwcg.campaign.Campaign;
import pwcg.campaign.api.Side;
import pwcg.campaign.company.Company;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.crewmember.CrewMember;
import pwcg.core.exception.PWCGException;
import pwcg.mission.data.PwcgGeneratedMissionVehicleData;
import pwcg.mission.ground.vehicle.VehicleDefinitionManager;

public class PwcgMissionDataEvaluator
{
    private Campaign campaign;
    private AARPreliminaryData aarPreliminarytData;

    public PwcgMissionDataEvaluator (Campaign campaign, AARPreliminaryData aarPreliminarytData) throws PWCGException
    {
        this.campaign = campaign;
        this.aarPreliminarytData = aarPreliminarytData;
    }

    public List<String> determineAxisTankTypesInMission() throws PWCGException
    {
        return determineTankTypesForSide(Side.AXIS);
    }

    public List<String> determineAlliedTankTypesInMission() throws PWCGException
    {
        return determineTankTypesForSide(Side.ALLIED);
    }

    private List<String> determineTankTypesForSide(Side side) throws PWCGException
    {
        Set<String> uniquePlanesForSide = new HashSet<>();

        for (PwcgGeneratedMissionVehicleData missionPlane : aarPreliminarytData.getPwcgMissionData().getMissionPlanes().values())
        {
            Company squadron = PWCGContext.getInstance().getCompanyManager().getCompany(missionPlane.getCompanyId());            
            if (squadron.determineSquadronCountry(campaign.getDate()).getSide() == side)
            {
                uniquePlanesForSide.add(missionPlane.getVehicleType());
            }
        }

        return new ArrayList<String>(uniquePlanesForSide);
    }

    public PwcgGeneratedMissionVehicleData getPlaneForCrewMemberBySerialNumber(Integer serialNumber) throws PWCGException
    {
        return aarPreliminarytData.getPwcgMissionData().getMissionPlane(serialNumber);
    }
    
    public PwcgGeneratedMissionVehicleData getPlaneForCrewMemberByName(String name) throws PWCGException
    {
        List<PwcgGeneratedMissionVehicleData> missionPlanes = new ArrayList<>(aarPreliminarytData.getPwcgMissionData().getMissionPlanes().values());
        for (PwcgGeneratedMissionVehicleData missionPlane : missionPlanes)
        {
            CrewMember crewMember = campaign.getPersonnelManager().getAnyCampaignMember(missionPlane.getCrewMemberSerialNumber());
            if (crewMember.isCrewMemberName(name))
            {
                return missionPlane;
            }
        }
        
        throw new PWCGException("No missionPlane found for name " + name);
    }

    public boolean wasCrewMemberAssignedToMission(Integer serialNumber) throws PWCGException
    {
        for (CrewMember crewMemberInMission : aarPreliminarytData.getCampaignMembersInMission().getCrewMemberCollection().values())
        {
            if (crewMemberInMission.getSerialNumber() == serialNumber)
            {
                return true;
            }
        }

        return false;
    }

    public boolean wasCrewMemberAssignedToMissionByName(String destroyedEntityName) throws PWCGException
    {
        if (VehicleDefinitionManager.isLocomotive(destroyedEntityName))
        {
            return false;
        }

        for (CrewMember crewMemberInMission : aarPreliminarytData.getCampaignMembersInMission().getCrewMemberCollection().values())
        {
            if (destroyedEntityName == null)
            {
                return false;
            }
            
            if (crewMemberInMission.getName() == null)
            {
                return false;
            }
            
            if (crewMemberInMission.isCrewMemberName(destroyedEntityName))
            {
                return true;
            }
        }

        return false;
    }
}
