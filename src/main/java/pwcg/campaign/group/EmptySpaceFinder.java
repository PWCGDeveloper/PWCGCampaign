package pwcg.campaign.group;

import java.util.ArrayList;
import java.util.List;

import pwcg.campaign.api.IAirfield;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.group.airfield.HotSpot;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.location.CoordinateBox;
import pwcg.core.utils.MathUtils;

public class EmptySpaceFinder
{
    private static final double CHUNK_SIZE = 5.0;
    private static final double HOT_SPOT_CLEARANCE = 40.0;
    
    private List<HotSpot> hotSpots = new ArrayList<>();
    private List<Block> blocksInArea = new ArrayList<>();
    private List<IAirfield> airfieldsInArea = new ArrayList<>();
    private Coordinate coordinateExaminedNow;
    private CoordinateBox coordinateBox;

    public List<HotSpot> findEmptySpaces (Coordinate center, int boxSize) throws PWCGException
    {
        coordinateBox = CoordinateBox.coordinateBoxFromCenter(center, boxSize);
        
        findBlocksInArea();
        findAirfieldsInArea();
        findEmptySpacesInRequestedArea();
        
        return hotSpots;
    }
    
    private void findEmptySpacesInRequestedArea() throws PWCGException
    {
        coordinateExaminedNow = coordinateBox.getSW().copy();
        coordinateExaminedNow.setXPos(coordinateExaminedNow.getXPos() + CHUNK_SIZE);
        coordinateExaminedNow.setZPos(coordinateExaminedNow.getZPos() + CHUNK_SIZE);
        
        while (coordinateExaminedNow.getXPos() < (coordinateBox.getNE().getXPos() - CHUNK_SIZE))
        {
            while (coordinateExaminedNow.getZPos() < (coordinateBox.getNE().getZPos()- CHUNK_SIZE))
            {
                if (isCoordinateEmpty())
                {
                    HotSpot hotSpot = new HotSpot();
                    hotSpot.setPosition(coordinateExaminedNow.copy());
                    hotSpots.add(hotSpot);
                }
                
                coordinateExaminedNow.setZPos(coordinateExaminedNow.getZPos() + CHUNK_SIZE);
            }
            
            nextRow();
        }
    }

    private void nextRow() throws PWCGException
    {
        coordinateExaminedNow.setZPos(coordinateBox.getSW().getZPos());
        coordinateExaminedNow.setXPos(coordinateExaminedNow.getXPos() + CHUNK_SIZE);
    }

    private boolean isCoordinateEmpty() throws PWCGException
    {
        if (isCoordinateNearBlock())
        {
            return false;
        }
        
        if (isNearRunway())
        {
            return false;
        }
        
        if (isNearHotSpot())
        {
            return false;
        }

        return true;
    }

    private boolean isNearRunway() throws PWCGException
    {
        for (IAirfield airfield : airfieldsInArea)
        {
            if (airfield.isNearRunwayOrTaxiway(coordinateExaminedNow))
                return true;
        }

        return false;
    }

    private boolean isCoordinateNearBlock()
    {
        for (Block block : blocksInArea)
        {
            double distance = MathUtils.calcDist(block.getPosition(), coordinateExaminedNow);
            if (distance < HOT_SPOT_CLEARANCE)
            {
                return true;
            }
        }
        
        return false;
    }

    private boolean isNearHotSpot()
    {
        for (HotSpot hotSpot : hotSpots)
        {
            double distance = MathUtils.calcDist(hotSpot.getPosition(), coordinateExaminedNow);
            if (distance < HOT_SPOT_CLEARANCE)
            {
                return true;
            }
        }
        
        return false;
    }

    private void findBlocksInArea() throws PWCGException
    {
        GroupManager groupManager  = PWCGContext.getInstance().getCurrentMap().getGroupManager();
        blocksInArea = groupManager.getBlockFinder().getBlocksWithinRadius(coordinateBox.getCenter(), coordinateBox.getLongestEdge() + 1000.0);
    }
    

    private void findAirfieldsInArea() throws PWCGException
    {
        AirfieldManager airfieldManager  = PWCGContext.getInstance().getCurrentMap().getAirfieldManager();
        airfieldsInArea = airfieldManager.getAirfieldFinder().getWithinRadius(coordinateBox.getCenter(), coordinateBox.getLongestEdge() + 1000.0);
    }

}
