package pwcg.campaign.plane.payload;

import pwcg.core.exception.PWCGException;

public class PlanePayloads
{
    private int selectedPrimaryPayloadId = 0;
    private PayloadDesignations payloads = new PayloadDesignations();
    private int noOrdnancePayloadId = 0;

	public PlanePayloads()
	{
	}

    public PlanePayloads copy()
    {
        PlanePayloads target = new PlanePayloads();
        target.payloads = this.payloads.copy();
        target.selectedPrimaryPayloadId = this.selectedPrimaryPayloadId;
        target.noOrdnancePayloadId = this.noOrdnancePayloadId;        
        return target;
    }

    public void addPayload(int payloadId, String modMask, PayloadElement ... requestedPayloadElements)
    {
        payloads.addPayload(payloadId, modMask, requestedPayloadElements);
    }

    public PayloadDesignations getPayloadDesignations()
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

    public PayloadDesignation getSelectedPayloadDesignation() throws PWCGException
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
