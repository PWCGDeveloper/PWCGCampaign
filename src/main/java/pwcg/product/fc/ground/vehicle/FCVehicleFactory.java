package pwcg.product.fc.ground.vehicle;

import pwcg.campaign.api.ICountry;
import pwcg.core.exception.PWCGException;
import pwcg.mission.flight.Flight;
import pwcg.mission.ground.unittypes.transport.ShipConvoyUnit.ShipConvoyTypes;
import pwcg.mission.ground.vehicle.ITrainLocomotive;
import pwcg.mission.ground.vehicle.IVehicle;
import pwcg.mission.ground.vehicle.IVehicleFactory;
import pwcg.product.fc.ground.vehicle.Truck.TruckType;

public class FCVehicleFactory implements IVehicleFactory
{
    
    @Override
    public IVehicle createBarge(ICountry country) throws PWCGException
    {
        Drifter drifter = new Drifter();
        drifter.makeRandomVehicleFromSet(country);

        return drifter;
    }

    @Override
    public IVehicle createShip(ICountry country, ShipConvoyTypes shipConvoyType) throws PWCGException
    {
        Ship ship = new Ship(shipConvoyType);
        ship.makeRandomVehicleFromSet(country);
        return ship;
    }

    @Override
    public IVehicle createTank(ICountry country) throws PWCGException
    {
        Tank tank = new Tank();
        tank.makeRandomVehicleFromSet(country);
        return tank;
    }

    @Override
    public IVehicle cloneTank(IVehicle source) throws PWCGException
    {
        if (source instanceof Tank)
        {
            Tank sourceTank = (Tank)source;
            return sourceTank.copy();
        }
        
        throw new PWCGException ("Attempt to clone incorrect vehicle type");
    }

    @Override
    public IVehicle createCar(ICountry country) throws PWCGException
    {
        Truck truck = new Truck(TruckType.CAR);
        truck.makeRandomVehicleFromSet(country);
        return truck;
    }

    @Override
    public IVehicle createAAATruck(ICountry country) throws PWCGException
    {
        Truck truck = new Truck(TruckType.TRUCK_AAA);
        truck.makeRandomVehicleFromSet(country);        
        return truck;
    }

    @Override
    public IVehicle createCargoTruck(ICountry country) throws PWCGException
    {
        Truck truck = new Truck(TruckType.TRUCK_CARGO);
        truck.makeRandomVehicleFromSet(country);
        return truck;
    }

    @Override
    public IVehicle cloneTruck(IVehicle source) throws PWCGException
    {
        if (source instanceof Truck)
        {
            Truck sourceTruck = (Truck)source;
            return sourceTruck.copy();
        }
        
        throw new PWCGException ("Attempt to clone incorrect vehicle type");
    }

    @Override
    public IVehicle createTrainCar(ICountry country) throws PWCGException
    {
        TrainCar trainCar = new TrainCar();
        trainCar.makeRandomVehicleFromSet(country);
        return trainCar;
    }

    @Override
    public IVehicle cloneTrainCar(IVehicle source) throws PWCGException
    {
        if (source instanceof TrainCar)
        {
            TrainCar sourceTrainCar = (TrainCar)source;
            return sourceTrainCar.copy();
        }
        
        throw new PWCGException ("Attempt to clone incorrect vehicle type");
    }

    @Override
    public ITrainLocomotive createTrainLocomotive(ICountry country) throws PWCGException
    {
        TrainLocomotive trainLocomotive = new TrainLocomotive();
        trainLocomotive.makeRandomVehicleFromSet(country);

        TrainCoalCar coalCar = new TrainCoalCar(trainLocomotive);
        coalCar.makeRandomVehicleFromSet(trainLocomotive.getCountry());
        trainLocomotive.addCar(coalCar);

        return trainLocomotive;
    }

    @Override
    public ITrainLocomotive cloneTrainLocomotive(IVehicle source) throws PWCGException
    {
        if (source instanceof TrainLocomotive)
        {
            TrainLocomotive sourceTrainLocomotive = (TrainLocomotive)source;
            return sourceTrainLocomotive.copy();
        }
        
        throw new PWCGException ("Attempt to clone incorrect vehicle type");
    }

    @Override
    public IVehicle createArtillery(ICountry country) throws PWCGException
    {
        Artillery artillery = new Artillery();
        artillery.makeRandomVehicleFromSet(country);
        
        return artillery;
    }

    @Override
    public IVehicle cloneArtillery(IVehicle source) throws PWCGException
    {
        if (source instanceof Artillery)
        {
            Artillery sourceArtillery = (Artillery)source;
            return sourceArtillery.copy();
        }
        
        throw new PWCGException ("Attempt to clone incorrect vehicle type");
    }

    @Override
    public IVehicle createAAAArtillery(ICountry country) throws PWCGException
    {
        AAAArtillery aaaArtillery = new AAAArtillery();
        aaaArtillery.makeRandomVehicleFromSet(country);

        return aaaArtillery;
    }

    @Override
    public IVehicle cloneAAAArtillery(IVehicle source) throws PWCGException
    {
        if (source instanceof AAAArtillery)
        {
            AAAArtillery sourceAAAArtillery = (AAAArtillery)source;
            return sourceAAAArtillery.copy();
        }
        
        throw new PWCGException ("Attempt to clone incorrect vehicle type");
    }

    @Override
    public IVehicle createATArtillery(ICountry country) throws PWCGException
    {
        Artillery artillery = new Artillery();
        artillery.makeRandomVehicleFromSet(country);
        
        return artillery;
    }

    @Override
    public IVehicle cloneATArtillery(IVehicle source) throws PWCGException
    {
        if (source instanceof Artillery)
        {
            Artillery sourceArtillery = (Artillery)source;
            return sourceArtillery.copy();
        }
        
        throw new PWCGException ("Attempt to clone incorrect vehicle type");
    }

    @Override
    public IVehicle createAAAMachineGun(ICountry country) throws PWCGException
    {
        AAAMachineGun aaaMachineGun = new AAAMachineGun();
        aaaMachineGun.makeRandomVehicleFromSet(country);

        return aaaMachineGun;
    }

    @Override
    public IVehicle cloneAAAMachineGun(IVehicle source) throws PWCGException
    {
        if (source instanceof AAAMachineGun)
        {
            AAAMachineGun sourceAAAMachineGun = (AAAMachineGun)source;
            return sourceAAAMachineGun.copy();
        }
        
        throw new PWCGException ("Attempt to clone incorrect vehicle type");
    }

    @Override
    public IVehicle createRadioBeacon(Flight flight)  throws PWCGException
    {
        RadioBeacon radioBeacon = new RadioBeacon();
        radioBeacon.makeRandomVehicleFromSet(flight.getCountry());

        radioBeacon.initialize(flight);
        radioBeacon.populateEntity();
        
        return radioBeacon;
    }

    @Override
    public IVehicle createSpotLight(ICountry country) throws PWCGException
    {
        SpotLight spotLight = new SpotLight();
        spotLight.makeRandomVehicleFromSet(country);

        return spotLight;
    }

    @Override
    public IVehicle createTent(ICountry country)
    {
        return null;
    }

    @Override
    public IVehicle cloneTent(IVehicle source) throws PWCGException
    {
        return null;
    }

    @Override
    public IVehicle createAmmoCrate(ICountry country)
    {
        return null;
    }

    @Override
    public IVehicle cloneAmmoCrate(IVehicle source) throws PWCGException
    {
        return null;
    }
}
