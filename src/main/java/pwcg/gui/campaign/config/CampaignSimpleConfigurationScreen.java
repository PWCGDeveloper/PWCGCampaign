package pwcg.gui.campaign.config;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ButtonGroup;
import javax.swing.ButtonModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import pwcg.core.config.ConfigItemKeys;
import pwcg.core.config.ConfigManagerCampaign;
import pwcg.core.config.ConfigSimple;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.PWCGLogger;
import pwcg.gui.ScreenIdentifier;
import pwcg.gui.UiImageResolver;
import pwcg.gui.campaign.home.CampaignHomeContext;
import pwcg.gui.colors.ColorMap;
import pwcg.gui.dialogs.ErrorDialog;
import pwcg.gui.utils.CommonUIActions;
import pwcg.gui.utils.ImageResizingPanel;
import pwcg.gui.utils.PWCGButtonFactory;
import pwcg.gui.utils.PWCGLabelFactory;
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
    private ButtonModel groundUltraLowButtonModel = null;
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

    private ButtonGroup structureButtonGroup = new ButtonGroup();
    private ButtonModel structureLowButtonModel = null;
    private ButtonModel structureMedButtonModel = null;
    private ButtonModel structureHighButtonModel = null;

    public CampaignSimpleConfigurationScreen()
    {
        super();
        this.setLayout(new BorderLayout());
        this.setOpaque(false);
    }

	public void makePanels() throws PWCGException 
	{
        String imagePath = UiImageResolver.getImage(ScreenIdentifier.CampaignSimpleConfigurationScreen);
        this.setThemedImageFromName(CampaignHomeContext.getCampaign().getReferenceService(), imagePath);

        this.add(BorderLayout.WEST, makeNavigatePanel());
	    this.add(BorderLayout.CENTER, makeConfigControlPanel());
        this.add(BorderLayout.EAST, SpacerPanelFactory.makeDocumentSpacerPanel(1400));
        
        initializeButtons();
	}

	private void initializeButtons() throws PWCGException 
	{
		ConfigManagerCampaign configManager = CampaignHomeContext.getCampaign().getCampaignConfigManager();
		
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
        
        if (currentGroundSetting.equals(ConfigSimple.CONFIG_LEVEL_ULTRA_LOW))
        {
            groundButtonGroup.setSelected(groundUltraLowButtonModel, true);
        }
        else if (currentGroundSetting.equals(ConfigSimple.CONFIG_LEVEL_LOW))
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
 
        String currentstructureSetting = configManager.getStringConfigParam(ConfigItemKeys.SimpleConfigStructuresKey);
        if (currentstructureSetting.equals(ConfigSimple.CONFIG_LEVEL_LOW))
        {
            structureButtonGroup.setSelected(structureLowButtonModel, true);
        }
        else if (currentstructureSetting.equals(ConfigSimple.CONFIG_LEVEL_MED))
        {
            structureButtonGroup.setSelected(structureMedButtonModel, true);
        }
        else if (currentstructureSetting.equals(ConfigSimple.CONFIG_LEVEL_HIGH))
        {
            structureButtonGroup.setSelected(structureHighButtonModel, true);
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

        buttonPanel.add(PWCGLabelFactory.makeDummyLabel());
        buttonPanel.add(PWCGLabelFactory.makeDummyLabel());

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
        JPanel structureButtonPanel = createStructureConfigPanel();
        
        String imagePath = UiImageResolver.getImage(ScreenIdentifier.Document);
        ImageResizingPanel simpleConfigButtonPanel = new ImageResizingPanel(CampaignHomeContext.getCampaign().getReferenceService(), imagePath);
        simpleConfigButtonPanel.setThemedImageFromName(CampaignHomeContext.getCampaign().getReferenceService(), imagePath);
        simpleConfigButtonPanel.setLayout(new GridLayout(0,1));
        simpleConfigButtonPanel.setBorder(PwcgBorderFactory.createStandardDocumentBorder());

		simpleConfigButtonPanel.add(airButtonPanel);
        simpleConfigButtonPanel.add(groundButtonPanel);
        simpleConfigButtonPanel.add(aaButtonPanel);
        simpleConfigButtonPanel.add(cpuAllowanceButtonPanel);
        simpleConfigButtonPanel.add(structureButtonPanel);
				
		return simpleConfigButtonPanel;
	}

    private JPanel createAirConfigPanel() throws PWCGException
    {
        JPanel airButtonPanel = new JPanel(new BorderLayout());
        airButtonPanel.setOpaque(false);

        airButtonPanel.add(PWCGLabelFactory.makeDummyLabel(), BorderLayout.WEST);

        JPanel airButtonPanelGrid = new JPanel(new GridLayout(0,1));
        airButtonPanelGrid.setOpaque(false);
        
        JLabel airDensityLabel = PWCGLabelFactory.makePaperLabelLarge(CampaignConfigurationSimpleGUIController.ACTION_SET_AIR_DENSITY);
        airButtonPanelGrid.add(airDensityLabel);

        JRadioButton airLowDensity = PWCGButtonFactory.makeRadioButton("Low", "Low Air Density", "Fewer aircraft for average machines", null, ColorMap.PAPER_FOREGROUND, false, this);       
        airButtonPanelGrid.add(airLowDensity);
        airLowButtonModel = airLowDensity.getModel();
        airButtonGroup.add(airLowDensity);

        JRadioButton airMedDensity = PWCGButtonFactory.makeRadioButton("Med", "Med Air Density", "Medium number of aircraft - requires a pretty good machine", null, ColorMap.PAPER_FOREGROUND, false, this);        
        airButtonPanelGrid.add(airMedDensity);
        airMedButtonModel = airMedDensity.getModel();
        airButtonGroup.add(airMedDensity);
        
        JRadioButton airHighDensity = PWCGButtonFactory.makeRadioButton("High", "High Air Density", "High number of aircraft - high end machines only", null, ColorMap.PAPER_FOREGROUND, false, this);       
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

		groundButtonPanel.add(PWCGLabelFactory.makeDummyLabel(), BorderLayout.WEST);

		JPanel groundDensityGrid = new JPanel(new GridLayout(0,1));
		groundDensityGrid.setOpaque(false);
		
        JLabel groundDensityLabel = PWCGLabelFactory.makePaperLabelLarge(CampaignConfigurationSimpleGUIController.ACTION_SET_GROUND_DENSITY);
		groundDensityGrid.add(groundDensityLabel);
        
        JRadioButton ultraLowDensity = PWCGButtonFactory.makeRadioButton("Ultra Low", "Ultra Low Ground Density", "No trucks or trains", null, ColorMap.PAPER_FOREGROUND, false, this);
        groundDensityGrid.add(ultraLowDensity);
        groundUltraLowButtonModel = ultraLowDensity.getModel();
        groundButtonGroup.add(ultraLowDensity);
        
        JRadioButton lowDensity = PWCGButtonFactory.makeRadioButton("Low", "Low Ground Density", "Fewer AI ground units", null, ColorMap.PAPER_FOREGROUND, false, this);
        groundDensityGrid.add(lowDensity);
        groundLowButtonModel = lowDensity.getModel();
        groundButtonGroup.add(lowDensity);

		JRadioButton medDensity = PWCGButtonFactory.makeRadioButton("Med", "Med Ground Density", "Medium numbers of AI ground units", null, ColorMap.PAPER_FOREGROUND, false, this);		
		groundDensityGrid.add(medDensity);
		groundMedButtonModel = medDensity.getModel();
		groundButtonGroup.add(medDensity);
		
		JRadioButton highDensity = PWCGButtonFactory.makeRadioButton("High", "High Ground Density", "Large numbers of AI ground units", null, ColorMap.PAPER_FOREGROUND, false, this);		
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

        aaButtonPanel.add(PWCGLabelFactory.makeDummyLabel(), BorderLayout.WEST);

        JPanel aaDensityGrid = new JPanel(new GridLayout(0,1));
        aaDensityGrid.setOpaque(false);
        
        JLabel aaDensityLabel = PWCGLabelFactory.makePaperLabelLarge(CampaignConfigurationSimpleGUIController.ACTION_SET_AA_DENSITY);
        aaDensityGrid.add(aaDensityLabel);

        JRadioButton lowDensity = PWCGButtonFactory.makeRadioButton("Low", "Low AA Density", "Fewer AA units", null, ColorMap.PAPER_FOREGROUND, false, this);     
        aaDensityGrid.add(lowDensity);
        aaLowButtonModel = lowDensity.getModel();
        aaButtonGroup.add(lowDensity);

        JRadioButton medDensity = PWCGButtonFactory.makeRadioButton("Med", "Med AA Density", "Medium numbers of AA units", null, ColorMap.PAPER_FOREGROUND, false, this);     
        aaDensityGrid.add(medDensity);
        aaMedButtonModel = medDensity.getModel();
        aaButtonGroup.add(medDensity);
        
        JRadioButton highDensity = PWCGButtonFactory.makeRadioButton("High", "High AA Density", "Large numbers of AA units", null, ColorMap.PAPER_FOREGROUND, false, this);       
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

        cpuAllowanceButtonPanel.add(PWCGLabelFactory.makeDummyLabel(), BorderLayout.WEST);

        JPanel cpuAllowanceGrid = new JPanel(new GridLayout(0,1));
        cpuAllowanceGrid.setOpaque(false);
        
        JLabel cpuAllowanceLabel = PWCGLabelFactory.makePaperLabelLarge(CampaignConfigurationSimpleGUIController.ACTION_SET_CPU_ALOWANCE_DENSITY);
        cpuAllowanceGrid.add(cpuAllowanceLabel);

        JRadioButton lowDensity = PWCGButtonFactory.makeRadioButton("Low", "Low CPU Allowance", "Very few CPU expensive units", null, ColorMap.PAPER_FOREGROUND, false, this);     
        cpuAllowanceGrid.add(lowDensity);
        cpuAllowanceLowButtonModel = lowDensity.getModel();
        cpuAllowanceButtonGroup.add(lowDensity);

        JRadioButton medDensity = PWCGButtonFactory.makeRadioButton("Med", "Med CPU Allowance", "Some CPU expensive units", null, ColorMap.PAPER_FOREGROUND, false, this);     
        cpuAllowanceGrid.add(medDensity);
        cpuAllowanceMedButtonModel = medDensity.getModel();
        cpuAllowanceButtonGroup.add(medDensity);
        
        JRadioButton highDensity = PWCGButtonFactory.makeRadioButton("High", "High CPU Allowance", "No consideration of CPU expense", null, ColorMap.PAPER_FOREGROUND, false, this);     
        cpuAllowanceGrid.add(highDensity);
        cpuAllowanceHighButtonModel = highDensity.getModel();
        cpuAllowanceButtonGroup.add(highDensity);

        cpuAllowanceButtonPanel.add(cpuAllowanceGrid, BorderLayout.NORTH);
        
        return cpuAllowanceButtonPanel;
    }
    
    private JPanel createStructureConfigPanel() throws PWCGException
    {
        JPanel structureButtonPanel = new JPanel(new BorderLayout());
        structureButtonPanel.setOpaque(false);

        structureButtonPanel.add(PWCGLabelFactory.makeDummyLabel(), BorderLayout.WEST);

        JPanel structureGrid = new JPanel(new GridLayout(0,1));
        structureGrid.setOpaque(false);
        
        JLabel structureLabel = PWCGLabelFactory.makePaperLabelLarge(CampaignConfigurationSimpleGUIController.ACTION_SET_STRUCTURE_DENSITY);
        structureGrid.add(structureLabel);

        JRadioButton lowDensity = PWCGButtonFactory.makeRadioButton("Low", "Low Structure", "Structures limited to mission box", null, ColorMap.PAPER_FOREGROUND, false, this);     
        structureGrid.add(lowDensity);
        structureLowButtonModel = lowDensity.getModel();
        structureButtonGroup.add(lowDensity);

        JRadioButton medDensity = PWCGButtonFactory.makeRadioButton("Med", "Med Structure", "Structures extend to players field", null, ColorMap.PAPER_FOREGROUND, false, this);     
        structureGrid.add(medDensity);
        structureMedButtonModel = medDensity.getModel();
        structureButtonGroup.add(medDensity);
        
        JRadioButton highDensity = PWCGButtonFactory.makeRadioButton("High", "High Structure", "Structures extend beyond players field", null, ColorMap.PAPER_FOREGROUND, false, this);     
        structureGrid.add(highDensity);
        structureHighButtonModel = highDensity.getModel();
        structureButtonGroup.add(highDensity);

        structureButtonPanel.add(structureGrid, BorderLayout.NORTH);
        
        return structureButtonPanel;
    }

	public void actionPerformed(ActionEvent ae)
	{
		try
		{
	        String action = ae.getActionCommand();
	        CampaignConfigurationSimpleGUIController controller = new CampaignConfigurationSimpleGUIController();
	        controller.setSimpleConfig(action);
		}
		catch (Exception e)
		{
			PWCGLogger.logException(e);
			ErrorDialog.internalError(e.getMessage());
		}
	}	
}

