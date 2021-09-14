package pwcg.mission.flight.factory;

import pwcg.campaign.Campaign;
import pwcg.campaign.plane.PwcgRole;
import pwcg.campaign.squadron.Squadron;
import pwcg.core.exception.PWCGException;
import pwcg.mission.flight.FlightTypes;


public class FCFlightTypeSpecialFactory implements IFlightTypeFactory
{
    protected Campaign campaign;
    
    public FCFlightTypeSpecialFactory (Campaign campaign) 
    {
        this.campaign = campaign;
    }

    @Override
    public FlightTypes getFlightType(Squadron squadron, boolean isPlayerFlight, PwcgRole missionRole) throws PWCGException
    {
        return FlightTypes.ANY;
    }
}
