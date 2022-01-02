package pwcg.gui.campaign.personnel;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JPanel;

import edu.cmu.relativelayout.Binding;
import edu.cmu.relativelayout.BindingFactory;
import edu.cmu.relativelayout.RelativeConstraints;
import edu.cmu.relativelayout.RelativeLayout;
import pwcg.campaign.Campaign;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.crewmember.CrewMember;
import pwcg.campaign.crewmember.CrewMembers;
import pwcg.campaign.crewmember.TankAce;
import pwcg.campaign.personnel.CompanyPersonnel;
import pwcg.campaign.personnel.CrewMemberFilter;
import pwcg.core.exception.PWCGException;
import pwcg.core.exception.PWCGUserException;
import pwcg.core.utils.PWCGLogger;
import pwcg.gui.CampaignGuiContextManager;
import pwcg.gui.ScreenIdentifier;
import pwcg.gui.UiImageResolver;
import pwcg.gui.campaign.home.CampaignHomeRightPanelFactory;
import pwcg.gui.dialogs.ErrorDialog;
import pwcg.gui.utils.ImageResizingPanel;
import pwcg.gui.utils.ImageToDisplaySizer;
import pwcg.gui.utils.PWCGButtonFactory;
import pwcg.gui.utils.PWCGLabelFactory;
import pwcg.gui.utils.UIUtils;

public class CampaignSkinConfigurationScreen extends ImageResizingPanel implements ActionListener
{
    private static final long serialVersionUID = 1L;
    
    private SkinSessionManager skinSessionManager = new SkinSessionManager();
    private Campaign campaign;
    private CampaignSkinConfigurationCrewMemberPanel skinControlPanel;
    private CampaignSkinConfigurationSelectionPanel skinSelectionPanel;

    public CampaignSkinConfigurationScreen(Campaign campaign) 
    {
        super("");
        this.setLayout(new RelativeLayout());

        this.campaign = campaign;
    }

    public void makePanels() throws PWCGException 
    {
        PWCGContext.getInstance().getSkinManager().initialize();
        String imagePath = UiImageResolver.getImage(ScreenIdentifier.CampaignSkinConfigurationScreen);
        this.setImageFromName(imagePath);

        CrewMember referencePlayer = campaign.findReferencePlayer();
        skinSessionManager.setCrewMember(referencePlayer);

        makeMainPanelForCrewMember(referencePlayer);
    }

    private void makeMainPanelForCrewMember(CrewMember crewMember) throws PWCGException
    {        
        JPanel navPanel  = makeLeftPanel();
        createSkinSelectionPanel();
        createSkinControlPanel(crewMember);

        Binding navPanelBinding = BindingFactory.getBindingFactory().directLeftEdge();
        RelativeConstraints navPanelConstraints = new RelativeConstraints();
        navPanelConstraints.addBinding(navPanelBinding);
        this.add(navPanel, navPanelConstraints);
        
        Binding centerPanelBinding = BindingFactory.getBindingFactory().directlyRightOf(navPanel);
        RelativeConstraints centerPanelConstraints = new RelativeConstraints();
        centerPanelConstraints.addBinding(centerPanelBinding);
        this.add(skinControlPanel, centerPanelConstraints);

        Binding skinListPanelBinding = BindingFactory.getBindingFactory().directlyRightOf(skinControlPanel);
        RelativeConstraints skinListPanelConstraints = new RelativeConstraints();
        skinListPanelConstraints.addBinding(skinListPanelBinding);
        this.add(skinSelectionPanel, skinListPanelConstraints);
    }

    private JPanel makeLeftPanel() throws PWCGException 
    {
        JPanel campaignButtonPanel = new JPanel(new BorderLayout());
        campaignButtonPanel.setOpaque(false);

        JPanel buttonPanel = new JPanel(new GridLayout(0,1));
        buttonPanel.setOpaque(false);

        makePlainButtons(buttonPanel);
        campaignButtonPanel.add (buttonPanel, BorderLayout.NORTH);

        JPanel crewMemberSelectionPanel  = createCrewMemberSelectionPanel();
        campaignButtonPanel.add (crewMemberSelectionPanel, BorderLayout.CENTER);

        return campaignButtonPanel;
    }

    private void createSkinControlPanel(CrewMember crewMember) throws PWCGException
    {
        skinControlPanel = new CampaignSkinConfigurationCrewMemberPanel(campaign, this);
        skinControlPanel.makePanels();
                        
        ImageToDisplaySizer.setDocumentSize(skinControlPanel);
    }

    private void createSkinSelectionPanel() throws PWCGException
    {
        skinSelectionPanel = new CampaignSkinConfigurationSelectionPanel(this);
        skinSelectionPanel.makePanels();

        ImageToDisplaySizer.setDocumentSize(skinSelectionPanel);
    }

    private JPanel createCrewMemberSelectionPanel() throws PWCGException, PWCGException
    {
        List<CrewMember> crewMembersNoAces = makeCrewMemberList();
        JPanel squadronPanel = CampaignHomeRightPanelFactory.makeCampaignHomeCrewMembersRightPanel(this, crewMembersNoAces);

        return squadronPanel;
    }

    private List<CrewMember> makeCrewMemberList() throws PWCGException 
    {
        CrewMember referencePlayer = campaign.findReferencePlayer();
        CompanyPersonnel squadronPersonnel = campaign.getPersonnelManager().getCompanyPersonnel(referencePlayer.getCompanyId());
        CrewMembers squadronMembers = CrewMemberFilter.filterActiveAIAndPlayer(squadronPersonnel.getCrewMembersWithAces().getCrewMemberCollection(), campaign.getDate());
        return squadronMembers.sortCrewMembers(campaign.getDate());
    }

    private void makePlainButtons(JPanel buttonPanel) throws PWCGException
    {
        buttonPanel.add(PWCGLabelFactory.makeDummyLabel());

        JButton acceptButton = PWCGButtonFactory.makeTranslucentMenuButton("Accept", "AcceptSkins", "Accept skin assignments", this);
        buttonPanel.add(acceptButton);
        
        JButton cancelButton = PWCGButtonFactory.makeTranslucentMenuButton("Cancel", "CancelSkins", "Do not accept skin assignments", this);
        buttonPanel.add(cancelButton);
     }

    private void showSkinsForCrewMember(String action) throws PWCGException 
    {
        CrewMember crewMember = UIUtils.getCrewMemberFromAction(campaign, action);
        if (crewMember != null)
        {
            if (crewMember instanceof TankAce)
            {
                return;
            }

            this.removeAll();

            skinSessionManager.setCrewMember(crewMember);
            skinSessionManager.clearSkinCategorySelectedFlags();
            makeMainPanelForCrewMember(crewMember);
            this.revalidate();
            this.repaint();
        }
    }

    public void actionPerformed(ActionEvent ae)
    {
        try
        {
            String action = ae.getActionCommand();
            
            if (action.startsWith("CampFlowCrewMember"))
            {
                showSkinsForCrewMember(action);
            }
            else if (action.equalsIgnoreCase("AcceptSkins"))
            {
                skinSessionManager.finalizeSkinAssignments();                
                campaign.write();                
                CampaignGuiContextManager.getInstance().popFromContextStack();
            }
            else if (action.equalsIgnoreCase("CancelSkins"))
            {
                campaign.write();                
                CampaignGuiContextManager.getInstance().popFromContextStack();
            }
        }
        catch (PWCGUserException ue)
        {
            PWCGLogger.logException(ue);
            ErrorDialog.userError(ue.getMessage());
        }
        catch (Exception e)
        {
            PWCGLogger.logException(e);
            ErrorDialog.internalError(e.getMessage());
        }
    }

    public SkinSessionManager getSkinSessionManager()
    {
        return skinSessionManager;
    }

    public CampaignSkinConfigurationCrewMemberPanel getSkinControlPanel()
    {
        return skinControlPanel;
    }

    public CampaignSkinConfigurationSelectionPanel getSkinSelectionPanel()
    {
        return skinSelectionPanel;
    }
}
