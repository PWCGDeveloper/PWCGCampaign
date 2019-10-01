package pwcg.core.config;

public class ConfigSetMissionLimits
{
	public static ConfigSet initialize()
	{
		ConfigSet configSet = new ConfigSet();
		configSet.setConfigSetName(ConfigSetKeys.ConfigSetMissionLimits);

		configSet.addConfigItem(ConfigItemKeys.MGSpacingKey, new ConfigItem("2500"));
		configSet.addConfigItem(ConfigItemKeys.UseAmbientGroundUnitsKey, new ConfigItem("0"));
        configSet.addConfigItem(ConfigItemKeys.UseAirfieldMGsKey, new ConfigItem("0"));
        configSet.addConfigItem(ConfigItemKeys.MissionBoxSizeKey, new ConfigItem("30000"));
        configSet.addConfigItem(ConfigItemKeys.MissionBoxMinDistanceFromBaseKey, new ConfigItem("40000"));
        configSet.addConfigItem(ConfigItemKeys.MissionBoxMaxDistancePercentRangeKey, new ConfigItem("40"));
		
        configSet.addConfigItem(ConfigItemKeys.AlliedPlanesToSpawnMaxKey, new ConfigItem("6"));
        configSet.addConfigItem(ConfigItemKeys.AxisPlanesToSpawnMaxKey, new ConfigItem("10"));
        configSet.addConfigItem(ConfigItemKeys.RandomizePlanesPerSideKey, new ConfigItem("1"));		
		
        configSet.addConfigItem(ConfigItemKeys.AlliedFlightsToKeepKey, new ConfigItem("4"));
        configSet.addConfigItem(ConfigItemKeys.AxisFlightsToKeepKey, new ConfigItem("3"));

        configSet.addConfigItem(ConfigItemKeys.AiFighterFlightsForGroundCampaignMaxKey, new ConfigItem("1"));
        configSet.addConfigItem(ConfigItemKeys.AiFighterFlightsForFighterCampaignMaxKey, new ConfigItem("3"));        
		
		configSet.addConfigItem(ConfigItemKeys.OddsOfAceFlyingKey, new ConfigItem("40"));		
		configSet.addConfigItem(ConfigItemKeys.InitialSquadronSearchRadiusKey, new ConfigItem("50000"));
		configSet.addConfigItem(ConfigItemKeys.MaxSquadronSearchRadiusKey, new ConfigItem("120000"));
		
        configSet.addConfigItem(ConfigItemKeys.AllowAirStartsKey, new ConfigItem("0"));
        configSet.addConfigItem(ConfigItemKeys.GenerateClimbWPKey, new ConfigItem("1"));
        configSet.addConfigItem(ConfigItemKeys.UsePlaneDeleteKey, new ConfigItem("1"));
        configSet.addConfigItem(ConfigItemKeys.PlaneDeleteEnemyDistanceKey, new ConfigItem("10000"));
        configSet.addConfigItem(ConfigItemKeys.PlaneDeletePlayerDistanceKey, new ConfigItem("12000"));
        configSet.addConfigItem(ConfigItemKeys.IsEscortedOddsKey, new ConfigItem("50"));
		
        configSet.addConfigItem(ConfigItemKeys.MaxAmbientBalloonsKey, new ConfigItem("1"));
        configSet.addConfigItem(ConfigItemKeys.TimeOnArtillerySpotKey, new ConfigItem("5"));
        configSet.addConfigItem(ConfigItemKeys.MaxSmokeInMissionKey, new ConfigItem("100"));
        configSet.addConfigItem(ConfigItemKeys.MaxSmokeInAreaKey, new ConfigItem("3"));

        return configSet;
	}
}
