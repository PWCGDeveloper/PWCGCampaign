package pwcg.mission.mcu;

import java.io.BufferedWriter;
import java.io.IOException;

import pwcg.core.exception.PWCGIOException;
import pwcg.core.utils.PWCGLogger;

public class McuMessage 
{
    static public int ONSPAWN = 2;
    static public int ONKILL = 9;
    static public int ONTAKEOFF = 6;
    static public int ONTTOOKOFF = 0;

    private int type = 14;
    private int cmdId = 14;
    private int tarId = 0;

    public void write(BufferedWriter writer) throws PWCGIOException
    {
        try
        {
            writer.write("    OnMsg");
            writer.newLine();
            writer.write("    {");
            writer.newLine();
            
            writer.write("    Type = " + type + ";");
            writer.newLine();
            writer.write("    CmdId = " + cmdId + ";");
            writer.newLine();
            writer.write("    TarId = " + tarId + ";");
            writer.newLine();
    
            writer.write("    }");
            writer.newLine();
        }
        catch (IOException e)
        {
            PWCGLogger.logException(e);
            throw new PWCGIOException(e.getMessage());
        }
    }

    public int getType()
    {
        return type;
    }

    public void setType(int type)
    {
        this.type = type;
    }

    public int getCmdId()
    {
        return cmdId;
    }

    public void setCmdId(int cmdId)
    {
        this.cmdId = cmdId;
    }

    public int getTarId()
    {
        return tarId;
    }

    public void setTarId(int tarId)
    {
        this.tarId = tarId;
    }
}
