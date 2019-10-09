package pwcg.product.rof.ground.vehicle;

import java.util.ArrayList;
import java.util.List;

import pwcg.campaign.api.ICountry;
import pwcg.campaign.api.Side;
import pwcg.campaign.context.Country;
import pwcg.campaign.utils.IndexGenerator;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.location.Orientation;
import pwcg.mission.ground.vehicle.VehicleDefinition;
import pwcg.mission.mcu.McuTREntity;

public class Infantry extends Vehicle
{
    private static final List<VehicleDefinition> germanInfantry = new ArrayList<VehicleDefinition>() 
    {
        private static final long serialVersionUID = 1L;
        {
            add(new VehicleDefinition("", "firingpoint\\soldiers\\", "infantryde", Country.GERMANY));
        }
    };

    private static final List<VehicleDefinition> alliedInfantry = new ArrayList<VehicleDefinition>() 
    {
        private static final long serialVersionUID = 1L;
        {
            add(new VehicleDefinition("", "firingpoint\\soldiers\\", "infantryen", Country.BRITAIN));
        }
    };
    
    public Infantry()
    {
        super();
    }
    
    @Override
    public List<VehicleDefinition> getAllVehicleDefinitions()
    {
        List<VehicleDefinition> allvehicleDefinitions = new ArrayList<>();
        allvehicleDefinitions.addAll(germanInfantry);
        allvehicleDefinitions.addAll(alliedInfantry);
        return allvehicleDefinitions;
    }

    @Override
    public void makeRandomVehicleFromSet(ICountry country) throws PWCGException
    {
        List<VehicleDefinition> vehicleSet = null;;
        if (country.getSideNoNeutral() == Side.ALLIED)
        {
            vehicleSet = alliedInfantry;
        }
        else if (country.getSideNoNeutral() == Side.AXIS)
        {
            vehicleSet = germanInfantry;
        }
        
        displayName = "Infantry";
        makeRandomVehicleInstance(vehicleSet);
    }

	public Infantry copy () throws PWCGException 
	{
		Infantry infantry = new Infantry();
		
		infantry.index = IndexGenerator.getInstance().getNextIndex();
		
		infantry.vehicleType = this.vehicleType;
		infantry.displayName = this.displayName;
		infantry.linkTrId = this.linkTrId;
		infantry.script = this.script;
		infantry.model = this.model;
		infantry.Desc = this.Desc;
		infantry.aiLevel = this.aiLevel;
		infantry.numberInFormation = this.numberInFormation;
		infantry.vulnerable = this.vulnerable;
		infantry.engageable = this.engageable;
		infantry.limitAmmo = this.limitAmmo;
		infantry.damageReport = this.damageReport;
		infantry.country = this.country;
		infantry.damageThreshold = this.damageThreshold; 
		
		infantry.position = new Coordinate();
		infantry.orientation = new Orientation();
		
		infantry.entity = new McuTREntity();
		
		infantry.populateEntity();
		
		return infantry;
	}

}
