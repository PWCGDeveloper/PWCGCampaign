package pwcg.gui.maingui.config;

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

import pwcg.core.config.ConfigManagerGlobal;
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
import pwcg.gui.sound.SoundManager;
import pwcg.gui.utils.ContextSpecificImages;
import pwcg.gui.utils.ImageResizingPanel;
import pwcg.gui.utils.PWCGButtonFactory;

public class ConfigurationGlobalGUI extends PwcgGuiContext implements ActionListener
{
    private static final long serialVersionUID = 1L;
    private Map<String, ConfigurationParametersGUI> configurationGUIs = new HashMap<String, ConfigurationParametersGUI>();
    private ButtonGroup buttonGroup = new ButtonGroup();
    private ConfigManagerGlobal configManager = null;

    public ConfigurationGlobalGUI()
    {
        super();
    }
    
    public void makePanels() 
    {
        try
        {
            configManager = ConfigManagerGlobal.getInstance();
            
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

    public JPanel makeCenterPanel()  
    {       
        String imagePath = ContextSpecificImages.imagesMisc() + "Paper.jpg";
        ImageResizingPanel blankPanel = new ImageResizingPanel(imagePath);
        blankPanel.setLayout(new BorderLayout());
                
        return blankPanel;
    }

    public JPanel makeNavigatePanel() throws PWCGException  
    {
        String imagePath = getSideImageMain("ConfigLeft.jpg");

        JPanel navPanel = new ImageResizingPanel(imagePath);
        navPanel.setLayout(new BorderLayout());

        JPanel buttonPanel = new JPanel(new GridLayout(0,1));
        buttonPanel.setOpaque(false);

        JButton acceptButton = PWCGButtonFactory.makeMenuButton("Accept Config Changes", "Accept", this);
        buttonPanel.add(acceptButton);

        JButton cancelButton = PWCGButtonFactory.makeMenuButton("Cancel Config Changes", "Cancel", this);
        buttonPanel.add(cancelButton);

        navPanel.add(buttonPanel, BorderLayout.NORTH);
        
        return navPanel;
    }

    public JPanel makeCategoryPanel() throws PWCGException  
    {
        String imagePath = getSideImageMain("ConfigRight.jpg");

        JPanel configPanel = new ImageResizingPanel(imagePath);
        configPanel.setLayout(new BorderLayout());

        JPanel buttonPanel = new JPanel(new GridLayout(0,1));
        buttonPanel.setOpaque(false);
        

        JLabel label = PWCGButtonFactory.makeMenuLabelLarge("Global Configuration Categories:");
        buttonPanel.add(label);

        JLabel spacer = PWCGButtonFactory.makeMenuLabelLarge("   ");
        buttonPanel.add(spacer);

        buttonPanel.add(makeCategoryRadioButton("User Preferences"));
        buttonPanel.add(makeCategoryRadioButton("GUI"));
        
        add (buttonPanel);

        configPanel.add(buttonPanel, BorderLayout.NORTH);
        
        return configPanel;
    }

    private JRadioButton makeCategoryRadioButton(String buttonText) throws PWCGException 
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

        buttonGroup.add(button);

        return button;
    }

    ConfigurationParametersGUI createConfigPanel(String action) throws PWCGException 
    {
        ConfigSet configSet = null;

        if (action.contains("GUI"))
        {
        	configSet = configManager.getMergedConfigSet(ConfigSetKeys.ConfigSetGUI);
        }
        else if (action.contains("User Preferences"))
        {
        	configSet = configManager.getMergedConfigSet(ConfigSetKeys.ConfigSetUserPref);
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
                
                configManager.write();
                SoundManager.getInstance().initialize();
                CampaignGuiContextManager.getInstance().popFromContextStack();
                return;
            }
            else if (action.equalsIgnoreCase("Cancel"))
            {
                configManager.readConfig();

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


