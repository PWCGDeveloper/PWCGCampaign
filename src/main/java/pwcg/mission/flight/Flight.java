package pwcg.mission.flight;

import java.io.BufferedWriter;

import pwcg.campaign.Campaign;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.mission.Mission;
import pwcg.mission.flight.waypoint.IVirtualWaypointPackage;
import pwcg.mission.flight.waypoint.IWaypointPackage;

public abstract class Flight implements IFlight
{
    private FlightData flightData;

    @Override
    public abstract void createFlight() throws PWCGException;
 
    public Flight(IFlightInformation flightInformation)
    {
        flightData = new FlightData(flightInformation);
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
    public void finalizeFlight() throws PWCGException
    {
        flightData.finalize();
    }
    

    @Override
    public IFlightInformation getFlightInformation()
    {
        return flightData.getFlightInformation();
    }

    @Override
    public IFlightPlanes getFlightPlanes()
    {
        return flightData.getFlightPlanes();
    }

    @Override
    public IWaypointPackage getWaypointPackage()
    {
        return flightData.getWaypointPackage();
    }

    @Override
    public ILinkedGroundUnits getLinkedGroundUnits()
    {
        return flightData.getLinkedGroundUnits();
    }

    @Override
    public ILinkedFlights getLinkedFlights()
    {
        return flightData.getLinkedFlights();
    }

    @Override
    public Coordinate getFlightHomePosition() throws PWCGException
    {
        return flightData.getFlightHomePosition();
    }

    @Override
    public IFlightPlayerContact getFlightPlayerContact()
    {
        return flightData.getFlightPlayerContact();
    }

    @Override
    public void initialize(IFlight flight) throws PWCGException
    {
        flightData.initialize(flight);        
    }

    @Override
    public IVirtualWaypointPackage getVirtualWaypointPackage()
    {
        return flightData.getVirtualWaypointPackage();
    }

}
