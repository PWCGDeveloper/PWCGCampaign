package pwcg.gui.rofmap.brief;

import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import javax.swing.JComponent;
import javafx.scene.control.Label;
import javax.swing.JTextField;

import pwcg.core.exception.PWCGException;
import pwcg.gui.dialogs.PWCGMonitorFonts;
import pwcg.gui.rofmap.brief.model.BriefingMapPoint;

public class WaypointEditor implements IWaypointDetails
{
    private long associatedWaypointID;
    private Label descTextField;
    private JTextField altitudeTextField;
    private JTextField cruisingSpeedTextField;
    private Label distanceTextField;
    private Label headingTextField;
    private String actionCommandKey = "";

    public WaypointEditor(long associatedWaypointID)
    {
        this.associatedWaypointID = associatedWaypointID;
    }
    
    public void initializeWPEdit(BriefingMapPoint previousMapPoint, BriefingMapPoint thisMapPoint) throws PWCGException
    {
        descTextField = makeLabelField();
        descTextField.setText(thisMapPoint.getDesc());

        altitudeTextField = makeTextField();
        cruisingSpeedTextField = makeTextField();
        distanceTextField = makeLabelField();
        headingTextField = makeLabelField();
        
        if (thisMapPoint.isEditable())
        {
            altitudeTextField.setEditable(true);
        }
        else
        {
            altitudeTextField.setEditable(false);
            altitudeTextField.setBorder(javax.swing.BorderFactory.createEmptyBorder());
        }

        calculateWPParameters(previousMapPoint, thisMapPoint);
    }

    public Label getDescriptionField()
    {
        return descTextField;
    }

    public JTextField getAltitudeField()
    {
        return altitudeTextField;
    }

    public JTextField getCruisingSpeedField()
    {
        return cruisingSpeedTextField;
    }

    public Label getDistanceField()
    {
        return distanceTextField;
    }

    public Label getHeadingField()
    {
        return headingTextField;
    }

    public int getAltitudeValue()
    {
        return Integer.parseInt(altitudeTextField.getText());
    }

    public int getCruisingSpeedValue()
    {
        return Integer.parseInt(cruisingSpeedTextField.getText());
    }

    public void setEnabled(boolean enabled)
    {
        altitudeTextField.setEditable(enabled);
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
    
    public void refreshTextFields()
    {
        refreshTextField(altitudeTextField);
        refreshTextField(cruisingSpeedTextField);
        refreshTextField(distanceTextField);
        refreshTextField(headingTextField);
    }

    private void refreshTextField(JComponent textField)
    {
        if (textField.getGraphics() != null)
        {
            textField.update(textField.getGraphics());
        }
    }

    private JTextField makeTextField() throws PWCGException
    {
        Font font = PWCGMonitorFonts.getTypewriterFont();

        JTextField field = new JTextField();
        field.setOpaque(false);
        field.setFont(font);
        field.setAlignment(JTextField.RIGHT);
        field.setForeground(Color.BLACK);

        return field;
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

    public String getActionCommandKey()
    {
        return actionCommandKey;
    }

    public long getAssociatedWaypointID()
    {
        return associatedWaypointID;
    }
}
