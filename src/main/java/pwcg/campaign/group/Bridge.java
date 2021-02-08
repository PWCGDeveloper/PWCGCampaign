package pwcg.campaign.group;

import java.io.BufferedWriter;
import java.io.IOException;

import pwcg.core.exception.PWCGException;
import pwcg.core.exception.PWCGIOException;
import pwcg.core.utils.PWCGLogger;

public class Bridge extends FixedPosition
{
	public Bridge()
	{
		super();
	}

	public void write(BufferedWriter writer) throws PWCGException
	{
		try
        {
            buildEntity();

            writer.write("Bridge");
            writer.newLine();
            writer.write("{");
            writer.newLine();
            
            super.write(writer);

            writer.write("}");
            writer.newLine();
            writer.newLine();
            
            if (entity != null)
            {
                entity.write(writer);
            }
        }
        catch (IOException e)
        {
            PWCGLogger.logException(e);
            throw new PWCGIOException(e.getMessage());
        }
	}
}
