package pwcg.aar.outofmission.phase2.resupply;

import pwcg.aar.data.AARContext;
import pwcg.campaign.Campaign;
import pwcg.core.exception.PWCGException;

public class AARResupplyCoordinator
{
    private Campaign campaign;
    private AARContext aarContext;
    private AARResupplyData resupplyData = new AARResupplyData();

    public AARResupplyCoordinator(Campaign campaign, AARContext aarContext)
    {
        this.campaign = campaign;
        this.aarContext = aarContext;
    }
    
    
    public AARResupplyData handleResupply() throws PWCGException 
    {
        handleAceTransfers();
        squadronTransfers();
        equipmentResupply();
        return resupplyData;
    }

    private void handleAceTransfers() throws PWCGException
    {
        HistoricalAceTransferHandler aceTransferHandler = new HistoricalAceTransferHandler(campaign, aarContext.getNewDate());
        SquadronTransferData acesTransferred =  aceTransferHandler.determineAceTransfers();
        resupplyData.setAcesTransferred(acesTransferred);
    }
    
    private void squadronTransfers() throws PWCGException
    {
        ResupplyNeedBuilder transferNeedBuilder = new ResupplyNeedBuilder(campaign);
        TransferHandler squadronTransferHandler = new TransferHandler(campaign, transferNeedBuilder);
        SquadronTransferData squadronTransferData = squadronTransferHandler.determineSquadronMemberTransfers();
        resupplyData.setSquadronTransferData(squadronTransferData);
    }
    
    private void equipmentResupply() throws PWCGException
    {
        ResupplyNeedBuilder transferNeedBuilder = new ResupplyNeedBuilder(campaign);
        EquipmentReplacementHandler equipmentResupplyHandler = new EquipmentReplacementHandler(campaign, transferNeedBuilder);
        EquipmentResupplyData equipmentResupplyData = equipmentResupplyHandler.determineEquipmentResupply();
        resupplyData.setEquipmentResupplyData(equipmentResupplyData);
    }
}
