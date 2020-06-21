package pwcg.gui.campaign.home;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import pwcg.campaign.Campaign;
import pwcg.core.exception.PWCGException;
import pwcg.core.exception.PWCGUserException;
import pwcg.core.utils.PWCGLogger;
import pwcg.gui.CampaignGuiContextManager;
import pwcg.gui.campaign.CampaignRosterBasePanelFactory;
import pwcg.gui.campaign.CampaignRosterSquadronPanelFactory;
import pwcg.gui.campaign.CampaignRosterTopAcesPanelFactory;
import pwcg.gui.dialogs.ErrorDialog;

public class ChalkboardSelector implements ActionListener
{
    private Campaign campaign;
    private CampaignHomeGUI parent;
    
    public ChalkboardSelector(CampaignHomeGUI parent)
    {
        this.parent = parent;
        this.campaign = parent.getCampaign();
    }

    @Override
    public void actionPerformed(ActionEvent ae)
    {
        try
        {
            String action = ae.getActionCommand();

            if (action.equalsIgnoreCase("CampPilots"))
            {
                createPlayerSquadronContext();
            }
            else if (action.equalsIgnoreCase("CampTopAces"))
            {
                createTopAceContext();
            }
            else if (action.equalsIgnoreCase("Equipment"))
            {
                createEquipmentContext();
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
        catch (Throwable t)
        {
            campaign.setCurrentMission(null);
            PWCGLogger.logException(t);
            ErrorDialog.internalError(t.getMessage());
        }
    }

    public void createEquipmentContext() throws PWCGException 
    {       
        CampaignEquipmentChalkBoard equipmentDisplay = new CampaignEquipmentChalkBoard();
        equipmentDisplay.makeEquipmentPanel(parent.getCampaign());
        // TODO UI RETHINK
        //this.add(BorderLayout.CENTER, equipmentDisplay);

        CampaignGuiContextManager.getInstance().clearContextStack();
        CampaignGuiContextManager.getInstance().pushToContextStack(parent);
    }    

    public void createPlayerSquadronContext() throws PWCGException 
    {
        CampaignRosterBasePanelFactory pilotListDisplay = new CampaignRosterSquadronPanelFactory(this);
        pilotListDisplay.makePilotList();
        createSquadronMemberContext(pilotListDisplay);
    }

    private void createTopAceContext() throws PWCGException 
    {
        CampaignRosterBasePanelFactory topAceListDisplay = new CampaignRosterTopAcesPanelFactory(parent);
        topAceListDisplay.makePilotList();
        createSquadronMemberContext(topAceListDisplay);
    }

    private void createSquadronMemberContext(CampaignRosterBasePanelFactory squadronMemberListDisplay) throws PWCGException 
    {
        squadronMemberListDisplay.makeCampaignHomePanels();
        // TODO UI RETHINK
        //parent.this.add(BorderLayout.CENTER, squadronMemberListDisplay.getChalkboardPanel());
        //parent.this.add(BorderLayout.EAST, squadronMemberListDisplay.getPilotListPanel());

        CampaignGuiContextManager.getInstance().clearContextStack();
        CampaignGuiContextManager.getInstance().pushToContextStack(parent);
    }

}
