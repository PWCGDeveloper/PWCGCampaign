package pwcg.campaign.group;

import java.util.HashMap;
import java.util.Map;

import pwcg.campaign.api.IFixedPosition;
import pwcg.campaign.utils.IndexGenerator;
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
