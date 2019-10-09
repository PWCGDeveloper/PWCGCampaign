package pwcg.product.rof.ground.vehicle;

import pwcg.campaign.api.ICountry;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.RandomNumberGenerator;
import pwcg.mission.flight.Flight;
import pwcg.mission.ground.unittypes.transport.ShipConvoyUnit.ShipConvoyTypes;
import pwcg.mission.ground.vehicle.ITrainLocomotive;
import pwcg.mission.ground.vehicle.IVehicle;
import pwcg.mission.ground.vehicle.IVehicleFactory;
import pwcg.product.rof.ground.vehicle.Drifter;
import pwcg.product.rof.ground.staticobject.StaticAmmo;
import pwcg.product.rof.ground.staticobject.StaticTent;

public class RoFVehicleFactory implements IVehicleFactory
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
    public IVehicle createCargoTruck(ICountry country) throws PWCGException
    {
        int roll = RandomNumberGenerator.getRandom(100);
        if (roll < 50)
        {
            Truck truck = new Truck();
            truck.makeRandomVehicleFromSet(country);        
            return truck;
        }
        else
        {
            Wagon wagon = new Wagon();
            wagon.makeRandomVehicleFromSet(country);        
            return wagon;
        }
    }

    @Override
    public IVehicle createCar(ICountry country) throws PWCGException
    {
        return createCargoTruck(country);
    }

    @Override
    public IVehicle createAAATruck(ICountry country) throws PWCGException
    {
        return createCargoTruck(country);
    }

    @Override
    public IVehicle cloneTruck(IVehicle source) throws PWCGException
    {
        if (source instanceof Truck)
        {
            Truck sourceTruck = (Truck)source;
            return sourceTruck.copy();
        }
        
        if (source instanceof Wagon)
        {
            Wagon sourceWagon = (Wagon)source;
            return sourceWagon.copy();
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

        TrainCoalCar coalCar = new TrainCoalCar();
        coalCar.makeRandomVehicleFromSet(country);        
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
        // For RoF us regular artillery
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
        throw new PWCGException("Unsupported vehicle type: radio beacon");
    }

    @Override
    public IVehicle createSpotLight(ICountry country) throws PWCGException
    {
        SpotLight spotLight = new SpotLight();
        spotLight.makeRandomVehicleFromSet(country);        
        return spotLight;
    }

    @Override
    public IVehicle createTent(ICountry country) throws PWCGException
    {
        StaticTent tent = new StaticTent();
        tent.makeRandomVehicleFromSet(country);
        return tent;
    }

    @Override
    public IVehicle cloneTent(IVehicle source) throws PWCGException
    {
        if (source instanceof StaticTent)
        {
            StaticTent tent = (StaticTent)source;
            return tent.copy();
        }
        
        throw new PWCGException ("Attempt to clone incorrect vehicle type");
    }

    @Override
    public IVehicle createAmmoCrate(ICountry country) throws PWCGException
    {
        StaticAmmo ammoCrate = new StaticAmmo();
        ammoCrate.makeRandomVehicleFromSet(country);
        return ammoCrate;
    }

    @Override
    public IVehicle cloneAmmoCrate(IVehicle source) throws PWCGException
    {
        if (source instanceof StaticAmmo)
        {
            StaticAmmo ammoCrate = (StaticAmmo)source;
            return ammoCrate.copy();
        }
        
        throw new PWCGException ("Attempt to clone incorrect vehicle type");
    }
}
