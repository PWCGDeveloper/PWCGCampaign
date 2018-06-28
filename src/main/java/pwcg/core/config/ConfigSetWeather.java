package pwcg.core.config;

public class ConfigSetWeather
{
	public static ConfigSet initialize()
	{
		ConfigSet configSet = new ConfigSet();
		configSet.setConfigSetName(ConfigSetKeys.ConfigSetWeather);

	    configSet.addConfigItem(ConfigItemKeys.WeatherClearCloudsKey, new ConfigItem("20"));
	    configSet.addConfigItem(ConfigItemKeys.WeatherLightCloudsKey, new ConfigItem("30"));
	    configSet.addConfigItem(ConfigItemKeys.WeatherAverageCloudsKey, new ConfigItem("30"));
	    configSet.addConfigItem(ConfigItemKeys.WeatherHeavyCloudsKey, new ConfigItem("15"));
	    configSet.addConfigItem(ConfigItemKeys.WeatherOvercastCloudsKey, new ConfigItem("5"));

        configSet.addConfigItem(ConfigItemKeys.MaxTurbulenceKey, new ConfigItem("3"));
        configSet.addConfigItem(ConfigItemKeys.MaxWindKey, new ConfigItem("10"));
		return configSet;
	}
}
