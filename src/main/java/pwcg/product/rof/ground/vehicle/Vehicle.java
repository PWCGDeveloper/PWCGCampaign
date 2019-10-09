package pwcg.product.rof.ground.vehicle;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.util.List;

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
import pwcg.core.utils.RandomNumberGenerator;
import pwcg.mission.Unit;
import pwcg.mission.ground.vehicle.IVehicle;
import pwcg.mission.ground.vehicle.VehicleDefinition;
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
	protected int numberInFormation = Unit.NUM_IN_FORMATION_START;
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

	protected Vehicle()
	{
		index = IndexGenerator.getInstance().getNextIndex();
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

    protected void makeRandomVehicleInstance(List<VehicleDefinition> vehicleSet) throws PWCGException
    {
        int selectedIndex = RandomNumberGenerator.getRandom(vehicleSet.size());
        VehicleDefinition vehicleDefinition = vehicleSet.get(selectedIndex);
        makeVehicleFromDefinition(vehicleDefinition);
    }

    public void makeVehicleFromDefinition(VehicleDefinition vehicleDefinition) throws PWCGException
    {
        country = vehicleDefinition.getCountry();
        vehicleType = vehicleDefinition.getVehicleType();
        script = "LuaScripts\\WorldObjects\\" + vehicleDefinition.getScriptDir() + vehicleDefinition.getVehicleType() + ".txt";
        model = "graphics\\" + vehicleDefinition.getModelDir() + vehicleDefinition.getVehicleType() + ".mgm";
        setPosition(new Coordinate());
        setOrientation(new Orientation());
        populateEntity();
    }
    
    public String getDescription()
    {
        return (vehicleType + " / " + script + " / " + model);
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

	public AiSkillLevel getAiLevel()
	{
		return aiLevel;
	}

	public void setAiLevel(AiSkillLevel aiLevel)
	{
		this.aiLevel = aiLevel;
	}

	public McuTREntity getEntity()
	{
		return entity;
	}

	public void setCountry(ICountry country)
	{
		this.country = country;
	}
	
    public String getVehicleType()
    {
        return vehicleType;
    }
}
