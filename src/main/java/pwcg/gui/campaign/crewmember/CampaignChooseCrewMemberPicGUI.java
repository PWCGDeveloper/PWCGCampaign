package pwcg.gui.campaign.crewmember;

import java.awt.Color;
import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;

import pwcg.gui.colors.ColorMap;

public class CampaignChooseCrewMemberPicGUI extends  JFileChooser
{
	private static final long serialVersionUID = 1L;

	public CampaignChooseCrewMemberPicGUI(File dir)
	{
		super(dir);
		
		Color bgColor = ColorMap.PAPER_BACKGROUND;
		setBackground(bgColor);
		this.setFileFilter(new CrewMemberPicFilter());
	}
	
	class CrewMemberPicFilter extends FileFilter 
	{
		CrewMemberPicFilter ()
		{
		}

		public boolean accept(File file) 
		{
		    if(file.getName().contains(".jpg") || file.getName().contains(".jpeg") ||  file.getName().contains(".gif")) 
		    {
		    	return true;
		    }
		    
            if(file.isDirectory()) 
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
