package pwcg.product.rof.ground.staticobject;

import java.util.ArrayList;
import java.util.List;

import pwcg.campaign.api.ICountry;
import pwcg.campaign.api.Side;
import pwcg.campaign.context.Country;
import pwcg.core.exception.PWCGException;
import pwcg.mission.ground.vehicle.VehicleDefinition;

public class AirfieldObject extends StaticObject
{
    private static final List<VehicleDefinition> germanAirfieldObjects = new ArrayList<VehicleDefinition>() 
    {
        private static final long serialVersionUID = 1L;
        {
            add(new VehicleDefinition("", "blocks\\", "airfieldobj_01", Country.GERMANY));
            add(new VehicleDefinition("", "blocks\\", "airfieldobj_02", Country.GERMANY));
            add(new VehicleDefinition("", "blocks\\", "airfieldobj_03", Country.GERMANY));
            add(new VehicleDefinition("", "blocks\\", "airfieldobj_04", Country.GERMANY));
            add(new VehicleDefinition("", "blocks\\", "airfieldobj_05", Country.GERMANY));
            add(new VehicleDefinition("", "blocks\\", "airfieldobj_06", Country.GERMANY));
            add(new VehicleDefinition("", "blocks\\", "airfieldobj_07", Country.GERMANY));
        }
    };
    
    private static final List<VehicleDefinition> alliedAirfieldObjects = new ArrayList<VehicleDefinition>() 
    {
        private static final long serialVersionUID = 1L;
        {
            add(new VehicleDefinition("", "blocks\\", "airfieldobj_01", Country.BRITAIN));
            add(new VehicleDefinition("", "blocks\\", "airfieldobj_02", Country.BRITAIN));
            add(new VehicleDefinition("", "blocks\\", "airfieldobj_03", Country.BRITAIN));
            add(new VehicleDefinition("", "blocks\\", "airfieldobj_04", Country.BRITAIN));
            add(new VehicleDefinition("", "blocks\\", "airfieldobj_05", Country.BRITAIN));
            add(new VehicleDefinition("", "blocks\\", "airfieldobj_06", Country.BRITAIN));
            add(new VehicleDefinition("", "blocks\\", "airfieldobj_07", Country.BRITAIN));
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
        allvehicleDefinitions.addAll(alliedAirfieldObjects);
        return allvehicleDefinitions;
    }

    @Override
    public void makeRandomVehicleFromSet(ICountry country) throws PWCGException
    {
        List<VehicleDefinition> vehicleSet = null;;
        if (country.getSideNoNeutral() == Side.ALLIED)
        {
            vehicleSet = alliedAirfieldObjects;
        }
        else if (country.getSideNoNeutral() == Side.AXIS)
        {
            vehicleSet = germanAirfieldObjects;
        }
        
        displayName = "Equipment";
        makeRandomVehicleInstance(vehicleSet);
    }
}
