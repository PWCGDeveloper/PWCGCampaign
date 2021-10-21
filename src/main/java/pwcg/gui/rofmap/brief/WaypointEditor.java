package pwcg.gui.rofmap.brief;

import java.awt.Color;
import java.awt.Font;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import pwcg.core.exception.PWCGException;
import pwcg.gui.colors.ColorMap;
import pwcg.gui.dialogs.PWCGMonitorFonts;
import pwcg.gui.rofmap.brief.model.BriefingMapPoint;
import pwcg.gui.utils.PWCGLabelFactory;

public class WaypointEditor implements IWaypointDetails
{
    private long associatedWaypointID;
    private JLabel descTextField;
    private JTextField altitudeTextField;
    private JTextField cruisingSpeedTextField;
    private JLabel distanceTextField;
    private JLabel headingTextField;
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

    public JLabel getDescriptionField()
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

    public JLabel getDistanceField()
    {
        return distanceTextField;
    }

    public JLabel getHeadingField()
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
        field.setHorizontalAlignment(JTextField.RIGHT);
        field.setForeground(Color.BLACK);

        return field;
    }

    private JLabel makeLabelField() throws PWCGException
    {
        Font font = PWCGMonitorFonts.getTypewriterFont();
        JLabel label = PWCGLabelFactory.makeTransparentLabel("", ColorMap.PAPER_FOREGROUND, font, SwingConstants.LEFT);
        return label;
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
