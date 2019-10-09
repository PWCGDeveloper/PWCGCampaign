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

public class Truck extends Vehicle
{
    private static final List<VehicleDefinition> germanWagon = new ArrayList<VehicleDefinition>() 
    {
        private static final long serialVersionUID = 1L;
        {
            add(new VehicleDefinition("", "vehicles\\daimlermarienfelde\\", "daimlermarienfelde", Country.GERMANY));
            add(new VehicleDefinition("", "vehicles\\daimlermarienfelde\\", "daimlermarienfelde_s", Country.GERMANY));
            add(new VehicleDefinition("", "vehicles\\benz\\", "benz_soft", Country.GERMANY));
            add(new VehicleDefinition("", "vehicles\\benz\\", "benz_open", Country.GERMANY));
        }
    };

    private static final List<VehicleDefinition> alliedWagon = new ArrayList<VehicleDefinition>() 
    {
        private static final long serialVersionUID = 1L;
        {
            add(new VehicleDefinition("", "vehicles\\quad\\", "quad", Country.BRITAIN));
            add(new VehicleDefinition("", "vehicles\\quad\\", "quada", Country.BRITAIN));
            add(new VehicleDefinition("", "vehicles\\leyland\\", "leyland", Country.BRITAIN));
            add(new VehicleDefinition("", "vehicles\\leyland\\", "leylands", Country.BRITAIN));
        }
    };
    
    protected Truck()
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

	public Truck copy () throws PWCGException 
	{
		Truck truck = new Truck();
		
		truck.index = IndexGenerator.getInstance().getNextIndex();
		
		truck.vehicleType = this.vehicleType;
		truck.displayName = this.displayName;
		truck.linkTrId = this.linkTrId;
		truck.script = this.script;
		truck.model = this.model;
		truck.Desc = this.Desc;
		truck.aiLevel = this.aiLevel;
		truck.numberInFormation = this.numberInFormation;
		truck.vulnerable = this.vulnerable;
		truck.engageable = this.engageable;
		truck.limitAmmo = this.limitAmmo;
		truck.damageReport = this.damageReport;
		truck.country = this.country;
		truck.damageThreshold = this.damageThreshold; 
		
		truck.position = new Coordinate();
		truck.orientation = new Orientation();
		
		truck.entity = new McuTREntity();
		
		truck.populateEntity();
		
		return truck;
	}
	
}
