package pwcg.core.config;

import pwcg.core.exception.PWCGException;

public class ConfigItem implements Cloneable
{
    public enum ConfigType
    {
        CONFIG_STRING,
        CONFIG_INT,
        CONFIG_DATE
    }
    
	public String value;
	public String label;
    public String help;

	private ConfigItem ()
	{
	}
	
	public ConfigItem (String value)
	{
        this.value = value;
	}
	

	public ConfigItem copy() 
	{
		ConfigItem clone = new ConfigItem();
		clone.value = this.value;
		clone.label = this.label;
        clone.help = this.help;
		return clone;
	}
	
    public void validate() throws PWCGException
    {
        if (value == null || value.equals(""))
        {
            throw new PWCGException("No value for key: " + label);
        }

        if (label == null || label.equals(""))
        {
            throw new PWCGException("No label for key: " + label);
        }

        if (help == null || help.equals(""))
        {
            throw new PWCGException("No help for key: " + label);
        }

        if (value.contains(";"))
        {
            throw new PWCGException("Malformed value for key: " + label);
        }

        if (label.contains(";"))
        {
            throw new PWCGException("Malformed label for key: " + label);
        }

        if (help.contains(";"))
        {
            throw new PWCGException("Malformed help for key: " + label);
        }
    }

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getLabelText() {
		return label;
	}

	public String getHelp() {
		return help;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public void setHelp(String help) {
		this.help = help;
	}
}
