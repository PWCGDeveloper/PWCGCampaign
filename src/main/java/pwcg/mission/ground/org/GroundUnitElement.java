package pwcg.mission.ground.org;

import java.io.BufferedWriter;
import java.util.ArrayList;
import java.util.List;

import pwcg.core.constants.AiSkillLevel;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.mission.ground.vehicle.IVehicle;
import pwcg.mission.mcu.McuDeactivate;
import pwcg.mission.mcu.McuDelete;
import pwcg.mission.mcu.McuEvent;
import pwcg.mission.mcu.McuSpawn;
import pwcg.mission.mcu.McuTimer;

public class GroundUnitElement
{
    private IVehicle vehicle;
    private McuTimer spawnTimer = new McuTimer();
    private McuSpawn spawn = new McuSpawn();
    private McuTimer deleteTimer = new McuTimer();
    private McuDelete delete = new McuDelete();
    private McuDeactivate spawnDeactivate = new McuDeactivate();
    private List<IGroundAspect> aspectsOfGroundUnit = new ArrayList<>();
    private Coordinate vehicleStartLocation;

    public GroundUnitElement(IVehicle vehicle, Coordinate vehicleStartLocation)
    {
        this.vehicle = vehicle;
        this.vehicleStartLocation = vehicleStartLocation;
    }

    public void createGroundUnitElement() throws PWCGException
    {
        createSpawns();
        createDeletes();
        createTargetAssociations();
        createObjectAssociations();
        createEventAssociations();
    }

    public void addAspect(IGroundAspect aspect)
    {
        aspectsOfGroundUnit.add(aspect);
    }

    public void linkAspects() throws PWCGException
    {
        IGroundAspect previousElement = null;
        for (IGroundAspect element : aspectsOfGroundUnit)
        {
            if (previousElement == null)
            {
                spawnTimer.setTarget(element.getEntryPoint());
            }
            else
            {
                previousElement.linkToNextElement(element.getEntryPoint());
            }
            previousElement = element;
        }
    }

    public void write(BufferedWriter writer) throws PWCGException
    {
        vehicle.write(writer);
        spawnTimer.write(writer);
        spawn.write(writer);
        deleteTimer.write(writer);
        delete.write(writer);
        spawnDeactivate.write(writer);

        for (IGroundAspect groundElement : aspectsOfGroundUnit)
        {
            groundElement.write(writer);
        }
    }

    public void setAiLevel(AiSkillLevel aiLevel)
    {
        vehicle.setAiLevel(aiLevel);
    }

    public int getEntryPoint()
    {
        return spawnTimer.getIndex();
    }

    public int getDeleteEntryPoint()
    {
        return deleteTimer.getIndex();
    }

    private void createSpawns() throws PWCGException
    {
        createSpawnTimer();
        createSpawn();
    }

    private void createSpawnTimer()
    {
        spawnTimer.setName("Ground Element Spawn Timer");
        spawnTimer.setDesc("Ground Element Spawn Timer");
        spawnTimer.setPosition(vehicleStartLocation);
    }

    private void createSpawn() throws PWCGException
    {
        spawn.setName("Spawn " + vehicle.getVehicleName());
        spawn.setDesc("Ground Element Spawn");
        spawn.setPosition(vehicleStartLocation);
        spawn.setOrientation(vehicle.getOrientation());
    }

    private void createDeletes() throws PWCGException
    {
        createDeleteTimer();
        createDelete();
        createSpawnDeactivate();
    }

    private void createDeleteTimer()
    {
        deleteTimer.setName("Ground Element Delete Timer");
        deleteTimer.setDesc("Ground Element Delete Timer");
        deleteTimer.setPosition(vehicleStartLocation);
    }

    private void createDelete() throws PWCGException
    {
        delete.setName("Ground Element Delete");
        delete.setDesc("Ground Element Delete");
        delete.setPosition(vehicleStartLocation);
    }

    private void createSpawnDeactivate() throws PWCGException
    {
        spawnDeactivate.setName("Ground Element Spawn Deactivate");
        spawnDeactivate.setDesc("Ground Element Spawn Deactivate");
        spawnDeactivate.setPosition(vehicleStartLocation);
    }

    private void createTargetAssociations()
    {
        spawnTimer.setTarget(spawn.getIndex());
        deleteTimer.setTarget(delete.getIndex());
        spawnDeactivate.setTarget(spawnTimer.getIndex());
        spawnDeactivate.setTarget(spawn.getIndex());
    }

    private void createObjectAssociations()
    {
        spawn.setObject(vehicle.getEntity().getIndex());
        delete.setObject(vehicle.getEntity().getIndex());
    }

    private void createEventAssociations()
    {
        McuEvent event = new McuEvent(McuEvent.ONKILLED, spawnDeactivate.getIndex());
        vehicle.getEntity().addEvent(event);
    }

    public List<IGroundAspect> getAspectsOfGroundUnit()
    {
        return aspectsOfGroundUnit;
    }

    public IVehicle getVehicle()
    {
        return vehicle;
    }

    public McuTimer getSpawnTimer()
    {
        return spawnTimer;
    }

    public McuSpawn getSpawn()
    {
        return spawn;
    }

    public Coordinate getVehicleStartLocation()
    {
        return vehicleStartLocation;
    }
}
