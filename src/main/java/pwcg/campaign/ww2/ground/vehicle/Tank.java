package pwcg.campaign.ww2.ground.vehicle;

import java.io.BufferedWriter;
import java.util.ArrayList;
import java.util.List;

import pwcg.campaign.api.ICountry;
import pwcg.campaign.api.Side;
import pwcg.campaign.context.Country;
import pwcg.campaign.utils.IndexGenerator;
import pwcg.campaign.ww1.ground.vehicle.VehicleDefinition;
import pwcg.core.exception.PWCGException;
import pwcg.core.exception.PWCGIOException;
import pwcg.core.location.Coordinate;
import pwcg.core.location.Orientation;
import pwcg.mission.mcu.McuTREntity;

class Tank extends Vehicle
{
    private static final List<VehicleDefinition> germanTank = new ArrayList<VehicleDefinition>() 
    {
        private static final long serialVersionUID = 1L;
        {
            add(new VehicleDefinition("vehicles\\", "vehicles\\pz38t\\", "pz38t", Country.GERMANY));
            add(new VehicleDefinition("vehicles\\", "vehicles\\pziii-h\\", "pziii-h", Country.GERMANY));
            add(new VehicleDefinition("vehicles\\", "vehicles\\pziii-l\\", "pziii-l", Country.GERMANY));
            add(new VehicleDefinition("vehicles\\", "vehicles\\pziv-f1\\", "pziv-f1", Country.GERMANY));
            add(new VehicleDefinition("vehicles\\", "vehicles\\pziv-g\\", "pziv-g", Country.GERMANY));
            add(new VehicleDefinition("vehicles\\", "vehicles\\stug37l24\\", "stug37l24", Country.GERMANY));
            add(new VehicleDefinition("vehicles\\", "vehicles\\stug40l43\\", "stug40l43", Country.GERMANY));
            add(new VehicleDefinition("vehicles\\", "vehicles\\pz38t\\", "marderiii-h", Country.GERMANY));
        }
    };
    
    private static final List<VehicleDefinition> russianTank = new ArrayList<VehicleDefinition>() 
    {
        private static final long serialVersionUID = 1L;
        {
            add(new VehicleDefinition("vehicles\\", "vehicles\\ba10m\\", "ba10m", Country.RUSSIA));
            add(new VehicleDefinition("vehicles\\", "vehicles\\ba64\\", "ba64", Country.RUSSIA));
            add(new VehicleDefinition("vehicles\\", "vehicles\\bt7m\\", "bt7m", Country.RUSSIA));
            add(new VehicleDefinition("vehicles\\", "vehicles\\kv1-41\\", "kv1-41", Country.RUSSIA));
            add(new VehicleDefinition("vehicles\\", "vehicles\\kv1-42\\", "kv1-42", Country.RUSSIA));
            add(new VehicleDefinition("vehicles\\", "vehicles\\t34-76stz-41\\", "t34-76stz-41", Country.RUSSIA));
            add(new VehicleDefinition("vehicles\\", "vehicles\\t34-76stz\\", "t34-76stz", Country.RUSSIA));
            add(new VehicleDefinition("vehicles\\", "vehicles\\t70\\", "t70", Country.RUSSIA));
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
        allvehicleDefinitions.addAll(russianTank);
        return allvehicleDefinitions;
    }

    @Override
    public void makeRandomVehicleFromSet(ICountry country) throws PWCGException
    {
        List<VehicleDefinition> vehicleSet = null;;
        if (country.getSideNoNeutral() == Side.ALLIED)
        {
            vehicleSet = russianTank;
        }
        else if (country.getSideNoNeutral() == Side.AXIS)
        {
            vehicleSet = germanTank;
        }
        
        displayName = "Tank";
        makeRandomVehicleInstance(vehicleSet);
    }

	public Tank copy () 
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
	
	public void write(BufferedWriter writer) throws PWCGIOException
	{
		super.write(writer);
	}
	
	public void setOrientation (Orientation orient)
	{
		super.setOrientation(orient);
	}

	public void setPosition (Coordinate coord)
	{
		super.setPosition(coord);
	}
}
