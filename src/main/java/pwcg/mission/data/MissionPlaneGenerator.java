package pwcg.mission.data;

import java.util.ArrayList;
import java.util.List;

import pwcg.core.exception.PWCGException;
import pwcg.mission.Mission;
import pwcg.mission.flight.IFlight;
import pwcg.mission.flight.plane.PlaneMcu;

public class MissionPlaneGenerator
{
    public static List<PwcgGeneratedMissionPlaneData> generateMissionPlaneData(Mission mission) throws PWCGException
    {
        List<PwcgGeneratedMissionPlaneData> missionPlanes  = new ArrayList<>();

        for (IFlight flight : mission.getMissionFlightBuilder().getAllAerialFlights())
        {
            for (PlaneMcu plane : flight.getFlightPlanes().getPlanes())
            {
                makeMissionPlaneEntry(missionPlanes, flight, plane);
            }
            
            for (PlaneMcu plane : flight.getFlightPlanes().getEascortsPlanes())
            {
                makeMissionPlaneEntry(missionPlanes, flight, plane);
            }
        }
        
        return missionPlanes;
    }

    private static void makeMissionPlaneEntry(List<PwcgGeneratedMissionPlaneData> missionPlanes, IFlight flight, PlaneMcu plane)
    {
        PwcgGeneratedMissionPlaneData missionPlaneData = new PwcgGeneratedMissionPlaneData();
        missionPlaneData.setPilotName(plane.getPilot().getName());
        missionPlaneData.setPilotSerialNumber(plane.getPilot().getSerialNumber());
        missionPlaneData.setPlaneSerialNumber(plane.getSerialNumber());
        missionPlaneData.setSquadronId(flight.getSquadron().getSquadronId());
        missionPlaneData.setAircraftType(plane.getType());
        
        missionPlanes.add(missionPlaneData);
    }
}
