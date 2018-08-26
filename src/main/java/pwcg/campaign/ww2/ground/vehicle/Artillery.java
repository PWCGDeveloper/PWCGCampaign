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

class Artillery extends Vehicle
{
    private static final List<VehicleDefinition> germanArtillery = new ArrayList<VehicleDefinition>() 
    {
        private static final long serialVersionUID = 1L;
        {
            add(new VehicleDefinition("vehicles\\", "artillery\\lefh18\\", "lefh18", Country.GERMANY));
        }
    };

    private static final List<VehicleDefinition> russianArtillery = new ArrayList<VehicleDefinition>() 
    {
        private static final long serialVersionUID = 1L;
        {
            add(new VehicleDefinition("vehicles\\", "artillery\\ml20\\", "ml20", Country.RUSSIA));
        }
    };
	
	public Artillery()
	{
        super();
	}

    @Override
    public List<VehicleDefinition> getAllVehicleDefinitions()
    {
        List<VehicleDefinition> allvehicleDefinitions = new ArrayList<>();
        allvehicleDefinitions.addAll(germanArtillery);
        allvehicleDefinitions.addAll(russianArtillery);
        return allvehicleDefinitions;
    }

    @Override
    public void makeRandomVehicleFromSet(ICountry country) throws PWCGException
    {
        List<VehicleDefinition> vehicleSet = null;;
        if (country.getSideNoNeutral() == Side.ALLIED)
        {
            vehicleSet = russianArtillery;
        }
        else if (country.getSideNoNeutral() == Side.AXIS)
        {
            vehicleSet = germanArtillery;
        }
        
        displayName = "Artillery";
        makeRandomVehicleInstance(vehicleSet);
    }

	public Artillery copy () 
	{
		Artillery gun = new Artillery();
		
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
