package pwcg.campaign.company;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import pwcg.campaign.ArmedService;
import pwcg.campaign.Campaign;
import pwcg.campaign.api.ICountry;
import pwcg.campaign.api.Side;
import pwcg.campaign.group.airfield.Airfield;
import pwcg.campaign.tank.PwcgRole;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.utils.DateUtils;
import pwcg.core.utils.MathUtils;

public class CompanyReducer
{

    public static List<Company> reduceToAIOnly(List<Company> selectedSquadrons, Campaign campaign) throws PWCGException
    {
        List<Company> selectedSquadronsNoPlayer = new ArrayList<>();
        for (Company company : selectedSquadrons)
        {
            if (!campaign.getPersonnelManager().companyHasActivePlayers(company.getCompanyId()))
            {
                selectedSquadronsNoPlayer.add(company);
            }
        }
        return selectedSquadronsNoPlayer;
    }
    
    public static List<Company> reduceToSide(List<Company> companies, Side side) throws PWCGException
    {       
        List<Company> companiesForSide = new ArrayList<>();
        for (Company company : companies)
        {
            if (side == company.determineSide())
            {
                companiesForSide.add(company);
            }
        }
        return companiesForSide;
    }

    public static List<Company> reduceToCurrentMap(List<Company> companies, Date date) throws PWCGException 
    {
        List<Company> companiesForMap = new ArrayList<>();
        for (Company company : companies)
        {
            Airfield field = company.determineCurrentAirfieldCurrentMap(date);
            if (field != null)
            {
                companiesForMap.add(company);
            }
        }

        return companiesForMap;
    }
    
    public static List<Company> reduceToService(List<Company> companies, Date date, ArmedService service) throws PWCGException 
    {
        List<Company> companiesForService = new ArrayList<>();
        for (Company company : companies)
        {
            if (company.determineServiceForSquadron(date).getServiceId() == service.getServiceId())
            {
                companiesForService.add(company);
            }
        }
        return companiesForService;
    }
    

    public static List<Company> reduceToCountry(List<Company> companies, Date date, ICountry country) throws PWCGException
    {
        List<Company> companiesForCountry = new ArrayList<>();
        for (Company company : companies)
        {
            ICountry squadCountry = company.determineSquadronCountry(date);
            if (squadCountry.equals(country))
            {
                companiesForCountry.add(company);
            }
        }
        return companiesForCountry;
    }

    public static List<Company> reduceToRole(List<Company> companies, List<PwcgRole> acceptableRoles, Date date) throws PWCGException 
    {       
        Map<Integer, Company> companiesWithRole = new HashMap<>();
        for(Company company : companies)
        {
            for (PwcgRole acceptableRole : acceptableRoles)
            {
                if (company.isSquadronThisRole(date, acceptableRole))
                {
                    companiesWithRole.put(company.getCompanyId(), company);
                }
            }
        }

        return new ArrayList<>(companiesWithRole.values());
    }

    public static List<Company> reduceToProximityOnCurrentMap(List<Company> companies, Date date, Coordinate referencePosition, double radius) throws PWCGException 
    {
        List<Company> companiesWithinRadius = new ArrayList<>();
        List<Company> companiesOnMap = reduceToCurrentMap(companies, date);
        for (Company company : companiesOnMap)
        {
            Coordinate companyPosition = company.determineCurrentPosition(date);
            double distanceFromReference = MathUtils.calcDist(referencePosition, companyPosition);
            if (distanceFromReference <= radius)
            {
                companiesWithinRadius.add(company);
            }
        }

        return companiesWithinRadius;
    }

    public static List<Company> sortByProximityOnCurrentMap(List<Company> companies, Date date, Coordinate referencePosition) throws PWCGException 
    {
        Map<Integer, Company> companiesByProximity = new TreeMap<>();
        List<Company> companiesOnMap = reduceToCurrentMap(companies, date);
        for (Company company : companiesOnMap)
        {
            Coordinate companyPosition = company.determineCurrentPosition(date);
            Double distanceFromReference = MathUtils.calcDist(referencePosition, companyPosition);
            companiesByProximity.put(distanceFromReference.intValue(), company);
        }

        return new ArrayList<Company>(companiesByProximity.values());
    }
    
    public static List<Company> reduceToNoAnomalies(List<Company> companies, Date date) throws PWCGException
    {       
        List<Company> companiesWithoutAnomalies = new ArrayList<>();
        for (Company company : companies)
        {
            if (!companyIsAnomaly(company, date))
            {
                companiesWithoutAnomalies.add(company);
            }
        }
        return companiesWithoutAnomalies;
    }
    
    private static boolean companyIsAnomaly(Company company, Date date) throws PWCGException
    {
        if (company.getCompanyId() == 20115021)
        {
            return true;
        }
        else if (company.getCompanyId() == 20111051 && (date.before(DateUtils.getDateYYYYMMDD("19430301"))))
        {
            return true;
        }
        
        return false;
    }
}
