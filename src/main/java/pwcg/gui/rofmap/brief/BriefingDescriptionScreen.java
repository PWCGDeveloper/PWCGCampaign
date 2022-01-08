package pwcg.gui.rofmap.brief;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import pwcg.campaign.Campaign;
import pwcg.campaign.company.Company;
import pwcg.campaign.context.PWCGContext;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.PWCGLogger;
import pwcg.gui.CampaignGuiContextManager;
import pwcg.gui.ScreenIdentifier;
import pwcg.gui.UiImageResolver;
import pwcg.gui.campaign.mission.MissionGeneratorHelper;
import pwcg.gui.dialogs.ErrorDialog;
import pwcg.gui.rofmap.brief.model.BriefingData;
import pwcg.gui.rofmap.brief.update.BriefingUnitUpdater;
import pwcg.gui.sound.SoundManager;
import pwcg.gui.utils.ImageResizingPanel;
import pwcg.gui.utils.PWCGButtonFactory;
import pwcg.gui.utils.PWCGLabelFactory;
import pwcg.gui.utils.ScrollBarWrapper;
import pwcg.mission.Mission;

public class BriefingDescriptionScreen extends ImageResizingPanel implements ActionListener, IUnitChanged
{
	private static final long serialVersionUID = 1L;

	private CampaignHomeGuiBriefingWrapper campaignHomeGuiBriefingWrapper;
    private Mission mission;
    private BriefingData briefingData;
    private BriefingCompanyChooser briefingFlightChooser;
    private BriefingDescriptionChalkboard briefingChalkboard;
    
	public BriefingDescriptionScreen(CampaignHomeGuiBriefingWrapper campaignHomeGuiBriefingWrapper, Mission mission) throws PWCGException 
	{
        super("");
        this.setLayout(new BorderLayout());
	    
        this.campaignHomeGuiBriefingWrapper =  campaignHomeGuiBriefingWrapper;
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

            briefingFlightChooser = new BriefingCompanyChooser(mission, this);
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
            
        if (mission.getFinalizer().isFinalized())
        {
            buttonGrid.add(PWCGLabelFactory.makeDummyLabel());
            JButton backToCampaignButton = makeButton("Back to Campaign", "Back to Campaign", "Return to campaign home screen");
            buttonGrid.add(backToCampaignButton);
        }
        
        buttonGrid.add(PWCGLabelFactory.makeDummyLabel());
        JButton scrubMissionButton = makeButton("Scrub Mission", "Scrub Mission", "Scrub this mission and return to campaign home screen");
        buttonGrid.add(scrubMissionButton);

        buttonGrid.add(PWCGLabelFactory.makeDummyLabel());
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
                MissionGeneratorHelper.scrubMission(mission.getCampaign(), campaignHomeGuiBriefingWrapper);
            }
        }
        catch (Exception e)
        {
            PWCGLogger.logException(e);
            ErrorDialog.internalError(e.getMessage());
        }
    }

    @Override
    public void unitChanged(Company squadron) throws PWCGException
    {
        briefingData.changeSelectedUnit(squadron.getCompanyId());
        briefingChalkboard.setMissionText();
    }

    private void forwardToBriefingMap() throws PWCGException 
    {
        SoundManager.getInstance().playSound("Typewriter.WAV");

        BriefingMapGUI briefingMap = new BriefingMapGUI(mission.getCampaign(), campaignHomeGuiBriefingWrapper);
        briefingMap.makePanels();
        CampaignGuiContextManager.getInstance().pushToContextStack(briefingMap);
    }

    private void backToCampaign() throws PWCGException
    {
        Campaign campaign  = PWCGContext.getInstance().getCampaign();

        BriefingUnitUpdater.pushEditsToMission(briefingData);
        
        campaign.setCurrentMission(mission);
        
        campaignHomeGuiBriefingWrapper.refreshCampaignPage();
        CampaignGuiContextManager.getInstance().popFromContextStack();
    }

    public void refreshScreen() throws PWCGException
    {
        briefingChalkboard.setMissionText();
        briefingFlightChooser.setSelectedButton(briefingData.getSelectedUnit().getCompany().getCompanyId());
    }
}
