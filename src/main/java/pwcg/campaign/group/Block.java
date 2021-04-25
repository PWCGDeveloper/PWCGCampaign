package pwcg.campaign.group;

import java.io.BufferedWriter;
import java.io.IOException;

import pwcg.core.exception.PWCGException;
import pwcg.core.exception.PWCGIOException;
import pwcg.core.utils.PWCGLogger;
import pwcg.mission.ground.building.PwcgBuildingIdentifier;
import pwcg.mission.ground.building.PwcgStructure;
import pwcg.product.bos.plane.BoSStaticPlane;
import pwcg.product.fc.plane.FCStaticPlane;

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
            if (isBuildEntity())
            {
                buildEntity();
            }

    		writer.write("Block");
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
	
	   
    private boolean isBuildEntity() throws PWCGException
    {
        if (determineCountry().isNeutral())
        {
            return false;
        }        
        if (this instanceof BoSStaticPlane)
        {
            return true;
        }
        
        if (this instanceof FCStaticPlane)
        {
            return true;
        }
        
        if (PwcgBuildingIdentifier.identifyBuilding(this.getModel()) != PwcgStructure.UNKNOWN)
        {
            return true;
        }
        
        return false;
    }

}
