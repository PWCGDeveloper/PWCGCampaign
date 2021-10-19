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
import pwcg.campaign.api.Side;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.personnel.SquadronMemberFilter;
import pwcg.campaign.personnel.SquadronPersonnel;
import pwcg.campaign.plane.EquippedPlane;
import pwcg.campaign.plane.PlaneSorter;
import pwcg.campaign.plane.PwcgRole;
import pwcg.campaign.squadmember.SquadronMember;
import pwcg.campaign.squadmember.SquadronMembers;
import pwcg.campaign.squadron.Squadron;
import pwcg.campaign.squadron.SquadronViability;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.PWCGLogger;
import pwcg.gui.ScreenIdentifier;
import pwcg.gui.UiImageResolver;
import pwcg.gui.dialogs.ErrorDialog;
import pwcg.gui.dialogs.PWCGMonitorFonts;
import pwcg.gui.utils.ImageResizingPanel;
import pwcg.gui.utils.PWCGButtonFactory;
import pwcg.gui.utils.PWCGLabelFactory;
import pwcg.gui.utils.PwcgBorderFactory;

public abstract class CampaignIntelligenceBase extends JPanel implements ActionListener
{
	private static final long serialVersionUID = 1L;
	
    protected StringBuffer intelHeaderBuffer = new StringBuffer("");
    protected Campaign campaign;
	protected SquadronMember referencePlayer;
    protected JPanel squadronsByRoleContainer;
    protected JTextArea squadronIntelText;
    
	public CampaignIntelligenceBase() throws PWCGException  
	{
        super();
        this.setLayout(new BorderLayout());
        this.setOpaque(false);

        this.campaign = PWCGContext.getInstance().getCampaign();
		this.referencePlayer = campaign.findReferencePlayer();
	}    

    protected void makePanel(Side side) throws PWCGException
    {
        try
        {
            formSquadronsByRole(side);
            
            formSquadronIntelText();
        }
        catch (Exception e)
        {
            PWCGLogger.logException(e);
            ErrorDialog.internalError(e.getMessage());
        }       
    }

    protected void formSquadronsByRole(Side side) throws PWCGException 
    {
        ImageResizingPanel squeezerPanel = new ImageResizingPanel("");
        squeezerPanel.setOpaque(false);
        squeezerPanel.setLayout(new BorderLayout());
        String imagePath = UiImageResolver.getImage(ScreenIdentifier.Document);
        squeezerPanel.setImageFromName(imagePath);
        this.setBorder(PwcgBorderFactory.createStandardDocumentBorder());

        squadronsByRoleContainer = new ImageResizingPanel("");
        squadronsByRoleContainer.setOpaque(false);
        squadronsByRoleContainer.setLayout(new GridLayout(0,1));        
        
        squeezerPanel.setOpaque(false);
        squeezerPanel.add(squadronsByRoleContainer, BorderLayout.NORTH);
        this.add(squeezerPanel, BorderLayout.WEST);

        formSquadronDescForRole(PwcgRole.ROLE_FIGHTER, side);
        formSquadronDescForRole(PwcgRole.ROLE_ATTACK, side);
        formSquadronDescForRole(PwcgRole.ROLE_DIVE_BOMB, side);
        formSquadronDescForRole(PwcgRole.ROLE_RECON, side);
        formSquadronDescForRole(PwcgRole.ROLE_BOMB, side);
        formSquadronDescForRole(PwcgRole.ROLE_STRATEGIC_INTERCEPT, side);
        formSquadronDescForRole(PwcgRole.ROLE_STRAT_BOMB, side);
        formSquadronDescForRole(PwcgRole.ROLE_SEA_PLANE, side);
        formSquadronDescForRole(PwcgRole.ROLE_TRANSPORT, side);
    }    

    private void formSquadronIntelText() throws PWCGException
    {
        ImageResizingPanel intelTextPanel = new ImageResizingPanel("");
        intelTextPanel.setOpaque(false);
        intelTextPanel.setLayout(new BorderLayout());
        String imagePath = UiImageResolver.getImage(ScreenIdentifier.Document);
        intelTextPanel.setImageFromName(imagePath);
        this.setBorder(PwcgBorderFactory.createStandardDocumentBorder());

        Font font = PWCGMonitorFonts.getTypewriterFont();
        squadronIntelText = new JTextArea();
        squadronIntelText.setFont(font);
        squadronIntelText.setOpaque(false);
        squadronIntelText.setLineWrap(false);
        squadronIntelText.setWrapStyleWord(true);
        squadronIntelText.setText("");
        
        intelTextPanel.add(squadronIntelText);
        
        this.add(intelTextPanel, BorderLayout.CENTER);
    }

    private void formSquadronDescForRole(PwcgRole role, Side side) throws PWCGException
    {
        List<Squadron> squadrons = getSquadronsForIntel(role, side);
        if (squadrons.size() == 0)
        {
            return;
        }

        JLabel headerLabel = PWCGLabelFactory.makePaperLabelLarge(role.getRoleDescription() + " Squadrons \n");
        squadronsByRoleContainer.add(headerLabel);

        for (Squadron squadron : squadrons)
        {
            if (SquadronViability.isSquadronViable(squadron, campaign))
            {
                JButton squadronSelectButton = PWCGButtonFactory.makePaperButton(squadron.determineDisplayName(campaign.getDate()), "SquadronSelected:" + squadron.getSquadronId(), "Detailed information for squadron", this);
                squadronsByRoleContainer.add(squadronSelectButton);                
            }
            else
            {
                JButton squadronSelectButton = PWCGButtonFactory.makeRedPaperButton(squadron.determineDisplayName(campaign.getDate()), "SquadronSelected:" + squadron.getSquadronId(), "Detailed information for squadron", this);
                squadronsByRoleContainer.add(squadronSelectButton);
            }
        }
    }

    private List<Squadron> getSquadronsForIntel(PwcgRole role, Side side) throws PWCGException
    {
        List<Squadron> squadronsWithPrimaryRole = new ArrayList<Squadron>();
        List<PwcgRole> acceptableRoles = new ArrayList<PwcgRole>();
        acceptableRoles.add(role);

        List<Squadron> squadronsForMap = PWCGContext.getInstance().getSquadronManager().getActiveSquadronsForCurrentMap(campaign.getDate());
        for (Squadron squadron : squadronsForMap)
        {
            if (squadron.determineSquadronPrimaryRoleCategory(campaign.getDate()) == role.getRoleCategory())
            {
                if (squadron.determineSide() == side)
                {
                    squadronsWithPrimaryRole.add(squadron);
                }
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
                setSquadronIntelText(squadronId);
            }
        }
        catch (Exception e)
        {
            PWCGLogger.logException(e);
            ErrorDialog.internalError(e.getMessage());
        }
    }

    private void setSquadronIntelText(int squadronId) throws PWCGException
    {
        Squadron squadron = PWCGContext.getInstance().getSquadronManager().getSquadron(squadronId);
        String squadronDesc = formSquadronDesc(squadron);
        squadronIntelText.setText(squadronDesc);
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
            intelBuffer.append("    " + plane.getDisplayName() + " (" + plane.getDisplayMarkings() + ")");
            intelBuffer.append(".\n");          
        }
    }
}
