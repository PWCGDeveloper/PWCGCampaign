package pwcg.gui.rofmap;

import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

import pwcg.core.utils.PWCGLogger;


public class PWCGMouseWheelListener implements MouseWheelListener
{
	private MapPanelBase parent = null;
	
	PWCGMouseWheelListener(MapPanelBase parent)
	{
		this.parent = parent;
	}

	@Override
	public void mouseWheelMoved(MouseWheelEvent e)
	{
	    try
	    {
    		e.getWheelRotation();
    		if (e.getWheelRotation() < 0)
    		{
    			parent.increaseZoom();
    		}
    		else if (e.getWheelRotation() > 0)
    		{
    			parent.decreaseZoom();
    		}
	    }
	    catch  (Exception exp)
	    {
	        PWCGLogger.logException(exp);
	    }
	}
}
