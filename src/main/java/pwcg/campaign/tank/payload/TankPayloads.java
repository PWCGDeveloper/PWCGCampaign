package pwcg.campaign.tank.payload;

import pwcg.core.exception.PWCGException;

public class TankPayloads
{
    private int selectedPrimaryPayloadId = 0;
    private TankPayloadDesignations payloads = new TankPayloadDesignations();
    private int noOrdnancePayloadId = 0;

	public TankPayloads()
	{
	}

    public TankPayloads copy()
    {
        TankPayloads target = new TankPayloads();
        target.payloads = this.payloads.copy();
        target.selectedPrimaryPayloadId = this.selectedPrimaryPayloadId;
        target.noOrdnancePayloadId = this.noOrdnancePayloadId;        
        return target;
    }

    public void addPayload(int payloadId, String modMask, TankPayloadElement ... requestedPayloadElements)
    {
        payloads.addPayload(payloadId, modMask, requestedPayloadElements);
    }

    public TankPayloadDesignations getPayloadDesignations()
    {
        return payloads;
    }

    public void setSelectedPayloadId(int payloadId)
    {
        selectedPrimaryPayloadId = payloadId;
    }

    public int getSelectedPayloadId()
    {
        return selectedPrimaryPayloadId;
    }

    public boolean isSelectedPayload(int payloadId)
    {
        return (payloadId == selectedPrimaryPayloadId);
    }

    public TankPayloadDesignation getSelectedPayloadDesignation() throws PWCGException
    {
        return payloads.getPayloadDesignation(selectedPrimaryPayloadId);
    }

    public void createStandardWeaponsPayload(int standardPayloadId)
    {
        selectedPrimaryPayloadId = standardPayloadId;
    }

    public void setNoOrdnancePayloadId(int noOrdnancePayloadId)
    {
        this.noOrdnancePayloadId = noOrdnancePayloadId;
    }

    public void selectNoOrdnancePayload()
    {
        selectedPrimaryPayloadId = noOrdnancePayloadId;
    }    

    public boolean isOrdnanceDroppedPayload()
    {
        return (selectedPrimaryPayloadId == noOrdnancePayloadId);
    }    
}
