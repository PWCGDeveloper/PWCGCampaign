package pwcg.campaign.group.airfield.staticobject;

import java.util.Date;

import pwcg.campaign.api.IAirfield;
import pwcg.campaign.api.IAirfieldObjectSelector;
import pwcg.campaign.group.airfield.hotspot.HotSpot;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Orientation;
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
        Orientation objectOrientation = Orientation.createRandomOrientation();
        IVehicle airfieldObject = StaticObjectFactory.createStaticObject(airfield.getCountry(date), date, VehicleClass.StaticAirfield);        
        airfieldObject.setPosition(hotSpot.getPosition().copy());
        airfieldObject.setOrientation(objectOrientation);
        airfieldObject.populateEntity();

        return airfieldObject;
    }

}
