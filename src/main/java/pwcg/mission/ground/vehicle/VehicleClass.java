package pwcg.mission.ground.vehicle;

import java.util.Arrays;
import java.util.List;

public enum VehicleClass
{
    MachineGun(false),
    AAAArtillery(false),
    AAAMachineGun(false),
    ArtilleryHowitzer(false),
    ArtilleryAntiTank(false),
    Drifter(false),
    DrifterAAA(false),
    RadioBeacon(false),
    SearchLight(false),
    ShipCargo(false),
    ShipWarship(false),
    Submarine(false),
    Tank(false),
    TrainCar(false),
    TrainCarAAA(false),
    TrainCoalCar(false),
    TrainLocomotive(false),
    Truck(false),
    TruckAAA(false),
    Car(false),
    StaticAirfield(true);
 
    private boolean isStatic = false;

    VehicleClass(boolean isStatic)
    {
        this.isStatic = isStatic;
    }
    
    public boolean isStatic()
    {
        return isStatic;
    }

    public void setStatic(boolean isStatic)
    {
        this.isStatic = isStatic;
    }

    public static List<VehicleClass> getAllVehicleClasses()
    {
        VehicleClass[] possibleValues = VehicleClass.class.getEnumConstants();
        return Arrays.asList(possibleValues);
    }
}
