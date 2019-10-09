package pwcg.product.fc.ground.vehicle;

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

class AAAArtillery extends Vehicle
{
    private static final List<VehicleDefinition> germanAAAArtillery = new ArrayList<VehicleDefinition>() 
    {
        private static final long serialVersionUID = 1L;
        {
            add(new VehicleDefinition("vehicles\\", "artillery\\flak36\\", "flak36", Country.GERMANY));
            add(new VehicleDefinition("vehicles\\", "artillery\\flak37\\", "flak37", Country.GERMANY));
            add(new VehicleDefinition("vehicles\\", "artillery\\flak38\\", "flak38", Country.GERMANY));
        }
    };

    private static final List<VehicleDefinition> britishAAAArtillery = new ArrayList<VehicleDefinition>() 
    {
        private static final long serialVersionUID = 1L;
        {
            add(new VehicleDefinition("vehicles\\", "artillery\\52k\\", "52k", Country.BRITAIN));
            add(new VehicleDefinition("vehicles\\", "artillery\\61k\\", "61k", Country.BRITAIN));
            add(new VehicleDefinition("vehicles\\", "artillery\\72k\\", "72k", Country.BRITAIN));
        }
    };
    
    public AAAArtillery()
    {
        super();
    }

    @Override
    public List<VehicleDefinition> getAllVehicleDefinitions()
    {
        List<VehicleDefinition> allvehicleDefinitions = new ArrayList<>();
        allvehicleDefinitions.addAll(germanAAAArtillery);
        allvehicleDefinitions.addAll(britishAAAArtillery);
        return allvehicleDefinitions;
    }

    @Override
    public void makeRandomVehicleFromSet(ICountry country) throws PWCGException
    {
        List<VehicleDefinition> vehicleSet = null;;
        if (country.getSideNoNeutral() == Side.ALLIED)
        {
            vehicleSet = britishAAAArtillery;
        }
        else if (country.getSideNoNeutral() == Side.AXIS)
        {
            vehicleSet = germanAAAArtillery;
        }
        
        displayName = "AAA Gun";
        makeRandomVehicleInstance(vehicleSet);
    }

	public AAAArtillery copy () 
	{
		AAAArtillery mg = new AAAArtillery();
		
		mg.index = IndexGenerator.getInstance().getNextIndex();
		
		mg.vehicleType = this.vehicleType;
		mg.displayName = this.displayName;
		mg.linkTrId = this.linkTrId;
		mg.script = this.script;
		mg.model = this.model;
		mg.Desc = this.Desc;
		mg.aiLevel = this.aiLevel;
		mg.numberInFormation = this.numberInFormation;
		mg.vulnerable = this.vulnerable;
		mg.engageable = this.engageable;
		mg.limitAmmo = this.limitAmmo;
		mg.damageReport = this.damageReport;
		mg.country = this.country;
		mg.damageThreshold = this.damageThreshold; 
		
		mg.position = new Coordinate();
		mg.orientation = new Orientation();
		
		mg.entity = new McuTREntity();
		
		mg.populateEntity();
		
		return mg;
	}

    @Override
    public void makeVehicleFromDefinition(VehicleDefinition vehicleDefinition) throws PWCGException
    {
        // TODO Auto-generated method stub
        
    }
}
