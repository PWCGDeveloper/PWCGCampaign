package pwcg.campaign.plane.payload;

import pwcg.core.exception.PWCGException;

public class PlanePayloads
{
    private int selectedPrimaryPayloadId = 0;
    private PayloadDesignations availablePayload = new PayloadDesignations();
    private int noOrdnancePayloadId = 0;

	public PlanePayloads()
	{
	}

    public PlanePayloads copy()
    {
        PlanePayloads target = new PlanePayloads();
        target.availablePayload = this.availablePayload.copy();
        target.selectedPrimaryPayloadId = this.selectedPrimaryPayloadId;
        target.noOrdnancePayloadId = this.noOrdnancePayloadId;        
        return target;
    }

    public void setAvailablePayload(int payloadId, String modMask, PayloadElement ... requestedPayloadElements)
    {
        if (payloadId >= 0)
        {
            availablePayload.setAvailablePayload(payloadId, modMask, requestedPayloadElements);
        }
    }

    public PayloadDesignations getPayloadDesignations()
    {
        return availablePayload;
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
        return availablePayload.getPayloadDesignation(selectedPrimaryPayloadId);
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
