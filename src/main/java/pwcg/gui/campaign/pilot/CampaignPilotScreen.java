package pwcg.gui.campaign.pilot;

import java.awt.BorderLayout;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Date;

import javax.swing.ImageIcon;
import javafx.scene.control.Button;
import javax.swing.JFileChooser;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
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
import pwcg.gui.utils.ButtonFactory;
import pwcg.gui.utils.ButtonNoBackground;
import pwcg.gui.utils.Button;
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
    protected Pane centerPanel;

    public CampaignPilotScreen(Campaign campaign, Squadron squad, SquadronMember pilot, CampaignHomeScreen parent)
    {
        super("");
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
        this.setImageFromName(imagePath);

        this.add(BorderLayout.WEST, makenavigationPanel());
        centerPanel = makeCenterPanel();
	    this.add(BorderLayout.CENTER, centerPanel);
	}

	public Pane makeCenterPanel() throws PWCGException 
	{	
        Pane campaignPilotPanel = new Pane(new BorderLayout());
        campaignPilotPanel.setOpaque(false);

		Pane pilotLogPanel = makePilotLog();
        Pane picturePanel = makePicturePanel();
		Pane pilotMedalBoxPanel = makeMedalBox();
		
        campaignPilotPanel.add(pilotMedalBoxPanel,BorderLayout.NORTH);
        campaignPilotPanel.add(picturePanel,BorderLayout.WEST);
		campaignPilotPanel.add(pilotLogPanel,BorderLayout.CENTER);
				
		return campaignPilotPanel;
	}	

    private Pane makenavigationPanel() throws PWCGException  
    {
        Pane pilotDesktopNavPanel = new Pane(new BorderLayout());
        pilotDesktopNavPanel.setOpaque(false);

        Pane buttonPanel = new Pane(new GridLayout(0,1));
        buttonPanel.setOpaque(false);
        
        Button finishedButton = ButtonFactory.makeTranslucentMenuButton("Finished", "PilotFinished", "Finished viewing pilot", this);
        buttonPanel.add(finishedButton);

        pilotDesktopNavPanel.add(buttonPanel, BorderLayout.NORTH);
        
        return pilotDesktopNavPanel;
    }

	private Pane makePilotLog() throws PWCGException 
	{
		Pane pilotLogPanel = new Pane(new BorderLayout());
		pilotLogPanel.setOpaque(false);

		String logSpacer = "          ";

        String imagePath = ContextSpecificImages.imagesMisc() + "PilotLogBook.png";
        ImageScaledPanel pilotLogBorderPanel = new ImageScaledPanel(imagePath, 0.75);
		pilotLogBorderPanel.setLayout(new BorderLayout());
		pilotLogBorderPanel.setOpaque(false);

		Font font = PWCGMonitorFonts.getPilotLogBookFont();

		Color bg = ColorMap.WOOD_BACKGROUND;
		Color fg = ColorMap.MAP_LOG_FOREGROUND;
		Color fadedFG = new Color(fg.getRed(),fg.getGreen(),fg.getBlue(),180);
		
		Pane pilotLogBookGrid = new Pane (new GridLayout(0, 2));
		pilotLogBookGrid.setOpaque(false);

		for (int i = 0; i < 1; ++i)
		{
	        for (int j = 0; j < 2; ++j)
	        {
    			Label lSpacer = new Label(logSpacer);
    			lSpacer.setBackground(bg);
    			lSpacer.setOpaque(false);
    			lSpacer.setFont(font);
    			pilotLogBookGrid.add(lSpacer);
	        }
		}
		
		Button logButton = new ButtonNoBackground("");
		logButton.setAlignment(SwingConstants.LEFT);
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

    private Pane makePicturePanel() throws PWCGException 
    {
        Pane pilotPicPanel = new Pane(new BorderLayout());
        pilotPicPanel.setOpaque(false);
        
        Button planeButton = makePlanePicture(campaign.getDate());
        Button pilotButton = makePilotPicture();
        
        pilotPicPanel.add(pilotButton, BorderLayout.WEST);
        pilotPicPanel.add(planeButton, BorderLayout.SOUTH);
        
        return pilotPicPanel;
    }

	private Button makePilotPicture() throws PWCGException 
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

		Button pilotPictureButton = null;
		if (imageIcon != null)
		{
			pilotPictureButton = new Button(imageIcon);
		}
		else
		{
			pilotPictureButton = new Button("press to set picture");
		}
		pilotPictureButton.addActionListener(this);
		pilotPictureButton.setBackground(bg);
		pilotPictureButton.setOpaque(false);
		pilotPictureButton.setBorderPainted(false);
		pilotPictureButton.setFocusPainted(false);
		pilotPictureButton.setActionCommand(changePilotPictureAction);
        ToolTipManager.setToolTip(pilotPictureButton, "Change pilot picture");

		return pilotPictureButton;
	}

    private Button makePlanePicture(Date date) throws PWCGException 
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

        Button planeSkinsButton = null;
        if (imageIcon != null)
        {
            planeSkinsButton = new Button(imageIcon);
        }
        else
        {
            planeSkinsButton = new Button("");
        }
        planeSkinsButton.addActionListener(this);
        planeSkinsButton.setBackground(bg);
        planeSkinsButton.setOpaque(false);
        planeSkinsButton.setBorderPainted(false);
        planeSkinsButton.setFocusPainted(false);
        String actionCommand =  changeSkinAction + pilot.getSerialNumber();
        planeSkinsButton.setActionCommand(actionCommand);
        ToolTipManager.setToolTip(planeSkinsButton, "Assign aircraft skins to this pilot");

        return planeSkinsButton;
    }

	private Pane makeMedalBox() throws PWCGException 
	{
		Color bg = ColorMap.WOOD_BACKGROUND;

		Pane pilotPMedalBoxPanel = new Pane(new BorderLayout());
		pilotPMedalBoxPanel.setOpaque(false);

        String imagePath = ContextSpecificImages.imagesMisc() + "PilotMedalBox.png";
		ImageIcon imageIcon = ImageIconCache.getInstance().getImageIcon(imagePath);
		Button medalBoxButton = new Button(imageIcon);


		medalBoxButton.setBackground(bg);
		medalBoxButton.setOpaque(false);
		medalBoxButton.setBorderPainted(false);
		medalBoxButton.setFocusPainted(false);
		String actionCommand =  openMedalBoxAction + pilot.getSerialNumber();
		medalBoxButton.setActionCommand(actionCommand);
		medalBoxButton.addActionListener(this);
        ToolTipManager.setToolTip(medalBoxButton, "View pilot medals");

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
            CampaignMedalScreen pilotMedalPanel = new CampaignMedalScreen(pilot);
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
