package pwcg.mission.mcu;

import java.io.BufferedWriter;
import java.io.IOException;

import pwcg.core.exception.PWCGIOException;
import pwcg.core.utils.Logger;

public class McuDelete extends BaseFlightMcu
{
	public McuDelete()
	{
		super();
	}

	public void write(BufferedWriter writer) throws PWCGIOException
	{
		try
        {
            writer.write("MCU_Delete");
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
            Logger.logException(e);
            throw new PWCGIOException(e.getMessage());
        }
	}
	
	public String toString()
	{
		StringBuffer output = new StringBuffer("");
		output.append("MCU_Delete\n");
		output.append("{\n");
		
		output.append(super.toString());

		output.append("}\n");
		output.append("\n");
		output.append("\n");
		
		return output.toString();

	}
}
