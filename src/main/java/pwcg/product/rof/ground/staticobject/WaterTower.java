package pwcg.product.rof.ground.staticobject;

import java.util.ArrayList;
import java.util.List;

import pwcg.campaign.api.ICountry;
import pwcg.campaign.api.Side;
import pwcg.campaign.context.Country;
import pwcg.core.exception.PWCGException;
import pwcg.mission.ground.vehicle.VehicleDefinition;


public class WaterTower extends StaticObject
{
    private static final List<VehicleDefinition> germanTents = new ArrayList<VehicleDefinition>() 
    {
        private static final long serialVersionUID = 1L;
        {
            add(new VehicleDefinition("", "blocks\\", "watertower_01", Country.GERMANY));
            add(new VehicleDefinition("", "blocks\\", "watertower_02", Country.GERMANY));
            add(new VehicleDefinition("", "blocks\\", "watertower_03", Country.GERMANY));
        }
    };
    
    private static final List<VehicleDefinition> alliedTents = new ArrayList<VehicleDefinition>() 
    {
        private static final long serialVersionUID = 1L;
        {
            add(new VehicleDefinition("", "blocks\\", "watertower_01", Country.BRITAIN));
            add(new VehicleDefinition("", "blocks\\", "watertower_02", Country.BRITAIN));
            add(new VehicleDefinition("", "blocks\\", "watertower_03", Country.BRITAIN));
        }
    };

    public WaterTower() throws PWCGException 
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
        
        displayName = "Water Tower";
        makeRandomVehicleInstance(vehicleSet);
    }

	public WaterTower copy () throws PWCGException 
	{
		WaterTower clonedObject = new WaterTower();
		
		super.makeCopy(clonedObject);
		
		return clonedObject;
	}
}
