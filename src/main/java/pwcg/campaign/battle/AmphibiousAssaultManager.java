package pwcg.campaign.battle;

import pwcg.campaign.context.FrontMapIdentifier;
import pwcg.campaign.io.json.AmphibiousAssaultIOJson;
import pwcg.campaign.skirmish.Skirmish;
import pwcg.core.utils.PWCGLogger;
import pwcg.mission.Mission;

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


	public AmphibiousAssault getActiveAmphibiousAssault(Mission mission)
    {
        Skirmish skirmish = mission.getSkirmish();
        if (skirmish != null)
        {
            return amphibiousAssaults.getAmphibiousAssault(skirmish.getSkirmishName());
        }
        return null;
    }
}
