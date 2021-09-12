package pwcg.gui.campaign.pilot;

import java.awt.BorderLayout;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionListener;
import java.util.List;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javax.swing.SwingConstants;

import pwcg.campaign.PictureManager;
import pwcg.campaign.squadmember.SquadronMember;
import pwcg.campaign.squadmember.SquadronMemberStatus;
import pwcg.core.exception.PWCGException;
import pwcg.core.exception.PWCGUserException;
import pwcg.core.utils.PWCGLogger;
import pwcg.gui.ScreenIdentifier;
import pwcg.gui.UiImageResolver;
import pwcg.gui.colors.ColorMap;
import pwcg.gui.dialogs.PWCGMonitorFonts;
import pwcg.gui.dialogs.PWCGMonitorSupport;
import pwcg.gui.image.ImageCache;
import pwcg.gui.utils.ContextSpecificImages;
import pwcg.gui.utils.ImageButton;
import pwcg.gui.utils.ImageResizingPanel;
import pwcg.gui.utils.ImageResizingPanelBuilder;
import pwcg.gui.utils.ButtonFactory;
import pwcg.gui.utils.ButtonNoBackground;
import pwcg.gui.utils.PwcgBorderFactory;

public class CampaignHomePilotPanel extends ImageResizingPanel
{
	private static final long serialVersionUID = 1L;
	private ActionListener actionListener = null;

	public CampaignHomePilotPanel(ActionListener actionListener)  
	{
        super("");
        this.setLayout(new BorderLayout());
        this.setOpaque(false);

        this.actionListener = actionListener;
	}

	public void makePanel(List<SquadronMember>pilots, String description, String action) throws PWCGException  
	{
        String imagePath = UiImageResolver.getImage(ScreenIdentifier.PlaqueBronzeBackground);
        this.setImageFromName(imagePath);
        this.setBorder(PwcgBorderFactory.createPlaqueBackgroundBorder());

        Pane pilotListGrid = new Pane(new GridLayout(0, 1));
		pilotListGrid.setOpaque(false);
		
		Pane headerPlaque = makeNamePlaque(description);
		pilotListGrid.add(headerPlaque);
		
		for (SquadronMember pilot : pilots)
		{
			try
			{
			    Pane buttonPanel = createPilotButton(action, pilot);
				pilotListGrid.add(buttonPanel);
			}
			catch (Exception e)
			{
				PWCGLogger.logException(e);
			}
		}
		
		this.add(pilotListGrid, BorderLayout.NORTH);		
	}

    private Pane createPilotButton(String action, SquadronMember pilot)
                    throws PWCGException
    {
        Pane pilotPanel = new Pane(new BorderLayout());
        pilotPanel.setOpaque(false);
        
        Label pilotPicButton = makePilotPicButton(pilot);
        pilotPanel.add(pilotPicButton, BorderLayout.WEST);
        
        Label pilotStatusButton = makePilotStatusButton(pilot);
        pilotPanel.add(pilotStatusButton, BorderLayout.EAST);
        
        String imagePath = ContextSpecificImages.imagesMisc() + "NamePlate.jpg";
        ImageResizingPanel nameplatePanel = ImageResizingPanelBuilder.makeImageResizingPanel(imagePath);
        nameplatePanel.setLayout(new BorderLayout());
                        
        Color buttonBG = ColorMap.CHALK_BACKGROUND;
        Color buttonFG = ColorMap.PLAQUE_GOLD;
        Font font = PWCGMonitorFonts.getPrimaryFont();
        

        Button namePlateButton = new ButtonNoBackground("          " + pilot.getNameAndRank());
        namePlateButton.setBackground(buttonBG);
        namePlateButton.setForeground(buttonFG);
        namePlateButton.setOpaque(false);
        namePlateButton.setFont(font);
        namePlateButton.setBorderPainted(false);
        namePlateButton.setFocusPainted(false);
        namePlateButton.setAlignment(SwingConstants.LEFT);
        namePlateButton.setFont(font);
        String actionCommand = action + pilot.getSerialNumber();
        namePlateButton.setActionCommand(actionCommand);
        namePlateButton.addActionListener(actionListener);
        
        nameplatePanel.add(namePlateButton, BorderLayout.CENTER);
        
        pilotPanel.add(nameplatePanel, BorderLayout.CENTER);

        return pilotPanel;
    }

    private Label makePilotPicButton(SquadronMember pilot) throws PWCGUserException, PWCGException, PWCGException
    {
        Label pilotPicButton = null;
        String picPath = PictureManager.getPicturePath(pilot);
        Image pilotPic = ImageCache.getInstance().getBufferedImage(picPath);
        if (pilotPic != null)
        {
        	int imageHeight = PWCGMonitorSupport.getPilotPlateHeight();
        	
        	Image scaledPic = pilotPic.getScaledInstance(imageHeight, -1, Image.SCALE_DEFAULT);

        	pilotPicButton = ImageButton.makePilotPicButton(scaledPic);
        }
        else
        {
        	pilotPicButton = new Label("");	
        }
        return pilotPicButton;
    }
    

    private Pane makeNamePlaque(String description) throws PWCGException  
    {
        String imagePath = ContextSpecificImages.imagesMisc() + "NamePlate.jpg";
        ImageResizingPanel headerPlaquePanel = ImageResizingPanelBuilder.makeImageResizingPanel(imagePath);
        headerPlaquePanel.setLayout(new BorderLayout());

        Label squadronPanelLabel = ButtonFactory.makePlaqueLabelLarge("     " + description);
        squadronPanelLabel.setAlignment(Label.LEFT);
        squadronPanelLabel.setVerticalAlignment(Label.CENTER);
        
        headerPlaquePanel.add(squadronPanelLabel, BorderLayout.CENTER);
         
        return headerPlaquePanel;
    }


    private Label makePilotStatusButton(SquadronMember pilot) throws PWCGUserException, PWCGException, PWCGException
    {
        Label pilotStatusButton = null;
        String imagePath = ContextSpecificImages.imagesMisc() + "Healthy.jpg";
        if (pilot.getPilotActiveStatus() == SquadronMemberStatus.STATUS_WOUNDED)
        {
            imagePath = ContextSpecificImages.imagesMisc() + "Wounded.jpg";
        }
        if (pilot.getPilotActiveStatus() == SquadronMemberStatus.STATUS_ON_LEAVE)
        {
            imagePath = ContextSpecificImages.imagesMisc() + "Leave.jpg";
        }
        else if (pilot.getPilotActiveStatus() == SquadronMemberStatus.STATUS_SERIOUSLY_WOUNDED)
        {
            imagePath = ContextSpecificImages.imagesMisc() + "Maimed.jpg";
        }
        else if (pilot.getPilotActiveStatus() == SquadronMemberStatus.STATUS_CAPTURED)
        {
            imagePath = ContextSpecificImages.imagesMisc() + "Captured.jpg";
        }
        else if (pilot.getPilotActiveStatus() == SquadronMemberStatus.STATUS_KIA)
        {
            imagePath = ContextSpecificImages.imagesMisc() + "RIP.jpg";
        }
        
        Image pilotStatusImage = ImageCache.getInstance().getBufferedImage(imagePath);
        if (pilotStatusImage != null)
        {
            int imageHeight = PWCGMonitorSupport.getPilotPlateHeight();            
            Image scaledPic = pilotStatusImage.getScaledInstance(imageHeight, -1, Image.SCALE_DEFAULT);
            pilotStatusButton = ImageButton.makePilotPicButton(scaledPic);
        }
        else
        {
            pilotStatusButton = new Label("");    
        }
        return pilotStatusButton;
    }

}
