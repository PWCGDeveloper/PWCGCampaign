package pwcg.gui.campaign.intel;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import pwcg.campaign.Campaign;
import pwcg.campaign.api.IAirfield;
import pwcg.campaign.api.Side;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.personnel.SquadronMemberFilter;
import pwcg.campaign.personnel.SquadronPersonnel;
import pwcg.campaign.plane.EquippedPlane;
import pwcg.campaign.plane.PlaneSorter;
import pwcg.campaign.plane.Role;
import pwcg.campaign.squadmember.SquadronMember;
import pwcg.campaign.squadmember.SquadronMembers;
import pwcg.campaign.squadron.Squadron;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.Logger;
import pwcg.gui.dialogs.ErrorDialog;
import pwcg.gui.dialogs.MonitorSupport;
import pwcg.gui.utils.ContextSpecificImages;
import pwcg.gui.utils.ImagePanel;
import pwcg.gui.utils.PWCGButtonFactory;

public abstract class CampaignIntelligenceBase extends ImagePanel implements ActionListener
{
	private static final long serialVersionUID = 1L;
	
    protected StringBuffer intelHeaderBuffer = new StringBuffer("");
    protected Campaign campaign;
	protected SquadronMember referencePlayer;
    protected JPanel squadronsByRoleContainer;
    protected JTextArea squadronInteltext;
    
	public CampaignIntelligenceBase() throws PWCGException  
	{
        super();
		this.campaign = PWCGContext.getInstance().getCampaign();
		this.referencePlayer = PWCGContext.getInstance().getReferencePlayer();
        this.setLayout(new BorderLayout());
        this.setOpaque(false);
	}    

    protected void makePanel(Side side) throws PWCGException
    {
        try
        {
            String imagePath = ContextSpecificImages.imagesMisc() + "PaperFull.jpg";
            setImage(imagePath);
            
            formSquadronDesc(side);
            formSquadronIntelText();
        }
        catch (Exception e)
        {
            Logger.logException(e);
            ErrorDialog.internalError(e.getMessage());
        }       
    }

    private void formSquadronIntelText() throws PWCGException
    {
        Font font = MonitorSupport.getTypewriterFont();
        squadronInteltext = new JTextArea();
        squadronInteltext.setFont(font);
        squadronInteltext.setOpaque(false);
        squadronInteltext.setLineWrap(true);
        squadronInteltext.setWrapStyleWord(true);
        squadronInteltext.setText("");

        this.add(squadronInteltext, BorderLayout.CENTER);
    }

    protected void formSquadronDesc(Side side) throws PWCGException 
    {
        squadronsByRoleContainer = new JPanel(new GridLayout(0,1));
        squadronsByRoleContainer.setOpaque(false);
        this.add(squadronsByRoleContainer, BorderLayout.WEST);

        formSquadronDescForRole(Role.ROLE_FIGHTER, side);
        formSquadronDescForRole(Role.ROLE_ATTACK, side);
        formSquadronDescForRole(Role.ROLE_DIVE_BOMB, side);
        formSquadronDescForRole(Role.ROLE_RECON, side);
        formSquadronDescForRole(Role.ROLE_BOMB, side);
        formSquadronDescForRole(Role.ROLE_STRAT_BOMB, side);
        formSquadronDescForRole(Role.ROLE_SEA_PLANE, side);
        formSquadronDescForRole(Role.ROLE_TRANSPORT, side);
    }    

    private void formSquadronDescForRole(Role role, Side side) throws PWCGException
    {
        List<Squadron> squadrons = getSquadronsForIntel(role, side);
        if (squadrons.size() == 0)
        {
            return;
        }

        JPanel squadronsForRolePanel = new JPanel(new GridLayout(0,1));
        squadronsForRolePanel.setOpaque(false);
        for (Squadron squadron : squadrons)
        {
            JButton squadronSelectButton = PWCGButtonFactory.makePaperButton(squadron.determineDisplayName(campaign.getDate()), "SquadronSelected:" + squadron.getSquadronId(), this);
            squadronsForRolePanel.add(squadronSelectButton);
        }

        JPanel squadronsByRolePanel = new JPanel(new BorderLayout());
        squadronsByRolePanel.setOpaque(false);
        JLabel headerLabel = PWCGButtonFactory.makePaperLabelLarge(Role.roleToSDesc(role) + " Squadrons: \n");
        squadronsByRolePanel.add(headerLabel, BorderLayout.NORTH);
        squadronsByRolePanel.add(squadronsForRolePanel, BorderLayout.CENTER);
        
        JPanel squadronsByRolePanelSqueezer = new JPanel(new BorderLayout());
        squadronsByRolePanelSqueezer.setOpaque(false);
        squadronsByRolePanelSqueezer.add(squadronsByRolePanel, BorderLayout.NORTH);

        squadronsByRoleContainer.add(squadronsByRolePanelSqueezer);
    }

    private List<Squadron> getSquadronsForIntel(Role role, Side side) throws PWCGException
    {
        List<Squadron> squadronsWithPrimaryRole = new ArrayList<Squadron>();
        
        List<Role> acceptableRoles = new ArrayList<Role>();
        acceptableRoles.add(role);

        Campaign campaign = PWCGContext.getInstance().getCampaign();
        String airfieldName = referencePlayer.determineSquadron().determineCurrentAirfieldName(campaign.getDate());
        IAirfield field =  PWCGContext.getInstance().getAirfieldAllMaps(airfieldName);

        List<Squadron> squadrons = PWCGContext.getInstance().getSquadronManager().getNearestSquadronsByRole(
                campaign, field.getPosition(), 5, 55000, acceptableRoles, side, campaign.getDate());

        for (Squadron squadron : squadrons)
        {
            if (squadron.determineSquadronPrimaryRole(campaign.getDate()) == role)
            {
                squadronsWithPrimaryRole.add(squadron);
            }
        }
        
        return squadronsWithPrimaryRole;
    }
    

    @Override
    public void actionPerformed(ActionEvent actionEvent)
    {
        String action = actionEvent.getActionCommand();

        try
        {
            if (action.contains("SquadronSelected"))
            {
                int beginIndex = action.indexOf(":");
                ++beginIndex;
                String squadronIdString = action.substring(beginIndex).trim();
                int squadronId = Integer.valueOf(squadronIdString).intValue();
                Squadron squadron = PWCGContext.getInstance().getSquadronManager().getSquadron(squadronId);
                String squadronDesc = formSquadronDesc(squadron);
                squadronInteltext.setText(squadronDesc);
            }
        }
        catch (Exception e)
        {
            Logger.logException(e);
            ErrorDialog.internalError(e.getMessage());
        }
    }
        
    private String formSquadronDesc(Squadron squadron) throws PWCGException
    {
        StringBuffer intelBuffer = new StringBuffer("");
        formHeader(squadron, intelBuffer); 
        formPersonnel(squadron.getSquadronId(), intelBuffer);
        formAircraftInventory(squadron, intelBuffer);

        intelBuffer.append("\n");          
        
        return intelBuffer.toString();
    }

    private void formPersonnel(int squadronId, StringBuffer intelBuffer) throws PWCGException
    {
        intelBuffer.append("\n  Personnel:\n");          

        SquadronPersonnel squadronPersonnel = campaign.getPersonnelManager().getSquadronPersonnel(squadronId);
        SquadronMembers activeSquadronMembers = SquadronMemberFilter.filterActiveAIAndPlayerAndAces(squadronPersonnel.getSquadronMembersWithAces().getSquadronMemberCollection(), campaign.getDate());
        List<SquadronMember> sortedPilots = activeSquadronMembers.sortPilots(campaign.getDate());
        for (SquadronMember squadronMember : sortedPilots)
        {
            intelBuffer.append("    " + squadronMember.getNameAndRank());          
            intelBuffer.append(".\n");          
        }
    }

    private void formHeader(Squadron squadron, StringBuffer intelBuffer) throws PWCGException
    {
        intelBuffer.append(squadron.determineDisplayName(campaign.getDate()));          
        intelBuffer.append(" at " + squadron.determineCurrentAirfieldName(campaign.getDate()));          
        intelBuffer.append(".\n");
        intelBuffer.append("\n  Callsign: " + squadron.determineCurrentCallsign(campaign.getDate()) + ".\n");
    }

    private void formAircraftInventory(Squadron squadron, StringBuffer intelBuffer) throws PWCGException
    {
        intelBuffer.append("\n  Aircraft on inventory are:\n");        
        Map<Integer, EquippedPlane> aircraftOnInventory = campaign.getEquipmentManager().getEquipmentForSquadron(squadron.getSquadronId()).getActiveEquippedPlanes();
        List<EquippedPlane> sortedAircraftOnInventory = PlaneSorter.sortEquippedPlanesByGoodness(new ArrayList<EquippedPlane>(aircraftOnInventory.values()));
        for (int i = 0; i < sortedAircraftOnInventory.size(); ++i)
        {
            EquippedPlane plane = sortedAircraftOnInventory.get(i);
            intelBuffer.append("    " + plane.getDisplayName() + " (" + plane.getSerialNumber() + ")");
            intelBuffer.append(".\n");          
        }
    }
}
