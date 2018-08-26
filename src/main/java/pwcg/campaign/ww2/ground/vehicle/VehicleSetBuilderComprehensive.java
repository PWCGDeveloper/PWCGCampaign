package pwcg.campaign.ww2.ground.vehicle;

import java.util.ArrayList;
import java.util.List;

import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.location.Orientation;
import pwcg.core.utils.RandomNumberGenerator;
import pwcg.mission.ground.vehicle.IVehicle;

public class VehicleSetBuilderComprehensive
{
    private List<IVehicle> allVehicles = new ArrayList<>();

    public List<IVehicle> makeOneOfEachType() throws PWCGException
    {
        makeAllATGuns();
        makeAllAAAGuns();
        makeAllAAAMachineGuns();
        makeAllArtillery();
        makeAllDrifters();
        makeAllRadioBeacons();
        makeAllShips();
        makeAllSpotLights();
        makeAllTanks();
        makeAllTrainCars();
        makeAllTrainCoalCars();
        makeAllTrainLocomotives();
        makeAllTrucks();
        
        return allVehicles;
    }
    
    public void scatterAroundPosition(Coordinate coordinate)
    {
        for (IVehicle vehicle : allVehicles)
        {
            int xOffset = RandomNumberGenerator.getRandom(4000) - 2000;
            int zOffset = RandomNumberGenerator.getRandom(4000) - 2000;
            Coordinate unitPosition = new Coordinate(coordinate.getXPos() + xOffset, 0.0, coordinate.getZPos() + zOffset);
            vehicle.setPosition(unitPosition);
            vehicle.setOrientation(new Orientation(RandomNumberGenerator.getRandom(360)));
        }
    }
    
    private void makeAllAAAGuns() throws PWCGException
    {
        VehicleSetBuilder<AAAArtillery> vehicleSetBuilder = new VehicleSetBuilder<>(AAAArtillery.class);
        List<IVehicle> vehicles = vehicleSetBuilder.makeVehicles();
        allVehicles.addAll(vehicles);
    }
    
    private void makeAllAAAMachineGuns() throws PWCGException
    {
        VehicleSetBuilder<AAAMachineGun> vehicleSetBuilder = new VehicleSetBuilder<>(AAAMachineGun.class);
        List<IVehicle> vehicles = vehicleSetBuilder.makeVehicles();
        allVehicles.addAll(vehicles);
    }

    private void makeAllArtillery() throws PWCGException
    {
        VehicleSetBuilder<Artillery> vehicleSetBuilder = new VehicleSetBuilder<>(Artillery.class);
        List<IVehicle> vehicles = vehicleSetBuilder.makeVehicles();
        allVehicles.addAll(vehicles);
    }
    
    private void makeAllATGuns() throws PWCGException
    {
        VehicleSetBuilder<ATArtillery> vehicleSetBuilder = new VehicleSetBuilder<>(ATArtillery.class);
        List<IVehicle> vehicles = vehicleSetBuilder.makeVehicles();
        allVehicles.addAll(vehicles);
    }
    
    private void makeAllDrifters() throws PWCGException
    {
        VehicleSetBuilder<Drifter> vehicleSetBuilder = new VehicleSetBuilder<>(Drifter.class);
        List<IVehicle> vehicles = vehicleSetBuilder.makeVehicles();
        allVehicles.addAll(vehicles);
    }
    
    private void makeAllRadioBeacons() throws PWCGException
    {
        VehicleSetBuilder<RadioBeacon> vehicleSetBuilder = new VehicleSetBuilder<>(RadioBeacon.class);
        List<IVehicle> vehicles = vehicleSetBuilder.makeVehicles();
        allVehicles.addAll(vehicles);
    }
    
    private void makeAllShips() throws PWCGException
    {
        VehicleSetBuilder<Ship> vehicleSetBuilder = new VehicleSetBuilder<>(Ship.class);
        List<IVehicle> vehicles = vehicleSetBuilder.makeVehicles();
        allVehicles.addAll(vehicles);
    }
    
    private void makeAllSpotLights() throws PWCGException
    {
        VehicleSetBuilder<SpotLight> vehicleSetBuilder = new VehicleSetBuilder<>(SpotLight.class);
        List<IVehicle> vehicles = vehicleSetBuilder.makeVehicles();
        allVehicles.addAll(vehicles);
    }
    
    private void makeAllTanks() throws PWCGException
    {
        VehicleSetBuilder<Tank> vehicleSetBuilder = new VehicleSetBuilder<>(Tank.class);
        List<IVehicle> vehicles = vehicleSetBuilder.makeVehicles();
        allVehicles.addAll(vehicles);
    }
    
    private void makeAllTrainCars() throws PWCGException
    {
        VehicleSetBuilder<TrainCar> vehicleSetBuilder = new VehicleSetBuilder<>(TrainCar.class);
        List<IVehicle> vehicles = vehicleSetBuilder.makeVehicles();
        allVehicles.addAll(vehicles);
    }
    
    private void makeAllTrainCoalCars() throws PWCGException
    {
        VehicleSetBuilder<TrainCoalCar> vehicleSetBuilder = new VehicleSetBuilder<>(TrainCoalCar.class);
        List<IVehicle> vehicles = vehicleSetBuilder.makeVehicles();
        allVehicles.addAll(vehicles);
    }
    
    private void makeAllTrainLocomotives() throws PWCGException
    {
        VehicleSetBuilder<TrainLocomotive> vehicleSetBuilder = new VehicleSetBuilder<>(TrainLocomotive.class);
        List<IVehicle> vehicles = vehicleSetBuilder.makeVehicles();
        allVehicles.addAll(vehicles);
    }
    
    private void makeAllTrucks() throws PWCGException
    {
        VehicleSetBuilder<Truck> vehicleSetBuilder = new VehicleSetBuilder<>(Truck.class);
        List<IVehicle> vehicles = vehicleSetBuilder.makeVehicles();
        allVehicles.addAll(vehicles);
    }

    public List<IVehicle> getAllVehicles()
    {
        return allVehicles;
    }
    
    
}
