package pwcg.gui.rofmap.brief;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import pwcg.campaign.context.PWCGContext;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.PWCGLogger;
import pwcg.gui.dialogs.ErrorDialog;
import pwcg.gui.dialogs.PWCGMonitorFonts;
import pwcg.gui.rofmap.brief.model.BriefingData;
import pwcg.gui.rofmap.brief.model.BriefingFlight;
import pwcg.gui.rofmap.brief.model.BriefingMapPoint;
import pwcg.gui.utils.ContextSpecificImages;
import pwcg.gui.utils.ImageResizingPanel;
import pwcg.gui.utils.ImageResizingPanelBuilder;
import pwcg.gui.utils.PWCGButtonFactory;
import pwcg.gui.utils.ScrollBarWrapper;
import pwcg.mission.Mission;
import pwcg.mission.utils.MissionTime;

/**
 * 1. Start - initialize mission parameters and editors
 * 2. Move waypoint
 *      Update waypoint list
 *      Change distances in edit field0
 *      Update map
 * 3. Add waypoint
 *      Clone next WP
 *      Add new WaypointEditorCouplet
 *      Update map
 * 3. Remove waypoint
 *      Remove WaypointEditorCouplet
 *      Update map
 * 4. Edit altitude
 *      Update WaypointEditorCouplet
 * 
 * @author Admin
 *
 */
public class BriefingMapEditorPanel extends ImageResizingPanel implements ActionListener
{
	private static final long serialVersionUID = 1L;

    private JComboBox<String> cbFuel;
    private JComboBox<String> cbMissionTime;
    private JPanel waypointPanel;
    private JPanel editorPanel;
    private JPanel editablePanel;
    private Mission mission;
    private BriefingData briefingContext;
    private WaypointEditorSet waypointEditors = new WaypointEditorSet();

	public BriefingMapEditorPanel(Mission mission, BriefingData briefingContext) throws PWCGException  
	{
		super("");
		this.setLayout(new BorderLayout());
		this.setOpaque(false);
		
        this.mission =  mission;
        this.briefingContext =  briefingContext;

		setLayout(new BorderLayout());		
	}    

	public void makePanels() throws PWCGException 
	{
        String imagePath = ContextSpecificImages.imagesMisc() + "PaperPart.jpg";
        this.setImage(imagePath);

		editorPanel = ImageResizingPanelBuilder.makeImageResizingPanel(imagePath);
		editorPanel.setLayout(new BorderLayout());
		editorPanel.setOpaque(false);

		waypointPanel = new JPanel(new BorderLayout());
		waypointPanel.setOpaque(false);

        JPanel editableLabelPanel = createEditableLabelPanel();
        editorPanel.add(editableLabelPanel, BorderLayout.NORTH);

        editablePanel = new JPanel(new BorderLayout());
        editablePanel.setOpaque(false);
        makeEditablePanel();
        editorPanel.add(editablePanel, BorderLayout.CENTER);

        this.add(editorPanel, BorderLayout.CENTER);
	}
	
	public void rebuildWaypointPanel() throws PWCGException
	{
	    editablePanel.remove(waypointPanel);
	    
        waypointPanel = new JPanel(new BorderLayout());
        waypointPanel.setOpaque(false);
        
        buildWaypointPanel();
        editablePanel.add(waypointPanel, BorderLayout.CENTER);
	}

    private JPanel createEditableLabelPanel() throws PWCGException
    {
        JPanel editableLabelPanel = new JPanel(new GridLayout(0,1));
        editableLabelPanel.setOpaque(false);
        
        JLabel summaryLabel = PWCGButtonFactory.makePaperLabelLarge("Mission Summary");
        editableLabelPanel.add(summaryLabel);
        
        JLabel spacer = PWCGButtonFactory.makePaperLabelLarge("  ");
        editableLabelPanel.add(spacer);
        
        return editableLabelPanel;
    }

    private void makeEditablePanel() throws PWCGException
    {
        editablePanel = new JPanel(new BorderLayout());
        editablePanel.setOpaque(false);

        JPanel dropDownPanel = createDropDownPanel();
        editablePanel.add(dropDownPanel, BorderLayout.NORTH);

        buildWaypointPanel();
        editablePanel.add(waypointPanel, BorderLayout.CENTER);        
    }

    private JPanel createDropDownPanel() throws PWCGException
    {
        JPanel dropDownPanel = new JPanel(new GridLayout(0,1));
        dropDownPanel.setOpaque(false);
        
        createFuelDisplay();
        dropDownPanel.add(cbFuel);

        createTimeDisplay();
        dropDownPanel.add(cbMissionTime);
        
        return dropDownPanel;
    }

    private void buildWaypointPanel() throws PWCGException
    {
        waypointEditors = new WaypointEditorSet();
        
        GridBagConstraints constraints = new GridBagConstraints();
		constraints.fill = GridBagConstraints.HORIZONTAL;
		constraints.ipadx = 3;
		constraints.ipady = 3;
		GridBagLayout gridBagLayout = new GridBagLayout();
		
		JPanel waypointDetailsPanel = new JPanel(gridBagLayout);
		waypointDetailsPanel.setOpaque(false);

		createMissionParametersHeader(constraints, waypointDetailsPanel);
	    
        BriefingFlight activeBriefingFlight = briefingContext.getActiveBriefingFlight();
        
        BriefingMapPoint previousMapPoint = null;
	    for (BriefingMapPoint briefingMapPoint : activeBriefingFlight.getBriefingFlightParameters().getBriefingMapMapPoints())
	    {
	        WaypointEditor waypointEditor = new WaypointEditor(briefingMapPoint.getWaypointID());
	        waypointEditor.initializeWPEdit(previousMapPoint, briefingMapPoint);
	        
			if (mission.isFinalized())
			{
			    briefingMapPoint.setIsEditable(false);
			}
	    	
	        constraints.gridy = constraints.gridy + 1;

			constraints.gridx = 0;
			waypointDetailsPanel.add(waypointEditor.getDescriptionField(), constraints);
			
			constraints.gridx = 1;
			waypointDetailsPanel.add(waypointEditor.getAltitudeTextField(), constraints);
            
            constraints.gridx = 2;
            waypointDetailsPanel.add(waypointEditor.getDistanceTextField(), constraints);
        
            constraints.gridx = 3;
            waypointDetailsPanel.add(waypointEditor.getHeadingtextField(), constraints);
            
            waypointEditors.addWaypointEditor(waypointEditor);
            
            previousMapPoint = briefingMapPoint;
	    }	    
	    
        JScrollPane waypointScrollPane = ScrollBarWrapper.makeScrollPane(waypointDetailsPanel);
        waypointPanel.add(waypointScrollPane, BorderLayout.NORTH);
    }

    private void createTimeDisplay()
    {
        MissionTime missionTime = PWCGContext.getInstance().getCurrentMap().getMissionOptions().getMissionTime();
		
        cbMissionTime = new JComboBox<String>();
        cbMissionTime.setActionCommand("ChangeTime");

        for (String time : missionTime.getMissionTimes())
        {
            cbMissionTime.addItem(time);
        }
        cbMissionTime.setOpaque(false);
        cbMissionTime.setSelectedIndex(missionTime.getIndexForTime());
        String selectedTime = (String)cbMissionTime.getSelectedItem();

        briefingContext.setSelectedTime(selectedTime);
        
        cbMissionTime.addActionListener(this);
        if (mission.isFinalized())
        {
            cbMissionTime.setEnabled(false);
        }
    }

    private void createFuelDisplay()
    {
		cbFuel = new JComboBox<String>();
		cbFuel.addItem("Fuel 100%");
		cbFuel.addItem("Fuel 95%");
		cbFuel.addItem("Fuel 90%");
		cbFuel.addItem("Fuel 85%");
		cbFuel.addItem("Fuel 80%");
		cbFuel.addItem("Fuel 75%");
		cbFuel.addItem("Fuel 70%");
		cbFuel.addItem("Fuel 65%");
        cbFuel.addItem("Fuel 60%");
        cbFuel.addItem("Fuel 55%");
        cbFuel.addItem("Fuel 50%");
        cbFuel.addItem("Fuel 45%");
        cbFuel.addItem("Fuel 40%");
        cbFuel.addItem("Fuel 35%");
        cbFuel.addItem("Fuel 30%");
		cbFuel.setOpaque(false);
		cbFuel.setSelectedIndex(getIndexForFuel());
		cbFuel.setActionCommand("ChangeFuel");
		cbFuel.addActionListener(this);
		if (mission.isFinalized())
		{
			cbFuel.setEnabled(false);
		}
    }

    private void createMissionParametersHeader(GridBagConstraints constraints, JPanel panel) throws PWCGException
    {
        Font font = PWCGMonitorFonts.getPrimaryFontSmall();

        JLabel wpName = new JLabel ("WP");		
        wpName.setFont(font);
        
		wpName.setHorizontalAlignment(JLabel.CENTER);
	    constraints.weightx = 0.15;
		constraints.gridx = 0;
		constraints.gridy = 0;
		panel.add(wpName, constraints);
		
		JLabel altLabel = new JLabel ("Alt (Meters)");
		altLabel.setFont(font);

        altLabel.setHorizontalAlignment(JLabel.CENTER);
	    constraints.weightx = 0.2;
		constraints.gridx = 1;
		constraints.gridy = 0;
		panel.add(altLabel, constraints);
        
        JLabel distLabel = new JLabel ("Dist (Km)");
        distLabel.setFont(font);

        distLabel.setHorizontalAlignment(JLabel.CENTER);
        constraints.weightx = 0.2;
        constraints.gridx = 2;
        constraints.gridy = 0;
        panel.add(distLabel, constraints);
        
        JLabel headingLabel = new JLabel ("Heading");
        headingLabel.setFont(font);
        
        headingLabel.setHorizontalAlignment(JLabel.CENTER);
        constraints.weightx = 0.2;
        constraints.gridx = 3;
        constraints.gridy = 0;
        panel.add(headingLabel, constraints);
    }

	private int getIndexForFuel()
	{
	    int index = 0;
	    
        BriefingFlight activeBriefingFlight = briefingContext.getActiveBriefingFlight();
	    double selectedFuel = activeBriefingFlight.getSelectedFuel();
	    
        if (selectedFuel > .95)
        {
            index = 0;
        }
        else if (selectedFuel > .90)
        {
            index = 1;
        }
        else if (selectedFuel > .85)
        {
            index = 2;
        }
        else if (selectedFuel > .80)
        {
            index = 3;
        }
        else if (selectedFuel > .75)
        {
            index = 4;
        }
        else if (selectedFuel > .70)
        {
            index = 5;
        }
        else if (selectedFuel > .65)
        {
            index = 6;
        }
        else if (selectedFuel > .60)
        {
            index = 7;
        }
        else if (selectedFuel > .55)
        {
            index = 8;
        }
        else if (selectedFuel > .50)
        {
            index = 9;
        }
        else if (selectedFuel > .45)
        {
            index = 10;
        }
        else if (selectedFuel > .40)
        {
            index = 11;
        }
        else if (selectedFuel > .35)
        {
            index = 12;
        }
        else if (selectedFuel > .30)
        {
            index = 13;
        }
        else
        {
            index = 14;
        }
		
		return index;
	}

	@Override
	public void actionPerformed(ActionEvent arg0) 
	{		
		try
		{
			String action = arg0.getActionCommand();
            if (action.equalsIgnoreCase("ChangeFuel"))
            {
                changeFuel();
            }
            else if (action.equalsIgnoreCase("ChangeTime"))
            {
                String selectedTime = (String)cbMissionTime.getSelectedItem();
                briefingContext.setSelectedTime(selectedTime);
            }
		}
		catch (Exception e)
		{
			PWCGLogger.logException(e);
			ErrorDialog.internalError(e.getMessage());
		}
	}

    private void changeFuel()
    {
        String fuelString = (String)cbFuel.getSelectedItem();
        int beginIndex = fuelString.indexOf(' ');
        int endIndex = fuelString.indexOf('%');
        String valueString = fuelString.substring(beginIndex+1, endIndex);
        int valueAsInt = Integer.valueOf (valueString);
        Double selectedFuel = Double.valueOf (valueAsInt).doubleValue() / 100.0;
        
        BriefingFlight activeBriefingFlight = briefingContext.getActiveBriefingFlight();
        activeBriefingFlight.setSelectedFuel(selectedFuel);
    }

    public WaypointEditorSet getWaypointEditors()
    {
        return waypointEditors;
    }
}
