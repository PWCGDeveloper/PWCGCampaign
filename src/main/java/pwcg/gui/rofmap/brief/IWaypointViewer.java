package pwcg.gui.rofmap.brief;

import javax.swing.JLabel;

import pwcg.core.exception.PWCGException;
import pwcg.gui.rofmap.brief.model.BriefingMapPoint;

public interface IWaypointViewer
{

    void initializeWPEdit(BriefingMapPoint previousMapPoint, BriefingMapPoint thisMapPoint) throws PWCGException;

    JLabel getDescriptionField();

    JLabel getAltitudeTextField();

    JLabel getCruisingSpeedTextField();

    JLabel getDistanceTextField();

    JLabel getHeadingTextField();

    int getAltitudeValue();

    int getCruisingSpeedValue();

    String getActionCommandKey();

    long getAssociatedWaypointID();

}