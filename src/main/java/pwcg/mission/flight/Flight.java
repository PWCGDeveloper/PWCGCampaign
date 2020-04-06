package pwcg.mission.flight;

import java.io.BufferedWriter;

import pwcg.campaign.Campaign;
import pwcg.campaign.squadron.Squadron;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.mission.Mission;
import pwcg.mission.MissionSkinGenerator;
import pwcg.mission.flight.plane.PlaneMcu;
import pwcg.mission.flight.waypoint.IVirtualWaypointPackage;
import pwcg.mission.flight.waypoint.IWaypointPackage;
import pwcg.mission.flight.waypoint.VirtualWaypointPackage;
import pwcg.mission.flight.waypoint.WaypointPackage;
import pwcg.mission.ground.org.IGroundUnitCollection;
import pwcg.mission.target.TargetDefinition;

public abstract class Flight implements IFlight
{
    private IFlightInformation flightInformation;
    private IFlightPlanes flightPlanes;
    private ILinkedGroundUnits linkedGroundUnits = new LinkedGroundUnits();
    private ILinkedFlights linkedFlights = new LinkedFlights();
    private IFlightPlayerContact flightPlayerContact = new FlightPlayerContact();
    private IWaypointPackage waypointPackage;
    private VirtualWaypointPackage virtualWaypointPackage;
    private TargetDefinition targetDefinition = new TargetDefinition();


    public Flight(IFlightInformation flightInformation, TargetDefinition targetDefinition)
    {
        this.flightInformation = flightInformation;
        this.targetDefinition = targetDefinition;
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
    public void finalizeFlight() throws PWCGException
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
    

    @Override
    public Mission getMission()
    {
        return flightInformation.getMission();
    }

    @Override
    public int getFlightId()
    {
        return flightInformation.getFlightId();
    }

    @Override
    public Squadron getSquadron()
    {
        return flightInformation.getSquadron();
    }

    @Override
    public FlightTypes getFlightType()
    {
        return flightInformation.getFlightType();
    }

    @Override
    public boolean isPlayerFlight()
    {
        return flightInformation.isPlayerFlight();
    }

    @Override
    public boolean isFlightHasFighterPlanes()
    {
        return flightPlanes.isFlightHasFighterPlanes();
    }

    @Override
    public double getClosestContactWithPlayerDistance()
    {
        return flightPlayerContact.getClosestContactWithPlayerDistance();
    }

    public TargetDefinition getTargetDefinition()
    {
        return targetDefinition;
    }

    public void setTargetDefinition(TargetDefinition targetDefinition)
    {
        this.targetDefinition = targetDefinition;
    }
}
