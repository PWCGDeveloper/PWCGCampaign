package pwcg.dev.utils;

import java.util.List;

import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGProduct;
import pwcg.campaign.plane.PlaneType;
import pwcg.campaign.plane.PlaneTypeFactory;
import pwcg.campaign.plane.SquadronPlaneAssignment;
import pwcg.campaign.squadron.Squadron;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.PWCGLogger;
import pwcg.core.utils.PWCGLogger.LogLevel;

public class SquadronPlaneLister 
{    
    static public void main (String[] args)
	{
        UserDir.setUserDir();

        try
		{
			SquadronPlaneLister finder = new SquadronPlaneLister(PWCGProduct.FC);
			finder.printPlanes();
		}
		catch (Exception e)
		{
			 PWCGLogger.logException(e);
		}
	}
    public SquadronPlaneLister(PWCGProduct product) throws PWCGException
    {
        PWCGContext.setProduct(product);
    }


    private void printPlanes() throws PWCGException  
    {       
        List<Squadron> allSq =  PWCGContext.getInstance().getSquadronManager().getAllSquadrons();
        PlaneTypeFactory planeTypeFactory = PWCGContext.getInstance().getPlaneTypeFactory();
        for (Squadron squad : allSq)
        {
            PWCGLogger.log(LogLevel.DEBUG, "Squadron: " + squad.getFileName());
            boolean hasPlane = false;
            for (SquadronPlaneAssignment planeAssignment : squad.getPlaneAssignments())
            {
                for (PlaneType plane : planeTypeFactory.createPlaneTypesForArchType(planeAssignment.getArchType()))
                {
                    PWCGLogger.log(LogLevel.DEBUG, "        " + plane.getDisplayName());
                }
            }

            if (hasPlane)
            {
                PWCGLogger.log(LogLevel.DEBUG, "" + squad.getSquadronId());
            }
        }
    }
}
