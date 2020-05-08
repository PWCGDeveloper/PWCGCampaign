package pwcg.gui.campaign.config;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ButtonGroup;
import javax.swing.ButtonModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import pwcg.campaign.Campaign;
import pwcg.core.config.ConfigItemKeys;
import pwcg.core.config.ConfigManagerCampaign;
import pwcg.core.config.ConfigSimple;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.PWCGLogger;
import pwcg.gui.PwcgGuiContext;
import pwcg.gui.colors.ColorMap;
import pwcg.gui.dialogs.ErrorDialog;
import pwcg.gui.dialogs.MonitorSupport;
import pwcg.gui.utils.CommonUIActions;
import pwcg.gui.utils.ContextSpecificImages;
import pwcg.gui.utils.ImageResizingPanel;
import pwcg.gui.utils.PWCGButtonFactory;

public class CampaignConfigurationSimpleGUI extends PwcgGuiContext implements ActionListener
{

	private static final long serialVersionUID = 1L;
	private ButtonGroup airButtonGroup = new ButtonGroup();
	private ButtonModel airLowButtonModel = null;
	private ButtonModel airMedButtonModel = null;
	private ButtonModel airHighButtonModel = null;

    private ButtonGroup groundButtonGroup = new ButtonGroup();
    private ButtonModel groundLowButtonModel = null;
    private ButtonModel groundMedButtonModel = null;
    private ButtonModel groundHighButtonModel = null;

    private Campaign campaign;

    public CampaignConfigurationSimpleGUI(Campaign campaign)
    {
        super();
        this.campaign = campaign;
    }

	public void makePanels() throws PWCGException 
	{
	    setRightPanel(makeConfigControlPanel());
        setCenterPanel(makeCenterPanel());
        setLeftPanel(makeNavigatePanel());
        
        initializeButtons();
	}

	private void initializeButtons() throws PWCGException 
	{
		ConfigManagerCampaign configManager = campaign.getCampaignConfigManager();
		
		String currentAirSetting = configManager.getStringConfigParam(ConfigItemKeys.SimpleConfigAirKey);
		if (currentAirSetting.equals(ConfigSimple.CONFIG_LEVEL_LOW))
		{
			airButtonGroup.setSelected(airLowButtonModel, true);
		}
		else if (currentAirSetting.equals(ConfigSimple.CONFIG_LEVEL_MED))
		{
			airButtonGroup.setSelected(airMedButtonModel, true);
		}
		else if (currentAirSetting.equals(ConfigSimple.CONFIG_LEVEL_HIGH))
		{
			airButtonGroup.setSelected(airHighButtonModel, true);
		}
        
        String currentGroundSetting = configManager.getStringConfigParam(ConfigItemKeys.SimpleConfigGroundKey);
        if (currentGroundSetting.equals(ConfigSimple.CONFIG_LEVEL_LOW))
        {
            groundButtonGroup.setSelected(groundLowButtonModel, true);
        }
        else if (currentGroundSetting.equals(ConfigSimple.CONFIG_LEVEL_MED))
        {
            groundButtonGroup.setSelected(groundMedButtonModel, true);
        }
        else if (currentGroundSetting.equals(ConfigSimple.CONFIG_LEVEL_HIGH))
        {
            groundButtonGroup.setSelected(groundHighButtonModel, true);
        }

	}

	private JPanel makeCenterPanel() throws PWCGException  
	{
        String leftImageName = "SimpleConfigCampaignCenter.jpg";
        String menuPath = ContextSpecificImages.menuPathForNation(campaign);
        String imagePath = menuPath + leftImageName;

        ImageResizingPanel centerPortraitPanel = new ImageResizingPanel(imagePath);
        centerPortraitPanel.setLayout(new BorderLayout());
        
		return centerPortraitPanel;
    }

	private JPanel makeNavigatePanel() throws PWCGException
	{
        String imagePath = getSideImage(campaign, "SimpleConfigCampaignLeft.jpg");

        ImageResizingPanel simpleConfigAcceptPanel = new ImageResizingPanel(imagePath);
        simpleConfigAcceptPanel.setLayout(new BorderLayout());
        simpleConfigAcceptPanel.setOpaque(false);

        JPanel buttonPanel = new JPanel(new GridLayout(0,1));
        buttonPanel.setOpaque(false);

        JButton acceptButton = PWCGButtonFactory.makeMenuButton("Accept Config Changes", CommonUIActions.ACTION_ACCEPT, this);
        buttonPanel.add(acceptButton);
        
        JButton cancelButton = PWCGButtonFactory.makeMenuButton("Cancel Config Changes", CommonUIActions.ACTION_CANCEL, this);
        buttonPanel.add(cancelButton);

        JLabel spacer1 = PWCGButtonFactory.makeMenuLabelLarge("   ");
        buttonPanel.add(spacer1);

        JLabel spacer2
        = PWCGButtonFactory.makeMenuLabelLarge("   ");
        buttonPanel.add(spacer2);

        JButton resetButton = PWCGButtonFactory.makeMenuButton("Reset to Default", CommonUIActions.ACTION_RESET, this);
        buttonPanel.add(resetButton);
		
		simpleConfigAcceptPanel.add(buttonPanel, BorderLayout.NORTH);

		return simpleConfigAcceptPanel;
	}

	private JPanel makeConfigControlPanel() throws PWCGException
	{
		JPanel airButtonPanel = createAirConfigPanel();
        JPanel groundButtonPanel = createGroundConfigPanel();
        String imagePath = getSideImage(campaign, "SimpleConfigCampaignRight.jpg");

        ImageResizingPanel simpleConfigButtonPanel = new ImageResizingPanel(imagePath);
		simpleConfigButtonPanel.setLayout(new GridLayout(0,1));

		simpleConfigButtonPanel.add(airButtonPanel);
        simpleConfigButtonPanel.add(groundButtonPanel);
				
		return simpleConfigButtonPanel;
	}
    
    private JPanel createAirConfigPanel() throws PWCGException
    {
        JPanel airButtonPanel = new JPanel(new BorderLayout());
        airButtonPanel.setOpaque(false);

        JLabel spacerLabel = makeLabel("          ");        
        airButtonPanel.add(spacerLabel, BorderLayout.WEST);

        JPanel shapePanel = new JPanel(new BorderLayout());
        shapePanel.setOpaque(false);

        JPanel airButtonPanelGrid = new JPanel(new GridLayout(0,1));
        airButtonPanelGrid.setOpaque(false);
        
        JLabel airDensityLabel = makeLabel(CampaignConfigurationSimpleGUIController.ACTION_SET_AIR_DENSITY + ":");      
        airButtonPanelGrid.add(airDensityLabel);

        JRadioButton airLowDensity = PWCGButtonFactory.makeRadioButton("Low", "Low Air Density", "Fewer aircraft for average machines", false, this);       
        airButtonPanelGrid.add(airLowDensity);
        airLowButtonModel = airLowDensity.getModel();
        airButtonGroup.add(airLowDensity);

        JRadioButton airMedDensity = PWCGButtonFactory.makeRadioButton("Med", "Med Air Density", "Medium number of aircraft - requires a pretty good machine", false, this);        
        airButtonPanelGrid.add(airMedDensity);
        airMedButtonModel = airMedDensity.getModel();
        airButtonGroup.add(airMedDensity);
        
        JRadioButton airHighDensity = PWCGButtonFactory.makeRadioButton("High", "High Air Density", "High number of aircraft - high end machines only", false, this);       
        airButtonPanelGrid.add(airHighDensity);
        airHighButtonModel = airHighDensity.getModel();
        airButtonGroup.add(airHighDensity);
        
        airButtonPanel.add(airButtonPanelGrid, BorderLayout.SOUTH);
        
        shapePanel.add(airButtonPanelGrid, BorderLayout.NORTH);
        airButtonPanel.add(shapePanel, BorderLayout.CENTER);

        return airButtonPanel;
    }

    private JPanel createGroundConfigPanel() throws PWCGException
    {
        JPanel groundButtonPanel = new JPanel(new BorderLayout());
		groundButtonPanel.setOpaque(false);

        JLabel spacerLabel = makeLabel("          ");        
        groundButtonPanel.add(spacerLabel, BorderLayout.WEST);

        JPanel shapePanel = new JPanel(new BorderLayout());
        shapePanel.setOpaque(false);

		JPanel groundDensityGrid = new JPanel(new GridLayout(0,1));
		groundDensityGrid.setOpaque(false);
		
        JLabel groundDensityLabel = PWCGButtonFactory.makeMenuLabelLarge(CampaignConfigurationSimpleGUIController.ACTION_SET_GROUND_DENSITY + ":");
		groundDensityGrid.add(groundDensityLabel);

		JRadioButton lowDensity = PWCGButtonFactory.makeRadioButton("Low", "Low Ground Density", "Fewer AI ground units", false, this);		
		groundDensityGrid.add(lowDensity);
		groundLowButtonModel = lowDensity.getModel();
		groundButtonGroup.add(lowDensity);

		JRadioButton medDensity = PWCGButtonFactory.makeRadioButton("Med", "Med Ground Density", "Medium numbers of AI ground units", false, this);		
		groundDensityGrid.add(medDensity);
		groundMedButtonModel = medDensity.getModel();
		groundButtonGroup.add(medDensity);
		
		JRadioButton highDensity = PWCGButtonFactory.makeRadioButton("High", "High Ground Density", "Large numbers of AI ground units", false, this);		
		groundDensityGrid.add(highDensity);
		groundHighButtonModel = highDensity.getModel();
		groundButtonGroup.add(highDensity);

        shapePanel.add(groundDensityGrid, BorderLayout.NORTH);
        groundButtonPanel.add(shapePanel, BorderLayout.CENTER);
        
        return groundButtonPanel;
    }

	private JLabel makeLabel(String buttonName) throws PWCGException
	{
		Color bg = ColorMap.WOOD_BACKGROUND;
		Color fg = ColorMap.CHALK_FOREGROUND;

		Font font = MonitorSupport.getPrimaryFontLarge();

		JLabel button= new JLabel(buttonName);
		button.setOpaque(false);
		button.setFont(font);
		button.setBackground(bg);
		button.setForeground(fg);

		return button;
	}

	public void actionPerformed(ActionEvent ae)
	{
		try
		{
	        String action = ae.getActionCommand();
	        CampaignConfigurationSimpleGUIController controller = new CampaignConfigurationSimpleGUIController(campaign);
	        controller.setSimpleConfig(action);
		}
		catch (Exception e)
		{
			PWCGLogger.logException(e);
			ErrorDialog.internalError(e.getMessage());
		}
	}
	
}

