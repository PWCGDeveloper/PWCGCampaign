package pwcg.mission.flight;

import java.io.BufferedWriter;

import pwcg.campaign.Campaign;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.mission.flight.plane.PlaneMcu;
import pwcg.mission.flight.waypoint.IVirtualWaypointPackage;
import pwcg.mission.flight.waypoint.IWaypointPackage;
import pwcg.mission.flight.waypoint.VirtualWaypointPackage;
import pwcg.mission.flight.waypoint.WaypointPackage;

public class FlightData implements IFlightData
{
    private IFlightInformation flightInformation;
    
    private IFlightPlanes flightPlanes;
    private ILinkedGroundUnits linkedGroundUnits = new LinkedGroundUnits();
    private ILinkedFLights linkedFlights = new LinkedFlights();
    private IFlightPlayerContact flightPlayerContact = new FlightPlayerContact();
    private IWaypointPackage waypointPackage;
    private VirtualWaypointPackage virtualWaypointPackage;


    public FlightData(IFlightInformation flightInformation)
    {
        this.flightInformation = flightInformation;
    }
    
    public void initialize(IFlight flight) throws PWCGException
    {
        this.flightPlanes = new FlightPlanes(flight);
        this.waypointPackage = new WaypointPackage(flight);
        this.virtualWaypointPackage = new VirtualWaypointPackage(flight);
    }

    @Override
    public void write(BufferedWriter writer) throws PWCGException 
    {
        flightPlanes.write(writer);
        if (flightInformation.isVirtual())
        {
            virtualWaypointPackage.write(writer);
        }
        else
        {
            waypointPackage.write(writer);
        }
    }

    @Override
    public IFlightInformation getFlightInformation()
    {
        return flightInformation;
    }

    @Override
    public IFlightPlanes getFlightPlanes()
    {
        return flightPlanes;
    }

    @Override
    public IWaypointPackage getWaypointPackage()
    {
        return waypointPackage;
    }

    @Override
    public ILinkedGroundUnits getLinkedGroundUnits()
    {
        return linkedGroundUnits;
    }

    @Override
    public ILinkedFLights getLinkedFlights()
    {
        return linkedFlights;
    }

    @Override
    public Coordinate getFlightHomePosition() throws PWCGException
    {
        return flightInformation.getFlightHomePosition();
    }

    @Override
    public Campaign getCampaign()
    {
        return flightInformation.getCampaign();
    }

    public IFlightPlayerContact getFlightPlayerContact()
    {
        return flightPlayerContact;
    }

    @Override
    public void finalize() throws PWCGException
    {
        flightPlanes.finalize();
        PlaneMcu flightLeader = flightPlanes.getFlightLeader();
        waypointPackage.finalize(flightLeader);
        
        for (IFlight linkedFlight : linkedFlights.getLinkedFlights())
        {
            linkedFlight.finalizeFlight();
        }

        if (flightInformation.isVirtual())
        {
            virtualWaypointPackage.buildVirtualWaypoints();                    
        }
    }

    @Override
    public IVirtualWaypointPackage getVirtualWaypointPackage()
    {
        return virtualWaypointPackage;
    }
}
