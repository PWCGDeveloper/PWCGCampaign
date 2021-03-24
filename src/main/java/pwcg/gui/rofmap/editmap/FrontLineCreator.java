package pwcg.gui.rofmap.editmap;

import java.util.ArrayList;
import java.util.List;

import pwcg.campaign.api.Side;
import pwcg.campaign.context.FrontLinePoint;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGFront;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.utils.MathUtils;

public class FrontLineCreator
{        
    public FrontLineCreator()
    {
    }

    public List<FrontLinePoint> createFrontLines(List<FrontLinePoint> userFrontLines) throws PWCGException
    {
        if (PWCGContext.getInstance().getCurrentMap().getMapIdentifier().getFront() == PWCGFront.WWII_EASTERN_FRONT)
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
        List<FrontLinePoint> axisLines = reduceToAxisLines(userFrontLines);
        List<FrontLinePoint> alliedLines =  createLines(axisLines, 270, FrontLinePoint.ALLIED_FRONT_LINE);
        List<FrontLinePoint> allFrontLines = new ArrayList<>();
        allFrontLines.addAll(alliedLines);
        allFrontLines.addAll(axisLines);
        return allFrontLines;
    }
    
    private List<FrontLinePoint> createAlliedLinesEastFront(List<FrontLinePoint> userFrontLines) throws PWCGException
    {
        List<FrontLinePoint> axisLines = reduceToAxisLines(userFrontLines);
        List<FrontLinePoint> alliedLines = createLines(axisLines, 90, FrontLinePoint.ALLIED_FRONT_LINE);
        List<FrontLinePoint> allFrontLines = new ArrayList<>();
        allFrontLines.addAll(alliedLines);
        allFrontLines.addAll(axisLines);
        return allFrontLines;
    }
    
    private List<FrontLinePoint> reduceToAxisLines(List<FrontLinePoint> userFrontLines) throws PWCGException
    {
        List<FrontLinePoint> axisLines = new ArrayList<>();
        for (FrontLinePoint frontLinePoint : userFrontLines)
        {
            if (frontLinePoint.getSide() == Side.AXIS)
            {
                axisLines.add(frontLinePoint);
            }
        }
        return axisLines;
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
        Coordinate generatedPoint = MathUtils.calcNextCoord(userCreatedPoint, oppositeFrontLineAngle, 3000.0);

        FrontLinePoint frontFrontLinePoint = new FrontLinePoint();
        frontFrontLinePoint.setPosition(generatedPoint);
        frontFrontLinePoint.setName(frontLinePointName);

        return frontFrontLinePoint;
    }
}
