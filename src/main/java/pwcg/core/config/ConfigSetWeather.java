package pwcg.core.config;

public class ConfigSetWeather
{
	public static ConfigSet initialize()
	{
		ConfigSet configSet = new ConfigSet();
		configSet.setConfigSetName(ConfigSetKeys.ConfigSetWeather);

        configSet.addConfigItem(ConfigItemKeys.UseRealisticWeatherKey, new ConfigItem("1"));
        configSet.addConfigItem(ConfigItemKeys.MaxTurbulenceKey, new ConfigItem("3"));
        configSet.addConfigItem(ConfigItemKeys.MaxWindKey, new ConfigItem("10"));
        configSet.addConfigItem(ConfigItemKeys.MinHazeKey, new ConfigItem("0"));
        configSet.addConfigItem(ConfigItemKeys.MaxHazeKey, new ConfigItem("10"));
		return configSet;
	}
}
