package pwcg.campaign.group;

import java.io.BufferedWriter;
import java.io.IOException;

import pwcg.core.exception.PWCGException;
import pwcg.core.exception.PWCGIOException;
import pwcg.core.utils.PWCGLogger;

public class Block extends FixedPosition
{	
    public Block()
    {
        super();
    }

	public void write(BufferedWriter writer) throws PWCGException
	{
        try
        {
    		writer.write("Block");
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
            throw new PWCGIOException(e.getMessage());
        }
	}
}
