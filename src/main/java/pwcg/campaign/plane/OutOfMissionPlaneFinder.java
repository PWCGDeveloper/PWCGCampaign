package pwcg.campaign.plane;

import java.util.Date;

import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.squadron.Company;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.DateUtils;

public class OutOfMissionPlaneFinder
{

    public PlaneType findPlaneType(Company squadron, PwcgRoleCategory roleCategory, Date date) throws PWCGException
    {
        PlaneType planeType = findPreferredPlaneTypeForSquadron(squadron, date);
        if (planeType == null)
        {
            planeType = findAlternativePlaneTypeForSquadron(squadron, roleCategory, date);
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

    private PlaneType findPreferredPlaneTypeForSquadron(Company squadron, Date date) throws PWCGException
    {
        PlaneType planeType = squadron.determineBestPlane(date);
        return planeType;
    }

    private PlaneType findAlternativePlaneTypeForSquadron(Company squadron, PwcgRoleCategory roleCategory, Date date) throws PWCGException
    {
        PlaneType planeType = PWCGContext.getInstance().getPlaneTypeFactory().findActivePlaneTypeByCountryDateAndRole(
                squadron.determineSquadronCountry(date), date, roleCategory);
        return planeType;        
    }

    private PlaneType findAnyPlaneTypeForCountryAndDate(Company squadron, Date date) throws PWCGException
    {
        PlaneType planeType = PWCGContext.getInstance().getPlaneTypeFactory().findAnyPlaneTypeForCountryAndDate(
                squadron.determineSquadronCountry(date), date);
        return planeType;        
    }

    private PlaneType findEarliestPlaneTypeForSquadron(Company squadron) throws PWCGException
    {
        PlaneType planeType = squadron.determineEarliestPlane();
        return planeType;
    }
}
