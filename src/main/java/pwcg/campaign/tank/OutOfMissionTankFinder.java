package pwcg.campaign.tank;

import java.util.Date;

import pwcg.campaign.company.Company;
import pwcg.campaign.context.PWCGContext;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.DateUtils;

public class OutOfMissionTankFinder
{

    public TankType findTankType(Company squadron, PwcgRoleCategory roleCategory, Date date) throws PWCGException
    {
        TankType planeType = findPreferredTankTypeForSquadron(squadron, date);
        if (planeType == null)
        {
            planeType = findAlternativeTankTypeForSquadron(squadron, roleCategory, date);
        }
        
        if (planeType == null)
        {
            planeType = findAnyTankTypeForCountryAndDate(squadron, date);
        }
        
        if (planeType == null)
        {
            planeType = findEarliestTankTypeForSquadron(squadron);
        }

        if (planeType == null)
        {
            throw new PWCGException("Unable to find any plane for squadron " + squadron.determineDisplayName(date) + " on date " + DateUtils.getDateStringYYYYMMDD(date));
        }

        return planeType;
    }

    private TankType findPreferredTankTypeForSquadron(Company squadron, Date date) throws PWCGException
    {
        TankType planeType = squadron.determineBestPlane(date);
        return planeType;
    }

    private TankType findAlternativeTankTypeForSquadron(Company squadron, PwcgRoleCategory roleCategory, Date date) throws PWCGException
    {
        TankType planeType = PWCGContext.getInstance().getTankTypeFactory().findActiveTankTypeByCountryDateAndRole(
                squadron.determineSquadronCountry(date), date, roleCategory);
        return planeType;        
    }

    private TankType findAnyTankTypeForCountryAndDate(Company squadron, Date date) throws PWCGException
    {
        TankType planeType = PWCGContext.getInstance().getTankTypeFactory().findAnyTankTypeForCountryAndDate(
                squadron.determineSquadronCountry(date), date);
        return planeType;        
    }

    private TankType findEarliestTankTypeForSquadron(Company squadron) throws PWCGException
    {
        TankType planeType = squadron.determineEarliestPlane();
        return planeType;
    }
}
