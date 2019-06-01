package pwcg.gui.rofmap.brief;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JPanel;

import pwcg.campaign.Campaign;
import pwcg.campaign.context.PWCGContextManager;
import pwcg.campaign.squadmember.SquadronMember;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.Logger;
import pwcg.gui.CampaignGuiContextManager;
import pwcg.gui.PwcgGuiContext;
import pwcg.gui.campaign.home.CampaignHomeGUI;
import pwcg.gui.campaign.home.GuiMissionInitiator;
import pwcg.gui.dialogs.ErrorDialog;
import pwcg.gui.sound.MusicManager;
import pwcg.gui.sound.SoundManager;
import pwcg.gui.utils.ImageResizingPanel;
import pwcg.gui.utils.PWCGButtonFactory;
import pwcg.mission.Mission;
import pwcg.mission.MissionHumanParticipants;

public class CoopPilotChooser extends PwcgGuiContext implements ActionListener
{
    private static final long serialVersionUID = 1L;
    private CoopPilotChooserPanel coopPilotAccept;
    private CampaignHomeGUI campaignHomeGui;
    private Campaign campaign;
    private MissionHumanParticipants participatingPlayers = new MissionHumanParticipants();

    public CoopPilotChooser(Campaign campaign,CampaignHomeGUI campaignHomeGui)
    {
        super();
        this.campaign = campaign;
        this.campaignHomeGui = campaignHomeGui;
    }
    
    public void makePanels() 
    {
        try
        {
        	coopPilotAccept = new CoopPilotChooserPanel(campaign);
            coopPilotAccept.makePanels();
            setCenterPanel(coopPilotAccept);
            setLeftPanel(makeNavigatePanel());
        }
        catch (Throwable e)
        {
            Logger.logException(e);
            ErrorDialog.internalError(e.getMessage());
        }
    }

    public JPanel makeNavigatePanel() throws PWCGException  
    {
        String imagePath = getSideImageMain("ConfigLeft.jpg");

        JPanel navPanel = new ImageResizingPanel(imagePath);
        navPanel.setLayout(new BorderLayout());

        JPanel buttonPanel = new JPanel(new GridLayout(0,1));
        buttonPanel.setOpaque(false);

        JButton missionButton = PWCGButtonFactory.makeMenuButton("Coop Mission", "CampCoopMission", this);
        buttonPanel.add(missionButton);

        JButton scrubButton = PWCGButtonFactory.makeMenuButton("Scrub Mission", "ScrubMission", this);
        buttonPanel.add(scrubButton);

        navPanel.add(buttonPanel, BorderLayout.NORTH);
        
        return navPanel;
    }

    public void actionPerformed(ActionEvent ae)
    {
        try
        {
            String action = ae.getActionCommand();
            if (action.equalsIgnoreCase("CampCoopMission"))
            {
            	List<SquadronMember> selectedCoopPilots = coopPilotAccept.getAcceptedPilots();
            	participatingPlayers.addSquadronMembers(selectedCoopPilots);
            	
             	GuiMissionInitiator missionInitiator = new GuiMissionInitiator(campaign, participatingPlayers);
                Mission mission = missionInitiator.makeMission(false);
                showBriefingMap(mission);
            }
            else if (action.equalsIgnoreCase("ScrubMission"))
            {
                scrubMission();
            }
        }
        catch (Throwable e)
        {
            Logger.logException(e);
            ErrorDialog.internalError(e.getMessage());
        }
    }

    private void showBriefingMap(Mission mission) throws PWCGException 
    {
    	MusicManager.playMissionBriefingTheme();
    	SoundManager.getInstance().playSound("Typewriter.WAV");

        BriefingDescriptionPanelSet briefingMap = new BriefingDescriptionPanelSet(campaignHomeGui, mission);
        briefingMap.makePanels();
        CampaignGuiContextManager.getInstance().pushToContextStack(briefingMap);
    }

    private void scrubMission() throws PWCGException
    {
        Campaign campaign  = PWCGContextManager.getInstance().getCampaign();
        campaign.setCurrentMission(null);
        
        campaignHomeGui.clean();
        campaignHomeGui.createPilotContext();

        campaignHomeGui.enableButtonsAsNeeded();
        CampaignGuiContextManager.getInstance().popFromContextStack();
    }

}


