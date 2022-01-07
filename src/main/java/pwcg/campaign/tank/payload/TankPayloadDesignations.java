package pwcg.campaign.tank.payload;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import pwcg.core.exception.PWCGException;

public class TankPayloadDesignations
{
    private Map<Integer, TankPayloadDesignation> payloadDesignations = new TreeMap<>();

	public TankPayloadDesignations()
	{
	}

    public TankPayloadDesignation getPayloadDesignation(int payloadId) throws PWCGException
    {
        if (!payloadDesignations.containsKey(payloadId))
        {
            throw new PWCGException("Invalid payload for plane: " + payloadId);
        }
        
        return payloadDesignations.get(payloadId);
    }

    public TankPayloadDesignations copy()
    {
        TankPayloadDesignations target  = new TankPayloadDesignations();
        target.payloadDesignations = this.payloadDesignations;
        return target;
    }

    public void addPayload(int payloadId, String modMask, TankPayloadElement ... requestedPayloadElements)
    {
        TankPayloadDesignation payloadDesignation = new TankPayloadDesignation();
        
    	List<TankPayloadElement> payloadElements = new ArrayList<>();
    	for (TankPayloadElement requestedPayloadElement : requestedPayloadElements)
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
        for (TankPayloadDesignation payloadDesignation : payloadDesignations.values())
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
        for (TankPayloadDesignation payloadDesignation : payloadDesignations.values())
        {
            if (payloadDesignation.getPayloadDescription().equals(payloadDescription))
            {
                return payloadDesignation.getModMask();
            }
        }

        return "";
    }

    public List<TankPayloadDesignation> getAllAvailablePayloadDesignations()
    {
        return new ArrayList<TankPayloadDesignation>(payloadDesignations.values());
    }

    public List<TankPayloadDesignation> getAvailablePayloadDesignations(List<Integer> availablePayloadIds)
    {
        List<TankPayloadDesignation> availablePayloads = new ArrayList<>();
        for (TankPayloadDesignation payloadDesignation : payloadDesignations.values())
        {
            if (availablePayloadIds.contains(payloadDesignation.getPayloadId()))
            {
                availablePayloads.add(payloadDesignation);
            }
        }
        
        return availablePayloads;
    }
}
