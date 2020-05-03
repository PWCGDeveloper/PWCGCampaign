package pwcg.gui.rofmap.brief;

import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JCheckBox;
import javax.swing.JPanel;

import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.plane.payload.IPayloadFactory;
import pwcg.campaign.plane.payload.IPlanePayload;
import pwcg.campaign.plane.payload.PayloadDesignation;
import pwcg.campaign.plane.payload.PayloadElementCategory;
import pwcg.core.exception.PWCGException;
import pwcg.gui.colors.ColorMap;
import pwcg.gui.utils.PWCGButtonFactory;
import pwcg.mission.flight.crew.CrewPlanePayloadPairing;

public class BriefingPlaneModificationsPicker
{
    private BriefingPilotPanelSet parent;
    private CrewPlanePayloadPairing crewPlane;
    private JPanel planeModificationsPanel = new JPanel(new GridLayout(0,1));
    private Map<String, JCheckBox> planeModifications = new HashMap<>();
    
    public BriefingPlaneModificationsPicker(BriefingPilotPanelSet parent, CrewPlanePayloadPairing crewPlane)
    {
        this.parent = parent;
        this.crewPlane = crewPlane;
    }

    public JPanel makePlaneModifications() throws PWCGException 
    {       
        makePlaneModificationCheckBoxes();        
        planeModificationsPanel = new JPanel(new GridLayout(0,1));
        planeModificationsPanel.setOpaque(false);

        for (String selectedModification : crewPlane.getModifications())
        {
            JCheckBox planeModificationCheckBox = planeModifications.get(selectedModification);
            if (planeModificationCheckBox != null)
            {
                planeModificationCheckBox.setSelected(true);
            }
        }

        for (JCheckBox planeModification : planeModifications.values())
        {
            planeModificationsPanel.add(planeModification);
        }
        
        return planeModificationsPanel;
    }    

    public List<String> getChosenPlaneModifications() throws PWCGException 
    {       
        List<String> chosenplaneModificationsMasks = new ArrayList<>();
        for (JCheckBox planeModification : planeModifications.values())
        {
            if (planeModification.isSelected())
            {
                String payloadDescription = planeModification.getText();
                String mask = getPayloadMaskForChosenModifications(crewPlane.getPlane().getType(), payloadDescription);
                chosenplaneModificationsMasks.add(mask);
            }
        }
        
        return chosenplaneModificationsMasks;
    }    

    private void makePlaneModificationCheckBoxes() throws PWCGException 
    {
        IPayloadFactory payloadfactory = PWCGContext.getInstance().getPayloadFactory();
        IPlanePayload payload = payloadfactory.createPlanePayload(crewPlane.getPlane().getType());
        
        for (PayloadDesignation payloadDesignation : payload.getPayloadDesignations())
        {
            if (payloadDesignation.getPayloadElements().get(0).getCategory() == PayloadElementCategory.MODIFICATION)
            {
                JCheckBox planeModificationsCheckBox= PWCGButtonFactory.makeCheckBox(
                        payloadDesignation.getPayloadDescription(), 
                        "SelectPlaneModification:" + crewPlane.getPilot().getSerialNumber(), 
                        ColorMap.CHALK_FOREGROUND,
                        parent);
                planeModifications.put(payloadDesignation.getPayloadDescription(), planeModificationsCheckBox);
            }
        }
    }

    private String getPayloadMaskForChosenModifications(String planeTypeName, String payloadDescription) throws PWCGException
    {
        IPayloadFactory payloadfactory = PWCGContext.getInstance().getPayloadFactory();
        IPlanePayload payload = payloadfactory.createPlanePayload(planeTypeName);
        
        return payload.getPayloadMaskByDescription(payloadDescription);
    }

    public Map<String, JCheckBox> getPlaneModifications()
    {
        return planeModifications;
    }
    
    
}
