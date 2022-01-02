package pwcg.gui.campaign.crewmember;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import pwcg.campaign.PictureManager;
import pwcg.campaign.crewmember.CrewMember;
import pwcg.campaign.crewmember.CrewMemberStatus;
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
import pwcg.gui.utils.ImageResizingPanel;
import pwcg.gui.utils.ImageResizingPanelBuilder;
import pwcg.gui.utils.PWCGButtonNoBackground;
import pwcg.gui.utils.PWCGLabelFactory;
import pwcg.gui.utils.PwcgBorderFactory;

public class CampaignHomeCrewMemberPanel extends ImageResizingPanel
{
	private static final long serialVersionUID = 1L;
	private ActionListener actionListener = null;

	public CampaignHomeCrewMemberPanel(ActionListener actionListener)  
	{
        super("");
        this.setLayout(new BorderLayout());
        this.setOpaque(false);

        this.actionListener = actionListener;
	}

	public void makePanel(List<CrewMember>crewMembers, String description, String action) throws PWCGException  
	{
        String imagePath = UiImageResolver.getImage(ScreenIdentifier.PlaqueBronzeBackground);
        this.setImageFromName(imagePath);
        this.setBorder(PwcgBorderFactory.createPlaqueBackgroundBorder());

        JPanel crewMemberListGrid = new JPanel(new GridLayout(0, 1));
		crewMemberListGrid.setOpaque(false);
		
		JPanel headerPlaque = makeNamePlaque(description);
		crewMemberListGrid.add(headerPlaque);
		
		for (CrewMember crewMember : crewMembers)
		{
			try
			{
			    JPanel buttonPanel = createCrewMemberButton(action, crewMember);
				crewMemberListGrid.add(buttonPanel);
			}
			catch (Exception e)
			{
				PWCGLogger.logException(e);
			}
		}
		
		this.add(crewMemberListGrid, BorderLayout.NORTH);		
	}

    private JPanel createCrewMemberButton(String action, CrewMember crewMember)
                    throws PWCGException
    {
        JPanel crewMemberPanel = new JPanel(new BorderLayout());
        crewMemberPanel.setOpaque(false);
        
        JLabel crewMemberPicButton = makeCrewMemberPicButton(crewMember);
        crewMemberPanel.add(crewMemberPicButton, BorderLayout.WEST);
        
        JLabel crewMemberStatusButton = makeCrewMemberStatusButton(crewMember);
        crewMemberPanel.add(crewMemberStatusButton, BorderLayout.EAST);
        
        String imagePath = ContextSpecificImages.imagesMisc() + "NamePlate.jpg";
        ImageResizingPanel nameplatePanel = ImageResizingPanelBuilder.makeImageResizingPanel(imagePath);
        nameplatePanel.setLayout(new BorderLayout());
                        
        Color buttonBG = ColorMap.CHALK_BACKGROUND;
        Color buttonFG = ColorMap.PLAQUE_GOLD;
        Font font = PWCGMonitorFonts.getPrimaryFont();
        

        JButton namePlateButton = new PWCGButtonNoBackground("          " + crewMember.getNameAndRank());
        namePlateButton.setBackground(buttonBG);
        namePlateButton.setForeground(buttonFG);
        namePlateButton.setOpaque(false);
        namePlateButton.setFont(font);
        namePlateButton.setBorderPainted(false);
        namePlateButton.setFocusPainted(false);
        namePlateButton.setHorizontalAlignment(SwingConstants.LEFT);
        namePlateButton.setFont(font);
        String actionCommand = action + crewMember.getSerialNumber();
        namePlateButton.setActionCommand(actionCommand);
        namePlateButton.addActionListener(actionListener);
        
        nameplatePanel.add(namePlateButton, BorderLayout.CENTER);
        
        crewMemberPanel.add(nameplatePanel, BorderLayout.CENTER);

        return crewMemberPanel;
    }

    private JLabel makeCrewMemberPicButton(CrewMember crewMember) throws PWCGUserException, PWCGException, PWCGException
    {
        JLabel crewMemberPicLabel = null;
        String picPath = PictureManager.getPicturePath(crewMember);
        Image crewMemberPic = ImageCache.getInstance().getBufferedImage(picPath);
        if (crewMemberPic != null)
        {
        	int imageHeight = PWCGMonitorSupport.getCrewMemberPlateHeight();
        	
        	Image scaledPic = crewMemberPic.getScaledInstance(imageHeight, -1, Image.SCALE_DEFAULT);
            ImageIcon icon = new ImageIcon(scaledPic);
            crewMemberPicLabel = PWCGLabelFactory.makeIconLabel(icon);
        }
        else
        {
        	crewMemberPicLabel = PWCGLabelFactory.makeDummyLabel();	
        }
        return crewMemberPicLabel;
    }
    

    private JPanel makeNamePlaque(String description) throws PWCGException  
    {
        String imagePath = ContextSpecificImages.imagesMisc() + "NamePlate.jpg";
        ImageResizingPanel headerPlaquePanel = ImageResizingPanelBuilder.makeImageResizingPanel(imagePath);
        headerPlaquePanel.setLayout(new BorderLayout());

        Color fgColor = ColorMap.PLAQUE_GOLD;
        Font font = PWCGMonitorFonts.getPrimaryFontLarge();

        JLabel squadronPanelLabel = PWCGLabelFactory.makeTransparentLabel(
                "     " + description,  fgColor, font, SwingConstants.LEFT);
        squadronPanelLabel.setHorizontalAlignment(JLabel.LEFT);
        squadronPanelLabel.setVerticalAlignment(JLabel.CENTER);
        
        headerPlaquePanel.add(squadronPanelLabel, BorderLayout.CENTER);
         
        return headerPlaquePanel;
    }


    private JLabel makeCrewMemberStatusButton(CrewMember crewMember) throws PWCGUserException, PWCGException, PWCGException
    {
        JLabel crewMemberStatusLabel = null;
        String imagePath = ContextSpecificImages.imagesMisc() + "Healthy.jpg";
        if (crewMember.getCrewMemberActiveStatus() == CrewMemberStatus.STATUS_WOUNDED)
        {
            imagePath = ContextSpecificImages.imagesMisc() + "Wounded.jpg";
        }
        if (crewMember.getCrewMemberActiveStatus() == CrewMemberStatus.STATUS_ON_LEAVE)
        {
            imagePath = ContextSpecificImages.imagesMisc() + "Leave.jpg";
        }
        else if (crewMember.getCrewMemberActiveStatus() == CrewMemberStatus.STATUS_SERIOUSLY_WOUNDED)
        {
            imagePath = ContextSpecificImages.imagesMisc() + "Maimed.jpg";
        }
        else if (crewMember.getCrewMemberActiveStatus() == CrewMemberStatus.STATUS_CAPTURED)
        {
            imagePath = ContextSpecificImages.imagesMisc() + "Captured.jpg";
        }
        else if (crewMember.getCrewMemberActiveStatus() == CrewMemberStatus.STATUS_KIA)
        {
            imagePath = ContextSpecificImages.imagesMisc() + "RIP.jpg";
        }
        
        Image crewMemberStatusImage = ImageCache.getInstance().getBufferedImage(imagePath);
        if (crewMemberStatusImage != null)
        {
            int imageHeight = PWCGMonitorSupport.getCrewMemberPlateHeight();            
            Image scaledPic = crewMemberStatusImage.getScaledInstance(imageHeight, -1, Image.SCALE_DEFAULT);
            ImageIcon icon = new ImageIcon(scaledPic);
            crewMemberStatusLabel = PWCGLabelFactory.makeIconLabel(icon);
        }
        else
        {
            crewMemberStatusLabel = PWCGLabelFactory.makeDummyLabel();    
        }
        return crewMemberStatusLabel;
    }

}
