package pwcg.gui.maingui;


import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.swing.SwingConstants;

import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import pwcg.campaign.Campaign;
import pwcg.campaign.CampaignMode;
import pwcg.campaign.api.ICountry;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGDirectorySimulatorManager;
import pwcg.campaign.factory.CountryFactory;
import pwcg.campaign.squadmember.SquadronMember;
import pwcg.campaign.utils.PlanesOwnedManager;
import pwcg.core.config.ConfigItemKeys;
import pwcg.core.config.ConfigManagerGlobal;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.MissionLogFileValidator;
import pwcg.core.utils.PWCGLogger;
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
import pwcg.gui.maingui.config.PwcgIconicMissionGenerationScreen;
import pwcg.gui.maingui.config.PwcgPlanesOwnedConfigurationScreen;
import pwcg.gui.maingui.config.PwcgSkinConfigurationAnalysisScreen;
import pwcg.gui.maingui.coop.PwcgCoopGlobalAdminScreen;
import pwcg.gui.rofmap.editmap.EditorMapGUI;
import pwcg.gui.rofmap.infoMap.InfoMapGUI;
import pwcg.gui.sound.MusicManager;
import pwcg.gui.sound.SoundManager;
import pwcg.gui.utils.ImageButton;
import pwcg.gui.utils.ButtonFactory;
import pwcg.gui.utils.PWCGFrame;
import pwcg.gui.utils.ToolTipManager;

public class PwcgMainScreen extends BorderPane
{
    private static final String VERSION = "   PWCG Version 13.1.0";

	private List<Button> campaignButtonList = new ArrayList<>();

	public PwcgMainScreen() 
	{
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
			this.setRight(makeCampaignPanel());
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
		    // JAVAFX set image background
//            String imagePath = UiImageResolver.getImage(ScreenIdentifier.PwcgMainScreen);
//	        this.setImageFromName(imagePath);
			
	        this.setLeft(makeLeftPanel());
            this.setCenter(makeCenterPanel());
            this.setRight(makeCampaignPanel());

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
			for (Button campaignButton : campaignButtonList)
			{
				campaignButton.setDisable(false);
			}
		}
		else
		{
			for (Button campaignButton : campaignButtonList)
			{
				if (campaignButton.getText().equals("Planes Owned") || 
			        campaignButton.getText().equals("Exit"))
				{
					campaignButton.setDisable(false);
				}
				else
				{
					campaignButton.setDisable(true);
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

	public Pane makeCenterPanel()  
	{
		BorderPane mainCenterPanel = new BorderPane();
		return mainCenterPanel;
	}

	public Pane makeLeftPanel() throws PWCGException  
	{
	    BorderPane mainLeftPanel = new BorderPane();

		Pane versionPanel = makeVersionPanel();
		mainLeftPanel.setTop(versionPanel);

		BorderPane buttonPanel = new BorderPane();
		
		GridPane buttonPanelGrid = new GridPane();
		makeMenuButtons(buttonPanelGrid) ;
		
		buttonPanel.setTop(buttonPanelGrid);
		mainLeftPanel.setCenter(buttonPanel);

		return mainLeftPanel;
	}

	private void makeMenuButtons(GridPane buttonPanel) throws PWCGException 
	{
		Label spacer = new Label("");
		buttonPanel.add(spacer, 0, 1);
		
 		makeMenuButton ("Planes Owned", "Planes Owned", buttonPanel);
        makeMenuButton ("Configuration", "Configuration",buttonPanel);
        makeMenuButton ("Music", "Music", buttonPanel);
        makeMenuButton ("Skin Analysis", "Skin Analysis", buttonPanel);
        makeMenuButton ("PWCG Information", "PWCG Information", buttonPanel);
        makeMenuButton ("Administer Coop", "Administer Coop", buttonPanel);
        makeMenuButton ("Iconic Missions", "Iconic Missions", buttonPanel);
        
        int showFrontLineEditor = ConfigManagerGlobal.getInstance().getIntConfigParam(ConfigItemKeys.ShowFrontLineEditorKey);
        if (showFrontLineEditor == 1)
        {
            makeMenuButton ("PWCG Edit Front", "PWCG Edit Front", buttonPanel);
        }
        
        makePlainLabel ("", buttonPanel);
        makePlainLabel ("", buttonPanel);
        makeMenuButton ("Exit", "Exit", buttonPanel);
	}

    private Button makeMenuButton(String buttonText, String commandText, Pane buttonPanel) throws PWCGException 
    {
        Button button = ButtonFactory.makeTranslucentMenuButtonGrayMenu(buttonText, commandText, "", this);

        buttonPanel.add(button);
        campaignButtonList.add(button);

        return button;
    }

    private Label makePlainLabel(String labelText, Pane buttonPanel) throws PWCGException 
    {
        Label label = ButtonFactory.makeMenuLabelLarge(labelText);
        buttonPanel.add(label);

        return label;
    }
    
	public Pane makeVersionPanel() throws PWCGException  
	{

		Font font = PWCGMonitorFonts.getPrimaryFontLarge();

		Color lbg = ColorMap.WOOD_BACKGROUND;
		Color fg = ColorMap.WOOD_FOREGROUND;

		Pane versionPanel = new Pane ();
		versionPanel.setLayout(new GridLayout(0,1));
		versionPanel.setOpaque(false);

		Label spacer = new Label("    ", Label.LEFT);
		versionPanel.add(spacer);

		Label lversion = new Label(VERSION, Label.LEFT);
		lversion.setBackground(lbg);
		lversion.setForeground(fg);
		lversion.setOpaque(false);
		lversion.setFont(font);
        versionPanel.add(lversion);

		Label spacer2 = new Label("    ", Label.LEFT);
		versionPanel.add(spacer2);
				
		return versionPanel;
	}

	public Pane makeCampaignPanel() throws PWCGException 
	{
		MusicManager.playTitleTheme();

        Pane campaignPanel = new Pane();
        campaignPanel.setLayout(new BorderLayout());
        campaignPanel.setOpaque(false);

		Color buttonBG = ColorMap.PAPER_BACKGROUND;
		Color buttonFG = ColorMap.CHALK_FOREGROUND;

		Font font = PWCGMonitorFonts.getPrimaryFontLarge();
		
		Pane campaignListPanel = new Pane (new GridLayout(0,1));
		campaignListPanel.setOpaque(false);
		
        Label assignedLabel = ButtonFactory.makeMenuLabelLarge("  Available Campaigns:"); 
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
    					icon = nation + "Pilot.jpg";
    				}
    
    				Button button = ImageButton.makeButton(campaignName, icon);
    	
    				button.setBackground(buttonBG);
    				button.setForeground(buttonFG);
    				button.setOpaque(false);
    				button.setFont(font);
    				button.setAlignment(SwingConstants.LEFT);
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
		
        Button newButton = ImageButton.makeButton("New", "NewPilot.jpg");

        newButton.setBackground(buttonBG);
        newButton.setForeground(buttonFG);
        newButton.setOpaque(false);
        newButton.setFont(font);
        newButton.setAlignment(SwingConstants.LEFT);
        newButton.setActionCommand("New Campaign");
        newButton.addActionListener(this );
        ToolTipManager.setToolTip(newButton, "Create a new campaign");
        campaignListPanel.add(newButton);
        campaignButtonList.add(newButton);

        Button deleteCampaignButton = ImageButton.makeButton("Delete", "DeletePilot.jpg");
        deleteCampaignButton.setBackground(buttonBG);
        deleteCampaignButton.setForeground(buttonFG);
        deleteCampaignButton.setOpaque(false);
        deleteCampaignButton.setFont(font);
        deleteCampaignButton.setAlignment(SwingConstants.LEFT);
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
            SquadronMember referencePlayer = campaign.findReferencePlayer();
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
            else if (action.equals("Iconic Missions"))
            {
                showIconicMissions();
            }
            else if (action.equals("Configuration"))
            {
                showGlobalConfig();
            }
            else if (action.equals("New Campaign"))
            {
                CampaignGeneratorScreen campaignNewGUI = new CampaignGeneratorScreen(this);
                campaignNewGUI.makePanels();
                // JAVAFX show new panel
            }
            else if (action.equals("Music"))
            {
            	PwcgMusicConfigScreen campaignMusicPanelSet = new PwcgMusicConfigScreen(this);
            	campaignMusicPanelSet.makePanels();
                // JAVAFX show new panel
            }
            else if (action.equals("Delete Campaign"))
            {
                CampaignDeleteScreen campaignDeleteGUI = new CampaignDeleteScreen(this);
                campaignDeleteGUI.makePanels();
                // JAVAFX show new panel
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

        // JAVAFX show new panel
    }

    private void showCoopAdmin() throws PWCGException 
    {
        SoundManager.getInstance().playSound("BookOpen.WAV");

        PwcgCoopGlobalAdminScreen coopAdmin = new PwcgCoopGlobalAdminScreen();
        coopAdmin.makePanels();

        // JAVAFX show new panel
    }

    private void showIconicMissions() throws PWCGException
    {
        SoundManager.getInstance().playSound("Stapler.WAV");

        PwcgIconicMissionGenerationScreen iconicMissionsScreen = new PwcgIconicMissionGenerationScreen();
        iconicMissionsScreen.makePanels();

        // JAVAFX show new panel
    }

    private void showSkinAnalysis() throws PWCGException 
    {
        SoundManager.getInstance().playSound("BookOpen.WAV");

        PwcgSkinConfigurationAnalysisScreen skinAnalysis = new PwcgSkinConfigurationAnalysisScreen(this);
        skinAnalysis.makePanels();

        // JAVAFX show new panel
    }

    private void showGlobalConfig() throws PWCGException 
    {
        SoundManager.getInstance().playSound("BookOpen.WAV");

        PwcgGlobalConfigurationScreen globalConfig = new PwcgGlobalConfigurationScreen();
        globalConfig.makePanels();

        // JAVAFX show new panel
    }

    private void showPWCGInfoMap() throws PWCGException 
    {
        SoundManager.getInstance().playSound("BookOpen.WAV");
        
        Date mapDate = PWCGContext.getInstance().getCurrentMap().getFrontDatesForMap().getEarliestMapDate();
        InfoMapGUI infoMapGUI = new InfoMapGUI(mapDate);
        infoMapGUI.makeGUI();

        // JAVAFX show new panel
    }

    private void showPWCGEditMap() throws PWCGException 
    {
        Date mapDate = PWCGContext.getInstance().getCurrentMap().getFrontDatesForMap().getEarliestMapDate();
        EditorMapGUI editMapGUI = new EditorMapGUI(mapDate);
        editMapGUI.makeGUI();

        // JAVAFX show new panel
    }
    

    private void verifyLoggingEnabled()
    {
        MissionLogFileValidator missionLogFileValidator = new MissionLogFileValidator();
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
