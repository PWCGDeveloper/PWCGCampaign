package pwcg.gui.campaign.config;

import java.awt.BorderLayout;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javafx.scene.control.ButtonGroup;
import javafx.scene.control.ButtonModel;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javax.swing.RadioButton ;

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
import pwcg.gui.utils.ButtonFactory;
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

    private ButtonGroup structureButtonGroup = new ButtonGroup();
    private ButtonModel structureLowButtonModel = null;
    private ButtonModel structureMedButtonModel = null;
    private ButtonModel structureHighButtonModel = null;

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

	private Pane makeNavigatePanel() throws PWCGException
	{
        Pane simpleConfigAcceptPanel = new Pane(new BorderLayout());
        simpleConfigAcceptPanel.setOpaque(false);

        Pane buttonPanel = new Pane(new GridLayout(0,1));
        buttonPanel.setOpaque(false);

        Button acceptButton = ButtonFactory.makeTranslucentMenuButton("Accept", CommonUIActions.ACTION_ACCEPT, "Accept configuration changes", this);
        buttonPanel.add(acceptButton);
        
        Button cancelButton = ButtonFactory.makeTranslucentMenuButton("Cancel", CommonUIActions.ACTION_CANCEL, "Cancel configuration changes", this);
        buttonPanel.add(cancelButton);

        Label spacer1 = ButtonFactory.makePaperLabelLarge("   ");
        buttonPanel.add(spacer1);

        Label spacer2
        = ButtonFactory.makePaperLabelLarge("   ");
        buttonPanel.add(spacer2);

        Button resetButton = ButtonFactory.makeTranslucentMenuButton("Reset", CommonUIActions.ACTION_RESET, "Reset all configurations to default", this);
        buttonPanel.add(resetButton);
		
		simpleConfigAcceptPanel.add(buttonPanel, BorderLayout.NORTH);

		return simpleConfigAcceptPanel;
	}

	private Pane makeConfigControlPanel() throws PWCGException
	{
		Pane airButtonPanel = createAirConfigPanel();
        Pane groundButtonPanel = createGroundConfigPanel();
        Pane aaButtonPanel = createAAConfigPanel();
        Pane cpuAllowanceButtonPanel = createCpuAllowanceConfigPanel();
        Pane structureButtonPanel = createStructureConfigPanel();
        
        String imagePath = UiImageResolver.getImage(ScreenIdentifier.Document);
        ImageResizingPanel simpleConfigButtonPanel = ImageResizingPanelBuilder.makeImageResizingPanel(imagePath);
        simpleConfigButtonPanel.setImageFromName(imagePath);
        simpleConfigButtonPanel.setLayout(new GridLayout(0,1));
        simpleConfigButtonPanel.setBorder(PwcgBorderFactory.createStandardDocumentBorder());

		simpleConfigButtonPanel.add(airButtonPanel);
        simpleConfigButtonPanel.add(groundButtonPanel);
        simpleConfigButtonPanel.add(aaButtonPanel);
        simpleConfigButtonPanel.add(cpuAllowanceButtonPanel);
        simpleConfigButtonPanel.add(structureButtonPanel);
				
		return simpleConfigButtonPanel;
	}

    private Pane createAirConfigPanel() throws PWCGException
    {
        Pane airButtonPanel = new Pane(new BorderLayout());
        airButtonPanel.setOpaque(false);

        Label spacerLabel = makeLabel("          ");        
        airButtonPanel.add(spacerLabel, BorderLayout.WEST);

        Pane airButtonPanelGrid = new Pane(new GridLayout(0,1));
        airButtonPanelGrid.setOpaque(false);
        
        Label airDensityLabel = makeLabel(CampaignConfigurationSimpleGUIController.ACTION_SET_AIR_DENSITY + ":");      
        airButtonPanelGrid.add(airDensityLabel);

        RadioButton  airLowDensity = ButtonFactory.makeRadioButton("Low", "Low Air Density", "Fewer aircraft for average machines", false, this, ColorMap.PAPER_FOREGROUND);       
        airButtonPanelGrid.add(airLowDensity);
        airLowButtonModel = airLowDensity.getModel();
        airButtonGroup.add(airLowDensity);

        RadioButton  airMedDensity = ButtonFactory.makeRadioButton("Med", "Med Air Density", "Medium number of aircraft - requires a pretty good machine", false, this, ColorMap.PAPER_FOREGROUND);        
        airButtonPanelGrid.add(airMedDensity);
        airMedButtonModel = airMedDensity.getModel();
        airButtonGroup.add(airMedDensity);
        
        RadioButton  airHighDensity = ButtonFactory.makeRadioButton("High", "High Air Density", "High number of aircraft - high end machines only", false, this, ColorMap.PAPER_FOREGROUND);       
        airButtonPanelGrid.add(airHighDensity);
        airHighButtonModel = airHighDensity.getModel();
        airButtonGroup.add(airHighDensity);
                
        airButtonPanel.add(airButtonPanelGrid, BorderLayout.NORTH);

        return airButtonPanel;
    }

    private Pane createGroundConfigPanel() throws PWCGException
    {
        Pane groundButtonPanel = new Pane(new BorderLayout());
		groundButtonPanel.setOpaque(false);

        Label spacerLabel = makeLabel("          ");        
        groundButtonPanel.add(spacerLabel, BorderLayout.WEST);

		Pane groundDensityGrid = new Pane(new GridLayout(0,1));
		groundDensityGrid.setOpaque(false);
		
        Label groundDensityLabel = ButtonFactory.makePaperLabelLarge(CampaignConfigurationSimpleGUIController.ACTION_SET_GROUND_DENSITY + ":");
		groundDensityGrid.add(groundDensityLabel);

		RadioButton  lowDensity = ButtonFactory.makeRadioButton("Low", "Low Ground Density", "Fewer AI ground units", false, this, ColorMap.PAPER_FOREGROUND);
		groundDensityGrid.add(lowDensity);
		groundLowButtonModel = lowDensity.getModel();
		groundButtonGroup.add(lowDensity);

		RadioButton  medDensity = ButtonFactory.makeRadioButton("Med", "Med Ground Density", "Medium numbers of AI ground units", false, this, ColorMap.PAPER_FOREGROUND);		
		groundDensityGrid.add(medDensity);
		groundMedButtonModel = medDensity.getModel();
		groundButtonGroup.add(medDensity);
		
		RadioButton  highDensity = ButtonFactory.makeRadioButton("High", "High Ground Density", "Large numbers of AI ground units", false, this, ColorMap.PAPER_FOREGROUND);		
		groundDensityGrid.add(highDensity);
		groundHighButtonModel = highDensity.getModel();
		groundButtonGroup.add(highDensity);

		groundButtonPanel.add(groundDensityGrid, BorderLayout.NORTH);
        
        return groundButtonPanel;
    }

    private Pane createAAConfigPanel() throws PWCGException
    {
        Pane aaButtonPanel = new Pane(new BorderLayout());
        aaButtonPanel.setOpaque(false);

        Label spacerLabel = makeLabel("          ");        
        aaButtonPanel.add(spacerLabel, BorderLayout.WEST);

        Pane aaDensityGrid = new Pane(new GridLayout(0,1));
        aaDensityGrid.setOpaque(false);
        
        Label aaDensityLabel = ButtonFactory.makePaperLabelLarge(CampaignConfigurationSimpleGUIController.ACTION_SET_AA_DENSITY + ":");
        aaDensityGrid.add(aaDensityLabel);

        RadioButton  lowDensity = ButtonFactory.makeRadioButton("Low", "Low AA Density", "Fewer AA units", false, this, ColorMap.PAPER_FOREGROUND);     
        aaDensityGrid.add(lowDensity);
        aaLowButtonModel = lowDensity.getModel();
        aaButtonGroup.add(lowDensity);

        RadioButton  medDensity = ButtonFactory.makeRadioButton("Med", "Med AA Density", "Medium numbers of AA units", false, this, ColorMap.PAPER_FOREGROUND);     
        aaDensityGrid.add(medDensity);
        aaMedButtonModel = medDensity.getModel();
        aaButtonGroup.add(medDensity);
        
        RadioButton  highDensity = ButtonFactory.makeRadioButton("High", "High AA Density", "Large numbers of AA units", false, this, ColorMap.PAPER_FOREGROUND);       
        aaDensityGrid.add(highDensity);
        aaHighButtonModel = highDensity.getModel();
        aaButtonGroup.add(highDensity);

        aaButtonPanel.add(aaDensityGrid, BorderLayout.NORTH);
        
        return aaButtonPanel;
    }
    
    private Pane createCpuAllowanceConfigPanel() throws PWCGException
    {
        Pane cpuAllowanceButtonPanel = new Pane(new BorderLayout());
        cpuAllowanceButtonPanel.setOpaque(false);

        Label spacerLabel = makeLabel("          ");        
        cpuAllowanceButtonPanel.add(spacerLabel, BorderLayout.WEST);

        Pane cpuAllowanceGrid = new Pane(new GridLayout(0,1));
        cpuAllowanceGrid.setOpaque(false);
        
        Label cpuAllowanceLabel = ButtonFactory.makePaperLabelLarge(CampaignConfigurationSimpleGUIController.ACTION_SET_CPU_ALOWANCE_DENSITY + ":");
        cpuAllowanceGrid.add(cpuAllowanceLabel);

        RadioButton  lowDensity = ButtonFactory.makeRadioButton("Low", "Low CPU Allowance", "Very few CPU expensive units", false, this, ColorMap.PAPER_FOREGROUND);     
        cpuAllowanceGrid.add(lowDensity);
        cpuAllowanceLowButtonModel = lowDensity.getModel();
        cpuAllowanceButtonGroup.add(lowDensity);

        RadioButton  medDensity = ButtonFactory.makeRadioButton("Med", "Med CPU Allowance", "Some CPU expensive units", false, this, ColorMap.PAPER_FOREGROUND);     
        cpuAllowanceGrid.add(medDensity);
        cpuAllowanceMedButtonModel = medDensity.getModel();
        cpuAllowanceButtonGroup.add(medDensity);
        
        RadioButton  highDensity = ButtonFactory.makeRadioButton("High", "High CPU Allowance", "No consideration of CPU expense", false, this, ColorMap.PAPER_FOREGROUND);     
        cpuAllowanceGrid.add(highDensity);
        cpuAllowanceHighButtonModel = highDensity.getModel();
        cpuAllowanceButtonGroup.add(highDensity);

        cpuAllowanceButtonPanel.add(cpuAllowanceGrid, BorderLayout.NORTH);
        
        return cpuAllowanceButtonPanel;
    }
    
    private Pane createStructureConfigPanel() throws PWCGException
    {
        Pane structureButtonPanel = new Pane(new BorderLayout());
        structureButtonPanel.setOpaque(false);

        Label spacerLabel = makeLabel("          ");        
        structureButtonPanel.add(spacerLabel, BorderLayout.WEST);

        Pane structureGrid = new Pane(new GridLayout(0,1));
        structureGrid.setOpaque(false);
        
        Label structureLabel = ButtonFactory.makePaperLabelLarge(CampaignConfigurationSimpleGUIController.ACTION_SET_STRUCTURE_DENSITY + ":");
        structureGrid.add(structureLabel);

        RadioButton  lowDensity = ButtonFactory.makeRadioButton("Low", "Low Structure", "Structures limited to mission box", false, this, ColorMap.PAPER_FOREGROUND);     
        structureGrid.add(lowDensity);
        structureLowButtonModel = lowDensity.getModel();
        structureButtonGroup.add(lowDensity);

        RadioButton  medDensity = ButtonFactory.makeRadioButton("Med", "Med Structure", "Structures extend to players field", false, this, ColorMap.PAPER_FOREGROUND);     
        structureGrid.add(medDensity);
        structureMedButtonModel = medDensity.getModel();
        structureButtonGroup.add(medDensity);
        
        RadioButton  highDensity = ButtonFactory.makeRadioButton("High", "High Structure", "Structures extend beyond players field", false, this, ColorMap.PAPER_FOREGROUND);     
        structureGrid.add(highDensity);
        structureHighButtonModel = highDensity.getModel();
        structureButtonGroup.add(highDensity);

        structureButtonPanel.add(structureGrid, BorderLayout.NORTH);
        
        return structureButtonPanel;
    }

    

	private Label makeLabel(String buttonName) throws PWCGException
	{
		Color fg = ColorMap.PAPER_FOREGROUND;

		Font font = PWCGMonitorFonts.getPrimaryFontLarge();

		Label button= new Label(buttonName);
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

