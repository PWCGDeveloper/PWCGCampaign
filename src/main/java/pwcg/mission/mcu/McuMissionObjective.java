package pwcg.mission.mcu;

import java.io.BufferedWriter;
import java.io.IOException;

import pwcg.campaign.api.ICountry;
import pwcg.campaign.api.Side;
import pwcg.core.exception.PWCGIOException;
import pwcg.core.utils.PWCGLogger;

public class McuMissionObjective  extends BaseFlightMcu
{

    private int enabled = 1;
    private int lcName = 3;
    private int lcDesc = 4;
    private int taskType = 0;
    private Coalition coalition = Coalition.COALITION_ALLIED;
    private int success = 1;
    private int iconType = 904;

    public McuMissionObjective ()
    {
        super();
    }

    public void write(BufferedWriter writer) throws PWCGIOException
    {
        try
        {
            writer.write("MCU_TR_MissionObjective");
            writer.newLine();
            writer.write("{");
            writer.newLine();
            
            super.write(writer);

            writer.write("  Enabled = " + enabled + ";");
            writer.newLine();
            writer.write("  LCName = " + lcName + ";");
            writer.newLine();
            writer.write("  LCDesc = " + lcDesc + ";");
            writer.newLine();
            writer.write("  TaskType = " + taskType + ";");
            writer.newLine();
            writer.write("  Coalition = " + coalition.getCoalitionValue() + ";");
            writer.newLine();
            writer.write("  Success = " + success + ";");
            writer.newLine();
            writer.write("  IconType = " + iconType + ";");
            writer.newLine();
            
            writer.write("}");
            writer.newLine();
            writer.newLine();
            writer.newLine();
        }
        catch (IOException e)
        {
            PWCGLogger.logException(e);
            throw new PWCGIOException(e.getMessage());
        }
    }

    public int getEnabled()
    {
        return this.enabled;
    }

    public void setEnabled(int enabled)
    {
        this.enabled = enabled;
    }

    public int getLcName()
    {
        return this.lcName;
    }

    public void setLcName(int lcName)
    {
        this.lcName = lcName;
    }

    public int getLcDesc()
    {
        return this.lcDesc;
    }

    public void setLcDesc(int lcDesc)
    {
        this.lcDesc = lcDesc;
    }

    public int getTaskType()
    {
        return this.taskType;
    }

    public void setTaskType(int taskType)
    {
        this.taskType = taskType;
    }

    public Coalition getCoalition()
    {
        return this.coalition;
    }

    public void setCoalition(ICountry country)
    {
        if (country.getSide() == Side.ALLIED)
        {
            coalition = Coalition.COALITION_ALLIED;
        }
        else
        {
            coalition = Coalition.COALITION_AXIS;
        }
     }

    public int getSuccess()
    {
        return this.success;
    }

    public void setSuccess(int success)
    {
        this.success = success;
    }

    public int getIconType()
    {
        return this.iconType;
    }

    public void setIconType(int iconType)
    {
        this.iconType = iconType;
    }

}
