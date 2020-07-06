package pwcg.gui.rofmap.brief.model;

import java.util.ArrayList;
import java.util.List;

import pwcg.core.location.Coordinate;

public class BriefingFlightParameters
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

    public void addBriefingMapMapPointsAtPosition()
    {
        // this.briefingMapPoints.add(index, briefingMapPoint);
    }

    public void removeBriefingMapMapPointsAtPosition()
    {
        if (actionMapPointIndex >= 0)
        {
            BriefingMapPoint briefingMapPointToEdit = briefingMapPoints.get(actionMapPointIndex);
            if (briefingMapPointToEdit.isEditable())
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

    public void setActionMapPointIndex(int selectedMapPointIndex)
    {
        actionMapPointIndex = selectedMapPointIndex;
    }

    public int getActionMapPointIndex()
    {
        return actionMapPointIndex;
    }
}
