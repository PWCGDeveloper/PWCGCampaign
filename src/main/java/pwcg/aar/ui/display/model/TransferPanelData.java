package pwcg.aar.ui.display.model;

import java.util.ArrayList;
import java.util.List;

import pwcg.aar.ui.events.model.TransferEvent;

public class TransferPanelData
{
    private TransferEvent playerTransferEvent = null;
    private List<TransferEvent> transferIntoSquadron = new ArrayList<>();
    private List<TransferEvent> transferOutOfSquadron = new ArrayList<>();
    
    public TransferEvent getPlayerTransferEvent()
    {
        return playerTransferEvent;
    }
    
    public void setPlayerTransferEvent(TransferEvent playerTransferEvent)
    {
        this.playerTransferEvent = playerTransferEvent;
    }
    
    public List<TransferEvent> getTransferIntoSquadron()
    {
        return transferIntoSquadron;
    }
    
    public void setTransferIntoSquadron(List<TransferEvent> transferIntoSquadron)
    {
        this.transferIntoSquadron = transferIntoSquadron;
    }
    
    public List<TransferEvent> getTransferOutOfSquadron()
    {
        return transferOutOfSquadron;
    }
    
    public void setTransferOutOfSquadron(List<TransferEvent> transferOutOfSquadron)
    {
        this.transferOutOfSquadron = transferOutOfSquadron;
    }

    
}
