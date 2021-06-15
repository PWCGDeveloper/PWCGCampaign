package pwcg.mission.ground.vehicle;

import java.util.Arrays;
import java.util.List;

import pwcg.mission.ground.org.GroundUnitType;
import pwcg.mission.target.TargetType;

public enum VehicleClass
{
    AAAArtillery(false, "AA Artillery", GroundUnitType.AAA_UNIT, TargetType.TARGET_ARTILLERY),
    AAAMachineGun(false, "AA MAchine Gun", GroundUnitType.AAA_UNIT, TargetType.TARGET_INFANTRY),
    
    ArtilleryHowitzer(false, "Artillery", GroundUnitType.ARTILLERY_UNIT, TargetType.TARGET_ARTILLERY),
    
    Balloon(false, "Balloon", GroundUnitType.BALLOON_UNIT, TargetType.TARGET_BALLOON),
    
    ArtilleryAntiTank(false, "Anti Tank Gun", GroundUnitType.INFANTRY_UNIT, TargetType.TARGET_INFANTRY),
    MachineGun(false, "Machine Gun", GroundUnitType.INFANTRY_UNIT, TargetType.TARGET_INFANTRY),
    
    Tank(false, "Tank", GroundUnitType.TANK_UNIT, TargetType.TARGET_ARMOR),
    
    LandCanvas(false, "Canvas", GroundUnitType.STATIC_UNIT, TargetType.TARGET_NONE),
    RadioBeacon(false, "Radio Beacon", GroundUnitType.STATIC_UNIT, TargetType.TARGET_NONE),
    SearchLight(false, "Search Light", GroundUnitType.INFANTRY_UNIT, TargetType.TARGET_NONE),
    StaticAirfield(true, "Equipment", GroundUnitType.STATIC_UNIT, TargetType.TARGET_NONE),
    StaticScenery(true, "Scenery", GroundUnitType.STATIC_UNIT, TargetType.TARGET_NONE),

    Car(false, "Car", GroundUnitType.TRANSPORT_UNIT, TargetType.TARGET_TRANSPORT),
    Truck(false, "Truck", GroundUnitType.TRANSPORT_UNIT, TargetType.TARGET_TRANSPORT),
    TruckAAA(false, "Truck AA", GroundUnitType.TRANSPORT_UNIT, TargetType.TARGET_TRANSPORT),
    TruckAmmo(false, "Truck Ammo", GroundUnitType.TRANSPORT_UNIT, TargetType.TARGET_TRANSPORT),

    TruckAAAPlayer(false, "Truck AA Player", GroundUnitType.TRANSPORT_UNIT, TargetType.TARGET_TRANSPORT),
    TankPlayer(false, "Tank Player", GroundUnitType.TANK_UNIT, TargetType.TARGET_ARMOR),

    Drifter(false, "Barge", GroundUnitType.TRANSPORT_UNIT, TargetType.TARGET_DRIFTER),
    ShipLandingCraft(false, "Landing Craft", GroundUnitType.TRANSPORT_UNIT, TargetType.TARGET_SHIPPING),
    ShipCargo(false, "Cargo Ship", GroundUnitType.TRANSPORT_UNIT, TargetType.TARGET_SHIPPING),
    ShipWarship(false, "Warship", GroundUnitType.TRANSPORT_UNIT, TargetType.TARGET_SHIPPING),
    Submarine(false, "Submarine", GroundUnitType.TRANSPORT_UNIT, TargetType.TARGET_SHIPPING),
    
    TrainCar(false, "Train Car", GroundUnitType.TRANSPORT_UNIT, TargetType.TARGET_RAIL),
    TrainCarAAA(false, "AA Platform", GroundUnitType.TRANSPORT_UNIT, TargetType.TARGET_RAIL),
    TrainCoalCar(false, "Coal Car", GroundUnitType.TRANSPORT_UNIT, TargetType.TARGET_RAIL),
    TrainLocomotive(false, "Locomotive", GroundUnitType.TRANSPORT_UNIT, TargetType.TARGET_RAIL);
 
    private boolean isStatic = false;
    private String name = "Ground Unit";
    private GroundUnitType groundUnitType;
    private TargetType targetType;

    VehicleClass(boolean isStatic, String name, GroundUnitType groundUnitType, TargetType targetType)
    {
        this.isStatic = isStatic;
        this.name = name;
        this.groundUnitType = groundUnitType;
        this.targetType = targetType;
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
}
