package pwcg.gui.maingui.config;

import java.awt.BorderLayout;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javafx.scene.control.Button;
import javax.swing.CheckBox;
import javax.swing.JComponent;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;

import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.plane.PlaneType;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.MissingSkin;
import pwcg.core.utils.PWCGLogger;
import pwcg.core.utils.SkinAnalyzer;
import pwcg.gui.CampaignGuiContextManager;
import pwcg.gui.ScreenIdentifier;
import pwcg.gui.UiImageResolver;
import pwcg.gui.colors.ColorMap;
import pwcg.gui.dialogs.ErrorDialog;
import pwcg.gui.dialogs.HelpDialog;
import pwcg.gui.dialogs.PWCGMonitorFonts;
import pwcg.gui.dialogs.PWCGMonitorSupport;
import pwcg.gui.dialogs.PWCGMonitorSupport.MonitorSize;
import pwcg.gui.maingui.PwcgMainScreen;
import pwcg.gui.maingui.SkinReportGenerator;
import pwcg.gui.utils.ContextSpecificImages;
import pwcg.gui.utils.ImagePanelLayout;
import pwcg.gui.utils.ImageResizingPanel;
import pwcg.gui.utils.ButtonFactory;
import pwcg.gui.utils.ScrollBarWrapper;

public class PwcgSkinConfigurationAnalysisScreen extends ImageResizingPanel implements ActionListener
{
    private static final long serialVersionUID = 1L;

    private PwcgMainScreen parent = null;
	private Map<String, CheckBox> selectionBoxes = new HashMap<String, CheckBox>();
	private SkinAnalyzer skinAnalyzer = new SkinAnalyzer();

	public PwcgSkinConfigurationAnalysisScreen(PwcgMainScreen parent) 
	{
        super("");
        this.setLayout(new BorderLayout());
        this.setOpaque(false);

		this.parent = parent;
	}

    public void makePanels()
    {
        try
        {
            String imagePath = UiImageResolver.getImage(ScreenIdentifier.PwcgSkinConfigurationAnalysisScreen);
            this.setImageFromName(imagePath);
            
            skinAnalyzer.analyze();
            
            this.add(BorderLayout.WEST, makeButtonPanel());
            this.add(BorderLayout.CENTER, makeCenterPanel());
        }
        catch (Exception e)
        {
            PWCGLogger.logException(e);
            ErrorDialog.internalError(e.getMessage());
        }
    }

	public Pane makeButtonPanel() throws PWCGException 
	{
        Pane navButtonPanel = new Pane(new BorderLayout());
        navButtonPanel.setOpaque(false);

        Pane buttonPanel = new Pane(new GridLayout(0,1));
        buttonPanel.setOpaque(false);

        for (int i = 0; i < 2; ++i)
        {
            Label spacerLabel = new Label("     ");
            spacerLabel.setAlignment(SwingConstants.LEFT);
            spacerLabel.setOpaque(false);
            
            buttonPanel.add(spacerLabel);
        }
        
        Button selectAllButton = ButtonFactory.makeTranslucentMenuButton("Select All", "SelectAll", "Select all planes for analysis", this);
        buttonPanel.add(selectAllButton);

        Button deselectAllButton = ButtonFactory.makeTranslucentMenuButton("Deselect All", "DeselectAll", "Deselect all planes for analysis", this);
        buttonPanel.add(deselectAllButton);
        
        Button displayMissingButton = ButtonFactory.makeTranslucentMenuButton("Display Missing", "DisplayMissing", "Display missing skins for selected planes", this);
        buttonPanel.add(displayMissingButton);
        
        Button displayConfigButton = ButtonFactory.makeTranslucentMenuButton("Display Configuration", "DisplayConfig", "Display configured skins for selected planes", this);
        buttonPanel.add(displayConfigButton);
        
        Button reportButton = ButtonFactory.makeTranslucentMenuButton("Report", "Report", "Generate report on missing skins for selected planes", this);
        buttonPanel.add(reportButton);
        

        for (int i = 0; i < 3; ++i)
        {
            Label spacerLabel = new Label("     ");
            spacerLabel.setAlignment(SwingConstants.LEFT);
            spacerLabel.setOpaque(false);
            
            buttonPanel.add(spacerLabel);
        }

        Button finishedButton = ButtonFactory.makeTranslucentMenuButton("Finished", "Return", "Return to main menu", this);
        buttonPanel.add(finishedButton);

        navButtonPanel.add (buttonPanel, BorderLayout.NORTH);
        
        return navButtonPanel;
 	}

    public Pane makeCenterPanel() throws PWCGException 
    {
        String imagePath = ContextSpecificImages.imagesMisc() + "paperFull.jpg";
        Pane planeSelectionPanel = new ImagePanelLayout(imagePath, new BorderLayout());

        JComponent alliedPanel = makeAlliedPanel();
        JComponent axisPanel = makeAxisPanel();
        JComponent blankPanel = makeBlankPanel();

        planeSelectionPanel.add (alliedPanel, BorderLayout.WEST);
        planeSelectionPanel.add (blankPanel, BorderLayout.CENTER);
        planeSelectionPanel.add (axisPanel, BorderLayout.EAST);
        
        return planeSelectionPanel;
    }

    public Pane makeBlankPanel() throws PWCGException 
    {        
        Pane blankPanel = new Pane(new GridLayout(0, 2));

        blankPanel.setOpaque(false);


        return blankPanel;
    }

    public Pane makeAxisPanel() throws PWCGException 
    {
        List<PlaneType> axisPlanes = PWCGContext.getInstance().getPlaneTypeFactory().getAxisPlanes();
        return makePlanePanel(axisPlanes);
    }

    public Pane makeAlliedPanel() throws PWCGException 
    {
        List<PlaneType> alliedPlanes = PWCGContext.getInstance().getPlaneTypeFactory().getAlliedPlanes();
        return makePlanePanel(alliedPlanes);
    }

	public Pane makePlanePanel(List<PlaneType> planes) throws PWCGException 
	{
        Pane planeListOuterPanel = new Pane(new BorderLayout());
        planeListOuterPanel.setOpaque(false);
		
		TreeMap<String, PlaneType> planeMap = sortPlanesByType(planes);
		
		Pane planeListPanel = createPlanePanel(planeMap);
		
        JScrollPane planeListScroll = ScrollBarWrapper.makeScrollPane(planeListPanel);
        
        planeListOuterPanel.add(planeListScroll, BorderLayout.NORTH);
        
        return planeListOuterPanel;
	}

    private TreeMap<String, PlaneType> sortPlanesByType(List<PlaneType> planes)
    {
        TreeMap<String, PlaneType> planeMap = new TreeMap<String, PlaneType>();
        for (int i = 0; i < planes.size(); ++i)
        {
            PlaneType plane = planes.get(i);
            planeMap.put(plane.getType(), plane);
        }
        return planeMap;
    }

    private Pane createPlanePanel(TreeMap<String, PlaneType> planeMap) throws PWCGException
    {
        MonitorSize monitorSize = PWCGMonitorSupport.getFrameWidth();
        int columns = 2;
        if (monitorSize == MonitorSize.FRAME_LARGE)
        {
            columns = 3;
        }
        
        Pane planeListPanel = new Pane(new GridLayout(0, columns));
        planeListPanel.setOpaque(false);

        Color buttonBG = ColorMap.PAPERPART_BACKGROUND;
        
        for (PlaneType plane : planeMap.values())
		{
			CheckBox planeCheckBox = makeCheckBox(plane);

			planeCheckBox.addActionListener(this);
			planeCheckBox.setBackground(buttonBG);
			planeCheckBox.setOpaque(false);
			planeListPanel.add(planeCheckBox);

			selectionBoxes.put(plane.getType(), planeCheckBox);
		}
        
        return planeListPanel;
    }

    private CheckBox makeCheckBox(PlaneType plane) throws PWCGException 
    {
        CheckBox checkBox= new CheckBox();
        
        Font font = PWCGMonitorFonts.getPrimaryFont();

        checkBox.setFont(font);
        checkBox.setAlignment(Label.LEFT);
        checkBox.setOpaque(false);
        checkBox.setSize(300, 50);
        checkBox.setName(plane.getType());
        checkBox.setText(plane.getDisplayName());

        return checkBox;
    }

	public void actionPerformed(ActionEvent ae)
	{
		try
		{
			if (ae.getActionCommand().equalsIgnoreCase("SelectAll"))
			{
				selectAllPlanes();
			}
			if (ae.getActionCommand().equalsIgnoreCase("DeselectAll"))
			{
				deselectAllPlanes();
			}
            if (ae.getActionCommand().equalsIgnoreCase("DisplayMissing"))
            {
                displayMissingSkinsForSelectedPlanes();
            }
            if (ae.getActionCommand().equalsIgnoreCase("DisplayConfig"))
            {
                displayConfiguredSkinsForSelectedPlanes();
            }
            if (ae.getActionCommand().equalsIgnoreCase("Report"))
            {
                generateReportMissingSkinsForSelectedPlanes();
            }
			if (ae.getActionCommand().equalsIgnoreCase("Return"))
			{
                parent.refresh();

                CampaignGuiContextManager.getInstance().popFromContextStack();
			}
		}
		catch (Exception e)
		{
			PWCGLogger.logException(e);
			ErrorDialog.internalError(e.getMessage());
		}
	}

    private void selectAllPlanes()
    {
        for (CheckBox checkBox: selectionBoxes.values())
        {
            checkBox.setSelected(true);
        }
    }

    private void deselectAllPlanes()
    {
        for (CheckBox checkBox: selectionBoxes.values())
        {
            checkBox.setSelected(false);
        }
    }

    private void displayMissingSkinsForSelectedPlanes()
    {
        try
        {
            Map<String, List<MissingSkin>> skinsToDisplay = formSelectedSkinsUnique();
            
            MissingSkinScreen configurationMissingSkinDisplayPanelSet = new MissingSkinScreen(skinsToDisplay);
            configurationMissingSkinDisplayPanelSet.makeGUI();
    
            CampaignGuiContextManager.getInstance().pushToContextStack(configurationMissingSkinDisplayPanelSet);
        }
        catch (PWCGException e)
        {
            ErrorDialog.internalError("Could not create missing skin report page: " + e.getMessage());
        }
    }

    private void displayConfiguredSkinsForSelectedPlanes()
    {
        try
        {
            Map<String, PlaneType> planeTypesToDisplay = new TreeMap<String, PlaneType>();
            for (String planeTypeDesc: selectionBoxes.keySet())
            {
                CheckBox selectionBox = selectionBoxes.get(planeTypeDesc);
                if (selectionBox.isSelected())
                {
                    PlaneType planeType = PWCGContext.getInstance().getPlaneTypeFactory().createPlaneTypeByAnyName(planeTypeDesc);
                    planeTypesToDisplay.put(planeTypeDesc, planeType);
                }
            }
            
            PwcgSkinConfigurationAnalysisDisplayScreen configurationSkinConfigDisplayPanelSet = new PwcgSkinConfigurationAnalysisDisplayScreen(planeTypesToDisplay);
            configurationSkinConfigDisplayPanelSet.makeGUI();
            CampaignGuiContextManager.getInstance().pushToContextStack(configurationSkinConfigDisplayPanelSet);
        }
        catch (PWCGException e)
        {
            ErrorDialog.internalError("Could not create missing skin report page: " + e.getMessage());
        }
    }

    private void generateReportMissingSkinsForSelectedPlanes()
    {
        try
        {
            Map<String, List<MissingSkin>> skinsToWrite = formSelectedSkinsUnique();
            
            SkinReportGenerator skinReportGenerator = new SkinReportGenerator();
            String reportPath = skinReportGenerator.writeMissingSkinReport(skinsToWrite);
            
            new  HelpDialog("See missing skin report at:" + reportPath);
        }
        catch (IOException e)
        {
            ErrorDialog.internalError("Could not write missing skin report: " + e.getMessage());
        }
        catch (PWCGException e)
        {
            ErrorDialog.internalError("Could not write missing skin report: " + e.getMessage());
        }
    }

    private Map<String, List<MissingSkin>> formSelectedSkinsUnique()
    {
        Map<String, List<MissingSkin>> selectedMissingSkins = new HashMap<String, List<MissingSkin>>();

        for (String planeType: selectionBoxes.keySet())
        {
            Map<String, MissingSkin> selectedMissingSkinsForPlane = new TreeMap<String, MissingSkin>();
            
            CheckBox selectionBox = selectionBoxes.get(planeType);
            if (selectionBox.isSelected())
            {
                List<MissingSkin> missingSkinsForPlane = skinAnalyzer.getMissingSkinsForPlane(planeType);
                
                for (MissingSkin missingSkin : missingSkinsForPlane)
                {
                    selectedMissingSkinsForPlane.put(missingSkin.getSkinName(), missingSkin);
                }
                
                List<MissingSkin> missingSkinListForPlane = new ArrayList<MissingSkin>(selectedMissingSkinsForPlane.values());
                selectedMissingSkins.put(planeType, missingSkinListForPlane);
            }
        }
        
        return selectedMissingSkins;
    }
}

