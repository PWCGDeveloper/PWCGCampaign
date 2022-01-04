package pwcg.campaign.plane.payload;

import java.util.Date;
import java.util.List;

import pwcg.campaign.tank.TankType;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.MathUtils;
import pwcg.mission.flight.IFlight;

public abstract class PlanePayload implements IPlanePayload
{
    private PlanePayloads payloads = new PlanePayloads();
    private PlaneModifications modifications;
    private TankType planeType;
    private Date date;

	public PlanePayload(TankType planeType, Date date)
	{
	    this.planeType = planeType;
        this.date = date;

        modifications = new PlaneModifications(planeType);
        
	    initialize();
	    createStandardWeaponsPayload();
        createWeaponsModAvailabilityDates();
        loadAvailableStockModifications();
        addStockModifications();
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
        return payloads.getSelectedPayloadDesignation();
    }

    protected List<PayloadDesignation> getAvailablePayloadDesignationsForPlane(IFlight flight) throws PWCGException
    {
        return payloads.getPayloadDesignations().getAllAvailablePayloadDesignations();
    }

    protected List<PayloadDesignation> getAvailablePayloadDesignations(List<Integer> availablePayloadIds)
    {
        return payloads.getPayloadDesignations().getAvailablePayloadDesignations(availablePayloadIds);
    }

    protected IPlanePayload copy(PlanePayload target)
    {
        target.payloads = this.payloads.copy();
        target.modifications = this.modifications.copy();
        target.planeType = this.planeType;        
        target.date = this.date;        
        return target;
    }

    protected int getSelectedPayload()
    {
        return payloads.getSelectedPayloadId();
    }

    protected boolean isSelectedPayload(int payloadId)
    {
        return payloads.isSelectedPayload(payloadId);
    }

    protected void setSelectedPayload(int payloadId)
    {
        payloads.setSelectedPayloadId(payloadId);
    }

    protected void createWeaponsModAvailabilityDates()
    {
    }

    protected void setNoOrdnancePayloadId(int noOrdnancePayloadId)
    {
        payloads.setNoOrdnancePayloadId(noOrdnancePayloadId);
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
    

    private void addStockModifications()
    {
        modifications.addStockModifications();        
    }

    @Override
    public int getPayloadIdByDescription(String payloadDescription)
    {
        return payloads.getPayloadDesignations().getPayloadIdByDescription(payloadDescription);
    }
    
    @Override
    public String getPayloadMaskByDescription(String payloadDescription)
    {
        return payloads.getPayloadDesignations().getPayloadMaskByDescription(payloadDescription);
    }

    @Override
    public void createStandardWeaponsPayload()
    {
        payloads.createStandardWeaponsPayload(0);
    }

    @Override
    public int getSelectedPayloadId() 
    {
        return payloads.getSelectedPayloadId();
    }
    
    @Override
    public void setSelectedPayloadId(int selectedPrimaryPayloadId) 
    {
        payloads.setSelectedPayloadId(selectedPrimaryPayloadId);
    }
    
    @Override
    public void selectNoOrdnancePayload()
    {
        payloads.selectNoOrdnancePayload();
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
        String fullModificationMask = payloads.getSelectedPayloadDesignation().getModMask();
        
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

    @Override
    public PlanePayloads getPayloads()
    {
        return payloads;
    }

    @Override
    public PlaneModifications getModifications()
    {
        return modifications;
    }

    protected void setAvailablePayload(int payloadId, String modMask, PayloadElement ... requestedPayloadElements)
    {
        if (payloadId >= 0)
        {
            payloads.addPayload(payloadId, modMask, requestedPayloadElements);
        }
        else
        {
            modifications.addModification(payloadId, modMask, requestedPayloadElements);
        }
    }

    protected TankType getTankType()
    {
        return planeType;
    }

    protected Date getDate()
    {
        return date;
    }

    protected boolean isOrdnanceDroppedPayload()
    {
        return payloads.isOrdnanceDroppedPayload();
    }


    protected abstract int createWeaponsPayloadForPlane(IFlight flight) throws PWCGException;
    protected abstract void initialize();
}
