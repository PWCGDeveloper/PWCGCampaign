package pwcg.mission.ground.vehicle;

import pwcg.campaign.api.ICountry;
import pwcg.core.exception.PWCGException;
import pwcg.mission.flight.Flight;
import pwcg.mission.ground.unittypes.transport.ShipConvoyUnit.ShipConvoyTypes;

public interface IVehicleFactory
{
    IVehicle createTank(ICountry country) throws PWCGException;

    IVehicle cloneTank(IVehicle source) throws PWCGException;

    IVehicle createCar(ICountry country) throws PWCGException;
    
    IVehicle createAAATruck(ICountry country) throws PWCGException;

    IVehicle createCargoTruck(ICountry country) throws PWCGException;

    IVehicle cloneTruck(IVehicle source) throws PWCGException;

    IVehicle createTrainCar(ICountry country) throws PWCGException;

    IVehicle cloneTrainCar(IVehicle source) throws PWCGException;

    ITrainLocomotive createTrainLocomotive(ICountry country) throws PWCGException;

    ITrainLocomotive cloneTrainLocomotive(IVehicle source) throws PWCGException;

    IVehicle cloneArtillery(IVehicle source) throws PWCGException;

    IVehicle createArtillery(ICountry country) throws PWCGException;

    IVehicle createAAAArtillery(ICountry country) throws PWCGException;

    IVehicle cloneAAAArtillery(IVehicle source) throws PWCGException;

    IVehicle createATArtillery(ICountry country) throws PWCGException;

    IVehicle cloneATArtillery(IVehicle source) throws PWCGException;

    IVehicle createAAAMachineGun(ICountry country) throws PWCGException;

    IVehicle cloneAAAMachineGun(IVehicle source) throws PWCGException;

    IVehicle createRadioBeacon(Flight flight)  throws PWCGException;

    IVehicle createSpotLight(ICountry country) throws PWCGException;
    
    IVehicle createTent(ICountry country) throws PWCGException;

    IVehicle cloneTent(IVehicle source) throws PWCGException;
    
    IVehicle createAmmoCrate(ICountry country) throws PWCGException;

    IVehicle cloneAmmoCrate(IVehicle source) throws PWCGException;

    IVehicle createBarge(ICountry country) throws PWCGException;
    
    IVehicle createShip(ICountry country, ShipConvoyTypes shipConvoyType) throws PWCGException;
}
