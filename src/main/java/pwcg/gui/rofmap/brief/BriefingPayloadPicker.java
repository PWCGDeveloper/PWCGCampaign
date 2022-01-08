package pwcg.gui.rofmap.brief;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JOptionPane;

import pwcg.campaign.tank.payload.ITankPayload;
import pwcg.campaign.tank.payload.TankPayloadDesignation;
import pwcg.campaign.tank.payload.TankPayloadFactory;
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

    public int pickPayload(String tankType, Date date) throws PWCGException 
    {       
        List<String> payloadDescriptions = getAvailablePayloadTypes(tankType, date);
        Object[] possibilities = payloadDescriptions.toArray();
        
        String payloadDescription = (String)JOptionPane.showInputDialog(
                parent, 
                "Select Payload", 
                "Select Payload", 
                JOptionPane.PLAIN_MESSAGE, 
                null, 
                possibilities, 
                "");
        
        int pickedPayload = getPayloadIndex(tankType, payloadDescription, date);
        
        return pickedPayload;
    }    

    private List<String> getAvailablePayloadTypes(String tankTypeName, Date date) throws PWCGException 
    {
        TankPayloadFactory payloadfactory = new TankPayloadFactory();
        ITankPayload payload = payloadfactory.createPayload(tankTypeName, date);
        
        List<String> payloadDescriptions = new ArrayList<>();
        for (TankPayloadDesignation payloadDesignation : payload.getAvailablePayloadDesignations(briefingData.getSelectedUnit()))
        {
            payloadDescriptions.add(payloadDesignation.getPayloadDescription());
        }
        
        return payloadDescriptions;
    }

    private int getPayloadIndex(String tankTypeName, String payloadDescription, Date date) throws PWCGException
    {
        TankPayloadFactory payloadfactory = new TankPayloadFactory();
        ITankPayload payload = payloadfactory.createPayload(tankTypeName, date);
        
        return payload.getPayloadIdByDescription(payloadDescription);
    }
}
