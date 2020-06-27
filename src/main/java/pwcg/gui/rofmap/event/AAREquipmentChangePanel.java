package pwcg.gui.rofmap.event;

import java.awt.BorderLayout;
import java.awt.Color;
import java.util.HashMap;

import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import pwcg.aar.AARCoordinator;
import pwcg.aar.ui.display.model.AAREquipmentLossPanelData;
import pwcg.aar.ui.events.model.PlaneStatusEvent;
import pwcg.campaign.Campaign;
import pwcg.campaign.squadmember.SquadronMember;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.PWCGLogger;
import pwcg.gui.UiImageResolver;
import pwcg.gui.colors.ColorMap;
import pwcg.gui.dialogs.ErrorDialog;
import pwcg.gui.rofmap.debrief.IAAREventPanel;
import pwcg.gui.utils.ImageResizingPanel;

public class AAREquipmentChangePanel extends ImageResizingPanel implements IAAREventPanel
{
    private static final long serialVersionUID = 1L;
    private AARCoordinator aarCoordinator;
    private Campaign campaign;
    private boolean shouldDisplay = false;

    public AAREquipmentChangePanel(Campaign campaign)
	{
        super("");
        this.setLayout(new BorderLayout());
        this.setOpaque(false);

        this.aarCoordinator = AARCoordinator.getInstance();
        this.campaign = campaign;
	}

	public void makePanel() throws PWCGException  
	{
        try
        {
            String imagePath = UiImageResolver.getImageMain("document.png");
            this.setImage(imagePath);

            JTabbedPane eventTabPane = createEquipmentLostTab();
            createPostCombatReportTabs(eventTabPane);            
            this.add(eventTabPane, BorderLayout.CENTER);
        }
        catch (Exception e)
        {
            PWCGLogger.logException(e);
            ErrorDialog.internalError(e.getMessage());
        }
    }
    
    private void createPostCombatReportTabs(JTabbedPane eventTabPane)
    {
        JPanel postCombatPanel = new JPanel(new BorderLayout());
        postCombatPanel.setOpaque(false);

        postCombatPanel.add(eventTabPane, BorderLayout.CENTER);
        this.add(postCombatPanel, BorderLayout.CENTER);
    }

    private JTabbedPane createEquipmentLostTab() throws PWCGException
    {
        Color bgColor = ColorMap.PAPER_BACKGROUND;

        JTabbedPane eventTabPane = new JTabbedPane();
        eventTabPane.setBackground(bgColor);
        eventTabPane.setOpaque(false);
       
        HashMap<String, CampaignReportEquipmentStatusGUI> equipmentGuiList = createEquipmentLostSubTabs() ;
        for (String tabName : equipmentGuiList.keySet())
        {
            eventTabPane.addTab(tabName, equipmentGuiList.get(tabName));
            shouldDisplay = true;
        }


        return eventTabPane;
    }

	private HashMap<String, CampaignReportEquipmentStatusGUI> createEquipmentLostSubTabs() throws PWCGException 
	{
	    AAREquipmentLossPanelData equipmentLossPanelData = aarCoordinator.getAarContext().getUiDebriefData().getEquipmentLossPanelData();
        HashMap<String, CampaignReportEquipmentStatusGUI> equipmentLostGuiList = new HashMap<>();
        for (PlaneStatusEvent planeStatusEvent : equipmentLossPanelData.getEquipmentLost().values())
		{
            SquadronMember referencePlayer = campaign.findReferencePlayer();
            if (planeStatusEvent.getSquadronId() == referencePlayer.getSquadronId())
            {
                CampaignReportEquipmentStatusGUI equipmentChangeGui = new CampaignReportEquipmentStatusGUI(campaign, planeStatusEvent);
                String tabName = "Plane Lost: " + planeStatusEvent.getPlaneSerialNumber();
                equipmentLostGuiList.put(tabName, equipmentChangeGui);
            }
		}
        
        return equipmentLostGuiList;
	}

    @Override
    public void finished()
    {
    }

    @Override
    public boolean isShouldDisplay()
    {
        return shouldDisplay;
    }

    @Override
    public JPanel getPanel()
    {
        return this;
    }
}
