package pwcg.gui.maingui.config;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
import pwcg.gui.utils.PWCGButtonFactory;
import pwcg.gui.utils.PWCGLabelFactory;
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

	public JPanel makeButtonPanel() throws PWCGException 
	{
        JPanel navPanel = new JPanel(new BorderLayout());
        navPanel.setOpaque(false);

        JPanel buttonPanel = new JPanel(new GridLayout(0,1));
        buttonPanel.setOpaque(false);

        for (int i = 0; i < 2; ++i)
        {
            buttonPanel.add(PWCGLabelFactory.makeDummyLabel());
        }
        
        JButton selectAllButton = PWCGButtonFactory.makeTranslucentMenuButton("Select All", "Select All", "Select all planes as owned", this);
        buttonPanel.add(selectAllButton);

        JButton deselectAllButton = PWCGButtonFactory.makeTranslucentMenuButton("Deselect All", "Deselect All", "Select all planes as not owned", this);
        buttonPanel.add(deselectAllButton);
        

        for (int i = 0; i < 3; ++i)
        {
            buttonPanel.add(PWCGLabelFactory.makeDummyLabel());
        }

        JButton acceptButton = PWCGButtonFactory.makeTranslucentMenuButton("Accept", "Accept", "Accept planes owned", this);
        buttonPanel.add(acceptButton);

        JButton cancelButton = PWCGButtonFactory.makeTranslucentMenuButton("Cancel", "Cancel", "Cancel planes owned edits", this);
        buttonPanel.add(cancelButton);

        navPanel.add (buttonPanel, BorderLayout.NORTH);
        
        return navPanel;
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
        
        planeListOuterPanel.add(planeListScroll, BorderLayout.CENTER);
        
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
			JCheckBox b1 = ImageButton.makeCheckBox(plane.getDisplayName(), plane.getType());

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
						JCheckBox selectionBox = box.checkBox;
						
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
		JCheckBox checkBox = null;
		TankType plane;
	}
}

