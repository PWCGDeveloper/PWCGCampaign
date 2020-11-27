package pwcg.mission.flight.factory;

import pwcg.campaign.Campaign;
import pwcg.campaign.squadron.Squadron;
import pwcg.core.exception.PWCGException;
import pwcg.mission.flight.FlightTypes;

public class FCFlightTypeCoopFactory implements IFlightTypeFactory
{
    private Campaign campaign;
    
    public FCFlightTypeCoopFactory (Campaign campaign) 
    {
        this.campaign = campaign;
    }

    @Override
    public FlightTypes getFlightType(Squadron squadron, boolean isPlayerFlight) throws PWCGException
    {
        FCFlightTypeFactory fcFlightTypeFactory = new FCFlightTypeFactory(campaign);
        return fcFlightTypeFactory.getFlightType(squadron, isPlayerFlight);
    }
}
