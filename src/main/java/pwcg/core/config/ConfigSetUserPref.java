package pwcg.core.config;

public class ConfigSetUserPref
{
	public static ConfigSet initialize()
	{
		ConfigSet configSet = new ConfigSet();
		configSet.setConfigSetName(ConfigSetKeys.ConfigSetUserPref);

		configSet.addConfigItem(ConfigItemKeys.UseCoopKey, new ConfigItem("0"));	
		configSet.addConfigItem(ConfigItemKeys.DeleteAllMissionLogsKey, new ConfigItem("0"));
        configSet.addConfigItem(ConfigItemKeys.SaveOldMissionsKey, new ConfigItem("2"));
        configSet.addConfigItem(ConfigItemKeys.PlaySoundsKey, new ConfigItem("1"));
        configSet.addConfigItem(ConfigItemKeys.PlayMusicKey, new ConfigItem("1"));
        configSet.addConfigItem(ConfigItemKeys.SoundVolumeKey, new ConfigItem("10"));
        configSet.addConfigItem(ConfigItemKeys.MusicVolumeKey, new ConfigItem("10"));
		return configSet;
	}
}
