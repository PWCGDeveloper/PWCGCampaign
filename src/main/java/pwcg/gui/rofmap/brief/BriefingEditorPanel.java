package pwcg.gui.rofmap.brief;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

import pwcg.core.exception.PWCGException;
import pwcg.core.utils.PWCGLogger;
import pwcg.gui.dialogs.ErrorDialog;
import pwcg.gui.rofmap.brief.model.BriefingData;
import pwcg.gui.rofmap.brief.model.BriefingUnit;
import pwcg.gui.utils.ContextSpecificImages;
import pwcg.gui.utils.ImageResizingPanel;
import pwcg.gui.utils.PWCGLabelFactory;
import pwcg.mission.Mission;
import pwcg.mission.utils.MissionTime;

public class BriefingEditorPanel extends ImageResizingPanel implements ActionListener
{
	private static final long serialVersionUID = 1L;

    private JComboBox<String> cbFuel;
    private JComboBox<String> cbMissionTime;
    private BriefingEditorDetailsPanel waypointDetailsPanel;
    private JPanel editorPanel;
    private JPanel editDetailsPanel;
    private Mission mission;
    private BriefingData briefingData;

	public BriefingEditorPanel() throws PWCGException  
	{
		super("");
		this.setLayout(new BorderLayout());
		this.setOpaque(false);
		
        this.briefingData =  BriefingContext.getInstance().getBriefingData();
        this.mission =  briefingData.getMission();

		setLayout(new BorderLayout());		
	}    

	public void makePanels() throws PWCGException 
	{
        String imagePath = ContextSpecificImages.imagesMisc() + "Document.png";
        this.setImageFromName(imagePath);

		editorPanel = new JPanel();
		editorPanel.setLayout(new BorderLayout());
		editorPanel.setOpaque(false);
		editorPanel.setBorder(BorderFactory.createEmptyBorder(50,50,50,100));

        BriefingUnit activeBriefingFlight = briefingData.getActiveBriefingUnit();
        waypointDetailsPanel = new BriefingEditorDetailsPanel(false);
        waypointDetailsPanel.buildWaypointPanel(activeBriefingFlight);
        
        JPanel editableLabelPanel = createEditableLabelPanel();
        editorPanel.add(editableLabelPanel, BorderLayout.NORTH);

        editDetailsPanel = new JPanel(new BorderLayout());
        editDetailsPanel.setOpaque(false);
        makeDetailsPanel();

        editorPanel.add(editDetailsPanel, BorderLayout.CENTER);
        this.add(editorPanel, BorderLayout.CENTER);
	}
	
	public void makeEditable() throws PWCGException
	{
	    JComponent previousPanel = waypointDetailsPanel.getWaypointPanel();
	    
        BriefingUnit activeBriefingFlight = briefingData.getActiveBriefingUnit();
        waypointDetailsPanel = new BriefingEditorDetailsPanel(true);
        waypointDetailsPanel.buildWaypointPanel(activeBriefingFlight);
        
        setWaypointViewPanel(previousPanel);        
	}

    private JPanel createEditableLabelPanel() throws PWCGException
    {
        JPanel editableLabelPanel = new JPanel(new GridLayout(0,1));
        editableLabelPanel.setOpaque(false);
        
        JLabel summaryLabel = PWCGLabelFactory.makePaperLabelLarge("Mission Summary");
        editableLabelPanel.add(summaryLabel);
        
        editableLabelPanel.add(PWCGLabelFactory.makeDummyLabel());
        
        return editableLabelPanel;
    }

    private void makeDetailsPanel() throws PWCGException
    {
        editDetailsPanel = new JPanel(new BorderLayout());
        editDetailsPanel.setOpaque(false);

        JPanel dropDownPanel = createDropDownPanel();
        editDetailsPanel.add(dropDownPanel, BorderLayout.NORTH);

        setWaypointViewPanel(null);        
    }
    
    private void setWaypointViewPanel(JComponent previousPanel)
    {
        if (previousPanel != null)
        {
            editDetailsPanel.remove(previousPanel);
        }
        
        editDetailsPanel.add(waypointDetailsPanel.getWaypointPanel(), BorderLayout.CENTER);
        editDetailsPanel.setVisible(false);
        editDetailsPanel.setVisible(true);
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

    private void createTimeDisplay()
    {
        MissionTime missionTime = mission.getMissionOptions().getMissionTime();
		
        cbMissionTime = new JComboBox<String>();
        cbMissionTime.setActionCommand("ChangeTime");
        cbMissionTime.setOpaque(false);

        for (String time : missionTime.getMissionTimes())
        {
            cbMissionTime.addItem(time);
        }
        cbMissionTime.setOpaque(false);
        cbMissionTime.setSelectedIndex(missionTime.getIndexForTime());
       
        cbMissionTime.setSelectedItem(briefingData.getMissionTime());
        
        cbMissionTime.addActionListener(this);
        if (mission.getFinalizer().isFinalized())
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
		if (mission.getFinalizer().isFinalized())
		{
			cbFuel.setEnabled(false);
		}
    }

	private int getIndexForFuel()
	{
	    int index = 0;
	    
        BriefingUnit activeBriefingFlight = briefingData.getActiveBriefingUnit();
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
                briefingData.setMissionTime(selectedTime);
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
        
        BriefingUnit activeBriefingFlight = briefingData.getActiveBriefingUnit();
        activeBriefingFlight.setSelectedFuel(selectedFuel);
    }

    public WaypointEditorSet getWaypointEditors()
    {
        return waypointDetailsPanel.getWaypointEditors();
    }
}
