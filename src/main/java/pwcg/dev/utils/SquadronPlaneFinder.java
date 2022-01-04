package pwcg.dev.utils;

import java.util.Date;
import java.util.List;

import pwcg.campaign.company.Company;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.tank.CompanyTankAssignment;
import pwcg.campaign.tank.TankType;
import pwcg.campaign.tank.TankTypeFactory;
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
        List<Company> allSq =  PWCGContext.getInstance().getCompanyManager().getAllCompanies();
        PWCGLogger.log(LogLevel.DEBUG, "TankType Id: " + planeId);
        for (Company company : allSq)
        {
            boolean hasPlane = false;
            for (CompanyTankAssignment planeAssignment : company.getPlaneAssignments())
            {
                if (planeAssignment.getArchType().equals(planeId))
                {
                    if (!planeAssignment.getCompanyWithdrawal().before(endDate))
                    {
                        hasPlane = true;
                    }
                }
            }

            if (hasPlane)
            {
                PWCGLogger.log(LogLevel.DEBUG, "" + company.getCompanyId());
            }
        }
    }
    
    private void printPlanes() throws PWCGException  
    {       
        List<Company> allSq =  PWCGContext.getInstance().getCompanyManager().getAllCompanies();
        TankTypeFactory planeTypeFactory = PWCGContext.getInstance().getTankTypeFactory();
        for (Company company : allSq)
        {
            PWCGLogger.log(LogLevel.DEBUG, "Squadron: " + company.getCompanyId());
            boolean hasPlane = false;
            for (CompanyTankAssignment planeAssignment : company.getPlaneAssignments())
            {
                for (TankType plane : planeTypeFactory.createTankTypesForArchType(planeAssignment.getArchType()))
                {
                    PWCGLogger.log(LogLevel.DEBUG, "        " + plane.getDisplayName());
                }
            }

            if (hasPlane)
            {
                PWCGLogger.log(LogLevel.DEBUG, "" + company.getCompanyId());
            }
        }
    }
}
