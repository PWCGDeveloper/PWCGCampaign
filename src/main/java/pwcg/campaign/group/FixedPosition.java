package pwcg.campaign.group;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import pwcg.campaign.api.ICountry;
import pwcg.campaign.api.IFixedPosition;
import pwcg.campaign.context.Country;
import pwcg.campaign.context.CountryDesignator;
import pwcg.campaign.factory.CountryFactory;
import pwcg.campaign.io.IOConstants;
import pwcg.campaign.utils.IndexGenerator;
import pwcg.core.exception.PWCGException;
import pwcg.core.exception.PWCGIOException;
import pwcg.core.location.Orientation;
import pwcg.core.location.PWCGLocation;
import pwcg.core.utils.Logger;

public class FixedPosition extends PWCGLocation implements Cloneable, IFixedPosition
{
    protected int index;
    protected int linkTrId;
    protected String desc = "";
    protected String model = "";
    protected String script = "";
    protected int durability = 1000;
    protected int damageReport = 50;
    protected int damageThreshold = 1;
    protected int deleteAfterDeath = 0;
    protected Map<Integer, Double> damaged = new HashMap<>();
    protected Country country = Country.NEUTRAL;

    public FixedPosition()
    {
        super();
        index = IndexGenerator.getInstance().getNextIndex();
    }

    public FixedPosition clone(FixedPosition clone)
    {
        clone.name = this.name;
        clone.position = this.position.copy();

        clone.index = this.index;
        clone.linkTrId = this.linkTrId;
        clone.orientation = this.orientation.copy();
        clone.model = this.model;
        clone.script = this.script;
        clone.country = this.country;
        clone.desc = this.desc;
        clone.durability = this.durability;
        clone.damageReport = this.damageReport;
        clone.damageThreshold = this.damageThreshold;

        return clone;
    }

    public void write(BufferedWriter writer) throws PWCGIOException, PWCGException
    {
        try
        {
            writer.write("  Index = " + index + ";");
            writer.newLine();
            writer.write("  Name = \"" + name + "\";");
            writer.newLine();
            writer.write("    Model = \"" + model + "\";");
            writer.newLine();
            writer.write("    Script = \"" + script + "\";");
            writer.newLine();

            determineCountry().writeAdjusted(writer);

            writer.write("    DamageReport = " + damageReport + ";");
            writer.newLine();
            writer.write("    DamageThreshold = " + damageThreshold + ";");
            writer.newLine();
            writer.write("    Desc = \"" + desc + "\";");
            writer.newLine();
            writer.write("    Durability = " + durability + ";");
            writer.newLine();
            writer.write("    LinkTrId = " + linkTrId + ";");
            writer.newLine();
            writer.write("    DeleteAfterDeath = " + deleteAfterDeath + ";");
            writer.newLine();

            position.write(writer);
            orientation.write(writer);
            writeDamaged(writer);

        }
        catch (IOException e)
        {
            Logger.logException(e);
            throw new PWCGIOException(e.getMessage());
        }
    }

    private void writeDamaged(BufferedWriter writer) throws IOException
    {
        if (damaged.size() > 0)
        {
            writer.write("  " + IOConstants.DAMAGED);
            writer.newLine();
            writer.write("  {");
            writer.newLine();
            for (Integer damageItemId : damaged.keySet())
            {
                String damageEntry = String.format("        %d = 0.6;", damageItemId);
                writer.write(damageEntry);
                writer.newLine();
            }

            writer.write("  }");
            writer.newLine();
        }
    }

    public ICountry createCountry(Date date) throws PWCGException
    {
        CountryDesignator countryDesignator = new CountryDesignator();
        return countryDesignator.determineCountry(this.position, date);
    }

    public ICountry getCountry(Date date) throws PWCGException
    {
        ICountry icountry = createCountry(date);
        return icountry;
    }

    public int getIndex()
    {
        return index;
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

    public Orientation getOrientation()
    {
        return orientation.copy();
    }

    public void setOrientation(Orientation orientation)
    {
        this.orientation = orientation;
    }

    public String getDesc()
    {
        return desc;
    }

    public void setDesc(String desc)
    {
        this.desc = desc;
    }

    public String getModel()
    {
        return model;
    }

    public void setModel(String model)
    {
        this.model = model;
    }

    public String getScript()
    {
        return script;
    }

    public void setScript(String script)
    {
        this.script = script;
    }

    public int getDurability()
    {
        return durability;
    }

    public void setDurability(int durability)
    {
        this.durability = durability;
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

    public int getDeleteAfterDeath()
    {
        return deleteAfterDeath;
    }

    public void setDeleteAfterDeath(int deleteAfterDeath)
    {
        this.deleteAfterDeath = deleteAfterDeath;
    }

    public Map<Integer, Double> getDamaged()
    {
        return damaged;
    }

    public void setDamaged(Map<Integer, Double> damaged)
    {
        this.damaged = damaged;
    }

    public ICountry determineCountry()
    {
        return CountryFactory.makeCountryByCountry(country);
    }
}
