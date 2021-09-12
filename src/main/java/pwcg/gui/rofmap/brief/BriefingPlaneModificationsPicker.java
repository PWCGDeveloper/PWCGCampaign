package pwcg.gui.rofmap.brief;

import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.CheckBox;
import javafx.scene.layout.Pane;

import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.plane.payload.IPayloadFactory;
import pwcg.campaign.plane.payload.IPlanePayload;
import pwcg.core.exception.PWCGException;
import pwcg.gui.colors.ColorMap;
import pwcg.gui.utils.ButtonFactory;
import pwcg.mission.flight.crew.CrewPlanePayloadPairing;

public class BriefingPlaneModificationsPicker
{
    private ActionListener actionListener;
    private CrewPlanePayloadPairing crewPlane;
    private Pane planeModificationsPanel = new Pane(new GridLayout(0,1));
    private Map<String, CheckBox> planeModifications = new HashMap<>();
    
    public BriefingPlaneModificationsPicker(ActionListener actionListener, CrewPlanePayloadPairing crewPlane)
    {
        this.actionListener = actionListener;
        this.crewPlane = crewPlane;
    }

    public Pane makePlaneModifications() throws PWCGException 
    {       
        makePlaneModificationCheckBoxes();        
        planeModificationsPanel = new Pane(new GridLayout(0,1));
        planeModificationsPanel.setOpaque(false);

        for (String selectedModification : crewPlane.getModifications())
        {
            CheckBox planeModificationCheckBox = planeModifications.get(selectedModification);
            if (planeModificationCheckBox != null)
            {
                planeModificationCheckBox.setSelected(true);
            }
        }

        for (CheckBox planeModification : planeModifications.values())
        {
            planeModificationsPanel.add(planeModification);
        }
        
        return planeModificationsPanel;
    }    

    public List<String> getChosenPlaneModifications() throws PWCGException 
    {       
        List<String> chosenplaneModificationsMasks = new ArrayList<>();
        for (CheckBox planeModification : planeModifications.values())
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
        BriefingPlaneModificationsFilter briefingPlaneModificationsFilter = new BriefingPlaneModificationsFilter(crewPlane);
        List<String> payloadsForPlane = briefingPlaneModificationsFilter.selectModificationsForPlane();
        
        for (String payloadForPlane : payloadsForPlane)
        {
            CheckBox planeModificationsCheckBox= ButtonFactory.makeSmallCheckBox(
                    payloadForPlane, 
                    "SelectPlaneModification:" + crewPlane.getPilot().getSerialNumber(), 
                    ColorMap.CHALK_FOREGROUND,
                    actionListener);
            planeModifications.put(payloadForPlane, planeModificationsCheckBox);
        }
    }

    private String getPayloadMaskForChosenModifications(String planeTypeName, String payloadDescription) throws PWCGException
    {
        IPayloadFactory payloadfactory = PWCGContext.getInstance().getPayloadFactory();
        IPlanePayload payload = payloadfactory.createPlanePayload(planeTypeName);
        
        return payload.getPayloadMaskByDescription(payloadDescription);
    }

    public Map<String, CheckBox> getPlaneModifications()
    {
        return planeModifications;
    }
    
    
}
