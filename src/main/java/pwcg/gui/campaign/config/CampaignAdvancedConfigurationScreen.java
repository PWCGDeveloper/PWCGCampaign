package pwcg.gui.campaign.config;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import pwcg.campaign.context.PWCGContext;
import pwcg.core.config.ConfigManagerCampaign;
import pwcg.core.config.ConfigSet;
import pwcg.core.config.ConfigSetKeys;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.PWCGLogger;
import pwcg.gui.CampaignGuiContextManager;
import pwcg.gui.PwcgThreePanelUI;
import pwcg.gui.ScreenIdentifier;
import pwcg.gui.UiImageResolver;
import pwcg.gui.campaign.home.CampaignHomeContext;
import pwcg.gui.colors.ColorMap;
import pwcg.gui.config.ConfigurationParametersGUI;
import pwcg.gui.dialogs.ErrorDialog;
import pwcg.gui.dialogs.PWCGMonitorFonts;
import pwcg.gui.utils.ImageResizingPanel;
import pwcg.gui.utils.PWCGButtonFactory;
import pwcg.gui.utils.PWCGLabelFactory;
import pwcg.gui.utils.PwcgBorderFactory;
import pwcg.gui.utils.SpacerPanelFactory;

public class CampaignAdvancedConfigurationScreen extends ImageResizingPanel implements ActionListener
{
    private static final long serialVersionUID = 1L;
    
    private Map<String, ConfigurationParametersGUI> configurationGUIs = new HashMap<String, ConfigurationParametersGUI>();
    private PwcgThreePanelUI pwcgThreePanel;
	private ButtonGroup buttonGroup = new ButtonGroup();

	public CampaignAdvancedConfigurationScreen()
	{
        super();
        this.setLayout(new BorderLayout());
        this.setOpaque(false);

	    this.pwcgThreePanel = new PwcgThreePanelUI(this);
	}
	
	public void makePanels() throws PWCGException 
	{
        String imagePath = UiImageResolver.getImage(ScreenIdentifier.CampaignAdvancedConfigurationScreen);
        this.setThemedImageFromName(CampaignHomeContext.getCampaign().getReferenceService(), imagePath);

        pwcgThreePanel.setLeftPanel(makeNavigatePanel());
        pwcgThreePanel.setCenterPanel(makeBlankCenterPanel());
        pwcgThreePanel.setRightPanel(makeCategoryPanel());
	}

    private JPanel makeNavigatePanel() throws PWCGException
    {
        JPanel advancedConfigAcceptPanel = new JPanel(new BorderLayout());
        advancedConfigAcceptPanel.setOpaque(false);

        JPanel buttonPanel = new JPanel(new GridLayout(0,1));
        buttonPanel.setOpaque(false);

        buttonPanel.add(PWCGLabelFactory.makeDummyLabel());

        JButton acceptButton = PWCGButtonFactory.makeTranslucentMenuButton("Accept Config Changes", "Accept", "Save and Leave", this);
        buttonPanel.add(acceptButton);

        buttonPanel.add(PWCGLabelFactory.makeDummyLabel());

        JButton cancelButton = PWCGButtonFactory.makeTranslucentMenuButton("Cancel Config Changes", "Cancel", "Leave without saving", this);
        buttonPanel.add(cancelButton);
        
        advancedConfigAcceptPanel.add(buttonPanel, BorderLayout.NORTH);

        return advancedConfigAcceptPanel;
    }

	public JPanel makeBlankCenterPanel() throws PWCGException 
	{		
        String imagePath = UiImageResolver.getImage(ScreenIdentifier.Document);
        ImageResizingPanel blankPanel = new ImageResizingPanel(CampaignHomeContext.getCampaign().getReferenceService(), imagePath);
		blankPanel.setBorder(PwcgBorderFactory.createStandardDocumentBorder());
		blankPanel.setLayout(new BorderLayout());
		return blankPanel;
	}

	public JPanel makeCategoryPanel() throws PWCGException  
	{
        JPanel configSelectionPanel = new JPanel(new BorderLayout());
        configSelectionPanel.setOpaque(false);
        JPanel buttonPanel = new JPanel(new GridLayout(0,1));
        buttonPanel.setOpaque(false);

        JLabel label = PWCGLabelFactory.makeMenuLabelLarge("Advanced Configuration Categories");
        buttonPanel.add(label);
        
        buttonPanel.add(makeButton("Campaign Preferences", "Fine tune campaign event probabilities"));
        buttonPanel.add(makeButton("Flight", "Set AI preferences"));
        buttonPanel.add(makeButton("Mission AI", "Set AI preferences"));
        buttonPanel.add(makeButton("Mission Ground Objects", "Set density of ground objects"));
        buttonPanel.add(makeButton("Fighter Mission Types", "Set odds of flying different kinds of fighter missions"));
        buttonPanel.add(makeButton("Ground Attack Mission Types", "Set odds of flying different kinds of ground attack missions"));
        buttonPanel.add(makeButton("Bomber Mission Types", "Set odds of flying different kinds of bombing missions"));
        buttonPanel.add(makeButton("Recon Mission Types", "Set odds of flying different kinds of recon missions"));
        buttonPanel.add(makeButton("Transport Mission Types", "Set odds of flying different kinds of transport missions"));
        buttonPanel.add(makeButton("Target Types", "Set odds of attacking different target types"));
        buttonPanel.add(makeButton("Aircraft Numbers", "How many planes are in the sky during a mission"));
        buttonPanel.add(makeButton("Mission Limits", "Set items that may affect mission performance"));
        buttonPanel.add(makeButton("Weather", "Set weather preferences"));
        
        add (buttonPanel);

        configSelectionPanel.add(buttonPanel, BorderLayout.NORTH);

        JPanel spacePanel = SpacerPanelFactory.makeDocumentSpacerPanel(2000);
        
        JPanel configPanel = new JPanel(new BorderLayout());
        configPanel.setOpaque(false);
        configPanel.add(configSelectionPanel, BorderLayout.CENTER);
        configPanel.add(spacePanel, BorderLayout.WEST);

        return configPanel;
	}

	private JRadioButton makeButton(String buttonText, String toolTipText) throws PWCGException
	{
        Color fgColor = ColorMap.CHALK_FOREGROUND;
        Font font = PWCGMonitorFonts.getPrimaryFont();
        String commandText = "Configuration Parameters: " + buttonText;

        JRadioButton button = PWCGButtonFactory.makeRadioButton(buttonText, commandText, toolTipText, font, fgColor, false, this);
		buttonGroup.add(button);

		return button;
	}

	ConfigurationParametersGUI createConfigPanel(String action) throws PWCGException 
	{
	    ConfigManagerCampaign configManager = CampaignHomeContext.getCampaign().getCampaignConfigManager();

		ConfigSet configSet = null;

		if (action.contains("Flight"))
		{
			configSet = configManager.getMergedConfigSet(ConfigSetKeys.ConfigSetFlight);				
		}
		else if (action.contains("Mission AI"))
		{
			configSet = configManager.getMergedConfigSet(ConfigSetKeys.ConfigSetMissionAi);				
		}
		else if (action.contains("Mission Ground Objects"))
		{
			configSet = configManager.getMergedConfigSet(ConfigSetKeys.ConfigSetGroundObjects);				
		}
        else if (action.contains("Fighter Mission Types"))
        {
            configSet = configManager.getMergedConfigSet(ConfigSetKeys.ConfigSetFighterMission);                
        }
        else if (action.contains("Ground Attack Mission Types"))
        {
            configSet = configManager.getMergedConfigSet(ConfigSetKeys.ConfigSetGroundAttackMission);                
        }		
        else if (action.contains("Bomber Mission Types"))
        {
            configSet = configManager.getMergedConfigSet(ConfigSetKeys.ConfigSetBomberMission);                
        }
        else if (action.contains("Recon Mission Types"))
        {
            configSet = configManager.getMergedConfigSet(ConfigSetKeys.ConfigSetReconMission);                
        }
        else if (action.contains("Transport Mission Types"))
        {
            configSet = configManager.getMergedConfigSet(ConfigSetKeys.ConfigSetTransportMission);                
        }
        else if (action.contains("Target Types"))
        {
            configSet = configManager.getMergedConfigSet(ConfigSetKeys.ConfigSetTarget);                
        }
		else if (action.contains("Aircraft Numbers"))
		{
			configSet = configManager.getMergedConfigSet(ConfigSetKeys.ConfigSetAircraftNumbers);				
		}
		else if (action.contains("Mission Limits"))
		{
			configSet = configManager.getMergedConfigSet(ConfigSetKeys.ConfigSetMissionLimits);				
		}
        else if (action.contains("Campaign Preferences"))
        {
			configSet = configManager.getMergedConfigSet(ConfigSetKeys.ConfigSetUserPrefCampaign);				
        }
        else if (action.contains("Weather"))
        {
			configSet = configManager.getMergedConfigSet(ConfigSetKeys.ConfigSetWeather);				
        }
		
		ConfigurationParametersGUI currentConfig = new ConfigurationParametersGUI (this, configManager, configSet);
		currentConfig.makeGUI();
		
		return currentConfig;
	}

	public void actionPerformed(ActionEvent ae)
	{
		try
		{
			String action = ae.getActionCommand();
			if (action.equalsIgnoreCase("Accept"))
			{
				for (ConfigurationParametersGUI configUI : configurationGUIs.values())
				{
					configUI.recordChanges();
				}
				
				CampaignHomeContext.getCampaign().getCampaignConfigManager().write();
				PWCGContext.getInstance().configurePwcgMaps();
		        CampaignGuiContextManager.getInstance().popFromContextStack();
				return;
			}
			else if (action.equalsIgnoreCase("Cancel"))
			{
			    CampaignHomeContext.getCampaign().getCampaignConfigManager().readConfig();
		        CampaignGuiContextManager.getInstance().popFromContextStack();
				return;
			}
			else if (action.contains("Configuration Parameters"))
			{
				ConfigurationParametersGUI newConfig = null;
				if (configurationGUIs.containsKey(action))
				{
					newConfig = configurationGUIs.get(action);
				}
				else
				{
					newConfig = createConfigPanel(action);
					configurationGUIs.put(action, newConfig);
				}

                pwcgThreePanel.setCenterPanel(newConfig);
			}
		}
		catch (Throwable e)
		{
			PWCGLogger.logException(e);
			ErrorDialog.internalError(e.getMessage());
		}
	}
}
