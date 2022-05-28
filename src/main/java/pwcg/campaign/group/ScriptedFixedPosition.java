package pwcg.campaign.group;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import pwcg.campaign.Campaign;
import pwcg.campaign.api.ICountry;
import pwcg.campaign.api.IFixedPosition;
import pwcg.campaign.context.Country;
import pwcg.campaign.context.CountryDesignator;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.factory.CountryFactory;
import pwcg.campaign.io.IOConstants;
import pwcg.campaign.utils.IndexGenerator;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.PWCGLogger;
import pwcg.mission.ground.building.PwcgBuildingIdentifier;
import pwcg.mission.ground.building.PwcgStructure;
import pwcg.mission.mcu.McuTREntity;

public class ScriptedFixedPosition extends FixedPosition implements Cloneable, IFixedPosition
{
    protected int deleteAfterDeath = 0;
    protected int durability = 1000;
    protected Map<Integer, Double> damaged = new HashMap<>();
    protected String script = "";
    protected McuTREntity entity = null;

    public ScriptedFixedPosition()
    {
        super();
        index = IndexGenerator.getInstance().getNextIndex();
    }

    public ScriptedFixedPosition clone(ScriptedFixedPosition clone)
    {
        clone.name = this.name;
        clone.position = this.position.copy();

        clone.index = this.index;
        clone.linkTrId = this.linkTrId;
        clone.orientation = this.orientation.copy();
        clone.model = this.model;
        clone.script = this.script;
        clone.desc = this.desc;
        clone.durability = this.durability;
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
            PWCGLogger.logException(e);
            throw new PWCGException(e.getMessage());
        }
    }
    
    public ICountry determineCountry() throws PWCGException
    {
        Campaign campaign = PWCGContext.getInstance().getCampaign();
        if (campaign != null)
        {
            return CountryDesignator.determineCountry(position, campaign.getDate());
        }
            
        return CountryFactory.makeCountryByCountry(Country.NEUTRAL);
    }

    public ICountry determineCountryOnDate(Date date) throws PWCGException
    {
        return CountryDesignator.determineCountry(position, date);
    }

    public void buildEntity()
    {
        entity = new McuTREntity(index);
        linkTrId = entity.getIndex();
        
        entity.setPosition(position.copy());
        entity.setOrientation(orientation.copy());
        entity.enableEntity();
        
        setEntityName();
    }

    private void setEntityName()
    {
        if (PwcgBuildingIdentifier.identifyBuilding(model) == PwcgStructure.BRIDGE)
        {
            entity.setName("Bridge entity");
        }
        else
        {
            entity.setName("Block entity");
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
}
