package pwcg.gui.campaign.skins;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import pwcg.campaign.Campaign;
import pwcg.campaign.squadmember.Ace;
import pwcg.campaign.squadmember.SquadronMember;
import pwcg.core.exception.PWCGException;
import pwcg.core.exception.PWCGUserException;
import pwcg.core.utils.PWCGLogger;
import pwcg.gui.CampaignGuiContextManager;
import pwcg.gui.UiImageResolver;
import pwcg.gui.campaign.home.CampaignRosterBasePanelFactory;
import pwcg.gui.campaign.home.SquadronPilotChalkboardBuilder;
import pwcg.gui.dialogs.ErrorDialog;
import pwcg.gui.utils.ImageResizingPanel;
import pwcg.gui.utils.ImageResizingPanelBuilder;
import pwcg.gui.utils.PWCGButtonFactory;
import pwcg.gui.utils.ToolTipManager;
import pwcg.gui.utils.UIUtils;

public class CampaignSkinManagerPanel extends JPanel implements ActionListener
{
    private static final long serialVersionUID = 1L;
    
    private SkinSessionManager skinSessionManager = new SkinSessionManager();
    private Campaign campaign;
    private JPanel centerPanel;

    public CampaignSkinManagerPanel(Campaign campaign) 
    {
        super();
        this.campaign = campaign;
    }

    public void makePanels() 
    {
        try
        {
            this.add(BorderLayout.WEST, makeLeftPanel());
            this.add(BorderLayout.EAST, createRightPanel());
            SquadronMember referencePlayer = campaign.findReferencePlayer();
            createCenterPanel(referencePlayer);                
        }
        catch (Exception e)
        {
            PWCGLogger.logException(e);
            ErrorDialog.internalError(e.getMessage());
        }
    }

    private JPanel createRightPanel() throws PWCGException, PWCGException
    {
        CampaignRosterBasePanelFactory pilotListDisplay = new SquadronPilotChalkboardBuilder(this);
        
        pilotListDisplay.setExcludeAces(true);
        
        pilotListDisplay.makePilotList();
        
        pilotListDisplay.makeCampaignHomePanels();
        
        return pilotListDisplay.getPilotListPanel();
    }

    private void createCenterPanel(SquadronMember pilot) throws PWCGException
    {
        if (centerPanel != null)
        {
            this.remove(centerPanel);
        }

        skinSessionManager.setPilot(pilot);
        CampaignSkinManagerForPilotPanel campaignSquadronSkinPilotPanel = new CampaignSkinManagerForPilotPanel(campaign, skinSessionManager);
        campaignSquadronSkinPilotPanel.makePanels();
        
        this.add(BorderLayout.CENTER, campaignSquadronSkinPilotPanel);
        
        add(centerPanel, BorderLayout.CENTER);
        
        centerPanel.revalidate();
        centerPanel.repaint();
    }

    private JPanel makeLeftPanel() throws PWCGException 
    {
        String imagePath = UiImageResolver.getImage(campaign, "CampaignSkinLeft.jpg");

        ImageResizingPanel campaignButtonPanel = ImageResizingPanelBuilder.makeImageResizingPanel(imagePath);
        campaignButtonPanel.setLayout(new BorderLayout());
        campaignButtonPanel.setOpaque(true);

        JPanel buttonPanel = new JPanel(new GridLayout(0,1));
        buttonPanel.setOpaque(false);

        makePlainButtons(buttonPanel);

        campaignButtonPanel.add (buttonPanel, BorderLayout.NORTH);

        add (campaignButtonPanel, BorderLayout.WEST);

        return campaignButtonPanel;
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
            
            createCenterPanel(pilot);
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
