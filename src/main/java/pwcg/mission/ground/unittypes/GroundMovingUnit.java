package pwcg.mission.ground.unittypes;

import java.util.ArrayList;
import java.util.List;

import pwcg.core.exception.PWCGException;
import pwcg.mission.ground.GroundUnitInformation;
import pwcg.mission.flight.waypoint.WaypointFactory;
import pwcg.mission.mcu.McuSpawn;
import pwcg.mission.mcu.McuTimer;
import pwcg.mission.mcu.McuWaypoint;

public abstract class GroundMovingUnit extends GroundUnitSpawning
{
    protected McuTimer waypointTimer = null;
    protected List<McuWaypoint> waypoints = new ArrayList<McuWaypoint>();
    protected int unitSpeed = 4;

    public GroundMovingUnit(GroundUnitInformation pwcgGroundUnitInformation) 
    {
        super(pwcgGroundUnitInformation);
    }

    @Override
    public void createUnitMission() throws PWCGException 
    {
        createTargetWaypoint();
        super.createUnitMission();
    }

    protected void createTargetWaypoint()  
    {
        McuWaypoint waypoint = WaypointFactory.createMoveToWaypointType();
        waypoint.setTriggerArea(0);
        waypoint.setDesc(pwcgGroundUnitInformation.getName() + " WP");
        waypoint.setSpeed(unitSpeed);
        waypoint.setPosition(pwcgGroundUnitInformation.getDestination().copy());
        waypoint.setTargetWaypoint(true);

        waypoints.add(waypoint);

        waypointTimer = new McuTimer();
        waypointTimer.setName("WP Timer for " + pwcgGroundUnitInformation.getName());
        waypointTimer.setDesc("WP for " + pwcgGroundUnitInformation.getName());
        waypointTimer.setPosition(pwcgGroundUnitInformation.getPosition().copy());

        waypointTimer.setTarget(waypoints.get(0).getIndex());
    }

    @Override
    protected void createGroundTargetAssociations() 
    {
        // MBU -> Spawn Timer
        pwcgGroundUnitInformation.getMissionBeginUnit().linkToMissionBegin(this.spawnTimer.getIndex());

        // Spawn Timer -> Spawns
        for (McuSpawn spawn : spawners)
        {
            spawnTimer.setTarget(spawn.getIndex());
        }

        // Spawn Timer -> WP Timer
        spawnTimer.setTarget(this.waypointTimer.getIndex());

        // WP Timer -> WP
        waypointTimer.setTarget(this.waypoints.get(0).getIndex());
        if (waypoints.size() > 1)
        {
            for (int i = 1; i < waypoints.size(); ++i)
            {
                McuWaypoint waypoint1 = waypoints.get(i-1);
                McuWaypoint waypoint2 = waypoints.get(i);

                waypoint1.setTarget(waypoint2.getIndex());
            }
        }
    }
    
    @Override
    protected void createObjectAssociations() 
    {
        for (McuWaypoint waypoint : waypoints)
        {
            waypoint.setObject(spawningVehicle.getEntity().getIndex());
        }

        super.createObjectAssociations();
    }


}	

