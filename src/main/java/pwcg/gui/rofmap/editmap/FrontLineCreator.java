package pwcg.gui.rofmap.editmap;

import java.util.ArrayList;
import java.util.List;

import pwcg.campaign.context.FrontLinePoint;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.utils.MathUtils;

public class FrontLineCreator
{
    private int oppositeFrontLineAngle = 270;
        
    public FrontLineCreator()
    {
    }

    public List<FrontLinePoint> createAxisLines(List<FrontLinePoint> userFrontLines) throws PWCGException
    {        
        List<FrontLinePoint> oppositeFrontLines = new ArrayList<>();
        for (int i = 0; i < userFrontLines.size(); ++i)
        {
            Coordinate userCreatedPoint = userFrontLines.get(i).getPosition();
            FrontLinePoint oppositeFrontFrontLinePoint = createOppositePoint( userCreatedPoint);
            oppositeFrontLines.add(oppositeFrontFrontLinePoint);
        }
        
        return oppositeFrontLines;
    }

    private FrontLinePoint createOppositePoint(Coordinate userCreatedPoint) throws PWCGException
    {
        Coordinate generatedPoint = MathUtils.calcNextCoord(userCreatedPoint, oppositeFrontLineAngle, 3000.0);

        FrontLinePoint frontFrontLinePoint = new FrontLinePoint();
        frontFrontLinePoint.setPosition(generatedPoint);
        frontFrontLinePoint.setName(FrontLinePoint.AXIS_FRONT_LINE);

        return frontFrontLinePoint;
    }
}
