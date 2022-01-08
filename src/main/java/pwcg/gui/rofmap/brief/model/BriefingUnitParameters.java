package pwcg.gui.rofmap.brief.model;

import java.util.ArrayList;
import java.util.List;

import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.utils.MathUtils;

public class BriefingUnitParameters
{
    private List<BriefingMapPoint> briefingMapPoints = new ArrayList<>();
    private int selectedMapPointIndex = -1;
    private int actionMapPointIndex = -1;
    
    public List<BriefingMapPoint> getBriefingMapMapPoints()
    {
        return briefingMapPoints;
    }

    public void addBriefingMapMapPoints(BriefingMapPoint briefingMapPoint)
    {
        this.briefingMapPoints.add(briefingMapPoint);
    }

    public int getSelectedMapPointIndex()
    {
        return selectedMapPointIndex;
    }

    public void setSelectedMapPointIndex(int selectedMapPointIndex)
    {
        this.selectedMapPointIndex = selectedMapPointIndex;
    }

    public void addBriefingMapMapPointsAtPosition() throws PWCGException
    {
        if (actionMapPointIndex >= 0)
        {
            if (briefingMapPoints.size() > (actionMapPointIndex+1))
            {
                BriefingMapPoint previousBriefingMapPoint = briefingMapPoints.get(actionMapPointIndex);
                if (previousBriefingMapPoint.isEditable())
                {
                    BriefingMapPoint nextBriefingMapPoint = briefingMapPoints.get(actionMapPointIndex+1);
                
                    BriefingMapPoint briefingMapPointToAdd = previousBriefingMapPoint.copy();
                    double distance = MathUtils.calcDist(previousBriefingMapPoint.getPosition(), nextBriefingMapPoint.getPosition());
                    double heading = MathUtils.calcAngle(previousBriefingMapPoint.getPosition(), nextBriefingMapPoint.getPosition());
                    
                    Coordinate newMapPointPosition = MathUtils.calcNextCoord(previousBriefingMapPoint.getPosition(), heading, (distance / 2));
                    newMapPointPosition.setYPos(previousBriefingMapPoint.getAltitude());
                    briefingMapPointToAdd.setPosition(newMapPointPosition);
                    this.briefingMapPoints.add(actionMapPointIndex+1, briefingMapPointToAdd);
                }
            }
        }
    }

    public void removeBriefingMapMapPointsAtPosition()
    {
        if (actionMapPointIndex >= 0)
        {
            BriefingMapPoint briefingMapPointToRemove = briefingMapPoints.get(actionMapPointIndex);
            if (briefingMapPointToRemove.isEditable())
            {
                this.briefingMapPoints.remove(actionMapPointIndex);
            }
        }
    }

    public void updatePosition(Coordinate updatedPosition)
    {
        if (selectedMapPointIndex >= 0)
        {
            BriefingMapPoint briefingMapPointToEdit = briefingMapPoints.get(selectedMapPointIndex);
            briefingMapPointToEdit.setPosition(updatedPosition);
        }
    }

    public BriefingMapPoint getSelectedMapPoint()
    {
        if (selectedMapPointIndex >= 0)
        {
            return briefingMapPoints.get(selectedMapPointIndex);
        }
        return null;
    }

    public BriefingMapPoint getSelectedActionMapPoint()
    {
        if (actionMapPointIndex >= 0)
        {
            return briefingMapPoints.get(actionMapPointIndex);
        }
        return null;
    }

    public void setActionMapPointIndex(int selectedMapPointIndex)
    {
        actionMapPointIndex = selectedMapPointIndex;
    }

    public int getActionMapPointIndex()
    {
        return actionMapPointIndex;
    }
}
