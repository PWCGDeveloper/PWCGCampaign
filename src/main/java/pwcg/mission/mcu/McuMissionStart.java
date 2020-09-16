package pwcg.mission.mcu;

import java.io.BufferedWriter;

import pwcg.core.exception.PWCGIOException;
import pwcg.core.utils.PWCGLogger;

public class McuMissionStart extends BaseFlightMcu
{
	private int enabled = 1;

	public McuMissionStart ()
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
            writer.write("MCU_TR_MissionBegin");
            writer.newLine();
            writer.write("{");
            writer.newLine();
            
            super.write(writer);
            
            writer.write("  Enabled = " + enabled + ";");
            writer.newLine();

            writer.write("}");
            writer.newLine();
            writer.newLine();
            writer.newLine();
        }
        catch (Exception e)
        {
            PWCGLogger.logException(e);
            throw new PWCGIOException(e.getMessage());
        }
	}
	
	/**
	 * @see rof.campaign.mcu.BaseFlightMcu#toString()
	 */
	public String toString()
	{
		StringBuffer output = new StringBuffer("");
		output.append("MCU_TR_MissionBegin\n");
		output.append("{\n");
		
		output.append(super.toString());
		output.append("  Enabled = " + enabled + ";\n");

		output.append("}\n");
		output.append("\n");
		output.append("\n");
		
		return output.toString();

	}
}
