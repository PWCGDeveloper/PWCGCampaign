package pwcg.mission.ground.unittypes;

import pwcg.core.exception.PWCGException;
import pwcg.core.utils.RandomNumberGenerator;
import pwcg.mission.ground.GroundUnitInformation;
import pwcg.mission.ground.GroundUnitSize;
import pwcg.mission.ground.vehicle.IVehicle;
import pwcg.mission.ground.vehicle.TrainLocomotive;
import pwcg.mission.ground.vehicle.VehicleClass;
import pwcg.mission.ground.vehicle.VehicleFactory;

public class GroundUnitSpawningTrainBuilder
{
    private GroundUnitInformation pwcgGroundUnitInformation;
    
    public GroundUnitSpawningTrainBuilder (GroundUnitInformation pwcgGroundUnitInformation)
    {
        this.pwcgGroundUnitInformation = pwcgGroundUnitInformation;
    }
    
    public IVehicle createTrainToSpawn() throws PWCGException
    {
        TrainLocomotive locomotive = makeLocomotive();
        addCars(locomotive);
        return locomotive;
    }

    private TrainLocomotive makeLocomotive() throws PWCGException
    {
        TrainLocomotive locomotive;
        locomotive = pwcg.mission.ground.vehicle.VehicleFactory.createLocomotive(pwcgGroundUnitInformation.getCountry(), pwcgGroundUnitInformation.getDate());
        locomotive.setPosition(pwcgGroundUnitInformation.getPosition().copy());
        locomotive.setOrientation(pwcgGroundUnitInformation.getOrientation().copy());
        locomotive.setCountry(pwcgGroundUnitInformation.getCountry());
        locomotive.populateEntity();
        locomotive.getEntity().setEnabled(1);
        return locomotive;
    }

    private void addCars(TrainLocomotive locomotive) throws PWCGException
    {
        makeCoalCar(locomotive);
        makeAAACar(locomotive);
        makeCars(locomotive);
        makeAAACar(locomotive);
    }

    private void makeCoalCar(TrainLocomotive locomotive) throws PWCGException
    {
        IVehicle coalCar = VehicleFactory.createVehicle(pwcgGroundUnitInformation.getCountry(), pwcgGroundUnitInformation.getDate(), VehicleClass.TrainCoalCar);
        locomotive.addCar(coalCar);
    }

    private void makeAAACar(TrainLocomotive locomotive) throws PWCGException
    {
        IVehicle aaaCar = VehicleFactory.createVehicle(pwcgGroundUnitInformation.getCountry(), pwcgGroundUnitInformation.getDate(), VehicleClass.TrainCarAAA);
        locomotive.addCar(aaaCar);
    }

    private void makeCars(TrainLocomotive locomotive)
            throws PWCGException, PWCGException
    {
        int numCars = calcNumCars();
        for (int i = 0; i < numCars; ++i)
        {   
            IVehicle car = VehicleFactory.createVehicle(pwcgGroundUnitInformation.getCountry(), pwcgGroundUnitInformation.getDate(), VehicleClass.TrainCar);
            locomotive.addCar(car);
        }
    }

    private int calcNumCars()
    {
        int baseTrainCars = 3;
        int randomTrainCars = 3;

        if (pwcgGroundUnitInformation.getUnitSize() == GroundUnitSize.GROUND_UNIT_SIZE_TINY)
        {
            baseTrainCars = 1;
            randomTrainCars = 1;
        }
        else if (pwcgGroundUnitInformation.getUnitSize() == GroundUnitSize.GROUND_UNIT_SIZE_LOW)
        {
            baseTrainCars = 3;
            randomTrainCars = 3;
        }
        else if (pwcgGroundUnitInformation.getUnitSize() == GroundUnitSize.GROUND_UNIT_SIZE_MEDIUM)
        {
            baseTrainCars = 4;
            randomTrainCars = 5;
        }
        else if (pwcgGroundUnitInformation.getUnitSize() == GroundUnitSize.GROUND_UNIT_SIZE_HIGH)
        {
            baseTrainCars = 5;
            randomTrainCars = 7;
        }
        
        int numUnits = baseTrainCars + (RandomNumberGenerator.getRandom(randomTrainCars - baseTrainCars));
        return numUnits;
    }
}
