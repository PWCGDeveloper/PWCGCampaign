package pwcg.mission.flight;

import java.io.BufferedWriter;
import java.io.IOException;

import pwcg.campaign.Campaign;
import pwcg.campaign.squadron.Squadron;
import pwcg.campaign.utils.IndexGenerator;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.utils.PWCGLogger;
import pwcg.mission.Mission;
import pwcg.mission.flight.initialposition.FlightPositionSetter;
import pwcg.mission.flight.waypoint.IWaypointPackage;
import pwcg.mission.flight.waypoint.WaypointPackage;
import pwcg.mission.flight.waypoint.virtual.IVirtualWaypointPackage;
import pwcg.mission.flight.waypoint.virtual.VirtualWaypointPackage;
import pwcg.mission.skin.MissionSkinGenerator;
import pwcg.mission.target.TargetDefinition;

public abstract class Flight implements IFlight
{
    private FlightInformation flightInformation;
    private FlightPlanes flightPlanes;
    private FlightPlayerContact flightPlayerContact = new FlightPlayerContact();
    private IWaypointPackage waypointPackage;
    private VirtualWaypointPackage virtualWaypointPackage;
    private TargetDefinition targetDefinition;
    private IFlight associatedFlight = null;
    private int index = IndexGenerator.getInstance().getNextIndex();

    public Flight(FlightInformation flightInformation, TargetDefinition targetDefinition)
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


    public void createFlightCommonPostBuild() throws PWCGException
    {
        FlightPositionSetter.setFlightInitialPosition(this);
    }

    public void setFlightPayload() throws PWCGException
    {
        FlightPayloadBuilder flightPayloadHelper = new FlightPayloadBuilder(this);
        flightPayloadHelper.setFlightPayload();
    }

    public void write(BufferedWriter writer) throws PWCGException 
    {
        try
        {
            writer.write("Group");
            writer.newLine();
            writer.write("{");
            writer.newLine();

            writer.write("  Name = \"Flight " + flightInformation.getSquadron().determineDisplayName(getCampaign().getDate()) + "\";");
            writer.newLine();
            writer.write("  Index = " + index + ";");
            writer.newLine();
            writer.write("  Desc = \"Flight " + flightInformation.getSquadron().determineDisplayName(getCampaign().getDate()) + "\";");
            writer.newLine();

            waypointPackage.write(writer);
            if (flightInformation.isVirtual())
            {
                virtualWaypointPackage.write(writer);
            }
            else
            {
                flightPlanes.write(writer);
            }

            writer.write("}");
            writer.newLine();
        }
        catch (IOException e)
        {
            PWCGLogger.logException(e);
            throw new PWCGException(e.getMessage());
        }
    }

    public FlightInformation getFlightInformation()
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
        finalizeCoreFlight();        
        finalizeWingmenForFlight();
        finalizeSkinsForFlight();
        finalizeVirtualFlight();        
    }

    @Override
    public void addVirtualEscort() throws PWCGException
    {
        virtualWaypointPackage.addEscort();
    }

    private void finalizeCoreFlight() throws PWCGException
    {
        flightPlanes.finalize();
        waypointPackage.finalize(flightPlanes);
    }

    private void finalizeWingmenForFlight() throws PWCGException
    {
        //WingmanBuilder wingmanBuilder = new WingmanBuilder(this);
        //wingmanBuilder.buildWingmenForFlight();
    }

    private void finalizeVirtualFlight() throws PWCGException
    {
        if (flightInformation.isVirtual())
        {
            virtualWaypointPackage.buildVirtualWaypoints();
            virtualWaypointPackage.addDelayForPlayerDelay(flightInformation.getMission());
         }
    }

    private void finalizeSkinsForFlight() throws PWCGException
    {
        MissionSkinGenerator.assignSkinsForFlight(this);
    }

    public IVirtualWaypointPackage getVirtualWaypointPackage()
    {
        return virtualWaypointPackage;
    }

    @Override
    public void overrideFlightCruisingSpeedForEscort(int cruisingSpeed)
    {
        flightInformation.setCruisingSpeed(cruisingSpeed);
    }

    @Override
    public int getFlightCruisingSpeed()
    {
        if (flightInformation.getFlightCruisingSpeed() > 0)
        {
            return flightInformation.getFlightCruisingSpeed();
        }
        else
        {
            return flightPlanes.getFlightCruisingSpeed();
        }
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
    public double getClosestContactWithPlayerDistance()
    {
        return flightPlayerContact.getClosestContactWithPlayerDistance();
    }

    @Override
    public TargetDefinition getTargetDefinition()
    {
        return targetDefinition;
    }    

    @Override
    public void setAssociatedFlight(IFlight associatedFlight)
    {
        this.associatedFlight = associatedFlight;;
    }    

    @Override
    public IFlight getAssociatedFlight()
    {
        return associatedFlight;
    }    
}
