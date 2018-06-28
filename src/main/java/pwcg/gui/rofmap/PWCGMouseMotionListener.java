package pwcg.gui.rofmap;

import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;


public class PWCGMouseMotionListener extends MouseMotionAdapter
{
	private MapPanelBase parent = null;
	
	PWCGMouseMotionListener(MapPanelBase parent)
	{
		this.parent = parent;
	}

	@Override
	public void mouseDragged(MouseEvent e) 
	{
		parent.mouseDraggedCallback(e);
	}

	@Override
	public void mouseMoved(MouseEvent e) 
	{	
		parent.mouseMovedCallback(e);
	}

}
