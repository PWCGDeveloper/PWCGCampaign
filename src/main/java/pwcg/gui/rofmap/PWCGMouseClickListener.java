package pwcg.gui.rofmap;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;



public class PWCGMouseClickListener extends MouseAdapter
{
	private MapPanelBase parent = null;
	
	public PWCGMouseClickListener(MapPanelBase parent)
	{
		this.parent = parent;
	}
	
	@Override
	public void mousePressed(MouseEvent e) 
	{	
		if (e.getButton() == MouseEvent.BUTTON1) 
		{
			// Selected for drag
			parent.leftClickCallback(e);
		}
		if (e.getButton() == MouseEvent.BUTTON2 || e.getButton() == MouseEvent.BUTTON3) 
		{
			// selected for operation			
			parent.rightClickCallback(e);			
		}
	}

	@Override
	public void mouseReleased(MouseEvent mouseEvent) 
	{
		try
		{
			if (mouseEvent.getButton() == MouseEvent.BUTTON1) 
			{
				// Selected for drag
				parent.leftClickReleasedCallback(mouseEvent);
			}
		}
		catch (Exception e)
		{
		}
	}
}
