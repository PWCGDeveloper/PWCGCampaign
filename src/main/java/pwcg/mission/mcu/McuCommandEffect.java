package pwcg.mission.mcu;

import java.io.BufferedWriter;
import java.io.IOException;

import pwcg.core.exception.PWCGIOException;
import pwcg.core.utils.PWCGLogger;

public class McuCommandEffect extends BaseFlightMcu
{
    public McuCommandEffect()
    {
        super();
    }

    public void write(BufferedWriter writer) throws PWCGIOException
    {
        try
        {
            writer.write("MCU_CMD_Effect");
            writer.newLine();
            writer.write("{");
            writer.newLine();
            
            super.write(writer);
            
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
    
    public String toString()
    {
        StringBuffer output = new StringBuffer("");
        output.append("MCU_CMD_Effect\n");
        output.append("{\n");
        
        output.append(super.toString());

        output.append("}\n");
        output.append("\n");
        output.append("\n");
        
        return output.toString();

    }
}
