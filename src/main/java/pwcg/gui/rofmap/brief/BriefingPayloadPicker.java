package pwcg.gui.rofmap.brief;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JOptionPane;

import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.plane.Role;
import pwcg.campaign.plane.RoleCategory;
import pwcg.campaign.plane.payload.IPayloadFactory;
import pwcg.campaign.plane.payload.IPlanePayload;
import pwcg.campaign.plane.payload.PayloadDesignation;
import pwcg.campaign.plane.payload.PayloadElement;
import pwcg.campaign.squadron.Squadron;
import pwcg.core.exception.PWCGException;
import pwcg.gui.rofmap.brief.model.BriefingData;
import pwcg.product.bos.plane.BosPlaneAttributeMapping;

public class BriefingPayloadPicker
{
    private JComponent parent;
    private BriefingData briefingData;
    
    public BriefingPayloadPicker(JComponent parent, BriefingData briefingData)
    {
        this.parent = parent;
        this.briefingData = briefingData;
    }

    public int pickPayload(String planeType) throws PWCGException 
    {       
        List<String> payloadDescriptions = getAvailablePayloadTypes(planeType);
        Object[] possibilities = payloadDescriptions.toArray();
        
        String payloadDescription = (String)JOptionPane.showInputDialog(
                parent, 
                "Select Payload", 
                "Select Payload", 
                JOptionPane.PLAIN_MESSAGE, 
                null, 
                possibilities, 
                "");
        
        int pickedPayload = getPayloadIndex(planeType, payloadDescription);
        
        return pickedPayload;
    }    

    private List<String> getAvailablePayloadTypes(String planeTypeName) throws PWCGException 
    {
        IPayloadFactory payloadfactory = PWCGContext.getInstance().getPayloadFactory();
        IPlanePayload payload = payloadfactory.createPlanePayload(planeTypeName);
        
        List<String> payloadDescriptions = new ArrayList<>();
        for (PayloadDesignation payloadDesignation : payload.getPayloadDesignations())
        {
            if (shouldIncludeModification(payloadDesignation, planeTypeName))
            {
                payloadDescriptions.add(payloadDesignation.getPayloadDescription());
            }
        }
        
        return payloadDescriptions;
    }

    private int getPayloadIndex(String planeTypeName, String payloadDescription) throws PWCGException
    {
        IPayloadFactory payloadfactory = PWCGContext.getInstance().getPayloadFactory();
        IPlanePayload payload = payloadfactory.createPlanePayload(planeTypeName);
        
        return payload.getPayloadIdByDescription(payloadDescription);
    }
    
    
    private boolean shouldIncludeModification(PayloadDesignation payloadDesignation, String planeType) throws PWCGException
    {
        Squadron squadron = briefingData.getSelectedFlight().getSquadron();
        Role squadronPrimaryRole = squadron.determineSquadronPrimaryRole(briefingData.getSelectedFlight().getCampaign().getDate());
        if (planeType.equals(BosPlaneAttributeMapping.FW190_A6.getPlaneType()))
        {
            return shouldIncludeFW190Payload(payloadDesignation, squadronPrimaryRole, PayloadElement.FW190G3);
        }
        else if (planeType.equals(BosPlaneAttributeMapping.FW190_A8.getPlaneType()))
        {
            return shouldIncludeFW190Payload(payloadDesignation, squadronPrimaryRole, PayloadElement.FW190F8);
        }
        
        return true;
    }

    private boolean shouldIncludeFW190Payload(PayloadDesignation payloadDesignation, Role squadronPrimaryRole, PayloadElement payloadElementKey)
    {
        if (squadronPrimaryRole.isRoleCategory(RoleCategory.ATTACK) && payloadDesignation.containsElement(payloadElementKey))
        {
            return true;
        }
        else if (!squadronPrimaryRole.isRoleCategory(RoleCategory.ATTACK) && !payloadDesignation.containsElement(payloadElementKey))
        {
            return true;
        }
        else
        {
            return false;
        }
    }
}
