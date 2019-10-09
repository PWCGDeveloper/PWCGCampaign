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

public class PillBox extends Vehicle
{
    private static final List<VehicleDefinition> germanPillBoxes = new ArrayList<VehicleDefinition>() 
    {
        private static final long serialVersionUID = 1L;
        {
            add(new VehicleDefinition("", "firingpoint\\pillbox\\", "pillbox01", Country.GERMANY));
            add(new VehicleDefinition("", "firingpoint\\pillbox\\", "pillbox02", Country.GERMANY));
            add(new VehicleDefinition("", "firingpoint\\pillbox\\", "pillbox03", Country.GERMANY));
            add(new VehicleDefinition("", "firingpoint\\pillbox\\", "pillbox04", Country.GERMANY));
        }
    };

    private static final List<VehicleDefinition> alliedPillBoxes = new ArrayList<VehicleDefinition>() 
    {
        private static final long serialVersionUID = 1L;
        {
            add(new VehicleDefinition("", "firingpoint\\pillbox\\", "pillbox01", Country.BRITAIN));
            add(new VehicleDefinition("", "firingpoint\\pillbox\\", "pillbox02", Country.BRITAIN));
            add(new VehicleDefinition("", "firingpoint\\pillbox\\", "pillbox03", Country.BRITAIN));
            add(new VehicleDefinition("", "firingpoint\\pillbox\\", "pillbox04", Country.BRITAIN));
        }
    };
    
    public PillBox()
    {
        super();
    }
    
    @Override
    public List<VehicleDefinition> getAllVehicleDefinitions()
    {
        List<VehicleDefinition> allvehicleDefinitions = new ArrayList<>();
        allvehicleDefinitions.addAll(germanPillBoxes);
        allvehicleDefinitions.addAll(alliedPillBoxes);
        return allvehicleDefinitions;
    }

    @Override
    public void makeRandomVehicleFromSet(ICountry country) throws PWCGException
    {
        List<VehicleDefinition> vehicleSet = null;;
        if (country.getSideNoNeutral() == Side.ALLIED)
        {
            vehicleSet = alliedPillBoxes;
        }
        else if (country.getSideNoNeutral() == Side.AXIS)
        {
            vehicleSet = germanPillBoxes;
        }
        
        displayName = "Pillbox";
        makeRandomVehicleInstance(vehicleSet);
    }

	public PillBox copy () throws PWCGException 
	{
		PillBox pillBox = new PillBox();
		
		pillBox.index = IndexGenerator.getInstance().getNextIndex();
		
		pillBox.vehicleType = this.vehicleType;
		pillBox.displayName = this.displayName;
		pillBox.linkTrId = this.linkTrId;
		pillBox.script = this.script;
		pillBox.model = this.model;
		pillBox.Desc = this.Desc;
		pillBox.aiLevel = this.aiLevel;
		pillBox.numberInFormation = this.numberInFormation;
		pillBox.vulnerable = this.vulnerable;
		pillBox.engageable = this.engageable;
		pillBox.limitAmmo = this.limitAmmo;
		pillBox.damageReport = this.damageReport;
		pillBox.country = this.country;
		pillBox.damageThreshold = this.damageThreshold; 
		
		pillBox.position = new Coordinate();
		pillBox.orientation = new Orientation();
		
		pillBox.entity = new McuTREntity();
		
		pillBox.populateEntity();
		
		return pillBox;
	}
}
