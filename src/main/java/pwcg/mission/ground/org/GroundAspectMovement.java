package pwcg.mission.ground.org;

import java.io.BufferedWriter;

import pwcg.core.exception.PWCGException;
import pwcg.core.exception.PWCGIOException;
import pwcg.mission.flight.waypoint.WaypointFactory;
import pwcg.mission.ground.GroundUnitInformation;
import pwcg.mission.ground.vehicle.IVehicle;
import pwcg.mission.mcu.BaseFlightMcu;
import pwcg.mission.mcu.McuTimer;
import pwcg.mission.mcu.McuValidator;
import pwcg.mission.mcu.McuWaypoint;

public class GroundAspectMovement implements IGroundAspect
{
    private McuTimer waypointTimer = null;
    private McuWaypoint waypoint;
    private int unitSpeed = 4;
    private GroundUnitInformation pwcgGroundUnitInformation;
    private IVehicle vehicle;

    public GroundAspectMovement(GroundUnitInformation pwcgGroundUnitInformation, IVehicle vehicle, int unitSpeed) 
    {
        this.pwcgGroundUnitInformation = pwcgGroundUnitInformation;
        this.vehicle = vehicle;
        this.unitSpeed = unitSpeed;
    }

    public void createGroundUnitAspect()  
    {
        waypoint = WaypointFactory.createMoveToWaypointType();
        waypoint.setTriggerArea(0);
        waypoint.setDesc(pwcgGroundUnitInformation.getName() + " WP");
        waypoint.setSpeed(unitSpeed);
        waypoint.setPosition(pwcgGroundUnitInformation.getDestination().copy());
        waypoint.setTargetWaypoint(true);

        waypointTimer = new McuTimer();
        waypointTimer.setName("WP Timer for " + pwcgGroundUnitInformation.getName());
        waypointTimer.setDesc("WP for " + pwcgGroundUnitInformation.getName());
        waypointTimer.setPosition(pwcgGroundUnitInformation.getPosition().copy());
        waypointTimer.setTarget(waypoint.getIndex());

        createTargetAssociations();
        createObjectAssociations();
    }

    @Override
    public void linkToNextElement(int targetIndex)
    {
        waypointTimer.setTarget(targetIndex);
    }

    @Override
    public int getEntryPoint()
    {
        return waypointTimer.getIndex();
    }


    @Override
    public void write(BufferedWriter writer) throws PWCGIOException
    {
        waypointTimer.write(writer);
        waypoint.write(writer);
    }

    private void createTargetAssociations() 
    {
        waypointTimer.setTarget(waypoint.getIndex());
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

