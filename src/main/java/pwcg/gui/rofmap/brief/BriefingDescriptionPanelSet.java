package pwcg.gui.rofmap.brief;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import pwcg.campaign.Campaign;
import pwcg.campaign.context.PWCGContextManager;
import pwcg.campaign.squadmember.SquadronMember;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.Logger;
import pwcg.gui.CampaignGuiContextManager;
import pwcg.gui.PwcgGuiContext;
import pwcg.gui.campaign.home.CampaignHomeGUI;
import pwcg.gui.colors.ColorMap;
import pwcg.gui.dialogs.ErrorDialog;
import pwcg.gui.dialogs.MonitorSupport;
import pwcg.gui.helper.BriefingMissionHandler;
import pwcg.gui.sound.SoundManager;
import pwcg.gui.utils.ContextSpecificImages;
import pwcg.gui.utils.ImageResizingPanel;
import pwcg.gui.utils.PWCGButtonFactory;
import pwcg.gui.utils.ScrollBarWrapper;
import pwcg.mission.IMissionDescription;
import pwcg.mission.Mission;
import pwcg.mission.MissionDescriptionFactory;
import pwcg.mission.flight.Flight;

public class BriefingDescriptionPanelSet extends PwcgGuiContext implements ActionListener
{
    private CampaignHomeGUI campaignHomeGui = null;

	private static final long serialVersionUID = 1L;
    private BriefingMissionHandler briefingMissionHandler = null;
    private JTextArea missionText = new JTextArea();

	public BriefingDescriptionPanelSet(CampaignHomeGUI campaignHomeGui, Mission mission) throws PWCGException 
	{
	    super();
	    
        this.campaignHomeGui =  campaignHomeGui;

        Flight myFlight = mission.getMissionFlightBuilder().getPlayerFlight(PWCGContextManager.getInstance().getReferencePlayer());
		briefingMissionHandler = new BriefingMissionHandler(mission);
		briefingMissionHandler.initializeFromMission(myFlight.getSquadron());

		SoundManager.getInstance().playSound("BriefingStart.WAV");
	}

	public void makePanels() 
	{
		try
		{
			this.removeAll();
			setLeftPanel(makeButtonPanel());
			setCenterPanel(makeBriefingPanel());
	        setMissionText();
		}
		catch (Exception e)
		{
			Logger.logException(e);
			ErrorDialog.internalError(e.getMessage());
		}
	}

    private JPanel makeButtonPanel() throws PWCGException 
    {
        String imagePath = getSideImage("BriefingNav.jpg");

        ImageResizingPanel buttonPanel = new ImageResizingPanel(imagePath);
        buttonPanel.setLayout(new BorderLayout());
        buttonPanel.setOpaque(false);

        JPanel buttonGrid = new JPanel();
        buttonGrid.setLayout(new GridLayout(0,1));
        buttonGrid.setOpaque(false);
            
        if (briefingMissionHandler.getMission().isFinalized())
        {
            buttonGrid.add(PWCGButtonFactory.makeDummy());
            makeButton(buttonGrid, "Back to Campaign");
        }
        
        buttonGrid.add(PWCGButtonFactory.makeDummy());
        makeButton(buttonGrid, "Scrub Mission");

        buttonGrid.add(PWCGButtonFactory.makeDummy());
        makeButton(buttonGrid, "Briefing Map");

        buttonPanel.add(buttonGrid, BorderLayout.NORTH);
        
        return buttonPanel;
    }
    
    public JPanel makeBriefingPanel() throws PWCGException  
    {
        String imagePath = ContextSpecificImages.imagesMisc() + "PilotSelectChalkboard.jpg";
        JPanel briefingPanel = new ImageResizingPanel(imagePath);
        briefingPanel.setLayout(new BorderLayout());
        briefingPanel.setOpaque(false);

        JPanel missionTextPanel = makeMissionText();
        
        JScrollPane missionScrollPane = ScrollBarWrapper.makeScrollPane(missionTextPanel);

        briefingPanel.add(missionScrollPane, BorderLayout.CENTER);

        return briefingPanel;
    }

    private JPanel makeMissionText() throws PWCGException 
    {
        JPanel missionTextPanel = new JPanel(new BorderLayout());
        missionTextPanel.setOpaque(false);
        
        Font font = MonitorSupport.getBriefingChalkboardFont();

        missionText.setFont(font);
        missionText.setOpaque(false);
        missionText.setLineWrap(true);
        missionText.setWrapStyleWord(true);
        missionText.setForeground(ColorMap.CHALK_FOREGROUND);
        
        // Calculate the writable area of the text and generate margins scaled to screen size
        Insets margins = MonitorSupport.calculateInset(50, 35, 65, 35);
        missionText.setMargin(margins);
        
        missionTextPanel.add(missionText, BorderLayout.CENTER);

        return missionTextPanel;
    }

    public void setMissionText() throws PWCGException 
    {
        Campaign campaign = PWCGContextManager.getInstance().getCampaign();
        IMissionDescription missionDescription =MissionDescriptionFactory.buildMissionDescription(campaign, briefingMissionHandler.getMission());
        String missionDescriptionText = missionDescription.createDescription();
        
        StringBuffer missionDescriptionBuffer = new StringBuffer("");
        missionDescriptionBuffer.append("Mission: \n");
        missionDescriptionBuffer.append(missionDescriptionText);

        String pilotList = makePilotList();
        missionDescriptionBuffer.append(pilotList.toString());
        
        missionText.setText(missionDescriptionBuffer.toString());
    }

    public String makePilotList() throws PWCGException 
    {
        StringBuffer assignedPilotsBuffer = new StringBuffer ("Assigned Pilots:\n");
        for (SquadronMember squadronMember : briefingMissionHandler.getSortedAssigned())
        {
            assignedPilotsBuffer.append("    " + squadronMember.getNameAndRank() + "\n");
        }
        
        return assignedPilotsBuffer.toString();
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
            
            if (action.equals("Back to Campaign"))
            {
                backToCampaign();
            }
            else if (action.equals("Briefing Map"))
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
            Logger.logException(e);
            ErrorDialog.internalError(e.getMessage());
        }
    }

    private void forwardToBriefingMap() throws PWCGException 
    {
        SoundManager.getInstance().playSound("Typewriter.WAV");

        BriefingMapGUI briefingMap = new BriefingMapGUI(campaignHomeGui, briefingMissionHandler, campaignHomeGui.getCampaign().getDate());
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

    private void backToCampaign() throws PWCGException
    {
        Campaign campaign  = PWCGContextManager.getInstance().getCampaign();

        briefingMissionHandler.updateMissionBriefingParameters();
        campaign.setCurrentMission(briefingMissionHandler.getMission());
        
        campaignHomeGui.clean();
        campaignHomeGui.createPilotContext();
        campaignHomeGui.enableButtonsAsNeeded();
        CampaignGuiContextManager.getInstance().popFromContextStack();
    }
}
