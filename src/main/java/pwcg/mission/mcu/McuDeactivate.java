package pwcg.mission.mcu;

import java.io.BufferedWriter;
import java.io.IOException;

import pwcg.core.exception.PWCGException;
import pwcg.core.utils.PWCGLogger;

public class McuDeactivate extends BaseFlightMcu
{
    
    
    public McuDeactivate copy ()
    {
        McuDeactivate clone = new McuDeactivate();
        
        super.clone(clone);

        return clone;
    }

	public void write(BufferedWriter writer) throws PWCGException
	{
		try
        {
            writer.write("MCU_Deactivate");
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
            throw new PWCGException(e.getMessage());
        }
	}
	
	public String toString()
	{
		StringBuffer output = new StringBuffer("");
		output.append("MCU_Deactivate\n");
		output.append("{\n");
		
		output.append(super.toString());

		output.append("}\n");
		output.append("\n");
		output.append("\n");
		
		return output.toString();

	}

    public void setDeactivateTarget(int target)
    {
        super.setTarget(target);
    }
}
