package pwcg.campaign.squadron;

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
import pwcg.campaign.plane.PwcgRole;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.utils.DateUtils;
import pwcg.core.utils.MathUtils;

public class SquadronReducer
{

    public static List<Company> reduceToAIOnly(List<Company> selectedSquadrons, Campaign campaign) throws PWCGException
    {
        List<Company> selectedSquadronsNoPlayer = new ArrayList<>();
        for (Company squadron : selectedSquadrons)
        {
            if (!campaign.getPersonnelManager().squadronHasActivePlayers(squadron.getSquadronId()))
            {
                selectedSquadronsNoPlayer.add(squadron);
            }
        }
        return selectedSquadronsNoPlayer;
    }
    
    public static List<Company> reduceToSide(List<Company> squadrons, Side side) throws PWCGException
    {       
        List<Company> squadronsForSide = new ArrayList<>();
        for (Company squadron : squadrons)
        {
            if (side == squadron.determineSide())
            {
                squadronsForSide.add(squadron);
            }
        }
        return squadronsForSide;
    }

    public static List<Company> reduceToCurrentMap(List<Company> squadrons, Date date) throws PWCGException 
    {
        List<Company> squadronsForMap = new ArrayList<>();
        for (Company squadron : squadrons)
        {
            Airfield field = squadron.determineCurrentAirfieldCurrentMap(date);
            if (field != null)
            {
                squadronsForMap.add(squadron);
            }
        }

        return squadronsForMap;
    }
    
    public static List<Company> reduceToService(List<Company> squadrons, Date date, ArmedService service) throws PWCGException 
    {
        List<Company> squadronsForService = new ArrayList<>();
        for (Company squadron : squadrons)
        {
            if (squadron.determineServiceForSquadron(date).getServiceId() == service.getServiceId())
            {
                squadronsForService.add(squadron);
            }
        }
        return squadronsForService;
    }
    

    public static List<Company> reduceToCountry(List<Company> squadrons, Date date, ICountry country) throws PWCGException
    {
        List<Company> squadronsForCountry = new ArrayList<>();
        for (Company squadron : squadrons)
        {
            ICountry squadCountry = squadron.determineSquadronCountry(date);
            if (squadCountry.equals(country))
            {
                squadronsForCountry.add(squadron);
            }
        }
        return squadronsForCountry;
    }

    public static List<Company> reduceToRole(List<Company> squadrons, List<PwcgRole> acceptableRoles, Date date) throws PWCGException 
    {       
        Map<Integer, Company> squadronsWithRole = new HashMap<>();
        for(Company squadron : squadrons)
        {
            for (PwcgRole acceptableRole : acceptableRoles)
            {
                if (squadron.isSquadronThisRole(date, acceptableRole))
                {
                    squadronsWithRole.put(squadron.getSquadronId(), squadron);
                }
            }
        }

        return new ArrayList<>(squadronsWithRole.values());
    }

    public static List<Company> reduceToProximityOnCurrentMap(List<Company> squadrons, Date date, Coordinate referencePosition, double radius) throws PWCGException 
    {
        List<Company> squadronsWithinRadius = new ArrayList<>();
        List<Company> squadronsOnMap = reduceToCurrentMap(squadrons, date);
        for (Company squadron : squadronsOnMap)
        {
            Coordinate squadronPosition = squadron.determineCurrentPosition(date);
            double distanceFromReference = MathUtils.calcDist(referencePosition, squadronPosition);
            if (distanceFromReference <= radius)
            {
                squadronsWithinRadius.add(squadron);
            }
        }

        return squadronsWithinRadius;
    }

    public static List<Company> sortByProximityOnCurrentMap(List<Company> squadrons, Date date, Coordinate referencePosition) throws PWCGException 
    {
        Map<Integer, Company> squadronsByProximity = new TreeMap<>();
        List<Company> squadronsOnMap = reduceToCurrentMap(squadrons, date);
        for (Company squadron : squadronsOnMap)
        {
            Coordinate squadronPosition = squadron.determineCurrentPosition(date);
            Double distanceFromReference = MathUtils.calcDist(referencePosition, squadronPosition);
            squadronsByProximity.put(distanceFromReference.intValue(), squadron);
        }

        return new ArrayList<Company>(squadronsByProximity.values());
    }
    
    public static List<Company> reduceToNoAnomalies(List<Company> squadrons, Date date) throws PWCGException
    {       
        List<Company> squadronsWithoutAnomalies = new ArrayList<>();
        for (Company squadron : squadrons)
        {
            if (!squadronIsAnomaly(squadron, date))
            {
                squadronsWithoutAnomalies.add(squadron);
            }
        }
        return squadronsWithoutAnomalies;
    }
    
    private static boolean squadronIsAnomaly(Company squadron, Date date) throws PWCGException
    {
        if (squadron.getSquadronId() == 20115021)
        {
            return true;
        }
        else if (squadron.getSquadronId() == 20111051 && (date.before(DateUtils.getDateYYYYMMDD("19430301"))))
        {
            return true;
        }
        
        return false;
    }
}
