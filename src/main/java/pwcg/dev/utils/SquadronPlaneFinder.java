package pwcg.dev.utils;

import java.util.Date;
import java.util.List;

import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.plane.PlaneType;
import pwcg.campaign.plane.PlaneTypeFactory;
import pwcg.campaign.plane.SquadronPlaneAssignment;
import pwcg.campaign.squadron.Company;
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
			SquadronPlaneFinder finder = new SquadronPlaneFinder();
			
			Date startDate = DateUtils.getBeginningOfWar();
			Date endDate = DateUtils.getEndOfWar();
			
			finder.findPlane("bf109", startDate, endDate);
			
			finder.printPlanes();
		}
		catch (Exception e)
		{
			 PWCGLogger.logException(e);;
		}
	}

    
    private void findPlane(String planeId, Date startDate, Date endDate) throws PWCGException  
    {       
        List<Company> allSq =  PWCGContext.getInstance().getSquadronManager().getAllSquadrons();
        PWCGLogger.log(LogLevel.DEBUG, "PlaneType Id: " + planeId);
        for (Company company : allSq)
        {
            boolean hasPlane = false;
            for (SquadronPlaneAssignment planeAssignment : company.getPlaneAssignments())
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
                PWCGLogger.log(LogLevel.DEBUG, "" + company.getSquadronId());
            }
        }
    }
    
    private void printPlanes() throws PWCGException  
    {       
        List<Company> allSq =  PWCGContext.getInstance().getSquadronManager().getAllSquadrons();
        PlaneTypeFactory planeTypeFactory = PWCGContext.getInstance().getPlaneTypeFactory();
        for (Company company : allSq)
        {
            PWCGLogger.log(LogLevel.DEBUG, "Squadron: " + company.getSquadronId());
            boolean hasPlane = false;
            for (SquadronPlaneAssignment planeAssignment : company.getPlaneAssignments())
            {
                for (PlaneType plane : planeTypeFactory.createPlaneTypesForArchType(planeAssignment.getArchType()))
                {
                    PWCGLogger.log(LogLevel.DEBUG, "        " + plane.getDisplayName());
                }
            }

            if (hasPlane)
            {
                PWCGLogger.log(LogLevel.DEBUG, "" + company.getSquadronId());
            }
        }
    }
}
