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
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.squadmember.SquadronMember;
import pwcg.coop.model.CoopPersona;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.PWCGLogger;
import pwcg.gui.CampaignGuiContextManager;
import pwcg.gui.UiImageResolver;
import pwcg.gui.campaign.home.CampaignHome;
import pwcg.gui.campaign.home.GuiMissionInitiator;
import pwcg.gui.dialogs.ErrorDialog;
import pwcg.gui.dialogs.PWCGMonitorFonts;
import pwcg.gui.sound.MusicManager;
import pwcg.gui.sound.SoundManager;
import pwcg.gui.utils.ContextSpecificImages;
import pwcg.gui.utils.ImageResizingPanelBuilder;
import pwcg.gui.utils.PWCGButtonFactory;
import pwcg.mission.Mission;
import pwcg.mission.MissionHumanParticipants;

public class CoopPersonaChooser extends JPanel implements ActionListener
{
    private static final long serialVersionUID = 1L;
    private CoopPersonaChooserPanel coopPersonaAccept;
    private JPanel coopPersonaErrorPanel;
    private List<String> errorMessages = new ArrayList<>();
    private CampaignHome campaignHomeGui;
    private Campaign campaign;
    private JButton missionButton;
    private MissionHumanParticipants participatingPlayers = new MissionHumanParticipants();

    public CoopPersonaChooser(Campaign campaign,CampaignHome campaignHomeGui)
    {
        super();
        this.campaign = campaign;
        this.campaignHomeGui = campaignHomeGui;
    }
    
    public void makePanels() 
    {
        try
        {
        	coopPersonaAccept = new CoopPersonaChooserPanel(campaign, this);
            coopPersonaAccept.makePanels();
            this.add(BorderLayout.CENTER, coopPersonaAccept);
            this.add(BorderLayout.WEST, makeNavigatePanel());
            buildErrorPanel();
            evaluateErrors();
        }
        catch (Throwable e)
        {
            PWCGLogger.logException(e);
            ErrorDialog.internalError(e.getMessage());
        }
    }

    private void buildErrorPanel() throws PWCGException 
    {
        String imagePath = ContextSpecificImages.imagesMisc() + "Paper.jpg";
        coopPersonaErrorPanel = ImageResizingPanelBuilder.makeImageResizingPanel(imagePath);
        coopPersonaErrorPanel.setLayout(new GridLayout(0, 2));
        this.add(BorderLayout.SOUTH, coopPersonaErrorPanel);
	}
    
    public void evaluateErrors() throws PWCGException
    {
    	errorMessages.clear();
    	coopPersonaErrorPanel.removeAll();

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

    	coopPersonaErrorPanel.revalidate();
    }

	private void formErrorMessages() throws PWCGException 
	{
		List<CoopPersona> selectedCoopPersonas = coopPersonaAccept.getAcceptedCoopPersonas();
    	if (selectedCoopPersonas.isEmpty())
    	{
            errorMessages.add("No pilots selected for mission");
    	}
    	
    	Map <String, CoopPersona> coopPersonasByCoopUser = new HashMap<>();
    	for (CoopPersona coopPersona : selectedCoopPersonas)
    	{
    		if (coopPersonasByCoopUser.containsKey(coopPersona.getCoopUsername()))
    		{
    		    if (!coopPersona.getCoopUsername().equals(CoopPersonaChooserPanel.NO_USER_FOR_PILOT))
    		    {
    		        SquadronMember pilot = campaign.getPersonnelManager().getAnyCampaignMember(coopPersona.getSerialNumber());
    		        errorMessages.add("More than one pilot in mission for player " + coopPersona.getCoopUsername() + " Pilot Name: " + pilot.getNameAndRank());
    		    }
    		}
    		else
    		{
    			coopPersonasByCoopUser.put(coopPersona.getCoopUsername(), coopPersona);
    		}
    	}
    	
    	SquadronMember referencePlayer = campaign.findReferencePlayer();
        boolean referencePlayerIncluded = false;
        for (CoopPersona coopPersona : selectedCoopPersonas)
        {
            if (coopPersona.getSerialNumber() == referencePlayer.getSerialNumber())
            {
                referencePlayerIncluded = true;
            }
        }
        
        if (!referencePlayerIncluded)
        {
            errorMessages.add("Reference player " + referencePlayer.getNameAndRank() + " must be included in the mission");
        }
	}

	private void addErrorMessages() throws PWCGException 
	{
	    Font font = PWCGMonitorFonts.getPrimaryFontLarge();
    	
        JLabel spacer = new JLabel("   ");
        spacer.setFont(font);
        coopPersonaErrorPanel.add(spacer);

		for (String errorMessage : errorMessages)
    	{
    		JLabel errorLabel = new JLabel(errorMessage);
    		errorLabel.setFont(font);
    		coopPersonaErrorPanel.add(errorLabel);
    	}

        JLabel spacer2 = new JLabel("   ");
        spacer2.setFont(font);
        coopPersonaErrorPanel.add(spacer2);
	}

	private void addBlankErrorLine() throws PWCGException 
	{
	    Font font = PWCGMonitorFonts.getPrimaryFontLarge();
    	
	    for (int i = 0; i < 3; ++i)
	    {
	        JLabel spacer = new JLabel("   ");
	        spacer.setFont(font);
	        coopPersonaErrorPanel.add(spacer);
	    }
	}

	public JPanel makeNavigatePanel() throws PWCGException  
    {
        String imagePath = UiImageResolver.getImageMain("ConfigLeft.jpg");

        JPanel navPanel = ImageResizingPanelBuilder.makeImageResizingPanel(imagePath);
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
            	List<SquadronMember> selectedCoopPersonas = coopPersonaAccept.getAcceptedSquadronMembers();
            	participatingPlayers.addSquadronMembers(selectedCoopPersonas);
            	
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
            PWCGLogger.logException(e);
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
        Campaign campaign  = PWCGContext.getInstance().getCampaign();
        campaign.setCurrentMission(null);        
        campaignHomeGui.createCampaignHomeContext();
        CampaignGuiContextManager.getInstance().backToCampaignHome();
    }

}


