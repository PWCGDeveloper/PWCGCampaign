package pwcg.gui.rofmap.debrief;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;

import javafx.scene.control.Button;
import javafx.scene.layout.Pane;

import pwcg.aar.inmission.phase3.reconcile.victories.singleplayer.PlayerDeclarations;
import pwcg.campaign.Campaign;
import pwcg.campaign.context.PWCGContext;
import pwcg.core.exception.PWCGException;
import pwcg.core.exception.PWCGUserException;
import pwcg.core.utils.PWCGLogger;
import pwcg.gui.CampaignGuiContextManager;
import pwcg.gui.ScreenIdentifier;
import pwcg.gui.UiImageResolver;
import pwcg.gui.campaign.home.CampaignHomeScreen;
import pwcg.gui.dialogs.ErrorDialog;
import pwcg.gui.dialogs.PWCGMonitorSupport;
import pwcg.gui.sound.SoundManager;
import pwcg.gui.utils.ImageResizingPanel;
import pwcg.gui.utils.ButtonFactory;
import pwcg.gui.utils.SpacerPanelFactory;

public class AARInitiationScreen extends ImageResizingPanel implements ActionListener
{
    private static final long serialVersionUID = 1L;

    private Campaign campaign = null;
	private CampaignHomeScreen home = null;
    private AARClaimPanels aarClaimPanel = null;

	public AARInitiationScreen(CampaignHomeScreen home)  
	{
        super("");
        this.setLayout(new BorderLayout());
        this.setOpaque(false);

		this.campaign = PWCGContext.getInstance().getCampaign();
        this.home = home;
	}

    public void makePanel() throws PWCGException
    {
        String imagePath = UiImageResolver.getImage(ScreenIdentifier.AARInitiationScreen);
        this.setImageFromName(imagePath);

        this.add(makeNavigationPanel(), BorderLayout.WEST);
        this.add(makeCenterPanel(), BorderLayout.CENTER);
        this.add(SpacerPanelFactory.makeSpacerConsumeRemainingPanel(1500), BorderLayout.EAST);
	}

    private Pane makeCenterPanel() throws PWCGException  
    {
        aarClaimPanel = new AARClaimPanels(campaign);
        aarClaimPanel.makePanels();

        int numSpacers = calcNumSpacers();
        Pane claimPanel = new Pane(new BorderLayout());
        claimPanel.setOpaque(false);
        claimPanel.add(SpacerPanelFactory.createVerticalSpacerPanel(numSpacers), BorderLayout.NORTH);
        claimPanel.add(aarClaimPanel, BorderLayout.CENTER);
        claimPanel.add(SpacerPanelFactory.createVerticalSpacerPanel(numSpacers), BorderLayout.SOUTH);

        return claimPanel;
    }

    private int calcNumSpacers()
    {
        if (PWCGMonitorSupport.isVerySmallScreen())
        {
            return 1;
        }
        else if (PWCGMonitorSupport.isSmallScreen())
        {
            return 2;
        }
        else if (PWCGMonitorSupport.isMediumScreen())
        {
            return 3;
        }
        else
        {
            return 6;
        }
    }

	private Pane makeNavigationPanel() throws PWCGException  
	{
        Pane aarButtonPanel = new Pane(new BorderLayout());
        aarButtonPanel.setOpaque(false);
		
		Pane buttonGrid = new Pane(new GridLayout(0,1));
		buttonGrid.setOpaque(false);
        
        makeButton ("Submit Report", "Submit Report", "Begin AAR process", buttonGrid);
        makeButton("Cancel AAR",  "Cancel", "Cancel AAR Process", buttonGrid);

		aarButtonPanel.add(buttonGrid, BorderLayout.NORTH);
		
		return aarButtonPanel;
	}

    private void makeButton(String buttonText, String command, String toolTipText, Pane buttonGrid) throws PWCGException 
    {
        buttonGrid.add(ButtonFactory.makeDummy());  
        Button button = ButtonFactory.makeTranslucentMenuButton(buttonText, command, toolTipText, this);
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
                Map<Integer, PlayerDeclarations> playerDeclarations = aarClaimPanel.getPlayerDeclarations();
                submitReport(playerDeclarations);
            }
            else if (action.equals("Cancel"))
            {
                home.refreshInformation();
                CampaignGuiContextManager.getInstance().popFromContextStack();
            }
        }
        catch (PWCGUserException ue)
        {
            campaign.setCurrentMission(null);
            PWCGLogger.logException(ue);
            ErrorDialog.userError(ue.getMessage());
        }
        catch (Exception e)
        {
            campaign.setCurrentMission(null);
            PWCGLogger.logException(e);
            ErrorDialog.internalError(e.getMessage());
        }
    }

    private void submitReport(Map<Integer, PlayerDeclarations> playerDeclarations) throws PWCGException
    {
        SoundManager.getInstance().playSound("Stapling.WAV");   
        AARSubmitter submitter = new AARSubmitter();
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
        DebriefMapGUI debriefMap = new DebriefMapGUI(campaign, home);
        debriefMap.makePanels();
        CampaignGuiContextManager.getInstance().pushToContextStack(debriefMap);

        campaign.setCurrentMission(null);
    }
}
