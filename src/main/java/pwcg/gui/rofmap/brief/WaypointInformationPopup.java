package pwcg.gui.rofmap.brief;

import java.awt.BorderLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
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
            JPanel infoPanel = new JPanel(new BorderLayout());
            JLabel waypointInformationText = new JLabel();
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
        waypointInformation.append("Heading  :        " + heading + "<br>");
        waypointInformation.append("Distance :        " + (distance/1000) + " km<br>");
        waypointInformation.append("Total    :        " + (accumulatedDistance/1000) + " km<br>");
        waypointInformation.append("</html>");

        return waypointInformation.toString();
    }
}
