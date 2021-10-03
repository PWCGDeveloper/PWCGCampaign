package pwcg.campaign.plane.payload;

import java.util.Date;
import java.util.List;

import pwcg.campaign.plane.PlaneType;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.MathUtils;
import pwcg.mission.flight.IFlight;

public abstract class PlanePayload implements IPlanePayload
{
    private PlanePayloads payload = new PlanePayloads();
    private PlaneModifications modifications;
    private PlaneType planeType;
    private Date date;

	public PlanePayload(PlaneType planeType, Date date)
	{
	    this.planeType = planeType;
        this.date = date;

        modifications = new PlaneModifications(planeType);
        
	    initialize();
	    createStandardWeaponsPayload();
        createWeaponsModAvailabilityDates();
        loadAvailableStockModifications();
	}

	@Override()
	public int createWeaponsPayload(IFlight flight) throws PWCGException
	{
	    int selectedPayloadId = createWeaponsPayloadForPlane(flight);
        setSelectedPayload(selectedPayloadId);
        return selectedPayloadId;
	}
	
    @Override
    public List<PayloadDesignation> getAvailablePayloadDesignations(IFlight flight) throws PWCGException
    {
        return getAvailablePayloadDesignationsForPlane(flight);
    }

    @Override
    public PayloadDesignation getSelectedPayloadDesignation() throws PWCGException
    {
        return payload.getSelectedPayloadDesignation();
    }

    protected List<PayloadDesignation> getAvailablePayloadDesignationsForPlane(IFlight flight) throws PWCGException
    {
        return payload.getPayloadDesignations().getAllAvailablePayloadDesignations();
    }

    protected List<PayloadDesignation> getAvailablePayloadDesignations(List<Integer> availablePayloadIds)
    {
        return payload.getPayloadDesignations().getAvailablePayloadDesignations(availablePayloadIds);
    }

    protected IPlanePayload copy(PlanePayload target)
    {
        target.payload = this.payload.copy();
        target.modifications = this.modifications.copy();
        target.planeType = this.planeType;        
        target.date = this.date;        
        return target;
    }

    protected int getSelectedPayload()
    {
        return payload.getSelectedPayloadId();
    }

    protected boolean isSelectedPayload(int payloadId)
    {
        return payload.isSelectedPayload(payloadId);
    }

    protected void setSelectedPayload(int payloadId)
    {
        payload.setSelectedPayloadId(payloadId);
    }

    protected void createWeaponsModAvailabilityDates()
    {
    }

    protected void setNoOrdnancePayloadId(int noOrdnancePayloadId)
    {
        payload.setNoOrdnancePayloadId(noOrdnancePayloadId);
    }

    protected void registerStockModification(PayloadElement payloadElement)
    {
        modifications.registerStockModification(payloadElement);
    }

    protected void loadAvailableStockModifications()
    {
        for(PayloadElement modification : planeType.getStockModifications())
        {
            modifications.registerStockModification(modification);
        }
        
        modifications.addStockModifications();
    }
    
    @Override
    public int getPayloadIdByDescription(String payloadDescription)
    {
        return payload.getPayloadDesignations().getPayloadIdByDescription(payloadDescription);
    }
    
    @Override
    public String getPayloadMaskByDescription(String payloadDescription)
    {
        return payload.getPayloadDesignations().getPayloadMaskByDescription(payloadDescription);
    }

    @Override
    public void createStandardWeaponsPayload()
    {
        payload.createStandardWeaponsPayload(0);
    }

    @Override
    public int getSelectedPayloadId() 
    {
        return payload.getSelectedPayloadId();
    }
    
    @Override
    public void setSelectedPayloadId(int selectedPrimaryPayloadId) 
    {
        payload.setSelectedPayloadId(selectedPrimaryPayloadId);
    }
    
    @Override
    public void selectNoOrdnancePayload()
    {
        payload.selectNoOrdnancePayload();
    }    

    @Override
    public void selectModification(PayloadElement payloadElement)
    {
        modifications.selectModification(payloadElement);
    }

    @Override
    public List<PayloadElement> getSelectedModifications() throws PWCGException
    {
        return modifications.getSelectedModificationElements();
    }
    
    @Override
    public void clearModifications()
    {
        modifications.clearModifications();
    }
    
    @Override
    public List<PayloadDesignation> getOptionalPayloadModifications()
    {
        return modifications.getOptionalPayloadModifications();
    }

    @Override
    public String generateFullModificationMask() throws PWCGException
    {
        String fullModificationMask = payload.getSelectedPayloadDesignation().getModMask();
        
        for (PayloadDesignation modificationDesignation : modifications.getSelectedModifications())
        {            
            String additionalModificationsMask = modificationDesignation.getModMask();
            int additionalModMaskValue = Integer.parseInt(additionalModificationsMask, 2);
            int fullModificationMaskValue = Integer.parseInt(fullModificationMask, 2);
            int newModmaskValue = fullModificationMaskValue + additionalModMaskValue;
            fullModificationMask = MathUtils.numberToBinaryForm(newModmaskValue);            
        }
        
        return fullModificationMask;
    }

    protected void setAvailablePayload(int payloadId, String modMask, PayloadElement ... requestedPayloadElements)
    {
        payload.setAvailablePayload(payloadId, modMask, requestedPayloadElements);
    }

    protected PlaneType getPlaneType()
    {
        return planeType;
    }

    protected Date getDate()
    {
        return date;
    }

    protected boolean isOrdnanceDroppedPayload()
    {
        return payload.isOrdnanceDroppedPayload();
    }


    protected abstract int createWeaponsPayloadForPlane(IFlight flight) throws PWCGException;
    protected abstract void initialize();
}
