package pwcg.dev.utils;

import java.util.Date;
import java.util.List;

import pwcg.campaign.context.PWCGContextManager;
import pwcg.campaign.plane.PlaneType;
import pwcg.campaign.plane.PlaneTypeFactory;
import pwcg.campaign.plane.SquadronPlaneAssignment;
import pwcg.campaign.squadron.Squadron;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.DateUtils;
import pwcg.core.utils.Logger;
import pwcg.core.utils.Logger.LogLevel;

public class SquadronPlaneFinder 
{
	static public void main (String[] args)
	{
        UserDir.setUserDir();

        try
		{
			SquadronPlaneFinder finder = new SquadronPlaneFinder();
			
			Date startDate = DateUtils.getBeginningOfWar();
			Date endDate = DateUtils.getEndOfWar();
			
			finder.findPlane("bf109", startDate, endDate);
			
			finder.printPlanes();
		}
		catch (Exception e)
		{
			 Logger.logException(e);;
		}
	}

    
    private void findPlane(String planeId, Date startDate, Date endDate) throws PWCGException  
    {       
        List<Squadron> allSq =  PWCGContextManager.getInstance().getSquadronManager().getAllSquadrons();
        Logger.log(LogLevel.DEBUG, "PlaneType Id: " + planeId);
        for (Squadron squad : allSq)
        {
            boolean hasPlane = false;
            for (SquadronPlaneAssignment planeAssignment : squad.getPlaneAssignments())
            {
                if (planeAssignment.getArchType().equals(planeId))
                {
                    if (!planeAssignment.getSquadronWithdrawal().before(endDate))
                    {
                        hasPlane = true;
                    }
                }
            }

            if (hasPlane)
            {
                Logger.log(LogLevel.DEBUG, "" + squad.getSquadronId());
            }
        }
    }
    
    private void printPlanes() throws PWCGException  
    {       
        List<Squadron> allSq =  PWCGContextManager.getInstance().getSquadronManager().getAllSquadrons();
        PlaneTypeFactory planeTypeFactory = PWCGContextManager.getInstance().getPlaneTypeFactory();
        for (Squadron squad : allSq)
        {
            System.out.println("Squadron: " + squad.getSquadronId());
            boolean hasPlane = false;
            for (SquadronPlaneAssignment planeAssignment : squad.getPlaneAssignments())
            {
                for (PlaneType plane : planeTypeFactory.createPlaneTypesForArchType(planeAssignment.getArchType()))
                {
                    System.out.println("        " + plane.getDisplayName());
                }
            }

            if (hasPlane)
            {
                Logger.log(LogLevel.DEBUG, "" + squad.getSquadronId());
            }
        }
    }
}
