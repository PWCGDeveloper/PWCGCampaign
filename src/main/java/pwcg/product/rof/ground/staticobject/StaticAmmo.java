package pwcg.product.rof.ground.staticobject;

import java.util.ArrayList;
import java.util.List;

import pwcg.campaign.api.ICountry;
import pwcg.campaign.api.Side;
import pwcg.campaign.context.Country;
import pwcg.core.exception.PWCGException;
import pwcg.mission.ground.vehicle.VehicleDefinition;

public class StaticAmmo extends StaticObject
{
    private static final List<VehicleDefinition> germanAmmo = new ArrayList<VehicleDefinition>() 
    {
        private static final long serialVersionUID = 1L;
        {
            add(new VehicleDefinition("", "battlefield\\", "shells_01", Country.GERMANY));
            add(new VehicleDefinition("", "battlefield\\", "shells_02", Country.GERMANY));
            add(new VehicleDefinition("", "battlefield\\", "shells_03", Country.GERMANY));
        }
    };
    
    private static final List<VehicleDefinition> alliedAmmo = new ArrayList<VehicleDefinition>() 
    {
        private static final long serialVersionUID = 1L;
        {
            add(new VehicleDefinition("", "battlefield\\", "shells_01", Country.BRITAIN));
            add(new VehicleDefinition("", "battlefield\\", "shells_02", Country.BRITAIN));
            add(new VehicleDefinition("", "battlefield\\", "shells_03", Country.BRITAIN));
        }
    };

    public StaticAmmo() throws PWCGException 
    {
        super();
    }

    @Override
    public List<VehicleDefinition> getAllVehicleDefinitions()
    {
        List<VehicleDefinition> allvehicleDefinitions = new ArrayList<>();
        allvehicleDefinitions.addAll(germanAmmo);
        allvehicleDefinitions.addAll(alliedAmmo);
        return allvehicleDefinitions;
    }

    @Override
    public void makeRandomVehicleFromSet(ICountry country) throws PWCGException
    {
        List<VehicleDefinition> vehicleSet = null;;
        if (country.getSideNoNeutral() == Side.ALLIED)
        {
            vehicleSet = alliedAmmo;
        }
        else if (country.getSideNoNeutral() == Side.AXIS)
        {
            vehicleSet = germanAmmo;
        }
        
        displayName = "Ammo";
        makeRandomVehicleInstance(vehicleSet);
    }

	public StaticAmmo copy () throws PWCGException 
	{
		StaticAmmo clonedObject = new StaticAmmo();
		
		super.makeCopy(clonedObject);
		
		return clonedObject;
	}
}
