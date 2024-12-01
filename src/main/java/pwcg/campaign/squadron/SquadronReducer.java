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
import pwcg.campaign.context.FrontMapIdentifier;
import pwcg.campaign.group.airfield.Airfield;
import pwcg.campaign.plane.PwcgRole;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.utils.DateUtils;
import pwcg.core.utils.MathUtils;

public class SquadronReducer
{

    public static List<Squadron> reduceToAIOnly(List<Squadron> selectedSquadrons, Campaign campaign) throws PWCGException
    {
        List<Squadron> selectedSquadronsNoPlayer = new ArrayList<>();
        for (Squadron squadron : selectedSquadrons)
        {
            if (!campaign.getPersonnelManager().squadronHasActivePlayers(squadron.getSquadronId()))
            {
                selectedSquadronsNoPlayer.add(squadron);
            }
        }
        return selectedSquadronsNoPlayer;
    }
    
    public static List<Squadron> reduceToSide(List<Squadron> squadrons, Side side) throws PWCGException
    {       
        List<Squadron> squadronsForSide = new ArrayList<>();
        for (Squadron squadron : squadrons)
        {
            if (side == squadron.determineSide())
            {
                squadronsForSide.add(squadron);
            }
        }
        return squadronsForSide;
    }

    public static List<Squadron> reduceToCurrentMap(FrontMapIdentifier mapIdentifier, List<Squadron> squadrons, Date date) throws PWCGException 
    {
        Map<Integer, Squadron> squadronsForMap = new TreeMap<>();
        for (Squadron squadron : squadrons)
        {
            Airfield field = squadron.determineCurrentAirfieldCurrentMap(mapIdentifier, date);
            if (field != null)
            {
                squadronsForMap.put(squadron.getSquadronId(), squadron);
            }
        }

        return new ArrayList<Squadron>(squadronsForMap.values());
    }
    
    public static List<Squadron> reduceToService(List<Squadron> squadrons, Date date, ArmedService service) throws PWCGException 
    {
        List<Squadron> squadronsForService = new ArrayList<>();
        for (Squadron squadron : squadrons)
        {
            if (squadron.determineServiceForSquadron(date).getServiceId() == service.getServiceId())
            {
                squadronsForService.add(squadron);
            }
        }
        return squadronsForService;
    }
    

    public static List<Squadron> reduceToCountry(List<Squadron> squadrons, Date date, ICountry country) throws PWCGException
    {
        List<Squadron> squadronsForCountry = new ArrayList<>();
        for (Squadron squadron : squadrons)
        {
            ICountry squadCountry = squadron.determineSquadronCountry(date);
            if (squadCountry.equals(country))
            {
                squadronsForCountry.add(squadron);
            }
        }
        return squadronsForCountry;
    }

    public static List<Squadron> reduceToRole(List<Squadron> squadrons, List<PwcgRole> acceptableRoles, Date date) throws PWCGException 
    {       
        Map<Integer, Squadron> squadronsWithRole = new HashMap<>();
        for(Squadron squadron : squadrons)
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

    public static List<Squadron> reduceToProximityOnCurrentMap(FrontMapIdentifier mapIdentifier, List<Squadron> squadrons, Date date, Coordinate referencePosition, double radius) throws PWCGException 
    {
        List<Squadron> squadronsWithinRadius = new ArrayList<>();
        List<Squadron> squadronsOnMap = reduceToCurrentMap(mapIdentifier, squadrons, date);
        for (Squadron squadron : squadronsOnMap)
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

    public static List<Squadron> sortByProximityOnCurrentMap(FrontMapIdentifier mapIdentifier, List<Squadron> squadrons, Date date, Coordinate referencePosition) throws PWCGException 
    {
        Map<Integer, Squadron> squadronsByProximity = new TreeMap<>();
        List<Squadron> squadronsOnMap = reduceToCurrentMap(mapIdentifier, squadrons, date);
        for (Squadron squadron : squadronsOnMap)
        {
            Coordinate squadronPosition = squadron.determineCurrentPosition(date);
            Double distanceFromReference = MathUtils.calcDist(referencePosition, squadronPosition);
            squadronsByProximity.put(distanceFromReference.intValue(), squadron);
        }

        return new ArrayList<Squadron>(squadronsByProximity.values());
    }
    
    public static List<Squadron> reduceToNoAnomalies(List<Squadron> squadrons, Date date) throws PWCGException
    {       
        List<Squadron> squadronsWithoutAnomalies = new ArrayList<>();
        for (Squadron squadron : squadrons)
        {
            if (!squadronIsAnomaly(squadron, date))
            {
                squadronsWithoutAnomalies.add(squadron);
            }
        }
        return squadronsWithoutAnomalies;
    }
    
    private static boolean squadronIsAnomaly(Squadron squadron, Date date) throws PWCGException
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
