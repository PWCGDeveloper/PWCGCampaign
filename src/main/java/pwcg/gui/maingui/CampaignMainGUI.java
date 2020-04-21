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
import javax.swing.UIManager;

import pwcg.campaign.Campaign;
import pwcg.campaign.CampaignMode;
import pwcg.campaign.api.ICountry;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGProduct;
import pwcg.campaign.factory.CountryFactory;
import pwcg.campaign.squadmember.SquadronMember;
import pwcg.campaign.utils.AutoStart;
import pwcg.campaign.utils.PlanesOwnedManager;
import pwcg.campaign.utils.TestDriver;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.PWCGLogger;
import pwcg.core.utils.PWCGLogger.LogLevel;
import pwcg.gui.CampaignGuiContextManager;
import pwcg.gui.PwcgGuiContext;
import pwcg.gui.campaign.home.CampaignHomeGUI;
import pwcg.gui.colors.ColorMap;
import pwcg.gui.dialogs.ErrorDialog;
import pwcg.gui.dialogs.MonitorSupport;
import pwcg.gui.maingui.campaigngenerate.CampaignGeneratorPanelSet;
import pwcg.gui.maingui.config.ConfigurationGlobalGUI;
import pwcg.gui.maingui.config.ConfigurationPlanesOwnedPanelSet;
import pwcg.gui.maingui.config.ConfigurationSkinAnalysisPanelSet;
import pwcg.gui.maingui.coop.CoopAdminGui;
import pwcg.gui.rofmap.MapPanelBase;
import pwcg.gui.rofmap.brief.PwcgGuiModSupport;
import pwcg.gui.rofmap.editmap.EditorMapGUI;
import pwcg.gui.rofmap.infoMap.InfoMapGUI;
import pwcg.gui.sound.MusicManager;
import pwcg.gui.sound.SoundManager;
import pwcg.gui.utils.ContextSpecificImages;
import pwcg.gui.utils.ImageButton;
import pwcg.gui.utils.ImageResizingPanel;
import pwcg.gui.utils.PWCGButtonFactory;
import pwcg.gui.utils.PWCGFrame;
import pwcg.gui.utils.PWCGJButton;
import pwcg.gui.utils.ToolTipManager;

public class CampaignMainGUI extends PwcgGuiContext implements ActionListener
{
	private static final long serialVersionUID = 1L;
    private static final String VERSION = "   PWCG Version 8.7.0";

	private List<JButton> campaignButtonList = new ArrayList<JButton>();
	
	private boolean displayFrontLineEditor = true;

	public static void main(String[] args) 
	{
        CampaignMainGUI pwcg = new CampaignMainGUI();
        pwcg.startPWCG(args);
	}

	public CampaignMainGUI() 
	{
		setLayout(new BorderLayout());
		
		try
		{
		    //SoundManager.getInstance().play("Song001.WAV");
		}
		catch (Exception e)
		{
			PWCGLogger.logException(e);
		}
	}
	

	private void startPWCG(String[] args)
	{
        try
        {
            cleanup();
            evaluateArgs(args);
            
            setupUI();

            if (PwcgGuiModSupport.isRunningDebrief())
            {
                initialize();
                startAtDebrief();
            }
            else
            {
                PWCGFrame.getInstance().setPanel(this);
                initialize();
            }
            
            boolean validInstall = validateInstallDirectory();
            if (!validInstall)
            {
                ErrorDialog.userError("PWCGCampaign is installed to the wrong directory.  It should be installed to the game root directory");
            }
            
            TestDriver testDriver = TestDriver.getInstance();
            if (testDriver.isEnabled())
            {
                ErrorDialog.userError("PWCG test driver is enabled - PWCG will not function normally");
            }
            
            MapPanelBase.preloadMaps();
        }
        catch (Exception e)
        {
            PWCGLogger.logException(e);
        }
	}

    private void cleanup()
    {
        File badFile = new File("BoSData\\Input\\Vehicles\\pziv-h1.json");
        if (badFile.exists())
        {
            badFile.delete();
        }
        
    }

    private void evaluateArgs(String[] args) throws PWCGException
    {
        boolean runningFromGuiMod = false;
        
        // If we are running from the GUI mod then it is RoF - find the entry point
        for (String arg: args)
        {
            if (arg.contains("integrated"))
            {
                PWCGContext.setProduct(PWCGProduct.FC);
                PwcgGuiModSupport.setRunningIntegrated(true);
                runningFromGuiMod = true;
            }
            else if (arg.contains("debrief"))
            {
                PWCGContext.setProduct(PWCGProduct.FC);
                PwcgGuiModSupport.setRunningDebrief(true);
                runningFromGuiMod = true;
            }
        }

        // If we are not running from the GUI mod then look fo RoF or BoS args
        if (!runningFromGuiMod)
        {
            if (args.length > 0)
            {
                if (args[0].equals("BoS"))
                {
                    PWCGContext.setProduct(PWCGProduct.BOS);
                    PWCGLogger.log(LogLevel.INFO, "Running BoS");
                }
                else if (args[0].equals("FC"))
                {
                    PWCGContext.setProduct(PWCGProduct.FC);
                    PWCGLogger.log(LogLevel.INFO, "Running FC");
                }
            }
        }
        
        
        // Kick off initialization
        PWCGContext.getInstance();
    }

    private void setupUI() throws PWCGException
    {
        Color tabSelectedColor = ColorMap.PAPER_BACKGROUND;
        UIManager.put("TabbedPane.selected", tabSelectedColor);

        PWCGFrame.getInstance();

        PWCGContext.getInstance().initializeMap();               
        makeGUI();
    }

	private void initialize() 
	{
		try
		{
			// On the main screen we will always baseline from the main map for the product
            PWCGContext.getInstance().initializeMap();    
		}
		catch (Exception e)
		{
			PWCGLogger.logException(e);
		}		
	}

	public void refresh() 
	{
		try
		{
		    CampaignGuiContextManager.getInstance().clearContextStack();
		    CampaignGuiContextManager.getInstance().pushToContextStack(this);

            // On the main screen we will always baseline from the main map for the product
		    PWCGContext.getInstance().setCampaign(null);
            PWCGContext.getInstance().initializeMap();    

			setButtonsEnabled();

			PWCGContext.getInstance().setCampaign(null);
			
            JPanel campaignPanel = makeCampaignPanel();
	        CampaignGuiContextManager.getInstance().changeCurrentContext(null, null, campaignPanel);
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
			setLeftPanel(makeLeftPanel());
			setRightPanel(makeCampaignPanel());
			setCenterPanel(makeCenterPanel());

			setButtonsEnabled();

			CampaignGuiContextManager.getInstance().clearContextStack();
            CampaignGuiContextManager.getInstance().pushToContextStack(this);
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

	private boolean validateInstallDirectory()
	{
		String missionFilepath = PWCGContext.getInstance().getDirectoryManager().getSimulatorDataDir() + "Missions";
		try
		{
			File file = new File(missionFilepath);
			if (!file.exists())
			{
				return false;
			}
		}
		catch (Exception e)
		{
			return false;
		}


		return true;
	}

	public JPanel makeCenterPanel()  
	{
		String imagePath = ContextSpecificImages.menuPathMain() + "Main.jpg";

		ImageResizingPanel mainCenterPanel = new ImageResizingPanel(imagePath);

		return mainCenterPanel;
	}

	public JPanel makeLeftPanel() throws PWCGException  
	{
        String imagePath = getSideImageMain("MainLeft.jpg");
        
		ImageResizingPanel configPanel = new ImageResizingPanel(imagePath);
		configPanel.setLayout(new BorderLayout());

		JPanel versionPanel = makeVersionPanel();
		configPanel.add(versionPanel, BorderLayout.NORTH);

		JPanel buttonPanel = new JPanel(new BorderLayout());
		buttonPanel.setOpaque(false);
		
		JPanel buttonPanelGrid = new JPanel(new GridLayout(0,1));
		buttonPanelGrid.setOpaque(false);
		
		makeMenuButtons(buttonPanelGrid) ;
		
		buttonPanel.add(buttonPanelGrid, BorderLayout.NORTH);
		
		configPanel.add(buttonPanel, BorderLayout.CENTER);

		return configPanel;
	}

	private void makeMenuButtons(JPanel buttonPanel) throws PWCGException 
	{
		JLabel spacer = new JLabel("");
		buttonPanel.add(spacer);
		
 		makeMenuButton ("Planes Owned", "Planes Owned", buttonPanel);
        makeMenuButton ("Configuration", "Configuration",buttonPanel);
        makeMenuButton ("Music", "Music", buttonPanel);
        makeMenuButton ("Skin Analysis", "Skin Analysis", buttonPanel);
        makeMenuButton ("PWCG Information", "PWCG Information", buttonPanel);
        makeMenuButton ("Administer Coop", "Administer Coop", buttonPanel);
        
        if (displayFrontLineEditor)
        {
            makeMenuButton ("PWCG Edit Front", "PWCG Edit Front", buttonPanel);
        }
        
        makePlainLabel ("", buttonPanel);
        makePlainLabel ("", buttonPanel);
        makeMenuButton ("Exit", "Exit", buttonPanel);
	}

    private JButton makeMenuButton(String buttonText, String commandText, JPanel buttonPanel) throws PWCGException 
    {
        JButton button = PWCGButtonFactory.makeMenuButton(buttonText, commandText, this);
        
        buttonPanel.add(button);
        campaignButtonList.add(button);

        return button;
    }

    private JLabel makePlainLabel(String labelText, JPanel buttonPanel) throws PWCGException 
    {
        JLabel label = PWCGButtonFactory.makeMenuLabelLarge(labelText);
        buttonPanel.add(label);

        return label;
    }
    
	public JPanel makeVersionPanel() throws PWCGException  
	{

		Font font = MonitorSupport.getPrimaryFontLarge();

		Color lbg = ColorMap.WOOD_BACKGROUND;
		Color fg = ColorMap.WOOD_FOREGROUND;

		JPanel versionPanel = new JPanel ();
		versionPanel.setLayout(new GridLayout(0,1));
		versionPanel.setOpaque(false);

		JLabel spacer = new JLabel("    ", JLabel.LEFT);
		versionPanel.add(spacer);

		JLabel lversion = new JLabel(VERSION, JLabel.LEFT);
		lversion.setBackground(lbg);
		lversion.setForeground(fg);
		lversion.setOpaque(false);
		lversion.setFont(font);
        versionPanel.add(lversion);

		JLabel spacer2 = new JLabel("    ", JLabel.LEFT);
		versionPanel.add(spacer2);
				
		return versionPanel;
	}

	public JPanel makeCampaignPanel() throws PWCGException 
	{
		MusicManager.playTitleTheme();

        String imagePath = getSideImageMain("Enlist.jpg");

		JPanel campaignPanel = new ImageResizingPanel(imagePath);
		campaignPanel.setLayout( new BorderLayout());

		Color buttonBG = ColorMap.PAPER_BACKGROUND;
		Color buttonFG = ColorMap.CHALK_FOREGROUND;

		Font font = MonitorSupport.getPrimaryFontLarge();
		
		JPanel campaignListPanel = new JPanel (new GridLayout(0,1));
		campaignListPanel.setOpaque(false);
		
        JLabel assignedLabel = PWCGButtonFactory.makeMenuLabelLarge("  Available Campaigns:"); 
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
                    else if (campaign.getCampaignData().getCampaignMode() == CampaignMode.CAMPAIGN_MODE_COMPETITIVE)
                    {
                        icon = "CoopCompIcon.jpg";
                    }
    				else
    				{
    				    String nation = determineCampaignCountryForIcon(campaign).getNationality();
    					icon = nation + "Pilot.jpg";
    				}
    
    				PWCGJButton button = ImageButton.makeButton(campaignName, icon);
    	
    				button.setBackground(buttonBG);
    				button.setForeground(buttonFG);
    				button.setOpaque(false);
    				button.setFont(font);
    				button.setHorizontalAlignment(SwingConstants.LEFT);
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
		
        PWCGJButton newButton = ImageButton.makeButton("New", "NewPilot.jpg");

        newButton.setBackground(buttonBG);
        newButton.setForeground(buttonFG);
        newButton.setOpaque(false);
        newButton.setFont(font);
        newButton.setHorizontalAlignment(SwingConstants.LEFT);
        newButton.setActionCommand("New Campaign");
        newButton.addActionListener(this );
        ToolTipManager.setToolTip(newButton, "Create a new campaign");
        campaignListPanel.add(newButton);
        campaignButtonList.add(newButton);

        PWCGJButton deleteCampaignButton = ImageButton.makeButton("Delete", "DeletePilot.jpg");
        deleteCampaignButton.setBackground(buttonBG);
        deleteCampaignButton.setForeground(buttonFG);
        deleteCampaignButton.setOpaque(false);
        deleteCampaignButton.setFont(font);
        deleteCampaignButton.setHorizontalAlignment(SwingConstants.LEFT);
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

	public void startAtDebrief() throws PWCGException 
	{
		AutoStart autoStartFile = new AutoStart();
		autoStartFile.read();
		loadCampaign(autoStartFile.getCampaignName());
	}

	private void loadCampaign(String campaignName) throws PWCGException 
	{
		Campaign campaign = new Campaign();
		campaign.open(campaignName);	
		PWCGContext.getInstance().setCampaign(campaign);

		CampaignHomeGUI campaignGUI = new CampaignHomeGUI (this, campaign);
		PWCGFrame.getInstance().setPanel(campaignGUI);

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
                showGlobalConfigOwned();
            }
            else if (action.equals("New Campaign"))
            {
                CampaignGeneratorPanelSet campaignNewGUI = new CampaignGeneratorPanelSet(this);
                campaignNewGUI.makePanels();
                CampaignGuiContextManager.getInstance().pushToContextStack(campaignNewGUI);
            }
            else if (action.equals("Music"))
            {
            	CampaignMusicPanelSet campaignMusicPanelSet = new CampaignMusicPanelSet(this);
            	campaignMusicPanelSet.makePanels();
                CampaignGuiContextManager.getInstance().pushToContextStack(campaignMusicPanelSet);
            }
            else if (action.equals("Delete Campaign"))
            {
                CampaignDeletePanelSet campaignDeleteGUI = new CampaignDeletePanelSet(this);
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

        ConfigurationPlanesOwnedPanelSet planesOwned = new ConfigurationPlanesOwnedPanelSet(this);
        planesOwned.makePanels();

        CampaignGuiContextManager.getInstance().pushToContextStack(planesOwned);
    }

    private void showCoopAdmin() throws PWCGException 
    {
        SoundManager.getInstance().playSound("BookOpen.WAV");

        CoopAdminGui coopAdmin = new CoopAdminGui();
        coopAdmin.makePanels();

        CampaignGuiContextManager.getInstance().pushToContextStack(coopAdmin);
    }
    
    private void showSkinAnalysis() throws PWCGException 
    {
        SoundManager.getInstance().playSound("BookOpen.WAV");

        ConfigurationSkinAnalysisPanelSet skinAnalysis = new ConfigurationSkinAnalysisPanelSet(this);
        skinAnalysis.makePanels();

        CampaignGuiContextManager.getInstance().pushToContextStack(skinAnalysis);
    }

    private void showGlobalConfigOwned() throws PWCGException 
    {
        SoundManager.getInstance().playSound("BookOpen.WAV");

        ConfigurationGlobalGUI globalConfig = new ConfigurationGlobalGUI();
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
}
