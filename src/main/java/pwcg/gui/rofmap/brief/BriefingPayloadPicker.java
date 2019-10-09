package pwcg.gui.rofmap.brief;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JOptionPane;

import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.plane.payload.IPayloadFactory;
import pwcg.campaign.plane.payload.IPlanePayload;
import pwcg.campaign.plane.payload.PayloadDesignation;
import pwcg.campaign.plane.payload.PayloadElementCategory;
import pwcg.core.exception.PWCGException;

public class BriefingPayloadPicker
{
    private JComponent parent;
    
    public BriefingPayloadPicker(JComponent parent)
    {
        this.parent = parent;
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
            if (!(payloadDesignation.getPayloadElements().get(0).getCategory() == PayloadElementCategory.PLANE_PART))
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
}
