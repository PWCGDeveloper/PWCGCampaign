package pwcg.product.bos.ground.vehicle;

import java.io.BufferedWriter;
import java.util.ArrayList;
import java.util.List;

import pwcg.campaign.api.ICountry;
import pwcg.campaign.api.Side;
import pwcg.campaign.context.Country;
import pwcg.campaign.utils.IndexGenerator;
import pwcg.core.exception.PWCGException;
import pwcg.core.exception.PWCGIOException;
import pwcg.core.location.Coordinate;
import pwcg.core.location.Orientation;
import pwcg.mission.ground.vehicle.VehicleDefinition;
import pwcg.mission.mcu.McuTREntity;

class ATArtillery extends Vehicle
{
    private static final List<VehicleDefinition> germanATArtillery = new ArrayList<VehicleDefinition>() 
    {
        private static final long serialVersionUID = 1L;
        {
            add(new VehicleDefinition("vehicles\\", "artillery\\pak35\\", "pak35", Country.GERMANY));
            add(new VehicleDefinition("vehicles\\", "artillery\\pak38\\", "pak38", Country.GERMANY));
            add(new VehicleDefinition("vehicles\\", "artillery\\pak40\\", "pak40", Country.GERMANY));
        }
    };

    private static final List<VehicleDefinition> russianATArtillery = new ArrayList<VehicleDefinition>() 
    {
        private static final long serialVersionUID = 1L;
        {
            add(new VehicleDefinition("vehicles\\", "artillery\\53k\\", "53k", Country.RUSSIA));
            add(new VehicleDefinition("vehicles\\", "artillery\\zis2gun\\", "zis2gun", Country.RUSSIA));
            add(new VehicleDefinition("vehicles\\", "artillery\\zis3gun\\", "zis3gun", Country.RUSSIA));
        }
    };
    
    public ATArtillery()
    {
        super();
    }

    @Override
    public List<VehicleDefinition> getAllVehicleDefinitions()
    {
        List<VehicleDefinition> allvehicleDefinitions = new ArrayList<>();
        allvehicleDefinitions.addAll(germanATArtillery);
        allvehicleDefinitions.addAll(russianATArtillery);
        return allvehicleDefinitions;
    }

    @Override
	public void makeRandomVehicleFromSet(ICountry country) throws PWCGException
    {
        List<VehicleDefinition> vehicleSet = null;;
        if (country.getSideNoNeutral() == Side.ALLIED)
        {
            vehicleSet = russianATArtillery;
        }
        else if (country.getSideNoNeutral() == Side.AXIS)
        {
            vehicleSet = germanATArtillery;
        }
        
        displayName = "Anti Tank Gun";
        makeRandomVehicleInstance(vehicleSet);
    }

	public ATArtillery copy () 
	{
		ATArtillery gun = new ATArtillery();
		
		gun.index = IndexGenerator.getInstance().getNextIndex();
		
		gun.vehicleType = this.vehicleType;
		gun.displayName = this.displayName;
		gun.linkTrId = this.linkTrId;
		gun.script = this.script;
		gun.model = this.model;
		gun.Desc = this.Desc;
		gun.aiLevel = this.aiLevel;
		gun.numberInFormation = this.numberInFormation;
		gun.vulnerable = this.vulnerable;
		gun.engageable = this.engageable;
		gun.limitAmmo = this.limitAmmo;
		gun.damageReport = this.damageReport;
		gun.country = this.country;
		gun.damageThreshold = this.damageThreshold; 
		
		gun.position = new Coordinate();
		gun.orientation = new Orientation();
		
		gun.entity = new McuTREntity();
		
		gun.populateEntity();
		
		return gun;
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
