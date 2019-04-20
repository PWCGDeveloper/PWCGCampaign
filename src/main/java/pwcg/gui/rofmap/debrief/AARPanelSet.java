package pwcg.gui.rofmap.debrief;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import pwcg.aar.AARCoordinator;
import pwcg.aar.inmission.phase3.reconcile.victories.singleplayer.PlayerDeclarations;
import pwcg.campaign.Campaign;
import pwcg.campaign.context.PWCGContextManager;
import pwcg.campaign.squadmember.SquadronMember;
import pwcg.campaign.squadmember.SquadronMembers;
import pwcg.core.exception.PWCGException;
import pwcg.core.exception.PWCGUserException;
import pwcg.core.utils.DateUtils;
import pwcg.core.utils.Logger;
import pwcg.gui.CampaignGuiContextManager;
import pwcg.gui.campaign.home.CampaignHomeGUI;
import pwcg.gui.colors.ColorMap;
import pwcg.gui.dialogs.ErrorDialog;
import pwcg.gui.dialogs.MonitorSupport;
import pwcg.gui.sound.SoundManager;
import pwcg.gui.utils.ContextSpecificImages;
import pwcg.gui.utils.ImageResizingPanel;
import pwcg.gui.utils.PWCGButtonFactory;

public class AARPanelSet extends AARPanel implements ActionListener
{
    private static final long serialVersionUID = 1L;

    private Campaign campaign = null;
	private CampaignHomeGUI home = null;
    private SquadronMember referencePlayer = null;
    private AARClaimPanels aarClaimPanel = null;

	public AARPanelSet(CampaignHomeGUI home)  
	{
        super();

		this.campaign = PWCGContextManager.getInstance().getCampaign();
		this.referencePlayer = PWCGContextManager.getInstance().getReferencePlayer();
        this.home = home;
	}

    public void makePanel() throws PWCGException
    {        
        setCenterPanel(makeCenterPanel());
        setLeftPanel(makeNavigationPanel());
	}
    
	private JPanel makeCenterPanel() throws PWCGException  
	{
		ImageResizingPanel aarMainPanel = new ImageResizingPanel(ContextSpecificImages.imagesMisc() + "paperRotated.jpg");
		aarMainPanel.setLayout(new BorderLayout());

		JPanel infoPanel = makeInfoPanel();
		aarMainPanel.add(infoPanel, BorderLayout.NORTH);
		
		aarClaimPanel = makeAARPanel();
		aarMainPanel.add(aarClaimPanel, BorderLayout.CENTER);
		
		return aarMainPanel;
	}

    private AARClaimPanels makeAARPanel() throws PWCGException  
    {
        AARClaimPanels aarClaimPanel = new AARClaimPanels();
        aarClaimPanel.makePanels();
        return aarClaimPanel;
    }

	private JPanel makeInfoPanel() throws PWCGException 
	{
        JPanel infoPanel = new JPanel (new BorderLayout());
        infoPanel.setOpaque(false);

		Color buttonBG = ColorMap.PAPER_BACKGROUND;
		
		Font font = MonitorSupport.getTypewriterFont();

		JPanel infoPanelGrid = new JPanel (new GridLayout(0,1));
		infoPanelGrid.setOpaque(false);

		for (int i = 0; i < 1; ++i)
		{
		    infoPanelGrid.add(PWCGButtonFactory.makeDummy());
		}

		JLabel lPilots = new JLabel("     Pilots assigned to this mission:", JLabel.LEFT);
		lPilots.setBackground(buttonBG);
		lPilots.setOpaque(false);
		lPilots.setFont(font);
		infoPanelGrid.add(lPilots);
		
        SquadronMembers pilotsInMission = AARCoordinator.getInstance().getAarContext().getPreliminaryData().getCampaignMembersInMission();
        List<SquadronMember> pilotsInMissionSorted = pilotsInMission.sortPilots(campaign.getDate());
		for (SquadronMember pilot : pilotsInMissionSorted)
		{
            if (pilot.getSquadronId() == referencePlayer.getSquadronId())
            {
                String crewDesc = "             " + pilot.getNameAndRank();
               
    			JLabel lPilot = new JLabel(crewDesc, JLabel.LEFT);
    			lPilot.setSize(200, 40);
    			lPilot.setBackground(buttonBG);
    			lPilot.setOpaque(false);
    			lPilot.setFont(font);
    			infoPanelGrid.add(lPilot);		
            }
		}
		
        JLabel space1 = new JLabel("", JLabel.LEFT);
        JLabel space2 = new JLabel("", JLabel.LEFT);
        infoPanelGrid.add(space1);
        infoPanelGrid.add(space2);

        JLabel lDate = new JLabel("     Date: " + DateUtils.getDateString(campaign.getDate()), JLabel.LEFT);
        lDate.setBackground(buttonBG);
        lDate.setFont(font);
        infoPanelGrid.add(lDate);

		for (int i = 0; i < 1; ++i)
		{
			infoPanelGrid.add(PWCGButtonFactory.makeDummy());
		}
		
		infoPanel.add(infoPanelGrid, BorderLayout.NORTH);

		return infoPanel;
	}

	private JPanel makeNavigationPanel() throws PWCGException  
	{
        String imagePath = getSideImage("CombatReportNav.jpg");

		ImageResizingPanel aarButtonPanel = new ImageResizingPanel(imagePath);
		aarButtonPanel.setLayout(new BorderLayout());
		aarButtonPanel.setOpaque(false);
		
		JPanel buttonGrid = new JPanel(new GridLayout(0,1));
		buttonGrid.setOpaque(false);
        
        makeButton ("Submit Report", "Submit Report", buttonGrid);
        makeButton("Cancel AAR",  "Cancel", buttonGrid);

		aarButtonPanel.add(buttonGrid, BorderLayout.NORTH);
		
		return aarButtonPanel;
	}

    private void makeButton(String buttonText, String command, JPanel buttonGrid) throws PWCGException 
    {
        buttonGrid.add(PWCGButtonFactory.makeDummy());  

        JButton button = PWCGButtonFactory.makeMenuButton(buttonText, command, this);
        buttonGrid.add(button);  
    }

    @Override
    public void actionPerformed(ActionEvent ae)
    {
        try
        {
            String action = ae.getActionCommand();
            
            if (action.equalsIgnoreCase("Submit Report"))
            {
                submitReport();
            }
            else if (action.equals("Cancel"))
            {
                home.clean();
                home.createPilotContext();

                home.enableButtonsAsNeeded();
                CampaignGuiContextManager.getInstance().popFromContextStack();
            }
        }
        catch (PWCGUserException ue)
        {
            campaign.setCurrentMission(null);
            Logger.logException(ue);
            ErrorDialog.userError(ue.getMessage());
        }
        catch (Exception e)
        {
            campaign.setCurrentMission(null);
            Logger.logException(e);
            ErrorDialog.internalError(e.getMessage());
        }
    }
	
    private void submitReport() throws PWCGException
    {
        SoundManager.getInstance().playSound("Stapling.WAV");
   
        Map<Integer, PlayerDeclarations> playerDeclarations = aarClaimPanel.getPlayerDeclarations();
        
        AARCoordinator.getInstance().submitAAR(playerDeclarations);
        String aarError = AARCoordinator.getInstance().getErrorBundleFileName();
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
        DebriefMapGUI debriefGMap = new DebriefMapGUI(campaign, home);
        debriefGMap.makePanels();
        CampaignGuiContextManager.getInstance().pushToContextStack(debriefGMap);

        // Reset the mission after a combat report has been submitted
        campaign.setCurrentMission(null);
    }
}
