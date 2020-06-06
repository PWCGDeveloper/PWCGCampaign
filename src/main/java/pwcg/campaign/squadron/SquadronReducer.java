package pwcg.campaign.squadron;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pwcg.campaign.ArmedService;
import pwcg.campaign.Campaign;
import pwcg.campaign.api.IAirfield;
import pwcg.campaign.api.ICountry;
import pwcg.campaign.api.Side;
import pwcg.campaign.plane.Role;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
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
        List<Squadron> squadronsForSide = new ArrayList<Squadron>();
        for (Squadron squadron : squadrons)
        {
            if (side == squadron.determineSide())
            {
                squadronsForSide.add(squadron);
            }
        }
        return squadronsForSide;
    }

    public static List<Squadron> reduceToCurrentMap(List<Squadron> squadrons, Date date) throws PWCGException 
    {
        List<Squadron> squadronsForMap = new ArrayList<Squadron>();
        for (Squadron squadron : squadrons)
        {
            IAirfield field = squadron.determineCurrentAirfieldCurrentMap(date);
            if (field != null)
            {
                squadronsForMap.add(squadron);
            }
        }

        return squadronsForMap;
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

    public static List<Squadron> reduceToRole(List<Squadron> squadrons, List<Role> acceptableRoles, Date date) throws PWCGException 
    {       
        Map<Integer, Squadron> squadronsWithRole = new HashMap<>();
        for(Squadron squadron : squadrons)
        {
            for (Role acceptableRole : acceptableRoles)
            {
                if (squadron.isSquadronThisRole(date, acceptableRole))
                {
                    squadronsWithRole.put(squadron.getSquadronId(), squadron);
                }
            }
        }

        return new ArrayList<>(squadronsWithRole.values());
    }

    public static List<Squadron> reduceToProximityOnCurrentMap(List<Squadron> squadrons, Date date, Coordinate referencePosition, double radius) throws PWCGException 
    {
        List<Squadron> squadronsWithinRadius = new ArrayList<Squadron>();
        for (Squadron squadron : squadrons)
        {
            IAirfield field = squadron.determineCurrentAirfieldCurrentMap(date);
            if (field != null)
            {
                double distanceFromReference = MathUtils.calcDist(referencePosition, field.getPosition());
                if (distanceFromReference <= radius)
                {
                    squadronsWithinRadius.add(squadron);
                }
            }
        }

        return squadronsWithinRadius;
    }
}
