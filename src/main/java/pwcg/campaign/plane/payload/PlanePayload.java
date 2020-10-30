package pwcg.campaign.plane.payload;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import pwcg.campaign.plane.PlaneType;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.MathUtils;
import pwcg.mission.flight.IFlight;

public abstract class PlanePayload implements IPlanePayload
{
	protected int selectedPrimaryPayloadId = 0;
	protected Map<String, PayloadElement>modifications = new TreeMap<>();
    protected Map<Integer, PayloadDesignation> availablePayload = new TreeMap<>();
    protected PlaneType planeType;
    protected int noOrdnancePayloadElement = 0;

	public PlanePayload(PlaneType planeType)
	{
	    this.planeType = planeType;
	    initialize();
	}
	
    public PayloadDesignation getSelectedPayloadDesignation() throws PWCGException
    {
        if (!availablePayload.containsKey(selectedPrimaryPayloadId))
        {
            throw new PWCGException("Invalid payload for plane: " + selectedPrimaryPayloadId);
        }
        
        return availablePayload.get(selectedPrimaryPayloadId);
    }


    public IPlanePayload copy(PlanePayload target)
    {
        target.selectedPrimaryPayloadId = this.selectedPrimaryPayloadId;
        target.modifications = this.modifications;
        target.availablePayload = this.availablePayload;
        target.planeType = this.planeType;
        
        return target;
    }

    public void setAvailablePayload(int payloadId, String modMask, PayloadElement ... requestedPayloadElements)
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
    	
    	availablePayload.put(payloadDesignation.getPayloadId(), payloadDesignation);
    }
    
    public int getSelectedPayloadId() 
    {
        return selectedPrimaryPayloadId;
    }
    
    public void setSelectedPayloadId(int selectedPrimaryPayloadId) 
    {
        this.selectedPrimaryPayloadId = selectedPrimaryPayloadId;
    }

    public void addModification(PayloadElement payloadElement)
    {
    	modifications.put(payloadElement.getDescription(), payloadElement);
    }

    public void clearModifications()
    {
    	modifications.clear();
    }

    public List<PayloadElement> getModifications()
    {
    	return new ArrayList<PayloadElement>(modifications.values());
    }

    public String generateFullModificationMask()
    {
        PayloadDesignation payloadDesignation = availablePayload.get(selectedPrimaryPayloadId);
        String fullModificationMask = payloadDesignation.getModMask();
        
        for (PayloadElement modification : modifications.values())
        {
            String additionalModificationsMask = this.getPayloadMaskByDescription(modification.getDescription());
            int additionalModMaskValue = Integer.parseInt(additionalModificationsMask, 2);
            int fullModificationMaskValue = Integer.parseInt(fullModificationMask, 2);
            int newModmaskValue = fullModificationMaskValue + additionalModMaskValue;
            fullModificationMask = MathUtils.numberToBinaryForm(newModmaskValue);            
        }
        
        return fullModificationMask;
    }
    
    public List<PayloadDesignation> getPayloadDesignations()
    {
        return new ArrayList<PayloadDesignation>(availablePayload.values());
    }

    public int getPayloadIdByDescription(String payloadDescription)
    {
        for (PayloadDesignation payloadDesignation : availablePayload.values())
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
        for (PayloadDesignation payloadDesignation : availablePayload.values())
        {
            if (payloadDesignation.getPayloadDescription().equals(payloadDescription))
            {
                return payloadDesignation.getModMask();
            }
        }
        
        return "";
    }
    
    public int createStandardWeaponsPayload()
    {
        selectedPrimaryPayloadId = 0;
        return selectedPrimaryPayloadId;
    }
    
    
    @Override
    public void noOrdnance()
    {
        selectedPrimaryPayloadId = noOrdnancePayloadElement ;
    }


    abstract public int createWeaponsPayload(IFlight flight) throws PWCGException;
    abstract protected void initialize();
}
