package pwcg.aar.data.ui;

import pwcg.aar.ui.display.model.AARAceLeavePanelData;
import pwcg.aar.ui.display.model.AAREquipmentLossPanelData;
import pwcg.aar.ui.display.model.AARMedalPanelData;
import pwcg.aar.ui.display.model.AARNewsPanelData;
import pwcg.aar.ui.display.model.AARPilotLossPanelData;
import pwcg.aar.ui.display.model.AARPromotionPanelData;
import pwcg.aar.ui.display.model.TransferPanelData;
import pwcg.aar.ui.display.model.VictoryEvents;
import pwcg.campaign.Campaign;
import pwcg.core.exception.PWCGException;

public class UIDebriefData
{
    private AARNewsPanelData newsPanelData = new AARNewsPanelData();
    private AARAceLeavePanelData aceLeavePanelData = new AARAceLeavePanelData();
    private AARMedalPanelData medalPanelData = new AARMedalPanelData();
    private AARPilotLossPanelData pilotLossPanelData = new AARPilotLossPanelData();
    private AAREquipmentLossPanelData equipmentLossPanelData = new AAREquipmentLossPanelData();
    private AARPromotionPanelData promotionPanelData = new AARPromotionPanelData();
    private TransferPanelData transferPanelData = new TransferPanelData();
    private VictoryEvents victoryPanelData = new VictoryEvents();

    public void addNewsPanelData(AARNewsPanelData newsPanelData)
    {
        this.newsPanelData.merge(newsPanelData);
    }

    public void addAceLeavePanelData(AARAceLeavePanelData aceLeavePanelData)
    {
        this.aceLeavePanelData.getAcesOnLeaveDuringElapsedTime().addAll(aceLeavePanelData.getAcesOnLeaveDuringElapsedTime());
    }

    public void setMedalPanelData(AARMedalPanelData medalPanelData)
    {
        this.medalPanelData.merge(medalPanelData);
    }

    public void addEquipmentLossPanelData(AAREquipmentLossPanelData equipmentLossPanelData)
    {
        this.equipmentLossPanelData.merge(equipmentLossPanelData);
    }

    public void addPilotLossPanelData(AARPilotLossPanelData pilotLossPanelData)
    {
        this.pilotLossPanelData.merge(pilotLossPanelData);
    }

    public void addPromotionPanelData(AARPromotionPanelData promotionPanelData)
    {
        this.promotionPanelData.merge(promotionPanelData);
    }

    public void addTransferPanelData(TransferPanelData transferPanelData)
    {
        this.transferPanelData.merge(transferPanelData);
    }

    public void addVictoryPanelData(VictoryEvents victoryEvents)
    {
        this.victoryPanelData.merge(victoryEvents);
    }

    public AARNewsPanelData getNewsPanelData()
    {
        return newsPanelData;
    }

    public AARAceLeavePanelData getAceLeavePanelData()
    {
        return aceLeavePanelData;
    }

    public AARMedalPanelData getMedalPanelData()
    {
        return medalPanelData;
    }

    public AARPilotLossPanelData getPilotLossPanelData()
    {
        return pilotLossPanelData;
    }

    public AAREquipmentLossPanelData getEquipmentLossPanelData()
    {
        return equipmentLossPanelData;
    }

    public AARPromotionPanelData getPromotionPanelData()
    {
        return promotionPanelData;
    }

    public TransferPanelData getTransferPanelData()
    {
        return transferPanelData;
    }

    public VictoryEvents getOutOfMissionVictoryPanelData()
    {
        return victoryPanelData;
    }

    public void merge(Campaign campaign, UIDebriefData uiDebriefDataForIteration) throws PWCGException
    {
        newsPanelData.merge(uiDebriefDataForIteration.getNewsPanelData());
        medalPanelData.merge(uiDebriefDataForIteration.getMedalPanelData());
        pilotLossPanelData.merge(uiDebriefDataForIteration.getPilotLossPanelData());
        equipmentLossPanelData.merge(uiDebriefDataForIteration.getEquipmentLossPanelData());
        promotionPanelData.merge(uiDebriefDataForIteration.getPromotionPanelData());
        transferPanelData.merge(uiDebriefDataForIteration.getTransferPanelData());
        victoryPanelData.merge(uiDebriefDataForIteration.getOutOfMissionVictoryPanelData());
    }
}
