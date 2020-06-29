package pwcg.gui.campaign.home;

import java.awt.BorderLayout;

import javax.swing.JPanel;

import pwcg.campaign.Campaign;
import pwcg.core.utils.PWCGLogger;
import pwcg.gui.dialogs.ErrorDialog;
import pwcg.gui.utils.SpacerPanelFactory;

public class CampaignEquipmentChalkBoardPanelSet extends JPanel
{
    private static final long serialVersionUID = 1L;
    
    private ChalkboardSelector chalkboardSelector;

    public CampaignEquipmentChalkBoardPanelSet(ChalkboardSelector chalkboardSelector)  
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

            CampaignEquipmentChalkboard equipmentChalkboardScreen = new CampaignEquipmentChalkboard(campaign);
            equipmentChalkboardScreen.makePanels();
            this.add(equipmentChalkboardScreen, BorderLayout.CENTER);
            
            this.add(SpacerPanelFactory.createVerticalSpacerPanel(5), BorderLayout.SOUTH);
        }
        catch (Exception e)
        {
            PWCGLogger.logException(e);
            ErrorDialog.internalError(e.getMessage());
        }
    }
}
