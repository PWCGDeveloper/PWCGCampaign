package pwcg.gui.campaign.pilot;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import pwcg.campaign.Campaign;
import pwcg.campaign.api.ICountry;
import pwcg.campaign.api.Side;
import pwcg.campaign.context.PWCGContextManager;
import pwcg.campaign.factory.CountryFactory;
import pwcg.campaign.medals.Medal;
import pwcg.campaign.medals.MedalManager;
import pwcg.campaign.squadmember.SquadronMember;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.Logger;
import pwcg.gui.PwcgGuiContext;
import pwcg.gui.colors.ColorMap;
import pwcg.gui.dialogs.ErrorDialog;
import pwcg.gui.dialogs.ImageCache;
import pwcg.gui.dialogs.MonitorSupport;
import pwcg.gui.sound.SoundManager;
import pwcg.gui.utils.ContextSpecificImages;
import pwcg.gui.utils.ImageResizingPanel;
import pwcg.gui.utils.PWCGButtonFactory;

public class CampaignPilotMedalPanel extends PwcgGuiContext implements ActionListener
{
    private static final long serialVersionUID = 1L;

    private int medalsPerRow = 5;
	private SquadronMember pilot = null;

	public CampaignPilotMedalPanel(SquadronMember pilot)
	{
        super();

        Dimension screenSize = MonitorSupport.getPWCGFrameSize();
		medalsPerRow = screenSize.width / 250;

        this.pilot = pilot;
	}

	public void makePanels() throws PWCGException  
	{
	    setCenterPanel(makeCenterPanel());
	    setLeftPanel(makeNavigationPanel());
	}

	private JPanel makeCenterPanel() throws PWCGException 
	{
        SoundManager.getInstance().playSound("MedalCaseOpen.WAV");

        String imagePath = ContextSpecificImages.imagesMisc() + "PilotDeskTop.jpg";
        ImageResizingPanel campaignPilotMedalPanel = new ImageResizingPanel(imagePath);
        campaignPilotMedalPanel.setLayout(new BorderLayout());

		JPanel pilotMedalPanel = makePilotMedalPanel();
		campaignPilotMedalPanel.add(pilotMedalPanel, BorderLayout.NORTH);
		
		return campaignPilotMedalPanel;
	}	

    private JPanel makeNavigationPanel() throws PWCGException  
    {
        String imagePath = getSideImage("PilotInfoNav.jpg");

        ImageResizingPanel medalPanel = new ImageResizingPanel(imagePath);
        medalPanel.setLayout(new BorderLayout());
        medalPanel.setOpaque(false);

        JPanel buttonPanel = new JPanel(new GridLayout(0,1));
        buttonPanel.setOpaque(false);
        
        JButton finishedButton = PWCGButtonFactory.makeMenuButton("Finished", "PilotMedalFinished", this);
        buttonPanel.add(finishedButton);

        medalPanel.add(buttonPanel, BorderLayout.NORTH);
        
        return medalPanel;
    }

	private JPanel makePilotMedalPanel() throws PWCGException 
	{
		JPanel medalPanel = new JPanel (new GridLayout(0, 1));
		
		if (pilot.getMedals().size() == 0)
		{
			JPanel medalRow = getEmptyMedalPanelRow();
			medalPanel.add(medalRow);
		}
		else
		{
			for (int i = 0; i < pilot.getMedals().size(); i+=medalsPerRow)
			{
				int lastMedal = i + medalsPerRow;
				if (lastMedal > pilot.getMedals().size())
				{
					lastMedal = pilot.getMedals().size();
				}
				
				JPanel medalRow = getMedalPanelRow(i, lastMedal);
				medalPanel.add(medalRow);
			}
		}
				
		return medalPanel;
	}

	private JPanel getEmptyMedalPanelRow() 
	{
        String medalBoxImagePath = ContextSpecificImages.imagesMedals() + "MedalBox.jpg";
        ImageResizingPanel medalPanel = new ImageResizingPanel(medalBoxImagePath);
		medalPanel.setImage(medalBoxImagePath);
		medalPanel.setLayout(new GridLayout(1, medalsPerRow));
		medalPanel.setOpaque(false);
		
        String medalPath = ContextSpecificImages.imagesMedals() + "EmptyMedal.gif";
		ImageIcon medalIcon = null;  
		try 
		{
			medalIcon = ImageCache.getInstance().getImageIcon(medalPath);
		}
		catch (Exception ex) 
		{
			Logger.logException(ex);
		}

		Color bg = ColorMap.PAPER_BACKGROUND;

		if (medalIcon != null)
		{
			for (int i = 0; i < medalsPerRow; ++i)
			{
				JLabel lMedal = new JLabel(medalIcon);
				lMedal.setBackground(bg);
				lMedal.setOpaque(false);

				medalPanel.add(lMedal);
			}
		}

		
		return medalPanel;
	}

	private JPanel getMedalPanelRow(int firstMedal, int lastMedal) throws PWCGException 
	{
        String medalBoxImagePath = ContextSpecificImages.imagesMedals() + "MedalBox.jpg";
		ImageResizingPanel medalPanel = new ImageResizingPanel(medalBoxImagePath);
		medalPanel.setImage(medalBoxImagePath);
		medalPanel.setLayout(new GridLayout(1, medalsPerRow));
		medalPanel.setOpaque(false);
		
		Color bg = ColorMap.PAPER_BACKGROUND;

		for (int i = firstMedal; i < lastMedal; ++i)
		{
			// The medal manager has the image
		    Campaign campaign = PWCGContextManager.getInstance().getCampaign();
		    ICountry country = CountryFactory.makeCountryByCountry(pilot.getCountry());
			Medal medal =  MedalManager.getMedalFromAnyManager(country, campaign, pilot.getMedals().get(i).getMedalName());
			
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
					medalIcon = ImageCache.getInstance().getImageIcon(medalPath);
				}
				catch (Exception ex) 
				{
		            Logger.logException(ex);
				}
				
				JLabel lMedal = new JLabel(medalIcon);
				lMedal.setBackground(bg);
				lMedal.setOpaque(false);

				medalPanel.add(lMedal);
			}
		}

		int remainder = medalsPerRow - (lastMedal % medalsPerRow);
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
	
	

	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent ae)
	{
		try
		{
            String action = ae.getActionCommand();

            if (action.equalsIgnoreCase("PilotMedalFinished"))
            {
                finishedWithCampaignScreen();
            }
		}
		catch (Exception e)
		{
			Logger.logException(e);
			ErrorDialog.internalError(e.getMessage());
		}
	}
}
