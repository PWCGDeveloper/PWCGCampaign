package pwcg.mission.ground.org;

import java.io.BufferedWriter;

import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.mission.flight.waypoint.WaypointFactory;
import pwcg.mission.ground.vehicle.IVehicle;
import pwcg.mission.mcu.BaseFlightMcu;
import pwcg.mission.mcu.McuTimer;
import pwcg.mission.mcu.McuValidator;
import pwcg.mission.mcu.McuWaypoint;

public class GroundAspectMovement implements IGroundAspect
{
    private McuTimer waypointTimer = null;
    private McuWaypoint waypoint;
    private IVehicle vehicle;
    private int unitSpeed = 4;
    private Coordinate destination;

    public GroundAspectMovement(IVehicle vehicle, int unitSpeed, Coordinate destination) 
    {
        this.vehicle = vehicle;
        this.unitSpeed = unitSpeed;
        this.destination = destination;
    }

    public void createGroundUnitAspect()  
    {
        waypoint = WaypointFactory.createMoveToWaypointType();
        waypoint.setTriggerArea(0);
        waypoint.setDesc("Vehicle Waypoint");
        waypoint.setSpeed(unitSpeed);
        waypoint.setPosition(destination);
        waypoint.setTargetWaypoint(true);

        waypointTimer = new McuTimer();
        waypointTimer.setName("Vehicle WP Timer");
        waypointTimer.setDesc("Vehicle WP TImer");
        waypointTimer.setPosition(vehicle.getPosition().copy());
        waypointTimer.setTimerTarget(waypoint.getIndex());

        createTargetAssociations();
        createObjectAssociations();
    }

    @Override
    public void linkToNextElement(int targetIndex)
    {
        waypointTimer.setTimerTarget(targetIndex);
    }

    @Override
    public int getEntryPoint()
    {
        return waypointTimer.getIndex();
    }


    @Override
    public void write(BufferedWriter writer) throws PWCGException
    {
        waypointTimer.write(writer);
        waypoint.write(writer);
    }

    private void createTargetAssociations() 
    {
        waypointTimer.setTimerTarget(waypoint.getIndex());
    }
    
    private void createObjectAssociations() 
    {
        waypoint.setObject(vehicle.getEntity().getIndex());
    }

    @Override
    public BaseFlightMcu getEntryPointMcu()
    {
        return waypointTimer;
    }

    @Override
    public void validate() throws PWCGException
    {
        if (!McuValidator.hasTarget(waypointTimer, waypoint.getIndex()))
        {
            throw new PWCGException("GroundElementMovement: waypoint timer not linked to waypoint");
        }
        
        if (!McuValidator.hasObject(waypoint, vehicle.getEntity().getIndex()))
        {
            throw new PWCGException("GroundElementMovement: waypoint not linked to vehicle");
        }
    }
}	

