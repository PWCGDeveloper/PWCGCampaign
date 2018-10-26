package pwcg.gui.campaign.depo;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.JTextArea;

import pwcg.campaign.ArmedService;
import pwcg.campaign.Campaign;
import pwcg.campaign.plane.EquipmentReplacement;
import pwcg.campaign.plane.EquippedPlane;
import pwcg.campaign.plane.PlaneSorter;
import pwcg.campaign.plane.Role;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.DateUtils;
import pwcg.core.utils.Logger;
import pwcg.gui.dialogs.ErrorDialog;
import pwcg.gui.dialogs.MonitorSupport;
import pwcg.gui.utils.ContextSpecificImages;
import pwcg.gui.utils.ImagePanel;

public class CampaignEquipmentDepoPanel extends ImagePanel
{
	private static final long serialVersionUID = 1L;
	
    protected Campaign campaign;
    protected ArmedService service;
    
	public CampaignEquipmentDepoPanel(Campaign campaign, ArmedService service) throws PWCGException  
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
            String imagePath = ContextSpecificImages.imagesMisc() + "PaperFull.jpg";
            setImage(imagePath);
            
            JPanel depoPanel = formDepoText();
            this.add(depoPanel, BorderLayout.NORTH);
        }
        catch (Exception e)
        {
            Logger.logException(e);
            ErrorDialog.internalError(e.getMessage());
        }       
    }

    private JPanel formDepoText() throws PWCGException
    {        
        JPanel depoHeaderPanel = formHeader();
        JPanel depoPanel = new JPanel(new BorderLayout());
        depoPanel.setOpaque(false);
        depoPanel.add(depoHeaderPanel, BorderLayout.NORTH);

        JPanel depoBodyPanel = new JPanel(new GridLayout(0, 3));
        depoBodyPanel.setOpaque(false);
        depoPanel.add(depoBodyPanel, BorderLayout.CENTER);

        createDepoForRole(Role.ROLE_FIGHTER, depoBodyPanel);
        createDepoForRole(Role.ROLE_ATTACK, depoBodyPanel);
        createDepoForRole(Role.ROLE_DIVE_BOMB, depoBodyPanel);
        createDepoForRole(Role.ROLE_RECON, depoBodyPanel);
        createDepoForRole(Role.ROLE_BOMB, depoBodyPanel);
        createDepoForRole(Role.ROLE_STRAT_BOMB, depoBodyPanel);
        createDepoForRole(Role.ROLE_SEA_PLANE, depoBodyPanel);
        createDepoForRole(Role.ROLE_TRANSPORT, depoBodyPanel);
        
        return depoPanel;
    }

    private JPanel formHeader() throws PWCGException
    {
        JPanel depoHeaderPanel = new JPanel(new BorderLayout());
        depoHeaderPanel.setOpaque(false);

        StringBuffer depoStatusBuffer = new StringBuffer("");
        depoStatusBuffer.append("Aircraft Depo Status Report\n");
        depoStatusBuffer.append("Date: " + DateUtils.getDateString(campaign.getDate()) + "\n");
        
        EquipmentReplacement aircraftOnInventory = campaign.getEquipmentManager().getEquipmentReplacementsForService(service.getServiceId());
        depoStatusBuffer.append("Last Replacement Date: " + DateUtils.getDateString(aircraftOnInventory.getLastReplacementDate()) + "\n");

        depoStatusBuffer.append(service.getName());          
        depoStatusBuffer.append(" Inventory.\n");

        JTextArea headertext = createDepoText();
        headertext.setText(depoStatusBuffer.toString());
        depoHeaderPanel.add(headertext, BorderLayout.NORTH);
        return depoHeaderPanel;
    }

    private void createDepoForRole(Role role, JPanel depoBodyPanel) throws PWCGException
    {
        List<EquippedPlane> aircraftForRole = getDepoAircraftForRole(role);
        if (aircraftForRole.size() > 0)
        {
            JPanel depoRolePanel = new JPanel(new BorderLayout());
            depoRolePanel.setOpaque(false);
            
            String aircraftForRoleReport = formAircraftInventory(aircraftForRole, role);
            
            JTextArea aircraftForRoleText = createDepoText();
            aircraftForRoleText.setText(aircraftForRoleReport);
            depoRolePanel.add(aircraftForRoleText, BorderLayout.NORTH);
            
            depoBodyPanel.add(depoRolePanel);
        }
    }

    private JTextArea createDepoText() throws PWCGException
    {
        JTextArea serviceDepoText;
        Font font = MonitorSupport.getTypewriterFont();
        serviceDepoText = new JTextArea();
        serviceDepoText.setFont(font);
        serviceDepoText.setOpaque(false);
        serviceDepoText.setLineWrap(true);
        serviceDepoText.setWrapStyleWord(true);
        serviceDepoText.setText("");

        return serviceDepoText;
    }

    private String formAircraftInventory(List<EquippedPlane> aircraftForRole, Role role) throws PWCGException
    {
        StringBuffer depoStatusBuffer = new StringBuffer();
        depoStatusBuffer.append("\n  " + Role.roleToSDesc(role) + "\n");        
        for (EquippedPlane plane : aircraftForRole)
        {
            depoStatusBuffer.append("    " + plane.getDisplayName() + " (" + plane.getSerialNumber() + ")");
            depoStatusBuffer.append(".\n");          
        }
        
        return depoStatusBuffer.toString();
    }
    
    private List<EquippedPlane> getDepoAircraftForRole(Role role) throws PWCGException
    {
        List<EquippedPlane> depoForRole = new ArrayList<>();
        EquipmentReplacement aircraftOnInventory = campaign.getEquipmentManager().getEquipmentReplacementsForService(service.getServiceId());
        for (EquippedPlane equippedPlane : aircraftOnInventory.getEquipment().getEquippedPlanes().values())
        {
            if (equippedPlane.getRoles().get(0) == role)
            {
                depoForRole.add(equippedPlane);
            }
        }
        List<EquippedPlane> sortedDepoForRole = PlaneSorter.sortEquippedPlanesByGoodness(depoForRole);
        return sortedDepoForRole;
    }
}
