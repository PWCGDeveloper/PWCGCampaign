package pwcg.mission.ground.org;

import java.io.BufferedWriter;
import java.util.ArrayList;
import java.util.List;

import pwcg.campaign.utils.IndexGenerator;
import pwcg.core.constants.AiSkillLevel;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.mission.ground.vehicle.IVehicle;
import pwcg.mission.mcu.McuSpawn;
import pwcg.mission.mcu.McuTimer;

public class GroundUnitElement
{
    private int index = IndexGenerator.getInstance().getNextIndex();
    private IVehicle vehicle;
    private McuTimer activateTimer = new McuTimer();
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
        createObjectAssociations();
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
                activateTimer.setTimerTarget(element.getEntryPoint());
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
        activateTimer.write(writer);
        if (!vehicle.getEntity().isEnabled())
        {
            spawn.write(writer);
        }
        
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
        return activateTimer.getIndex();
    }

    private void createSpawns() throws PWCGException
    {
        createSpawnTimer();
        createSpawn();
    }

    private void createSpawnTimer()
    {
        activateTimer.setName("Ground Element Activate Timer");
        activateTimer.setDesc("Ground Element Activate Timer");
        activateTimer.setPosition(vehicleStartLocation);
    }

    private void createSpawn() throws PWCGException
    {
        spawn.setName("Spawn " + vehicle.getVehicleName());
        spawn.setDesc("Ground Element Spawn");
        spawn.setPosition(vehicleStartLocation);
        spawn.setOrientation(vehicle.getOrientation());
    }

    private void createTargetAssociations()
    {
        activateTimer.setTimerTarget(spawn.getIndex());
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
        return activateTimer;
    }

    public McuSpawn getSpawn()
    {
        return spawn;
    }

    public Coordinate getVehicleStartLocation()
    {
        return vehicleStartLocation;
    }

    public int getIndex()
    {
        return index;
    }

    public void convertGroundUnitElementToNotSpawning() throws PWCGException
    {
        spawn = null;                   // no need for a spawn
        activateTimer.clearTargets();   // eliminate the link to the spawn
        
        vehicle.getEntity().enableEntity(); // Enable the vehicle
        linkAspects();                      // Relink the aspects such that they trigger
    }
}
