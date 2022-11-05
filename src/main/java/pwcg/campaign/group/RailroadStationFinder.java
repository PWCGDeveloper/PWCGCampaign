package pwcg.campaign.group;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import pwcg.campaign.api.ICountry;
import pwcg.campaign.api.Side;
import pwcg.campaign.context.FrontMapIdentifier;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.utils.PositionFinder;

public class RailroadStationFinder
{
    private List<Block> railroadStations = new ArrayList<Block>();

    public RailroadStationFinder(List<Block> standaloneBlocks)
    {
        this.railroadStations = standaloneBlocks;
    }
    
    public Block getClosestTrainPosition (Coordinate referenceLocation) throws PWCGException 
    {
        PositionFinder<Block> positionFinder = new PositionFinder<Block>();
        Block closestStation = positionFinder.selectClosestPosition(railroadStations, referenceLocation);
        return closestStation;
    }
    
    public Block getClosestTrainPositionBySide (FrontMapIdentifier mapIdentifier, Side side, Date date, Coordinate referenceLocation) throws PWCGException 
    {
        PositionFinder<Block> positionFinder = new PositionFinder<Block>();
        Block closestStation = positionFinder.selectClosestPosition(getTrainPositionsBySide(mapIdentifier, side, date), referenceLocation);
        return closestStation;
    }

    public Block getDestinationTrainPosition(FrontMapIdentifier mapIdentifier, Coordinate startingPosition, ICountry country, Date date) throws PWCGException
    {
        PositionFinder<Block> positionFinder = new PositionFinder<Block>();
        List<Block> trainStationsForSide = getTrainPositionsBySide(mapIdentifier, country.getSide(), date);
        Block destinationRailroadStation = positionFinder.selectDestinationPosition(trainStationsForSide, startingPosition);
        return destinationRailroadStation;
    }

    public List<Block> getTrainPositionWithinRadiusBySide(FrontMapIdentifier mapIdentifier, Side side, Date date, Coordinate targetGeneralLocation, double radius) throws PWCGException
    {
        PositionFinder<Block> positionFinder = new PositionFinder<Block>();
        List<Block> selectedStations = positionFinder.findWithinExpandingRadius(getTrainPositionsBySide(mapIdentifier, side, date), targetGeneralLocation, radius, radius);
        return selectedStations;
    }
    
    private List<Block> getTrainPositionsBySide(FrontMapIdentifier mapIdentifier, Side side, Date date) throws PWCGException 
    {
        List<Block>selectedRailroadStations = new ArrayList<Block>();
        for (Block railroadStations : railroadStations)
        {
            if (railroadStations.determineCountryOnDate(mapIdentifier, date).getSide() == side)
            {
                selectedRailroadStations.add(railroadStations);
            }
        }
                
        return selectedRailroadStations;
    }
 }
