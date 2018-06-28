package pwcg.mission.flight.factory;

import java.util.Date;

import pwcg.campaign.Campaign;
import pwcg.campaign.squadron.Squadron;
import pwcg.campaign.utils.TestDriver;
import pwcg.core.exception.PWCGException;
import pwcg.mission.flight.FlightTypes;

public class TestFlightFactory extends FlightFactory
{
    public TestFlightFactory (Campaign campaign)
    {
        super(campaign);
    }
    
    public FlightTypes getActualFlightType(Squadron squadron, Date date, boolean isPlayerFlight) 
                    throws PWCGException
    {
        return TestDriver.getInstance().getTestFlightType(isPlayerFlight);
    }
}
