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
import pwcg.gui.ScreenIdentifier;
import pwcg.gui.UiImageResolver;
import pwcg.gui.campaign.home.CampaignHomeScreen;
import pwcg.gui.colors.ColorMap;
import pwcg.gui.dialogs.ErrorDialog;
import pwcg.gui.dialogs.PWCGMonitorFonts;
import pwcg.gui.image.ImageCache;
import pwcg.gui.image.ImageIconCache;
import pwcg.gui.sound.SoundManager;
import pwcg.gui.utils.ContextSpecificImages;
import pwcg.gui.utils.ImagePreviewPanel;
import pwcg.gui.utils.ImageResizingPanel;
import pwcg.gui.utils.ImageScaledPanel;
import pwcg.gui.utils.PWCGButtonFactory;
import pwcg.gui.utils.PWCGButtonNoBackground;
import pwcg.gui.utils.PWCGJButton;
import pwcg.gui.utils.PWCGLabelFactory;
import pwcg.gui.utils.ToolTipManager;
import pwcg.gui.utils.UIUtils;

public class CampaignPilotScreen extends ImageResizingPanel implements ActionListener
{
    private static final long serialVersionUID = 1L;

    private SquadronMember pilot;
	private Squadron squad;
    private Campaign campaign;
    private CampaignHomeScreen parent;

    protected String changePilotPictureAction = "";
    protected String changeSkinAction = "";
	protected String openMedalBoxAction = "";
    protected String openLogBookAction = "";
    protected JPanel centerPanel;

    public CampaignPilotScreen(Campaign campaign, Squadron squad, SquadronMember pilot, CampaignHomeScreen parent)
    {
        super();
        this.setLayout(new BorderLayout());

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
        String imagePath = UiImageResolver.getImage(ScreenIdentifier.CampaignPilotScreen);
        this.setThemedImageFromName(campaign, imagePath);

        this.add(BorderLayout.WEST, makenavigationPanel());
        centerPanel = makeCenterPanel();
	    this.add(BorderLayout.CENTER, centerPanel);
	}

	public JPanel makeCenterPanel() throws PWCGException 
	{	
        JPanel campaignPilotPanel = new JPanel(new BorderLayout());
        campaignPilotPanel.setOpaque(false);

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
        JPanel pilotDesktopNavPanel = new JPanel(new BorderLayout());
        pilotDesktopNavPanel.setOpaque(false);

        JPanel buttonPanel = new JPanel(new GridLayout(0,1));
        buttonPanel.setOpaque(false);
        
        JButton finishedButton = PWCGButtonFactory.makeTranslucentMenuButton("Finished", "PilotFinished", "Finished viewing pilot", this);
        buttonPanel.add(finishedButton);

        pilotDesktopNavPanel.add(buttonPanel, BorderLayout.NORTH);
        
        return pilotDesktopNavPanel;
    }

	private JPanel makePilotLog() throws PWCGException 
	{
		JPanel pilotLogPanel = new JPanel(new BorderLayout());
		pilotLogPanel.setOpaque(false);

        String imagePath = ContextSpecificImages.imagesMisc() + "PilotLogBook.png";
        ImageScaledPanel pilotLogBorderPanel = new ImageScaledPanel(pilot.determineService(campaign.getDate()), imagePath, 0.75);
		pilotLogBorderPanel.setLayout(new BorderLayout());
		pilotLogBorderPanel.setOpaque(false);

		Font font = PWCGMonitorFonts.getPilotLogBookFont();

		Color bg = ColorMap.WOOD_BACKGROUND;
		Color fg = ColorMap.MAP_LOG_FOREGROUND;
		Color fadedFG = new Color(fg.getRed(),fg.getGreen(),fg.getBlue(),180);
		
		JPanel pilotLogBookGrid = new JPanel (new GridLayout(0, 2));
		pilotLogBookGrid.setOpaque(false);

		for (int i = 0; i < 1; ++i)
		{
	        for (int j = 0; j < 2; ++j)
	        {
    			pilotLogBookGrid.add(PWCGLabelFactory.makeDummyLabel());
	        }
		}
		
		JButton logButton = new PWCGButtonNoBackground("");
		logButton.setHorizontalAlignment(SwingConstants.LEFT);
		logButton.setForeground(fadedFG);
		logButton.setBackground(bg);
		logButton.setOpaque(false);
		logButton.setBorderPainted(false);
		logButton.setFocusPainted(false);
		logButton.setFont(font);
		logButton.setText("<html>               <br />               <br />               <br />               <br />               <br />               <br />               <br />               </html>");;
		String actionCommand = openLogBookAction + pilot.getSerialNumber();
		logButton.addActionListener(this);
		logButton.setActionCommand(actionCommand);
        ToolTipManager.setToolTip(logButton, "View pilot log book");

        pilotLogBookGrid.add(logButton);

		pilotLogBorderPanel.add(pilotLogBookGrid, BorderLayout.NORTH);

		pilotLogPanel.add(pilotLogBorderPanel, BorderLayout.CENTER);
        
		return pilotLogPanel;
	}

    private JPanel makePicturePanel() throws PWCGException 
    {
        JPanel pilotPicPanel = new JPanel(new BorderLayout());
        pilotPicPanel.setOpaque(false);
        
        PWCGJButton planeButton = makePlanePicture(campaign.getDate());
        PWCGJButton pilotButton = makePilotPicture();
        
        pilotPicPanel.add(pilotButton, BorderLayout.WEST);
        pilotPicPanel.add(planeButton, BorderLayout.SOUTH);
        
        return pilotPicPanel;
    }

	private PWCGJButton makePilotPicture() throws PWCGException 
	{
		Color bg = ColorMap.WOOD_BACKGROUND;
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

		PWCGJButton pilotPictureButton = PWCGButtonFactory.makeImageButton(imageIcon, bg,  changePilotPictureAction, "Press to set picture",  "Change pilot picture", this);

		return pilotPictureButton;
	}

    private PWCGJButton makePlanePicture(Date date) throws PWCGException 
    {
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

        String commandText =  changeSkinAction + pilot.getSerialNumber();
        PWCGJButton planeSkinsButton = PWCGButtonFactory.makeImageButton(imageIcon, bg,  commandText, "Press to assign skins",  "Assign aircraft skins to this pilot", this);

        return planeSkinsButton;
    }

	private JPanel makeMedalBox() throws PWCGException 
	{
		Color bg = ColorMap.WOOD_BACKGROUND;

		JPanel pilotPMedalBoxPanel = new JPanel(new BorderLayout());
		pilotPMedalBoxPanel.setOpaque(false);

        String imagePath = ContextSpecificImages.imagesMisc() + "PilotMedalBox.png";
		ImageIcon imageIcon = ImageIconCache.getInstance().getImageIcon(imagePath);
        String commandText =  openMedalBoxAction + pilot.getSerialNumber();
		PWCGJButton medalBoxButton = PWCGButtonFactory.makeImageButton(imageIcon, bg,  commandText, "View pilot medals",  "View pilot medals", this);

		pilotPMedalBoxPanel.add(medalBoxButton, BorderLayout.CENTER);
		return pilotPMedalBoxPanel;
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
                changePilotPicture();
            }
            else if (action.startsWith("Open Medal Box"))
            {
                openMedalBox(action);
            }
            else if (action.startsWith("PilotFinished"))
            {
                parent.refreshInformation();
                CampaignGuiContextManager.getInstance().popFromContextStack();
            }
            
        }
        catch (Exception e)
        {
            PWCGLogger.logException(e);
            ErrorDialog.internalError(e.getMessage());
        }
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
            CampaignPilotLogScreen pilotLogPanel = new CampaignPilotLogScreen(campaign, pilot);
            pilotLogPanel.makePanels();
            
            CampaignGuiContextManager.getInstance().pushToContextStack(pilotLogPanel);
        }
    }

    private void openMedalBox(String action) throws PWCGException 
    {
        SquadronMember pilot = UIUtils.getPilotFromAction(campaign, action);
        if (pilot != null)
        {
            CampaignMedalScreen pilotMedalPanel = new CampaignMedalScreen(campaign, pilot);
            pilotMedalPanel.makePanels();
            CampaignGuiContextManager.getInstance().pushToContextStack(pilotMedalPanel);
        }
    }

    private void changePilotPicture() throws PWCGException
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
}
