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
import javax.swing.SwingConstants;

import pwcg.campaign.Campaign;
import pwcg.campaign.context.PWCGContextManager;
import pwcg.core.config.ConfigManagerCampaign;
import pwcg.core.config.ConfigSet;
import pwcg.core.config.ConfigSetKeys;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.Logger;
import pwcg.gui.CampaignGuiContextManager;
import pwcg.gui.PwcgGuiContext;
import pwcg.gui.colors.ColorMap;
import pwcg.gui.config.ConfigurationParametersGUI;
import pwcg.gui.dialogs.ErrorDialog;
import pwcg.gui.dialogs.MonitorSupport;
import pwcg.gui.utils.ContextSpecificImages;
import pwcg.gui.utils.ImageResizingPanel;
import pwcg.gui.utils.PWCGButtonFactory;
import pwcg.gui.utils.ToolTipManager;

public class CampaignConfigurationAdvancedGUI extends PwcgGuiContext implements ActionListener
{
	private static final long serialVersionUID = 1L;
	private Map<String, ConfigurationParametersGUI> configurationGUIs = new HashMap<String, ConfigurationParametersGUI>();
	private ButtonGroup buttonGroup = new ButtonGroup();
	private ConfigManagerCampaign configManager;

	public CampaignConfigurationAdvancedGUI(Campaign campaign)
	{
	    super();
        configManager = campaign.getCampaignConfigManager();
	}
	
	public void makePanels() 
	{
		try
		{
	        setRightPanel(makeCategoryPanel());
	        setCenterPanel(makeCenterPanel());
	        setLeftPanel(makeNavigatePanel());
		}
		catch (Throwable e)
		{
			Logger.logException(e);
			ErrorDialog.internalError(e.getMessage());
		}
	}

    private JPanel makeNavigatePanel() throws PWCGException
    {
        String imagePath = getSideImage("AdvancedConfigCampaignLeft.jpg");

        ImageResizingPanel simpleConfigAcceptPanel = new ImageResizingPanel(imagePath);
        simpleConfigAcceptPanel.setLayout(new BorderLayout());
        simpleConfigAcceptPanel.setOpaque(false);

        JPanel buttonPanel = new JPanel(new GridLayout(0,1));
        buttonPanel.setOpaque(false);

        JLabel spacerLabel = makeLabel("");        
        buttonPanel.add(spacerLabel);

        JButton acceptButton = PWCGButtonFactory.makeMenuButton("Accept Config Changes", "Accept", this);
        buttonPanel.add(acceptButton);

        JLabel spacer2 = new JLabel("   ");
        spacer2.setOpaque(false);
        buttonPanel.add(spacer2);

        JButton cancelButton = PWCGButtonFactory.makeMenuButton("Cancel Config Changes", "Cancel", this);
        buttonPanel.add(cancelButton);
        
        simpleConfigAcceptPanel.add(buttonPanel, BorderLayout.NORTH);

        return simpleConfigAcceptPanel;
    }

	public JPanel makeCenterPanel() 
	{		
        String imagePath = ContextSpecificImages.imagesMisc() + "Paper.jpg";
		ImageResizingPanel blankPanel = new ImageResizingPanel(imagePath);
		blankPanel.setLayout(new BorderLayout());
		
		add(blankPanel, BorderLayout.CENTER);
		
		return blankPanel;
	}

	public JPanel makeCategoryPanel() throws PWCGException  
	{
        String imagePath = null;
        if (PWCGContextManager.getInstance().getCampaign() != null)
        {
            imagePath = getSideImage("AdvancedConfigCampaignRight.jpg");
        }
        else
        {
            imagePath = getSideImage("ConfigLeft.jpg");
         }
        		
        ImageResizingPanel configPanel = new ImageResizingPanel(imagePath);
		configPanel.setLayout(new BorderLayout());

		try
		{
			JPanel buttonPanel = new JPanel(new GridLayout(0,1));
			buttonPanel.setOpaque(false);

	        JLabel label = PWCGButtonFactory.makeMenuLabelLarge("Advanced Configuration Categories:");
			buttonPanel.add(label);
			
			buttonPanel.add(makeButton("Campaign Preferences", "Fine tune campaign event probabilities"));
			buttonPanel.add(makeButton("Flight", "Set AI preferences"));
			buttonPanel.add(makeButton("Mission AI", "Set AI preferences"));
			buttonPanel.add(makeButton("Mission Ground Objects", "Set density of ground objects"));
			buttonPanel.add(makeButton("Fighter Mission Types", "Set odds of flying different kinds of fighter missions"));
			buttonPanel.add(makeButton("Aircraft Numbers", "How many planes are in the sky during a mission"));
			buttonPanel.add(makeButton("Mission Limits", "Set items that may affect mission performance"));
            buttonPanel.add(makeButton("Mission Spacing", "Set how often you fly"));
            buttonPanel.add(makeButton("Weather", "Set weather preferences"));
			
			add (buttonPanel);

			configPanel.add(buttonPanel, BorderLayout.NORTH);
			
		} 
		catch (Exception e)
		{
			Logger.logException(e);
			ErrorDialog.internalError(e.getMessage());
		}

		return configPanel;
	}

	private JLabel makeLabel(String labelText) throws PWCGException
	{
		Color fgColor = ColorMap.CHALK_FOREGROUND;

		Font font = MonitorSupport.getPrimaryFontLarge();

		JLabel label = new JLabel(labelText);
		label.setHorizontalAlignment(SwingConstants.LEFT );
		label.setOpaque(false);
		label.setForeground(fgColor);
		label.setFont(font);

		return label;
	}

	private JRadioButton makeButton(String buttonText, String toolTipText) throws PWCGException
	{
		Color fgColor = ColorMap.CHALK_FOREGROUND;

		Font font = MonitorSupport.getPrimaryFont();

		JRadioButton button = new JRadioButton(buttonText);
		button.setActionCommand("Configuration Parameters: " + buttonText);
		button.setHorizontalAlignment(SwingConstants.LEFT );
		button.setBorderPainted(false);
		button.addActionListener(this);
		button.setOpaque(false);
		button.setForeground(fgColor);
		button.setFont(font);

		ToolTipManager.setToolTip(button, toolTipText);
		
		buttonGroup.add(button);

		return button;
	}

	ConfigurationParametersGUI createConfigPanel(String action) throws PWCGException 
	{
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
		else if (action.contains("Aircraft Numbers"))
		{
			configSet = configManager.getMergedConfigSet(ConfigSetKeys.ConfigSetAircraftNumbers);				
		}
		else if (action.contains("Mission Limits"))
		{
			configSet = configManager.getMergedConfigSet(ConfigSetKeys.ConfigSetMissionLimits);				
		}
		else if (action.contains("Mission Spacing"))
		{
			configSet = configManager.getMergedConfigSet(ConfigSetKeys.ConfigSetMissionSpacing);				
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
	        Campaign campaign =     PWCGContextManager.getInstance().getCampaign();

			String action = ae.getActionCommand();
			if (action.equalsIgnoreCase("Accept"))
			{
				for (ConfigurationParametersGUI configUI : configurationGUIs.values())
				{
					configUI.recordChanges();
				}
				
				campaign.getCampaignConfigManager().write();
				PWCGContextManager.getInstance().resetForMovingFront();
		        CampaignGuiContextManager.getInstance().popFromContextStack();
				return;
			}
			else if (action.equalsIgnoreCase("Cancel"))
			{
			    campaign.getCampaignConfigManager().readConfig();

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
				
				CampaignGuiContextManager.getInstance().changeCurrentContext(null, newConfig, null);
			}
		}
		catch (Throwable e)
		{
			Logger.logException(e);
			ErrorDialog.internalError(e.getMessage());
		}
	}
}
