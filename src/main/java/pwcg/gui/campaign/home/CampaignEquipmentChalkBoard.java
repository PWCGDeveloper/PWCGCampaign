package pwcg.gui.campaign.home;

import java.awt.BorderLayout;
import java.util.Map;

import javax.swing.JPanel;

import pwcg.campaign.Campaign;
import pwcg.campaign.plane.EquippedPlane;
import pwcg.campaign.squadmember.SquadronMember;
import pwcg.core.utils.PWCGLogger;
import pwcg.gui.dialogs.ErrorDialog;

public class CampaignEquipmentChalkBoard extends JPanel
{
    private static final long serialVersionUID = 1L;
    
    private ChalkboardSelector chalkboardSelector;

    public CampaignEquipmentChalkBoard(ChalkboardSelector chalkboardSelector)  
    {
        super();
        this.setLayout(new BorderLayout());
        this.setOpaque(false);
        this.chalkboardSelector = chalkboardSelector;
    }
    

    public void makeEquipmentPanel(Campaign campaign)  
    {
        try
        {
            this.add(chalkboardSelector, BorderLayout.NORTH);
            
        	SquadronMember referencePlayer = campaign.findReferencePlayer();            
            Map<Integer, EquippedPlane> planesForSquadron = campaign.getEquipmentManager().getEquipmentForSquadron(referencePlayer.getSquadronId()).getActiveEquippedPlanes();
            
            EquipmentChalkboardBuilder equipmentChalkboardBuilder = new EquipmentChalkboardBuilder();
            JPanel equipmentPanel = equipmentChalkboardBuilder.createEquipmentListPanel(planesForSquadron);
                    
            this.add(equipmentPanel, BorderLayout.CENTER);
        }
        catch (Exception e)
        {
            PWCGLogger.logException(e);
            ErrorDialog.internalError(e.getMessage());
        }
    }
}
