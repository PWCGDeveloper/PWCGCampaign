package pwcg.mission.data;

import java.util.ArrayList;
import java.util.List;

import pwcg.campaign.squadron.Company;
import pwcg.core.exception.PWCGException;
import pwcg.mission.Mission;
import pwcg.mission.flight.IFlight;
import pwcg.mission.flight.plane.PlaneMcu;

public class MissionPlaneGenerator
{
    private Mission mission;
    private List<PwcgGeneratedMissionPlaneData> missionPlanes = new ArrayList<>();
    
    public MissionPlaneGenerator(Mission mission)
    {
        this.mission = mission;
    }
    
    public List<PwcgGeneratedMissionPlaneData> generateMissionPlaneData() throws PWCGException
    {
        for (IFlight flight : mission.getFlights().getAllAerialFlights())
        {
            makePlaneEntriesForFlight(flight);
        }
        
        return missionPlanes;
    }

    private void makePlaneEntriesForFlight(IFlight flight)
    {
        for (PlaneMcu plane : flight.getFlightPlanes().getPlanes())
        {
            makeMissionPlaneEntry(flight.getSquadron(), plane);
        }
    }

    private void makeMissionPlaneEntry(Company squadron, PlaneMcu plane)
    {
        PwcgGeneratedMissionPlaneData missionPlaneData = new PwcgGeneratedMissionPlaneData();
        missionPlaneData.setCrewMemberName(plane.getCrewMember().getName());
        missionPlaneData.setCrewMemberSerialNumber(plane.getCrewMember().getSerialNumber());
        missionPlaneData.setPlaneSerialNumber(plane.getSerialNumber());
        missionPlaneData.setSquadronId(squadron.getSquadronId());
        missionPlaneData.setAircraftType(plane.getType());
        
        missionPlanes.add(missionPlaneData);
    }
}
