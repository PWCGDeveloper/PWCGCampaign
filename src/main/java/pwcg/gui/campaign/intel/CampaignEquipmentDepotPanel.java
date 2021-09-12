package pwcg.gui.campaign.intel;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.swing.JPanel;
import javax.swing.JTextArea;

import pwcg.campaign.ArmedService;
import pwcg.campaign.Campaign;
import pwcg.campaign.plane.EquippedPlane;
import pwcg.campaign.plane.PwcgRole;
import pwcg.campaign.resupply.depot.EquipmentDepot;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.DateUtils;
import pwcg.core.utils.PWCGLogger;
import pwcg.gui.dialogs.ErrorDialog;
import pwcg.gui.dialogs.PWCGMonitorFonts;

public class CampaignEquipmentDepotPanel extends JPanel
{
	private static final long serialVersionUID = 1L;
	
    protected Campaign campaign;
    protected ArmedService service;
    
	public CampaignEquipmentDepotPanel(Campaign campaign, ArmedService service) throws PWCGException  
	{
        super();
        this.campaign = campaign;
        this.service = service;
        this.setLayout(new BorderLayout());
        this.setOpaque(false);
	}    

    protected void makePanel() throws PWCGException
    {
        try
        {
            JPanel depoPanel = formDepotText();
            this.add(depoPanel, BorderLayout.NORTH);
        }
        catch (Exception e)
        {
            PWCGLogger.logException(e);
            ErrorDialog.internalError(e.getMessage());
        }       
    }

    private JPanel formDepotText() throws PWCGException
    {        
        JPanel depoHeaderPanel = formHeader();
        JPanel depoPanel = new JPanel(new BorderLayout());
        depoPanel.setOpaque(false);
        depoPanel.add(depoHeaderPanel, BorderLayout.NORTH);

        JPanel depoBodyPanel = new JPanel(new GridLayout(0, 3));
        depoBodyPanel.setOpaque(false);
        depoPanel.add(depoBodyPanel, BorderLayout.CENTER);

        createDepotForRole(PwcgRole.ROLE_FIGHTER, depoBodyPanel);
        createDepotForRole(PwcgRole.ROLE_ATTACK, depoBodyPanel);
        createDepotForRole(PwcgRole.ROLE_DIVE_BOMB, depoBodyPanel);
        createDepotForRole(PwcgRole.ROLE_RECON, depoBodyPanel);
        createDepotForRole(PwcgRole.ROLE_BOMB, depoBodyPanel);
        createDepotForRole(PwcgRole.ROLE_STRAT_BOMB, depoBodyPanel);
        createDepotForRole(PwcgRole.ROLE_SEA_PLANE, depoBodyPanel);
        createDepotForRole(PwcgRole.ROLE_TRANSPORT, depoBodyPanel);
        
        return depoPanel;
    }

    private JPanel formHeader() throws PWCGException
    {
        JPanel depoHeaderPanel = new JPanel(new BorderLayout());
        depoHeaderPanel.setOpaque(false);

        StringBuffer depoStatusBuffer = new StringBuffer("");
        depoStatusBuffer.append("Aircraft Depot Status Report\n");
        depoStatusBuffer.append("Date: " + DateUtils.getDateString(campaign.getDate()) + "\n");
        
        EquipmentDepot aircraftOnInventory = campaign.getEquipmentManager().getEquipmentDepotForService(service.getServiceId());
        depoStatusBuffer.append("Last Replacement Date: " + DateUtils.getDateString(aircraftOnInventory.getLastReplacementDate()) + "\n");

        depoStatusBuffer.append(service.getName());          
        depoStatusBuffer.append(" Inventory.\n");

        JTextArea headertext = createDepotText();
        headertext.setText(depoStatusBuffer.toString());
        depoHeaderPanel.add(headertext, BorderLayout.NORTH);
        return depoHeaderPanel;
    }

    private void createDepotForRole(PwcgRole role, JPanel depotBodyPanel) throws PWCGException
    {
        List<EquippedPlane> aircraftForRole = getDepotAircraftForRole(role);
        if (aircraftForRole.size() > 0)
        {
            JPanel depotRolePanel = new JPanel(new BorderLayout());
            depotRolePanel.setOpaque(false);
            
            String aircraftForRoleReport = formAircraftInventory(aircraftForRole, role);
            
            JTextArea aircraftForRoleText = createDepotText();
            aircraftForRoleText.setText(aircraftForRoleReport);
            depotRolePanel.add(aircraftForRoleText, BorderLayout.NORTH);
            
            depotBodyPanel.add(depotRolePanel);
        }
    }

    private JTextArea createDepotText() throws PWCGException
    {
        JTextArea serviceDepotText;
        Font font = PWCGMonitorFonts.getTypewriterFont();
        serviceDepotText = new JTextArea();
        serviceDepotText.setFont(font);
        serviceDepotText.setOpaque(false);
        serviceDepotText.setLineWrap(true);
        serviceDepotText.setWrapStyleWord(true);
        serviceDepotText.setText("");

        return serviceDepotText;
    }

    private String formAircraftInventory(List<EquippedPlane> aircraftForRole, PwcgRole role) throws PWCGException
    {
        Map<String, Integer> planeCount = new TreeMap<>();
        for (EquippedPlane plane : aircraftForRole)
        {
            if (!planeCount.containsKey(plane.getDisplayName()))
            {
                planeCount.put(plane.getDisplayName(), 0);
            }
            
            int count = planeCount.get(plane.getDisplayName());
            ++count;
            planeCount.put(plane.getDisplayName(), count);
        }
        
        StringBuffer depoStatusBuffer = new StringBuffer();
        depoStatusBuffer.append("\n  " + role.getRoleDescription() + "\n");        
        for (String planeDesc : planeCount.keySet())
        {
            int count = planeCount.get(planeDesc);
            depoStatusBuffer.append("    " + planeDesc + ": " + count);
            depoStatusBuffer.append(".\n");          
        }
        
        return depoStatusBuffer.toString();
    }
    
    private List<EquippedPlane> getDepotAircraftForRole(PwcgRole role) throws PWCGException
    {
        EquipmentDepot equipmentDepot = campaign.getEquipmentManager().getEquipmentDepotForService(service.getServiceId());
        return equipmentDepot.getDepotAircraftForRole(role);
    }
}
