package pwcg.campaign.group.airfield.staticobject;

import java.util.Date;

import pwcg.campaign.api.ICountry;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.group.airfield.Airfield;
import pwcg.campaign.group.airfield.hotspot.HotSpot;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Orientation;
import pwcg.mission.ground.vehicle.IVehicle;
import pwcg.mission.ground.vehicle.VehicleClass;

public class AirfieldObjectSelector
{
	private Date date;
	
    public AirfieldObjectSelector(Date date)
	{
    	this.date = date;
	}

    public IVehicle createAirfieldObject(HotSpot hotSpot, Airfield airfield, ICountry airfieldCountry) throws PWCGException 
    {
        Orientation objectOrientation = Orientation.createRandomOrientation();
        
        ICountry groundObjectCountry = PWCGContext.getInstance().getCurrentMap().getGroundCountryForMapBySide(airfieldCountry.getSide());
                
        IVehicle airfieldObject = StaticObjectFactory.createStaticObject(groundObjectCountry, date, VehicleClass.StaticAirfield);        
        airfieldObject.setPosition(hotSpot.getPosition().copy());
        airfieldObject.setOrientation(objectOrientation);
        airfieldObject.populateEntity();

        return airfieldObject;
    }
}
