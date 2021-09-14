package pwcg.gui.rofmap.brief;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;

import pwcg.core.exception.PWCGException;
import pwcg.core.utils.PWCGLogger;
import pwcg.gui.CampaignGuiContextManager;
import pwcg.gui.ScreenIdentifier;
import pwcg.gui.UiImageResolver;
import pwcg.gui.campaign.mission.MissionGeneratorHelper;
import pwcg.gui.dialogs.ErrorDialog;
import pwcg.gui.rofmap.brief.model.BriefingData;
import pwcg.gui.rofmap.brief.model.BriefingMapPoint;
import pwcg.gui.utils.ImageResizingPanel;
import pwcg.gui.utils.PWCGButtonFactory;
import pwcg.gui.utils.SpacerPanelFactory;
import pwcg.mission.Mission;

public class BriefingEditorScreen extends ImageResizingPanel implements ActionListener
{
	private static final long serialVersionUID = 1L;

    private CampaignHomeGuiBriefingWrapper campaignHomeGuiBriefingWrapper;
    private Mission mission;
    private BriefingData briefingData;
    private BriefingEditorPanel editorPanel;

	public BriefingEditorScreen(CampaignHomeGuiBriefingWrapper campaignHomeGuiBriefingWrapper) throws PWCGException  
	{
		super("");
		this.setLayout(new BorderLayout());
		this.setOpaque(false);
		
		this.campaignHomeGuiBriefingWrapper = campaignHomeGuiBriefingWrapper;
        this.briefingData =  BriefingContext.getInstance().getBriefingData();
        this.mission =  briefingData.getMission();

		setLayout(new BorderLayout());		
	}    

	public void makePanels() throws PWCGException 
	{
        String imagePath = UiImageResolver.getImage(ScreenIdentifier.BriefingEditorEditorScreen);
        this.setImageFromName(imagePath);

		editorPanel = new BriefingEditorPanel();
		editorPanel.makePanels();
		
        this.add(BorderLayout.WEST, makeNavPanel());
        this.add(BorderLayout.CENTER, editorPanel);
        this.add(BorderLayout.EAST, SpacerPanelFactory.makeDocumentSpacerPanel(1400));
	}
	

    private JPanel makeNavPanel() throws PWCGException 
    {
        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.setOpaque(false);

        JPanel buttonPanel = makeButtonPanel();
        leftPanel.add(buttonPanel, BorderLayout.NORTH);
        return leftPanel;
    }
    
    private JPanel makeButtonPanel() throws PWCGException 
    {
        JPanel buttonPanel = new JPanel(new BorderLayout());
        buttonPanel.setOpaque(false);

        JPanel buttonGrid = new JPanel();
        buttonGrid.setLayout(new GridLayout(0,1));
        buttonGrid.setOpaque(false);
        
        if (mission.isFinalized())
        {
            buttonGrid.add(PWCGButtonFactory.makeDummy());
            JButton backToCampaignButton = makeButton("Back to Campaign", "Back to Campaign", "Return to campaign home screen");
            buttonGrid.add(backToCampaignButton);
        }

        buttonGrid.add(PWCGButtonFactory.makeDummy());
        JButton scrubMissionButton = makeButton("Scrub Mission", "Scrub Mission", "Scrub this mission and return to campaign home screen");
        buttonGrid.add(scrubMissionButton);

        buttonGrid.add(PWCGButtonFactory.makeDummy());
        JButton goBackToBriefingMapButton = makeButton("Back: Map", "Back: Map", "Go back to briefing map screen");
        buttonGrid.add(goBackToBriefingMapButton);

        buttonGrid.add(PWCGButtonFactory.makeDummy());
        JButton goToPilotSelectionButton = makeButton("Next: Pilots", "Next: Pilots", "Progress to pilot selection screen");
        buttonGrid.add(goToPilotSelectionButton);

        buttonGrid.add(PWCGButtonFactory.makeDummy());
        JButton makeWaypointsEditableButton = makeButton("Edit Waypoint Details", "Edit Waypoints", "Make waypoint data editable");
        buttonGrid.add(makeWaypointsEditableButton);

        buttonGrid.add(PWCGButtonFactory.makeDummy());
        buttonGrid.add(PWCGButtonFactory.makeDummy());

        buttonPanel.add(buttonGrid, BorderLayout.NORTH);
        
        return buttonPanel;
    }

    private JButton makeButton(String buttonText, String command, String toolTipText) throws PWCGException
    {
        return PWCGButtonFactory.makeTranslucentMenuButton(buttonText, command, toolTipText, this);
    }

    @Override
    public void actionPerformed(ActionEvent arg0) 
    {       
        try
        {
            String action = arg0.getActionCommand();
            if (action.equalsIgnoreCase("Back: Map"))
            {
                backToBriefingMap();
            }
            else if (action.equals("Next: Pilots"))
            {
                forwardToPilotSelection();
            }
            else if (action.equals("Back to Campaign"))
            {
                backToCampaign();
            }
            else if (action.equals("Scrub Mission"))
            {
                MissionGeneratorHelper.scrubMission(mission.getCampaign(), campaignHomeGuiBriefingWrapper);
            }
            else if (action.equals("Edit Waypoints"))
            {
                makeWaypointsEditable();
            }
            
        }
        catch (Exception e)
        {
            PWCGLogger.logException(e);
            ErrorDialog.internalError(e.getMessage());
        }
    }

    private void backToBriefingMap() throws PWCGException
    {
        pushEditsToModel();
        CampaignGuiContextManager.getInstance().popFromContextStack();
    }

    private void forwardToPilotSelection() throws PWCGException
    {
        pushEditsToModel();
        BriefingPilotSelectionScreen pilotSelection = new BriefingPilotSelectionScreen(campaignHomeGuiBriefingWrapper);
        pilotSelection.makePanels();
        CampaignGuiContextManager.getInstance().pushToContextStack(pilotSelection);
    }

    private void makeWaypointsEditable() throws PWCGException
    {
        editorPanel.makeEditable();        
    }

    private void backToCampaign() throws PWCGException
    {
        campaignHomeGuiBriefingWrapper.refreshCampaignPage();
        CampaignGuiContextManager.getInstance().popFromContextStack();
    }

    private void pushEditsToModel()
    {
        for (BriefingMapPoint briefingMapPoint : briefingData.getActiveBriefingFlight().getBriefingFlightParameters().getBriefingMapMapPoints())
        {
            IWaypointDetails editor = editorPanel.getWaypointEditors().getWaypointEditorByid(briefingMapPoint.getWaypointID());
            if (editor != null)
            {
                int altitude = editor.getAltitudeValue();
                briefingMapPoint.setAltitude(altitude);
                
                int cruisingSpeed = editor.getCruisingSpeedValue();
                briefingMapPoint.setCruisingSpeed(cruisingSpeed);
            }
        }
    }

}
