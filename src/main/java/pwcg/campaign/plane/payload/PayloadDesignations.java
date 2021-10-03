package pwcg.campaign.plane.payload;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import pwcg.core.exception.PWCGException;

public class PayloadDesignations
{
    private Map<Integer, PayloadDesignation> payloadDesignations = new TreeMap<>();

	public PayloadDesignations()
	{
	}

    public PayloadDesignation getPayloadDesignation(int payloadId) throws PWCGException
    {
        if (!payloadDesignations.containsKey(payloadId))
        {
            throw new PWCGException("Invalid payload for plane: " + payloadId);
        }
        
        return payloadDesignations.get(payloadId);
    }

    public PayloadDesignations copy()
    {
        PayloadDesignations target  = new PayloadDesignations();
        target.payloadDesignations = this.payloadDesignations;
        return target;
    }

    public void addPayload(int payloadId, String modMask, PayloadElement ... requestedPayloadElements)
    {
        PayloadDesignation payloadDesignation = new PayloadDesignation();
        
    	List<PayloadElement> payloadElements = new ArrayList<>();
    	for (PayloadElement requestedPayloadElement : requestedPayloadElements)
    	{
    		payloadElements.add(requestedPayloadElement);
    	}
    	
    	payloadDesignation.setPayloadId(payloadId);
    	payloadDesignation.setModMask(modMask);
    	payloadDesignation.setPayloadElements(payloadElements);
    	
    	payloadDesignations.put(payloadDesignation.getPayloadId(), payloadDesignation);
    }

    public int getPayloadIdByDescription(String payloadDescription)
    {
        for (PayloadDesignation payloadDesignation : payloadDesignations.values())
        {
            if (payloadDesignation.getPayloadDescription().equals(payloadDescription))
            {
                return payloadDesignation.getPayloadId();
            }
        }
        
        return 0;
    }

    public String getPayloadMaskByDescription(String payloadDescription)
    {
        for (PayloadDesignation payloadDesignation : payloadDesignations.values())
        {
            if (payloadDesignation.getPayloadDescription().equals(payloadDescription))
            {
                return payloadDesignation.getModMask();
            }
        }

        return "";
    }

    public List<PayloadDesignation> getAllAvailablePayloadDesignations()
    {
        return new ArrayList<PayloadDesignation>(payloadDesignations.values());
    }

    public List<PayloadDesignation> getAvailablePayloadDesignations(List<Integer> availablePayloadIds)
    {
        List<PayloadDesignation> availablePayloads = new ArrayList<>();
        for (PayloadDesignation payloadDesignation : payloadDesignations.values())
        {
            if (availablePayloadIds.contains(payloadDesignation.getPayloadId()))
            {
                availablePayloads.add(payloadDesignation);
            }
        }
        
        return availablePayloads;
    }
}
