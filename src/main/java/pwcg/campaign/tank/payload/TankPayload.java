package pwcg.campaign.tank.payload;

import java.util.Date;
import java.util.List;

import pwcg.campaign.tank.TankType;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.MathUtils;
import pwcg.mission.playerunit.PlayerUnit;

public abstract class TankPayload implements ITankPayload
{
    private TankPayloads payloads = new TankPayloads();
    private TankModifications modifications;
    private TankType tankType;
    private Date date;

	public TankPayload(TankType tankType, Date date)
	{
	    this.tankType = tankType;
        this.date = date;

        modifications = new TankModifications(tankType);
        
	    initialize();
	    createStandardWeaponsPayload();
        createWeaponsModAvailabilityDates();
        loadAvailableStockModifications();
        addStockModifications();
	}

    @Override()
	public int createWeaponsPayload(PlayerUnit unit) throws PWCGException
	{
	    int selectedPayloadId = createWeaponsPayloadForTank(unit);
        setSelectedPayload(selectedPayloadId);
        return selectedPayloadId;
	}
	
    @Override
    public List<TankPayloadDesignation> getAvailablePayloadDesignations(PlayerUnit unit) throws PWCGException
    {
        return getAvailablePayloadDesignationsForTank(unit);
    }

    @Override
    public TankPayloadDesignation getSelectedPayloadDesignation() throws PWCGException
    {
        return payloads.getSelectedPayloadDesignation();
    }

    protected List<TankPayloadDesignation> getAvailablePayloadDesignationsForTank(PlayerUnit unit) throws PWCGException
    {
        return payloads.getPayloadDesignations().getAllAvailablePayloadDesignations();
    }

    protected List<TankPayloadDesignation> getAvailablePayloadDesignations(List<Integer> availablePayloadIds)
    {
        return payloads.getPayloadDesignations().getAvailablePayloadDesignations(availablePayloadIds);
    }

    protected ITankPayload copy(TankPayload target)
    {
        target.payloads = this.payloads.copy();
        target.modifications = this.modifications.copy();
        target.tankType = this.tankType;        
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

    protected void registerStockModification(TankPayloadElement payloadElement)
    {
        modifications.registerStockModification(payloadElement);
    }

    protected void loadAvailableStockModifications()
    {
        for(TankPayloadElement modification : tankType.getStockModifications())
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
    public void selectModification(TankPayloadElement payloadElement)
    {
        modifications.selectModification(payloadElement);
    }

    @Override
    public List<TankPayloadElement> getSelectedModifications() throws PWCGException
    {
        return modifications.getSelectedModificationElements();
    }
    
    @Override
    public void clearModifications()
    {
        modifications.clearModifications();
    }
    
    @Override
    public List<TankPayloadDesignation> getOptionalPayloadModifications()
    {
        return modifications.getOptionalPayloadModifications();
    }

    @Override
    public String generateFullModificationMask() throws PWCGException
    {
        String fullModificationMask = payloads.getSelectedPayloadDesignation().getModMask();
        
        for (TankPayloadDesignation modificationDesignation : modifications.getSelectedModifications())
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
    public TankPayloads getPayloads()
    {
        return payloads;
    }

    @Override
    public TankModifications getModifications()
    {
        return modifications;
    }

    protected void setAvailablePayload(int payloadId, String modMask, TankPayloadElement ... requestedPayloadElements)
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
        return tankType;
    }

    protected Date getDate()
    {
        return date;
    }

    protected abstract int createWeaponsPayloadForTank(PlayerUnit unit) throws PWCGException;
    protected abstract void initialize();
}
