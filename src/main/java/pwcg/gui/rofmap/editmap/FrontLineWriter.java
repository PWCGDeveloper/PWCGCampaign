package pwcg.gui.rofmap.editmap;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import pwcg.campaign.api.Side;
import pwcg.campaign.context.FrontLinePoint;
import pwcg.campaign.context.PWCGContextManager;
import pwcg.campaign.context.PWCGDirectoryManager;
import pwcg.campaign.io.json.LocationIOJson;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.LocationSet;
import pwcg.core.utils.Logger;
import pwcg.core.utils.MathUtils;

public class FrontLineWriter
{
    private List<FrontLinePoint> userFrontLines = new ArrayList<>();
        
    public FrontLineWriter(List<FrontLinePoint> userFrontLines)
    {
        this.userFrontLines = userFrontLines;
    }

    public void finished()
    {
        try
        {
            setFrontOrientation();
            writeFront();
        }
        catch (Exception exp)
        {
            Logger.logException(exp);
        }
    }

    private void setFrontOrientation() throws PWCGException
    {
        for (FrontLinePoint frontLinePoint : userFrontLines)
        {
            double angle = calcFrontAngle(frontLinePoint);
            frontLinePoint.setOrientation(angle);
        }
    }
    
    private double calcFrontAngle(FrontLinePoint referenceFrontLinePoint) throws PWCGException
    {
        double angle = 0.0;
        FrontLinePoint closestEnemyFrontLinePoint = findClosestEnemyPoint(referenceFrontLinePoint);
        if (closestEnemyFrontLinePoint != null)
        {
            angle = MathUtils.calcAngle(referenceFrontLinePoint.getPosition(), closestEnemyFrontLinePoint.getPosition());
        }
        else
        {
            angle = 90;
            if (referenceFrontLinePoint.getCountry().getSide() == Side.ALLIED)
            {
                angle = 270;
            }
        }
        
        return angle;
    }
    
    private FrontLinePoint findClosestEnemyPoint(FrontLinePoint referenceFrontLinePoint) throws PWCGException
    {
        FrontLinePoint closestEnemyFrontLinePoint = null;
        for (FrontLinePoint frontLinePoint : userFrontLines)
        {
            if (frontLinePoint.getCountry().getSide() != referenceFrontLinePoint.getCountry().getSide())
            {
                if (closestEnemyFrontLinePoint == null)
                {
                    closestEnemyFrontLinePoint = frontLinePoint;
                }
                else
                {
                    double closestDistance = MathUtils.calcDist(referenceFrontLinePoint.getPosition(), closestEnemyFrontLinePoint.getPosition());
                    double thisDistance = MathUtils.calcDist(referenceFrontLinePoint.getPosition(), frontLinePoint.getPosition());
                    if (thisDistance < closestDistance)
                    {
                        closestEnemyFrontLinePoint = frontLinePoint;
                    }
                }
            }
        }
        
        return closestEnemyFrontLinePoint;
    }

    private void writeFront() throws PWCGException, IOException
    {
        String outputPath = PWCGDirectoryManager.getInstance().getPwcgInputDir() + PWCGContextManager.getInstance().getCurrentMap().getMapName() + "\\";      
        
        LocationSet locationSet = new LocationSet("FrontLines");
        frontLinePointToLocationSet(locationSet, userFrontLines);
        
        LocationIOJson.writeJson(outputPath, "FrontLines", locationSet);
    }


    public LocationSet frontLinePointToLocationSet(LocationSet locationSet, List<FrontLinePoint> frontLinePoints)
    {        
        for (FrontLinePoint frontLinePoint : frontLinePoints)
        {
        	locationSet.getLocations().add(frontLinePoint.getLocation());
        }

        return locationSet;
    }
}
