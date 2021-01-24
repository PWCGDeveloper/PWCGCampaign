package pwcg.mission.target.locator;

import java.util.ArrayList;
import java.util.List;

import pwcg.campaign.Campaign;
import pwcg.campaign.context.FrontLinePoint;
import pwcg.campaign.context.FrontLinesForMap;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.group.AirfieldManager;
import pwcg.campaign.group.GroupManager;
import pwcg.campaign.group.airfield.Airfield;
import pwcg.campaign.squadron.Squadron;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.location.PWCGLocation;
import pwcg.core.utils.MathUtils;
import pwcg.core.utils.RandomNumberGenerator;
import pwcg.mission.flight.FlightInformation;

public class TransportReferenceLocationSelector
{
    public static double FAR_ENOUGH_AWAY_FOR_TRANSPORT = 40000.0;
    public static double CLOSE_ENOUGH_FOR_TRANSPORT = 150000.0;
    
    private Campaign campaign;
    private Squadron squadron;
    
    public TransportReferenceLocationSelector(FlightInformation flightInformation)
    {
        this.campaign = flightInformation.getCampaign();
        this.squadron = flightInformation.getSquadron();
    }

    public Coordinate getTargetCoordinate() throws PWCGException    
    {
        int roll = RandomNumberGenerator.getRandom(100);
        if (roll < 50)
        {
            return getTargetCoordinateNearFront();
        }
        else if (roll < 50)
        {
            return getTargetCoordinateForAirfield();
        }
        else
        {
            return getTargetCoordinateForTown();
        }
        
    }

    private Coordinate getTargetCoordinateNearFront() throws PWCGException    
    {
        Coordinate startCoords = squadron.determineCurrentPosition(campaign.getDate());

        FrontLinesForMap frontLinesForMap = PWCGContext.getInstance().getCurrentMap().getFrontLinesForMap(campaign.getDate());
        List<FrontLinePoint> frontPointsWithinRadius = frontLinesForMap.findClosestFrontPositionsForSide(startCoords, CLOSE_ENOUGH_FOR_TRANSPORT, squadron.getCountry().getSide());
        List<FrontLinePoint> frontPointsFarEnoughAway = getPointsFarEnoughAway(startCoords, frontPointsWithinRadius);        
        return choosePoint(frontPointsWithinRadius, frontPointsFarEnoughAway);
    }

    private Coordinate choosePoint(List<FrontLinePoint> frontPointsWithinRadius, List<FrontLinePoint> frontPointsFarEnoughAway)
    {
        List<FrontLinePoint> frontPointsToChooseFrom = frontPointsWithinRadius;
        if (frontPointsFarEnoughAway.size() > 0)
        {
            frontPointsToChooseFrom = frontPointsFarEnoughAway;
        }
        
        int index = RandomNumberGenerator.getRandom(frontPointsToChooseFrom.size());
        return frontPointsToChooseFrom.get(index).getPosition();
    }

    private List<FrontLinePoint> getPointsFarEnoughAway(Coordinate startCoords, List<FrontLinePoint> frontPointsWithinRadius)
    {
        List<FrontLinePoint> frontPointsFarEnoughAway = new ArrayList<>();
        for (FrontLinePoint frontLinePoint : frontPointsWithinRadius)
        {
            double distance = MathUtils.calcDist(startCoords, frontLinePoint.getPosition());
            if (distance > FAR_ENOUGH_AWAY_FOR_TRANSPORT)
            {
                frontPointsFarEnoughAway.add(frontLinePoint);
            }
        }
        
        return frontPointsFarEnoughAway;
    }

    private Coordinate getTargetCoordinateForAirfield() throws PWCGException    
    {
        Coordinate startCoords = squadron.determineCurrentPosition(campaign.getDate());

        AirfieldManager airfieldManager = PWCGContext.getInstance().getCurrentMap().getAirfieldManager();
        List<Airfield> airfieldsWithinRadius = airfieldManager.getAirfieldFinder().getAirfieldsWithinRadiusBySide(startCoords, campaign.getDate(), CLOSE_ENOUGH_FOR_TRANSPORT, squadron.getCountry().getSide());
        List<Airfield> airfieldsFarEnoughAway = getAirfieldsFarEnoughAway(startCoords, airfieldsWithinRadius);        
        return chooseAirfield(airfieldsWithinRadius, airfieldsFarEnoughAway);
    }

    private List<Airfield> getAirfieldsFarEnoughAway(Coordinate startCoords, List<Airfield> airfields)
    {
        List<Airfield> airfieldsFarEnoughAway = new ArrayList<>();
        for (Airfield airfield : airfields)
        {
            double distance = MathUtils.calcDist(startCoords, airfield.getPosition());
            if (distance > FAR_ENOUGH_AWAY_FOR_TRANSPORT)
            {
                airfieldsFarEnoughAway.add(airfield);
            }
        }
        
        return airfieldsFarEnoughAway;
    }

    private Coordinate chooseAirfield(List<Airfield> airfieldsWithinRadius, List<Airfield> airfieldsFarEnoughAway)
    {
        List<Airfield> airfieldsToChooseFrom = airfieldsWithinRadius;
        if (airfieldsFarEnoughAway.size() > 0)
        {
            airfieldsToChooseFrom = airfieldsFarEnoughAway;
        }
        
        int index = RandomNumberGenerator.getRandom(airfieldsToChooseFrom.size());
        return airfieldsToChooseFrom.get(index).getPosition();
    }

    private Coordinate getTargetCoordinateForTown() throws PWCGException    
    {
        Coordinate startCoords = squadron.determineCurrentPosition(campaign.getDate());

        GroupManager groupManager = PWCGContext.getInstance().getCurrentMap().getGroupManager();
        List<PWCGLocation> townsWithinRadius = groupManager.getTownFinder().findAllTownsForSide(squadron.getCountry().getSide(), campaign.getDate());
                
        List<PWCGLocation> townsWithinLimits = getTownsWithinLimits(startCoords, townsWithinRadius);        
        return chooseTown(townsWithinRadius, townsWithinLimits);
    }

    private List<PWCGLocation> getTownsWithinLimits(Coordinate startCoords, List<PWCGLocation> towns)
    {
        List<PWCGLocation> townsWithinLimits = new ArrayList<>();
        for (PWCGLocation town : towns)
        {
            double distance = MathUtils.calcDist(startCoords, town.getPosition());
            if (distance > FAR_ENOUGH_AWAY_FOR_TRANSPORT)
            {
                if (distance < CLOSE_ENOUGH_FOR_TRANSPORT)
                {
                    townsWithinLimits.add(town);
                }
            }
        }
        
        return townsWithinLimits;
    }

    private Coordinate chooseTown(List<PWCGLocation> townsWithinRadius, List<PWCGLocation> townsWithinLimits)
    {
        List<PWCGLocation> townsToChooseFrom = townsWithinRadius;
        if (townsWithinLimits.size() > 0)
        {
            townsToChooseFrom = townsWithinLimits;
        }
        
        int index = RandomNumberGenerator.getRandom(townsToChooseFrom.size());
        return townsToChooseFrom.get(index).getPosition();
    }
}
