package pwcg.mission.data;

import java.util.ArrayList;
import java.util.List;

import pwcg.campaign.company.Company;
import pwcg.core.exception.PWCGException;
import pwcg.mission.Mission;
import pwcg.mission.playerunit.PlayerUnit;
import pwcg.mission.playerunit.PlayerVehicleMcu;

public class MissionPlaneGenerator
{
    private Mission mission;
    private List<PwcgGeneratedMissionVehicleData> missionPlanes = new ArrayList<>();
    
    public MissionPlaneGenerator(Mission mission)
    {
        this.mission = mission;
    }
    
    public List<PwcgGeneratedMissionVehicleData> generateMissionPlaneData() throws PWCGException
    {
        for (PlayerUnit unit : mission.getPlayerUnits().getPlayerUnits())
        {
            makePlaneEntriesForUnit(unit);
        }
        
        return missionPlanes;
    }

    private void makePlaneEntriesForUnit(PlayerUnit unit)
    {
        for (PlayerVehicleMcu vehicle : unit.getVehicles())
        {
            makeMissionPlaneEntry(unit.getCompany(), vehicle);
        }
    }

    private void makeMissionPlaneEntry(Company squadron, PlayerVehicleMcu vehicle)
    {
        PwcgGeneratedMissionVehicleData missionPlaneData = new PwcgGeneratedMissionVehicleData();
        missionPlaneData.setCrewMemberName(vehicle.getCrewMember().getNameAndRank());
        missionPlaneData.setCrewMemberSerialNumber(vehicle.getCrewMember().getSerialNumber());
        missionPlaneData.setVehicleSerialNumber(vehicle.getSerialNumber());
        missionPlaneData.setCompanyId(squadron.getCompanyId());
        missionPlaneData.setVehicleType(vehicle.getType());
        
        missionPlanes.add(missionPlaneData);
    }
}
