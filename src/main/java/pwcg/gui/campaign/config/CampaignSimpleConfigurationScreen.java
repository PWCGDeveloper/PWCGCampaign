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
import pwcg.gui.ScreenIdentifier;
import pwcg.gui.UiImageResolver;
import pwcg.gui.colors.ColorMap;
import pwcg.gui.dialogs.ErrorDialog;
import pwcg.gui.dialogs.PWCGMonitorFonts;
import pwcg.gui.utils.CommonUIActions;
import pwcg.gui.utils.ImageResizingPanel;
import pwcg.gui.utils.ImageResizingPanelBuilder;
import pwcg.gui.utils.PWCGButtonFactory;
import pwcg.gui.utils.PwcgBorderFactory;
import pwcg.gui.utils.SpacerPanelFactory;

public class CampaignSimpleConfigurationScreen extends ImageResizingPanel implements ActionListener
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

    private ButtonGroup aaButtonGroup = new ButtonGroup();
    private ButtonModel aaLowButtonModel = null;
    private ButtonModel aaMedButtonModel = null;
    private ButtonModel aaHighButtonModel = null;

    private ButtonGroup cpuAllowanceButtonGroup = new ButtonGroup();
    private ButtonModel cpuAllowanceLowButtonModel = null;
    private ButtonModel cpuAllowanceMedButtonModel = null;
    private ButtonModel cpuAllowanceHighButtonModel = null;

    private Campaign campaign;

    public CampaignSimpleConfigurationScreen(Campaign campaign)
    {
        super("");
        this.setLayout(new BorderLayout());
        this.setOpaque(false);

        this.campaign = campaign;
    }

	public void makePanels() throws PWCGException 
	{
        String imagePath = UiImageResolver.getImage(ScreenIdentifier.CampaignSimpleConfigurationScreen);
        this.setImageFromName(imagePath);

        this.add(BorderLayout.WEST, makeNavigatePanel());
	    this.add(BorderLayout.CENTER, makeConfigControlPanel());
        this.add(BorderLayout.EAST, SpacerPanelFactory.makeDocumentSpacerPanel(1400));
        
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
        
        String currentAASetting = configManager.getStringConfigParam(ConfigItemKeys.SimpleConfigAAKey);
        if (currentAASetting.equals(ConfigSimple.CONFIG_LEVEL_LOW))
        {
            aaButtonGroup.setSelected(aaLowButtonModel, true);
        }
        else if (currentAASetting.equals(ConfigSimple.CONFIG_LEVEL_MED))
        {
            aaButtonGroup.setSelected(aaMedButtonModel, true);
        }
        else if (currentAASetting.equals(ConfigSimple.CONFIG_LEVEL_HIGH))
        {
            aaButtonGroup.setSelected(aaHighButtonModel, true);
        }
        
        String currentCpuAllowanceSetting = configManager.getStringConfigParam(ConfigItemKeys.SimpleConfigCpuAllowanceKey);
        if (currentCpuAllowanceSetting.equals(ConfigSimple.CONFIG_LEVEL_LOW))
        {
            cpuAllowanceButtonGroup.setSelected(cpuAllowanceLowButtonModel, true);
        }
        else if (currentCpuAllowanceSetting.equals(ConfigSimple.CONFIG_LEVEL_MED))
        {
            cpuAllowanceButtonGroup.setSelected(cpuAllowanceMedButtonModel, true);
        }
        else if (currentCpuAllowanceSetting.equals(ConfigSimple.CONFIG_LEVEL_HIGH))
        {
            cpuAllowanceButtonGroup.setSelected(cpuAllowanceHighButtonModel, true);
        }

	}

	private JPanel makeNavigatePanel() throws PWCGException
	{
        JPanel simpleConfigAcceptPanel = new JPanel(new BorderLayout());
        simpleConfigAcceptPanel.setOpaque(false);

        JPanel buttonPanel = new JPanel(new GridLayout(0,1));
        buttonPanel.setOpaque(false);

        JButton acceptButton = PWCGButtonFactory.makeTranslucentMenuButton("Accept", CommonUIActions.ACTION_ACCEPT, "Accept configuration changes", this);
        buttonPanel.add(acceptButton);
        
        JButton cancelButton = PWCGButtonFactory.makeTranslucentMenuButton("Cancel", CommonUIActions.ACTION_CANCEL, "Cancel configuration changes", this);
        buttonPanel.add(cancelButton);

        JLabel spacer1 = PWCGButtonFactory.makePaperLabelLarge("   ");
        buttonPanel.add(spacer1);

        JLabel spacer2
        = PWCGButtonFactory.makePaperLabelLarge("   ");
        buttonPanel.add(spacer2);

        JButton resetButton = PWCGButtonFactory.makeTranslucentMenuButton("Reset", CommonUIActions.ACTION_RESET, "Reset all configurations to default", this);
        buttonPanel.add(resetButton);
		
		simpleConfigAcceptPanel.add(buttonPanel, BorderLayout.NORTH);

		return simpleConfigAcceptPanel;
	}

	private JPanel makeConfigControlPanel() throws PWCGException
	{
		JPanel airButtonPanel = createAirConfigPanel();
        JPanel groundButtonPanel = createGroundConfigPanel();
        JPanel aaButtonPanel = createAAConfigPanel();
        JPanel cpuAllowanceButtonPanel = createCpuAllowanceConfigPanel();
        
        String imagePath = UiImageResolver.getImage(ScreenIdentifier.Document);
        ImageResizingPanel simpleConfigButtonPanel = ImageResizingPanelBuilder.makeImageResizingPanel(imagePath);
        simpleConfigButtonPanel.setImageFromName(imagePath);
        simpleConfigButtonPanel.setLayout(new GridLayout(0,1));
        simpleConfigButtonPanel.setBorder(PwcgBorderFactory.createStandardDocumentBorder());

		simpleConfigButtonPanel.add(airButtonPanel);
        simpleConfigButtonPanel.add(groundButtonPanel);
        simpleConfigButtonPanel.add(aaButtonPanel);
        simpleConfigButtonPanel.add(cpuAllowanceButtonPanel);
				
		return simpleConfigButtonPanel;
	}

    private JPanel createAirConfigPanel() throws PWCGException
    {
        JPanel airButtonPanel = new JPanel(new BorderLayout());
        airButtonPanel.setOpaque(false);

        JLabel spacerLabel = makeLabel("          ");        
        airButtonPanel.add(spacerLabel, BorderLayout.WEST);

        JPanel airButtonPanelGrid = new JPanel(new GridLayout(0,1));
        airButtonPanelGrid.setOpaque(false);
        
        JLabel airDensityLabel = makeLabel(CampaignConfigurationSimpleGUIController.ACTION_SET_AIR_DENSITY + ":");      
        airButtonPanelGrid.add(airDensityLabel);

        JRadioButton airLowDensity = PWCGButtonFactory.makeRadioButton("Low", "Low Air Density", "Fewer aircraft for average machines", false, this, ColorMap.PAPER_FOREGROUND);       
        airButtonPanelGrid.add(airLowDensity);
        airLowButtonModel = airLowDensity.getModel();
        airButtonGroup.add(airLowDensity);

        JRadioButton airMedDensity = PWCGButtonFactory.makeRadioButton("Med", "Med Air Density", "Medium number of aircraft - requires a pretty good machine", false, this, ColorMap.PAPER_FOREGROUND);        
        airButtonPanelGrid.add(airMedDensity);
        airMedButtonModel = airMedDensity.getModel();
        airButtonGroup.add(airMedDensity);
        
        JRadioButton airHighDensity = PWCGButtonFactory.makeRadioButton("High", "High Air Density", "High number of aircraft - high end machines only", false, this, ColorMap.PAPER_FOREGROUND);       
        airButtonPanelGrid.add(airHighDensity);
        airHighButtonModel = airHighDensity.getModel();
        airButtonGroup.add(airHighDensity);
                
        airButtonPanel.add(airButtonPanelGrid, BorderLayout.NORTH);

        return airButtonPanel;
    }

    private JPanel createGroundConfigPanel() throws PWCGException
    {
        JPanel groundButtonPanel = new JPanel(new BorderLayout());
		groundButtonPanel.setOpaque(false);

        JLabel spacerLabel = makeLabel("          ");        
        groundButtonPanel.add(spacerLabel, BorderLayout.WEST);

		JPanel groundDensityGrid = new JPanel(new GridLayout(0,1));
		groundDensityGrid.setOpaque(false);
		
        JLabel groundDensityLabel = PWCGButtonFactory.makePaperLabelLarge(CampaignConfigurationSimpleGUIController.ACTION_SET_GROUND_DENSITY + ":");
		groundDensityGrid.add(groundDensityLabel);

		JRadioButton lowDensity = PWCGButtonFactory.makeRadioButton("Low", "Low Ground Density", "Fewer AI ground units", false, this, ColorMap.PAPER_FOREGROUND);
		groundDensityGrid.add(lowDensity);
		groundLowButtonModel = lowDensity.getModel();
		groundButtonGroup.add(lowDensity);

		JRadioButton medDensity = PWCGButtonFactory.makeRadioButton("Med", "Med Ground Density", "Medium numbers of AI ground units", false, this, ColorMap.PAPER_FOREGROUND);		
		groundDensityGrid.add(medDensity);
		groundMedButtonModel = medDensity.getModel();
		groundButtonGroup.add(medDensity);
		
		JRadioButton highDensity = PWCGButtonFactory.makeRadioButton("High", "High Ground Density", "Large numbers of AI ground units", false, this, ColorMap.PAPER_FOREGROUND);		
		groundDensityGrid.add(highDensity);
		groundHighButtonModel = highDensity.getModel();
		groundButtonGroup.add(highDensity);

		groundButtonPanel.add(groundDensityGrid, BorderLayout.NORTH);
        
        return groundButtonPanel;
    }

    private JPanel createAAConfigPanel() throws PWCGException
    {
        JPanel aaButtonPanel = new JPanel(new BorderLayout());
        aaButtonPanel.setOpaque(false);

        JLabel spacerLabel = makeLabel("          ");        
        aaButtonPanel.add(spacerLabel, BorderLayout.WEST);

        JPanel aaDensityGrid = new JPanel(new GridLayout(0,1));
        aaDensityGrid.setOpaque(false);
        
        JLabel aaDensityLabel = PWCGButtonFactory.makePaperLabelLarge(CampaignConfigurationSimpleGUIController.ACTION_SET_AA_DENSITY + ":");
        aaDensityGrid.add(aaDensityLabel);

        JRadioButton lowDensity = PWCGButtonFactory.makeRadioButton("Low", "Low AA Density", "Fewer AA units", false, this, ColorMap.PAPER_FOREGROUND);     
        aaDensityGrid.add(lowDensity);
        aaLowButtonModel = lowDensity.getModel();
        aaButtonGroup.add(lowDensity);

        JRadioButton medDensity = PWCGButtonFactory.makeRadioButton("Med", "Med AA Density", "Medium numbers of AA units", false, this, ColorMap.PAPER_FOREGROUND);     
        aaDensityGrid.add(medDensity);
        aaMedButtonModel = medDensity.getModel();
        aaButtonGroup.add(medDensity);
        
        JRadioButton highDensity = PWCGButtonFactory.makeRadioButton("High", "High AA Density", "Large numbers of AA units", false, this, ColorMap.PAPER_FOREGROUND);       
        aaDensityGrid.add(highDensity);
        aaHighButtonModel = highDensity.getModel();
        aaButtonGroup.add(highDensity);

        aaButtonPanel.add(aaDensityGrid, BorderLayout.NORTH);
        
        return aaButtonPanel;
    }
    
    private JPanel createCpuAllowanceConfigPanel() throws PWCGException
    {
        JPanel cpuAllowanceButtonPanel = new JPanel(new BorderLayout());
        cpuAllowanceButtonPanel.setOpaque(false);

        JLabel spacerLabel = makeLabel("          ");        
        cpuAllowanceButtonPanel.add(spacerLabel, BorderLayout.WEST);

        JPanel cpuAllowanceGrid = new JPanel(new GridLayout(0,1));
        cpuAllowanceGrid.setOpaque(false);
        
        JLabel cpuAllowanceLabel = PWCGButtonFactory.makePaperLabelLarge(CampaignConfigurationSimpleGUIController.ACTION_SET_CPU_ALOWANCE_DENSITY + ":");
        cpuAllowanceGrid.add(cpuAllowanceLabel);

        JRadioButton lowDensity = PWCGButtonFactory.makeRadioButton("Low", "Low CPU Allowance", "Very few CPU expensive units", false, this, ColorMap.PAPER_FOREGROUND);     
        cpuAllowanceGrid.add(lowDensity);
        cpuAllowanceLowButtonModel = lowDensity.getModel();
        cpuAllowanceButtonGroup.add(lowDensity);

        JRadioButton medDensity = PWCGButtonFactory.makeRadioButton("Med", "Med CPU Allowance", "Some CPU expensive units", false, this, ColorMap.PAPER_FOREGROUND);     
        cpuAllowanceGrid.add(medDensity);
        cpuAllowanceMedButtonModel = medDensity.getModel();
        cpuAllowanceButtonGroup.add(medDensity);
        
        JRadioButton highDensity = PWCGButtonFactory.makeRadioButton("High", "High CPU Allowance", "No consideration of CPU expense", false, this, ColorMap.PAPER_FOREGROUND);     
        cpuAllowanceGrid.add(highDensity);
        cpuAllowanceHighButtonModel = highDensity.getModel();
        cpuAllowanceButtonGroup.add(highDensity);

        cpuAllowanceButtonPanel.add(cpuAllowanceGrid, BorderLayout.NORTH);
        
        return cpuAllowanceButtonPanel;
    }

	private JLabel makeLabel(String buttonName) throws PWCGException
	{
		Color fg = ColorMap.PAPER_FOREGROUND;

		Font font = PWCGMonitorFonts.getPrimaryFontLarge();

		JLabel button= new JLabel(buttonName);
		button.setOpaque(false);
		button.setFont(font);
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

