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
import pwcg.core.utils.PWCGLogger;
import pwcg.gui.CampaignGuiContextManager;
import pwcg.gui.PwcgThreePanelUI;
import pwcg.gui.ScreenIdentifier;
import pwcg.gui.UiImageResolver;
import pwcg.gui.colors.ColorMap;
import pwcg.gui.config.ConfigurationParametersGUI;
import pwcg.gui.dialogs.ErrorDialog;
import pwcg.gui.dialogs.PWCGMonitorFonts;
import pwcg.gui.sound.SoundManager;
import pwcg.gui.utils.ImageResizingPanel;
import pwcg.gui.utils.PWCGButtonFactory;

public class PwcgGlobalConfigurationScreen extends ImageResizingPanel implements ActionListener
{
    private static final long serialVersionUID = 1L;
    private Map<String, ConfigurationParametersGUI> configurationGUIs = new HashMap<String, ConfigurationParametersGUI>();
    private PwcgThreePanelUI pwcgThreePanel;
    private ButtonGroup buttonGroup = new ButtonGroup();
    private ConfigManagerGlobal configManager = null;

    public PwcgGlobalConfigurationScreen()
    {
        super("");
        this.setLayout(new BorderLayout());
        this.setOpaque(false);

        this.pwcgThreePanel = new PwcgThreePanelUI(this);
    }
    
    public void makePanels() throws PWCGException 
    {
        String imagePath = UiImageResolver.getImage(ScreenIdentifier.PwcgGlobalConfigurationScreen);
        this.setImage(imagePath);

        configManager = ConfigManagerGlobal.getInstance();
        
        pwcgThreePanel.setLeftPanel(makeNavigatePanel());
        pwcgThreePanel.setCenterPanel(makeBlankCenterPanel());
        pwcgThreePanel.setRightPanel(makeCategoryPanel());
    }

    public JPanel makeBlankCenterPanel()  
    {       
        JPanel blankPanel = new JPanel(new BorderLayout());
        blankPanel.setOpaque(false);
        blankPanel.setLayout(new BorderLayout());
        return blankPanel;
    }

    public JPanel makeNavigatePanel() throws PWCGException  
    {
        JPanel navPanel = new JPanel(new BorderLayout());
        navPanel.setOpaque(false);

        JPanel buttonPanel = new JPanel(new GridLayout(0,1));
        buttonPanel.setOpaque(false);

        JButton acceptButton = PWCGButtonFactory.makeTranslucentMenuButton("Accept", "Accept", "Accept configuration changes", this);
        buttonPanel.add(acceptButton);

        JLabel spacer2 = new JLabel("   ");
        spacer2.setOpaque(false);
        buttonPanel.add(spacer2);

        JButton cancelButton = PWCGButtonFactory.makeTranslucentMenuButton("Cancel", "Cancel", "Cancel configuration changes", this);
        buttonPanel.add(cancelButton);

        navPanel.add(buttonPanel, BorderLayout.NORTH);
        
        return navPanel;
    }

    public JPanel makeCategoryPanel() throws PWCGException  
    {
        JPanel configPanel = new JPanel(new BorderLayout());
        configPanel.setOpaque(false);

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

        Font font = PWCGMonitorFonts.getPrimaryFont();

        JRadioButton button = new JRadioButton(buttonText);
        button.setActionCommand("Configuration Parameters: " + buttonText);
        button.setHorizontalAlignment(SwingConstants.LEFT );
        button.setBorderPainted(false);
        button.setFocusPainted(false);
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


