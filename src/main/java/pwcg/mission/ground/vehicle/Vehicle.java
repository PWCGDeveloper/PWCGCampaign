package pwcg.mission.ground.vehicle;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;

import pwcg.campaign.api.ICountry;
import pwcg.campaign.context.Country;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.factory.CountryFactory;
import pwcg.campaign.group.airfield.staticobject.StaticObject;
import pwcg.campaign.utils.IndexGenerator;
import pwcg.core.constants.AiSkillLevel;
import pwcg.core.exception.PWCGException;
import pwcg.core.exception.PWCGIOException;
import pwcg.core.location.Coordinate;
import pwcg.core.location.Orientation;
import pwcg.core.utils.PWCGLogger;
import pwcg.mission.mcu.McuTREntity;

public class Vehicle implements Cloneable, IVehicle
{
    protected VehicleDefinition vehicleDefinition;
    protected String vehicleName = "";
    protected String vehicleType = "";
    protected int index;
    protected int linkTrId;
    protected Coordinate position;
    protected Orientation orientation;
    protected String script = "";
    protected String model = "";
    protected String Desc = "";
    protected int numberInFormation = 1;
    protected int vulnerable = 1;
    protected int engageable = 1;
    protected int limitAmmo = 1;
    protected AiSkillLevel aiLevel = AiSkillLevel.NOVICE;
    protected int damageReport = 50;
    protected int damageThreshold = 1;
    protected int deleteAfterDeath = 1;
    protected int spotter = -1;
    protected int beaconChannel = 0;
    protected ICountry country = CountryFactory.makeCountryByCountry(Country.NEUTRAL);
    protected IVehicle associatedBlock;

    protected McuTREntity entity;

    private Vehicle()
    {
    }

    public Vehicle(VehicleDefinition vehicleDefinition)
    {
        this.vehicleDefinition = vehicleDefinition;
        index = IndexGenerator.getInstance().getNextIndex();
        entity = new McuTREntity(index);
        linkTrId = entity.getIndex();
    }

    public IVehicle clone()
    {
        Vehicle clone = new Vehicle();
        clone.vehicleDefinition = this.vehicleDefinition;
        clone.vehicleName = this.vehicleName;
        clone.vehicleType = this.vehicleType;
        clone.position = this.position.copy();
        clone.orientation = this.orientation.copy();
        clone.script = this.script;
        clone.model = this.model;
        clone.Desc = this.Desc;
        clone.numberInFormation = this.numberInFormation;
        clone.vulnerable = this.vulnerable;
        clone.engageable = this.engageable;
        clone.limitAmmo = this.limitAmmo;
        clone.aiLevel = this.aiLevel;
        clone.damageReport = this.damageReport;
        clone.damageThreshold = this.damageThreshold;
        clone.deleteAfterDeath = this.deleteAfterDeath;
        clone.beaconChannel = this.beaconChannel;
        clone.country = this.country;
        if (associatedBlock != null)
        {
            clone.associatedBlock = this.associatedBlock.clone();
        }

        clone.index = IndexGenerator.getInstance().getNextIndex();
        clone.entity = this.entity.copy(clone.index);
        clone.linkTrId = entity.getIndex();

        return clone;
    }

    public void makeVehicleFromDefinition(ICountry vehicleCountry)
    {
        country = vehicleCountry;
        vehicleType = vehicleDefinition.getVehicleType();
        vehicleName = vehicleDefinition.getVehicleName();
        script = "LuaScripts\\WorldObjects\\" + vehicleDefinition.getScriptDir() + vehicleDefinition.getVehicleType() + ".txt";
        model = "graphics\\" + vehicleDefinition.getModelDir() + vehicleDefinition.getVehicleType() + ".mgm";
        setPosition(new Coordinate());
        setOrientation(new Orientation());
        populateEntity();
        buildAssociatedBlock();
    }

    public void populateEntity()
    {
        entity.setPosition(position);
        entity.setOrientation(orientation);
    }

    private void buildAssociatedBlock()
    {
        VehicleDefinition blockDefinition = PWCGContext.getInstance().getStaticObjectDefinitionManager()
                .getVehicleDefinitionByType(vehicleDefinition.getAssociatedBlock());
        if (blockDefinition != null)
        {
            associatedBlock = new StaticObject(blockDefinition);
            associatedBlock.makeVehicleFromDefinition(country);
            associatedBlock.setPosition(position);
            associatedBlock.setOrientation(orientation);
        }
    }

    public void write(BufferedWriter writer) throws PWCGException
    {
        try
        {
            writer.write("Vehicle");
            writer.newLine();
            writer.write("{");
            writer.newLine();

            writeInternals(writer);

            writer.write("}");
            writer.newLine();
            writer.newLine();
            writer.newLine();

            entity.write(writer);

            if (associatedBlock != null)
            {
                associatedBlock.write(writer);
            }
        }
        catch (IOException e)
        {
            PWCGLogger.logException(e);
            throw new PWCGIOException(e.getMessage());
        }
    }

    private void writeInternals(BufferedWriter writer) throws PWCGIOException
    {
        try
        {
            writer.write("  Name = \"" + vehicleName + "\";");
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

            country.writeAdjusted(writer);

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
            writer.write("  Spotter = " + spotter + ";");
            writer.newLine();
            writer.write("  BeaconChannel = " + beaconChannel + ";");
            writer.newLine();
        }
        catch (IOException e)
        {
            PWCGLogger.logException(e);
            throw new PWCGIOException(e.getMessage());
        }
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

    public Coordinate getPosition()
    {
        return position;
    }

    public void setPosition(Coordinate position)
    {
        this.position = position;
        entity.setPosition(position);
        if (associatedBlock != null)
        {
            associatedBlock.setPosition(position);
        }
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

    public String getDescription()
    {
        return (vehicleType + " / " + script + " / " + model);
    }

    public McuTREntity getEntity()
    {
        return entity;
    }

    public int getEngageable()
    {
        return engageable;
    }

    public void setEngageable(int engageable)
    {
        this.engageable = engageable;
    }

    public void setAiLevel(AiSkillLevel aiLevel)
    {
        this.aiLevel = aiLevel;
    }

    public void setCountry(ICountry country)
    {
        this.country = country;
    }

    public ICountry getCountry()
    {
        return country;
    }

    public String getVehicleName()
    {
        return vehicleName;
    }

    public String getScript()
    {
        return script;
    }

    @Override
    public int getIndex()
    {
        return index;
    }

    @Override
    public void setSpotterRange(int spotterRange)
    {
        this.spotter = spotterRange;
    }

    @Override
    public int getBeaconChannel()
    {
        return beaconChannel;
    }

    @Override
    public void setBeaconChannel(int beaconChannel)
    {
        this.beaconChannel = beaconChannel;
    }

    @Override
    public String getVehicleType()
    {
        return vehicleType;
    }

    @Override
    public int getLinkTrId()
    {
        return linkTrId;
    }
}
