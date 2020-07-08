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
import pwcg.gui.utils.SpacerPanelFactory;

public class CampaignCoopAdminScreen extends ImageResizingPanel implements ActionListener
{
    private static final long serialVersionUID = 1L;
    private Campaign campaign;
    private CampaignAdminCoopPilotPanel coopPersonaInfoPanel;
    private CampaignHomeScreen parent;

    public CampaignCoopAdminScreen(CampaignHomeScreen parent, Campaign campaign)
    {
        super("");
        this.setLayout(new BorderLayout());
        this.setOpaque(false);

        this.parent = parent;
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
            this.add(BorderLayout.EAST, makeCoopAdminActionSelectPanel());
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
        JPanel navPanel = new JPanel(new BorderLayout());
        navPanel.setOpaque(false);

        JPanel buttonPanel = new JPanel(new GridLayout(0,1));
        buttonPanel.setOpaque(false);

        JButton acceptButton = PWCGButtonFactory.makeMenuButton("Finished", "Finished", this);
        buttonPanel.add(acceptButton);

        navPanel.add(buttonPanel, BorderLayout.NORTH);
        
        return navPanel;
    }

    public JPanel makeCoopAdminActionSelectPanel() throws PWCGException  
    {
        JPanel actionSelectionPanel = new JPanel(new BorderLayout());
        actionSelectionPanel.setOpaque(false);

        JPanel buttonPanel = new JPanel(new GridLayout(0,1));
        buttonPanel.setOpaque(false);
        

        JLabel label = PWCGButtonFactory.makeMenuLabelLarge("Select Admin Action:");
        buttonPanel.add(label);

        buttonPanel.add(makeActionButton("Add Pilot"));
        buttonPanel.add(makeActionButton("Transfer Pilot"));
        buttonPanel.add(makeActionButton("Retire Pilot"));
        
        add (buttonPanel);

        actionSelectionPanel.add(buttonPanel, BorderLayout.NORTH);

        if (PWCGMonitorSupport.getFrameWidth() == MonitorSize.FRAME_LARGE)
        {
            JPanel spacePanel = SpacerPanelFactory.makeDocumentSpacerPanel(2000);
            
            JPanel actionPanel = new JPanel(new BorderLayout());
            actionPanel.setOpaque(false);
            actionPanel.add(actionSelectionPanel, BorderLayout.CENTER);
            actionPanel.add(spacePanel, BorderLayout.WEST);
            
            return actionPanel;
        }
        else
        {
            return actionSelectionPanel;
        }

    }

    private JButton makeActionButton(String buttonText) throws PWCGException 
    {
        JButton button = PWCGButtonFactory.makeMenuButton(buttonText, buttonText, this);
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
                CampaignNewPilotScreen addPilotDisplay = new CampaignNewPilotScreen(campaign, parent, this);
                addPilotDisplay.makePanels();        
                CampaignGuiContextManager.getInstance().pushToContextStack(addPilotDisplay);
            }
            else if (action.equalsIgnoreCase("Transfer Pilot"))
            {
                SquadronMember pilot = getSquadronMemberForSelectedPilot();
                if (pilot != null)
                {
                    SoundManager.getInstance().playSound("Typewriter.WAV");
                    CampaignTransferScreen transferDisplay = new CampaignTransferScreen(null, this, pilot);
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
                        this.makePanels();
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
}


