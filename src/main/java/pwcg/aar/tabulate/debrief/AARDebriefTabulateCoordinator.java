package pwcg.aar.tabulate.debrief;

import pwcg.aar.data.AARContext;
import pwcg.aar.data.UIDebriefData;
import pwcg.aar.ui.display.model.AARAceLeavePanelData;
import pwcg.aar.ui.display.model.AARMedalPanelData;
import pwcg.aar.ui.display.model.AARNewsPanelData;
import pwcg.aar.ui.display.model.AARPilotLossPanelData;
import pwcg.aar.ui.display.model.AARPromotionPanelData;
import pwcg.aar.ui.display.model.TransferPanelData;
import pwcg.campaign.Campaign;
import pwcg.core.exception.PWCGException;

public class AARDebriefTabulateCoordinator
{
    private Campaign campaign;
    private AARContext aarContext;
    private UIDebriefData uiDebriefData = new UIDebriefData();

    public AARDebriefTabulateCoordinator (Campaign campaign, AARContext aarContext)
    {
        this.campaign = campaign;
        this.aarContext = aarContext;
    }
    
    public UIDebriefData tabulateForDebriefUI() throws PWCGException 
    {        
        tabulateAceLeave();
        tabulateMedals();        
        tabulateNews();
        tabulatePilotLosses();
        tabulatePromotions();        
        tabulateTransfers();
        
        return uiDebriefData;
    }

    private void tabulateTransfers() throws PWCGException
    {
        TransferPanelEventTabulator aarTransferPanelEventTabulator = new TransferPanelEventTabulator(campaign, aarContext);
        TransferPanelData transferPanelData = aarTransferPanelEventTabulator.tabulateForAARTransferPanel();
        uiDebriefData.setTransferPanelData(transferPanelData);
    }

    private void tabulatePromotions() throws PWCGException
    {
        PromotionPanelEventTabulator promotionEventTabulator = new PromotionPanelEventTabulator(campaign, aarContext);
        AARPromotionPanelData promotionPanelData = promotionEventTabulator.tabulateForAARPromotionPanel();
        uiDebriefData.setPromotionPanelData(promotionPanelData);
    }

    private void tabulatePilotLosses() throws PWCGException
    {
        PilotLossPanelEventTabulator pilotLossPanelEventTabulator = new PilotLossPanelEventTabulator(campaign, aarContext);
        AARPilotLossPanelData pilotLossPanelData = pilotLossPanelEventTabulator.tabulateForAARPilotLossPanel();
        uiDebriefData.setPilotLossPanelData(pilotLossPanelData);
    }

    private void tabulateNews() throws PWCGException
    {
        NewsPanelEventTabulator newsPanelEventTabulator = new NewsPanelEventTabulator(campaign, aarContext);
        AARNewsPanelData newsPanelData = newsPanelEventTabulator.createNewspaperEvents();
        uiDebriefData.setNewsPanelData(newsPanelData);
    }

    private void tabulateMedals() throws PWCGException
    {
        MedalPanelEventTabulator medaLEventTabulator = new MedalPanelEventTabulator(campaign, aarContext);
        AARMedalPanelData medalPanelData = medaLEventTabulator.tabulateForAARMedalPanel();
        uiDebriefData.setMedalPanelData(medalPanelData);
    }

    private void tabulateAceLeave() throws PWCGException
    {
        AceLeavePanelEventTabulator aceLeavePanelEventTabulator = new AceLeavePanelEventTabulator(campaign, aarContext);
        AARAceLeavePanelData aceLeavePanelData = aceLeavePanelEventTabulator.tabulateForAARAceLeavePanel();
        uiDebriefData.addAceLeavePanelData(aceLeavePanelData);
    }

}
