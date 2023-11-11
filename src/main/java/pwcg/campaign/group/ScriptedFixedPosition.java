package pwcg.campaign.group;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import pwcg.campaign.api.IFixedPosition;
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
        super.clone(clone);

        clone.model = this.model;
        clone.script = this.script;
        clone.durability = this.durability;
        clone.deleteAfterDeath = this.deleteAfterDeath;
        
        clone.damaged = new HashMap<>();
        clone.damaged.putAll(this.damaged);

        return clone;
    }

    public void write(BufferedWriter writer) throws PWCGException, PWCGException
    {
        try
        {
            super.write(writer);
            
            writer.write("  Script = \"" + script + "\";");
            writer.newLine();
            writer.write("  DeleteAfterDeath = " + deleteAfterDeath + ";");
            writer.newLine();
            writer.write("  Durability = " + durability + ";");
            writer.newLine();
            if (!damaged.isEmpty())
            {
                writer.write("  Damaged");
                writer.newLine();
                writer.write("  {");
                writer.newLine();
                for (int damagedIndex : damaged.keySet())
                {
                    double damageValue = damaged.get(damagedIndex);
                    writer.write("    " + damagedIndex + " = " + damageValue + ";");
                    writer.newLine();
                }
                writer.write("  }");
                writer.newLine();
            }            
        }
        catch (IOException e)
        {
            PWCGLogger.logException(e);
            throw new PWCGException(e.getMessage());
        }
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
