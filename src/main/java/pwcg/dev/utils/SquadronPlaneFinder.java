package pwcg.dev.utils;

import java.util.Date;
import java.util.List;

import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGProduct;
import pwcg.campaign.plane.SquadronPlaneAssignment;
import pwcg.campaign.squadron.Squadron;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.DateUtils;
import pwcg.core.utils.PWCGLogger;
import pwcg.core.utils.PWCGLogger.LogLevel;

public class SquadronPlaneFinder 
{    
    static public void main (String[] args)
	{
        UserDir.setUserDir();

        try
		{
			SquadronPlaneFinder finder = new SquadronPlaneFinder(PWCGProduct.FC);
			
			Date startDate = DateUtils.getBeginningOfWar();
			Date endDate = DateUtils.getEndOfWar();
			
			finder.findPlane("sopstrutter", startDate, endDate);
		}
		catch (Exception e)
		{
			 PWCGLogger.logException(e);
		}
	}
    public SquadronPlaneFinder(PWCGProduct product) throws PWCGException
    {
        PWCGContext.setProduct(product);
    }



    
    private void findPlane(String planeId, Date startDate, Date endDate) throws PWCGException  
    {       
        List<Squadron> allSq =  PWCGContext.getInstance().getSquadronManager().getAllSquadrons();
        for (Squadron squad : allSq)
        {
            boolean hasPlane = false;
            for (SquadronPlaneAssignment planeAssignment : squad.getPlaneAssignments())
            {
                if (planeAssignment.getArchType().equals(planeId))
                {
                    hasPlane = true;
                }
            }

            if (hasPlane)
            {
                PWCGLogger.log(LogLevel.DEBUG, "" + squad.getFileName() + " uses the " + planeId);
            }
        }
    }
 }
