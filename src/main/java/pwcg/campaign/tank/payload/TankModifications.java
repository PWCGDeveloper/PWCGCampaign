package pwcg.campaign.tank.payload;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import pwcg.campaign.tank.TankType;
import pwcg.core.exception.PWCGException;

public class TankModifications
{
    private TankPayloadDesignations modifications = new TankPayloadDesignations();
    private TankPayloadDesignations stockModifications = new TankPayloadDesignations();
    private Set<Integer> selectedModifications = new HashSet<>();
    private TankType tankType;

    public TankModifications(TankType tankType)
    {
        this.tankType = tankType;
    }
    
    public void registerStockModification(TankPayloadElement payloadElement)
    {
        TankPayloadDesignation stockModificationPayloadDesignation = getDesignationByElement(payloadElement);
        if (stockModificationPayloadDesignation  != null)
        {
            stockModifications.addPayload(stockModificationPayloadDesignation.getPayloadId(), stockModificationPayloadDesignation.getModMask(), payloadElement);
        }
    }

    public TankModifications copy()
    {
        TankModifications target = new TankModifications(tankType);
        target.modifications = this.modifications.copy();
        target.stockModifications = this.stockModifications.copy();
        target.selectedModifications = new HashSet<Integer>(selectedModifications);
        return target;
    }

    public void addModification(int payloadId, String modMask, TankPayloadElement ... requestedPayloadElements)
    {
        modifications.addPayload(payloadId, modMask, requestedPayloadElements);
    }

    public void clearModifications()
    {
        selectedModifications.clear();
        addStockModifications();
    }

    public void addStockModifications()
    {
        for (TankPayloadDesignation payloadDesignation : stockModifications.getAllAvailablePayloadDesignations())
        {
            selectedModifications.add(payloadDesignation.getPayloadId());
        }
    }

    public List<TankPayloadElement> getSelectedModificationElements() throws PWCGException
    {
        List<TankPayloadElement> selectedModificationElements  = new ArrayList<>();
        for (int selectedModificationId : selectedModifications)
        {
            TankPayloadDesignation selectedModification = modifications.getPayloadDesignation(selectedModificationId);
            TankPayloadElement payloadElement =  getElementForDesignation(selectedModification);
            selectedModificationElements.add(payloadElement);
        }
        return selectedModificationElements;
    }

    public List<TankPayloadDesignation> getSelectedModifications() throws PWCGException
    {
        List<TankPayloadDesignation> selectedModificationDesignations  = new ArrayList<>();
        for (int selectedModificationId : selectedModifications)
        {
            TankPayloadDesignation selectedModification = modifications.getPayloadDesignation(selectedModificationId);
            selectedModificationDesignations.add(selectedModification);
        }
        return selectedModificationDesignations;
    }

    public void selectModification(TankPayloadElement payloadElement)
    {
        TankPayloadDesignation selectedModificationPayloadDesignation = getDesignationByElement(payloadElement);
        if (selectedModificationPayloadDesignation  != null)
        {
            selectedModifications.add(selectedModificationPayloadDesignation.getPayloadId());
        }        
    }

    public List<TankPayloadDesignation> getOptionalPayloadModifications()
    {
        List<TankPayloadDesignation> availableModifications = new ArrayList<>();
        for (TankPayloadDesignation payloadDesignation : modifications.getAllAvailablePayloadDesignations())
        {
            TankPayloadElement modification = getElementForDesignation(payloadDesignation);
            if (!tankType.isStockModification(modification))
            {
                availableModifications.add(payloadDesignation);
            }
        }
        return availableModifications;
    }
    
    private TankPayloadElement getElementForDesignation(TankPayloadDesignation payloadDesignation)
    {
        // All mods have only one payload element
        return payloadDesignation.getPayloadElements().get(0);
    }

    private TankPayloadDesignation getDesignationByElement(TankPayloadElement selectedPayloadElement) 
    {
        for (TankPayloadDesignation payloadDesignation : modifications.getAllAvailablePayloadDesignations())
        {
            for (TankPayloadElement payloadElement : payloadDesignation.getPayloadElements())
            {
                if (selectedPayloadElement == payloadElement)
                {
                    return payloadDesignation;
                }
            }
        }
        
        return null;
    }
}
