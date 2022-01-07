package pwcg.mission.flight;

import java.io.BufferedWriter;
import java.io.IOException;

import pwcg.campaign.Campaign;
import pwcg.campaign.api.ICountry;
import pwcg.campaign.utils.IndexGenerator;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.PWCGLogger;
import pwcg.mission.Mission;
import pwcg.mission.flight.waypoint.IWaypointPackage;
import pwcg.mission.flight.waypoint.WaypointPackage;
import pwcg.mission.flight.waypoint.virtual.IVirtualWaypointPackage;
import pwcg.mission.flight.waypoint.virtual.VirtualWaypointPackage;
import pwcg.mission.target.TargetDefinition;

public abstract class Flight implements IFlight
{
    private FlightInformation flightInformation;
    private FlightPlanes flightPlanes;
    private IWaypointPackage waypointPackage;
    private VirtualWaypointPackage virtualWaypointPackage;
    private TargetDefinition targetDefinition;
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

            writer.write("  Name = \"Flight " + flightInformation.getFlightName() + "\";");
            writer.newLine();
            writer.write("  Index = " + index + ";");
            writer.newLine();
            writer.write("  Desc = \"Flight " + flightInformation.getFlightName() + "\";");
            writer.newLine();

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

            writer.newLine();

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

    public Campaign getCampaign()
    {
        return flightInformation.getCampaign();
    }

    @Override
    public void finalizeFlight() throws PWCGException
    {
        finalizeCoreFlight();        
        finalizeVirtualFlight();        
    }

    private void finalizeCoreFlight() throws PWCGException
    {
        flightPlanes.finalize();
        waypointPackage.finalize(flightPlanes);
    }

    private void finalizeVirtualFlight() throws PWCGException
    {
        if (flightInformation.isVirtual())
        {
            virtualWaypointPackage.buildVirtualWaypoints();
            virtualWaypointPackage.addDelayForPlayerDelay(flightInformation.getMission());
         }
    }

    public IVirtualWaypointPackage getVirtualWaypointPackage()
    {
        return virtualWaypointPackage;
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
    public FlightTypes getFlightType()
    {
        return flightInformation.getFlightType();
    }
    

    @Override
    public ICountry getCountry()
    {
        return flightInformation.getCountry();
    }

    @Override
    public TargetDefinition getTargetDefinition()
    {
        return targetDefinition;
    }    
}
