package pwcg.aar.outofmission.phase1.elapsedtime;

import java.util.Date;

import pwcg.campaign.context.PWCGContextManager;
import pwcg.campaign.plane.PlaneType;
import pwcg.campaign.plane.Role;
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
            planeType = findAnyPlaneTypeForSideAndDate(squadron, date);
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
        PlaneType planeType = PWCGContextManager.getInstance().getPlaneTypeFactory().getActivePlaneBySideDateAndRole(
                squadron.determineSquadronCountry(date).getSide(), date, role);
        return planeType;        
    }

    private PlaneType findAnyPlaneTypeForSideAndDate(Squadron squadron, Date date) throws PWCGException
    {
        PlaneType planeType = PWCGContextManager.getInstance().getPlaneTypeFactory().getActivePlaneBySideAndDate(
                squadron.determineSquadronCountry(date).getSide(), date);
        return planeType;        
        
    }
}
