package pwcg.mission.mcu.group.virtual;

import java.io.BufferedWriter;
import java.io.IOException;

import pwcg.campaign.utils.IndexGenerator;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.utils.PWCGLogger;
import pwcg.mission.flight.FlightTypes;
import pwcg.mission.flight.IFlight;
import pwcg.mission.flight.plane.PlaneMcu;
import pwcg.mission.flight.waypoint.WaypointAction;
import pwcg.mission.flight.waypoint.virtual.VirtualWayPointCoordinate;
import pwcg.mission.mcu.BaseFlightMcu;
import pwcg.mission.mcu.McuAttack;
import pwcg.mission.mcu.McuAttackArea;
import pwcg.mission.mcu.McuWaypoint;

public final class VirtualWaypoint implements IVirtualWaypoint 
{       
    private IFlight flight;
    private VirtualWayPointCoordinate vwpCoordinate;
    private int index = IndexGenerator.getInstance().getNextIndex();
    private int vwpIdentifier = 1;
    private boolean shouldLinkToAttack = false;

    private VirtualWaypointPlanes vwpPlanes;
    private VirtualWaypointEscort vwpEscort;
    
    private VirtualWaypointCheckZone vwpCheckZone;
    private VirtualWaypointTriggered vwpTriggered;

    public static int VWP_TRIGGGER_DISTANCE = 20000;
    
    public VirtualWaypoint(IFlight flight, VirtualWayPointCoordinate vwpCoordinate, int vwpIdentifier)
    {
        index = IndexGenerator.getInstance().getNextIndex();

        this.flight = flight; 
        this.vwpCoordinate = vwpCoordinate; 
        this.vwpIdentifier = vwpIdentifier; 
    }

    @Override
    public void build() throws PWCGException 
    {
        determineShouldCarryOrdnance();
        buildElements();
        linkTriggeredElements();
    }

    private void determineShouldCarryOrdnance()
    {
        if (FlightTypes.isFlightWithTargetArea(flight.getFlightType()))
        {
            for (BaseFlightMcu flightPoint : flight.getWaypointPackage().getAllFlightPoints())
            {
                if (flightPoint.getIndex() == vwpCoordinate.getWaypointIdentifier())
                {
                    if (flightPoint instanceof McuWaypoint)
                    {
                        McuWaypoint waypoint = (McuWaypoint)flightPoint;
                        WaypointAction action = waypoint.getWpAction();
                        if (action.isBeforeTarget())
                        {
                            shouldLinkToAttack = true;
                        }
                    }
                    
                    if (flightPoint instanceof McuAttack || flightPoint instanceof McuAttackArea)
                    {
                        shouldLinkToAttack = true;
                    }
                }
            }
        }
    }

    @Override
    public void write(BufferedWriter writer) throws PWCGException 
    {
        try
        {
            writer.write("Group");
            writer.newLine();
            writer.write("{");
            writer.newLine();

            writer.write("  Name = \"Virtual WP\";");
            writer.newLine();
            writer.write("  Index = " + index + ";");
            writer.newLine();
            writer.write("  Desc = \"Virtual WP\";");
            writer.newLine();

            vwpPlanes.write(writer);
            vwpCheckZone.write(writer);
            vwpTriggered.write(writer);

            if (vwpEscort != null)
            {
                vwpEscort.write(writer);
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

    private void buildElements() throws PWCGException
    {
        vwpPlanes = new VirtualWaypointPlanes(flight, vwpCoordinate);
        vwpPlanes.build();

        vwpCheckZone = new VirtualWaypointCheckZone(vwpCoordinate, vwpIdentifier);
        vwpCheckZone.build();

        vwpTriggered = new VirtualWaypointTriggered(flight, vwpCoordinate, vwpPlanes, vwpIdentifier);
        vwpTriggered.build();
    }

    private void linkTriggeredElements() throws PWCGException
    {
        vwpCheckZone.linkToTriggered(vwpTriggered);
    }

    @Override
    public void linkToNextVirtualWaypoint(IVirtualWaypoint nextVwp)
    {
        vwpCheckZone.linkToTimedOut(nextVwp);
    }
    
    @Override
    public PlaneMcu getVwpFlightLeader()
    {
        return vwpPlanes.getLeadActivatePlane();
    }
    
    @Override
    public void setVwpTriggerObject(int planeIndex)
    {
        vwpCheckZone.setVwpTriggerObject(planeIndex);        
    }    

    @Override
    public void addAdditionalTime(int additionalTime)
    {
        vwpCheckZone.addAdditionalTime(additionalTime);
    }

    @Override
    public int getEntryPoint()
    {
        return vwpCheckZone.getEntryPoint();
    }

    @Override
    public Coordinate getVwpPosition()
    {
        return vwpCoordinate.getPosition();
    }

    @Override
    public VirtualWayPointCoordinate getVwpCoordinate()
    {
        return vwpCoordinate;
    }

    @Override
    public VirtualWaypointTriggered getVwpTriggered()
    {
        return vwpTriggered;
    }

    @Override
    public VirtualWaypointCheckZone getVwpCheckZone()
    {
        return vwpCheckZone;
    }

    @Override
    public VirtualWaypointPlanes getVwpPlanes()
    {
        return vwpPlanes;
    }

    public VirtualWaypointEscort getVwpEscort()
    {
        return vwpEscort;
    }

    @Override
    public boolean isShouldLinkToAttack()
    {
        return shouldLinkToAttack;
    }
}
