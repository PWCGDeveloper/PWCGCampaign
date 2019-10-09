package pwcg.product.rof.ground.staticobject;

import java.util.ArrayList;
import java.util.List;

import pwcg.campaign.api.ICountry;
import pwcg.campaign.api.Side;
import pwcg.campaign.context.Country;
import pwcg.core.exception.PWCGException;
import pwcg.mission.ground.vehicle.VehicleDefinition;


public class StaticTent extends StaticObject
{
    private static final List<VehicleDefinition> germanTents = new ArrayList<VehicleDefinition>() 
    {
        private static final long serialVersionUID = 1L;
        {
            add(new VehicleDefinition("", "battlefield\\", "tent02", Country.GERMANY));
        }
    };
    
    private static final List<VehicleDefinition> alliedTents = new ArrayList<VehicleDefinition>() 
    {
        private static final long serialVersionUID = 1L;
        {
            add(new VehicleDefinition("", "battlefield\\", "tent02", Country.BRITAIN));
        }
    };

    public StaticTent() throws PWCGException 
    {
        super();
    }

    @Override
    public List<VehicleDefinition> getAllVehicleDefinitions()
    {
        List<VehicleDefinition> allvehicleDefinitions = new ArrayList<>();
        allvehicleDefinitions.addAll(germanTents);
        allvehicleDefinitions.addAll(alliedTents);
        return allvehicleDefinitions;
    }

    @Override
    public void makeRandomVehicleFromSet(ICountry country) throws PWCGException
    {
        List<VehicleDefinition> vehicleSet = null;;
        if (country.getSideNoNeutral() == Side.ALLIED)
        {
            vehicleSet = alliedTents;
        }
        else if (country.getSideNoNeutral() == Side.AXIS)
        {
            vehicleSet = germanTents;
        }
        
        displayName = "Tent";
        makeRandomVehicleInstance(vehicleSet);
    }

	public StaticTent copy () throws PWCGException 
	{
		StaticTent clonedObject = new StaticTent();
		
		super.makeCopy(clonedObject);
		
		return clonedObject;
	}

}
