package pwcg.mission.ground.org;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import pwcg.campaign.api.ICountry;
import pwcg.campaign.utils.IndexGenerator;
import pwcg.core.constants.AiSkillLevel;
import pwcg.core.exception.PWCGException;
import pwcg.core.exception.PWCGIOException;
import pwcg.core.location.Coordinate;
import pwcg.core.utils.MathUtils;
import pwcg.core.utils.PWCGLogger;
import pwcg.core.utils.RandomNumberGenerator;
import pwcg.mission.ground.GroundUnitInformation;
import pwcg.mission.ground.unittypes.GroundUnitSpawningTrainBuilder;
import pwcg.mission.ground.unittypes.GroundUnitSpawningVehicleBuilder;
import pwcg.mission.ground.vehicle.IVehicle;
import pwcg.mission.ground.vehicle.IVehicleDefinition;
import pwcg.mission.ground.vehicle.VehicleClass;
import pwcg.mission.mcu.AttackAreaType;
import pwcg.mission.mcu.McuFormation;
import pwcg.mission.mcu.McuTimer;
import pwcg.mission.target.TargetType;


public abstract class GroundUnit implements IGroundUnit
{    
    public enum GroundFormationType
    {
        FORMATION_TYPE_ON_WAYPOINT,
        FORMATION_TYPE_ON_ROAD,
        FORMATION_TYPE_OFF_ROAD,
    };
    
    static private int ARTY_ATTACK_AREA_RADIUS = 500;

    protected int index = IndexGenerator.getInstance().getNextIndex();  
    protected GroundUnitInformation pwcgGroundUnitInformation;
    protected VehicleClass vehicleClass;

    protected McuTimer spawnUnitTimer = new McuTimer();
    protected McuTimer deleteUnitTimer = new McuTimer();
    protected List<GroundUnitElement> groundElements = new ArrayList<>();

    public GroundUnit(VehicleClass vehicleClass, GroundUnitInformation pwcgGroundUnitInformation) 
    {
        this.vehicleClass = vehicleClass;
        this.pwcgGroundUnitInformation = pwcgGroundUnitInformation;
    }

    @Override
    public TargetType getTargetType()
    {
        return pwcgGroundUnitInformation.getTargetType();
    }

    @Override
    public void write(BufferedWriter writer) throws PWCGException 
    {      
        try
        {
            writer.write("Group");
            writer.newLine();
            writer.write("{");
            writer.newLine();
            
            writer.write("  Name = \"" + vehicleClass.getName() + "\";");
            writer.newLine();
            writer.write("  Index = " + index + ";");
            writer.newLine();
            writer.write("  Desc = \"" + vehicleClass.getName() + "\";");
            writer.newLine();

            writeMcus(writer);
            
            writer.write("}");
            writer.newLine();
        }
        catch (IOException e)
        {
            PWCGLogger.logException(e);
            throw new PWCGIOException(e.getMessage());
        }
    }

    protected void writeMcus(BufferedWriter writer) throws PWCGException
    {
        spawnUnitTimer.write(writer);
        deleteUnitTimer.write(writer);
        for (GroundUnitElement groundElement : groundElements)
        {
            groundElement.write(writer);
        }
    }

    protected void linkElements() throws PWCGException 
    {
        for (GroundUnitElement element : groundElements)
        {
            spawnUnitTimer.setTarget(element.getEntryPoint());
            deleteUnitTimer.setTarget(element.getDeleteEntryPoint());
            element.linkAspects();
        }
    }

    @Override
    public ICountry getCountry() throws PWCGException
    {
        return pwcgGroundUnitInformation.getCountry();
    }
    
    @Override
    public Coordinate getPosition() throws PWCGException
    {
        return pwcgGroundUnitInformation.getPosition();
    }
    
    @Override
    public String getName() throws PWCGException
    {
        return pwcgGroundUnitInformation.getName();
    }

    @Override
    public List<IVehicle> getVehicles()
    {
        List<IVehicle> vehicles = new ArrayList<>();
        for (GroundUnitElement groundElement : groundElements)
        {
            vehicles.add(groundElement.getVehicle());
        }
        return vehicles;
    }

    @Override
    public VehicleClass getVehicleClass()
    {
        return vehicleClass;
    }
    
    @Override
    public void setAiLevel(AiSkillLevel aiLevel)
    {
        for (GroundUnitElement groundElement : groundElements)
        {
            groundElement.setAiLevel(aiLevel);
        }
    }

    @Override
    public int getEntryPoint()
    {
        return spawnUnitTimer.getIndex();
    }
    
    @Override
    public int getDeleteEntryPoint()
    {
        return deleteUnitTimer.getIndex();
    }

    @Override
    public void validate() throws PWCGException
    {
        if (spawnUnitTimer == null)
        {
            throw new PWCGException("GroundUnit: no spawn timer");
        }
        if (deleteUnitTimer == null)
        {
            throw new PWCGException("GroundUnit: no delete timer");
        }
        
        GroundUnitValidator validator = new GroundUnitValidator(this);
        validator.validate();
    }

    protected void addIndirectFireAspect() throws PWCGException
    {
        for (GroundUnitElement groundElement : groundElements)
        {
            int distanceOffset = RandomNumberGenerator.getRandom(500);
            int directionOffset = RandomNumberGenerator.getRandom(360);
            Coordinate fireCoordinates = MathUtils.calcNextCoord(pwcgGroundUnitInformation.getDestination(), directionOffset, distanceOffset);
            
            IGroundAspect areaFire = GroundAspectFactory.createGroundAspectAreaFire(fireCoordinates, 
                    groundElement.getVehicle(), AttackAreaType.INDIRECT, ARTY_ATTACK_AREA_RADIUS);
            groundElement.addAspect(areaFire);
        }
    }
    
    protected void addDirectFireAspect() throws PWCGException
    {
        for (GroundUnitElement groundElement : groundElements)
        {
            IGroundAspect directFire = GroundAspectFactory.createGroundAspectDirectFire(groundElement.getVehicle());
            groundElement.addAspect(directFire);
        }
    }

    protected void addAAAFireAspect(int attackAreaRadius) throws PWCGException
    {
        for (GroundUnitElement groundElement : groundElements)
        {
            Coordinate fireCoordinates = groundElement.getVehicle().getPosition();

            IGroundAspect areaFire = GroundAspectFactory.createGroundAspectAreaFire(fireCoordinates, 
                    groundElement.getVehicle(), AttackAreaType.AIR_TARGETS, attackAreaRadius);
            groundElement.addAspect(areaFire);
        }
    }

    protected void addMovementAspect(int unitSpeed, List<Coordinate> destinations, GroundFormationType groundFormationType) throws PWCGException
    {
        int numElements = groundElements.size();
        if (destinations.size() < groundElements.size())
        {
            numElements = destinations.size();
        }
        
        for (int i = 0; i < numElements; ++i)
        {
            GroundUnitElement groundElement = groundElements.get(i);
            IGroundAspect movementAspect = makegroundAspect(groundElement, unitSpeed, destinations.get(i), groundFormationType);
            groundElement.addAspect(movementAspect);
        }
    }
    
    private IGroundAspect makegroundAspect(GroundUnitElement groundElement, int unitSpeed, Coordinate destination, GroundFormationType groundFormationType) throws PWCGException
    {
        if (groundFormationType == GroundFormationType.FORMATION_TYPE_ON_ROAD)
        {
            return GroundAspectFactory.createGroundAspectFormationMovement(groundElement.getVehicle(), unitSpeed, destination, McuFormation.FORMATION_ON_ROAD_COLUMN);
        }
        else if (groundFormationType == GroundFormationType.FORMATION_TYPE_OFF_ROAD)
        {
            return GroundAspectFactory.createGroundAspectFormationMovement(groundElement.getVehicle(), unitSpeed, destination, McuFormation.FORMATION_OFF_ROAD_USE_POSITION);
        }
        else
        {
            return GroundAspectFactory.createGroundAspectMovement(groundElement.getVehicle(), unitSpeed, destination);
        }
    }
    
    protected void createVehicles(List<Coordinate> vehicleStartPositions) throws PWCGException 
    {       
        if (vehicleClass == VehicleClass.TrainLocomotive)
        {
            GroundUnitSpawningTrainBuilder trainBuilder = new GroundUnitSpawningTrainBuilder(pwcgGroundUnitInformation);
            IVehicle vehicle = trainBuilder.createTrainToSpawn();
            GroundUnitElement groundElement = new GroundUnitElement(vehicle, pwcgGroundUnitInformation.getPosition());
            groundElement.createGroundUnitElement();
            groundElements.add(groundElement);
        }
        else
        {
            for (Coordinate vehiclePosition : vehicleStartPositions)
            {
                IVehicle vehicle = GroundUnitSpawningVehicleBuilder.createVehicleToSpawn(pwcgGroundUnitInformation, vehicleClass);
                vehicle.setPosition(vehiclePosition);
                GroundUnitElement groundElement = new GroundUnitElement(vehicle, vehiclePosition);
                groundElement.createGroundUnitElement();
                groundElements.add(groundElement);
            }
        }
    }
    
    protected void createVehiclesFromDefinition(List<Coordinate> vehicleStartPositions, IVehicleDefinition vehicleDefinition) throws PWCGException 
    {       
        for (Coordinate vehiclePosition : vehicleStartPositions)
        {
            IVehicle vehicle = GroundUnitSpawningVehicleBuilder.createVehicleToSpawnFromDefinition(pwcgGroundUnitInformation, vehicleClass, vehicleDefinition);
            vehicle.setPosition(vehiclePosition);
            GroundUnitElement groundElement = new GroundUnitElement(vehicle, vehiclePosition);
            groundElement.createGroundUnitElement();
            groundElements.add(groundElement);
        }
    }


    protected void createSpawnTimer() 
    {
        spawnUnitTimer.setName("Ground Unit Spawn Timer");
        spawnUnitTimer.setDesc("Ground Unit Spawn Timer");
        spawnUnitTimer.setPosition(pwcgGroundUnitInformation.getPosition());

        deleteUnitTimer.setName("Ground Unit Delete Timer");
        deleteUnitTimer.setDesc("Ground Unit Delete Timer");
        deleteUnitTimer.setPosition(pwcgGroundUnitInformation.getPosition());
    }

    
    public GroundUnitType getGroundUnitType()
    {
        return vehicleClass.getGroundUnitType();
    }
    
    public List<GroundUnitElement> getGroundElements()
    {
        return groundElements;
    }

    public McuTimer getSpawnUnitTimer()
    {
        return spawnUnitTimer;
    }
    
    public int getUnitCount()
    {
        return groundElements.size();
    }

}	


