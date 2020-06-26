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
import pwcg.gui.UiImageResolver;
import pwcg.gui.campaign.home.CampaignHome;
import pwcg.gui.campaign.transfer.CampaignTransferPanelSet;
import pwcg.gui.dialogs.ConfirmDialog;
import pwcg.gui.dialogs.ErrorDialog;
import pwcg.gui.maingui.campaigngenerate.NewPilotGeneratorUI;
import pwcg.gui.sound.SoundManager;
import pwcg.gui.utils.ImageResizingPanel;
import pwcg.gui.utils.PWCGButtonFactory;

public class CampaignAdminCoopPilotPanelSet extends ImageResizingPanel implements ActionListener
{
    private static final long serialVersionUID = 1L;
    private Campaign campaign;
    private CampaignAdminCoopPilotPanel coopPersonaInfoPanel;
    private CampaignHome parent;

    public CampaignAdminCoopPilotPanelSet(CampaignHome parent, Campaign campaign)
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
            String imagePath = UiImageResolver.getImageMain("CampaignTable.jpg");
            this.setImage(imagePath);

            this.add(BorderLayout.EAST, makeCoopAdminActionSelectPanel());
            this.add(BorderLayout.CENTER, makeCenterPanel());
            this.add(BorderLayout.WEST, makeNavigatePanel());
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
        JPanel configPanel = new JPanel(new BorderLayout());
        configPanel.setOpaque(false);

        JPanel buttonPanel = new JPanel(new GridLayout(0,1));
        buttonPanel.setOpaque(false);
        

        JLabel label = PWCGButtonFactory.makeMenuLabelLarge("Select Admin Action:");
        buttonPanel.add(label);

        buttonPanel.add(makeActionButton("Add Pilot"));
        buttonPanel.add(makeActionButton("Transfer Pilot"));
        buttonPanel.add(makeActionButton("Retire Pilot"));
        
        add (buttonPanel);

        configPanel.add(buttonPanel, BorderLayout.NORTH);
        
        return configPanel;
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
                NewPilotGeneratorUI addPilotDisplay = new NewPilotGeneratorUI(campaign, parent, this);
                addPilotDisplay.makePanels();        
                CampaignGuiContextManager.getInstance().pushToContextStack(addPilotDisplay);
            }
            else if (action.equalsIgnoreCase("Transfer Pilot"))
            {
                SquadronMember pilot = getSquadronMemberForSelectedPilot();
                if (pilot != null)
                {
                    SoundManager.getInstance().playSound("Typewriter.WAV");
                    CampaignTransferPanelSet transferDisplay = new CampaignTransferPanelSet(null, this, pilot);
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


