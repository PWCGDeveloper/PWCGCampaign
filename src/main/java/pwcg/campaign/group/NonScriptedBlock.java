package pwcg.campaign.group;

import java.io.BufferedWriter;
import java.io.IOException;

import pwcg.core.exception.PWCGException;
import pwcg.core.utils.PWCGLogger;

public class NonScriptedBlock extends FixedPosition
{	

    public NonScriptedBlock()
    {
        super();
    }

	public void write(BufferedWriter writer) throws PWCGException
	{
        try
        {            
    		writer.write("Ground");
    		writer.newLine();
    		writer.write("{");
    		writer.newLine();
    		
    		super.write(writer);
    		    			
    		writer.write("}");
    		writer.newLine();
    		writer.newLine();
        }
        catch (IOException e)
        {
            PWCGLogger.logException(e);
            throw new PWCGException(e.getMessage());
        }
	}
}
