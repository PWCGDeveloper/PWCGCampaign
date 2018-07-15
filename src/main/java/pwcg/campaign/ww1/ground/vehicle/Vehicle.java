package pwcg.campaign.ww1.ground.vehicle;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;

import pwcg.campaign.api.ICountry;
import pwcg.campaign.context.Country;
import pwcg.campaign.factory.CountryFactory;
import pwcg.campaign.utils.IndexGenerator;
import pwcg.core.constants.AiSkillLevel;
import pwcg.core.exception.PWCGException;
import pwcg.core.exception.PWCGIOException;
import pwcg.core.location.Coordinate;
import pwcg.core.location.Orientation;
import pwcg.core.utils.Logger;
import pwcg.mission.ground.vehicle.IVehicle;
import pwcg.mission.mcu.McuTREntity;

public abstract class Vehicle implements Cloneable, IVehicle
{
    protected String displayName = "Vehicle";
    protected String vehicleType = "";
	protected int index;
	protected int linkTrId;
    protected Coordinate position;
    protected Orientation orientation;
	protected String script = "";
	protected String model = "";
	protected String Desc = "";
	protected int numberInFormation = 0;
	protected int vulnerable = 1;
	protected int engageable = 1;
	protected int limitAmmo = 1;
	protected AiSkillLevel aiLevel = AiSkillLevel.NOVICE;
	protected int damageReport = 50;
	protected int damageThreshold = 1;
	protected int deleteAfterDeath = 1;
	protected boolean isThirdParty = false;
	protected ICountry country = CountryFactory.makeCountryByCountry(Country.NEUTRAL);

	protected McuTREntity entity = new McuTREntity();

	protected Vehicle(ICountry country)
	{
		index = IndexGenerator.getInstance().getNextIndex();
		this.country = country;
	}

	public void populateEntity() throws PWCGException
	{
        if (position == null)
        {
            throw new PWCGException ("No position set for populate entity");
        }
        
        if (orientation == null)
        {
            throw new PWCGException ("No orientation set for populate entity");
        }
        
		// Link this plane to the MCU
		this.linkTrId = entity.getIndex();

		// Position is same as vehicle
		entity.setPosition(position);
		entity.setOrientation(orientation);

		entity.setMisObjID(index);
	}

	public boolean vehicleExists()
	{
		String scriptFilename = "..\\data\\" + script;
		File scriptFile = new File(scriptFilename);

		String modelFilename = "..\\data\\" + model;
		File modelFile = new File(modelFilename);

		if (scriptFile.exists() && modelFile.exists())

		{
			return true;
		}

		return false;
	}

	public void write(BufferedWriter writer) throws PWCGIOException
	{
		try
		{
			writer.write("Vehicle");
			writer.newLine();
			writer.write("{");
			writer.newLine();

			writer.write("  Name = \"" + vehicleType + "\";");
			writer.newLine();
			writer.write("  Index = " + index + ";");
			writer.newLine();
			writer.write("  LinkTrId = " + linkTrId + ";");
			writer.newLine();

			position.write(writer);
			orientation.write(writer);

			writer.write("  Script = \"" + script + "\";");
			writer.newLine();
			writer.write("  Model = \"" + model + "\";");
			writer.newLine();
			
			writer.write("  Country = " + country.getCountryCode() + ";");

			writer.newLine();
			writer.write("  Desc = \"" + Desc + "\";");
			writer.newLine();
			writer.write("  AILevel = " + aiLevel.getAiSkillLevel() + ";");
			writer.newLine();
			writer.write("  NumberInFormation = " + numberInFormation + ";");
			writer.newLine();
			writer.write("  Vulnerable = " + vulnerable + ";");
			writer.newLine();
			writer.write("  Engageable = " + engageable + ";");
			writer.newLine();
			writer.write("  LimitAmmo = " + limitAmmo + ";");
			writer.newLine();
			writer.write("  DamageReport = " + damageReport + ";");
			writer.newLine();
			writer.write("  DamageThreshold = " + damageThreshold + ";");
			writer.newLine();
			writer.write("  DeleteAfterDeath = " + deleteAfterDeath + ";");
			writer.newLine();

			writer.write("}");
			writer.newLine();
			writer.newLine();
			writer.newLine();

			entity.write(writer);
		} catch (IOException e)
		{
			Logger.logException(e);
			throw new PWCGIOException(e.getMessage());
		}
	}

	public int getIndex()
	{
		return index;
	}

	public String getDisplayName()
	{
		return displayName;
	}

	public void setDisplayName(String displayName)
	{
		this.displayName = displayName;
	}

	public void setIndex(int index)
	{
		this.index = index;
	}

	public int getLinkTrId()
	{
		return linkTrId;
	}

	public void setLinkTrId(int linkTrId)
	{
		this.linkTrId = linkTrId;
	}

	public Coordinate getPosition()
    {
        return position;
    }

    public void setPosition(Coordinate position)
    {
        this.position = position;
    }

    public Orientation getOrientation()
	{
		return orientation;
	}

	public void setOrientation(Orientation orientation)
	{
		this.orientation = orientation;
		entity.setOrientation(orientation);
	}

	public String getScript()
	{
		return script;
	}

	public void setScript(String script)
	{
		this.script = script;
	}

	public String getModel()
	{
		return model;
	}

	public void setModel(String model)
	{
		this.model = model;
	}

	public String getDesc()
	{
		return Desc;
	}

	public void setDesc(String desc)
	{
		Desc = desc;
	}

	public AiSkillLevel getAiLevel()
	{
		return aiLevel;
	}

	public void setAiLevel(AiSkillLevel aiLevel)
	{
		this.aiLevel = aiLevel;
	}

	public int getNumberInFormation()
	{
		return numberInFormation;
	}

	public void setNumberInFormation(int numberInFormation)
	{
		this.numberInFormation = numberInFormation;
	}

	public int getVulnerable()
	{
		return vulnerable;
	}

	public void setVulnerable(int vulnerable)
	{
		this.vulnerable = vulnerable;
	}

	public int getEngageable()
	{
		return engageable;
	}

	public void setEngageable(int engageable)
	{
		this.engageable = engageable;
	}

	public int getLimitAmmo()
	{
		return limitAmmo;
	}

	public void setLimitAmmo(int limitAmmo)
	{
		this.limitAmmo = limitAmmo;
	}

	public int getDamageReport()
	{
		return damageReport;
	}

	public void setDamageReport(int damageReport)
	{
		this.damageReport = damageReport;
	}

	public int getDamageThreshold()
	{
		return damageThreshold;
	}

	public void setDamageThreshold(int damageThreshold)
	{
		this.damageThreshold = damageThreshold;
	}

	public McuTREntity getEntity()
	{
		return entity;
	}

	public void setEntity(McuTREntity entity)
	{
		this.entity = entity;
	}

	public void setCountry(ICountry country)
	{
		this.country = country;
	}

	public ICountry getCountry()
	{
		return country;
	}
}
