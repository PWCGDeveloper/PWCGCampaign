package pwcg.gui.campaign.skins;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import pwcg.campaign.Campaign;
import pwcg.campaign.personnel.SquadronMemberFilter;
import pwcg.campaign.personnel.SquadronPersonnel;
import pwcg.campaign.squadmember.Ace;
import pwcg.campaign.squadmember.SquadronMember;
import pwcg.campaign.squadmember.SquadronMembers;
import pwcg.core.exception.PWCGException;
import pwcg.core.exception.PWCGUserException;
import pwcg.core.utils.PWCGLogger;
import pwcg.gui.CampaignGuiContextManager;
import pwcg.gui.UiImageResolver;
import pwcg.gui.campaign.home.CampaignHomeRightPanelFactory;
import pwcg.gui.dialogs.ErrorDialog;
import pwcg.gui.utils.ImageResizingPanel;
import pwcg.gui.utils.PWCGButtonFactory;
import pwcg.gui.utils.ToolTipManager;
import pwcg.gui.utils.UIUtils;

public class CampaignSkinManagerPanel extends ImageResizingPanel implements ActionListener
{
    private static final long serialVersionUID = 1L;
    
    private SkinSessionManager skinSessionManager = new SkinSessionManager();
    private Campaign campaign;
    private JPanel centerPanel;

    public CampaignSkinManagerPanel(Campaign campaign) 
    {
        super("");
        this.setLayout(new BorderLayout());

        this.campaign = campaign;
    }

    public void makePanels() throws PWCGException 
    {
        String imagePath = UiImageResolver.getImageMain("TableTop.jpg");
        this.setImage(imagePath);

        SquadronMember referencePlayer = campaign.findReferencePlayer();

        this.add(BorderLayout.WEST, makeLeftPanel());
        this.add(BorderLayout.CENTER, createCenterPanel(referencePlayer));
        this.add(BorderLayout.EAST, createRightPanel());
    }

    private JPanel makeLeftPanel() throws PWCGException 
    {
        JPanel campaignButtonPanel = new JPanel(new BorderLayout());
        campaignButtonPanel.setOpaque(false);

        JPanel buttonPanel = new JPanel(new GridLayout(0,1));
        buttonPanel.setOpaque(false);

        makePlainButtons(buttonPanel);

        campaignButtonPanel.add (buttonPanel, BorderLayout.NORTH);

        add (campaignButtonPanel, BorderLayout.WEST);

        return campaignButtonPanel;
    }

    private JPanel createCenterPanel(SquadronMember pilot) throws PWCGException
    {
        if (centerPanel != null)
        {
            this.remove(centerPanel);
        }

        centerPanel = new JPanel(new BorderLayout());
        centerPanel.setOpaque(false);
        
        skinSessionManager.setPilot(pilot);
        CampaignSkinManagerForPilotPanel campaignSquadronSkinPilotPanel = new CampaignSkinManagerForPilotPanel(campaign, skinSessionManager);
        campaignSquadronSkinPilotPanel.makePanels();
        
        centerPanel.add(BorderLayout.CENTER, campaignSquadronSkinPilotPanel);
                
        centerPanel.revalidate();
        centerPanel.repaint();
        return centerPanel;
    }

    private JPanel createRightPanel() throws PWCGException, PWCGException
    {
        List<SquadronMember> pilotsNoAces = makePilotList();
        JPanel squadronPanel = CampaignHomeRightPanelFactory.makeCampaignHomePilotsRightPanel(this, pilotsNoAces);

        return squadronPanel;
    }

    private List<SquadronMember> makePilotList() throws PWCGException 
    {
        SquadronMember referencePlayer = campaign.findReferencePlayer();
        SquadronPersonnel squadronPersonnel = campaign.getPersonnelManager().getSquadronPersonnel(referencePlayer.getSquadronId());
        SquadronMembers squadronMembers = SquadronMemberFilter.filterActiveAIAndPlayer(squadronPersonnel.getSquadronMembersWithAces().getSquadronMemberCollection(), campaign.getDate());
        return squadronMembers.sortPilots(campaign.getDate());
    }

    private void makePlainButtons(JPanel buttonPanel) throws PWCGException
    {
        JLabel spacer = new JLabel("");
        buttonPanel.add(spacer);

        JButton acceptButton = makePlainButton("Accept Skin Assignments", "AcceptSkins", "Accept changes to skin assignments");
        buttonPanel.add(acceptButton);
        
        JButton cancelButton = makePlainButton("Cancel Skin Assignments", "CancelSkins", "Cancel changes to skin assignments");
        buttonPanel.add(cancelButton);
     }

    private JButton makePlainButton(String buttonText, String command, String toolTiptext) throws PWCGException
    {
        JButton button = PWCGButtonFactory.makeMenuButton(buttonText, command, this);
        
        ToolTipManager.setToolTip(button, toolTiptext);

        return button;
    }

    private void showSkinsForPilot(String action) throws PWCGException 
    {
        SquadronMember pilot = UIUtils.getPilotFromAction(campaign, action);
        if (pilot != null)
        {
            if (pilot instanceof Ace)
            {
                return;
            }
            
            this.add(BorderLayout.CENTER, createCenterPanel(pilot));
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
    
 
 }
