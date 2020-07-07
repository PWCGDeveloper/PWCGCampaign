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

public class BriefingEditorEditorScreen extends ImageResizingPanel implements ActionListener
{
	private static final long serialVersionUID = 1L;

	private CampaignHomeScreen campaignHomeGui;
    private Mission mission;
    private BriefingData briefingData;
    private BriefingMapEditorPanel editorPanel;

	public BriefingEditorEditorScreen(CampaignHomeScreen campaignHomeGui) throws PWCGException  
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
        this.setImage(imagePath);

		editorPanel = new BriefingMapEditorPanel();
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
            makeButton(buttonGrid, "Back to Campaign");
        }

        buttonGrid.add(PWCGButtonFactory.makeDummy());
        makeButton(buttonGrid, "Scrub Mission");

        buttonGrid.add(PWCGButtonFactory.makeDummy());
        makeButton(buttonGrid, "Back To Map");

        buttonGrid.add(PWCGButtonFactory.makeDummy());
        makeButton(buttonGrid, "Pilot Selection");

        buttonGrid.add(PWCGButtonFactory.makeDummy());
        buttonGrid.add(PWCGButtonFactory.makeDummy());
        buttonGrid.add(PWCGButtonFactory.makeDummy());

        buttonPanel.add(buttonGrid, BorderLayout.NORTH);
        
        return buttonPanel;
    }

    private JButton makeButton(JPanel buttonPanel, String buttonText) throws PWCGException
    {
        JButton button = PWCGButtonFactory.makeMenuButton(buttonText, buttonText, this);
        buttonPanel.add(button);
        return button;
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
            }
        }
    }

}
