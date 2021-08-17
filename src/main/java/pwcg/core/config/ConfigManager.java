package pwcg.core.config;

import java.util.HashMap;
import java.util.Map;

import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.io.json.ConfigurationIOJson;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.FileUtils;

public abstract class ConfigManager
{
    protected Map<String, ConfigSet> defaultCampaignConfigSets = new HashMap<String, ConfigSet>();
    protected Map<String, ConfigSet> userCampaignConfigSets = new HashMap<String, ConfigSet>();
	protected String userConfigSourceDirectory = "";

	public abstract void initialize() throws PWCGException;

	public ConfigManager (String userConfigSourceDirectory)
	{
		this.userConfigSourceDirectory = userConfigSourceDirectory;
	}
	
	public void write() throws PWCGException 
    {
        for (ConfigSet userConfigSet : userCampaignConfigSets.values())
        {
        	ConfigurationIOJson.writeJsonConfigSet(userConfigSourceDirectory, userConfigSet);
        }
        
        readConfig();
    }

    public void readConfig() throws PWCGException 
    {
    	loadInitialConfigSets();
    	readUserConfigSets();
    }

    public void reset() throws PWCGException 
    {
        FileUtils.deleteFilesInDirectory(userConfigSourceDirectory);
        readConfig();
    }

    public void loadInitialConfigSets() throws PWCGException 
    {
    	Map<String, ConfigSet> initialConfigSets = readInitialConfigSets();
    	for (String configSetKey : defaultCampaignConfigSets.keySet())
    	{
    		ConfigSet initialConfigSet = initialConfigSets.get(configSetKey);
    		if (initialConfigSet == null)
    		{
                throw new PWCGException("No configuration set for key: " + configSetKey);
    		}

    		defaultCampaignConfigSets.put(configSetKey, initialConfigSet);
    	}
    }

    public Map<String, ConfigSet> readInitialConfigSets() throws PWCGException 
    {
        return ConfigurationIOJson.readJson(PWCGContext.getInstance().getDirectoryManager().getPwcgConfigurationDir());
    }

    private void readUserConfigSets() throws PWCGException 
    {
    	Map<String, ConfigSet> userModifiedConfigSets = ConfigurationIOJson.readJson(userConfigSourceDirectory);
    	
    	for (String configSetName : defaultCampaignConfigSets.keySet())
    	{
    		ConfigSet userModifiedConfigSet = userModifiedConfigSets.get(configSetName);
    		if (userModifiedConfigSet != null)
    		{
                userCampaignConfigSets.put(configSetName, userModifiedConfigSet);
    		}
    		else
    		{
    		    userModifiedConfigSet = makeNewUserConfigSet(configSetName);
    		    userCampaignConfigSets.put(configSetName, userModifiedConfigSet);
    		}
    	}
    }

    private ConfigSet makeNewUserConfigSet(String configSetName)
    {
        ConfigSet userModifiedConfigSet = new ConfigSet();
        userModifiedConfigSet.setConfigSetName(configSetName);
        return userModifiedConfigSet;
    }

	public void setParam(String parameterKey, String value) throws PWCGException
	{
	    String configSetKey = identifyConfigSetKey(parameterKey);
	    ConfigSet defaultConfigSet = defaultCampaignConfigSets.get(configSetKey);        
        ConfigItem configItem = defaultConfigSet.getConfigItem(parameterKey);
        ConfigItem userConfigItem = configItem.copy();
        userConfigItem.setValue(value);

        ConfigSet userConfigSet = userCampaignConfigSets.get(configSetKey);
        userConfigSet.addConfigItem(parameterKey, userConfigItem);
	}

	public String getStringConfigParam(String parameterKey) throws PWCGException
	{
        return getConfigParam(parameterKey);
	}

	public int getIntConfigParam(String parameterKey) throws PWCGException
	{
        String stringValue = getConfigParam(parameterKey);
        return Integer.valueOf(stringValue);
	}

    public double getDoubleConfigParam(String parameterKey) throws PWCGException
    {
        String stringValue = getConfigParam(parameterKey);
        return Double.valueOf(stringValue);
    }

	private String getConfigParam(String parameterKey) throws PWCGException
    {
	    String configSetKey = identifyConfigSetKey(parameterKey);

	    ConfigSet defaultConfigSet = defaultCampaignConfigSets.get(configSetKey);        
        ConfigSet userConfigSet = userCampaignConfigSets.get(configSetKey);

        if (userConfigSet.hasConfigItem(parameterKey))
        {
            String config = userConfigSet.getConfigItem(parameterKey).getValue();
            config = config.trim();
            return config;
        }

        if (defaultConfigSet.hasConfigItem(parameterKey))
        {
            String config = defaultConfigSet.getConfigItem(parameterKey).getValue();
            config = config.trim();
            return config;
        }

        throw new PWCGException("Config value not found: " + parameterKey);
    }

    public String setConfigParam(String parameterKey, String value) throws PWCGException
    {
        String configSetKey = identifyConfigSetKey(parameterKey);

        ConfigSet defaultConfigSet = defaultCampaignConfigSets.get(configSetKey);        
        ConfigSet userConfigSet = userCampaignConfigSets.get(configSetKey);

        if (userConfigSet.hasConfigItem(parameterKey))
        {
            ConfigItem configItem = userConfigSet.getConfigItem(parameterKey);
            configItem.setValue(value);
            userConfigSet.addConfigItem(parameterKey, configItem);
            return value;
        }

        if (defaultConfigSet.hasConfigItem(parameterKey))
        {
            ConfigItem configItem = defaultConfigSet.getConfigItem(parameterKey);
            configItem.setValue(value);
            defaultConfigSet.addConfigItem(parameterKey, configItem);
            return value;
        }

        throw new PWCGException("Config value not found: " + parameterKey);
    }

    private String identifyConfigSetKey(String parameterKey) throws PWCGException
    {
        for (ConfigSet defaultConfigSet : defaultCampaignConfigSets.values())
        {
            if (defaultConfigSet.hasConfigItem(parameterKey))
            {
                return defaultConfigSet.getConfigSetName();
            }
        }
        
        throw new PWCGException("identifyConfigSetKey: Unable to find configuration parameter : " + parameterKey);
    }

    public ConfigSet getMergedConfigSet(String configSetKey)
    {
        ConfigSet defaultConfigSet = defaultCampaignConfigSets.get(configSetKey);        
        ConfigSet userConfigSet = userCampaignConfigSets.get(configSetKey);
        
        return defaultConfigSet.merge(userConfigSet);
    }

    public void incorporateEditedConfigSet(ConfigSet editedConfigSet)
    {
        ConfigSet defaultConfigSet = defaultCampaignConfigSets.get(editedConfigSet.getConfigSetName());
        
        ConfigEditResolver configEditResolver = new ConfigEditResolver();
        ConfigSet userModifiedConfigSet = configEditResolver.resolveUserEdits(defaultConfigSet, editedConfigSet);
        userCampaignConfigSets.put(editedConfigSet.getConfigSetName(), userModifiedConfigSet);
    }

	public Map<String, ConfigSet> getDefaultCampaignConfigSets()
	{
		return defaultCampaignConfigSets;
	}
}
