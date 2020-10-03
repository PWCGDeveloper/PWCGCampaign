package pwcg.gui.campaign.coop;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import pwcg.campaign.Campaign;
import pwcg.campaign.squadmember.SquadronMember;
import pwcg.campaign.squadmember.SquadronMemberStatus;
import pwcg.coop.model.CoopDisplayRecord;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.PWCGLogger;
import pwcg.gui.CampaignGuiContextManager;
import pwcg.gui.ScreenIdentifier;
import pwcg.gui.UiImageResolver;
import pwcg.gui.campaign.home.CampaignHomeScreen;
import pwcg.gui.campaign.transfer.CampaignTransferScreen;
import pwcg.gui.dialogs.ConfirmDialog;
import pwcg.gui.dialogs.ErrorDialog;
import pwcg.gui.dialogs.PWCGMonitorSupport;
import pwcg.gui.dialogs.PWCGMonitorSupport.MonitorSize;
import pwcg.gui.maingui.campaigngenerate.CampaignNewPilotScreen;
import pwcg.gui.sound.SoundManager;
import pwcg.gui.utils.ImageResizingPanel;
import pwcg.gui.utils.PWCGButtonFactory;

public class CampaignCoopAdminScreen extends ImageResizingPanel implements ActionListener
{
    private static final long serialVersionUID = 1L;
    private Campaign campaign;
    private CampaignAdminCoopPilotPanel coopPersonaInfoPanel;
    private CampaignHomeScreen campaignHome;

    public CampaignCoopAdminScreen(CampaignHomeScreen campaignHome, Campaign campaign)
    {
        super("");
        this.setLayout(new BorderLayout());
        this.setOpaque(false);

        this.campaignHome = campaignHome;
        this.campaign = campaign;
    }
    
    public void makePanels() 
    {
        try
        {        	
            String imagePath = UiImageResolver.getImage(ScreenIdentifier.CampaignCoopAdminScreen);
            this.setImage(imagePath);

            this.add(BorderLayout.WEST, makeNavigatePanel());
            this.add(BorderLayout.CENTER, makeCenterPanel());
        }
        catch (Throwable e)
        {
            PWCGLogger.logException(e);
            ErrorDialog.internalError(e.getMessage());
        }
    }

	public JPanel makeCenterPanel()  
    {       
        coopPersonaInfoPanel = new CampaignAdminCoopPilotPanel(campaign);
        coopPersonaInfoPanel.makePanels();                
        return coopPersonaInfoPanel;
    }

    public JPanel makeNavigatePanel() throws PWCGException  
    {
        JPanel buttonPanel = new JPanel(new GridLayout(0,1));
        buttonPanel.setOpaque(false);

        JButton finished = PWCGButtonFactory.makeTranslucentMenuButton("Finished", "Finished", "Finished with coop administration", this);
        buttonPanel.add(finished);

        for (int i = 0; i < 3; ++i)
        {
            JLabel space1 = PWCGButtonFactory.makeDummy();
            buttonPanel.add(space1);
        }
        
        JPanel actionPanel = makeCoopAdminActionSelectPanel();

        JPanel navPanel = new JPanel(new BorderLayout());
        navPanel.setOpaque(false);
        navPanel.add(buttonPanel, BorderLayout.NORTH);
        navPanel.add(actionPanel, BorderLayout.CENTER);

        return navPanel;
    }

    public JPanel makeCoopAdminActionSelectPanel() throws PWCGException  
    {

        JPanel buttonPanel = new JPanel(new GridLayout(0,1));
        buttonPanel.setOpaque(false);
        
        JLabel label = PWCGButtonFactory.makeMenuLabelLarge("Select Admin Action:");
        buttonPanel.add(label);

        buttonPanel.add(makeActionButton("Add Pilot", "Add a coop persona to the campaign"));
        buttonPanel.add(makeActionButton("Transfer Pilot", "Transfer a coop persona to a new squadron"));
        buttonPanel.add(makeActionButton("Retire Pilot", "Retire a coop persona. Cannot be undone"));
        
        add (buttonPanel);

        JPanel actionSelectionPanel = new JPanel(new BorderLayout());
        actionSelectionPanel.setOpaque(false);
        actionSelectionPanel.add(buttonPanel, BorderLayout.NORTH);

        if (PWCGMonitorSupport.getFrameWidth() == MonitorSize.FRAME_LARGE)
        {
            JPanel actionPanel = new JPanel(new BorderLayout());
            actionPanel.setOpaque(false);
            actionPanel.add(actionSelectionPanel, BorderLayout.CENTER);
            
            return actionPanel;
        }
        else
        {
            return actionSelectionPanel;
        }

    }

    private JButton makeActionButton(String buttonText, String tooltip) throws PWCGException 
    {
        JButton button = PWCGButtonFactory.makeTranslucentMenuButton(buttonText, buttonText, tooltip, this);
        return button;
    }

    public void actionPerformed(ActionEvent ae)
    {
        try
        {
            String action = ae.getActionCommand();
            if (action.equalsIgnoreCase("Finished"))
            {
                CampaignGuiContextManager.getInstance().popFromContextStack();
                return;
            }

            if (action.equalsIgnoreCase("Add Pilot"))
            {
                SoundManager.getInstance().playSound("Typewriter.WAV");
                CampaignNewPilotScreen addPilotDisplay = new CampaignNewPilotScreen(campaign, campaignHome, this);
                addPilotDisplay.makePanels();        
                CampaignGuiContextManager.getInstance().pushToContextStack(addPilotDisplay);
            }
            else if (action.equalsIgnoreCase("Transfer Pilot"))
            {
                SquadronMember pilot = getSquadronMemberForSelectedPilot();
                if (pilot != null)
                {
                    SoundManager.getInstance().playSound("Typewriter.WAV");
                    CampaignTransferScreen transferDisplay = new CampaignTransferScreen(campaignHome, pilot);
                    transferDisplay.makePanels();        
                    CampaignGuiContextManager.getInstance().pushToContextStack(transferDisplay);
                }
            }
            else if (action.contains("Retire Pilot"))
            {
                SquadronMember pilot = getSquadronMemberForSelectedPilot();
                if (pilot != null)
                {
                    int result = ConfirmDialog.areYouSure("Confirm Retire " + pilot.getNameAndRank() + ".  Cannot be reversed");
                    if (result == JOptionPane.YES_OPTION)
                    {
                        SoundManager.getInstance().playSound("Typewriter.WAV");
                        pilot.setPilotActiveStatus(SquadronMemberStatus.STATUS_RETIRED, campaign.getDate(), null);
                        campaign.write();
                        
                        redisplayUpdatedCoopAdminScreen(campaignHome);
                    }
                }
            }
        }
        catch (Throwable e)
        {
            PWCGLogger.logException(e);
            ErrorDialog.internalError(e.getMessage());
        }
    }

    public static  void redisplayUpdatedCoopAdminScreen(CampaignHomeScreen campaignHome) throws PWCGException
    {
        CampaignGuiContextManager.getInstance().backToCampaignHome();
        CampaignCoopAdminScreen adminCoopPilotDisplay = new CampaignCoopAdminScreen(campaignHome, campaignHome.getCampaign());
        adminCoopPilotDisplay.makePanels();
        CampaignGuiContextManager.getInstance().pushToContextStack(adminCoopPilotDisplay);
    }
    
    private SquadronMember getSquadronMemberForSelectedPilot() throws PWCGException
    {
        CoopDisplayRecord coopDisplayRecord = coopPersonaInfoPanel.getSelectedPilot();
        if (coopDisplayRecord != null)
        {
            SquadronMember pilot = campaign.getPersonnelManager().getAnyCampaignMember(coopDisplayRecord.getPilotSerialNumber());
            return pilot;
        }
        
        return null;
    }
    
    public void refresh()
    {
        this.revalidate();
        this.repaint();
    }
}


