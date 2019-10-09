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

public class Wagon extends Vehicle
{
    private static final List<VehicleDefinition> germanWagon = new ArrayList<VehicleDefinition>() 
    {
        private static final long serialVersionUID = 1L;
        {
            add(new VehicleDefinition("", "vehicles\\ammowagon\\", "guncarriagede", Country.GERMANY));
            add(new VehicleDefinition("", "vehicles\\ammowagon\\", "ammowagon", Country.GERMANY));
        }
    };

    private static final List<VehicleDefinition> alliedWagon = new ArrayList<VehicleDefinition>() 
    {
        private static final long serialVersionUID = 1L;
        {
            add(new VehicleDefinition("", "vehicles\\ammowagon\\", "guncarriagede", Country.BRITAIN));
            add(new VehicleDefinition("", "vehicles\\ammowagon\\", "ammowagon", Country.BRITAIN));
        }
    };
    
    protected Wagon()
    {
        super();
    }

    @Override
    public List<VehicleDefinition> getAllVehicleDefinitions()
    {
        List<VehicleDefinition> allvehicleDefinitions = new ArrayList<>();
        allvehicleDefinitions.addAll(germanWagon);
        allvehicleDefinitions.addAll(alliedWagon);
        return allvehicleDefinitions;
    }

    @Override
    public void makeRandomVehicleFromSet(ICountry country) throws PWCGException
    {
        List<VehicleDefinition> vehicleSet = null;;
        if (country.getSideNoNeutral() == Side.ALLIED)
        {
            vehicleSet = alliedWagon;
        }
        else if (country.getSideNoNeutral() == Side.AXIS)
        {
            vehicleSet = germanWagon;
        }
        
        displayName = "Wagon";
        makeRandomVehicleInstance(vehicleSet);
    }

	public Wagon copy () throws PWCGException 
	{
		Wagon wagon = new Wagon();
		
		wagon.index = IndexGenerator.getInstance().getNextIndex();
		
		wagon.vehicleType = this.vehicleType;
		wagon.displayName = this.displayName;
		wagon.linkTrId = this.linkTrId;
		wagon.script = this.script;
		wagon.model = this.model;
		wagon.Desc = this.Desc;
		wagon.aiLevel = this.aiLevel;
		wagon.numberInFormation = this.numberInFormation;
		wagon.vulnerable = this.vulnerable;
		wagon.engageable = this.engageable;
		wagon.limitAmmo = this.limitAmmo;
		wagon.damageReport = this.damageReport;
		wagon.country = this.country;
		wagon.damageThreshold = this.damageThreshold; 
		
		wagon.position = new Coordinate();
		wagon.orientation = new Orientation();
		
		wagon.entity = new McuTREntity();
		
		wagon.populateEntity();
		
		return wagon;
	}
}
