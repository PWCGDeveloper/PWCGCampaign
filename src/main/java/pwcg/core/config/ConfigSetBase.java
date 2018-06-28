package pwcg.core.config;

import pwcg.core.exception.PWCGException;

public abstract class ConfigSetBase
{
	protected ConfigSet configSet = new ConfigSet();

	public ConfigSetBase()
	{
	}
	
	public boolean contains(String key)
	{
		return configSet.hasConfigItem(key);
	}

	public String getStringConfigParam(String key)
	{
		ConfigItem val = configSet.getConfigItem(key);
		return val.getValue();
	}

	public int getIntConfigParam(String key)
	{
		ConfigItem val = configSet.getConfigItem(key);
		return new Integer(val.getValue());
	}

	public void setParam(String key, String val) throws PWCGException
	{
		if (contains(key))
		{
			ConfigItem item = configSet.getConfigItem(key);

			item.setValue(val);
			configSet.addConfigItem(key, item);
		}
		else
		{
			throw new PWCGException("Parameter not found at setParam: " + key);
		}
	}

	public void setLabel(String key, String label) throws PWCGException
	{
		if (contains(key))
		{
			ConfigItem item = configSet.getConfigItem(key);

			item.setLabel(label);
			configSet.addConfigItem(key, item);
		}
		else
		{
			throw new PWCGException("Parameter not found at setLabel: " + key);
		}
	}

	public void setHelp(String key, String help) throws PWCGException
	{
		if (contains(key))
		{
			ConfigItem item = configSet.getConfigItem(key);

			item.setHelp(help);
			configSet.addConfigItem(key, item);
		}
		else
		{
			throw new PWCGException("Parameter not found at setHelp: " + key);
		}
	}

	public void validate() throws PWCGException
	{
	    configSet.validate();
	}

	abstract public void initialize();
}
