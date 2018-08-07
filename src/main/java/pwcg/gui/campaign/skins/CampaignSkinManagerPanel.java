package pwcg.gui.campaign.skins;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import pwcg.campaign.Campaign;
import pwcg.campaign.context.PWCGContextManager;
import pwcg.campaign.squadmember.Ace;
import pwcg.campaign.squadmember.SquadronMember;
import pwcg.core.exception.PWCGException;
import pwcg.core.exception.PWCGUserException;
import pwcg.core.utils.Logger;
import pwcg.gui.PwcgGuiContext;
import pwcg.gui.campaign.CampaignRosterBasePanelFactory;
import pwcg.gui.campaign.CampaignRosterSquadronPanelFactory;
import pwcg.gui.dialogs.ErrorDialog;
import pwcg.gui.utils.ImageResizingPanel;
import pwcg.gui.utils.PWCGButtonFactory;
import pwcg.gui.utils.ToolTipManager;
import pwcg.gui.utils.UIUtils;

public class CampaignSkinManagerPanel extends PwcgGuiContext implements ActionListener
{
    private static final long serialVersionUID = 1L;
    
    private SkinSessionManager skinSessionManager = new SkinSessionManager();


    public CampaignSkinManagerPanel() 
    {
        super();
    }

    public void makePanels() 
    {
        try
        {
            setLeftPanel(makeLeftPanel());
            setRightPanel(createRightPanel());
            Campaign campaign = PWCGContextManager.getInstance().getCampaign();
            SquadronMember player = campaign.getPlayers().get(0);
            createCenterPanel(player);                
        }
        catch (Exception e)
        {
            Logger.logException(e);
            ErrorDialog.internalError(e.getMessage());
        }
    }

    private JPanel createRightPanel() throws PWCGException, PWCGException
    {
        CampaignRosterBasePanelFactory pilotListDisplay = new CampaignRosterSquadronPanelFactory(this);
        
        pilotListDisplay.setExcludeAces(true);
        
        pilotListDisplay.makePilotList();
        
        pilotListDisplay.makeCampaignHomePanels();
        
        return pilotListDisplay.getPilotListPanel();
    }

    private void createCenterPanel(SquadronMember pilot) throws PWCGException
    {
        if (getCenterPanel() != null)
        {
            this.remove(getCenterPanel());
        }

        skinSessionManager.setPilot(pilot);
        CampaignSkinManagerForPilotPanel campaignSquadronSkinPilotPanel = new CampaignSkinManagerForPilotPanel(skinSessionManager);
        campaignSquadronSkinPilotPanel.makePanels();
        
        setCenterPanel(campaignSquadronSkinPilotPanel);
        
        add(getCenterPanel(), BorderLayout.CENTER);
        
        getCenterPanel().revalidate();
        getCenterPanel().repaint();
    }

    private JPanel makeLeftPanel() throws PWCGException 
    {
        String imagePath = getSideImage("CampaignSkinLeft.jpg");

        ImageResizingPanel campaignButtonPanel = new ImageResizingPanel(imagePath);
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
        Campaign campaign = PWCGContextManager.getInstance().getCampaign();
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
                
                finishedWithCampaignScreen();
            }
            else if (action.equalsIgnoreCase("CancelSkins"))
            {
                finishedWithCampaignScreen();
            }
        }
        catch (PWCGUserException ue)
        {
            Logger.logException(ue);
            ErrorDialog.userError(ue.getMessage());
        }
        catch (Exception e)
        {
            Logger.logException(e);
            ErrorDialog.internalError(e.getMessage());
        }
    }
    
 
 }
