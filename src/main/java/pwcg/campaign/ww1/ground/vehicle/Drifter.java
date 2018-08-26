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

public class Drifter extends Vehicle
{
    private static final List<VehicleDefinition> germanDrifters = new ArrayList<VehicleDefinition>() 
    {
        private static final long serialVersionUID = 1L;
        {
            add(new VehicleDefinition("", "vehicles\\platoon\\", "gerdrifter", Country.GERMANY));
        }
    };

    private static final List<VehicleDefinition> alliedDrifters = new ArrayList<VehicleDefinition>() 
    {
        private static final long serialVersionUID = 1L;
        {
            add(new VehicleDefinition("", "vehicles\\platoon\\", "hmsDrifter", Country.GERMANY));
            add(new VehicleDefinition("", "vehicles\\platoon\\", "hmsdrifter6pdraaa", Country.GERMANY));
        }
    };
    
    public Drifter()
    {
        super();
    }
    
    @Override
    public List<VehicleDefinition> getAllVehicleDefinitions()
    {
        List<VehicleDefinition> allvehicleDefinitions = new ArrayList<>();
        allvehicleDefinitions.addAll(germanDrifters);
        allvehicleDefinitions.addAll(alliedDrifters);
        return allvehicleDefinitions;
    }

    @Override
    public void makeRandomVehicleFromSet(ICountry country) throws PWCGException
    {
        List<VehicleDefinition> vehicleSet = null;;
        if (country.getSideNoNeutral() == Side.ALLIED)
        {
            vehicleSet = alliedDrifters;
        }
        else if (country.getSideNoNeutral() == Side.AXIS)
        {
            vehicleSet = germanDrifters;
        }
        
        displayName = "River Ship";
        makeRandomVehicleInstance(vehicleSet);
    }

	public Drifter copy () throws PWCGException 
	{
		Drifter ship = new Drifter();
		
		ship.index = IndexGenerator.getInstance().getNextIndex();
		
		ship.vehicleType = this.vehicleType;
		ship.displayName = this.displayName;
		ship.linkTrId = this.linkTrId;
		ship.script = this.script;
		ship.model = this.model;
		ship.Desc = this.Desc;
		ship.aiLevel = this.aiLevel;
		ship.numberInFormation = this.numberInFormation;
		ship.vulnerable = this.vulnerable;
		ship.engageable = this.engageable;
		ship.limitAmmo = this.limitAmmo;
		ship.damageReport = this.damageReport;
		ship.country = this.country;
		ship.damageThreshold = this.damageThreshold; 
		
		ship.position = new Coordinate();
		ship.orientation = new Orientation();
		
		ship.entity = new McuTREntity();
		
		ship.populateEntity();
		
		return ship;
	}

	/**
	 * Override to always enable ships
	 * @throws PWCGException 
	 */
	@Override
	public void populateEntity() throws PWCGException
	{
		super.populateEntity();
		entity.setEnabled(1);
	}
}
