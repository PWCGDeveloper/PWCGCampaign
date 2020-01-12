package pwcg.mission.flight;

import java.io.BufferedWriter;

import pwcg.campaign.Campaign;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.mission.Mission;

public abstract class Flight implements IFlight
{
    protected IFlightData flightData;

    @Override
    public abstract void createFlight() throws PWCGException;
 
    public Flight(IFlightInformation flightInformation)
    {
        flightData = new FlightData(flightInformation);
    }

    @Override
    public IFlightData getFlightData()
    {
        return flightData;
    }

    @Override
    public Mission getMission()
    {
        return flightData.getFlightInformation().getMission();
    }

    @Override
    public Campaign getCampaign()
    {
        return flightData.getFlightInformation().getCampaign();
    }

    @Override
    public void write(BufferedWriter writer) throws PWCGException
    {
        flightData.write(writer);
    }

    @Override
    public Coordinate getTargetPosition()
    {
        return flightData.getFlightInformation().getTargetPosition();
    }

    @Override
    public void finalizeFlight() throws PWCGException
    {
        flightData.getWaypointPackage().finalize();
    }
}
