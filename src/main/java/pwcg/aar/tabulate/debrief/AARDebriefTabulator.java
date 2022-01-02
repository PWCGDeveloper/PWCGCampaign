package pwcg.aar.tabulate.debrief;

import pwcg.aar.data.AARContext;
import pwcg.aar.data.ui.UIDebriefData;
import pwcg.aar.ui.display.model.AARAceLeavePanelData;
import pwcg.aar.ui.display.model.AARCrewMemberLossPanelData;
import pwcg.aar.ui.display.model.AAREquipmentLossPanelData;
import pwcg.aar.ui.display.model.AARMedalPanelData;
import pwcg.aar.ui.display.model.AARNewsPanelData;
import pwcg.aar.ui.display.model.AARPromotionPanelData;
import pwcg.aar.ui.display.model.TransferPanelData;
import pwcg.aar.ui.display.model.VictoryEvents;
import pwcg.campaign.Campaign;
import pwcg.core.exception.PWCGException;

public class AARDebriefTabulator
{
    private Campaign campaign;
    private AARContext aarContext;
    private UIDebriefData uiDebriefData = new UIDebriefData();

    public AARDebriefTabulator (Campaign campaign, AARContext aarContext)
    {
        this.campaign = campaign;
        this.aarContext = aarContext;
    }
    
    public UIDebriefData tabulateForDebriefUI() throws PWCGException 
    {        
        createVictoryEvents();
        tabulateAceLeave();
        tabulateMedals();        
        tabulateNews();
        tabulateCrewMemberLosses();
        tabulateEquipmentLosses();
        tabulatePromotions();   
        tabulateTransfers();
        
        return uiDebriefData;
    }

    private void tabulateTransfers() throws PWCGException
    {
        TransferPanelEventTabulator aarTransferPanelEventTabulator = new TransferPanelEventTabulator(campaign, aarContext);
        TransferPanelData transferPanelData = aarTransferPanelEventTabulator.tabulateForAARTransferPanel();
        uiDebriefData.addTransferPanelData(transferPanelData);
    }

    private void tabulatePromotions() throws PWCGException
    {
        PromotionPanelEventTabulator promotionEventTabulator = new PromotionPanelEventTabulator(campaign, aarContext);
        AARPromotionPanelData promotionPanelData = promotionEventTabulator.tabulateForAARPromotionPanel();
        uiDebriefData.addPromotionPanelData(promotionPanelData);
    }

    private void tabulateCrewMemberLosses() throws PWCGException
    {
        CrewMemberLossPanelEventTabulator crewMemberLossPanelEventTabulator = new CrewMemberLossPanelEventTabulator(campaign, aarContext);
        AARCrewMemberLossPanelData crewMemberLossPanelData = crewMemberLossPanelEventTabulator.tabulateForAARCrewMemberLossPanel();
        uiDebriefData.addCrewMemberLossPanelData(crewMemberLossPanelData);
    }

    private void tabulateEquipmentLosses() throws PWCGException
    {
        EquipmentLossPanelEventTabulator equipmentLossPanelEventTabulator = new EquipmentLossPanelEventTabulator(campaign, aarContext);
        AAREquipmentLossPanelData equipmentLossPanelData = equipmentLossPanelEventTabulator.tabulateForAAREquipmentLossPanel();
        uiDebriefData.addEquipmentLossPanelData(equipmentLossPanelData);        
    }

    private void createVictoryEvents() throws PWCGException
    {
        VictoryPanelEventTabulator squadronOutOfMissionVictoryPanelEventTabulator = new VictoryPanelEventTabulator(campaign, aarContext);
        VictoryEvents victoryPanelData = squadronOutOfMissionVictoryPanelEventTabulator.createVictoryEvents();
        uiDebriefData.addVictoryPanelData(victoryPanelData);        
    }
    
    private void tabulateNews() throws PWCGException
    {
        NewsPanelEventTabulator newsPanelEventTabulator = new NewsPanelEventTabulator(campaign, aarContext);
        AARNewsPanelData newsPanelData = newsPanelEventTabulator.createNewspaperEvents();
        uiDebriefData.addNewsPanelData(newsPanelData);
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
