package pwcg.core.config;

public class ConfigSetGUI
{
	public static ConfigSet initialize()
	{
		ConfigSet configSet = new ConfigSet();
		configSet.setConfigSetName(ConfigSetKeys.ConfigSetGUI);

		configSet.addConfigItem(ConfigItemKeys.DebriefSpeedKey, new ConfigItem("2"));	
		configSet.addConfigItem(ConfigItemKeys.PrimaryFontKey, new ConfigItem("Serif"));
		configSet.addConfigItem(ConfigItemKeys.PrimaryFontSizeKey, new ConfigItem("20"));
		configSet.addConfigItem(ConfigItemKeys.PrimaryFontSmallSizeKey, new ConfigItem("15"));
		configSet.addConfigItem(ConfigItemKeys.PrimaryFontLargeSizeKey, new ConfigItem("25"));
		configSet.addConfigItem(ConfigItemKeys.ChalkboardFontKey, new ConfigItem("Serif"));
		configSet.addConfigItem(ConfigItemKeys.ChalkboardFontSizeKey, new ConfigItem("30"));		
        configSet.addConfigItem(ConfigItemKeys.BriefingFontKey, new ConfigItem("Serif"));
        configSet.addConfigItem(ConfigItemKeys.BriefingFontSizeKey, new ConfigItem("22"));
		configSet.addConfigItem(ConfigItemKeys.CrewMemberFontSizeKey, new ConfigItem("22"));
		configSet.addConfigItem(ConfigItemKeys.CrewMemberFontKey, new ConfigItem("Serif"));		
        configSet.addConfigItem(ConfigItemKeys.TypewriterFontSizeKey, new ConfigItem("18"));
        configSet.addConfigItem(ConfigItemKeys.TypewriterFontKey, new ConfigItem("Serif"));
        configSet.addConfigItem(ConfigItemKeys.CursiveFontSizeKey, new ConfigItem("20"));
        configSet.addConfigItem(ConfigItemKeys.CursiveFontKey, new ConfigItem("Serif"));
        configSet.addConfigItem(ConfigItemKeys.DecorativeFontSizeKey, new ConfigItem("26"));
        configSet.addConfigItem(ConfigItemKeys.DecorativeFontKey, new ConfigItem("Serif"));

		configSet.addConfigItem(ConfigItemKeys.MissionDescriptionFontSizeKey, new ConfigItem("12"));
        configSet.addConfigItem(ConfigItemKeys.MissionDescriptionFontKey, new ConfigItem("Serif"));
        configSet.addConfigItem(ConfigItemKeys.UseToolTipKey, new ConfigItem("1"));
        configSet.addConfigItem(ConfigItemKeys.ScreenSizeAutoKey, new ConfigItem("0"));
        configSet.addConfigItem(ConfigItemKeys.ScreenSizeHeightKey, new ConfigItem("1366"));
        configSet.addConfigItem(ConfigItemKeys.ScreenSizeWidthKey, new ConfigItem("768"));
        configSet.addConfigItem(ConfigItemKeys.UseGenericUIKey, new ConfigItem("0"));
        configSet.addConfigItem(ConfigItemKeys.ShowFrontLineEditorKey, new ConfigItem("0"));
		return configSet;
	}
}
