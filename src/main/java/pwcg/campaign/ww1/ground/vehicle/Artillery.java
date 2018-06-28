package pwcg.campaign.ww1.ground.vehicle;

import pwcg.campaign.api.ICountry;
import pwcg.campaign.context.Country;
import pwcg.campaign.utils.IndexGenerator;
import pwcg.core.location.Coordinate;
import pwcg.core.location.Orientation;
import pwcg.core.utils.RandomNumberGenerator;
import pwcg.mission.mcu.McuTREntity;

public class Artillery extends Vehicle
{
	// German
	private String[][] germanArtillery = 
	{
		{ "fk96", "fk96" },
		{ "m13", "m13" },
	};
	
	// French
	private String[][] frenchArtillery = 
	{
		{ "45qf", "45qf" },
		{ "75fg1897", "75fg1897" },
	};
	
	// British
	private String[][] britishArtillery = 
	{
		{ "45qf", "45qf" },
		{ "75fg1897", "75fg1897" },
	};
	
	public Artillery(ICountry country) 
	{
        super(country);
		
		String artilleryId= "";
		String artilleryDir = "";
		
		if (country.getCountry() == Country.FRANCE)
		{
			int selectedArtillery = RandomNumberGenerator.getRandom(frenchArtillery.length);
			artilleryId = frenchArtillery[selectedArtillery] [0];
			artilleryDir = frenchArtillery[selectedArtillery] [1];
			displayName = "French Artillery";
		}
		else if (country.getCountry() == Country.USA)
		{
			int selectedArtillery = RandomNumberGenerator.getRandom(frenchArtillery.length);
			artilleryId = frenchArtillery[selectedArtillery] [0];
			artilleryDir = frenchArtillery[selectedArtillery] [1];
			displayName = "American Artillery";
		}
		else if (country.getCountry() == Country.GERMANY)
		{
			int selectedArtillery = RandomNumberGenerator.getRandom(germanArtillery.length);
			artilleryId = germanArtillery[selectedArtillery] [0];
			artilleryDir = germanArtillery[selectedArtillery] [1];
			displayName = "German Artillery";
		}
		else
		{
			int selectedArtillery = RandomNumberGenerator.getRandom(britishArtillery.length);
			artilleryId = britishArtillery[selectedArtillery] [0];			
			artilleryDir = britishArtillery[selectedArtillery] [1];
			displayName = "British Artillery";
		}
		
		name = artilleryId;
		script = "LuaScripts\\WorldObjects\\" + artilleryId + ".txt";
		model = "graphics\\artillery\\" + artilleryDir + "\\" + artilleryId + ".mgm";
	}


	public Artillery copy () 
	{
		Artillery gun = new Artillery(country);
		
		gun.index = IndexGenerator.getInstance().getNextIndex();
		
		gun.name = this.name;
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
}
