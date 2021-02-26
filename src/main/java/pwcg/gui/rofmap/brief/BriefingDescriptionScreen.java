package pwcg.gui.rofmap.brief;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import pwcg.campaign.Campaign;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.squadron.Squadron;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.PWCGLogger;
import pwcg.gui.CampaignGuiContextManager;
import pwcg.gui.ScreenIdentifier;
import pwcg.gui.UiImageResolver;
import pwcg.gui.campaign.home.CampaignHomeScreen;
import pwcg.gui.dialogs.ErrorDialog;
import pwcg.gui.rofmap.brief.model.BriefingData;
import pwcg.gui.rofmap.brief.update.BriefingMissionUpdater;
import pwcg.gui.sound.SoundManager;
import pwcg.gui.utils.ImageResizingPanel;
import pwcg.gui.utils.PWCGButtonFactory;
import pwcg.gui.utils.ScrollBarWrapper;
import pwcg.mission.Mission;

public class BriefingDescriptionScreen extends ImageResizingPanel implements ActionListener, IFlightChanged
{
    private CampaignHomeScreen campaignHomeGui = null;

	private static final long serialVersionUID = 1L;
    private Mission mission;
    private BriefingData briefingData;
    private BriefingFlightChooser briefingFlightChooser;
    private BriefingDescriptionChalkboard briefingChalkboard;
    
	public BriefingDescriptionScreen(CampaignHomeScreen campaignHomeGui, Mission mission) throws PWCGException 
	{
        super("");
        this.setLayout(new BorderLayout());
	    
        this.campaignHomeGui =  campaignHomeGui;
        this.mission =  mission;

        BriefingContext briefingContext = BriefingContext.getInstance();
        briefingContext.buildBriefingData(mission);
        this.briefingData = briefingContext.getBriefingData();

		SoundManager.getInstance().playSound("BriefingStart.WAV");
	}

	public void makePanels() 
	{
		try
		{
	        String imagePath = UiImageResolver.getImage(ScreenIdentifier.BriefingDescriptionScreen);
            this.setImageFromName(imagePath);

            briefingFlightChooser = new BriefingFlightChooser(mission, this);
            briefingFlightChooser.createBriefingSquadronSelectPanel();

			this.removeAll();
			this.add(BorderLayout.WEST, makeLeftPanel());
			this.add(BorderLayout.CENTER, makeBriefingPanel());
		}
		catch (Exception e)
		{
			PWCGLogger.logException(e);
			ErrorDialog.internalError(e.getMessage());
		}
	}

    private JPanel makeLeftPanel() throws PWCGException 
    {
        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.setOpaque(false);

        JPanel buttonPanel = makeButtonPanel();
        leftPanel.add(buttonPanel, BorderLayout.NORTH);
        leftPanel.add(briefingFlightChooser.getFlightChooserPanel(), BorderLayout.CENTER);
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
        JButton goToBriefingMapButton = makeButton("Next: Map", "Next: Map", "Progress to briefing map screen");
        buttonGrid.add(goToBriefingMapButton);

        buttonPanel.add(buttonGrid, BorderLayout.NORTH);
        
        return buttonPanel;
    }
    
    public JPanel makeBriefingPanel() throws PWCGException  
    {
        JPanel briefingPanel = new JPanel(new BorderLayout());
        briefingPanel.setOpaque(false);

        briefingChalkboard = new BriefingDescriptionChalkboard(mission, briefingData);
        briefingChalkboard.makePanel();
        JScrollPane missionScrollPane = ScrollBarWrapper.makeScrollPane(briefingChalkboard);

        briefingPanel.add(missionScrollPane, BorderLayout.CENTER);

        return briefingPanel;
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
            
            if (action.equals("Back to Campaign"))
            {
                backToCampaign();
            }
            else if (action.equals("Next: Map"))
            {
                forwardToBriefingMap();
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

    @Override
    public void flightChanged(Squadron squadron) throws PWCGException
    {
        briefingData.changeSelectedFlight(squadron.getSquadronId());
        briefingChalkboard.setMissionText();
    }

    private void forwardToBriefingMap() throws PWCGException 
    {
        SoundManager.getInstance().playSound("Typewriter.WAV");

        BriefingMapGUI briefingMap = new BriefingMapGUI(campaignHomeGui.getCampaign(), campaignHomeGui);
        briefingMap.makePanels();
        CampaignGuiContextManager.getInstance().pushToContextStack(briefingMap);
    }

    private void scrubMission() throws PWCGException
    {
        Campaign campaign  = PWCGContext.getInstance().getCampaign();
        campaign.setCurrentMission(null);
        campaignHomeGui.refreshInformation();
        CampaignGuiContextManager.getInstance().backToCampaignHome();
    }

    private void backToCampaign() throws PWCGException
    {
        Campaign campaign  = PWCGContext.getInstance().getCampaign();

        BriefingMissionUpdater.pushEditsToMission(briefingData);
        
        campaign.setCurrentMission(mission);
        
        campaignHomeGui.refreshInformation();
        CampaignGuiContextManager.getInstance().popFromContextStack();
    }
    
    public void refreshScreen() throws PWCGException
    {
        briefingChalkboard.setMissionText();
        briefingFlightChooser.setSelectedButton(briefingData.getSelectedFlight().getSquadron().getSquadronId());
    }
}
