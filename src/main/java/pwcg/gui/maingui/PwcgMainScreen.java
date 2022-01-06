package pwcg.gui.maingui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import pwcg.campaign.Campaign;
import pwcg.campaign.CampaignMode;
import pwcg.campaign.api.ICountry;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGDirectorySimulatorManager;
import pwcg.campaign.crewmember.CrewMember;
import pwcg.campaign.factory.CountryFactory;
import pwcg.campaign.utils.PlanesOwnedManager;
import pwcg.core.config.ConfigItemKeys;
import pwcg.core.config.ConfigManagerGlobal;
import pwcg.core.config.InternationalizationManager;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.PWCGLogger;
import pwcg.core.utils.ProductSetupFileReader;
import pwcg.gui.CampaignGuiContextManager;
import pwcg.gui.PwcgThreePanelUI;
import pwcg.gui.ScreenIdentifier;
import pwcg.gui.UiImageResolver;
import pwcg.gui.campaign.home.CampaignHomeScreen;
import pwcg.gui.colors.ColorMap;
import pwcg.gui.dialogs.ErrorDialog;
import pwcg.gui.dialogs.PWCGMonitorFonts;
import pwcg.gui.maingui.campaigngenerate.CampaignGeneratorScreen;
import pwcg.gui.maingui.config.PwcgGlobalConfigurationScreen;
import pwcg.gui.maingui.config.PwcgPlanesOwnedConfigurationScreen;
import pwcg.gui.maingui.config.PwcgSkinConfigurationAnalysisScreen;
import pwcg.gui.maingui.coop.PwcgCoopGlobalAdminScreen;
import pwcg.gui.rofmap.editmap.EditorMapGUI;
import pwcg.gui.rofmap.infoMap.InfoMapGUI;
import pwcg.gui.sound.MusicManager;
import pwcg.gui.sound.SoundManager;
import pwcg.gui.utils.ImageButton;
import pwcg.gui.utils.ImageResizingPanel;
import pwcg.gui.utils.PWCGButtonFactory;
import pwcg.gui.utils.PWCGFrame;
import pwcg.gui.utils.PWCGJButton;
import pwcg.gui.utils.PWCGLabelFactory;
import pwcg.gui.utils.ToolTipManager;

public class PwcgMainScreen extends ImageResizingPanel implements ActionListener
{
	private static final long serialVersionUID = 1L;
    private static final String VERSION = "13.8.0";

    private PwcgThreePanelUI pwcgThreePanel;
	private List<JButton> campaignButtonList = new ArrayList<JButton>();

	public PwcgMainScreen() 
	{
	    super("");
		setLayout(new BorderLayout());
		this.setBackground(Color.DARK_GRAY);
		
		this.pwcgThreePanel = new PwcgThreePanelUI(this);
	}

	public void makePanels()
	{
        try
        {
            startMusic();

            makeFrame();
            makeGUI();
            refresh();

            setButtonsEnabled();
            validateInstallDirectory();
            verifyLoggingEnabled();
        }
        catch (Exception e)
        {
            PWCGLogger.logException(e);
        }
	}


    private void startMusic()
    {
        try
        {
            //SoundManager.getInstance().play("Song001.WAV");
        }
        catch (Exception e)
        {
            PWCGLogger.logException(e);
        }
    }
    private void makeFrame() throws PWCGException
    {
        PWCGFrame.getInstance();
        PWCGContext.getInstance().initializeMap();               
    }

	public void refresh() 
	{
		try
		{
		    PWCGContext.getInstance().setCampaign(null);
            PWCGContext.getInstance().initializeMap();    

			setButtonsEnabled();

			PWCGContext.getInstance().setCampaign(null);
			pwcgThreePanel.setRightPanel(makeCampaignPanel());
            
            CampaignGuiContextManager.getInstance().clearContextStack();
            CampaignGuiContextManager.getInstance().pushToContextStack(this);
		}
		catch (Exception e)
		{
			PWCGLogger.logException(e);
		}		
	}

	public void makeGUI()
	{
		try
		{
            String imagePath = UiImageResolver.getImage(ScreenIdentifier.PwcgMainScreen);
	        this.setImageFromName(imagePath);
			
            pwcgThreePanel.setLeftPanel(makeLeftPanel());
            pwcgThreePanel.setCenterPanel(makeCenterPanel());
			pwcgThreePanel.setRightPanel(makeCampaignPanel());
            CampaignGuiContextManager.getInstance().pushToContextStack(this);

			setButtonsEnabled();
		}
		catch (Exception e)
		{
			PWCGLogger.logException(e);
		}		
	}

	private void setButtonsEnabled()
	{
		if (PlanesOwnedManager.getInstance().hasPlanesOwned())
		{
			for (JButton campaignButton : campaignButtonList)
			{
				campaignButton.setEnabled(true);
			}
		}
		else
		{
			for (JButton campaignButton : campaignButtonList)
			{
				if (campaignButton.getActionCommand().equals("Planes Owned") || 
			        campaignButton.getActionCommand().equals("Exit"))
				{
					campaignButton.setEnabled(true);
				}
				else
				{
					campaignButton.setEnabled(false);
				}
			}
		}
	}

	private void validateInstallDirectory()
	{
		String missionFilepath = PWCGDirectorySimulatorManager.getInstance().getSimulatorRootDir() + "Data\\Missions";
		try
		{
			File file = new File(missionFilepath);
			if (!file.exists())
			{
                ErrorDialog.userError("PWCGCampaign is installed to the wrong directory.  It should be installed to the game root directory");
			}
		}
		catch (Exception e)
		{
            PWCGLogger.logException(e);
            ErrorDialog.userError("Error during install validation");
		}
	}

	public JPanel makeCenterPanel()  
	{
		JPanel mainCenterPanel = new JPanel();
		mainCenterPanel.setLayout(new BorderLayout());
		mainCenterPanel.setOpaque(false);
		return mainCenterPanel;
	}

	public JPanel makeLeftPanel() throws PWCGException  
	{
	    JPanel mainLeftPanel = new JPanel();
	    mainLeftPanel.setLayout(new BorderLayout());
	    mainLeftPanel.setOpaque(false);

		JPanel versionPanel = makeVersionPanel();
		mainLeftPanel.add(versionPanel, BorderLayout.NORTH);

		JPanel buttonPanel = new JPanel(new BorderLayout());
		buttonPanel.setOpaque(false);
		
		JPanel buttonPanelGrid = new JPanel(new GridLayout(0,1));
		buttonPanelGrid.setOpaque(false);
		
		makeMenuButtons(buttonPanelGrid) ;
		
		buttonPanel.add(buttonPanelGrid, BorderLayout.NORTH);
		
		mainLeftPanel.add(buttonPanel, BorderLayout.CENTER);

		return mainLeftPanel;
	}
 
	private void makeMenuButtons(JPanel buttonPanel) throws PWCGException 
	{
		buttonPanel.add(PWCGLabelFactory.makeDummyLabel());
				
 		makeMenuButton (InternationalizationManager.getTranslation("Planes Owned"), "Planes Owned", buttonPanel);
        makeMenuButton (InternationalizationManager.getTranslation("Configuration"), "Configuration",buttonPanel);
        makeMenuButton (InternationalizationManager.getTranslation("Music"), "Music", buttonPanel);
        makeMenuButton (InternationalizationManager.getTranslation("Skin Analysis"), "Skin Analysis", buttonPanel);
        makeMenuButton (InternationalizationManager.getTranslation("PWCG Information"), "PWCG Information", buttonPanel);
        makeMenuButton (InternationalizationManager.getTranslation("Administer Coop"), "Administer Coop", buttonPanel);
        
        int showFrontLineEditor = ConfigManagerGlobal.getInstance().getIntConfigParam(ConfigItemKeys.ShowFrontLineEditorKey);
        if (showFrontLineEditor == 1)
        {
            makeMenuButton (InternationalizationManager.getTranslation("PWCG Edit Front"), "PWCG Edit Front", buttonPanel);
        }
        
        buttonPanel.add(PWCGLabelFactory.makeDummyLabel());
        buttonPanel.add(PWCGLabelFactory.makeDummyLabel());
        makeMenuButton (InternationalizationManager.getTranslation("Exit"), "Exit", buttonPanel);
	}

    private JButton makeMenuButton(String buttonText, String commandText, JPanel buttonPanel) throws PWCGException 
    {
        JButton button = PWCGButtonFactory.makeTranslucentMenuButtonGrayMenu(buttonText, commandText, "", this);

        buttonPanel.add(button);
        campaignButtonList.add(button);

        return button;
    }
    
	public JPanel makeVersionPanel() throws PWCGException  
	{
		Font font = PWCGMonitorFonts.getPrimaryFontLarge();

        String versionText = InternationalizationManager.getTranslation("PWCG Version") + ": " + VERSION;
        JLabel lversion = PWCGLabelFactory.makeTransparentLabel(versionText, ColorMap.WOOD_FOREGROUND, font, SwingConstants.RIGHT);

        JPanel versionPanel = new JPanel ();
        versionPanel.setLayout(new GridLayout(0,1));
        versionPanel.setOpaque(false);
        versionPanel.add(PWCGLabelFactory.makeDummyLabel());
        versionPanel.add(lversion);
		versionPanel.add(PWCGLabelFactory.makeDummyLabel());
				
		return versionPanel;
	}

	public JPanel makeCampaignPanel() throws PWCGException 
	{
		MusicManager.playTitleTheme();

        JPanel campaignPanel = new JPanel();
        campaignPanel.setLayout(new BorderLayout());
        campaignPanel.setOpaque(false);
		
		JPanel campaignListPanel = new JPanel (new GridLayout(0,1));
		campaignListPanel.setOpaque(false);
		
        JLabel assignedLabel = PWCGLabelFactory.makeMenuLabelLarge(InternationalizationManager.getTranslation("Available Campaigns")); 
		campaignListPanel.add(assignedLabel);
		
		List<String> campaigns = Campaign.getCampaignNames();
		for (String campaignName : campaigns)
		{
			String icon = "";
			try
			{
				Campaign campaign = new Campaign();
                if (campaign.open(campaignName))              
				{
    				if (!campaign.isCampaignActive())
    				{
    					icon = "RIP.jpg";
    				}
                    else if (campaign.getCampaignData().getCampaignMode() == CampaignMode.CAMPAIGN_MODE_COOP)
                    {
                        icon = "CoopIcon.jpg";
                    }
    				else
    				{
    				    String nation = determineCampaignCountryForIcon(campaign).getNationality();
    					icon = nation + "CrewMember.jpg";
    				}
    
    				PWCGJButton button = ImageButton.makeCampaignFlagButton(campaignName, icon);
    				button.setActionCommand("Load Campaign:" + campaignName);
    				button.addActionListener(this);
    				campaignListPanel.add(button);
    				
    				ToolTipManager.setToolTip(button, campaign.getCampaignDescription());
    				
    				campaignButtonList.add(button);
				}
			}
			catch (Exception e)
			{
				PWCGLogger.logException(e);
			}
			
		}
		
        PWCGJButton newButton = ImageButton.makeCampaignFlagButton(InternationalizationManager.getTranslation("New"), "NewCrewMember.jpg");
        newButton.setActionCommand("New Campaign");
        newButton.addActionListener(this );
        ToolTipManager.setToolTip(newButton, "Create a new campaign");
        campaignListPanel.add(newButton);
        campaignButtonList.add(newButton);

        PWCGJButton deleteCampaignButton = ImageButton.makeCampaignFlagButton(InternationalizationManager.getTranslation("Delete"), "DeleteCrewMember.jpg");
        deleteCampaignButton.setActionCommand("Delete Campaign");
        deleteCampaignButton.addActionListener(this );
        ToolTipManager.setToolTip(deleteCampaignButton, "Delete campaigns");
        campaignListPanel.add(deleteCampaignButton);
        campaignButtonList.add(deleteCampaignButton);

		campaignPanel.add(BorderLayout.NORTH, campaignListPanel);

		return campaignPanel;
	}
	
    
    private ICountry determineCampaignCountryForIcon(Campaign campaign) throws PWCGException
    {
        if (campaign.isCoop())
        {
            return CountryFactory.makeNeutralCountry();
        }
        else
        {
            CrewMember referencePlayer = campaign.findReferencePlayer();
            return CountryFactory.makeCountryByCountry(referencePlayer.getCountry());
        }
     }

	private void loadCampaign(String campaignName) throws PWCGException 
	{
		Campaign campaign = new Campaign();
		campaign.open(campaignName);	
		PWCGContext.getInstance().setCampaign(campaign);

		CampaignHomeScreen campaignGUI = new CampaignHomeScreen (this, campaign);
        CampaignGuiContextManager.getInstance().pushToContextStack(campaignGUI);

		return;
	}

	public void actionPerformed(ActionEvent ae)
	{
		try
		{
			String action = ae.getActionCommand();

			if (action.equals("Exit"))
			{
				System.exit(0);
			}
			else if (action.equals("Skin Analysis"))
            {
                showSkinAnalysis();
            }
            else if (action.equals("Planes Owned"))
            {
                showPlanesOwned();
            }
            else if (action.equals("Administer Coop"))
            {
                showCoopAdmin();
            }
            else if (action.equals("Configuration"))
            {
                showGlobalConfig();
            }
            else if (action.equals("New Campaign"))
            {
                CampaignGeneratorScreen campaignNewGUI = new CampaignGeneratorScreen(this);
                campaignNewGUI.makePanels();
                CampaignGuiContextManager.getInstance().pushToContextStack(campaignNewGUI);
            }
            else if (action.equals("Music"))
            {
            	PwcgMusicConfigScreen campaignMusicPanelSet = new PwcgMusicConfigScreen(this);
            	campaignMusicPanelSet.makePanels();
                CampaignGuiContextManager.getInstance().pushToContextStack(campaignMusicPanelSet);
            }
            else if (action.equals("Delete Campaign"))
            {
                CampaignDeleteScreen campaignDeleteGUI = new CampaignDeleteScreen(this);
                campaignDeleteGUI.makePanels();
                CampaignGuiContextManager.getInstance().pushToContextStack(campaignDeleteGUI);
            }
			else if (action.contains("Load Campaign:"))
			{
				int index = action.indexOf(":");
				String campaignName = action.substring(index+1);
				loadCampaign(campaignName);
				return;
			}
            else if (action.contains("PWCG Information"))
            {
                showPWCGInfoMap();
                return;
            }
            else if (action.contains("PWCG Edit Front"))
            {
                showPWCGEditMap();
                return;
            }

		}
		catch (Exception e)
		{
			PWCGLogger.logException(e);
			ErrorDialog.internalError(e.getMessage());
		}
	}

    private void showPlanesOwned() throws PWCGException 
    {
        SoundManager.getInstance().playSound("BookOpen.WAV");

        PwcgPlanesOwnedConfigurationScreen planesOwned = new PwcgPlanesOwnedConfigurationScreen(this);
        planesOwned.makePanels();

        CampaignGuiContextManager.getInstance().pushToContextStack(planesOwned);
    }

    private void showCoopAdmin() throws PWCGException 
    {
        SoundManager.getInstance().playSound("BookOpen.WAV");

        PwcgCoopGlobalAdminScreen coopAdmin = new PwcgCoopGlobalAdminScreen();
        coopAdmin.makePanels();

        CampaignGuiContextManager.getInstance().pushToContextStack(coopAdmin);
    }

    private void showSkinAnalysis() throws PWCGException 
    {
        SoundManager.getInstance().playSound("BookOpen.WAV");

        PwcgSkinConfigurationAnalysisScreen skinAnalysis = new PwcgSkinConfigurationAnalysisScreen(this);
        skinAnalysis.makePanels();

        CampaignGuiContextManager.getInstance().pushToContextStack(skinAnalysis);
    }

    private void showGlobalConfig() throws PWCGException 
    {
        SoundManager.getInstance().playSound("BookOpen.WAV");

        PwcgGlobalConfigurationScreen globalConfig = new PwcgGlobalConfigurationScreen();
        globalConfig.makePanels();

        CampaignGuiContextManager.getInstance().pushToContextStack(globalConfig);
    }

    private void showPWCGInfoMap() throws PWCGException 
    {
        SoundManager.getInstance().playSound("BookOpen.WAV");
        
        Date mapDate = PWCGContext.getInstance().getCurrentMap().getFrontDatesForMap().getEarliestMapDate();
        InfoMapGUI infoMapGUI = new InfoMapGUI(mapDate);
        infoMapGUI.makeGUI();

        CampaignGuiContextManager.getInstance().pushToContextStack(infoMapGUI);
    }

    private void showPWCGEditMap() throws PWCGException 
    {
        Date mapDate = PWCGContext.getInstance().getCurrentMap().getFrontDatesForMap().getEarliestMapDate();
        EditorMapGUI editMapGUI = new EditorMapGUI(mapDate);
        editMapGUI.makeGUI();

        CampaignGuiContextManager.getInstance().pushToContextStack(editMapGUI);
    }
    

    private void verifyLoggingEnabled()
    {
        ProductSetupFileReader missionLogFileValidator = new ProductSetupFileReader();
        missionLogFileValidator.analyzeStartupCfg();
        
        if (!missionLogFileValidator.isMissionLoggingEnabled())
        {
            ErrorDialog.userError(
                    "Mission logging is not enabled.  Before flying the mission open <game install dir>\\Data\\Startup.cfg and set mission_text_log = 1");
        }
        
        if (!missionLogFileValidator.getMissionLogPath().isEmpty())
        {
            PWCGContext.getInstance().setMissionLogDirectory(missionLogFileValidator.getMissionLogPath());
        }
    }
}
