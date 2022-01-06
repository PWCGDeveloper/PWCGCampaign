package pwcg.campaign.plane.payload;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import pwcg.core.exception.PWCGException;

public class PlanePayloadDesignations
{
    private Map<Integer, PlanePayloadDesignation> payloadDesignations = new TreeMap<>();

	public PlanePayloadDesignations()
	{
	}

    public PlanePayloadDesignation getPayloadDesignation(int payloadId) throws PWCGException
    {
        if (!payloadDesignations.containsKey(payloadId))
        {
            throw new PWCGException("Invalid payload for plane: " + payloadId);
        }
        
        return payloadDesignations.get(payloadId);
    }

    public PlanePayloadDesignations copy()
    {
        PlanePayloadDesignations target  = new PlanePayloadDesignations();
        target.payloadDesignations = this.payloadDesignations;
        return target;
    }

    public void addPayload(int payloadId, String modMask, PlanePayloadElement ... requestedPayloadElements)
    {
        PlanePayloadDesignation payloadDesignation = new PlanePayloadDesignation();
        
    	List<PlanePayloadElement> payloadElements = new ArrayList<>();
    	for (PlanePayloadElement requestedPayloadElement : requestedPayloadElements)
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
        for (PlanePayloadDesignation payloadDesignation : payloadDesignations.values())
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
        for (PlanePayloadDesignation payloadDesignation : payloadDesignations.values())
        {
            if (payloadDesignation.getPayloadDescription().equals(payloadDescription))
            {
                return payloadDesignation.getModMask();
            }
        }

        return "";
    }

    public List<PlanePayloadDesignation> getAllAvailablePayloadDesignations()
    {
        return new ArrayList<PlanePayloadDesignation>(payloadDesignations.values());
    }

    public List<PlanePayloadDesignation> getAvailablePayloadDesignations(List<Integer> availablePayloadIds)
    {
        List<PlanePayloadDesignation> availablePayloads = new ArrayList<>();
        for (PlanePayloadDesignation payloadDesignation : payloadDesignations.values())
        {
            if (availablePayloadIds.contains(payloadDesignation.getPayloadId()))
            {
                availablePayloads.add(payloadDesignation);
            }
        }
        
        return availablePayloads;
    }
}
