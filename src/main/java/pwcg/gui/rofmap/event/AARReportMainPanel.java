package pwcg.gui.rofmap.event;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JPanel;

import pwcg.aar.ui.events.model.TransferEvent;
import pwcg.campaign.Campaign;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.PWCGLogger;
import pwcg.gui.CampaignGuiContextManager;
import pwcg.gui.IRefreshableParentUI;
import pwcg.gui.ScreenIdentifier;
import pwcg.gui.UiImageResolver;
import pwcg.gui.dialogs.ErrorDialog;
import pwcg.gui.utils.ImageResizingPanel;
import pwcg.gui.utils.PWCGButtonFactory;

public class AARReportMainPanel extends ImageResizingPanel implements ActionListener
{
    private static final long serialVersionUID = 1L;
    
    private Campaign campaign;
    private IRefreshableParentUI parentScreen;
    
    private List<IAAREventPanel> eventPanelsToDisplay = new ArrayList<>();
    
    private JButton prevButton = null;
    private JButton nextButton = null;
    private JButton finishedButton = null;
    private IAAREventPanel centerPanel;

    private int currentPanelIndex = 0;
    private EventPanelReason reasonToAdvanceTime = EventPanelReason.EVENT_PANEL_REASON_AAR;
    private TransferEvent transferEventForTimeDueToTransfer = null;
    
    public enum EventPanelReason
    {
        EVENT_PANEL_REASON_AAR,
        EVENT_PANEL_REASON_LEAVE,
        EVENT_PANEL_REASON_TRANSFER
    }
    
    public AARReportMainPanel(Campaign campaign, IRefreshableParentUI parentScreen, EventPanelReason reasonToAdvanceTime)
    {
        super("");
        this.setLayout(new BorderLayout());
        this.setOpaque(false);

        this.campaign = campaign;
        this.parentScreen = parentScreen;
        this.reasonToAdvanceTime = reasonToAdvanceTime;
        this.transferEventForTimeDueToTransfer = null;
    }
    
    public AARReportMainPanel(Campaign campaign, IRefreshableParentUI parentScreen, EventPanelReason reasonToAdvanceTime, TransferEvent transferEventForTimeDueToTransfer)
    {
        super("");
        this.setLayout(new BorderLayout());
        this.setOpaque(false);

        this.campaign = campaign;
        this.parentScreen = parentScreen;
        this.reasonToAdvanceTime = reasonToAdvanceTime;
        this.transferEventForTimeDueToTransfer = transferEventForTimeDueToTransfer;
    }

	public void makePanels() throws PWCGException  
	{        
        String imagePath = UiImageResolver.getImage(ScreenIdentifier.AARReportMainPanel);
        this.setImageFromName(imagePath);

        makeAarEventPanels();            

        this.add(BorderLayout.WEST, makeNavigationPanel());
        resetCenterPanels();        
	}

	private JPanel makeNavigationPanel() throws PWCGException  
	{
	    JPanel navPanel = new JPanel(new BorderLayout());
	    navPanel.setOpaque(false);

		JPanel buttonPanel = new JPanel(new GridLayout(0,1));
		buttonPanel.setOpaque(false);
		
        prevButton = makeMenuButton("Previous Page", "Previous", "Return to previous page");
        buttonPanel.add(prevButton);

        nextButton = makeMenuButton("Next Page", "Next", "Go tothe next page");
        buttonPanel.add(nextButton);

        finishedButton = makeMenuButton("Finished", "Finished", "Finished reading");
        buttonPanel.add(finishedButton);
		
        navPanel.add(buttonPanel, BorderLayout.NORTH);
		
		return navPanel;
	}

    private JButton makeMenuButton(String buttonText, String command, String toolTipText) throws PWCGException
    {
        return PWCGButtonFactory.makeTranslucentMenuButton(buttonText, command, toolTipText, this);
    }

    private void enableButtonsAsNeeded() throws PWCGException  
    {
        prevButton.setEnabled(false);
        nextButton.setEnabled(false);
        finishedButton.setEnabled(false);

        if (currentPanelIndex > 0)
        {
            prevButton.setEnabled(true);
        }

        if (currentPanelIndex < (eventPanelsToDisplay.size() - 1))
        {
            nextButton.setEnabled(true);
        }
        else
        {
            finishedButton.setEnabled(true);
        }
    }

    private void makeAarEventPanels() 
    {
        try
        {
            eventPanelsToDisplay.clear();
            
            List<IAAREventPanel> allEventPanels = new ArrayList<>();

            createPanelSetBasedOnReasonForAdvancingTime(allEventPanels);
            
            AARCrewMemberLossPanel crewMembersLostPanelSet = new AARCrewMemberLossPanel(campaign);
            allEventPanels.add(crewMembersLostPanelSet);
            
            AARMissionVictoryPanel outOfMissionVictoryPanel = new AARMissionVictoryPanel(campaign);
            allEventPanels.add(outOfMissionVictoryPanel);
            
            AAREquipmentChangePanel equipmentChangePanelSet = new AAREquipmentChangePanel(campaign);
            allEventPanels.add(equipmentChangePanelSet);

            AARCrewMembersTransferredPanel crewMembersTransferredPanelSet = new AARCrewMembersTransferredPanel(campaign);
            allEventPanels.add(crewMembersTransferredPanelSet);

            AARCrewMemberLeavePanel crewMembersLeavePanelSet = new AARCrewMemberLeavePanel();
            allEventPanels.add(crewMembersLeavePanelSet);

            AARMedalPanel crewMembersMedalPanelSet = new AARMedalPanel(campaign);
            allEventPanels.add(crewMembersMedalPanelSet);

            AARPromotionPanel crewMembersPromotionPanelSet = new AARPromotionPanel(campaign);
            allEventPanels.add(crewMembersPromotionPanelSet);

            AARNewsPanel newsPanelSet = new AARNewsPanel();
            allEventPanels.add(newsPanelSet);
            
            for (IAAREventPanel eventPanel : allEventPanels)
            {
                eventPanel.makePanel();
                if (eventPanel.isShouldDisplay())
                {
                    eventPanelsToDisplay.add(eventPanel);
                }
            }
        }
        catch (Exception e)
        {
            PWCGLogger.logException(e);
            ErrorDialog.internalError(e.getMessage());
        }
    }

    private void createPanelSetBasedOnReasonForAdvancingTime(List<IAAREventPanel> allEventPanels) throws PWCGException
    {
        if (reasonToAdvanceTime == EventPanelReason.EVENT_PANEL_REASON_AAR)
        {
            AARCombatReportPanel combatReportPanelSet = new AARCombatReportPanel();
            allEventPanels.add(combatReportPanelSet);
        }
        else if (reasonToAdvanceTime == EventPanelReason.EVENT_PANEL_REASON_LEAVE)
        {
            CampaignReportLeaveGUI leaveReportPanelSet = new CampaignReportLeaveGUI(campaign);
            allEventPanels.add(leaveReportPanelSet);
        }
        else if (reasonToAdvanceTime == EventPanelReason.EVENT_PANEL_REASON_TRANSFER)
        {
            CampaignReportTransferPanel leaveReportPanelSet = new CampaignReportTransferPanel(campaign, transferEventForTimeDueToTransfer);
            allEventPanels.add(leaveReportPanelSet);
        }
    }

	private void resetCenterPanels() 
	{
		try
		{
            if (centerPanel != null)
            {
                this.remove(centerPanel.getPanel());
            }

            if (!eventPanelsToDisplay.isEmpty())
            {
    		    centerPanel = eventPanelsToDisplay.get(currentPanelIndex);
    		    this.add(centerPanel.getPanel(), BorderLayout.CENTER);
            }
            
		    enableButtonsAsNeeded();

	        this.revalidate();
	        this.repaint();
		}
		catch (Exception e)
		{
			PWCGLogger.logException(e);
			ErrorDialog.internalError(e.getMessage());
		}
	}

    public void actionPerformed(ActionEvent ae)
    {
        try
        {
            String action = ae.getActionCommand();

            if (action.equalsIgnoreCase("Previous"))
            {
                IAAREventPanel thisPanel = eventPanelsToDisplay.get(currentPanelIndex);
                thisPanel.finished();
                
                --currentPanelIndex;
                
                resetCenterPanels();
            }
            if (action.equalsIgnoreCase("Next"))
            {
                IAAREventPanel thisPanel = eventPanelsToDisplay.get(currentPanelIndex);
                thisPanel.finished();
                
                ++currentPanelIndex;
                
                resetCenterPanels() ;
            }
            else if (action.equalsIgnoreCase("Finished"))
            {
                if (!eventPanelsToDisplay.isEmpty())
                {
                    IAAREventPanel thisPanel = eventPanelsToDisplay.get(currentPanelIndex);
                    thisPanel.finished();
                }
                CampaignGuiContextManager.getInstance().backToCampaignHome();
                parentScreen.refreshInformation();
                CampaignGuiContextManager.getInstance().refreshCurrentContext(parentScreen.getScreen());
            }
        }
        catch (Exception e)
        {
            PWCGLogger.logException(e);
            ErrorDialog.internalError(e.getMessage());
        }
    }

}
