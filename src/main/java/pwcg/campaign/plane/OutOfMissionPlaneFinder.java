package pwcg.campaign.plane;

import java.util.Date;

import pwcg.campaign.context.PWCGContextManager;
import pwcg.campaign.squadron.Squadron;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.DateUtils;

public class OutOfMissionPlaneFinder
{

    public PlaneType findPlaneType(Squadron squadron, Role role, Date date) throws PWCGException
    {
        PlaneType planeType = findPreferredPlaneTypeForSquadron(squadron, date);
        if (planeType == null)
        {
            planeType = findAlternativePlaneTypeForSquadron(squadron, role, date);
        }
        
        if (planeType == null)
        {
            planeType = findAnyPlaneTypeForCountryAndDate(squadron, date);
        }
        
        if (planeType == null)
        {
            planeType = findEarliestPlaneTypeForSquadron(squadron);
        }

        if (planeType == null)
        {
            throw new PWCGException("Unable to find any plane for squadron " + squadron.determineDisplayName(date) + " on date " + DateUtils.getDateStringYYYYMMDD(date));
        }

        return planeType;
    }

    private PlaneType findPreferredPlaneTypeForSquadron(Squadron squadron, Date date) throws PWCGException
    {
        PlaneType planeType = squadron.determineBestPlane(date);
        return planeType;
    }

    private PlaneType findAlternativePlaneTypeForSquadron(Squadron squadron, Role role, Date date) throws PWCGException
    {
        PlaneType planeType = PWCGContextManager.getInstance().getPlaneTypeFactory().findActivePlaneTypeByCountryDateAndRole(
                squadron.determineSquadronCountry(date), date, role);
        return planeType;        
    }

    private PlaneType findAnyPlaneTypeForCountryAndDate(Squadron squadron, Date date) throws PWCGException
    {
        PlaneType planeType = PWCGContextManager.getInstance().getPlaneTypeFactory().findAnyPlaneTypeForCountryAndDate(
                squadron.determineSquadronCountry(date), date);
        return planeType;        
    }

    private PlaneType findEarliestPlaneTypeForSquadron(Squadron squadron) throws PWCGException
    {
        PlaneType planeType = squadron.determineEarliestPlane();
        return planeType;
    }
}
