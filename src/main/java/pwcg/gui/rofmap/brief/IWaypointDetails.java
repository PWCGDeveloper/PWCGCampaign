package pwcg.gui.rofmap.brief;

import javax.swing.JComponent;

import pwcg.core.exception.PWCGException;
import pwcg.gui.rofmap.brief.model.BriefingMapPoint;

public interface IWaypointDetails
{

    void initializeWPEdit(BriefingMapPoint previousMapPoint, BriefingMapPoint thisMapPoint) throws PWCGException;

    JComponent getDescriptionField();

    JComponent getAltitudeField();

    JComponent getCruisingSpeedField();

    JComponent getDistanceField();

    JComponent getHeadingField();

    int getAltitudeValue();

    int getCruisingSpeedValue();

    String getActionCommandKey();

    long getAssociatedWaypointID();

}