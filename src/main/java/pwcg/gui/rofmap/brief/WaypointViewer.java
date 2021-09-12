package pwcg.gui.rofmap.brief;

import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import javafx.scene.control.Label;

import pwcg.core.exception.PWCGException;
import pwcg.gui.dialogs.PWCGMonitorFonts;
import pwcg.gui.rofmap.brief.model.BriefingMapPoint;

public class WaypointViewer implements IWaypointDetails
{
    private long associatedWaypointID;
    private Label descField;
    private Label altitudeField;
    private Label cruisingSpeedField;
    private Label distanceField;
    private Label headingField;
    private String actionCommandKey = "";

    public WaypointViewer(long associatedWaypointID)
    {
        this.associatedWaypointID = associatedWaypointID;
    }
    
    @Override
    public void initializeWPEdit(BriefingMapPoint previousMapPoint, BriefingMapPoint thisMapPoint) throws PWCGException
    {
        descField = makeLabelField();
        descField.setText(thisMapPoint.getDesc());

        altitudeField = makeLabelField();
        cruisingSpeedField = makeLabelField();
        distanceField = makeLabelField();
        headingField = makeLabelField();

        calculateWPParameters(previousMapPoint, thisMapPoint);
    }

    @Override
    public Label getDescriptionField()
    {
        return descField;
    }

    @Override
    public Label getAltitudeField()
    {
        return altitudeField;
    }

    @Override
    public Label getCruisingSpeedField()
    {
        return cruisingSpeedField;
    }

    @Override
    public Label getDistanceField()
    {
        return distanceField;
    }

    @Override
    public Label getHeadingField()
    {
        return headingField;
    }

    @Override
    public int getAltitudeValue()
    {
        return Integer.parseInt(altitudeField.getText());
    }

    @Override
    public int getCruisingSpeedValue()
    {
        return Integer.parseInt(cruisingSpeedField.getText());
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

        altitudeField.setText("" + briefingMapPoint.getAltitude());
        cruisingSpeedField.setText("" + briefingMapPoint.getCruisingSpeed());
        distanceField.setText(Integer.valueOf(distance / 1000).toString());
        headingField.setText(Integer.valueOf(heading).toString());
    }

    private Label makeLabelField() throws PWCGException
    {
        Font font = PWCGMonitorFonts.getTypewriterFont();

        Label field = new Label();
        field.setOpaque(false);
        field.setFont(font);
        field.setAlignment(Label.RIGHT);
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
