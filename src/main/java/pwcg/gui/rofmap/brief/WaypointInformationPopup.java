package pwcg.gui.rofmap.brief;

import java.awt.BorderLayout;

import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javax.swing.JPopupMenu;

import pwcg.core.exception.PWCGException;
import pwcg.core.utils.PWCGLogger;
import pwcg.gui.rofmap.brief.model.BriefingMapPoint;

public class WaypointInformationPopup extends JPopupMenu
{
	private static final long serialVersionUID = 1L;

	public WaypointInformationPopup(int selectedMapPointIndex)
    {
        try
        {
            Pane infoPanel = new Pane(new BorderLayout());
            Label waypointInformationText = new Label();
            waypointInformationText.setText(formWaypointInfoText(selectedMapPointIndex));
            infoPanel.add(waypointInformationText, BorderLayout.CENTER);
            this.add(infoPanel);
        }
        catch (Exception e)
        {
            PWCGLogger.logException(e);
        }
    }

    private String formWaypointInfoText(int selectedMapPointIndex) throws PWCGException
	{
        int accumulatedDistance = 0;
        int distance = 0;
        int heading = 0;
        int altitude = 0;
        int cruiseSpeed = 0;
        String description = "";
        
        
        int currentMapIndex = 0;
        BriefingMapPoint previousMapPoint = null;
        for (BriefingMapPoint briefingMapPoint : BriefingContext.getInstance().getBriefingData().getActiveBriefingFlight().getBriefingFlightParameters().getBriefingMapMapPoints())
        {
            if (previousMapPoint != null)
            {
                distance = BriefingMapPointDistanceCalculator.calculateDistanceAsInteger(previousMapPoint.getPosition(), briefingMapPoint.getPosition());
                heading = BriefingMapPointDistanceCalculator.calculateHeading(previousMapPoint.getPosition(), briefingMapPoint.getPosition());
            }
            
            description = briefingMapPoint.getDesc();
            altitude = briefingMapPoint.getAltitude();
            cruiseSpeed = briefingMapPoint.getCruisingSpeed();
            
            accumulatedDistance += distance;

            if (selectedMapPointIndex == currentMapIndex)
            {
                break;
            }
            previousMapPoint = briefingMapPoint;
            ++currentMapIndex;
        }
        
        StringBuffer waypointInformation = new StringBuffer("<html>");
        waypointInformation.append(description + "<br>");
        waypointInformation.append("Altitude  :       " + altitude + " meters<br>");
        waypointInformation.append("Speed  :          " + cruiseSpeed + " kph<br>");
        waypointInformation.append("Heading  :        " + heading + "<br>");
        waypointInformation.append("Distance :        " + (distance/1000) + " km<br>");
        waypointInformation.append("Total    :        " + (accumulatedDistance/1000) + " km<br>");
        waypointInformation.append("</html>");

        return waypointInformation.toString();
    }
}
