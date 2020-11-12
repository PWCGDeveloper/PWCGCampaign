package pwcg.mission.ground.vehicle;

import java.util.Arrays;
import java.util.List;

import pwcg.mission.ground.org.GroundUnitType;

public enum VehicleClass
{
    AAAArtillery(false, "AA Artillery", GroundUnitType.AAA_UNIT),
    AAAMachineGun(false, "AA MAchine Gun", GroundUnitType.AAA_UNIT),
    
    ArtilleryHowitzer(false, "Artillery", GroundUnitType.ARTILLERY_UNIT),
    
    Balloon(false, "Balloon", GroundUnitType.BALLOON_UNIT),
    
    ArtilleryAntiTank(false, "Anti Tank Gun", GroundUnitType.INFANTRY_UNIT),
    MachineGun(false, "Machine Gun", GroundUnitType.INFANTRY_UNIT),
    
    Tank(false, "Tank", GroundUnitType.TANK_UNIT),
    
    LandCanvas(false, "Canvas", GroundUnitType.STATIC_UNIT),
    RadioBeacon(false, "Radio Beacon", GroundUnitType.STATIC_UNIT),
    SearchLight(false, "Search Light", GroundUnitType.INFANTRY_UNIT),

    Car(false, "Car", GroundUnitType.TRANSPORT_UNIT),
    Drifter(false, "Barge", GroundUnitType.TRANSPORT_UNIT),
    DrifterAAA(false, "Barge AA", GroundUnitType.TRANSPORT_UNIT),
    ShipCargo(false, "Cargo Ship", GroundUnitType.TRANSPORT_UNIT),
    ShipWarship(false, "Warship", GroundUnitType.TRANSPORT_UNIT),
    Submarine(false, "Submarine", GroundUnitType.TRANSPORT_UNIT),
    StaticAirfield(true, "Equipment", GroundUnitType.STATIC_UNIT),
    StaticScenery(true, "Scenery", GroundUnitType.STATIC_UNIT),
    TrainCar(false, "Train Car", GroundUnitType.TRANSPORT_UNIT),
    TrainCarAAA(false, "AA Platform", GroundUnitType.TRANSPORT_UNIT),
    TrainCoalCar(false, "Coal Car", GroundUnitType.TRANSPORT_UNIT),
    TrainLocomotive(false, "Locomotive", GroundUnitType.TRANSPORT_UNIT),
    Truck(false, "Truck", GroundUnitType.TRANSPORT_UNIT),
    TruckAAA(false, "Truck AA", GroundUnitType.TRANSPORT_UNIT);
 
    private boolean isStatic = false;
    private String name = "Ground Unit";
    private GroundUnitType groundUnitType;

    VehicleClass(boolean isStatic, String name, GroundUnitType groundUnitType)
    {
        this.isStatic = isStatic;
        this.name = name;
        this.groundUnitType = groundUnitType;
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

    public static List<VehicleClass> getAllVehicleClasses()
    {
        VehicleClass[] possibleValues = VehicleClass.class.getEnumConstants();
        return Arrays.asList(possibleValues);
    }
    
    public static boolean isShip(VehicleClass vehicleClass)
    {
        if (vehicleClass == Drifter || vehicleClass == DrifterAAA)
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
