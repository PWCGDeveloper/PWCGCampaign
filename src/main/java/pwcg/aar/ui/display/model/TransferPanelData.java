package pwcg.aar.ui.display.model;

import java.util.ArrayList;
import java.util.List;

import pwcg.aar.ui.events.model.TransferEvent;

public class TransferPanelData
{
    private TransferEvent playerTransferEvent = null;
    private List<TransferEvent> transfers = new ArrayList<>();
    
    public TransferEvent getPlayerTransferEvent()
    {
        return playerTransferEvent;
    }
    
    public void setPlayerTransferEvent(TransferEvent playerTransferEvent)
    {
        this.playerTransferEvent = playerTransferEvent;
    }

    public List<TransferEvent> getTransfers()
    {
        return transfers;
    }

    public void setTransfers(List<TransferEvent> transfers)
    {
        this.transfers = transfers;
    }
}
