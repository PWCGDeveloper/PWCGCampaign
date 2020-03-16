package pwcg.mission.mcu;

import java.io.BufferedWriter;
import java.io.IOException;

import pwcg.core.exception.PWCGIOException;
import pwcg.core.utils.PWCGLogger;

public class McuFlare extends BaseFlightMcu
{
    public static final int FLARE_COLOR_RED = 0;
    public static final int FLARE_COLOR_GREEN = 1;
    public static final int FLARE_COLOR_YELLOW = 2;
    public static final int FLARE_COLOR_WHITE = 3;
    
    protected int color = FLARE_COLOR_RED;

    public McuFlare()
    {
        super();
        
        name = "Flare";
        desc = "Flare";
        
    }

    public int getColor()
    {
        return this.color;
    }

    public void setColor(int color)
    {
        this.color = color;
    }

    public void write(BufferedWriter writer) throws PWCGIOException
    {
        try
        {
            writer.write("MCU_CMD_Flare");
            writer.newLine();
            writer.write("{");
            writer.newLine();
            
            super.write(writer);
            
            writer.write("  Color = " + color + ";");

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
}
