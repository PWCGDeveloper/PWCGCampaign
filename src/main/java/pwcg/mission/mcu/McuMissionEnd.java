package pwcg.mission.mcu;

import java.io.BufferedWriter;
import java.io.IOException;

import pwcg.core.exception.PWCGIOException;
import pwcg.core.utils.PWCGLogger;

public class McuMissionEnd extends BaseFlightMcu
{
	private int enabled = 1;
	private int success = 1;

	public McuMissionEnd ()
	{
	}

	public int getEnabled() {
		return enabled;
	}

	public void setEnabled(int enabled) {
		this.enabled = enabled;
	}
	
	public void write(BufferedWriter writer) throws PWCGIOException
	{
		try
        {
            writer.write("MCU_TR_MissionEnd");
            writer.newLine();
            writer.write("{");
            writer.newLine();
            
            super.write(writer);
            
            writer.write("  Enabled = " + enabled + ";");
            writer.newLine();
            writer.write("  Succeeded  = " + success + ";");
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

	public String toString()
	{
		StringBuffer output = new StringBuffer("");
		output.append("MCU_TR_MissionEnd\n");
		output.append("{\n");
		
		output.append(super.toString());
		output.append("  Enabled = " + enabled + ";\n");
		output.append("  Succeeded  = " + success + ";\n");

		output.append("}\n");
		output.append("\n");
		output.append("\n");
		
		return output.toString();

	}
}
