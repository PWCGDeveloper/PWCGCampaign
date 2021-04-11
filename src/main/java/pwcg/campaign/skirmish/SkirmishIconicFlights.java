package pwcg.campaign.skirmish;

import pwcg.campaign.api.Side;
import pwcg.mission.flight.FlightTypes;

public class SkirmishIconicFlights
{
    private FlightTypes flightType;
    private Side side;
    private int maxForcedFlightTypes;

    public FlightTypes getFlightType()
    {
        return flightType;
    }

    public int getMaxForcedFlightTypes()
    {
        return maxForcedFlightTypes;
    }

    public Side getSide()
    {
        return side;
    }
}
