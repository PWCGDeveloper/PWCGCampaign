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

public class MachineGun extends Vehicle
{
    private static final List<VehicleDefinition> germanMachineGuns = new ArrayList<VehicleDefinition>() 
    {
        private static final long serialVersionUID = 1L;
        {
            add(new VehicleDefinition("", "artillery\\machineguns\\", "lmg08", Country.GERMANY));
        }
    };

    private static final List<VehicleDefinition> alliedMachineGuns = new ArrayList<VehicleDefinition>() 
    {
        private static final long serialVersionUID = 1L;
        {
            add(new VehicleDefinition("", "artillery\\machineguns\\", "hotchkiss", Country.FRANCE));
        }
    };

    
    public MachineGun()
    {
        super();
    }

    @Override
    public List<VehicleDefinition> getAllVehicleDefinitions()
    {
        List<VehicleDefinition> allvehicleDefinitions = new ArrayList<>();
        allvehicleDefinitions.addAll(germanMachineGuns);
        allvehicleDefinitions.addAll(alliedMachineGuns);
        return allvehicleDefinitions;
    }

    @Override
    public void makeRandomVehicleFromSet(ICountry country) throws PWCGException
    {
        List<VehicleDefinition> vehicleSet = null;;
        if (country.getSideNoNeutral() == Side.ALLIED)
        {
            vehicleSet = alliedMachineGuns;
        }
        else if (country.getSideNoNeutral() == Side.AXIS)
        {
            vehicleSet = germanMachineGuns;
        }
        
        displayName = "Machine Gun";
        makeRandomVehicleInstance(vehicleSet);
    }

	public MachineGun copy () throws PWCGException 
	{
		MachineGun mg = new MachineGun();
		
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

}
