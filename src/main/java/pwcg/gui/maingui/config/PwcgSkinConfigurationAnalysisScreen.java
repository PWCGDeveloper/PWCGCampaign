package pwcg.gui.maingui.config;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.tank.TankType;
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
import pwcg.gui.utils.PWCGButtonFactory;
import pwcg.gui.utils.PWCGLabelFactory;
import pwcg.gui.utils.ScrollBarWrapper;

public class PwcgSkinConfigurationAnalysisScreen extends ImageResizingPanel implements ActionListener
{
    private static final long serialVersionUID = 1L;

    private PwcgMainScreen parent = null;
	private Map<String, JCheckBox> selectionBoxes = new HashMap<String, JCheckBox>();
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

	public JPanel makeButtonPanel() throws PWCGException 
	{
        JPanel navButtonPanel = new JPanel(new BorderLayout());
        navButtonPanel.setOpaque(false);

        JPanel buttonPanel = new JPanel(new GridLayout(0,1));
        buttonPanel.setOpaque(false);

        for (int i = 0; i < 2; ++i)
        {
            buttonPanel.add(PWCGLabelFactory.makeDummyLabel());
        }
        
        JButton selectAllButton = PWCGButtonFactory.makeTranslucentMenuButton("Select All", "SelectAll", "Select all planes for analysis", this);
        buttonPanel.add(selectAllButton);

        JButton deselectAllButton = PWCGButtonFactory.makeTranslucentMenuButton("Deselect All", "DeselectAll", "Deselect all planes for analysis", this);
        buttonPanel.add(deselectAllButton);
        
        JButton displayMissingButton = PWCGButtonFactory.makeTranslucentMenuButton("Display Missing", "DisplayMissing", "Display missing skins for selected planes", this);
        buttonPanel.add(displayMissingButton);
        
        JButton displayConfigButton = PWCGButtonFactory.makeTranslucentMenuButton("Display Configuration", "DisplayConfig", "Display configured skins for selected planes", this);
        buttonPanel.add(displayConfigButton);
        
        JButton reportButton = PWCGButtonFactory.makeTranslucentMenuButton("Report", "Report", "Generate report on missing skins for selected planes", this);
        buttonPanel.add(reportButton);
        

        for (int i = 0; i < 3; ++i)
        {
            buttonPanel.add(PWCGLabelFactory.makeDummyLabel());
        }

        JButton finishedButton = PWCGButtonFactory.makeTranslucentMenuButton("Finished", "Return", "Return to main menu", this);
        buttonPanel.add(finishedButton);

        navButtonPanel.add (buttonPanel, BorderLayout.NORTH);
        
        return navButtonPanel;
 	}

    public JPanel makeCenterPanel() throws PWCGException 
    {
        String imagePath = ContextSpecificImages.imagesMisc() + "paperFull.jpg";
        JPanel planeSelectionPanel = new ImagePanelLayout(imagePath, new BorderLayout());

        JComponent alliedPanel = makeAlliedPanel();
        JComponent axisPanel = makeAxisPanel();
        JComponent blankPanel = makeBlankPanel();

        planeSelectionPanel.add (alliedPanel, BorderLayout.WEST);
        planeSelectionPanel.add (blankPanel, BorderLayout.CENTER);
        planeSelectionPanel.add (axisPanel, BorderLayout.EAST);
        
        return planeSelectionPanel;
    }

    public JPanel makeBlankPanel() throws PWCGException 
    {        
        JPanel blankPanel = new JPanel(new GridLayout(0, 2));

        blankPanel.setOpaque(false);


        return blankPanel;
    }

    public JPanel makeAxisPanel() throws PWCGException 
    {
        List<TankType> axisPlanes = PWCGContext.getInstance().getTankTypeFactory().getAxisPlanes();
        return makePlanePanel(axisPlanes);
    }

    public JPanel makeAlliedPanel() throws PWCGException 
    {
        List<TankType> alliedPlanes = PWCGContext.getInstance().getTankTypeFactory().getAlliedPlanes();
        return makePlanePanel(alliedPlanes);
    }

	public JPanel makePlanePanel(List<TankType> planes) throws PWCGException 
	{
        JPanel planeListOuterPanel = new JPanel(new BorderLayout());
        planeListOuterPanel.setOpaque(false);
		
		TreeMap<String, TankType> planeMap = sortPlanesByType(planes);
		
		JPanel planeListPanel = createPlanePanel(planeMap);
		
        JScrollPane planeListScroll = ScrollBarWrapper.makeScrollPane(planeListPanel);
        
        planeListOuterPanel.add(planeListScroll, BorderLayout.NORTH);
        
        return planeListOuterPanel;
	}

    private TreeMap<String, TankType> sortPlanesByType(List<TankType> planes)
    {
        TreeMap<String, TankType> planeMap = new TreeMap<String, TankType>();
        for (int i = 0; i < planes.size(); ++i)
        {
            TankType plane = planes.get(i);
            planeMap.put(plane.getType(), plane);
        }
        return planeMap;
    }

    private JPanel createPlanePanel(TreeMap<String, TankType> planeMap) throws PWCGException
    {
        MonitorSize monitorSize = PWCGMonitorSupport.getFrameWidth();
        int columns = 2;
        if (monitorSize == MonitorSize.FRAME_LARGE)
        {
            columns = 3;
        }
        
        JPanel planeListPanel = new JPanel(new GridLayout(0, columns));
        planeListPanel.setOpaque(false);

        Color buttonBG = ColorMap.PAPERPART_BACKGROUND;
        
        for (TankType plane : planeMap.values())
		{
			JCheckBox planeCheckBox = makeCheckBox(plane);

			planeCheckBox.addActionListener(this);
			planeCheckBox.setBackground(buttonBG);
			planeCheckBox.setOpaque(false);
			planeListPanel.add(planeCheckBox);

			selectionBoxes.put(plane.getType(), planeCheckBox);
		}
        
        return planeListPanel;
    }

    private JCheckBox makeCheckBox(TankType plane) throws PWCGException 
    {        
        Font font = PWCGMonitorFonts.getPrimaryFont();
        JCheckBox checkBox = PWCGButtonFactory.makeCheckBoxWithDimensions(plane.getType(), plane.getDisplayName(), font, new Dimension(300, 50));
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
        for (JCheckBox checkBox: selectionBoxes.values())
        {
            checkBox.setSelected(true);
        }
    }

    private void deselectAllPlanes()
    {
        for (JCheckBox checkBox: selectionBoxes.values())
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
            Map<String, TankType> planeTypesToDisplay = new TreeMap<String, TankType>();
            for (String planeTypeDesc: selectionBoxes.keySet())
            {
                JCheckBox selectionBox = selectionBoxes.get(planeTypeDesc);
                if (selectionBox.isSelected())
                {
                    TankType planeType = PWCGContext.getInstance().getTankTypeFactory().createTankTypeByAnyName(planeTypeDesc);
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
            
            JCheckBox selectionBox = selectionBoxes.get(planeType);
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

