package pwcg.gui.rofmap.editmap;

import java.awt.Point;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import pwcg.campaign.api.ICountry;
import pwcg.campaign.context.Country;
import pwcg.campaign.context.FrontLinePoint;
import pwcg.campaign.context.FrontMapIdentifier;
import pwcg.campaign.factory.CountryFactory;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.utils.MathUtils;
import pwcg.core.utils.PWCGLogger;
import pwcg.core.utils.PositionFinder;
import pwcg.gui.rofmap.MapPanelBase;

public class FrontLineEditor
{
    private static final int LOCATION_INDEX_NOT_FOUND = -1;
    
    private List<FrontLinePoint> userFrontLines = new ArrayList<>();
    private MapPanelBase mapPanel = null;
    
    private FrontLinePoint frontLinePointToEdit = null;
    private FrontMapIdentifier mapIdentifier;
    
    public FrontLineEditor(FrontMapIdentifier mapIdentifier, MapPanelBase mapPanel)
    {
        this.mapPanel = mapPanel;
        this.mapIdentifier = mapIdentifier;
    }

    public void createFrontPoint(MouseEvent e)
    {
        try
        {
            Point point = new Point();
            point.x = e.getX();
            point.y = e.getY();
            Coordinate coordinate = mapPanel.pointToCoordinate(point);

            FrontLinePoint frontFrontLinePoint = makeFrontLinePoint(coordinate, FrontLinePoint.AXIS_FRONT_LINE, Country.GERMANY);

            userFrontLines.add(frontFrontLinePoint);
        }
        catch (Exception exp)
        {
            PWCGLogger.logException(exp);
        }
    }
    
    public void selectFrontPointToMove(MouseEvent e)
    {
        int selectedFrontLineIndex = selectNearestFrontPointToMouseClick(e, 3000.0);
        if (selectedFrontLineIndex != LOCATION_INDEX_NOT_FOUND)
        {
            frontLinePointToEdit = userFrontLines.get(selectedFrontLineIndex);
        }
        else
        {
            frontLinePointToEdit = null;
        }
    }

    public void releaseFrontPointToMove(MouseEvent e)
    {
        try
        {
            if (frontLinePointToEdit != null)
            {
                Point point = new Point();
                point.x = e.getX();
                point.y = e.getY();
                Coordinate releaseCoordinate = mapPanel.pointToCoordinate(point);
                
                frontLinePointToEdit.setPosition(releaseCoordinate);
                
                frontLinePointToEdit = null;
            }
        }
        catch (Exception exp)
        {
            PWCGLogger.logException(exp);
        }
    }

    public void deletePoint(MouseEvent e)
    {
        int selectedFrontLineIndex = selectNearestFrontPointToMouseClick(e, 3000.0);
        if (selectedFrontLineIndex != LOCATION_INDEX_NOT_FOUND)
        {
            userFrontLines.remove(selectedFrontLineIndex);
        }
    }

    public void addFrontPointToLines(MouseEvent e) throws PWCGException
    {
        int selectedFrontPointIndex = selectNearestFrontPointToMouseClick(e, 20000.0);
        if (selectedFrontPointIndex != LOCATION_INDEX_NOT_FOUND)
        {
            addFrontPoint(selectedFrontPointIndex);
        }        
    }   

    private void addFrontPoint(int selectedFrontPointIndex) throws PWCGException
    {
        if (selectedFrontPointIndex == (userFrontLines.size()-1))
        {
            return;
        }
        
        FrontLinePoint selectedFrontPointLeft = userFrontLines.get(selectedFrontPointIndex);
        FrontLinePoint selectedFrontPointRight = userFrontLines.get(selectedFrontPointIndex+1);
        
        double angle = MathUtils.calcAngle(selectedFrontPointRight.getPosition(), selectedFrontPointLeft.getPosition());
        double distance = MathUtils.calcDist(selectedFrontPointRight.getPosition(), selectedFrontPointLeft.getPosition());
        Coordinate frontCoordinate = MathUtils.calcNextCoord( mapIdentifier, selectedFrontPointRight.getPosition(), angle, distance / 2);

        FrontLinePoint newFrontFrontLinePoint = makeFrontLinePoint(frontCoordinate, selectedFrontPointRight.getName(),selectedFrontPointRight.getCountry().getCountry());
        userFrontLines.add(selectedFrontPointIndex+1, newFrontFrontLinePoint);
    }

    public void setFromMap(List<FrontLinePoint> frontLines)
    {
        userFrontLines.clear();
        for (FrontLinePoint location : frontLines)
        {
            userFrontLines.add(location);
        }        
    }

    public List<FrontLinePoint> getUserCreatedFrontLines()
    {
        return userFrontLines;
    }


    public void replaceFrontLines(List<FrontLinePoint> modifiedFrontLinePoints)
    {
        userFrontLines = modifiedFrontLinePoints;
    }

    private int selectNearestFrontPointToMouseClick(MouseEvent e, double radius)
    {
        try
        {
            int closestFrontLinePointIndex = LOCATION_INDEX_NOT_FOUND;

            Point point = new Point();
            point.x = e.getX();
            point.y = e.getY();
            Coordinate clickCoordinate = mapPanel.pointToCoordinate(point);

            double closestDistance = PositionFinder.ABSURDLY_LARGE_DISTANCE;
            for (int i = 0; i < userFrontLines.size(); ++i)
            {
                FrontLinePoint location = userFrontLines.get(i);
                double distance = MathUtils.calcDist(location.getPosition(), clickCoordinate);
                if (distance < radius)
                {
                    if (distance < closestDistance)
                    {
                        closestDistance = distance;
                        closestFrontLinePointIndex = i;
                    }
                }
            }
            
            return closestFrontLinePointIndex;
        }
        catch (Exception exp)
        {
            PWCGLogger.logException(exp);
        }
        
        return LOCATION_INDEX_NOT_FOUND;
    }

    private FrontLinePoint makeFrontLinePoint(Coordinate coordinate, String name, Country country)
    {
        ICountry icountry = CountryFactory.makeCountryByCountry(country);
        FrontLinePoint frontFrontLinePoint = new FrontLinePoint();
        frontFrontLinePoint.setPosition(coordinate);
        frontFrontLinePoint.setName(name);
        frontFrontLinePoint.setCountry(icountry);
        return frontFrontLinePoint;
    }

}
