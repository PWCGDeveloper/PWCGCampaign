package pwcg.campaign.group.airfield.staticobject;

import java.util.Date;

import pwcg.campaign.api.IAirfield;
import pwcg.campaign.api.IAirfieldObjectSelector;
import pwcg.campaign.group.airfield.hotspot.HotSpot;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Orientation;
import pwcg.core.utils.RandomNumberGenerator;
import pwcg.mission.ground.vehicle.IVehicle;
import pwcg.mission.ground.vehicle.VehicleClass;

public class AirfieldObjectSelector implements IAirfieldObjectSelector
{
	private Date date;
	
    public AirfieldObjectSelector(Date date)
	{
    	this.date = date;
	}

    @Override
    public IVehicle createAirfieldObject(HotSpot hotSpot, IAirfield airfield) throws PWCGException 
    {
        // Finally put the object in its place - orientation is random
        double orientation = RandomNumberGenerator.getRandom(360);
        Orientation objectOrientation = new Orientation();
        objectOrientation.setyOri(orientation);
            

        IVehicle airfieldObject = StaticObjectFactory.createStaticObject(airfield.getCountry(date), date, VehicleClass.StaticAirfield);        
        airfieldObject.setPosition(hotSpot.getPosition().copy());
        airfieldObject.setOrientation(objectOrientation);
        airfieldObject.populateEntity();

        return airfieldObject;
    }

}
