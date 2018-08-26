package pwcg.campaign.ww2.ground.staticobject;

import java.util.ArrayList;
import java.util.List;

import pwcg.campaign.api.ICountry;
import pwcg.campaign.api.Side;
import pwcg.campaign.context.Country;
import pwcg.campaign.ww1.ground.vehicle.VehicleDefinition;
import pwcg.core.exception.PWCGException;

public class AirfieldObject extends StaticObject
{
    private static final List<VehicleDefinition> germanAirfieldObjects = new ArrayList<VehicleDefinition>() 
    {
        private static final long serialVersionUID = 1L;
        {
            add(new VehicleDefinition("blocks\\", "blocks\\", "art_position_amb1", Country.GERMANY));
            add(new VehicleDefinition("blocks\\", "blocks\\", "art_position_amb2", Country.GERMANY));
        }
    };
    
    private static final List<VehicleDefinition> russianAirfieldObjects = new ArrayList<VehicleDefinition>() 
    {
        private static final long serialVersionUID = 1L;
        {
            add(new VehicleDefinition("blocks\\", "blocks\\", "art_position_amb1", Country.RUSSIA));
            add(new VehicleDefinition("blocks\\", "blocks\\", "art_position_amb2", Country.RUSSIA));
        }
    };

	public AirfieldObject() throws PWCGException 
	{
		super();
	}

    @Override
    public List<VehicleDefinition> getAllVehicleDefinitions()
    {
        List<VehicleDefinition> allvehicleDefinitions = new ArrayList<>();
        allvehicleDefinitions.addAll(germanAirfieldObjects);
        allvehicleDefinitions.addAll(russianAirfieldObjects);
        return allvehicleDefinitions;
    }

    @Override
    public void makeRandomVehicleFromSet(ICountry country) throws PWCGException
    {
        List<VehicleDefinition> vehicleSet = null;;
        if (country.getSideNoNeutral() == Side.ALLIED)
        {
            vehicleSet = russianAirfieldObjects;
        }
        else if (country.getSideNoNeutral() == Side.AXIS)
        {
            vehicleSet = germanAirfieldObjects;
        }
        
        displayName = "Equipment";
        makeRandomVehicleInstance(vehicleSet);
    }

    public AirfieldObject copy () throws PWCGException 
	{
		AirfieldObject clonedObject = new AirfieldObject();
		super.copy(clonedObject);
		return clonedObject;
	}
}
