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

public class MovingInfantry extends Vehicle
{
    private static final List<VehicleDefinition> germanInfantry = new ArrayList<VehicleDefinition>() 
    {
        private static final long serialVersionUID = 1L;
        {
            add(new VehicleDefinition("", "vehicles\\platoon\\", "platoonde", Country.GERMANY));
            add(new VehicleDefinition("", "vehicles\\platoon\\", "platoonde02", Country.GERMANY));
        }
    };

    private static final List<VehicleDefinition> britishInfantry = new ArrayList<VehicleDefinition>() 
    {
        private static final long serialVersionUID = 1L;
        {
            add(new VehicleDefinition("", "vehicles\\platoon\\", "platoonen", Country.BRITAIN));
            add(new VehicleDefinition("", "vehicles\\platoon\\", "platoonen02", Country.BRITAIN));
        }
    };

    private static final List<VehicleDefinition> frenchInfantry = new ArrayList<VehicleDefinition>() 
    {
        private static final long serialVersionUID = 1L;
        {
            add(new VehicleDefinition("", "vehicles\\platoon\\", "platoonfr", Country.FRANCE));
            add(new VehicleDefinition("", "vehicles\\platoon\\", "platoonfr02", Country.FRANCE));
        }
    };
    
    public MovingInfantry()
    {
        super();
    }
    
    @Override
    public List<VehicleDefinition> getAllVehicleDefinitions()
    {
        List<VehicleDefinition> allvehicleDefinitions = new ArrayList<>();
        allvehicleDefinitions.addAll(germanInfantry);
        allvehicleDefinitions.addAll(britishInfantry);
        allvehicleDefinitions.addAll(frenchInfantry);
        return allvehicleDefinitions;
    }

    @Override
    public void makeRandomVehicleFromSet(ICountry country) throws PWCGException
    {
        List<VehicleDefinition> vehicleSet = null;;
        if (country.getSideNoNeutral() == Side.ALLIED)
        {
            if (country.getCountry() == Country.FRANCE)
            {
                vehicleSet = frenchInfantry;
            }
            else
            {
                vehicleSet = britishInfantry;
            }
        }
        else if (country.getSideNoNeutral() == Side.AXIS)
        {
            vehicleSet = germanInfantry;
        }
        
        displayName = "Infantry";
        makeRandomVehicleInstance(vehicleSet);
    }

	public MovingInfantry copy () throws PWCGException 
	{
		MovingInfantry infantry = new MovingInfantry();
		
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
