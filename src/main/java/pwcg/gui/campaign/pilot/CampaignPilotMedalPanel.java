package pwcg.gui.campaign.pilot;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import pwcg.campaign.api.ICountry;
import pwcg.campaign.api.Side;
import pwcg.campaign.factory.CountryFactory;
import pwcg.campaign.medals.Medal;
import pwcg.campaign.squadmember.SquadronMember;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.PWCGLogger;
import pwcg.gui.colors.ColorMap;
import pwcg.gui.dialogs.PWCGMonitorSupport;
import pwcg.gui.dialogs.PWCGMonitorSupport.MonitorSize;
import pwcg.gui.image.ImageIconCache;
import pwcg.gui.sound.SoundManager;
import pwcg.gui.utils.ContextSpecificImages;
import pwcg.gui.utils.ImageResizingPanel;
import pwcg.gui.utils.PwcgBorderFactory;

public class CampaignPilotMedalPanel extends ImageResizingPanel
{
    private static final long serialVersionUID = 1L;

    private int medalsPerRow = 5;
	private SquadronMember pilot = null;

	public CampaignPilotMedalPanel(SquadronMember pilot)
	{
        super("");
        this.setLayout(new BorderLayout());
        this.setOpaque(false);

        this.medalsPerRow = calcMedalsPerRow();
        this.pilot = pilot;
	}

	public void makePanels() throws PWCGException  
	{
        SoundManager.getInstance().playSound("MedalCaseOpen.WAV");

        String imagePath = ContextSpecificImages.imagesMedals() + "OpenMedalBox.png";
        this.setImage(imagePath);
        this.setBorder(PwcgBorderFactory.createMedalBoxBorder());

	    this.add(BorderLayout.CENTER, makeCenterPanel());
	}

	private JPanel makeCenterPanel() throws PWCGException 
	{
        JPanel campaignPilotMedalPanel = new JPanel(new BorderLayout());
        campaignPilotMedalPanel.setOpaque(false);

		JPanel pilotMedalPanel = makePilotMedalPanel();
		campaignPilotMedalPanel.add(pilotMedalPanel, BorderLayout.NORTH);
		
		return campaignPilotMedalPanel;
	}	
    
    private int calcMedalsPerRow()
    {
        MonitorSize monitorSize = PWCGMonitorSupport.getFrameWidth();
        if (monitorSize == MonitorSize.FRAME_VERY_SMALL)
        {
            return 1;
        }
        else if (monitorSize == MonitorSize.FRAME_SMALL)
        {
            return 3;
        }
        if (monitorSize == MonitorSize.FRAME_MEDIUM)
        {
            return 4;
        }
        else
        {
            return 5;
        }
    }

	private JPanel makePilotMedalPanel() throws PWCGException 
	{
        JPanel medalPanel = new JPanel(new GridLayout(0, medalsPerRow));
		medalPanel.setOpaque(false);
		
		Color bg = ColorMap.PAPER_BACKGROUND;

		for (Medal medal : pilot.getMedals())
		{
		    ICountry country = CountryFactory.makeCountryByCountry(pilot.getCountry());			
			if (medal != null)
			{
				String medalSide = "Axis";
				if (country.getSide() == Side.ALLIED)
				{
					medalSide = "Allied";
				}
					
		        String medalPath = ContextSpecificImages.imagesMedals() + medalSide + "\\" + medal.getMedalImage();
				ImageIcon medalIcon = null;  
				try 
				{
					medalIcon = ImageIconCache.getInstance().getImageIcon(medalPath);
				}
				catch (Exception ex) 
				{
		            PWCGLogger.logException(ex);
				}
				
				JLabel lMedal = new JLabel(medalIcon);
				lMedal.setBackground(bg);
				lMedal.setOpaque(false);

				medalPanel.add(lMedal);
			}
		}

		int remainder = medalsPerRow - (pilot.getMedals().size() % medalsPerRow);
		if (remainder != medalsPerRow)
		{
			for (int i = 0; i < remainder; ++i)
			{
				JLabel ldummy = new JLabel("");
				ldummy.setBackground(bg);
				ldummy.setOpaque(false);
	
				medalPanel.add(ldummy);
			}
		}
		
		return medalPanel;
	}
}
