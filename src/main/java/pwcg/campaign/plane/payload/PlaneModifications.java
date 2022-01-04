package pwcg.campaign.plane.payload;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import pwcg.campaign.tank.TankType;
import pwcg.core.exception.PWCGException;

public class PlaneModifications
{
    private PayloadDesignations modifications = new PayloadDesignations();
    private PayloadDesignations stockModifications = new PayloadDesignations();
    private Set<Integer> selectedModifications = new HashSet<>();
    private TankType planeType;

    public PlaneModifications(TankType planeType)
    {
        this.planeType = planeType;
    }
    
    public void registerStockModification(PayloadElement payloadElement)
    {
        PayloadDesignation stockModificationPayloadDesignation = getDesignationByElement(payloadElement);
        if (stockModificationPayloadDesignation  != null)
        {
            stockModifications.addPayload(stockModificationPayloadDesignation.getPayloadId(), stockModificationPayloadDesignation.getModMask(), payloadElement);
        }
    }

    public PlaneModifications copy()
    {
        PlaneModifications target = new PlaneModifications(planeType);
        target.modifications = this.modifications.copy();
        target.stockModifications = this.stockModifications.copy();
        target.selectedModifications = new HashSet<Integer>(selectedModifications);
        return target;
    }

    public void addModification(int payloadId, String modMask, PayloadElement ... requestedPayloadElements)
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
        for (PayloadDesignation payloadDesignation : stockModifications.getAllAvailablePayloadDesignations())
        {
            selectedModifications.add(payloadDesignation.getPayloadId());
        }
    }

    public List<PayloadElement> getSelectedModificationElements() throws PWCGException
    {
        List<PayloadElement> selectedModificationElements  = new ArrayList<>();
        for (int selectedModificationId : selectedModifications)
        {
            PayloadDesignation selectedModification = modifications.getPayloadDesignation(selectedModificationId);
            PayloadElement payloadElement =  getElementForDesignation(selectedModification);
            selectedModificationElements.add(payloadElement);
        }
        return selectedModificationElements;
    }

    public List<PayloadDesignation> getSelectedModifications() throws PWCGException
    {
        List<PayloadDesignation> selectedModificationDesignations  = new ArrayList<>();
        for (int selectedModificationId : selectedModifications)
        {
            PayloadDesignation selectedModification = modifications.getPayloadDesignation(selectedModificationId);
            selectedModificationDesignations.add(selectedModification);
        }
        return selectedModificationDesignations;
    }

    public void selectModification(PayloadElement payloadElement)
    {
        PayloadDesignation selectedModificationPayloadDesignation = getDesignationByElement(payloadElement);
        if (selectedModificationPayloadDesignation  != null)
        {
            selectedModifications.add(selectedModificationPayloadDesignation.getPayloadId());
        }        
    }

    public List<PayloadDesignation> getOptionalPayloadModifications()
    {
        List<PayloadDesignation> availableModifications = new ArrayList<>();
        for (PayloadDesignation payloadDesignation : modifications.getAllAvailablePayloadDesignations())
        {
            PayloadElement modification = getElementForDesignation(payloadDesignation);
            if (!planeType.isStockModification(modification))
            {
                if (!isMissionSpecialModification(payloadDesignation))
                {
                    availableModifications.add(payloadDesignation);
                }
            }
        }
        return availableModifications;
    }
    
    private boolean isMissionSpecialModification(PayloadDesignation payloadDesignation)
    {
        for (PayloadElement payloadElement : payloadDesignation.getPayloadElements())
        {
            if (payloadElement == PayloadElement.CAMERA)
            {
                return true;
            }
            
            if (payloadElement == PayloadElement.RADIO)
            {
                return true;
            }
        }
        return false;
    }
    
    private PayloadElement getElementForDesignation(PayloadDesignation payloadDesignation)
    {
        // All mods have only one payload element
        return payloadDesignation.getPayloadElements().get(0);
    }

    private PayloadDesignation getDesignationByElement(PayloadElement selectedPayloadElement) 
    {
        for (PayloadDesignation payloadDesignation : modifications.getAllAvailablePayloadDesignations())
        {
            for (PayloadElement payloadElement : payloadDesignation.getPayloadElements())
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
