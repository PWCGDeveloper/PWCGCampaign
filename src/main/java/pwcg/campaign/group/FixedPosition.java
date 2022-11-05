package pwcg.campaign.group;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.Date;

import pwcg.campaign.Campaign;
import pwcg.campaign.api.ICountry;
import pwcg.campaign.api.IFixedPosition;
import pwcg.campaign.context.Country;
import pwcg.campaign.context.CountryDesignator;
import pwcg.campaign.context.FrontMapIdentifier;
import pwcg.campaign.factory.CountryFactory;
import pwcg.campaign.utils.IndexGenerator;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Orientation;
import pwcg.core.location.PWCGLocation;
import pwcg.core.utils.PWCGLogger;

public class FixedPosition extends PWCGLocation implements Cloneable, IFixedPosition
{
    protected int index;
    protected int linkTrId;
    protected String desc = "";
    protected String model = "";
    protected int damageReport = 50;
    protected int damageThreshold = 1;

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
        clone.desc = this.desc;
        clone.damageReport = this.damageReport;
        clone.damageThreshold = this.damageThreshold;

        return clone;
    }

    public void write(BufferedWriter writer) throws PWCGException, PWCGException
    {
        try
        {
            writer.write("  Index = " + index + ";");
            writer.newLine();
            writer.write("  Name = \"" + name + "\";");
            writer.newLine();
            writer.write("    Model = \"" + model + "\";");
            writer.newLine();

            writer.write("    DamageReport = " + damageReport + ";");
            writer.newLine();
            writer.write("    DamageThreshold = " + damageThreshold + ";");
            writer.newLine();
            writer.write("    Desc = \"" + desc + "\";");
            writer.newLine();
            writer.write("    LinkTrId = " + linkTrId + ";");
            writer.newLine();

            position.write(writer);
            orientation.write(writer);
        }
        catch (IOException e)
        {
            PWCGLogger.logException(e);
            throw new PWCGException(e.getMessage());
        }
    }
    
    public ICountry determineCountry(Campaign campaign) throws PWCGException
    {
        if (campaign != null)
        {
            return CountryDesignator.determineCountry(campaign.getCampaignMap(), position, campaign.getDate());
        }
            
        return CountryFactory.makeCountryByCountry(Country.NEUTRAL);
    }

    public ICountry determineCountryOnDate(FrontMapIdentifier mapIdentifier, Date date) throws PWCGException
    {
        return CountryDesignator.determineCountry(mapIdentifier, position, date);
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
}
