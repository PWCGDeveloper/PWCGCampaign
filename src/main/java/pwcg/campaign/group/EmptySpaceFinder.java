package pwcg.campaign.group;

import java.util.ArrayList;
import java.util.List;

import pwcg.campaign.api.IAirfield;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.group.airfield.hotspot.HotSpot;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.location.CoordinateBox;
import pwcg.core.utils.MathUtils;
import pwcg.mission.Mission;

public class EmptySpaceFinder
{
    private static final double HOT_SPOT_CLEARANCE = 40.0;
    
    private Mission mission;
    
    private List<HotSpot> hotSpots = new ArrayList<>();
    private List<Block> blocksInArea = new ArrayList<>();
    private List<IAirfield> airfieldsInArea = new ArrayList<>();
    private Coordinate coordinateExaminedNow;
    private CoordinateBox coordinateBox;
    private List<Coordinate> boundary;
    private int targetNumber;
    
    public EmptySpaceFinder(Mission mission)
    {
        this.mission = mission;
    }

    public List<HotSpot> findEmptySpaces (List<Coordinate> boundary, int targetNumber) throws PWCGException
    {
        coordinateBox = CoordinateBox.coordinateBoxFromCoordinateList(boundary);
        this.boundary = boundary;
        this.targetNumber = targetNumber;
        
        findBlocksInArea();
        findAirfieldsInArea();
        findEmptySpacesInRequestedArea();
        
        return hotSpots;
    }
    
    private void findEmptySpacesInRequestedArea() throws PWCGException
    {
        for (int i = 0; i < targetNumber * 3 && hotSpots.size() < targetNumber; i++)
        {
            coordinateExaminedNow = coordinateBox.chooseCoordinateWithinBox();

            if (MathUtils.pointInsidePolygon(coordinateExaminedNow, boundary) && isCoordinateEmpty())
            {
                HotSpot hotSpot = new HotSpot();
                hotSpot.setPosition(coordinateExaminedNow.copy());
                hotSpots.add(hotSpot);
            }
        }
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
            if (airfield.isNearRunwayOrTaxiway(mission, coordinateExaminedNow))
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
