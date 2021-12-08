package pwcg.mission.ground.vehicle;

import java.util.Arrays;
import java.util.List;

import pwcg.core.utils.IWeight;
import pwcg.mission.ground.org.GroundUnitType;
import pwcg.mission.target.TargetType;

public enum VehicleClass implements IWeight
{
    AAAArtillery(false, "AA Artillery", GroundUnitType.AAA_UNIT, TargetType.TARGET_ARTILLERY, 10),
    AAAMachineGun(false, "AA MAchine Gun", GroundUnitType.AAA_UNIT, TargetType.TARGET_INFANTRY, 10),
    
    ArtilleryHowitzer(false, "Artillery", GroundUnitType.ARTILLERY_UNIT, TargetType.TARGET_ARTILLERY, 10),
    
    Balloon(false, "Balloon", GroundUnitType.BALLOON_UNIT, TargetType.TARGET_BALLOON, 0),
    
    ArtilleryAntiTank(false, "Anti Tank Gun", GroundUnitType.INFANTRY_UNIT, TargetType.TARGET_INFANTRY, 10),
    MachineGun(false, "Machine Gun", GroundUnitType.INFANTRY_UNIT, TargetType.TARGET_INFANTRY, 10),
    
    Tank(false, "Tank", GroundUnitType.TANK_UNIT, TargetType.TARGET_ARMOR, 2),
    
    LandCanvas(false, "Canvas", GroundUnitType.STATIC_UNIT, TargetType.TARGET_NONE, 0),
    RadioBeacon(false, "Radio Beacon", GroundUnitType.STATIC_UNIT, TargetType.TARGET_NONE, 0),
    SearchLight(false, "Search Light", GroundUnitType.INFANTRY_UNIT, TargetType.TARGET_NONE, 0),
    StaticAirfield(true, "Equipment", GroundUnitType.STATIC_UNIT, TargetType.TARGET_NONE, 0),
    StaticScenery(true, "Scenery", GroundUnitType.STATIC_UNIT, TargetType.TARGET_NONE, 0),

    Car(false, "Car", GroundUnitType.TRANSPORT_UNIT, TargetType.TARGET_TRANSPORT, 3),
    Truck(false, "Truck", GroundUnitType.TRANSPORT_UNIT, TargetType.TARGET_TRANSPORT, 10),
    TruckAAA(false, "Truck AA", GroundUnitType.TRANSPORT_UNIT, TargetType.TARGET_TRANSPORT, 0),
    TruckAmmo(false, "Truck Ammo", GroundUnitType.TRANSPORT_UNIT, TargetType.TARGET_TRANSPORT, 0),

    Drifter(false, "Barge", GroundUnitType.TRANSPORT_UNIT, TargetType.TARGET_DRIFTER, 1),
    ShipLandingCraft(false, "Landing Craft", GroundUnitType.TRANSPORT_UNIT, TargetType.TARGET_SHIPPING, 0),
    ShipCargo(false, "Cargo Ship", GroundUnitType.TRANSPORT_UNIT, TargetType.TARGET_SHIPPING, 0),
    ShipWarship(false, "Warship", GroundUnitType.TRANSPORT_UNIT, TargetType.TARGET_SHIPPING, 0),
    Submarine(false, "Submarine", GroundUnitType.TRANSPORT_UNIT, TargetType.TARGET_SHIPPING, 0),
    
    TrainCar(false, "Train Car", GroundUnitType.TRANSPORT_UNIT, TargetType.TARGET_RAIL, 3),
    TrainCarAAA(false, "AA Platform", GroundUnitType.TRANSPORT_UNIT, TargetType.TARGET_RAIL, 0),
    TrainCoalCar(false, "Coal Car", GroundUnitType.TRANSPORT_UNIT, TargetType.TARGET_RAIL, 0),
    TrainLocomotive(false, "Locomotive", GroundUnitType.TRANSPORT_UNIT, TargetType.TARGET_RAIL, 1);
 
    private boolean isStatic = false;
    private String name = "Ground Unit";
    private GroundUnitType groundUnitType;
    private TargetType targetType;
    private int weight;

    VehicleClass(boolean isStatic, String name, GroundUnitType groundUnitType, TargetType targetType, int weight)
    {
        this.isStatic = isStatic;
        this.name = name;
        this.groundUnitType = groundUnitType;
        this.targetType = targetType;
        this.weight = weight;
    }
    
    public boolean isStatic()
    {
        return isStatic;
    }

    public String getName()
    {
        return name;
    }

    public GroundUnitType getGroundUnitType()
    {
        return groundUnitType;
    }    
    
    public TargetType getTargetType()
    {
        return targetType;
    }

    public static List<VehicleClass> getAllVehicleClasses()
    {
        VehicleClass[] possibleValues = VehicleClass.class.getEnumConstants();
        return Arrays.asList(possibleValues);
    }
    
    public static boolean isShip(VehicleClass vehicleClass)
    {
        if (vehicleClass == Drifter)
        {
            return true;
        }
        
        if (vehicleClass == ShipCargo || vehicleClass == ShipWarship || vehicleClass == Submarine)
        {
            return true;
        }
        
        return false;
    }

    @Override
    public int getWeight()
    {
        return weight;
    }
}
