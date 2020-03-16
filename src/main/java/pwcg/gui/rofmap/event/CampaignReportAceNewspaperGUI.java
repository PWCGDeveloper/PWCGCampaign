package pwcg.gui.rofmap.event;

import java.awt.Color;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

import pwcg.aar.ui.events.model.AceKilledEvent;
import pwcg.campaign.Campaign;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.squadmember.SquadronMember;
import pwcg.core.utils.PWCGLogger;
import pwcg.core.utils.PWCGLogger.LogLevel;
import pwcg.gui.colors.ColorMap;
import pwcg.gui.dialogs.ErrorDialog;
import pwcg.gui.utils.ContextSpecificImages;
import pwcg.gui.utils.ImageResizingPanel;
import pwcg.gui.utils.WrappingLabel;

public class CampaignReportAceNewspaperGUI extends ImageResizingPanel implements ActionListener
{
	private static final long serialVersionUID = 1L;

	// Display an existing combat report
	public CampaignReportAceNewspaperGUI(AceKilledEvent aceEvent, Campaign campaign)
	{
		super(ContextSpecificImages.imagesNewspaper() + "newspaper.jpg");
		setLayout(null);
		makeGUI(aceEvent, campaign);		
	}
	
	private void makeGUI(AceKilledEvent aceEvent, Campaign campaign) 
	{
		setSize(800, 640);
		
		try
		{
			Color bgColor = ColorMap.NEWSPAPER_BACKGROUND;
			
			// Picture
			SquadronMember pilot = PWCGContext.getInstance().getAceManager().getHistoricalAceBySerialNumber(aceEvent.getPilotSerialNumber());
			if (pilot != null)
			{
				ImageIcon imageIcon = pilot.determinePilotPicture();  
				
				// Picture label
				JLabel pilotLabel = new JLabel(imageIcon);
				pilotLabel.setOpaque(false);
				pilotLabel.setBackground(bgColor);
				pilotLabel.setLocation(new Point(20, 170));
				pilotLabel.setSize(imageIcon.getIconWidth(), imageIcon.getIconHeight());
				this.add(pilotLabel);
				
				String newsReport = aceEvent.getPilotName() + " was lost in action today";
				
				WrappingLabel lNewsLabel = new WrappingLabel(newsReport, 220, 30);
				lNewsLabel.setLocation(new Point(15, (175 + imageIcon.getIconHeight())));
				lNewsLabel.setBackground(bgColor);
				lNewsLabel.setOpaque(false);
				this.add(lNewsLabel);
			}
			else
			{
			    PWCGLogger.log(LogLevel.ERROR, "CampaignReportAceNewspaperGUI: Failed to find pilot"); 
			}
		}
		catch (Exception e)
		{
			PWCGLogger.logException(e);
			ErrorDialog.internalError(e.getMessage());
		}
	}
	
	public void actionPerformed(ActionEvent ae)
	{
	}
	
	public void makeVisible(boolean visible) 
	{
	}
}
