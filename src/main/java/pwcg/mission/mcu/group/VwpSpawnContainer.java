package pwcg.mission.mcu.group;

import java.io.BufferedWriter;

import pwcg.core.exception.PWCGIOException;
import pwcg.core.location.Coordinate;
import pwcg.mission.flight.plane.PlaneMcu;
import pwcg.mission.flight.waypoint.virtual.VirtualWayPointCoordinate;
import pwcg.mission.mcu.BaseFlightMcu;
import pwcg.mission.mcu.McuSpawn;
import pwcg.mission.mcu.McuTimer;

public class VwpSpawnContainer
{
    private McuTimer spawnTimer = new McuTimer();
    private McuSpawn spawner = new McuSpawn();
    private McuTimer wpActivateTimer = new McuTimer();
    private BaseFlightMcu waypoint = null;
    private VirtualWayPointCoordinate vwpPosition;

    public VwpSpawnContainer(BaseFlightMcu waypoint, VirtualWayPointCoordinate vwpPosition)
    {
        this.waypoint = waypoint;
        this.vwpPosition = vwpPosition;
    }

    public void create(PlaneMcu plane, int index, Coordinate planeCoordinate)
    {
        buildMcus(plane, index, planeCoordinate);
        createTargetAssociations(plane);
    }

    private void buildMcus(PlaneMcu plane, int index, Coordinate planeCoordinate)
    {
        spawner.setPosition(planeCoordinate.copy());
        spawner.setOrientation(vwpPosition.getOrientation().copy());
        spawner.setObject(plane.getEntity().getIndex());
        spawner.setName("Spawn " + (index + 1));
        spawner.setDesc("Spawn " + (index + 1));

        spawnTimer.setTimer(1);
        spawnTimer.setPosition(planeCoordinate.copy());
        spawnTimer.setOrientation(vwpPosition.getOrientation().copy());
        spawnTimer.setName("Spawn Timer " + (index + 1));
        spawnTimer.setDesc("Spawn Timer " + (index + 1));

        wpActivateTimer.setTimer(1);
        wpActivateTimer.setPosition(planeCoordinate.copy());
        wpActivateTimer.setOrientation(vwpPosition.getOrientation().copy());
        wpActivateTimer.setName("Spawn WP Timer " + (index + 1));
        wpActivateTimer.setDesc("Spawn WP Timer " + (index + 1));
    }

    private void createTargetAssociations(PlaneMcu plane)
    {
        spawnTimer.setTarget(spawner.getIndex());
        spawnTimer.setTarget(wpActivateTimer.getIndex());
        wpActivateTimer.setTarget(waypoint.getIndex());
        wpActivateTimer.setTarget(plane.getAttackTimer().getIndex());
    }

    public void write(BufferedWriter writer) throws PWCGIOException
    {
        spawnTimer.write(writer);
        spawner.write(writer);
        wpActivateTimer.write(writer);
    }
    
    public int getEntryPoint()
    {
        return spawnTimer.getIndex();
    }
    
    public void linkToNextSpawnContainer(int nextASpawnContainerIndex)
    {
        spawnTimer.setTarget(nextASpawnContainerIndex);
    }
    
    public void registerPlaneCounter(int counterIndex)
    {
        spawnTimer.setTarget(counterIndex);
    }

    public McuTimer getSpawnTimer()
    {
        return spawnTimer;
    }

    public McuSpawn getSpawner()
    {
        return spawner;
    }

    public McuTimer getWpActivateTimer()
    {
        return wpActivateTimer;
    }

    public BaseFlightMcu getWaypoint()
    {
        return waypoint;
    }

    public VirtualWayPointCoordinate getVwpPosition()
    {
        return vwpPosition;
    }
}
