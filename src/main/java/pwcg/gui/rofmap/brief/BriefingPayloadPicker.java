package pwcg.gui.rofmap.brief;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JOptionPane;

import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.plane.payload.IPayloadFactory;
import pwcg.campaign.plane.payload.IPlanePayload;
import pwcg.campaign.plane.payload.PayloadDesignation;
import pwcg.core.exception.PWCGException;
import pwcg.gui.rofmap.brief.model.BriefingData;

public class BriefingPayloadPicker
{
    private JComponent parent;
    private BriefingData briefingData;
    
    public BriefingPayloadPicker(JComponent parent, BriefingData briefingData)
    {
        this.parent = parent;
        this.briefingData = briefingData;
    }

    public int pickPayload(String planeType, Date date) throws PWCGException 
    {       
        List<String> payloadDescriptions = getAvailablePayloadTypes(planeType, date);
        Object[] possibilities = payloadDescriptions.toArray();
        
        String payloadDescription = (String)JOptionPane.showInputDialog(
                parent, 
                "Select Payload", 
                "Select Payload", 
                JOptionPane.PLAIN_MESSAGE, 
                null, 
                possibilities, 
                "");
        
        int pickedPayload = getPayloadIndex(planeType, payloadDescription, date);
        
        return pickedPayload;
    }    

    private List<String> getAvailablePayloadTypes(String planeTypeName, Date date) throws PWCGException 
    {
        IPayloadFactory payloadfactory = PWCGContext.getInstance().getPayloadFactory();
        IPlanePayload payload = payloadfactory.createPlanePayload(planeTypeName, date);
        
        List<String> payloadDescriptions = new ArrayList<>();
        for (PayloadDesignation payloadDesignation : payload.getAvailablePayloadDesignations(briefingData.getSelectedFlight()))
        {
            payloadDescriptions.add(payloadDesignation.getPayloadDescription());
        }
        
        return payloadDescriptions;
    }

    private int getPayloadIndex(String planeTypeName, String payloadDescription, Date date) throws PWCGException
    {
        IPayloadFactory payloadfactory = PWCGContext.getInstance().getPayloadFactory();
        IPlanePayload payload = payloadfactory.createPlanePayload(planeTypeName, date);
        
        return payload.getPayloadIdByDescription(payloadDescription);
    }
}
