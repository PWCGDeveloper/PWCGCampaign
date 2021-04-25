package pwcg.campaign.group;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import pwcg.campaign.api.ICountry;
import pwcg.campaign.api.Side;
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
    
    public Block getClosestTrainPositionBySide (Side side, Date date, Coordinate referenceLocation) throws PWCGException 
    {
        PositionFinder<Block> positionFinder = new PositionFinder<Block>();
        Block closestStation = positionFinder.selectClosestPosition(getTrainPositionsBySide(side, date), referenceLocation);
        return closestStation;
    }
    
    public Block getNearbyTrainPosition(Side side, Date date, Coordinate referenceLocation, double radius) throws PWCGException
    {
        PositionFinder<Block> positionFinder = new PositionFinder<Block>();
        List<Block> trainStationsForSide = getTrainPositionsBySide(side, date);
        Block selectedStation = positionFinder.selectPositionWithinExpandingRadius(trainStationsForSide, referenceLocation, radius, radius );
        return selectedStation;
    }

    public Block getDestinationTrainPosition(Coordinate startingPosition, ICountry country, Date date) throws PWCGException
    {
        PositionFinder<Block> positionFinder = new PositionFinder<Block>();
        List<Block> trainStationsForSide = getTrainPositionsBySide(country.getSide(), date);
        Block destinationRailroadStation = positionFinder.selectDestinationPosition(trainStationsForSide, startingPosition);
        return destinationRailroadStation;
    }

    public List<Block> getTrainPositionWithinRadiusBySide(Side side, Date date, Coordinate targetGeneralLocation, double radius) throws PWCGException
    {
        PositionFinder<Block> positionFinder = new PositionFinder<Block>();
        List<Block> selectedStations = positionFinder.findWithinExpandingRadius(getTrainPositionsBySide(side, date), targetGeneralLocation, radius, radius);
        return selectedStations;
    }
    
    private List<Block> getTrainPositionsBySide(Side side, Date date) throws PWCGException 
    {
        List<Block>selectedRailroadStations = new ArrayList<Block>();
        for (Block railroadStations : railroadStations)
        {
            if (railroadStations.determineCountryOnDate(date).getSide() == side)
            {
                selectedRailroadStations.add(railroadStations);
            }
        }
                
        return selectedRailroadStations;
    }
 }
