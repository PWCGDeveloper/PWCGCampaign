package pwcg.gui.rofmap.editmap;

import java.util.ArrayList;
import java.util.List;

import pwcg.campaign.context.FrontLinePoint;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.utils.MathUtils;

public class FrontLineCreator
{        
    public FrontLineCreator()
    {
    }

    public List<FrontLinePoint> createAlliedLinesWestFront(List<FrontLinePoint> userFrontLines) throws PWCGException
    {
        return createLines(userFrontLines, 270, FrontLinePoint.ALLIED_FRONT_LINE);
    }
    
    public List<FrontLinePoint> createAlliedLinesEastFront(List<FrontLinePoint> userFrontLines) throws PWCGException
    {
        return createLines(userFrontLines, 90, FrontLinePoint.ALLIED_FRONT_LINE);
    }

    public List<FrontLinePoint> createAxisLinesWestFront(List<FrontLinePoint> userFrontLines) throws PWCGException
    {
        return createLines(userFrontLines, 90, FrontLinePoint.AXIS_FRONT_LINE);
    }
    
    public List<FrontLinePoint> createAxisLinesEastFront(List<FrontLinePoint> userFrontLines) throws PWCGException
    {
        return createLines(userFrontLines, 270, FrontLinePoint.AXIS_FRONT_LINE);
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
