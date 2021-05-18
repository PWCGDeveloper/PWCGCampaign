package pwcg.gui.rofmap.brief;

import java.awt.Color;
import java.awt.Font;

import javax.swing.JLabel;

import pwcg.core.exception.PWCGException;
import pwcg.gui.dialogs.PWCGMonitorFonts;
import pwcg.gui.rofmap.brief.model.BriefingMapPoint;

public class WaypointViewer implements IWaypointViewer
{
    private long associatedWaypointID;
    private JLabel descTextField;
    private JLabel altitudeTextField;
    private JLabel cruisingSpeedTextField;
    private JLabel distanceTextField;
    private JLabel headingTextField;
    private String actionCommandKey = "";

    public WaypointViewer(long associatedWaypointID)
    {
        this.associatedWaypointID = associatedWaypointID;
    }
    
    @Override
    public void initializeWPEdit(BriefingMapPoint previousMapPoint, BriefingMapPoint thisMapPoint) throws PWCGException
    {
        descTextField = makeLabelField();
        descTextField.setText(thisMapPoint.getDesc());

        altitudeTextField = makeLabelField();
        cruisingSpeedTextField = makeLabelField();
        distanceTextField = makeLabelField();
        headingTextField = makeLabelField();

        calculateWPParameters(previousMapPoint, thisMapPoint);
    }

    @Override
    public JLabel getDescriptionField()
    {
        return descTextField;
    }

    @Override
    public JLabel getAltitudeTextField()
    {
        return altitudeTextField;
    }

    @Override
    public JLabel getCruisingSpeedTextField()
    {
        return cruisingSpeedTextField;
    }

    @Override
    public JLabel getDistanceTextField()
    {
        return distanceTextField;
    }

    @Override
    public JLabel getHeadingTextField()
    {
        return headingTextField;
    }

    @Override
    public int getAltitudeValue()
    {
        return Integer.parseInt(altitudeTextField.getText());
    }

    @Override
    public int getCruisingSpeedValue()
    {
        return Integer.parseInt(cruisingSpeedTextField.getText());
    }

    private void calculateWPParameters(BriefingMapPoint previousMapPoint, BriefingMapPoint briefingMapPoint) throws PWCGException
    {
        int distance = 0;
        int heading = 0;

        if (previousMapPoint != null)
        {
            distance = BriefingMapPointDistanceCalculator.calculateDistanceAsInteger(previousMapPoint.getPosition(), briefingMapPoint.getPosition());
            heading = BriefingMapPointDistanceCalculator.calculateHeading(previousMapPoint.getPosition(), briefingMapPoint.getPosition());
        }

        altitudeTextField.setText("" + briefingMapPoint.getAltitude());
        cruisingSpeedTextField.setText("" + briefingMapPoint.getCruisingSpeed());
        distanceTextField.setText(Integer.valueOf(distance / 1000).toString());
        headingTextField.setText(Integer.valueOf(heading).toString());
    }

    private JLabel makeLabelField() throws PWCGException
    {
        Font font = PWCGMonitorFonts.getTypewriterFont();

        JLabel field = new JLabel();
        field.setOpaque(false);
        field.setFont(font);
        field.setHorizontalAlignment(JLabel.RIGHT);
        field.setForeground(Color.BLACK);

        return field;
    }

    @Override
    public String getActionCommandKey()
    {
        return actionCommandKey;
    }

    @Override
    public long getAssociatedWaypointID()
    {
        return associatedWaypointID;
    }
}
