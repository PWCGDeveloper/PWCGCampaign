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
import pwcg.gui.image.ImageIconCache;
import pwcg.gui.sound.SoundManager;
import pwcg.gui.utils.ContextSpecificImages;
import pwcg.gui.utils.ImageResizingPanel;
import pwcg.gui.utils.ImageResizingPanelBuilder;

public class CampaignPilotMedalPanel extends JPanel
{
    private static final long serialVersionUID = 1L;

    private int medalsPerRow = 5;
	private SquadronMember pilot = null;

	public CampaignPilotMedalPanel(SquadronMember pilot)
	{
        this.setLayout(new BorderLayout());
        this.medalsPerRow = calcMedalsPerRow();
        this.pilot = pilot;
	}

	public void makePanels() throws PWCGException  
	{
	    this.add(BorderLayout.CENTER, makeCenterPanel());
	}

	private JPanel makeCenterPanel() throws PWCGException 
	{
        SoundManager.getInstance().playSound("MedalCaseOpen.WAV");

        String imagePath = ContextSpecificImages.imagesMedals() + "MedalBox.jpg";
        ImageResizingPanel campaignPilotMedalPanel = ImageResizingPanelBuilder.makeImageResizingPanel(imagePath);
        campaignPilotMedalPanel.setLayout(new BorderLayout());
        campaignPilotMedalPanel.setOpaque(false);
        campaignPilotMedalPanel.setBorder(BorderFactory.createEmptyBorder(75,75,75,75));

		JPanel pilotMedalPanel = makePilotMedalPanel();
		campaignPilotMedalPanel.add(pilotMedalPanel, BorderLayout.NORTH);
		
		return campaignPilotMedalPanel;
	}	
    
    private int calcMedalsPerRow()
    {
        if (PWCGMonitorSupport.getPWCGFrameSize().width < 800)
        {
            return 1;
        }
        else if (PWCGMonitorSupport.getPWCGFrameSize().width <= 1200)
        {
            return 3;
        }
        else if (PWCGMonitorSupport.getPWCGFrameSize().width <= 1600)
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
