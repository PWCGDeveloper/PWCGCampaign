package pwcg.mission;

import pwcg.campaign.context.PWCGContext;
import pwcg.core.exception.PWCGException;
import pwcg.mission.flight.IFlight;
import pwcg.mission.options.MissionOptions;


public class MissionCoopConverter
{

    public void convertToCoop(MissionFlightBuilder missionFlightBuilder) throws PWCGException 
    {
        MissionOptions missionOptions = PWCGContext.getInstance().getCurrentMap().getMissionOptions();
        missionOptions.setMissionType(MissionOptions.COOP_MISSION);

        for (IFlight flight : missionFlightBuilder.getAllAerialFlights())
        {
            flight.getFlightData().getFlightPlanes().preparePlaneForCoop(flight);
        }
    }
}
