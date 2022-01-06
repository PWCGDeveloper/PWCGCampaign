package pwcg.campaign.plane.payload;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import pwcg.campaign.plane.PlaneType;
import pwcg.core.exception.PWCGException;

public class PlaneModifications
{
    private PlanePayloadDesignations modifications = new PlanePayloadDesignations();
    private PlanePayloadDesignations stockModifications = new PlanePayloadDesignations();
    private Set<Integer> selectedModifications = new HashSet<>();
    private PlaneType planeType;

    public PlaneModifications(PlaneType planeType)
    {
        this.planeType = planeType;
    }
    
    public void registerStockModification(PlanePayloadElement payloadElement)
    {
        PlanePayloadDesignation stockModificationPayloadDesignation = getDesignationByElement(payloadElement);
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

    public void addModification(int payloadId, String modMask, PlanePayloadElement ... requestedPayloadElements)
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
        for (PlanePayloadDesignation payloadDesignation : stockModifications.getAllAvailablePayloadDesignations())
        {
            selectedModifications.add(payloadDesignation.getPayloadId());
        }
    }

    public List<PlanePayloadElement> getSelectedModificationElements() throws PWCGException
    {
        List<PlanePayloadElement> selectedModificationElements  = new ArrayList<>();
        for (int selectedModificationId : selectedModifications)
        {
            PlanePayloadDesignation selectedModification = modifications.getPayloadDesignation(selectedModificationId);
            PlanePayloadElement payloadElement =  getElementForDesignation(selectedModification);
            selectedModificationElements.add(payloadElement);
        }
        return selectedModificationElements;
    }

    public List<PlanePayloadDesignation> getSelectedModifications() throws PWCGException
    {
        List<PlanePayloadDesignation> selectedModificationDesignations  = new ArrayList<>();
        for (int selectedModificationId : selectedModifications)
        {
            PlanePayloadDesignation selectedModification = modifications.getPayloadDesignation(selectedModificationId);
            selectedModificationDesignations.add(selectedModification);
        }
        return selectedModificationDesignations;
    }

    public void selectModification(PlanePayloadElement payloadElement)
    {
        PlanePayloadDesignation selectedModificationPayloadDesignation = getDesignationByElement(payloadElement);
        if (selectedModificationPayloadDesignation  != null)
        {
            selectedModifications.add(selectedModificationPayloadDesignation.getPayloadId());
        }        
    }

    public List<PlanePayloadDesignation> getOptionalPayloadModifications()
    {
        List<PlanePayloadDesignation> availableModifications = new ArrayList<>();
        for (PlanePayloadDesignation payloadDesignation : modifications.getAllAvailablePayloadDesignations())
        {
            PlanePayloadElement modification = getElementForDesignation(payloadDesignation);
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
    
    private boolean isMissionSpecialModification(PlanePayloadDesignation payloadDesignation)
    {
        for (PlanePayloadElement payloadElement : payloadDesignation.getPayloadElements())
        {
            if (payloadElement == PlanePayloadElement.CAMERA)
            {
                return true;
            }
            
            if (payloadElement == PlanePayloadElement.RADIO)
            {
                return true;
            }
        }
        return false;
    }
    
    private PlanePayloadElement getElementForDesignation(PlanePayloadDesignation payloadDesignation)
    {
        // All mods have only one payload element
        return payloadDesignation.getPayloadElements().get(0);
    }

    private PlanePayloadDesignation getDesignationByElement(PlanePayloadElement selectedPayloadElement) 
    {
        for (PlanePayloadDesignation payloadDesignation : modifications.getAllAvailablePayloadDesignations())
        {
            for (PlanePayloadElement payloadElement : payloadDesignation.getPayloadElements())
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
