package pwcg.gui.maingui;

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
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;

import pwcg.campaign.context.PWCGContextManager;
import pwcg.campaign.plane.PlaneType;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.Logger;
import pwcg.core.utils.MissingSkin;
import pwcg.core.utils.SkinAnalyzer;
import pwcg.gui.CampaignGuiContextManager;
import pwcg.gui.PwcgGuiContext;
import pwcg.gui.colors.ColorMap;
import pwcg.gui.dialogs.ErrorDialog;
import pwcg.gui.dialogs.HelpDialog;
import pwcg.gui.dialogs.MonitorSupport;
import pwcg.gui.utils.ContextSpecificImages;
import pwcg.gui.utils.ImagePanelLayout;
import pwcg.gui.utils.ImageResizingPanel;
import pwcg.gui.utils.PWCGButtonFactory;
import pwcg.gui.utils.ScrollBarWrapper;
import pwcg.gui.utils.ToolTipManager;

public class ConfigurationSkinAnalysisPanelSet extends PwcgGuiContext implements ActionListener
{
    private static final long serialVersionUID = 1L;

    private CampaignMainGUI parent = null;
	private Map<String, JCheckBox> selectionBoxes = new HashMap<String, JCheckBox>();
	private SkinAnalyzer skinAnalyzer = new SkinAnalyzer();
	
	/**
	 * 
	 */
	public ConfigurationSkinAnalysisPanelSet(CampaignMainGUI parent) 
	{
		setLayout(new BorderLayout());

		this.parent = parent;
	}
	

    /**
     * 
     */
    public void makePanels()
    {
        try
        {
            // Perform the analysis once
            skinAnalyzer.analyze();
            
            setLeftPanel(makeButtonPanel());
            setCenterPanel(makeCenterPanel());
        }
        catch (Exception e)
        {
            Logger.logException(e);
            ErrorDialog.internalError(e.getMessage());
        }
    }

	
	/**
	 * @return
	 * @throws PWCGException
	 */
	public JPanel makeButtonPanel() throws PWCGException 
	{
        String imagePath = getSideImageMain("SkinAnalysisNav.jpg");

        ImageResizingPanel campaignButtonPanel = new ImageResizingPanel(imagePath);
        campaignButtonPanel.setLayout(new BorderLayout());
        campaignButtonPanel.setOpaque(true);

        JPanel buttonPanel = new JPanel(new GridLayout(0,1));
        buttonPanel.setOpaque(false);

        for (int i = 0; i < 2; ++i)
        {
            JLabel spacerLabel = new JLabel("     ");
            spacerLabel.setHorizontalAlignment(SwingConstants.LEFT);
            spacerLabel.setOpaque(false);
            
            buttonPanel.add(spacerLabel);
        }
        
        JButton selectAllButton = makePlainButton("      Select All", "SelectAll", "Select all planes");
        buttonPanel.add(selectAllButton);

        JButton deselectAllButton = makePlainButton("      Deselect All", "DeselectAll", "Deselect all planes");
        buttonPanel.add(deselectAllButton);
        
        JButton displayMissingButton = makePlainButton("      Display Missing", "DisplayMissing", "Display missing skins for selected planes");
        buttonPanel.add(displayMissingButton);
        
        JButton displayConfigButton = makePlainButton("      Display Configuration", "DisplayConfig", "Display configured skins for selected planes");
        buttonPanel.add(displayConfigButton);
        
        JButton reportButton = makePlainButton("      Report", "Report", "Generate report on missing skins for selected planes");
        buttonPanel.add(reportButton);
        

        for (int i = 0; i < 3; ++i)
        {
            JLabel spacerLabel = new JLabel("     ");
            spacerLabel.setHorizontalAlignment(SwingConstants.LEFT);
            spacerLabel.setOpaque(false);
            
            buttonPanel.add(spacerLabel);
        }

        JButton acceptButton = makePlainButton("      Return", "Return", "Return to main menu");
        buttonPanel.add(acceptButton);

        campaignButtonPanel.add (buttonPanel, BorderLayout.NORTH);
        
        return campaignButtonPanel;
 	}

    
    /**
     * @param imageName
     * @return
     * @throws PWCGException 
     * @
     */
    private JButton makePlainButton(String buttonText, String commandText, String toolTiptext) throws PWCGException
    {
        JButton button = PWCGButtonFactory.makeMenuButton(buttonText, commandText, this);
        ToolTipManager.setToolTip(button, toolTiptext);

        return button;
    }



    /**
     * @return
     * @throws PWCGException
     */
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

    
    /**
     * @throws PWCGException 
     * @
     */
    public JPanel makeBlankPanel() throws PWCGException 
    {        
        JPanel blankPanel = new JPanel(new GridLayout(0, 2));

        blankPanel.setOpaque(false);


        return blankPanel;
    }


    /**
     * @throws PWCGException
     */
    public JPanel makeAxisPanel() throws PWCGException 
    {
        List<PlaneType> axisPlanes = PWCGContextManager.getInstance().getPlaneTypeFactory().getAxisPlanes();
        return makePlanePanel(axisPlanes);
    }


    /**
     * @throws PWCGException
     */
    public JPanel makeAlliedPanel() throws PWCGException 
    {
        List<PlaneType> alliedPlanes = PWCGContextManager.getInstance().getPlaneTypeFactory().getAlliedPlanes();
        return makePlanePanel(alliedPlanes);
    }
    
	/**
	 * @throws PWCGException
	 */
	public JPanel makePlanePanel(List<PlaneType> planes) throws PWCGException 
	{
        JPanel planeListOuterPanel = new JPanel(new BorderLayout());
        planeListOuterPanel.setOpaque(false);
		
		TreeMap<String, PlaneType> planeMap = sortPlanesByType(planes);
		
		JPanel planeListPanel = createPlanePanel(planeMap);
		
        JScrollPane planeListScroll = ScrollBarWrapper.makeScrollPane(planeListPanel);
        
        planeListOuterPanel.add(planeListScroll, BorderLayout.NORTH);
        
        return planeListOuterPanel;
	}


    /**
     * @param planes
     * @return
     */
    private TreeMap<String, PlaneType> sortPlanesByType(List<PlaneType> planes)
    {
        TreeMap<String, PlaneType> planeMap = new TreeMap<String, PlaneType>();
        for (int i = 0; i < planes.size(); ++i)
        {
            PlaneType plane = planes.get(i);
            if (plane.isFlyable())
            {
                planeMap.put(plane.getType(), plane);
            }
        }
        return planeMap;
    }


    /**
     * @param buttonBG
     * @param planeMap
     * @throws PWCGException
     */
    private JPanel createPlanePanel(TreeMap<String, PlaneType> planeMap) throws PWCGException
    {
        Dimension frameSize = MonitorSupport.getPWCGFrameSize();
        int columns = 3;
        if (frameSize.getWidth() < 1900)
        {
            columns = 2;
        }
        
        JPanel planeListPanel = new JPanel(new GridLayout(0, columns));
        planeListPanel.setOpaque(false);

        Color buttonBG = ColorMap.PAPERPART_BACKGROUND;
        
        for (PlaneType plane : planeMap.values())
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
    

    /**
     * @param planeDesc
     * @param planeName
     * @return
     * @throws PWCGException 
     * @
     */
    private JCheckBox makeCheckBox(PlaneType plane) throws PWCGException 
    {
        JCheckBox checkBox= new JCheckBox();
        
        Font font = MonitorSupport.getPrimaryFont();

        checkBox.setFont(font);
        checkBox.setHorizontalAlignment(JLabel.LEFT);
        checkBox.setOpaque(false);
        checkBox.setSize(300, 50);
        checkBox.setName(plane.getType());
        checkBox.setText(plane.getDisplayName());

        return checkBox;
    }



	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
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
			Logger.logException(e);
			ErrorDialog.internalError(e.getMessage());
		}
	}


    /**
     * Select all planes
     */
    private void selectAllPlanes()
    {
        for (JCheckBox checkBox: selectionBoxes.values())
        {
            checkBox.setSelected(true);
        }
    }


    /**
     * Deselect allplanes
     */
    private void deselectAllPlanes()
    {
        for (JCheckBox checkBox: selectionBoxes.values())
        {
            checkBox.setSelected(false);
        }
    }


    /**
     * Display missing skins for selected planes
     */
    private void displayMissingSkinsForSelectedPlanes()
    {
        try
        {
            Map<String, List<MissingSkin>> skinsToDisplay = formSelectedSkinsUnique();
            
            ConfigurationSkinMissingDisplayPanelSet configurationMissingSkinDisplayPanelSet = new ConfigurationSkinMissingDisplayPanelSet(skinsToDisplay);
            configurationMissingSkinDisplayPanelSet.makeGUI();
    
            CampaignGuiContextManager.getInstance().pushToContextStack(configurationMissingSkinDisplayPanelSet);
        }
        catch (PWCGException e)
        {
            ErrorDialog.internalError("Could not create missing skin report page: " + e.getMessage());
        }
    }


    /**
     * Display configured skins for selected planes
     */
    private void displayConfiguredSkinsForSelectedPlanes()
    {
        try
        {
            Map<String, PlaneType> planeTypesToDisplay = new TreeMap<String, PlaneType>();
            for (String planeTypeDesc: selectionBoxes.keySet())
            {
                JCheckBox selectionBox = selectionBoxes.get(planeTypeDesc);
                if (selectionBox.isSelected())
                {
                    PlaneType planeType = PWCGContextManager.getInstance().getPlaneTypeFactory().createPlaneTypeByAnyName(planeTypeDesc);
                    planeTypesToDisplay.put(planeTypeDesc, planeType);
                }
            }
            
            ConfigurationSkinConfigDisplayPanelSet configurationSkinConfigDisplayPanelSet = new ConfigurationSkinConfigDisplayPanelSet(planeTypesToDisplay);
            configurationSkinConfigDisplayPanelSet.makeGUI();
    
            CampaignGuiContextManager.getInstance().pushToContextStack(configurationSkinConfigDisplayPanelSet);
        }
        catch (PWCGException e)
        {
            ErrorDialog.internalError("Could not create missing skin report page: " + e.getMessage());
        }
    }

    

    /**
     * Generate a text report for missing skins for selected planes
     */
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
   
    
    /**
     * Display skins for selected planes
     * Shows unique instances (i.e. a skin will appear only one time)
     */
    private Map<String, List<MissingSkin>> formSelectedSkinsUnique()
    {
        Map<String, List<MissingSkin>> selectedMissingSkins = new HashMap<String, List<MissingSkin>>();

        for (String planeType: selectionBoxes.keySet())
        {
            // The map eliminates duplicates
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

