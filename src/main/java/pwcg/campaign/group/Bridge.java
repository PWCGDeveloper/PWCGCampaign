package pwcg.campaign.group;

import java.io.BufferedWriter;
import java.io.IOException;

import pwcg.campaign.context.Country;
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
            if (isBuildEntity())
            {
                buildEntity();
            }

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
                System.out.println(this.getModel());
                entity.write(writer);
            }
        }
        catch (IOException e)
        {
            PWCGLogger.logException(e);
            throw new PWCGIOException(e.getMessage());
        }
	}
	
	private boolean isBuildEntity()
	{
	    if (country != Country.NEUTRAL)
	    {
	        return true;
	    }
	    return false;
	}
}
