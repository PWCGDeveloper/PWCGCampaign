package pwcg.aar.data;

import pwcg.aar.ui.display.model.AARAceLeavePanelData;
import pwcg.aar.ui.display.model.AARMedalPanelData;
import pwcg.aar.ui.display.model.AARNewsPanelData;
import pwcg.aar.ui.display.model.AARPilotLossPanelData;
import pwcg.aar.ui.display.model.AARPromotionPanelData;
import pwcg.aar.ui.display.model.TransferPanelData;

public class UIDebriefData
{
    private AARAceLeavePanelData aceLeavePanelData = new AARAceLeavePanelData();
    private AARMedalPanelData medalPanelData = new AARMedalPanelData();
    private AARNewsPanelData newsPanelData = new AARNewsPanelData();
    private AARPilotLossPanelData pilotLossPanelData = new AARPilotLossPanelData();
    private AARPromotionPanelData promotionPanelData = new AARPromotionPanelData();
    private TransferPanelData transferPanelData = new TransferPanelData();

    public void merge(UIDebriefData source)
    {
        aceLeavePanelData.getAcesOnLeaveDuringElapsedTime().addAll(source.getAceLeavePanelData().getAcesOnLeaveDuringElapsedTime());
        medalPanelData.getMedalsAwarded().addAll(source.getMedalPanelData().getMedalsAwarded());
        newsPanelData.getAcesKilledDuringElapsedTime().addAll(source.getNewsPanelData().getAcesKilledDuringElapsedTime());
        newsPanelData.getNewspaperEventsDuringElapsedTime().addAll(source.getNewsPanelData().getNewspaperEventsDuringElapsedTime());
        pilotLossPanelData.getSquadMembersLost().putAll(source.getPilotLossPanelData().getSquadMembersLost());
        promotionPanelData.getPromotionEventsDuringElapsedTime().addAll(source.getPromotionPanelData().getPromotionEventsDuringElapsedTime());
        transferPanelData.getTransferIntoSquadron().addAll(source.getTransferPanelData().getTransferIntoSquadron());
        transferPanelData.getTransferOutOfSquadron().addAll(source.getTransferPanelData().getTransferOutOfSquadron());
    }
    
    public AARAceLeavePanelData getAceLeavePanelData()
    {
        return aceLeavePanelData;
    }

    public void addAceLeavePanelData(AARAceLeavePanelData aceLeavePanelData)
    {
    	this.aceLeavePanelData.getAcesOnLeaveDuringElapsedTime().addAll(aceLeavePanelData.getAcesOnLeaveDuringElapsedTime());
    }

    public AARMedalPanelData getMedalPanelData()
    {
        return medalPanelData;
    }

    public void setMedalPanelData(AARMedalPanelData medalPanelData)
    {
        this.medalPanelData = medalPanelData;
    }

    public AARNewsPanelData getNewsPanelData()
    {
        return newsPanelData;
    }

    public void setNewsPanelData(AARNewsPanelData newsPanelData)
    {
        this.newsPanelData = newsPanelData;
    }

    public AARPilotLossPanelData getPilotLossPanelData()
    {
        return pilotLossPanelData;
    }

    public void setPilotLossPanelData(AARPilotLossPanelData pilotLossPanelData)
    {
        this.pilotLossPanelData = pilotLossPanelData;
    }
    
    public AARPromotionPanelData getPromotionPanelData()
    {
        return promotionPanelData;
    }

    public void setPromotionPanelData(AARPromotionPanelData promotionPanelData)
    {
        this.promotionPanelData = promotionPanelData;
    }

    public TransferPanelData getTransferPanelData()
    {
        return transferPanelData;
    }

    public void setTransferPanelData(TransferPanelData transferPanelData)
    {
        this.transferPanelData = transferPanelData;
    }
}
