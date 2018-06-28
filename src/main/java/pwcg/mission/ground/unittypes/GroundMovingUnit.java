package pwcg.mission.ground.unittypes;

import java.util.ArrayList;
import java.util.List;

import pwcg.campaign.api.ICountry;
import pwcg.campaign.target.TacticalTarget;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.mission.MissionBeginUnit;
import pwcg.mission.flight.waypoint.WaypointFactory;
import pwcg.mission.mcu.McuSpawn;
import pwcg.mission.mcu.McuTimer;
import pwcg.mission.mcu.McuWaypoint;

public abstract class GroundMovingUnit extends GroundUnitSpawning
{
    protected McuTimer waypointTimer = null;
    protected List<McuWaypoint> waypoints = new ArrayList<McuWaypoint>();
    protected int unitSpeed = 4;

    public GroundMovingUnit(TacticalTarget targetType) 
    {
        super(targetType);
    }

    public void initialize (MissionBeginUnit missionBeginUnit, String name, Coordinate startCoords, Coordinate destinationCoords, ICountry country) 
    {
        super.initialize(missionBeginUnit, name, startCoords,  destinationCoords, country);
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
        waypoint.setDesc(name + " WP");
        waypoint.setSpeed(unitSpeed);
        waypoint.setPosition(destinationCoords.copy());
        waypoint.setTargetWaypoint(true);

        waypoints.add(waypoint);

        waypointTimer = new McuTimer();
        waypointTimer.setName("WP Timer for " + name);
        waypointTimer.setDesc("WP for " + name);
        waypointTimer.setPosition(position.copy());

        waypointTimer.setTarget(waypoints.get(0).getIndex());
    }

    @Override
    protected void createGroundTargetAssociations() 
    {
        // MBU -> Spawn Timer
        this.missionBeginUnit.linkToMissionBegin(this.spawnTimer.getIndex());

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

