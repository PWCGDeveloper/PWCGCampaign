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
import pwcg.gui.UiImageResolver;
import pwcg.gui.campaign.home.CampaignHome;
import pwcg.gui.dialogs.ErrorDialog;
import pwcg.gui.sound.SoundManager;
import pwcg.gui.utils.ImageResizingPanel;
import pwcg.gui.utils.PWCGButtonFactory;
import pwcg.gui.utils.ScrollBarWrapper;
import pwcg.mission.Mission;

public class BriefingDescriptionPanelSet extends ImageResizingPanel implements ActionListener, IFlightChanged
{
    private CampaignHome campaignHomeGui = null;

	private static final long serialVersionUID = 1L;
    private Mission mission;
    private BriefingContext briefingContext;
    private BriefingFlightChooser briefingFlightChooser;
    private BriefingChalkboard briefingChalkboard;
    
	public BriefingDescriptionPanelSet(CampaignHome campaignHomeGui, Mission mission) throws PWCGException 
	{
        super("");
        this.setLayout(new BorderLayout());
	    
        this.campaignHomeGui =  campaignHomeGui;
        this.mission =  mission;

        briefingContext = new BriefingContext(mission);
        briefingContext.buildBriefingMissions();

		SoundManager.getInstance().playSound("BriefingStart.WAV");
	}

	public void makePanels() 
	{
		try
		{
            String imagePath = UiImageResolver.getImageMain("CampaignHome.jpg");
            this.setImage(imagePath);

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
        JPanel briefingPanel = new JPanel(new BorderLayout());
        briefingPanel.setOpaque(false);

        briefingChalkboard = new BriefingChalkboard(mission, briefingContext);
        briefingChalkboard.makePanel();
        JScrollPane missionScrollPane = ScrollBarWrapper.makeScrollPane(briefingChalkboard);

        briefingPanel.add(missionScrollPane, BorderLayout.CENTER);

        return briefingPanel;
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
            PWCGLogger.logException(e);
            ErrorDialog.internalError(e.getMessage());
        }
    }

    @Override
    public void flightChanged(Squadron squadron) throws PWCGException
    {
        briefingContext.changeSelectedFlight(squadron);
        briefingChalkboard.setMissionText();
    }

    private void forwardToBriefingMap() throws PWCGException 
    {
        SoundManager.getInstance().playSound("Typewriter.WAV");

        BriefingMapGUI briefingMap = new BriefingMapGUI(campaignHomeGui, mission, briefingContext, campaignHomeGui.getCampaign().getDate());
        briefingMap.makePanels();
        CampaignGuiContextManager.getInstance().pushToContextStack(briefingMap);
    }

    private void scrubMission() throws PWCGException
    {
        Campaign campaign  = PWCGContext.getInstance().getCampaign();
        campaign.setCurrentMission(null);
        
        campaignHomeGui.createCampaignHomeContext();

        CampaignGuiContextManager.getInstance().popFromContextStack();
    }

    private void backToCampaign() throws PWCGException
    {
        Campaign campaign  = PWCGContext.getInstance().getCampaign();

        briefingContext.updateMissionBriefingParameters();
        
        campaign.setCurrentMission(mission);
        
        campaignHomeGui.createCampaignHomeContext();
        CampaignGuiContextManager.getInstance().popFromContextStack();
    }
    
    public void refreshScreen() throws PWCGException
    {
        briefingChalkboard.setMissionText();
        briefingFlightChooser.setSelectedButton(briefingContext.getSelectedFlight().getSquadron().getSquadronId());
    }
}
