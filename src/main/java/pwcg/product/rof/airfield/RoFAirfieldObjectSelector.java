package pwcg.product.rof.airfield;

import java.util.Date;

import pwcg.campaign.api.IAirfield;
import pwcg.campaign.api.IAirfieldObjectSelector;
import pwcg.campaign.group.airfield.HotSpot;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Orientation;
import pwcg.core.utils.RandomNumberGenerator;
import pwcg.mission.ground.vehicle.IVehicle;
import pwcg.product.rof.ground.staticobject.AirfieldObject;
import pwcg.product.rof.ground.staticobject.WaterTower;

public class RoFAirfieldObjectSelector implements IAirfieldObjectSelector
{
	private Date date;
	
    public RoFAirfieldObjectSelector(Date date)
	{
    	this.date = date;
	}

	@Override
    public IVehicle createWaterTower(HotSpot hotSpot, IAirfield airfield) throws PWCGException 
    {
        // Finally put the object in its place - orientation is random
        double orientation = RandomNumberGenerator.getRandom(360);
        Orientation objectOrientation = new Orientation();
        objectOrientation.setyOri(orientation);
            

        IVehicle airfieldObject = null;
        // First hot spot is the water tower
        airfieldObject = new WaterTower();
        airfieldObject.makeRandomVehicleFromSet(airfield.getCountry(date));

        airfieldObject.setPosition(hotSpot.getPosition().copy());
        airfieldObject.setOrientation(objectOrientation);
        airfieldObject.populateEntity();
        
        return airfieldObject;
    }

    @Override
    public IVehicle createAirfieldObject(HotSpot hotSpot, IAirfield airfield) throws PWCGException 
    {
        // Finally put the object in its place - orientation is random
        double orientation = RandomNumberGenerator.getRandom(360);
        Orientation objectOrientation = new Orientation();
        objectOrientation.setyOri(orientation);
            
        IVehicle airfieldStuff;
        
        AirfieldObject airfieldObject = new AirfieldObject();
        airfieldObject.makeRandomVehicleFromSet(airfield.getCountry(date));
        airfieldStuff = airfieldObject;
        
        airfieldObject.setPosition(hotSpot.getPosition().copy());
        airfieldObject.setOrientation(objectOrientation);
        airfieldObject.populateEntity();

        return airfieldStuff;
    }

}
