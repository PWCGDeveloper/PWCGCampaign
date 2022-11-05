package pwcg.campaign.group;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import pwcg.campaign.api.Side;
import pwcg.campaign.context.FrontMapIdentifier;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.utils.PositionFinder;

public class BlockFinder
{
    private List<Block> standaloneBlocks = new ArrayList<Block>();

    public BlockFinder(List<Block> standaloneBlocks)
    {
        this.standaloneBlocks = standaloneBlocks;
    }
    
    public List<Block> getBlocksWithinRadius(Coordinate selectedCoord, double radius) throws PWCGException 
    {
        PositionFinder<Block> positionFinder = new PositionFinder<Block>();
        List<Block> selectedBlocks = positionFinder.findWithinExpandingRadius(standaloneBlocks, selectedCoord, radius, 300000.0);
        return selectedBlocks;
    }

    public Block getBlockWithinRadius(Coordinate selectedCoord, double radius) throws PWCGException 
    {
        PositionFinder<Block> positionFinder = new PositionFinder<Block>();
        Block selectedBlock = positionFinder.selectPositionWithinExpandingRadius(standaloneBlocks, selectedCoord, radius, 300000.0);
        return selectedBlock;
    }

    public List<Block> getBlocksBySideWithinRadius(FrontMapIdentifier mapIdentifier, Side side, Date date, Coordinate referenceLocation, double radius) throws PWCGException 
    {
        PositionFinder<Block> positionFinder = new PositionFinder<Block>();
        List<Block> selectedBlocks = positionFinder.findWithinExpandingRadius(this.getBlocksBySide(mapIdentifier, side, date), referenceLocation, radius, radius*3);
        return selectedBlocks;
    }

    public Block getNearbyBlockPositionBySide(FrontMapIdentifier mapIdentifier, Side side, Date date, Coordinate targetGeneralLocation, double radius) throws PWCGException 
    {
        PositionFinder<Block> positionFinder = new PositionFinder<Block>();
        Block selectedBlock = positionFinder.selectPositionWithinExpandingRadius(getBlocksBySide(mapIdentifier, side, date), targetGeneralLocation, radius, 300000.0);
        return selectedBlock;
    }

    private List<Block> getBlocksBySide(FrontMapIdentifier mapIdentifier, Side side, Date date) throws PWCGException 
    {
        List<Block> blocksForSide = new ArrayList<>();
        for (Block block : this.getAllBlocks())
        {
            if (block.determineCountryOnDate(mapIdentifier, date).getSide() == side)
            {
                blocksForSide.add(block);
            }
        }
        
        return blocksForSide;
    }

    public List<Block> getAllBlocks() 
    {
        List<Block>blocks = new ArrayList<Block>();
        blocks.addAll(standaloneBlocks);
        return blocks;
    }
}
