package pwcg.gui.rofmap.brief;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import pwcg.campaign.Campaign;
import pwcg.campaign.context.PWCGContextManager;
import pwcg.campaign.squadmember.SquadronMember;
import pwcg.coop.model.CoopPilot;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.Logger;
import pwcg.gui.CampaignGuiContextManager;
import pwcg.gui.PwcgGuiContext;
import pwcg.gui.campaign.home.CampaignHomeGUI;
import pwcg.gui.campaign.home.GuiMissionInitiator;
import pwcg.gui.dialogs.ErrorDialog;
import pwcg.gui.dialogs.MonitorSupport;
import pwcg.gui.sound.MusicManager;
import pwcg.gui.sound.SoundManager;
import pwcg.gui.utils.ContextSpecificImages;
import pwcg.gui.utils.ImageResizingPanel;
import pwcg.gui.utils.PWCGButtonFactory;
import pwcg.mission.Mission;
import pwcg.mission.MissionHumanParticipants;

public class CoopPilotChooser extends PwcgGuiContext implements ActionListener
{
    private static final long serialVersionUID = 1L;
    private CoopPilotChooserPanel coopPilotAccept;
    private JPanel coopPilotErrorPanel;
    private List<String> errorMessages = new ArrayList<>();
    private CampaignHomeGUI campaignHomeGui;
    private Campaign campaign;
    private JButton missionButton;
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
        	coopPilotAccept = new CoopPilotChooserPanel(campaign, this);
            coopPilotAccept.makePanels();
            setCenterPanel(coopPilotAccept);
            setLeftPanel(makeNavigatePanel());
            buildErrorPanel();
            evaluateErrors();
        }
        catch (Throwable e)
        {
            Logger.logException(e);
            ErrorDialog.internalError(e.getMessage());
        }
    }

    private void buildErrorPanel() throws PWCGException 
    {
        String imagePath = ContextSpecificImages.imagesMisc() + "Paper.jpg";
        coopPilotErrorPanel = new ImageResizingPanel(imagePath);
        coopPilotErrorPanel.setLayout(new GridLayout(0, 2));
        this.add(BorderLayout.SOUTH, coopPilotErrorPanel);
	}
    
    public void evaluateErrors() throws PWCGException
    {
    	errorMessages.clear();
    	coopPilotErrorPanel.removeAll();

    	formErrorMessages();
    	
		addBlankErrorLine();
    	addErrorMessages();    	
		addBlankErrorLine();
		
		if (errorMessages.isEmpty())
		{
			missionButton.setEnabled(true);
		}
		else
		{
			missionButton.setEnabled(false);
		}

    	coopPilotErrorPanel.revalidate();
    }

	private void formErrorMessages() throws PWCGException 
	{
		List<CoopPilot> selectedCoopPilots = coopPilotAccept.getAcceptedCoopPilots();
    	if (selectedCoopPilots.isEmpty())
    	{
            errorMessages.add("No pilots selected for mission");
    	}
    	
    	Map <String, CoopPilot> coopPilotsByCoopUser = new HashMap<>();
    	for (CoopPilot coopPilot : selectedCoopPilots)
    	{
    		if (coopPilotsByCoopUser.containsKey(coopPilot.getUsername()))
    		{
                errorMessages.add("More than one pilot in mission for player " + coopPilot.getUsername() + " Pilot Name: " + coopPilot.getPilotName());
    		}
    		else
    		{
    			coopPilotsByCoopUser.put(coopPilot.getUsername(), coopPilot);
    		}
    	}
	}

	private void addErrorMessages() throws PWCGException 
	{
	    Font font = MonitorSupport.getPrimaryFontLarge();
    	
		for (String errorMessage : errorMessages)
    	{
    		JLabel spacer = new JLabel("   ");
    		spacer.setFont(font);
    		coopPilotErrorPanel.add(spacer);

    		JLabel errorLabel = new JLabel(errorMessage);
    		errorLabel.setFont(font);
    		coopPilotErrorPanel.add(errorLabel);
    	}
	}

	private void addBlankErrorLine() throws PWCGException 
	{
	    Font font = MonitorSupport.getPrimaryFontLarge();
    	
		JLabel spacer = new JLabel("   ");
		spacer.setFont(font);
		coopPilotErrorPanel.add(spacer);
		spacer = new JLabel("   ");
		spacer.setFont(font);
		coopPilotErrorPanel.add(spacer);
	}

	public JPanel makeNavigatePanel() throws PWCGException  
    {
        String imagePath = getSideImageMain("ConfigLeft.jpg");

        JPanel navPanel = new ImageResizingPanel(imagePath);
        navPanel.setLayout(new BorderLayout());

        JPanel buttonPanel = new JPanel(new GridLayout(0,1));
        buttonPanel.setOpaque(false);

        missionButton = PWCGButtonFactory.makeMenuButton("Coop Mission", "CampCoopMission", this);
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
            	List<SquadronMember> selectedCoopPilots = coopPilotAccept.getAcceptedSquadronMembers();
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


