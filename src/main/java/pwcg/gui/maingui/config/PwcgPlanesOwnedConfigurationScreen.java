package pwcg.gui.maingui.config;

import java.awt.BorderLayout;
import javafx.scene.paint.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
import pwcg.campaign.utils.PlanesOwnedManager;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.PWCGLogger;
import pwcg.gui.CampaignGuiContextManager;
import pwcg.gui.ScreenIdentifier;
import pwcg.gui.UiImageResolver;
import pwcg.gui.colors.ColorMap;
import pwcg.gui.dialogs.ErrorDialog;
import pwcg.gui.dialogs.PWCGMonitorSupport;
import pwcg.gui.dialogs.PWCGMonitorSupport.MonitorSize;
import pwcg.gui.maingui.PwcgMainScreen;
import pwcg.gui.utils.ContextSpecificImages;
import pwcg.gui.utils.ImageButton;
import pwcg.gui.utils.ImagePanelLayout;
import pwcg.gui.utils.ImageResizingPanel;
import pwcg.gui.utils.ButtonFactory;
import pwcg.gui.utils.ScrollBarWrapper;

public class PwcgPlanesOwnedConfigurationScreen extends ImageResizingPanel implements ActionListener
{
    private static final long serialVersionUID = 1L;

    private PwcgMainScreen parent = null;
    private Map<String, PlaneOwned> selectionBoxes = new HashMap<String, PlaneOwned>();
    
	public PwcgPlanesOwnedConfigurationScreen(PwcgMainScreen parent) 
	{
        super("");
        this.setLayout(new BorderLayout());

		this.parent = parent;
	}

    public void makePanels()
    {
        try
        {
            String imagePath = UiImageResolver.getImage(ScreenIdentifier.PwcgPlanesOwnedConfigurationScreen);
            this.setImageFromName(imagePath);
            
            this.add(makeButtonPanel(), BorderLayout.WEST);
            this.add(makeCenterPanel(), BorderLayout.CENTER);

            this.setCheckBoxes();
        }
        catch (Exception e)
        {
            PWCGLogger.logException(e);
            ErrorDialog.internalError(e.getMessage());
        }
    }

	public void setCheckBoxes()
	{
		try
		{
			PlanesOwnedManager planesOwnedManager = PlanesOwnedManager.getInstance();
	
			for (PlaneOwned selectionBox : selectionBoxes.values())
			{				
				if (planesOwnedManager.isPlaneOwned(selectionBox.plane.getType()))
				{
					selectionBox.checkBox.setSelected(true);
				}
				else			
				{
					selectionBox.checkBox.setSelected(false);
				}
			}
			
		}
		catch (Exception e)
		{
			PWCGLogger.logException(e);
			ErrorDialog.internalError(e.getMessage());
		}
	}

	public Pane makeButtonPanel() throws PWCGException 
	{
        Pane navPanel = new Pane(new BorderLayout());
        navPanel.setOpaque(false);

        Pane buttonPanel = new Pane(new GridLayout(0,1));
        buttonPanel.setOpaque(false);

        for (int i = 0; i < 2; ++i)
        {
            Label spacerLabel = new Label("     ");
            spacerLabel.setAlignment(SwingConstants.LEFT);
            spacerLabel.setOpaque(false);
            
            buttonPanel.add(spacerLabel);
        }
        
        Button selectAllButton = ButtonFactory.makeTranslucentMenuButton("Select All", "Select All", "Select all planes as owned", this);
        buttonPanel.add(selectAllButton);

        Button deselectAllButton = ButtonFactory.makeTranslucentMenuButton("Deselect All", "Deselect All", "Select all planes as not owned", this);
        buttonPanel.add(deselectAllButton);
        

        for (int i = 0; i < 3; ++i)
        {
            Label spacerLabel = new Label("     ");
            spacerLabel.setAlignment(SwingConstants.LEFT);
            spacerLabel.setOpaque(false);
            
            buttonPanel.add(spacerLabel);
        }

        Button acceptButton = ButtonFactory.makeTranslucentMenuButton("Accept", "Accept", "Accept planes owned", this);
        buttonPanel.add(acceptButton);

        Button cancelButton = ButtonFactory.makeTranslucentMenuButton("Cancel", "Cancel", "Cancel planes owned edits", this);
        buttonPanel.add(cancelButton);

        navPanel.add (buttonPanel, BorderLayout.NORTH);
        
        return navPanel;
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
        
        planeListOuterPanel.add(planeListScroll, BorderLayout.CENTER);
        
        return planeListOuterPanel;
	}

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
			CheckBox b1 = ImageButton.makeCheckBox(plane.getDisplayName(), plane.getType());

			b1.addActionListener(this);
			b1.setBackground(buttonBG);
			b1.setOpaque(false);
			planeListPanel.add(b1);
			
			PlaneOwned owned = new PlaneOwned();
			owned.plane = plane;
			owned.checkBox = b1;
			selectionBoxes.put(plane.getDisplayName(), owned);
		}
        
        return planeListPanel;
    }

	public void actionPerformed(ActionEvent ae)
	{
		try
		{
			if (ae.getActionCommand().equalsIgnoreCase("Select All"))
			{
				for (PlaneOwned box: selectionBoxes.values())
				{
					box.checkBox.setSelected(true);
				}
			}
			if (ae.getActionCommand().equalsIgnoreCase("Deselect All"))
			{
				for (PlaneOwned box: selectionBoxes.values())
				{
					box.checkBox.setSelected(false);
				}
			}
			if (ae.getActionCommand().equalsIgnoreCase("Accept"))
			{
					PlanesOwnedManager planesOwnedManager = PlanesOwnedManager.getInstance();
					planesOwnedManager.clear();
					
					for (PlaneOwned box: selectionBoxes.values())
					{
						CheckBox selectionBox = box.checkBox;
						
						if (selectionBox.isSelected())
						{
							planesOwnedManager.setPlaneOwned(box.plane.getType());
						}
					}
					
					planesOwnedManager.write();
 
					parent.refresh();
					
			        CampaignGuiContextManager.getInstance().popFromContextStack();
			}
			if (ae.getActionCommand().equalsIgnoreCase("Cancel"))
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

	private class PlaneOwned
	{
		CheckBox checkBox = null;
		PlaneType plane;
	}
}

