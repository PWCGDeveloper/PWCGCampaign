package pwcg.mission;

import pwcg.core.exception.PWCGException;
import pwcg.mission.flight.IFlight;
import pwcg.mission.options.MissionOptions;
import pwcg.mission.options.MissionType;


public class MissionCoopConverter
{

    public void convertToCoop(Mission mission) throws PWCGException 
    {
        MissionOptions missionOptions = mission.getMissionOptions();
        missionOptions.setMissionType(MissionType.COOP_MISSION);

        for (IFlight flight : mission.getFlights().getAllAerialFlights())
        {
            flight.getFlightPlanes().preparePlaneForCoop(flight);
        }
    }
}
