package pwcg.gui.rofmap.debrief;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;

import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javax.swing.JScrollPane;

import pwcg.aar.AARCoordinator;
import pwcg.aar.inmission.phase3.reconcile.victories.singleplayer.PlayerDeclarations;
import pwcg.campaign.Campaign;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.PWCGLogger;
import pwcg.gui.CampaignGuiContextManager;
import pwcg.gui.ScreenIdentifier;
import pwcg.gui.UiImageResolver;
import pwcg.gui.campaign.home.CampaignHomeScreen;
import pwcg.gui.dialogs.ErrorDialog;
import pwcg.gui.sound.SoundManager;
import pwcg.gui.utils.ImageResizingPanel;
import pwcg.gui.utils.ButtonFactory;
import pwcg.gui.utils.ScrollBarWrapper;

public class DebriefMissionDescriptionScreen extends ImageResizingPanel implements ActionListener
{
    private AARCoordinator aarCoordinator;
    private CampaignHomeScreen homeGui;
    private Campaign campaign;

	private static final long serialVersionUID = 1L;

	public DebriefMissionDescriptionScreen(Campaign campaign, CampaignHomeScreen homeGui) 
	{
        super("");
        this.setLayout(new BorderLayout());
        this.setOpaque(false);
	    
        this.homeGui = homeGui;        
        this.campaign = campaign;        
        this.aarCoordinator = AARCoordinator.getInstance();
	}

	public void makePanels() 
	{
		try
		{
	        String imagePath = UiImageResolver.getImage(ScreenIdentifier.DebriefMissionDescriptionScreen);
	        this.setImageFromName(imagePath);

			this.removeAll();	
			this.add(BorderLayout.WEST, makeButtonPanel());
			this.add(BorderLayout.CENTER, makeMissionDescriptionPanel());
		}
		catch (Exception e)
		{
			PWCGLogger.logException(e);
			ErrorDialog.internalError(e.getMessage());
		}
	}

    private Pane makeButtonPanel() throws PWCGException 
    {
        Pane navPanel = new Pane(new BorderLayout());
        navPanel.setOpaque(false);

        Pane buttonGrid = new Pane();
        buttonGrid.setLayout(new GridLayout(0,1));
        buttonGrid.setOpaque(false);
        
        Button submitWithClaimsButton = ButtonFactory.makeTranslucentMenuButton("Continue With Claims", "submitWithClaims", "Continue with AAR process - make claims", this);
        buttonGrid.add(submitWithClaimsButton);
        
        if (campaign.getCampaignData().isCoop())
        {
            Button submitWithoutClaimsButton = ButtonFactory.makeTranslucentMenuButton("Continue Without Claims", "submitWithoutClaims", "Continue with AAR process - make no claims", this);
            buttonGrid.add(submitWithoutClaimsButton);
        }
        
        buttonGrid.add(ButtonFactory.makeDummy());

        Button cancelButton = ButtonFactory.makeTranslucentMenuButton("Cancel AAR", "Cancel", "Cancel the AAR process", this);
        buttonGrid.add(cancelButton);

        navPanel.add(buttonGrid, BorderLayout.NORTH);
        
        return navPanel;
    }

    public Pane makeMissionDescriptionPanel() throws PWCGException  
    {
        Pane debriefPanel = new Pane(new BorderLayout());
        debriefPanel.setOpaque(false);

        Pane missionTextPanel = makeMissionTextArea();
        
        JScrollPane missionScrollPane = ScrollBarWrapper.makeScrollPane(missionTextPanel);

        debriefPanel.add(missionScrollPane, BorderLayout.CENTER);

        return debriefPanel;
    }

    private Pane makeMissionTextArea() throws PWCGException
    {
        DebriefDescriptionChalkboard debriefDescriptionChalkboard = new DebriefDescriptionChalkboard(campaign, aarCoordinator);
        debriefDescriptionChalkboard.makePanel();
        return debriefDescriptionChalkboard;
    }

    @Override
    public void actionPerformed(ActionEvent arg0) 
    {       
        try
        {
            String action = arg0.getActionCommand();
            
            if (action.equals("Cancel"))
            {
                backToCampaign();
            }
            else if (action.equals("submitWithClaims"))
            {
                showClaims();
            }
            else
            {
                submitReportUsingOnlyLogs();
            }
        }
        catch (Exception e)
        {
            PWCGLogger.logException(e);
            ErrorDialog.internalError(e.getMessage());
        }
    }

    private void showClaims() throws PWCGException 
    {
        SoundManager.getInstance().playSound("Typewriter.WAV");
        
        AARInitiationScreen combatReportDisplay = new AARInitiationScreen(homeGui);
        combatReportDisplay.makePanel();
        CampaignGuiContextManager.getInstance().pushToContextStack(combatReportDisplay);
    }
    
    
    private void submitReportUsingOnlyLogs() throws PWCGException
    {
        SoundManager.getInstance().playSound("Stapling.WAV");   
        AARSubmitter submitter = new AARSubmitter();
        Map<Integer, PlayerDeclarations> playerDeclarations = new HashMap<>();
        String aarError = submitter.submitAAR(playerDeclarations);
        if (aarError != null && !aarError.isEmpty())
        {
            ErrorDialog.internalError("Error during AAR process - please post " + aarError);
        }
        else
        {
            showDebrief();
        }
    }   


    private void showDebrief() throws PWCGException 
    {
        DebriefMapGUI debriefMap = new DebriefMapGUI(campaign, homeGui);
        debriefMap.makePanels();
        CampaignGuiContextManager.getInstance().pushToContextStack(debriefMap);
    
        campaign.setCurrentMission(null);
    }

    private void backToCampaign() throws PWCGException
    {
        homeGui.refreshInformation();
        CampaignGuiContextManager.getInstance().popFromContextStack();
    }
}
