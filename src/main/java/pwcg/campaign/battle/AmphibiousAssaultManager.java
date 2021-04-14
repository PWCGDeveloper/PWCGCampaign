package pwcg.campaign.battle;

import pwcg.campaign.context.FrontMapIdentifier;
import pwcg.campaign.io.json.AmphibiousAssaultIOJson;
import pwcg.core.utils.PWCGLogger;

public class AmphibiousAssaultManager
{
	private AmphibiousAssaults amphibiousAssaults = new AmphibiousAssaults();
    private FrontMapIdentifier map;

	public AmphibiousAssaultManager(FrontMapIdentifier map)
	{
	    this.map = map;
	}
	
	public void initialize()
	{
        try
        {
            amphibiousAssaults = AmphibiousAssaultIOJson.readJson(map.getMapName());
        }
        catch (Exception e)
        {
            PWCGLogger.logException(e);
        }
	}

    public AmphibiousAssault getAmphibiousAssaultsForCampaign(String skirmishName) 
    {     
        return amphibiousAssaults.getAmphibiousAssault(skirmishName);
    }
}
