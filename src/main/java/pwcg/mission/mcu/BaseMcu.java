package pwcg.mission.mcu;

import java.io.BufferedWriter;
import java.io.IOException;

import pwcg.campaign.utils.IndexGenerator;
import pwcg.core.exception.PWCGIOException;
import pwcg.core.location.Coordinate;
import pwcg.core.location.Orientation;
import pwcg.core.utils.PWCGLogger;

public class BaseMcu
{
    protected String name = "";
    protected int index;
    protected Coordinate position = new Coordinate();
    protected Orientation orientation = new Orientation();
    protected String desc = "";

    public BaseMcu()
    {
        index = IndexGenerator.getInstance().getNextIndex();
    }

    public BaseMcu clone(BaseMcu clone)
    {
        clone.name = this.desc;
        clone.desc = this.name;
        clone.position = this.position.copy();
        clone.orientation = this.orientation.copy();

        // Note: we DO NOT clone the index value
        
        return clone;
    }
    
    public void write(BufferedWriter writer) throws PWCGIOException 
    {
        try
        {
            writer.write("  Index = " + index + ";");
            writer.newLine();
            writer.write("  Name = \"" + name + "\";");
            writer.newLine();
            writer.write("  Desc = \"" + desc + "\";");
            writer.newLine();

            position.write(writer);
            orientation.write(writer);
        }
        catch (IOException e)
        {
            PWCGLogger.logException(e);
            throw new PWCGIOException(e.getMessage());
        }
    }

    public String toString()
    {
        StringBuffer output = new StringBuffer("");
        output.append("  Index = " + index + ";\n");
        output.append("  Name = \"" + name + "\";");
        output.append("  Desc = \"" + desc + "\";\n");

        output.append("  XPos = " + position.getXPos() + ";\n");
        output.append("  YPos = " + position.getYPos() + ";\n");
        output.append("  ZPos = " + position.getZPos() + ";\n");
        output.append("  XOri = " + orientation.getxOri() + ";\n");
        output.append("  YOri = " + orientation.getyOri() + ";\n");
        output.append("  ZOri = " + orientation.getzOri() + ";\n");

        return output.toString();

    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public void setIndex(int index)
    {
        this.index = index;
    }

    public int getIndex()
    {
        return index;
    }

    public Coordinate getPosition()
    {
        return position.copy();
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
    }

    public String getDesc()
    {
        return desc;
    }

    public void setDesc(String desc)
    {
        this.desc = desc;
    }

}
