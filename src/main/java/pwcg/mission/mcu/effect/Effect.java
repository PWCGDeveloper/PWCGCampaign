package pwcg.mission.mcu.effect;

import java.io.BufferedWriter;
import java.io.IOException;

import pwcg.campaign.utils.IndexGenerator;
import pwcg.core.exception.PWCGIOException;
import pwcg.core.location.Coordinate;
import pwcg.core.location.Orientation;
import pwcg.core.utils.PWCGLogger;
import pwcg.mission.mcu.McuTREntity;

public abstract class Effect implements Cloneable
{
    protected String displayName = "Effect";
    protected String name = "Effect";
    protected int index;
    protected int linkTrId = 0;
    protected Coordinate position = new Coordinate();;
    protected Orientation orientation = new Orientation();
    protected String script = "";
    protected String model = "";
    protected String Desc = "";

    protected McuTREntity entity;

    protected Effect()
    {
        index = IndexGenerator.getInstance().getNextIndex();
        entity = new McuTREntity(index);
        linkTrId = entity.getIndex();
    }

    public void populateEntity()
    {
        this.linkTrId = entity.getIndex();

        entity.setPosition(position);
        entity.setOrientation(orientation);
        entity.setEnabled(1);
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

    public Orientation getOrientation()
    {
        return orientation;
    }

    public void setOrientation(Orientation orientation)
    {
        this.orientation = orientation;
        entity.setOrientation(orientation);
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public Coordinate getPosition()
    {
        return position;
    }

    public void setPosition(Coordinate position)
    {
        this.position = position;
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

    public McuTREntity getEntity()
    {
        return entity;
    }

    public void setEntity(McuTREntity entity)
    {
        this.entity = entity;
    }

    public void write(BufferedWriter writer) throws PWCGIOException
    {
        try
        {
            writer.write("Effect");
            writer.newLine();
            writer.write("{");
            writer.newLine();

            writer.write("  Name = \"" + name + "\";");
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
            writer.write("  Desc = \"" + Desc + "\";");
            writer.newLine();

            writer.write("}");
            writer.newLine();
            writer.newLine();
            writer.newLine();

            entity.write(writer);
        }
        catch (IOException e)
        {
            PWCGLogger.logException(e);
            throw new PWCGIOException(e.getMessage());
        }
    }
}
