package pwcg.mission.flight.factory;

import pwcg.campaign.Campaign;
import pwcg.campaign.squadron.Squadron;
import pwcg.core.exception.PWCGException;
import pwcg.mission.flight.FlightTypes;


public class RoFFlightTypeSpecialFactory implements IFlightTypeFactory
{
    protected Campaign campaign;
    
    public RoFFlightTypeSpecialFactory (Campaign campaign) 
    {
        this.campaign = campaign;
    }

    @Override
    public FlightTypes getFlightType(Squadron squadron, boolean isMyFlight) throws PWCGException
    {
        return FlightTypes.ANY;
    }
}
