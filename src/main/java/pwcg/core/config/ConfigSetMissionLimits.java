package pwcg.core.config;

public class ConfigSetMissionLimits
{
	public static ConfigSet initialize()
	{
		ConfigSet configSet = new ConfigSet();
		configSet.setConfigSetName(ConfigSetKeys.ConfigSetMissionLimits);

        configSet.addConfigItem(ConfigItemKeys.MissionBoxSizeKey, new ConfigItem("30000"));
        configSet.addConfigItem(ConfigItemKeys.MissionBoxMinDistanceFromBaseKey, new ConfigItem("40000"));
        configSet.addConfigItem(ConfigItemKeys.MissionBoxMaxDistanceFromBaseKey, new ConfigItem("40"));
				
        configSet.addConfigItem(ConfigItemKeys.AlliedFlightsToKeepKey, new ConfigItem("4"));
        configSet.addConfigItem(ConfigItemKeys.AxisFlightsToKeepKey, new ConfigItem("3"));

        configSet.addConfigItem(ConfigItemKeys.AiFighterFlightsForGroundCampaignMaxKey, new ConfigItem("1"));
        configSet.addConfigItem(ConfigItemKeys.AiFighterFlightsForFighterCampaignMaxKey, new ConfigItem("3"));        
		
        configSet.addConfigItem(ConfigItemKeys.OddsOfRareAircraftFlyingKey, new ConfigItem("10"));       
		
        configSet.addConfigItem(ConfigItemKeys.AllowAirStartsKey, new ConfigItem("0"));
        configSet.addConfigItem(ConfigItemKeys.GenerateClimbWPKey, new ConfigItem("1"));

        configSet.addConfigItem(ConfigItemKeys.MaxVirtualEscortedFlightKey, new ConfigItem("2"));
        configSet.addConfigItem(ConfigItemKeys.IsVirtualBombingEscortedOddsKey, new ConfigItem("40"));
        configSet.addConfigItem(ConfigItemKeys.IsVirtualGroundAttackEscortedOddsKey, new ConfigItem("10"));
        configSet.addConfigItem(ConfigItemKeys.IsVirtualDiveBombEscortedOddsKey, new ConfigItem("30"));
        configSet.addConfigItem(ConfigItemKeys.IsVirtualTransportEscortedOddsKey, new ConfigItem("20"));
		
        configSet.addConfigItem(ConfigItemKeys.MaxSmokeInMissionKey, new ConfigItem("100"));
        configSet.addConfigItem(ConfigItemKeys.MaxSmokeInAreaKey, new ConfigItem("3"));
        configSet.addConfigItem(ConfigItemKeys.GroundUnitSpawnDistanceKey, new ConfigItem("10000"));
        
        configSet.addConfigItem(ConfigItemKeys.TimeOnArtillerySpotKey, new ConfigItem("8"));
        

        return configSet;
	}
}
