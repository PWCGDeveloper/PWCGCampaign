package pwcg.mission.ground.org;

import java.io.BufferedWriter;
import java.util.ArrayList;
import java.util.List;

import pwcg.core.constants.AiSkillLevel;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.mission.ground.vehicle.IVehicle;
import pwcg.mission.mcu.McuSpawn;
import pwcg.mission.mcu.McuTimer;

public class GroundUnitElement
{
    private IVehicle vehicle;
    private McuTimer spawnTimer = new McuTimer();
    private McuSpawn spawn = new McuSpawn();
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
        createTargetAssociations();
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

    private void createSpawns() throws PWCGException
    {
        createSpawnTimer();
        createSpawn();
        createTargetAssociations();
        createObjectAssociations();
    }

    private void createSpawnTimer()
    {
        spawnTimer.setName("Ground Element Spawn Timer");
        spawnTimer.setDesc("Ground Element Spawn Timer");
        spawnTimer.setPosition(vehicleStartLocation);
    }

    private void createSpawn() throws PWCGException
    {
        spawn.setName("Ground Element Spawn");
        spawn.setDesc("Ground Element Spawn");
        spawn.setPosition(vehicleStartLocation);
    }

    private void createTargetAssociations()
    {
        spawnTimer.setTarget(spawn.getIndex());
    }

    private void createObjectAssociations()
    {
        spawn.setObject(vehicle.getEntity().getIndex());
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
