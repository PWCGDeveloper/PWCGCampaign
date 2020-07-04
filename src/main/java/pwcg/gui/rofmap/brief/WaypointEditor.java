package pwcg.gui.rofmap.brief;

import java.awt.Font;

import javax.swing.JTextField;

import pwcg.core.exception.PWCGException;
import pwcg.core.utils.MathUtils;
import pwcg.gui.dialogs.PWCGMonitorFonts;
import pwcg.mission.mcu.McuWaypoint;


public class WaypointEditor
{
	private JTextField descTextField;
	private JTextField altitudeTextField;
    private JTextField distanceTextField;
    private JTextField headingTextField;
    private String actionCommandKey = "";
	
	public WaypointEditor() 
	{	    
	}
	
	public JTextField getDesc() 
	{
		return descTextField;
	}
	
    public JTextField getAltitude() 
    {
        return altitudeTextField;
    }
    
    public int getAltitudeValue() 
    {
        return new Integer(altitudeTextField.getText());
    }
    
    public JTextField getDistance() 
    {
        return distanceTextField;
    }
    
    public JTextField getHeading() 
    {
        return headingTextField;
    }
	
	public void setEnabled(boolean enabled)
	{
		descTextField.setEditable(false);
		altitudeTextField.setEditable(enabled);
		distanceTextField.setEditable(false);
	}

	public void initializeWPEdit(McuWaypoint previousWP, McuWaypoint thisWP) throws PWCGException
	{
	    String displayDescription = getWaypointDescription(thisWP);
	    descTextField = makeTextField(displayDescription);
	    
        String yPos = getPosition(thisWP);
        altitudeTextField = makeTextField(yPos);

        int distanceAsInt = 0;
        int headingAsInt = 0;
        if (previousWP != null)
	    {
            distanceAsInt = getDistance(previousWP, thisWP);
            headingAsInt = getHeading(previousWP, thisWP);
	    }

        distanceTextField = makeTextField(Integer.valueOf(distanceAsInt / 1000).toString());
        headingTextField = makeTextField(Integer.valueOf(headingAsInt).toString());
        
        descTextField.setEditable(false);
	    altitudeTextField.setEditable(true);
	    distanceTextField.setEditable(false);
	    headingTextField.setEditable(false);
	}

    private JTextField makeTextField(String value) throws PWCGException
    {
        Font font = PWCGMonitorFonts.getPrimaryFontSmall();

        JTextField field = new JTextField (value);
        field.setOpaque(false);
        field.setFont(font);
        field.setHorizontalAlignment(JTextField.RIGHT);
        
        return field;
    }

    public void updateDistance(McuWaypoint previousWP, McuWaypoint thisWP) throws PWCGException
    {
        if (previousWP != null)
        {
            int distanceAsInt = getDistance(previousWP, thisWP);
            distanceTextField.setText(Integer.valueOf(distanceAsInt / 1000).toString());
        }
        else
        {
            distanceTextField.setText("0");
        }
    }

    private int getHeading(McuWaypoint previousWP, McuWaypoint thisWP) throws PWCGException
    {
        int headingAsInt = 0;
        if (previousWP != null)
        {
            double angle = MathUtils.calcAngle(previousWP.getPosition(), thisWP.getPosition());
            headingAsInt = Double.valueOf(angle).intValue();
        }
        return headingAsInt;
    }

    private int getDistance(McuWaypoint previousWP, McuWaypoint thisWP)
    {
        int distanceAsInt = 0;
	    if (previousWP != null)
	    {
	        double distanceExact = MathUtils.calcDist(previousWP.getPosition(), thisWP.getPosition());
	        distanceAsInt = Double.valueOf(distanceExact).intValue();
	    }
	    
	    return distanceAsInt;
    }

    private String getPosition(McuWaypoint thisWP)
    {
        String yPos = "";
	    if (thisWP.getPosition() != null)
	    {
	        int yPosAsInt = Double.valueOf(thisWP.getPosition().getYPos()).intValue();
	        yPos += yPosAsInt;
	    }
        return yPos;
    }

    private String getWaypointDescription(McuWaypoint nextWP)
    {
        int startIndex = nextWP.getDesc().indexOf(':');
        int endIndex = nextWP.getDesc().lastIndexOf("Waypoint");
                
        String displayDescription = "";
	    if (endIndex > 0)
	    {
	        displayDescription = nextWP.getDesc().substring(startIndex + 1, endIndex);
	    }
	    else
	    {
	        displayDescription = nextWP.getDesc().substring(startIndex + 1);
	    }
        return displayDescription;
    }

    public String getActionCommandKey()
    {
        return actionCommandKey;
    }       
}
