package pwcg.campaign.ww1.airfield;

import java.util.Date;

import pwcg.campaign.Campaign;
import pwcg.campaign.api.IAirfield;
import pwcg.campaign.api.IAirfieldObjectSelector;
import pwcg.campaign.context.PWCGContextManager;
import pwcg.campaign.group.airfield.HotSpot;
import pwcg.campaign.ww1.ground.staticobject.AirfieldObject;
import pwcg.campaign.ww1.ground.staticobject.WaterTower;
import pwcg.campaign.ww1.ground.vehicle.GroundCrew;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Orientation;
import pwcg.core.utils.RandomNumberGenerator;
import pwcg.mission.ground.vehicle.IVehicle;

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
        
        double roll = RandomNumberGenerator.getRandom(100);
        if (roll < 20)
        {
            // Since ground crew are AI objects, player field only
            Campaign campaign = PWCGContextManager.getInstance().getCampaign();
            String playerAirfieldName = campaign.getAirfieldName();
            if (playerAirfieldName.equals(airfield.getName()))
            {
                GroundCrew groundCrew = new GroundCrew();
                groundCrew.makeRandomVehicleFromSet(airfield.getCountry(date));
                if (groundCrew.vehicleExists())
                {
                    airfieldStuff = groundCrew;
                }
            }
        }
        
        airfieldObject.setPosition(hotSpot.getPosition().copy());
        airfieldObject.setOrientation(objectOrientation);
        airfieldObject.populateEntity();

        return airfieldStuff;
    }

}
