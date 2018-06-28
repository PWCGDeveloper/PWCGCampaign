package pwcg.gui.campaign.pilot;

import java.awt.Color;
import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;

import pwcg.gui.colors.ColorMap;

public class CampaignChoosePilotPicGUI extends  JFileChooser
{
	private static final long serialVersionUID = 1L;

	public CampaignChoosePilotPicGUI(File dir)
	{
		super(dir);
		
		Color bgColor = ColorMap.PAPER_BACKGROUND;
		setBackground(bgColor);
		this.setFileFilter(new PilotPicFilter());

	}
	
	class PilotPicFilter extends FileFilter 
	{
		PilotPicFilter ()
		{
		}

		public boolean accept(File file) 
		{
		    if(file.getName().contains(".jpg") || file.getName().contains(".jpeg") ||  file.getName().contains(".gif")) 
		    {
		    	return true;
		    }

			return false;
		}

		public String getDescription() 
		{
			return "*.*";
		}
		
	}
}
