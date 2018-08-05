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
		
        configSet.addConfigItem(ConfigItemKeys.SquadronGeneratesMissionOddsKey, new ConfigItem("100"));
        configSet.addConfigItem(ConfigItemKeys.SquadronGeneratesMissionModifierKey, new ConfigItem("20"));
        configSet.addConfigItem(ConfigItemKeys.FriendlyPlanesMaxKey, new ConfigItem("6"));
        configSet.addConfigItem(ConfigItemKeys.EnemyPlanesMaxKey, new ConfigItem("10"));
        configSet.addConfigItem(ConfigItemKeys.RandomizePlanesPerSideKey, new ConfigItem("1"));		
		
        configSet.addConfigItem(ConfigItemKeys.FriendlyFlightsToKeepMinKey, new ConfigItem("6"));
        configSet.addConfigItem(ConfigItemKeys.FriendlyFlightsToKeepMaxKey, new ConfigItem("10"));
        configSet.addConfigItem(ConfigItemKeys.EnemyFlightsToKeepMinKey, new ConfigItem("6"));
        configSet.addConfigItem(ConfigItemKeys.EnemyFlightsToKeepMaxKey, new ConfigItem("10"));
        configSet.addConfigItem(ConfigItemKeys.AiFighterFlightsForGroundCampaignMaxKey, new ConfigItem("1"));
        configSet.addConfigItem(ConfigItemKeys.AiFighterFlightsForFighterCampaignMaxKey, new ConfigItem("3"));        
        configSet.addConfigItem(ConfigItemKeys.DistanceFromPlayerFieldZoneKey, new ConfigItem("20000"));
        configSet.addConfigItem(ConfigItemKeys.DistanceToPlayerFieldOverrideOddsKey, new ConfigItem("5"));
		
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
        configSet.addConfigItem(ConfigItemKeys.Use10xFlakKey, new ConfigItem("0"));
        configSet.addConfigItem(ConfigItemKeys.MaxSmokeInMissionKey, new ConfigItem("100"));
        configSet.addConfigItem(ConfigItemKeys.MaxSmokeInAreaKey, new ConfigItem("3"));

        return configSet;
	}
}
