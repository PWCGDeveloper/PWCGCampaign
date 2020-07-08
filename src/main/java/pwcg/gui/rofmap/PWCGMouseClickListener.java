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
			parent.leftClickCallback(e);
		}
        if (e.getButton() == MouseEvent.BUTTON2) 
        {
            parent.centerClickCallback(e);           
        }
        if (e.getButton() == MouseEvent.BUTTON3) 
        {
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
				parent.leftClickReleasedCallback(mouseEvent);
			}
		}
		catch (Exception e)
		{
		}
	}
}
