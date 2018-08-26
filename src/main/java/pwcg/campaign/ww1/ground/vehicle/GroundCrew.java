package pwcg.campaign.ww1.ground.vehicle;

import java.util.ArrayList;
import java.util.List;

import pwcg.campaign.api.ICountry;
import pwcg.campaign.api.Side;
import pwcg.campaign.context.Country;
import pwcg.campaign.utils.IndexGenerator;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.location.Orientation;
import pwcg.mission.mcu.McuTREntity;

public class GroundCrew extends Vehicle
{
    private static final List<VehicleDefinition> germanGroundCrews = new ArrayList<VehicleDefinition>() 
    {
        private static final long serialVersionUID = 1L;
        {
            add(new VehicleDefinition("", "firingpoint\\soldiers\\", "groundcrew_01", Country.GERMANY));
            add(new VehicleDefinition("", "firingpoint\\soldiers\\", "groundcrew_02", Country.GERMANY));
        }
    };

    private static final List<VehicleDefinition> alliedGroundCrews = new ArrayList<VehicleDefinition>() 
    {
        private static final long serialVersionUID = 1L;
        {
            add(new VehicleDefinition("", "firingpoint\\soldiers\\", "groundcrewen_01", Country.BRITAIN));
            add(new VehicleDefinition("", "firingpoint\\soldiers\\", "groundcrewen_02", Country.BRITAIN));
        }
    };
    
    public GroundCrew()
    {
        super();
    }
    
    @Override
    public List<VehicleDefinition> getAllVehicleDefinitions()
    {
        List<VehicleDefinition> allvehicleDefinitions = new ArrayList<>();
        allvehicleDefinitions.addAll(germanGroundCrews);
        allvehicleDefinitions.addAll(alliedGroundCrews);
        return allvehicleDefinitions;
    }

    @Override
    public void makeRandomVehicleFromSet(ICountry country) throws PWCGException
    {
        List<VehicleDefinition> vehicleSet = null;;
        if (country.getSideNoNeutral() == Side.ALLIED)
        {
            vehicleSet = alliedGroundCrews;
        }
        else if (country.getSideNoNeutral() == Side.AXIS)
        {
            vehicleSet = germanGroundCrews;
        }
        
        displayName = "Ground Crew";
        makeRandomVehicleInstance(vehicleSet);
    }

	public GroundCrew copy () throws PWCGException 
	{
		GroundCrew groundCrew = new GroundCrew();
		
		groundCrew.index = IndexGenerator.getInstance().getNextIndex();
		
		groundCrew.vehicleType = this.vehicleType;
		groundCrew.displayName = this.displayName;
		groundCrew.linkTrId = this.linkTrId;
		groundCrew.script = this.script;
		groundCrew.model = this.model;
		groundCrew.Desc = this.Desc;
		groundCrew.aiLevel = this.aiLevel;
		groundCrew.numberInFormation = this.numberInFormation;
		groundCrew.vulnerable = this.vulnerable;
		groundCrew.engageable = this.engageable;
		groundCrew.limitAmmo = this.limitAmmo;
		groundCrew.damageReport = this.damageReport;
		groundCrew.country = this.country;
		groundCrew.damageThreshold = this.damageThreshold; 
		
		groundCrew.position = new Coordinate();
		groundCrew.orientation = new Orientation();
		
		groundCrew.entity = new McuTREntity();
		
		groundCrew.populateEntity();
		
		return groundCrew;
	}

}
