package pwcg.gui.campaign.pilot;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Date;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import pwcg.campaign.Campaign;
import pwcg.campaign.PictureManager;
import pwcg.campaign.api.ICountry;
import pwcg.campaign.factory.CountryFactory;
import pwcg.campaign.plane.PlaneType;
import pwcg.campaign.squadmember.SquadronMember;
import pwcg.campaign.squadron.Squadron;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.PWCGLogger;
import pwcg.gui.CampaignGuiContextManager;
import pwcg.gui.UiImageResolver;
import pwcg.gui.campaign.home.CampaignHomeGUI;
import pwcg.gui.colors.ColorMap;
import pwcg.gui.dialogs.ErrorDialog;
import pwcg.gui.dialogs.PWCGMonitorFonts;
import pwcg.gui.dialogs.PWCGMonitorSupport;
import pwcg.gui.image.ImageCache;
import pwcg.gui.image.ImageIconCache;
import pwcg.gui.sound.SoundManager;
import pwcg.gui.utils.ContextSpecificImages;
import pwcg.gui.utils.ImagePreviewPanel;
import pwcg.gui.utils.ImageResizingPanel;
import pwcg.gui.utils.ImageResizingPanelBuilder;
import pwcg.gui.utils.ImageScaledPanel;
import pwcg.gui.utils.PWCGButtonFactory;
import pwcg.gui.utils.PWCGButtonNoBackground;
import pwcg.gui.utils.PWCGJButton;
import pwcg.gui.utils.ToolTipManager;
import pwcg.gui.utils.UIUtils;

public class CampaignPilotPanelSet extends JPanel implements ActionListener
{
    private static final long serialVersionUID = 1L;

    private SquadronMember pilot;
	private Squadron squad;
    private Campaign campaign;
    private CampaignHomeGUI parent;

    protected String changePilotPictureAction = "";
    protected String changeSkinAction = "";
	protected String openMedalBoxAction = "";
    protected String openLogBookAction = "";
    protected JPanel centerPanel;

    public CampaignPilotPanelSet(Campaign campaign, Squadron squad, SquadronMember pilot, CampaignHomeGUI parent)
    {
         super();

        this.pilot = pilot;
        this.squad = squad;
        this.parent = parent;
        this.campaign = campaign;

        changePilotPictureAction = "Change Picture";
		openMedalBoxAction = "Open Medal Box:";
		openLogBookAction = "View Pilot Log:";
	}

	public void makePanels() throws PWCGException  
	{
	    centerPanel = makeCenterPanel();
	    this.add(BorderLayout.CENTER, centerPanel);
	    this.add(BorderLayout.WEST, makenavigationPanel());
	}

	public JPanel makeCenterPanel() throws PWCGException 
	{	
        String imagePath = ContextSpecificImages.imagesMisc() + "PilotDeskTop.jpg";
        ImageResizingPanel campaignPilotPanel = ImageResizingPanelBuilder.makeImageResizingPanel(imagePath);
        campaignPilotPanel.setLayout(new BorderLayout());

		JPanel pilotLogPanel = makePilotLog();
        JPanel picturePanel = makePicturePanel();
		JPanel pilotMedalBoxPanel = makeMedalBox();
		
        campaignPilotPanel.add(pilotMedalBoxPanel,BorderLayout.NORTH);
        campaignPilotPanel.add(picturePanel,BorderLayout.WEST);
		campaignPilotPanel.add(pilotLogPanel,BorderLayout.CENTER);
				
		return campaignPilotPanel;
	}	

    private JPanel makenavigationPanel() throws PWCGException  
    {
        String imagePath = UiImageResolver.getSideImage(campaign, "PilotInfoNav.jpg");

        ImageResizingPanel pilotPanel = ImageResizingPanelBuilder.makeImageResizingPanel(imagePath);
        pilotPanel.setLayout(new BorderLayout());
        pilotPanel.setOpaque(false);

        JPanel buttonPanel = new JPanel(new GridLayout(0,1));
        buttonPanel.setOpaque(false);
        
        JButton finishedButton = PWCGButtonFactory.makeMenuButton("Finished", "PilotFinished", this);
        buttonPanel.add(finishedButton);

        pilotPanel.add(buttonPanel, BorderLayout.NORTH);
        
        return pilotPanel;
    }

	private JPanel makePilotLog() throws PWCGException 
	{
		JPanel pilotLogPanel = new JPanel(new BorderLayout());
		pilotLogPanel.setOpaque(false);

		String logSpacer = "          ";

		// The Log book
        String imagePath = ContextSpecificImages.imagesMisc() + "PilotLogBook.gif";
        ImageScaledPanel pilotLogBorderPanel = new ImageScaledPanel(imagePath, 0.75);
		pilotLogBorderPanel.setLayout(new BorderLayout());
		pilotLogBorderPanel.setOpaque(false);

		Font font = PWCGMonitorFonts.getPilotLogBookFont();

		Color bg = ColorMap.WOOD_BACKGROUND;
		Color fg = ColorMap.MAP_LOG_FOREGROUND;
		Color fadedFG = new Color(fg.getRed(),fg.getGreen(),fg.getBlue(),180);
		
		JPanel pilotInfoPanel = new JPanel (new GridLayout(0, 1));
		pilotInfoPanel.setOpaque(false);

		for (int i = 0; i < 2; ++i)
		{
			JLabel lSpacer = new JLabel(logSpacer);
			lSpacer.setBackground(bg);
			lSpacer.setOpaque(false);
			lSpacer.setFont(font);
			pilotInfoPanel.add(lSpacer);
		}
		
        String labelSpacing = "";
        int numSpacesForLabelSpacing = Double.valueOf(PWCGMonitorSupport.getPWCGFrameSize().getWidth() / 100 * 1.7).intValue();
		for (int i = 0; i < numSpacesForLabelSpacing; ++i)
		{
		    labelSpacing += " ";
		}
		
		JButton logButton = new PWCGButtonNoBackground(labelSpacing + "Pilot Log");
		logButton.setHorizontalAlignment(SwingConstants.LEFT);
		logButton.setForeground(fadedFG);
		logButton.setBackground(bg);
		logButton.setOpaque(false);
		logButton.setBorderPainted(false);
		logButton.setFont(font);
		String actionCommand = openLogBookAction + pilot.getSerialNumber();
		logButton.addActionListener(this);
		logButton.setActionCommand(actionCommand);
        ToolTipManager.setToolTip(logButton, "View pilot log book");

		pilotInfoPanel.add(logButton);
		

		for (int i = 0; i < 1; ++i)
		{
			JLabel lSpacer = new JLabel(logSpacer);
			lSpacer.setBackground(bg);
			lSpacer.setOpaque(false);
			lSpacer.setFont(font);
			pilotInfoPanel.add(lSpacer);
		}

		String pilotRank = labelSpacing + pilot.getNameAndRank();
		JLabel lRank = new JLabel(pilotRank, JLabel.LEFT);
		lRank.setForeground(fadedFG);
		lRank.setBackground(bg);
		lRank.setOpaque(false);
		lRank.setFont(font);
		pilotInfoPanel.add(lRank);

		for (int i = 0; i < 6; ++i)
		{
			JLabel lSpacer = new JLabel("          ");
			lSpacer.setBackground(bg);
			lSpacer.setOpaque(false);
			lSpacer.setFont(font);
			pilotInfoPanel.add(lSpacer);
		}
		
		pilotLogBorderPanel.add(pilotInfoPanel, BorderLayout.CENTER);

		pilotLogPanel.add(pilotLogBorderPanel, BorderLayout.CENTER);
        
		return pilotLogPanel;
	}

    private JPanel makePicturePanel() throws PWCGException 
    {
        // Picture button
        // Set up the picture
        JPanel pilotPicPanel = new JPanel(new BorderLayout());
        pilotPicPanel.setOpaque(false);
        
        PWCGJButton planeButton = makePlanePicture(campaign.getDate());
        PWCGJButton pilotButton = makePilotPicture();
        
        pilotPicPanel.add(pilotButton, BorderLayout.NORTH);
        pilotPicPanel.add(planeButton, BorderLayout.SOUTH);
        
        return pilotPicPanel;
    }

	private PWCGJButton makePilotPicture() throws PWCGException 
	{
		Color bg = ColorMap.WOOD_BACKGROUND;
		// Picture
		ImageIcon imageIcon = null;  
		try 
		{
			String picPath = PictureManager.getPicturePath(pilot);			
			imageIcon = ImageCache.getInstance().getRotatedImageIcon(picPath, 20);
		}
		catch (Exception ex) 
		{
            PWCGLogger.logException(ex);
		}

		PWCGJButton pilotPictureButton = null;
		if (imageIcon != null)
		{
			pilotPictureButton = new PWCGJButton(imageIcon);
		}
		else
		{
			pilotPictureButton = new PWCGJButton("press to set picture");
		}
		pilotPictureButton.addActionListener(this);
		pilotPictureButton.setBackground(bg);
		pilotPictureButton.setOpaque(false);
		pilotPictureButton.setBorderPainted(false);
		pilotPictureButton.setActionCommand(changePilotPictureAction);
        ToolTipManager.setToolTip(pilotPictureButton, "Change pilot picture");

		return pilotPictureButton;
	}

    private PWCGJButton makePlanePicture(Date date) throws PWCGException 
    {
        // Find the best plane in the squadron and use it for the picture
        PlaneType bestPlane = null;
        if (squad != null)
        {
            bestPlane = squad.determineBestPlane(date);
        }
        
        String planePic = null;
        if (bestPlane != null)
        {
            planePic = bestPlane.getType();
        }

        Color bg = ColorMap.WOOD_BACKGROUND;
        
        // Picture
        ImageIcon imageIcon = null;  
        try 
        {
            if (planePic != null)
            {
                String picPath = PictureManager.getPlanePicturePath(planePic);          
                imageIcon = ImageIconCache.getInstance().getImageIconResized(picPath);
            }
        }
        catch (Exception ex) 
        {
            PWCGLogger.logException(ex);
        }

        PWCGJButton planeSkinsButton = null;
        if (imageIcon != null)
        {
            planeSkinsButton = new PWCGJButton(imageIcon);
        }
        else
        {
            planeSkinsButton = new PWCGJButton("");
        }
        planeSkinsButton.addActionListener(this);
        planeSkinsButton.setBackground(bg);
        planeSkinsButton.setOpaque(false);
        planeSkinsButton.setBorderPainted(false);
        String actionCommand =  changeSkinAction + pilot.getSerialNumber();
        planeSkinsButton.setActionCommand(actionCommand);
        ToolTipManager.setToolTip(planeSkinsButton, "Assign aircraft skins to this pilot");

        return planeSkinsButton;
    }

	private JPanel makeMedalBox() throws PWCGException 
	{
		Color bg = ColorMap.WOOD_BACKGROUND;
		
		// Picture button
		// Set up the picture
		JPanel pilotPMedalBoxPanel = new JPanel(new BorderLayout());
		pilotPMedalBoxPanel.setOpaque(false);

        String imagePath = ContextSpecificImages.imagesMisc() + "PilotMedalBox.gif";
		ImageIcon imageIcon = ImageIconCache.getInstance().getImageIcon(imagePath);
		//ImageIcon imageIcon = ImageCache.getInstance().getRotatedImageIcon(imagePath, 350);
		PWCGJButton medalBoxButton = new PWCGJButton(imageIcon);


		medalBoxButton.setBackground(bg);
		medalBoxButton.setOpaque(false);
		medalBoxButton.setBorderPainted(false);
		String actionCommand =  openMedalBoxAction + pilot.getSerialNumber();
		medalBoxButton.setActionCommand(actionCommand);
		medalBoxButton.addActionListener(this);
        ToolTipManager.setToolTip(medalBoxButton, "View pilot medals");

		pilotPMedalBoxPanel.add(medalBoxButton, BorderLayout.CENTER);
		
		return pilotPMedalBoxPanel;
	}

    private void viewPilotLog(String action) throws PWCGException 
    {
        SoundManager.getInstance().playSound("BookOpen.WAV");

        int index = action.indexOf(":");
        String pilotSerialNumberString = action.substring(index + 1);
        Integer serialNumber = Integer.valueOf(pilotSerialNumberString);

        SquadronMember pilot = campaign.getPersonnelManager().getAnyCampaignMember(serialNumber);
        if (pilot == null)
        {
            pilot = campaign.getPersonnelManager().getCampaignAces().retrieveAceBySerialNumber(serialNumber);
        }
        
        if (pilot != null)
        {
            CampaignPilotLogPanel pilotLogPanel = new CampaignPilotLogPanel(campaign, pilot);
            pilotLogPanel.makePanels();
            
            CampaignGuiContextManager.getInstance().pushToContextStack(pilotLogPanel);
        }
    }

    private void openMedalBox(String action) throws PWCGException 
    {
        SquadronMember pilot = UIUtils.getPilotFromAction(campaign, action);
        if (pilot != null)
        {
            CampaignPilotMedalPanel pilotMedalPanel = new CampaignPilotMedalPanel(campaign, pilot);
            pilotMedalPanel.makePanels();
            
            CampaignGuiContextManager.getInstance().pushToContextStack(pilotMedalPanel);
        }
    }

	public void actionPerformed(ActionEvent ae)
	{
		try
		{
			String action = ae.getActionCommand();
			
			if (action.startsWith("View Pilot Log"))
            {
                viewPilotLog(action);
            }
			else if (action.equalsIgnoreCase("Change Picture"))
			{
		        ICountry country = CountryFactory.makeCountryByCountry(pilot.getCountry());
				String countryName = country.getNationality();
		        String picPath = ContextSpecificImages.imagesPilotPictures() + countryName;
				File picDir = new File(picPath);

				CampaignChoosePilotPicGUI picChooser = new CampaignChoosePilotPicGUI(picDir);
				
				ImagePreviewPanel preview = new ImagePreviewPanel();
				picChooser.setAccessory(preview);
				picChooser.addPropertyChangeListener(preview);
				
			    int returnVal = picChooser.showOpenDialog(null);
				
			    if (returnVal == JFileChooser.APPROVE_OPTION) 
			    {
					String name = picChooser.getSelectedFile().getName();
					pilot.setPicName(name);
			    }
			    
                makePanels();
                
                this.add(BorderLayout.CENTER, centerPanel);
                CampaignGuiContextManager.getInstance().refreshCurrentContext(this);
			}
            else if (action.startsWith("Open Medal Box"))
            {
                openMedalBox(action);
            }
            else if (action.startsWith("PilotFinished"))
            {
                parent.createCampaignHomeContext();
                CampaignGuiContextManager.getInstance().popFromContextStack();
            }
			
		}
		catch (Exception e)
		{
			PWCGLogger.logException(e);
			ErrorDialog.internalError(e.getMessage());
		}
	}
}
