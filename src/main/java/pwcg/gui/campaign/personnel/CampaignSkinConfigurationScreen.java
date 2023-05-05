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
import pwcg.campaign.personnel.SquadronMemberFilter;
import pwcg.campaign.personnel.SquadronPersonnel;
import pwcg.campaign.squadmember.Ace;
import pwcg.campaign.squadmember.SquadronMember;
import pwcg.campaign.squadmember.SquadronMembers;
import pwcg.core.exception.PWCGException;
import pwcg.core.exception.PWCGUserException;
import pwcg.core.utils.PWCGLogger;
import pwcg.gui.CampaignGuiContextManager;
import pwcg.gui.ScreenIdentifier;
import pwcg.gui.UiImageResolver;
import pwcg.gui.campaign.home.CampaignHomeContext;
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
    
    private SkinSessionManager skinSessionManager;
    private CampaignSkinConfigurationPilotPanel skinControlPanel;
    private CampaignSkinConfigurationSelectionPanel skinSelectionPanel;

    public CampaignSkinConfigurationScreen() 
    {
        super();
        this.setLayout(new RelativeLayout());
        skinSessionManager = new SkinSessionManager(CampaignHomeContext.getCampaign());
    }

    public void makePanels() throws PWCGException 
    {
        PWCGContext.getInstance().getSkinManager().initialize();
        String imagePath = UiImageResolver.getImage(ScreenIdentifier.CampaignSkinConfigurationScreen);
        this.setThemedImageFromName(CampaignHomeContext.getCampaign().getReferenceService(), imagePath);

        SquadronMember referencePlayer = CampaignHomeContext.getCampaign().findReferencePlayer();
        skinSessionManager.setPilot(referencePlayer);

        makeMainPanelForPilot(referencePlayer);
    }

    private void makeMainPanelForPilot(SquadronMember pilot) throws PWCGException
    {        
        JPanel navPanel  = makeLeftPanel();
        createSkinSelectionPanel();
        createSkinControlPanel(pilot);

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

        JPanel pilotSelectionPanel  = createPilotSelectionPanel();
        campaignButtonPanel.add (pilotSelectionPanel, BorderLayout.CENTER);

        return campaignButtonPanel;
    }

    private void createSkinControlPanel(SquadronMember pilot) throws PWCGException
    {
        skinControlPanel = new CampaignSkinConfigurationPilotPanel(this);
        skinControlPanel.makePanels();
                        
        ImageToDisplaySizer.setDocumentSize(skinControlPanel);
    }

    private void createSkinSelectionPanel() throws PWCGException
    {
        skinSelectionPanel = new CampaignSkinConfigurationSelectionPanel(this);
        skinSelectionPanel.makePanels();

        ImageToDisplaySizer.setDocumentSize(skinSelectionPanel);
    }

    private JPanel createPilotSelectionPanel() throws PWCGException, PWCGException
    {
        List<SquadronMember> pilotsNoAces = makePilotList();
        JPanel squadronPanel = CampaignHomeRightPanelFactory.makeCampaignHomePilotsRightPanel(this, pilotsNoAces);

        return squadronPanel;
    }

    private List<SquadronMember> makePilotList() throws PWCGException 
    {
        Campaign campaign = CampaignHomeContext.getCampaign();
        SquadronMember referencePlayer = campaign.findReferencePlayer();
        SquadronPersonnel squadronPersonnel = campaign.getPersonnelManager().getSquadronPersonnel(referencePlayer.getSquadronId());
        SquadronMembers squadronMembers = SquadronMemberFilter.filterActiveAIAndPlayer(squadronPersonnel.getSquadronMembersWithAces().getSquadronMemberCollection(), campaign.getDate());
        return squadronMembers.sortPilots(campaign.getDate());
    }

    private void makePlainButtons(JPanel buttonPanel) throws PWCGException
    {
        buttonPanel.add(PWCGLabelFactory.makeDummyLabel());

        JButton acceptButton = PWCGButtonFactory.makeTranslucentMenuButton("Accept", "AcceptSkins", "Accept skin assignments", this);
        buttonPanel.add(acceptButton);
        
        JButton cancelButton = PWCGButtonFactory.makeTranslucentMenuButton("Cancel", "CancelSkins", "Do not accept skin assignments", this);
        buttonPanel.add(cancelButton);
     }

    private void showSkinsForPilot(String action) throws PWCGException 
    {
        SquadronMember pilot = UIUtils.getPilotFromAction(CampaignHomeContext.getCampaign(), action);
        if (pilot != null)
        {
            if (pilot instanceof Ace)
            {
                return;
            }

            this.removeAll();

            skinSessionManager.setPilot(pilot);
            skinSessionManager.clearSkinCategorySelectedFlags();
            makeMainPanelForPilot(pilot);
            this.revalidate();
            this.repaint();
        }
    }

    public void actionPerformed(ActionEvent ae)
    {
        try
        {
            String action = ae.getActionCommand();
            
            if (action.startsWith("CampFlowPilot"))
            {
                showSkinsForPilot(action);
            }
            else if (action.equalsIgnoreCase("AcceptSkins"))
            {
                skinSessionManager.finalizeSkinAssignments();                
                CampaignHomeContext.writeCampaign();
                CampaignGuiContextManager.getInstance().popFromContextStack();
            }
            else if (action.equalsIgnoreCase("CancelSkins"))
            {
                CampaignHomeContext.writeCampaign();
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

    public CampaignSkinConfigurationPilotPanel getSkinControlPanel()
    {
        return skinControlPanel;
    }

    public CampaignSkinConfigurationSelectionPanel getSkinSelectionPanel()
    {
        return skinSelectionPanel;
    }
}
