package pwcg.mission.ground.org;

import java.io.BufferedWriter;

import pwcg.core.exception.PWCGException;
import pwcg.core.exception.PWCGIOException;
import pwcg.core.location.Coordinate;
import pwcg.mission.flight.waypoint.WaypointFactory;
import pwcg.mission.ground.vehicle.IVehicle;
import pwcg.mission.mcu.BaseFlightMcu;
import pwcg.mission.mcu.McuFormation;
import pwcg.mission.mcu.McuTimer;
import pwcg.mission.mcu.McuValidator;
import pwcg.mission.mcu.McuWaypoint;

public class GroundAspectFormationMovement implements IGroundAspect
{
    private McuTimer formationTimer;
    private McuTimer waypointTimer;
    private McuWaypoint waypoint;
    private McuFormation formation;
    private IVehicle vehicle;
    private int unitSpeed = 4;
    private Coordinate destination;
    private int movementType;

    public GroundAspectFormationMovement(IVehicle vehicle, int unitSpeed, Coordinate destination, int movementType) 
    {
        this.vehicle = vehicle;
        this.unitSpeed = unitSpeed;
        this.destination = destination;
        this.movementType = movementType;
    }

    public void createGroundUnitAspect()  
    {
        createFormation();
        createWaypoint();

        createTargetAssociations();
        createObjectAssociations();
    }

    private void createFormation()
    {
        formationTimer = new McuTimer();
        formationTimer.setName("Vehicle WP Timer");
        formationTimer.setDesc("Vehicle WP TImer");
        formationTimer.setPosition(vehicle.getPosition().copy());
        formationTimer.setTime(1);

        formation = new McuFormation(movementType, McuFormation.FORMATION_DENSITY_MED);
        formation.setPosition(vehicle.getPosition().copy());
    }

    private void createWaypoint()
    {
        waypointTimer = new McuTimer();
        waypointTimer.setName("Vehicle WP Timer");
        waypointTimer.setDesc("Vehicle WP TImer");
        waypointTimer.setPosition(vehicle.getPosition().copy());
        waypointTimer.setTime(1);

        waypoint = WaypointFactory.createMoveToWaypointType();
        waypoint.setTriggerArea(0);
        waypoint.setDesc("Vehicle Waypoint");
        waypoint.setSpeed(unitSpeed);
        waypoint.setPosition(destination);
        waypoint.setTargetWaypoint(true);
    }

    @Override
    public void linkToNextElement(int targetIndex)
    {
        waypointTimer.setTarget(targetIndex);
    }

    @Override
    public int getEntryPoint()
    {
        return formationTimer.getIndex();
    }


    @Override
    public void write(BufferedWriter writer) throws PWCGIOException
    {
        formationTimer.write(writer);
        formation.write(writer);

        waypointTimer.write(writer);
        waypoint.write(writer);
    }

    private void createTargetAssociations() 
    {
        formationTimer.setTarget(formation.getIndex());
        formationTimer.setTarget(waypointTimer.getIndex());
        waypointTimer.setTarget(waypoint.getIndex());
    }
    
    private void createObjectAssociations() 
    {
        formation.setObject(vehicle.getEntity().getIndex());
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

