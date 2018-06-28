package pwcg.aar.outofmission.phase2.transfer;

import pwcg.aar.data.AARContext;
import pwcg.campaign.Campaign;
import pwcg.core.exception.PWCGException;

public class AARPhaseOutOfMissionPhase2TransferCoordinator
{
    private Campaign campaign;
    private AARContext aarContext;
    private AARTransferData transferData = new AARTransferData();

    public AARPhaseOutOfMissionPhase2TransferCoordinator(Campaign campaign, AARContext aarContext)
    {
        this.campaign = campaign;
        this.aarContext = aarContext;
    }
    
    
    public AARTransferData handleTransfers() throws PWCGException 
    {
        handleAceTransfers();
        squadronTransfers();
        return transferData;
    }
    
    private void handleAceTransfers() throws PWCGException
    {
        HistoricalAceTransferHandler aceTransferHandler = new HistoricalAceTransferHandler(campaign, aarContext.getNewDate());
        SquadronTransferData acesTransferred =  aceTransferHandler.determineAceTransfers();
        transferData.setAcesTransferred(acesTransferred);
    }
    
    private void squadronTransfers() throws PWCGException
    {
        TransferNeedBuilder transferNeedBuilder = new TransferNeedBuilder(campaign);
        
        TransferHandler squadronTransferHandler = new TransferHandler(campaign, transferNeedBuilder);
        SquadronTransferData squadronTransferData = squadronTransferHandler.determineSquadronMemberTransfers();
        
        transferData.setSquadronTransferData(squadronTransferData);
    }
}
