package pwcg.mission.ground.unittypes;

import java.util.ArrayList;
import java.util.List;

import pwcg.core.exception.PWCGException;
import pwcg.mission.ground.GroundUnitInformation;
import pwcg.core.utils.RandomNumberGenerator;
import pwcg.mission.flight.waypoint.WaypointAction;
import pwcg.mission.flight.waypoint.WaypointFactory;
import pwcg.mission.flight.waypoint.WaypointPriority;
import pwcg.mission.mcu.McuDelete;
import pwcg.mission.mcu.McuSpawn;
import pwcg.mission.mcu.McuTimer;
import pwcg.mission.mcu.McuWaypoint;

public abstract class GroundMovingDirectFireUnit extends GroundDirectFireUnit implements IGroundDirectFireUnit
{
	protected McuTimer waypointTimer = null;
	protected List<McuWaypoint> waypoints = new ArrayList<McuWaypoint>();
	protected int unitSpeed = 3;
    protected McuTimer deleteTimer = new McuTimer();
    protected McuDelete deleteEntity = new McuDelete();

    public GroundMovingDirectFireUnit(GroundUnitInformation pwcgGroundUnitInformation) 
    {
        super(pwcgGroundUnitInformation);
    }

    @Override
    public void createUnitMission() throws PWCGException 
    {
        createTargetWaypoint();
        makeDeactivation();
        super.createUnitMission();
    }

	private void makeDeactivation()
	{
		deleteTimer.setName("Delete Timer");
		deleteTimer.setDesc("Delete Timer");
		deleteTimer.setTimer(10);
		deleteTimer.setPosition(pwcgGroundUnitInformation.getPosition().copy());

		deleteEntity.setName("Delete");
		deleteEntity.setDesc("Delete");
		deleteEntity.setPosition(pwcgGroundUnitInformation.getPosition().copy());
	}

	protected void createTargetWaypoint() throws PWCGException  
	{
        McuWaypoint assaultWP = WaypointFactory.createMoveToWaypointType();

		assaultWP.setTriggerArea(0);
		assaultWP.setDesc(pwcgGroundUnitInformation.getName() + " WP");
		assaultWP.setSpeed(unitSpeed);
		assaultWP.setPosition(pwcgGroundUnitInformation.getDestination().copy());
		assaultWP.setTargetWaypoint(true);
		
        waypoints.add(assaultWP);
        
        // Half of all assaults end in retreat
        int retreat = RandomNumberGenerator.getRandom(100);
        if (retreat < 50)
        {
            McuWaypoint retreatWP = WaypointFactory.createMoveToWaypointType();

            retreatWP.setTriggerArea(0);
            retreatWP.setDesc(pwcgGroundUnitInformation.getName() + " WP");
            retreatWP.setName(pwcgGroundUnitInformation.getName() + " WP");

            retreatWP.setSpeed(unitSpeed);
            retreatWP.setPriority(WaypointPriority.PRIORITY_HIGH);          
            retreatWP.setWpAction(WaypointAction.WP_ACTION_MOVE_TO);

            retreatWP.setPosition(pwcgGroundUnitInformation.getPosition().copy());
            
            retreatWP.setTargetWaypoint(true);
            
            waypoints.add(retreatWP);
        }
        
        waypointTimer = new McuTimer();
        waypointTimer.setName("WP Timer for " + pwcgGroundUnitInformation.getName());
        waypointTimer.setDesc("WP for " + pwcgGroundUnitInformation.getName());
        waypointTimer.setPosition(pwcgGroundUnitInformation.getPosition().copy());        
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
        
        // Delete the units after they have reached their target
        if (waypoints.size() > 0)
        {
            McuWaypoint lastWP = waypoints.get(waypoints.size()-1);
            lastWP.setTarget(deleteTimer.getIndex());
            deleteTimer.setTarget(deleteEntity.getIndex());
        }

        // WP Timer -> Attack Timer
        waypointTimer.setTarget(this.attackTimer.getIndex());

        // Attack Timer -> Attack
        attackTimer.setTarget(this.attackEntity.getIndex());

    }

    @Override
    protected void createObjectAssociations() 
    {
        for (McuWaypoint waypoint : waypoints)
        {
            waypoint.setObject(spawningVehicle.getEntity().getIndex());
        }
        
        deleteEntity.setObject(spawningVehicle.getEntity().getIndex());

        super.createObjectAssociations();
    }



}	

