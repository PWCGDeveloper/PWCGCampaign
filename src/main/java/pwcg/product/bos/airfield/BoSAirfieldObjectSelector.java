package pwcg.product.bos.airfield;

import java.util.Date;

import pwcg.campaign.api.IAirfield;
import pwcg.campaign.api.IAirfieldObjectSelector;
import pwcg.campaign.group.airfield.HotSpot;
import pwcg.product.bos.ground.staticobject.AirfieldObject;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Orientation;
import pwcg.core.utils.RandomNumberGenerator;
import pwcg.mission.ground.vehicle.IVehicle;

public class BoSAirfieldObjectSelector implements IAirfieldObjectSelector
{
	private Date date;
	
    public BoSAirfieldObjectSelector(Date date)
	{
    	this.date = date;
	}

	@Override
    public IVehicle createWaterTower(HotSpot hotSpot, IAirfield airfield) throws PWCGException 
    {
        // Water towers are predefined in BoS so just send back a random object
        return createAirfieldObject(hotSpot, airfield);
    }

    @Override
    public IVehicle createAirfieldObject(HotSpot hotSpot, IAirfield airfield) throws PWCGException 
    {
        // Finally put the object in its place - orientation is random
        double orientation = RandomNumberGenerator.getRandom(360);
        Orientation objectOrientation = new Orientation();
        objectOrientation.setyOri(orientation);
            

        IVehicle airfieldObject = new AirfieldObject();
        airfieldObject.makeRandomVehicleFromSet(airfield.getCountry(date));
        
        airfieldObject.setPosition(hotSpot.getPosition().copy());
        airfieldObject.setOrientation(objectOrientation);
        airfieldObject.populateEntity();

        return airfieldObject;
    }

}
