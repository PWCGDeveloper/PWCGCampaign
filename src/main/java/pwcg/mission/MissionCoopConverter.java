package pwcg.mission;

import pwcg.core.exception.PWCGException;
import pwcg.mission.flight.IFlight;
import pwcg.mission.options.MissionOptions;


public class MissionCoopConverter
{

    public void convertToCoop(Mission mission) throws PWCGException 
    {
        MissionOptions missionOptions = mission.getMissionOptions();
        missionOptions.setMissionType(MissionOptions.COOP_MISSION);

        for (IFlight flight : mission.getMissionFlightBuilder().getAllAerialFlights())
        {
            flight.getFlightPlanes().preparePlaneForCoop(flight);
        }
    }
}
