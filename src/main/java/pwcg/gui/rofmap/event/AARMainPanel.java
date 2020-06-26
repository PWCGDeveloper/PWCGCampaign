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
import pwcg.gui.UiImageResolver;
import pwcg.gui.campaign.home.CampaignHome;
import pwcg.gui.dialogs.ErrorDialog;
import pwcg.gui.rofmap.debrief.AAREventPanel;
import pwcg.gui.utils.ImageResizingPanel;
import pwcg.gui.utils.ImageResizingPanelBuilder;
import pwcg.gui.utils.PWCGButtonFactory;

public class AARMainPanel extends JPanel implements ActionListener
{
    private static final long serialVersionUID = 1L;
    
    private Campaign campaign;
    private CampaignHome home = null;

    private List<AAREventPanel> eventPanelsToDisplay = new ArrayList<AAREventPanel>();
    
    private JButton prevButton = null;
    private JButton nextButton = null;
    private JButton finishedButton = null;
    private JPanel centerPanel;

    private int currentPanelIndex = 0;
    private EventPanelReason reasonToAdvanceTime = EventPanelReason.EVENT_PANEL_REASON_AAR;
    private TransferEvent transferEventForTimeDueToTransfer = null;
    
    public enum EventPanelReason
    {
        EVENT_PANEL_REASON_AAR,
        EVENT_PANEL_REASON_LEAVE,
        EVENT_PANEL_REASON_TRANSFER
    }
    
    public AARMainPanel(Campaign campaign, CampaignHome home, EventPanelReason reasonToAdvanceTime)
    {
        super();

        this.campaign = campaign;
        this.home = home;
        this.reasonToAdvanceTime = reasonToAdvanceTime;
        this.transferEventForTimeDueToTransfer = null;
    }
    
    public AARMainPanel(Campaign campaign, CampaignHome home, EventPanelReason reasonToAdvanceTime, TransferEvent transferEventForTimeDueToTransfer)
    {
        super();

        this.campaign = campaign;
        this.home = home;
        this.reasonToAdvanceTime = reasonToAdvanceTime;
        this.transferEventForTimeDueToTransfer = transferEventForTimeDueToTransfer;
    }

	public void makePanels() throws PWCGException  
	{        
        this.add(BorderLayout.WEST, makeNavigationPanel());

        resetPanels();
	}

	private JPanel makeNavigationPanel() throws PWCGException  
	{
        String imagePath = UiImageResolver.getImage(campaign, "MissionResultsNav.jpg");

		ImageResizingPanel resultPanel = ImageResizingPanelBuilder.makeImageResizingPanel(imagePath);
		resultPanel.setLayout(new BorderLayout());
		resultPanel.setOpaque(false);

		JPanel buttonPanel = new JPanel(new GridLayout(0,1));
		buttonPanel.setOpaque(false);
		
        prevButton = PWCGButtonFactory.makeMenuButton("Previous Page", "Previous", this);
        buttonPanel.add(prevButton);

        nextButton = PWCGButtonFactory.makeMenuButton("Next Page", "Next", this);
        buttonPanel.add(nextButton);

        finishedButton = PWCGButtonFactory.makeMenuButton("Finished", "Finished", this);
        buttonPanel.add(finishedButton);
		
		resultPanel.add(buttonPanel, BorderLayout.NORTH);
		
		return resultPanel;
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

    private JPanel makeCenterPanels() 
    {
        ImageResizingPanel postCombatPanel = null;

        try
        {
            eventPanelsToDisplay.clear();
            
            List<AAREventPanel> allEventPanels = new ArrayList<AAREventPanel>();

            createPanelSetBasedOnReasonForAdvancingTime(allEventPanels);
            
            AARPilotLossPanel pilotsLostPanelSet = new AARPilotLossPanel(campaign);
            allEventPanels.add(pilotsLostPanelSet);
            
            AAROutOfMissionVictoryPanel outOfMissionVictoryPanel = new AAROutOfMissionVictoryPanel(campaign);
            allEventPanels.add(outOfMissionVictoryPanel);
            
            AAREquipmentChangePanel equipmentChangePanelSet = new AAREquipmentChangePanel(campaign);
            allEventPanels.add(equipmentChangePanelSet);

            AARPilotsTransferredPanel pilotsTransferredPanelSet = new AARPilotsTransferredPanel(campaign);
            allEventPanels.add(pilotsTransferredPanelSet);

            AARPilotLeavePanel pilotsLeavePanelSet = new AARPilotLeavePanel();
            allEventPanels.add(pilotsLeavePanelSet);

            AARMedalPanel pilotsMedalPanelSet = new AARMedalPanel(campaign);
            allEventPanels.add(pilotsMedalPanelSet);

            AARPromotionPanel pilotsPromotionPanelSet = new AARPromotionPanel(campaign);
            allEventPanels.add(pilotsPromotionPanelSet);

            AARNewsPanel newsPanelSet = new AARNewsPanel(campaign);
            allEventPanels.add(newsPanelSet);
            
            for (AAREventPanel eventPanel : allEventPanels)
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
        
        return postCombatPanel;
    }

    private void createPanelSetBasedOnReasonForAdvancingTime(List<AAREventPanel> allEventPanels)
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
            AARTransferPanel leaveReportPanelSet = new AARTransferPanel(campaign, transferEventForTimeDueToTransfer);
            allEventPanels.add(leaveReportPanelSet);
        }
    }

	private void resetPanels() 
	{
		try
		{
            if (centerPanel != null)
            {
                centerPanel.removeAll();
            }

            centerPanel = makeCenterPanels();
            
		    JPanel nextPanel = eventPanelsToDisplay.get(currentPanelIndex);

		    this.add(BorderLayout.CENTER, nextPanel);
		    
		    enableButtonsAsNeeded();
		    
	        centerPanel.setVisible(false);
	        centerPanel.setVisible(true);
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
                AAREventPanel thisPanel = eventPanelsToDisplay.get(currentPanelIndex);
                thisPanel.finished();
                
                --currentPanelIndex;
                
                resetPanels();
            }
            if (action.equalsIgnoreCase("Next"))
            {
                AAREventPanel thisPanel = eventPanelsToDisplay.get(currentPanelIndex);
                thisPanel.finished();
                
                ++currentPanelIndex;
                
                resetPanels() ;
            }
            else if (action.equalsIgnoreCase("Finished"))
            {
                AAREventPanel thisPanel = eventPanelsToDisplay.get(currentPanelIndex);
                thisPanel.finished();
                
                home.createCampaignHomeContext();
            }
        }
        catch (Exception e)
        {
            PWCGLogger.logException(e);
            ErrorDialog.internalError(e.getMessage());
        }
    }

}
