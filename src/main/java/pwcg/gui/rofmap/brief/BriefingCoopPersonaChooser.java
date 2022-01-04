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
import javax.swing.SwingConstants;

import pwcg.campaign.Campaign;
import pwcg.campaign.crewmember.CrewMember;
import pwcg.campaign.tank.PwcgRole;
import pwcg.coop.CoopUserManager;
import pwcg.coop.model.CoopUser;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.PWCGLogger;
import pwcg.gui.CampaignGuiContextManager;
import pwcg.gui.ScreenIdentifier;
import pwcg.gui.UiImageResolver;
import pwcg.gui.campaign.mission.MissionGeneratorHelper;
import pwcg.gui.colors.ColorMap;
import pwcg.gui.dialogs.ErrorDialog;
import pwcg.gui.dialogs.PWCGMonitorFonts;
import pwcg.gui.sound.SoundManager;
import pwcg.gui.utils.ImageResizingPanel;
import pwcg.gui.utils.PWCGButtonFactory;
import pwcg.gui.utils.PWCGLabelFactory;
import pwcg.mission.MissionHumanParticipants;

public class BriefingCoopPersonaChooser extends ImageResizingPanel implements ActionListener
{
    private static final String CAMP_COOP_MISSION = "CampCoopMission";

    private static final long serialVersionUID = 1L;
    
    private CoopPersonaChooserPanel coopPersonaAccept;
    private JPanel coopPersonaErrorPanel;
    private List<String> errorMessages = new ArrayList<>();
    private CampaignHomeGuiBriefingWrapper campaignHomeGuiBriefingWrapper;
    private Campaign campaign;
    private JButton missionButton;
    private boolean overrideRole;
    private MissionHumanParticipants participatingPlayers = new MissionHumanParticipants();

    public BriefingCoopPersonaChooser(Campaign campaign, String missionChoice, CampaignHomeGuiBriefingWrapper campaignHomeGuiBriefingWrapper, boolean overrideRole)
    {
        super("");
        this.setLayout(new BorderLayout());
        this.setOpaque(false);

        this.campaign = campaign;
        this.campaignHomeGuiBriefingWrapper = campaignHomeGuiBriefingWrapper;
        this.overrideRole = overrideRole;
    }
    
    public void makePanels() 
    {
        try
        {
            String imagePath = UiImageResolver.getImage(ScreenIdentifier.BriefingCoopPersonaChooser);
            this.setImageFromName(imagePath);

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
        coopPersonaErrorPanel = new JPanel(new GridLayout(0, 2));
        coopPersonaErrorPanel.setOpaque(false);
        coopPersonaAccept.add(BorderLayout.SOUTH, coopPersonaErrorPanel);
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
		List<Integer> selectedCoopPersonas = coopPersonaAccept.getAcceptedCoopPersonas();
    	if (selectedCoopPersonas.isEmpty())
    	{
            errorMessages.add("No crewMembers selected for mission");
    	}
    	
    	CoopUserManager coopUserManager = CoopUserManager.getIntance();
    	Map <String, Integer> coopPersonasByCoopUser = new HashMap<>();
    	for (Integer coopPersona : selectedCoopPersonas)
    	{
    	    CoopUser coopUser = coopUserManager.getCoopUserForCrewMember(campaign.getName(), coopPersona);
            if (coopPersonasByCoopUser.containsKey(coopUser.getUsername()))
            {
                errorMessages.add("More than one crewMember in mission for player " + coopUser.getUsername());
            }
    		else
    		{
    			coopPersonasByCoopUser.put(coopUser.getUsername(), coopPersona);
    		}
    	}
    	
    	CrewMember referencePlayer = campaign.findReferencePlayer();
        boolean referencePlayerIncluded = false;
        for (int coopPersona : selectedCoopPersonas)
        {
            if (coopPersona == referencePlayer.getSerialNumber())
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
    	
        coopPersonaErrorPanel.add(PWCGLabelFactory.makeDummyLabel());

		for (String errorMessage : errorMessages)
    	{
	        JLabel errorLabel = PWCGLabelFactory.makeTransparentLabel(errorMessage, ColorMap.PAPER_FOREGROUND, font, SwingConstants.LEFT);
    		coopPersonaErrorPanel.add(errorLabel);
    	}

        coopPersonaErrorPanel.add(PWCGLabelFactory.makeDummyLabel());
	}

	private void addBlankErrorLine() throws PWCGException 
	{    	
	    for (int i = 0; i < 3; ++i)
	    {
	        coopPersonaErrorPanel.add(PWCGLabelFactory.makeDummyLabel());
	    }
	}

	public JPanel makeNavigatePanel() throws PWCGException  
    {
        JPanel navPanel = new JPanel(new BorderLayout());
        navPanel.setOpaque(false);

        JPanel buttonPanel = new JPanel(new GridLayout(0,1));
        buttonPanel.setOpaque(false);

        missionButton = makeMenuButton("Coop Mission", CAMP_COOP_MISSION, "Create a coop mission");
        buttonPanel.add(missionButton);

        JButton scrubButton = makeMenuButton("Scrub Mission", "ScrubMission", "Scrub this mission");
        buttonPanel.add(scrubButton);

        navPanel.add(buttonPanel, BorderLayout.NORTH);
        
        return navPanel;
    }

    private JButton makeMenuButton(String buttonText, String command, String toolTipText) throws PWCGException
    {
        return PWCGButtonFactory.makeTranslucentMenuButton(buttonText, command, toolTipText, this);
    }

    public void actionPerformed(ActionEvent ae)
    {
        try
        {
            String action = ae.getActionCommand();
            if (action.equalsIgnoreCase(CAMP_COOP_MISSION))
            {
                if (!overrideRole)
                {
                    generateMission();
                }
                else
                {
                    generateMissionWithRoleOverride();
                }
            }
            else if (action.equalsIgnoreCase("ScrubMission"))
            {
                MissionGeneratorHelper.scrubMission(campaign, campaignHomeGuiBriefingWrapper);
            }
        }
        catch (Throwable e)
        {
            PWCGLogger.logException(e);
            ErrorDialog.internalError(e.getMessage());
        }
    }

    private void generateMission() throws PWCGException
    {
        Map<Integer, PwcgRole> squadronRoleOverride = new HashMap<>();
        List<CrewMember> selectedCoopPersonas = coopPersonaAccept.getAcceptedCrewMembers();
        participatingPlayers.addCrewMembers(selectedCoopPersonas);            	
        MissionGeneratorHelper.showBriefingMap(campaign, campaignHomeGuiBriefingWrapper, participatingPlayers, squadronRoleOverride);
    }


    private void generateMissionWithRoleOverride() throws PWCGException
    {
        SoundManager.getInstance().playSound("Typewriter.WAV");
        List<CrewMember> selectedCoopPersonas = coopPersonaAccept.getAcceptedCrewMembers();
        participatingPlayers.addCrewMembers(selectedCoopPersonas);              
        BriefingRoleChooser briefingRoleChooser = new BriefingRoleChooser(campaign, campaignHomeGuiBriefingWrapper, participatingPlayers);
        briefingRoleChooser.makePanels();
        CampaignGuiContextManager.getInstance().pushToContextStack(briefingRoleChooser);
    }
}


