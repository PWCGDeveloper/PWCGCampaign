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

public class Tank extends Vehicle
{
    private static final List<VehicleDefinition> germanTank = new ArrayList<VehicleDefinition>() 
    {
        private static final long serialVersionUID = 1L;
        {
            add(new VehicleDefinition("", "vehicles\\mk4\\", "mk4fger", Country.GERMANY));
            add(new VehicleDefinition("", "vehicles\\mk4\\", "mk4mger", Country.GERMANY));
            add(new VehicleDefinition("", "vehicles\\a7v\\", "a7v-l", Country.GERMANY));
        }
    };

    private static final List<VehicleDefinition> britishTank = new ArrayList<VehicleDefinition>() 
    {
        private static final long serialVersionUID = 1L;
        {
            add(new VehicleDefinition("", "vehicles\\mk4\\", "mk4f", Country.BRITAIN));
            add(new VehicleDefinition("", "vehicles\\mk4\\", "mk4m", Country.BRITAIN));
            add(new VehicleDefinition("", "vehicles\\mk5\\", "mk5f", Country.BRITAIN));
            add(new VehicleDefinition("", "vehicles\\mk5\\", "mk5m", Country.BRITAIN));
        }
    };

    private static final List<VehicleDefinition> frenchTank = new ArrayList<VehicleDefinition>() 
    {
        private static final long serialVersionUID = 1L;
        {
            add(new VehicleDefinition("", "vehicles\\ft17\\", "ft17c", Country.BRITAIN));
            add(new VehicleDefinition("", "vehicles\\ft17\\", "ft17m", Country.BRITAIN));
            add(new VehicleDefinition("", "vehicles\\whippet\\", "whippet", Country.BRITAIN));
            add(new VehicleDefinition("", "vehicles\\ca1\\", "ca1", Country.BRITAIN));
            add(new VehicleDefinition("", "vehicles\\stchamond\\", "stchamond", Country.BRITAIN));
        }
    };
    
    protected Tank()
    {
        super();
    }

    @Override
    public List<VehicleDefinition> getAllVehicleDefinitions()
    {
        List<VehicleDefinition> allvehicleDefinitions = new ArrayList<>();
        allvehicleDefinitions.addAll(germanTank);
        allvehicleDefinitions.addAll(britishTank);
        allvehicleDefinitions.addAll(frenchTank);
        return allvehicleDefinitions;
    }

    @Override
    public void makeRandomVehicleFromSet(ICountry country) throws PWCGException
    {
        List<VehicleDefinition> vehicleSet = null;;
        if (country.getSideNoNeutral() == Side.ALLIED)
        {
            if (country.getCountry() == Country.BRITAIN)
            {
                vehicleSet = britishTank;
            }
            else
            {
                vehicleSet = frenchTank;
            }
        }
        else if (country.getSideNoNeutral() == Side.AXIS)
        {
            vehicleSet = germanTank;
        }
        
        displayName = "Tank";
        makeRandomVehicleInstance(vehicleSet);
    }


	public Tank copy () throws PWCGException 
	{
		Tank tank = new Tank();
		
		tank.index = IndexGenerator.getInstance().getNextIndex();
		
		tank.vehicleType = this.vehicleType;
		tank.displayName = this.displayName;
		tank.linkTrId = this.linkTrId;
		tank.script = this.script;
		tank.model = this.model;
		tank.Desc = this.Desc;
		tank.aiLevel = this.aiLevel;
		tank.numberInFormation = this.numberInFormation;
		tank.vulnerable = this.vulnerable;
		tank.engageable = this.engageable;
		tank.limitAmmo = this.limitAmmo;
		tank.damageReport = this.damageReport;
		tank.country = this.country;
		tank.damageThreshold = this.damageThreshold; 
		
		tank.position = new Coordinate();
		tank.orientation = new Orientation();
		
		tank.entity = new McuTREntity();
		
		tank.populateEntity();
		
		return tank;
	}
}
