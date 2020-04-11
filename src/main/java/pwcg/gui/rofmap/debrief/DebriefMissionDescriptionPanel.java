package pwcg.gui.rofmap.debrief;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import pwcg.aar.AARCoordinator;
import pwcg.aar.inmission.phase3.reconcile.victories.singleplayer.PlayerDeclarations;
import pwcg.campaign.Campaign;
import pwcg.campaign.squadmember.SquadronMember;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.PWCGLogger;
import pwcg.gui.CampaignGuiContextManager;
import pwcg.gui.campaign.home.CampaignHomeGUI;
import pwcg.gui.colors.ColorMap;
import pwcg.gui.dialogs.ErrorDialog;
import pwcg.gui.dialogs.MonitorSupport;
import pwcg.gui.sound.SoundManager;
import pwcg.gui.utils.ContextSpecificImages;
import pwcg.gui.utils.ImageResizingPanel;
import pwcg.gui.utils.PWCGButtonFactory;
import pwcg.gui.utils.ScrollBarWrapper;

public class DebriefMissionDescriptionPanel extends AARPanel implements ActionListener
{
    private AARCoordinator aarCoordinator;
    private CampaignHomeGUI homeGui;
    private Campaign campaign;

	private static final long serialVersionUID = 1L;
    private JTextArea missionTextArea = new JTextArea();

	public DebriefMissionDescriptionPanel(Campaign campaign, CampaignHomeGUI homeGui) 
	{
	    super();
	    
        this.homeGui = homeGui;        
        this.campaign = campaign;        
        this.aarCoordinator = AARCoordinator.getInstance();
	}

	public void makePanels() 
	{
		try
		{
			this.removeAll();
	
			setLeftPanel(makeButtonPanel());

			setCenterPanel(makeMissionDescriptionPanel());
		}
		catch (Exception e)
		{
			PWCGLogger.logException(e);
			ErrorDialog.internalError(e.getMessage());
		}
	}

    private JPanel makeButtonPanel() throws PWCGException 
    {
        String imagePath = getSideImage(campaign, "CombatReportNav.jpg");

        ImageResizingPanel buttonPanel = new ImageResizingPanel(imagePath);
        buttonPanel.setLayout(new BorderLayout());
        buttonPanel.setOpaque(false);

        JPanel buttonGrid = new JPanel();
        buttonGrid.setLayout(new GridLayout(0,1));
        buttonGrid.setOpaque(false);
            
        JButton submitButton = PWCGButtonFactory.makeMenuButton("Continue", "Continue", this);
        buttonGrid.add(submitButton);

        buttonGrid.add(PWCGButtonFactory.makeDummy());

        JButton cancelButton = PWCGButtonFactory.makeMenuButton("Cancel AAR", "Cancel", this);
        buttonGrid.add(cancelButton);

        buttonPanel.add(buttonGrid, BorderLayout.NORTH);
        
        return buttonPanel;
    }

    public JPanel makeMissionDescriptionPanel() throws PWCGException  
    {
        String imagePath = ContextSpecificImages.imagesMisc() + "PilotSelectChalkboard.jpg";
        JPanel debriefPanel = new ImageResizingPanel(imagePath);
        debriefPanel.setLayout(new BorderLayout());
        debriefPanel.setOpaque(false);

        JPanel missionTextPanel = makeMissionTextArea();
        
        JScrollPane missionScrollPane = ScrollBarWrapper.makeScrollPane(missionTextPanel);

        debriefPanel.add(missionScrollPane, BorderLayout.CENTER);

        return debriefPanel;
    }

    private JPanel makeMissionTextArea() throws PWCGException 
    {
        JPanel missionTextPanel = new JPanel(new BorderLayout());
        missionTextPanel.setOpaque(false);
        
        Font font = MonitorSupport.getBriefingChalkboardFont();

        missionTextArea.setFont(font);
        missionTextArea.setOpaque(false);
        missionTextArea.setLineWrap(true);
        missionTextArea.setWrapStyleWord(true);
        missionTextArea.setForeground(ColorMap.CHALK_FOREGROUND);
        
        Insets margins = MonitorSupport.calculateInset(50, 35, 65, 35);
        missionTextArea.setMargin(margins);
        
        String missionText = makeMissionText();
        missionTextArea.setText(missionText);
        
        missionTextPanel.add(missionTextArea, BorderLayout.CENTER);

        return missionTextPanel;
    }

    private String makeMissionText() throws PWCGException
    {
        String missionText = "Assigned Personnel:\n";
        
        for (SquadronMember pilotInMission : aarCoordinator.getAarContext().getPreliminaryData().getCampaignMembersInMission().getSquadronMemberCollection().values())
        {            
            SquadronMember referencePlayer = campaign.findReferencePlayer();
            if (pilotInMission.getSquadronId() == referencePlayer.getSquadronId())
            {
                missionText += "             " + pilotInMission.getNameAndRank();
                missionText += "\n";
            }
        }

        missionText += "\n";
        missionText += aarCoordinator.getAarContext().getPreliminaryData().getPwcgMissionData().getMissionDescription();
        
        return missionText;
        
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
            else if (action.equals("Continue"))
            {
                if (aarCoordinator.getAarContext().getPreliminaryData().getPwcgMissionData().getMissionHeader().isSingleHumanSquadron())
                {
                    showClaims();
                }
                else
                {
                    submitReportDirectlyForCoop();
                }
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
        
        AARPanelSet combatReportDisplay = new AARPanelSet(homeGui);
        combatReportDisplay.makePanel();
        CampaignGuiContextManager.getInstance().pushToContextStack(combatReportDisplay);
    }
    
    
    private void submitReportDirectlyForCoop() throws PWCGException
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
    
        // Reset the mission after a combat report has been submitted
        campaign.setCurrentMission(null);
    }

    private void backToCampaign() throws PWCGException
    {
        homeGui.clean();
        homeGui.createPilotContext();
        homeGui.enableButtonsAsNeeded();
        CampaignGuiContextManager.getInstance().popFromContextStack();
    }
}
