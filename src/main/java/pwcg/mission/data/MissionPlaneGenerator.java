package pwcg.mission.data;

import java.util.ArrayList;
import java.util.List;

import pwcg.core.exception.PWCGException;
import pwcg.mission.Mission;
import pwcg.mission.flight.Flight;
import pwcg.mission.flight.plane.PlaneMCU;

public class MissionPlaneGenerator
{
    public List<PwcgGeneratedMissionPlaneData> generateMissionPlaneData(Mission mission) throws PWCGException
    {
        List<PwcgGeneratedMissionPlaneData> missionPlanes  = new ArrayList<>();

        for (Flight flight : mission.getMissionFlightBuilder().getAllAerialFlights())
        {
            for (PlaneMCU plane : flight.getPlanes())
            {
                PwcgGeneratedMissionPlaneData missionPlaneData = new PwcgGeneratedMissionPlaneData();
                missionPlaneData.setPilotName(plane.getPilot().getName());
                missionPlaneData.setPilotSerialNumber(plane.getPilot().getSerialNumber());
                missionPlaneData.setSquadronId(flight.getSquadron().getSquadronId());
                missionPlaneData.setAircraftType(plane.getType());
                
                missionPlanes.add(missionPlaneData);
            }
        }
        
        return missionPlanes;
    }
}
