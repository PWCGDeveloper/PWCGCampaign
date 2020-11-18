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
import pwcg.gui.campaign.home.CampaignHomeScreen;
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

	private CampaignHomeScreen campaignHomeGui;
    private Mission mission;
    private BriefingData briefingData;
    private BriefingEditorPanel editorPanel;

	public BriefingEditorScreen(CampaignHomeScreen campaignHomeGui) throws PWCGException  
	{
		super("");
		this.setLayout(new BorderLayout());
		this.setOpaque(false);
		
		this.campaignHomeGui = campaignHomeGui;
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
        JButton goBackToBriefingMapButton = makeButton("Back To Map", "Back To Map", "Go back to briefing map screen");
        buttonGrid.add(goBackToBriefingMapButton);

        buttonGrid.add(PWCGButtonFactory.makeDummy());
        JButton goToPilotSelectionButton = makeButton("Pilot Selection", "Pilot Selection", "Progress to pilot selection screen");
        buttonGrid.add(goToPilotSelectionButton);

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
            if (action.equalsIgnoreCase("Back To Map"))
            {
                backToBriefingMap();
            }
            else if (action.equals("Pilot Selection"))
            {
                forwardToPilotSelection();
            }
            else if (action.equals("Back to Campaign"))
            {
                backToCampaign();
            }
            else if (action.equals("Scrub Mission"))
            {
                scrubMission();
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
        BriefingPilotSelectionScreen pilotSelection = new BriefingPilotSelectionScreen(campaignHomeGui);
        pilotSelection.makePanels();
        CampaignGuiContextManager.getInstance().pushToContextStack(pilotSelection);
    }

    private void scrubMission() throws PWCGException
    {
        mission.getCampaign().setCurrentMission(null);
        campaignHomeGui.createCampaignHomeContext();
        CampaignGuiContextManager.getInstance().backToCampaignHome();
    }
    
    private void backToCampaign() throws PWCGException
    {
        campaignHomeGui.createCampaignHomeContext();
        CampaignGuiContextManager.getInstance().popFromContextStack();
    }

    private void pushEditsToModel()
    {
        for (BriefingMapPoint briefingMapPoint : briefingData.getActiveBriefingFlight().getBriefingFlightParameters().getBriefingMapMapPoints())
        {
            WaypointEditor editor = editorPanel.getWaypointEditors().getWaypointEditorByid(briefingMapPoint.getWaypointID());
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
