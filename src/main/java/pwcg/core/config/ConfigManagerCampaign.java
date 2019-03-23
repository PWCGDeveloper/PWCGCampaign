package pwcg.core.config;

import pwcg.core.exception.PWCGException;

public class ConfigManagerCampaign extends ConfigManager
{	
    public ConfigManagerCampaign(String campaignConfigDir) 
    {
        super(campaignConfigDir);
    }
    
	public void initialize() throws PWCGException 
	{
	    initializeDefault();

        readConfig();
	}

	public void initializeDefault()
    {
        defaultCampaignConfigSets.put(ConfigSetKeys.ConfigSetFlight, ConfigSetFlight.initialize());
	    defaultCampaignConfigSets.put(ConfigSetKeys.ConfigSetMissionAi, ConfigSetMissionAI.initialize());
	    defaultCampaignConfigSets.put(ConfigSetKeys.ConfigSetGroundObjects, ConfigSetGroundObjects.initialize());
        defaultCampaignConfigSets.put(ConfigSetKeys.ConfigSetMissionLimits, ConfigSetMissionLimits.initialize());
        defaultCampaignConfigSets.put(ConfigSetKeys.ConfigSetFighterMission, ConfigSetFighterMissionTypes.initialize());
        defaultCampaignConfigSets.put(ConfigSetKeys.ConfigSetMissionSpacing, ConfigSetMissionSpacing.initialize());
        defaultCampaignConfigSets.put(ConfigSetKeys.ConfigSetAircraftNumbers, ConfigSetAircraftNumbers.initialize());
        defaultCampaignConfigSets.put(ConfigSetKeys.ConfigSetUserPrefCampaign, ConfigSetUserPrefCampaign.initialize());
        defaultCampaignConfigSets.put(ConfigSetKeys.ConfigSetWeather, ConfigSetWeather.initialize());
        defaultCampaignConfigSets.put(ConfigSetKeys.ConfigSetSimple, ConfigSetSimple.initialize());
    }
}
