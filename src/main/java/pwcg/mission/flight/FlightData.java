package pwcg.mission.flight;

import java.io.BufferedWriter;

import pwcg.campaign.Campaign;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.mission.MissionSkinGenerator;
import pwcg.mission.flight.plane.PlaneMcu;
import pwcg.mission.flight.waypoint.IVirtualWaypointPackage;
import pwcg.mission.flight.waypoint.IWaypointPackage;
import pwcg.mission.flight.waypoint.VirtualWaypointPackage;
import pwcg.mission.flight.waypoint.WaypointPackage;
import pwcg.mission.ground.org.IGroundUnitCollection;

public class FlightData
{
    private IFlightInformation flightInformation;
    private IFlightPlanes flightPlanes;
    private ILinkedGroundUnits linkedGroundUnits = new LinkedGroundUnits();
    private ILinkedFlights linkedFlights = new LinkedFlights();
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
        
        writeLinkedFlights(writer);
        writeLinkedGroundUnits(writer);
    }

    private void writeLinkedFlights(BufferedWriter writer) throws PWCGException
    {
        for (IFlight flight : linkedFlights.getLinkedFlights())
        {
            flight.write(writer);
            for (IFlight linkedFlight : flight.getLinkedFlights().getLinkedFlights())
            {
                linkedFlight.write(writer);
            }
        }
    }

    private void writeLinkedGroundUnits(BufferedWriter writer) throws PWCGException
    {
        for (IGroundUnitCollection linkedGroundUnit : linkedGroundUnits.getLinkedGroundUnits())
        {
            linkedGroundUnit.write(writer);
        }
    }

    public IFlightInformation getFlightInformation()
    {
        return flightInformation;
    }

    public IFlightPlanes getFlightPlanes()
    {
        return flightPlanes;
    }

    public IWaypointPackage getWaypointPackage()
    {
        return waypointPackage;
    }

    public ILinkedGroundUnits getLinkedGroundUnits()
    {
        return linkedGroundUnits;
    }

    public void addLinkedGroundUnit(IGroundUnitCollection groundUnit)
    {
        linkedGroundUnits.addLinkedGroundUnit(groundUnit);        
    }

    public ILinkedFlights getLinkedFlights()
    {
        return linkedFlights;
    }

    public Coordinate getFlightHomePosition() throws PWCGException
    {
        return flightInformation.getFlightHomePosition();
    }

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
            virtualWaypointPackage.addDelayForPlayerDelay(flightInformation.getMission());
        }        

        MissionSkinGenerator skinGenerator = new MissionSkinGenerator();
        skinGenerator.assignSkinsForFlight(this);
    }

    public IVirtualWaypointPackage getVirtualWaypointPackage()
    {
        return virtualWaypointPackage;
    }
}
