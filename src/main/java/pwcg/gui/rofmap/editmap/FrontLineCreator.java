package pwcg.gui.rofmap.editmap;

import java.util.ArrayList;
import java.util.List;

import pwcg.campaign.api.Side;
import pwcg.campaign.context.FrontLinePoint;
import pwcg.campaign.context.FrontMapIdentifier;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGFront;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.utils.MathUtils;

public class FrontLineCreator
{        
    private FrontMapIdentifier mapIdentifier;

    public FrontLineCreator(FrontMapIdentifier mapIdentifier)
    {
        this.mapIdentifier = mapIdentifier;
    }

    public List<FrontLinePoint> createFrontLines(List<FrontLinePoint> userFrontLines) throws PWCGException
    {
        if (PWCGContext.getInstance().getMap(mapIdentifier).getMapIdentifier().getFront() == PWCGFront.WWII_EASTERN_FRONT)
        {
            return createAlliedLinesEastFront(userFrontLines);
        }
        else
        {
            return createAlliedLinesWestFront(userFrontLines);
        }
    }
    
    private List<FrontLinePoint> createAlliedLinesWestFront(List<FrontLinePoint> userFrontLines) throws PWCGException
    {
        List<FrontLinePoint> axisLines = reduceLinesToSide(userFrontLines, Side.ALLIED);
        List<FrontLinePoint> alliedLines = createLines(axisLines, 90, FrontLinePoint.AXIS_FRONT_LINE);
        List<FrontLinePoint> allFrontLines = new ArrayList<>();
        allFrontLines.addAll(alliedLines);
        allFrontLines.addAll(axisLines);
        return allFrontLines;
    }
    
    private List<FrontLinePoint> createAlliedLinesEastFront(List<FrontLinePoint> userFrontLines) throws PWCGException
    {
        List<FrontLinePoint> axisLines = reduceLinesToSide(userFrontLines, Side.ALLIED);
        List<FrontLinePoint> alliedLines = createLines(axisLines, 270, FrontLinePoint.AXIS_FRONT_LINE);
        List<FrontLinePoint> allFrontLines = new ArrayList<>();
        allFrontLines.addAll(alliedLines);
        allFrontLines.addAll(axisLines);
        return allFrontLines;
    }

    private List<FrontLinePoint> reduceLinesToSide(List<FrontLinePoint> userFrontLines, Side side) throws PWCGException
    {
        List<FrontLinePoint> frontLinesForSide = new ArrayList<>();
        for (FrontLinePoint frontLinePoint : userFrontLines)
        {
            if (frontLinePoint.getSide() == side)
            {
                frontLinesForSide.add(frontLinePoint);
            }
        }
        return frontLinesForSide;
    }

    private List<FrontLinePoint> createLines(List<FrontLinePoint> userFrontLines, int oppositeFrontLineAngle, String frontLinePointName) throws PWCGException
    {        
        List<FrontLinePoint> oppositeFrontLines = new ArrayList<>();
        for (int i = 0; i < userFrontLines.size(); ++i)
        {
            Coordinate userCreatedPoint = userFrontLines.get(i).getPosition();
            FrontLinePoint oppositeFrontFrontLinePoint = createOppositePoint(userCreatedPoint, oppositeFrontLineAngle, frontLinePointName);
            oppositeFrontLines.add(oppositeFrontFrontLinePoint);
        }
        
        return oppositeFrontLines;
    }

    private FrontLinePoint createOppositePoint(Coordinate userCreatedPoint, int oppositeFrontLineAngle, String frontLinePointName) throws PWCGException
    {
        Coordinate generatedPoint = MathUtils.calcNextCoord(mapIdentifier, userCreatedPoint, oppositeFrontLineAngle, 3000.0);

        FrontLinePoint frontFrontLinePoint = new FrontLinePoint();
        frontFrontLinePoint.setPosition(generatedPoint);
        frontFrontLinePoint.setName(frontLinePointName);

        return frontFrontLinePoint;
    }
}
