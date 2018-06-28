package pwcg.campaign.ww1.ground.vehicle;

import pwcg.campaign.Campaign;
import pwcg.campaign.api.ICountry;
import pwcg.campaign.context.Country;
import pwcg.campaign.context.PWCGContextManager;
import pwcg.campaign.utils.IndexGenerator;
import pwcg.core.config.ConfigItemKeys;
import pwcg.core.config.ConfigManagerCampaign;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.location.Orientation;
import pwcg.core.utils.RandomNumberGenerator;
import pwcg.mission.mcu.McuTREntity;

public class AAAArtillery extends AAA
{
    boolean use10x = false;
    
	// German
	private String[][] germanMGs = 
	{
		{ "77mmaaa", "77mmaaa" },
	};
	
	// Allied
	private String[][] alliedMGs = 
	{
		{ "13pdraaa", "13pdraaa" },
	};
	
	public AAAArtillery(ICountry country) throws PWCGException 
	{
		super(country);

        // If the user does not want 10x then never use it
        set10xUsage();
		
		// Make AAA not engageable to avoid aircraft diving on every MG that they come across
		this.setEngageable(0);
		
		String mgId= "";
		String mgDir = "";
		
		if (country.isCountry(Country.FRANCE))
		{
			int selectedMG = RandomNumberGenerator.getRandom(alliedMGs.length);
			mgId = alliedMGs[selectedMG] [0];
			mgDir = alliedMGs[selectedMG] [1];
			displayName = "French MG";
		}
        if (country.isCountry(Country.USA))
		{
			int selectedMG = RandomNumberGenerator.getRandom(alliedMGs.length);
			mgId = alliedMGs[selectedMG] [0];
			mgDir = alliedMGs[selectedMG] [1];
			displayName = "American MG";
		}
        if (country.isCountry(Country.GERMANY))
		{
			int selectedMG = RandomNumberGenerator.getRandom(germanMGs.length);
			mgId = germanMGs[selectedMG] [0];
			mgDir = germanMGs[selectedMG] [1];
			displayName = "German MG";
		}
		else
		{
			int selectedMG = RandomNumberGenerator.getRandom(alliedMGs.length);
			mgId = alliedMGs[selectedMG] [0];			
			mgDir = alliedMGs[selectedMG] [1];
			displayName = "British MG";
		}
		
		name = mgId;
        script = "LuaScripts\\WorldObjects\\" + mgId + ".txt";
		// For 10x AAA fire rate
        if (use10x)
        {
            script = "LuaScripts\\WorldObjects\\" + mgId + "10g.txt";
        }
        
		model = "graphics\\artillery\\" + mgDir + "\\" + mgId + ".mgm";
	}

    /**
     * Set 10x usage from config
     * @throws PWCGException 
     */
    private void set10xUsage() throws PWCGException
    {
        Campaign campaign =     PWCGContextManager.getInstance().getCampaign();
        ConfigManagerCampaign configManager = campaign.getCampaignConfigManager();
        try
        {
            if (configManager.getIntConfigParam(ConfigItemKeys.Use10xFlakKey) == 1)
            {
                this.use10x = true;
            }
        }
        catch (PWCGException e)
        {
            e.printStackTrace();
        }
    }

	public AAAArtillery copy () throws PWCGException 
	{
		AAAArtillery mg = new AAAArtillery(country);
		
		mg.index = IndexGenerator.getInstance().getNextIndex();
		
		mg.name = this.name;
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
