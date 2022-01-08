package pwcg.gui.rofmap.brief;

import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JCheckBox;
import javax.swing.JPanel;

import pwcg.campaign.tank.payload.ITankPayload;
import pwcg.campaign.tank.payload.TankPayloadFactory;
import pwcg.core.exception.PWCGException;
import pwcg.gui.colors.ColorMap;
import pwcg.gui.dialogs.PWCGMonitorFonts;
import pwcg.gui.utils.PWCGButtonFactory;
import pwcg.mission.playerunit.crew.CrewTankPayloadPairing;

public class BriefingTankModificationsPicker
{
    private ActionListener actionListener;
    private CrewTankPayloadPairing crewPlane;
    private JPanel tankModificationsPanel = new JPanel(new GridLayout(0,1));
    private Map<String, JCheckBox> tankModifications = new HashMap<>();
    private Date date;
    
    public BriefingTankModificationsPicker(ActionListener actionListener, CrewTankPayloadPairing crewPlane, Date date)
    {
        this.actionListener = actionListener;
        this.crewPlane = crewPlane;
        this.date = date;
    }

    public JPanel makeTankModifications() throws PWCGException 
    {       
        makePlaneModificationCheckBoxes();        
        tankModificationsPanel = new JPanel(new GridLayout(0,1));
        tankModificationsPanel.setOpaque(false);

        for (String selectedModification : crewPlane.getModifications())
        {
            JCheckBox tankModificationCheckBox = tankModifications.get(selectedModification);
            if (tankModificationCheckBox != null)
            {
                tankModificationCheckBox.setSelected(true);
            }
        }

        for (JCheckBox tankModification : tankModifications.values())
        {
            tankModificationsPanel.add(tankModification);
        }
        
        return tankModificationsPanel;
    }    

    public List<String> getChosenPlaneModifications() throws PWCGException 
    {       
        List<String> chosentankModificationsMasks = new ArrayList<>();
        for (JCheckBox tankModification : tankModifications.values())
        {
            if (tankModification.isSelected())
            {
                String payloadDescription = tankModification.getText();
                String mask = getPayloadMaskForChosenModifications(crewPlane.getTank().getType(), payloadDescription);
                chosentankModificationsMasks.add(mask);
            }
        }
        
        return chosentankModificationsMasks;
    }    

    private void makePlaneModificationCheckBoxes() throws PWCGException 
    {
        BriefingTankModificationsFilter briefingPlaneModificationsFilter = new BriefingTankModificationsFilter(crewPlane);
        List<String> payloadsForPlane = briefingPlaneModificationsFilter.selectModificationsForPlane(date);

        Font font = PWCGMonitorFonts.getPrimaryFontSmall();

        for (String payloadForPlane : payloadsForPlane)
        {
            JCheckBox tankModificationsCheckBox= PWCGButtonFactory.makeCheckBox(
                    payloadForPlane, 
                    "SelectPlaneModification:" + crewPlane.getCrewMember().getSerialNumber(), 
                    font,
                    ColorMap.CHALK_FOREGROUND,
                    actionListener);
            tankModifications.put(payloadForPlane, tankModificationsCheckBox);
        }
    }

    private String getPayloadMaskForChosenModifications(String tankTypeName, String payloadDescription) throws PWCGException
    {
        TankPayloadFactory payloadfactory = new TankPayloadFactory();
        ITankPayload payload = payloadfactory.createPayload(tankTypeName, date);
        
        return payload.getPayloadMaskByDescription(payloadDescription);
    }

    public Map<String, JCheckBox> getPlaneModifications()
    {
        return tankModifications;
    }
    
    
}
