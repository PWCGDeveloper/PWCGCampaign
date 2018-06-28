package pwcg.gui.campaign.pilot;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionListener;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import pwcg.campaign.PictureManager;
import pwcg.campaign.squadmember.SquadronMember;
import pwcg.core.exception.PWCGException;
import pwcg.core.exception.PWCGIOException;
import pwcg.core.exception.PWCGUserException;
import pwcg.core.utils.Logger;
import pwcg.gui.colors.ColorMap;
import pwcg.gui.dialogs.ImageCache;
import pwcg.gui.dialogs.MonitorSupport;
import pwcg.gui.utils.ContextSpecificImages;
import pwcg.gui.utils.ImageButton;
import pwcg.gui.utils.ImageResizingPanel;
import pwcg.gui.utils.PWCGButtonFactory;
import pwcg.gui.utils.PWCGButtonNoBackground;

public class CampaignPilotListPanel extends ImageResizingPanel
{
	private static final long serialVersionUID = 1L;
	private ActionListener parent = null;

	public CampaignPilotListPanel(ActionListener parent)  
	{
        super(ContextSpecificImages.imagesMisc() + "paperPart.jpg");
	    this.parent = parent;
	}

	public JPanel makeSquadronRightPanel(Map<String, SquadronMember>pilots, String description, String action) throws PWCGException  
	{
        ImageResizingPanel pilotsPanel = new ImageResizingPanel(imagePath);
        pilotsPanel.setLayout(new BorderLayout());
		pilotsPanel.setOpaque(false);

		int totalEntries = 1 + pilots.size();

		JPanel pilotListGrid = new JPanel(new GridLayout(totalEntries,1));
		pilotListGrid.setOpaque(false);
		
		JPanel headerPlaque = makeHeaderPlaque(description);
		pilotListGrid.add(headerPlaque);
		
		for (SquadronMember pilot : pilots.values())
		{
			try
			{
			    JPanel buttonPanel = createPilotButton(action, pilot);

				// button.addActionListener(campaignListener);
				pilotListGrid.add(buttonPanel);
			}
			catch (Exception e)
			{
				Logger.logException(e);
			}
		}
		
		pilotsPanel.add(pilotListGrid, BorderLayout.NORTH);
		
		return pilotsPanel;
	}

    public JPanel makeHeaderPlaque(String description) throws PWCGException  
    {
        String imagePath = ContextSpecificImages.imagesMisc() + "NamePlate2.jpg";
        ImageResizingPanel headerPlaquePanel = new ImageResizingPanel(imagePath);
        headerPlaquePanel.setLayout(new BorderLayout());

        JLabel squadronPanelLabel = PWCGButtonFactory.makePlaqueLabelLarge("     " + description);
        squadronPanelLabel.setHorizontalAlignment(JLabel.LEFT);
        squadronPanelLabel.setVerticalAlignment(JLabel.CENTER);
        
        headerPlaquePanel.add(squadronPanelLabel, BorderLayout.CENTER);
         
        return headerPlaquePanel;
    }

    /**
     * @param action
     * @param buttonBG
     * @param buttonFG
     * @param font
     * @param pilot
     * @return
     * @throws PWCGUserException
     * @throws PWCGIOException
     * @throws PWCGException
     */
    private JPanel createPilotButton(String action, SquadronMember pilot)
                    throws PWCGException
    {
        JPanel pilotPanel = new JPanel(new BorderLayout());
        
        String picPath = PictureManager.getPicturePath(pilot);

        JLabel pilotPicButton = null;
        Image pilotPic = ImageCache.getInstance().getBufferedImage(picPath);
        if (pilotPic != null)
        {
        	int imageHeight = MonitorSupport.getPilotPlateHeight();
        	
        	Image scaledPic = pilotPic.getScaledInstance(imageHeight, -1, Image.SCALE_DEFAULT);

        	pilotPicButton = ImageButton.makePilotPicButton(scaledPic);
        }
        else
        {
        	pilotPicButton = new JLabel("");	
        }
        
        pilotPanel.add(pilotPicButton, BorderLayout.WEST);
        
        String imagePath = ContextSpecificImages.imagesMisc() + "NamePlate2.jpg";
        ImageResizingPanel nameplatePanel = new ImageResizingPanel(imagePath);
        nameplatePanel.setLayout(new BorderLayout());
                        
        Color buttonBG = ColorMap.CHALK_BACKGROUND;
        Color buttonFG = ColorMap.PLAQUE_GOLD;
        Font font = MonitorSupport.getPrimaryFont();
        

        JButton namePlateButton = new PWCGButtonNoBackground("          " + pilot.getNameAndRank());
        namePlateButton.setBackground(buttonBG);
        namePlateButton.setForeground(buttonFG);
        namePlateButton.setOpaque(false);
        namePlateButton.setFont(font);
        namePlateButton.setHorizontalAlignment(SwingConstants.LEFT);
        namePlateButton.setFont(font);
        String actionCommand = action + pilot.getSerialNumber();
        namePlateButton.setActionCommand(actionCommand);
        namePlateButton.addActionListener(parent);
        
        nameplatePanel.add(namePlateButton, BorderLayout.CENTER);
        
        pilotPanel.add(nameplatePanel, BorderLayout.CENTER);

        return pilotPanel;
    }
}
