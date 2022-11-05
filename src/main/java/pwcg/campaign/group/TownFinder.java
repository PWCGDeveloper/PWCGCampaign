package pwcg.campaign.group;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import pwcg.campaign.api.Side;
import pwcg.campaign.context.FrontMapIdentifier;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.location.LocationSet;
import pwcg.core.location.PWCGLocation;
import pwcg.core.utils.PositionFinder;

public class TownFinder
{
    private LocationSet townLocations;

    public TownFinder(LocationSet townLocations)
    {
        this.townLocations = townLocations;
    }
    
    public PWCGLocation findClosestTown (Coordinate referenceLocation) throws PWCGException 
    {
        PositionFinder<PWCGLocation> positionFinder = new PositionFinder<PWCGLocation>();
        PWCGLocation closestTown = positionFinder.selectClosestPosition(townLocations.getLocations(), referenceLocation);
        return closestTown;
    }

    public PWCGLocation findClosestTownForSide (FrontMapIdentifier mapIdentifier, Side side, Date date, Coordinate referenceLocation) throws PWCGException 
    {
        PositionFinder<PWCGLocation> positionFinder = new PositionFinder<PWCGLocation>();
        PWCGLocation closestTown = positionFinder.selectClosestPosition(findAllTownsForSide(mapIdentifier, side, date), referenceLocation);
        return closestTown;
    }

    public PWCGLocation findTownForSideWithinRadius(FrontMapIdentifier mapIdentifier, Side side, Date date, Coordinate referenceLocation, double radius) throws PWCGException 
    {
        PositionFinder<PWCGLocation> positionFinder = new PositionFinder<PWCGLocation>();
        List<PWCGLocation> townsForSide = findAllTownsForSide(mapIdentifier, side, date);
        PWCGLocation selectedTown = positionFinder.selectPositionWithinExpandingRadius(
                townsForSide, 
                referenceLocation, 
                radius, 
                radius * 2);
        return selectedTown;
    }

    public List<PWCGLocation> findTownsForSideWithinRadius (FrontMapIdentifier mapIdentifier, Side side, Date date, Coordinate referenceLocation, double radius) throws PWCGException 
    {
        PositionFinder<PWCGLocation> positionFinder = new PositionFinder<PWCGLocation>();
        List<PWCGLocation> closestTowns = positionFinder.findWithinExpandingRadius(findAllTownsForSide(mapIdentifier, side, date), referenceLocation, radius, radius * 3);
        return closestTowns;
    }

    public List<PWCGLocation> findAllTownsForSide (FrontMapIdentifier mapIdentifier, Side side, Date date) throws PWCGException 
    {       
        List<PWCGLocation> townsForSide = new ArrayList<>();
        for (PWCGLocation town : townLocations.getLocations())
        {
            if (town.getCountry(mapIdentifier, date).getSide() == side)
            {
                townsForSide.add(town);
            }
        }
        return townsForSide;
    }

    public LocationSet getTownLocations()
    {
        return townLocations;
    }
}
