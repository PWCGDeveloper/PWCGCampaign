package pwcg.gui.maingui.campaigngenerate;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import pwcg.campaign.ArmedService;
import pwcg.campaign.Campaign;
import pwcg.campaign.CampaignMode;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.factory.CampaignModeFactory;
import pwcg.campaign.squadmember.ISquadronMemberReplacer;
import pwcg.campaign.squadmember.SquadronMember;
import pwcg.core.exception.PWCGException;
import pwcg.core.exception.PWCGUserException;
import pwcg.core.utils.PWCGLogger;
import pwcg.gui.CampaignGuiContextManager;
import pwcg.gui.ScreenIdentifier;
import pwcg.gui.UiImageResolver;
import pwcg.gui.campaign.home.CampaignHomeScreen;
import pwcg.gui.campaign.personnel.CampaignCoopAdminScreen;
import pwcg.gui.dialogs.ErrorDialog;
import pwcg.gui.utils.ImageResizingPanel;
import pwcg.gui.utils.PWCGButtonFactory;
import pwcg.gui.utils.SpacerPanelFactory;

public class CampaignNewPilotScreen extends ImageResizingPanel implements ActionListener
{    
    private static final long serialVersionUID = 1L;

    private Campaign campaign;
    private JButton newPilotCreateButton;
    private NewPilotDataEntryGUI dataEntry;
    private CampaignHomeScreen campaignHome = null;
    private CampaignCoopAdminScreen alternateParent = null;
    
    private NewPilotGeneratorDO newPilotGeneratorDO = new NewPilotGeneratorDO();
    private NewPilotState newPilotState;

    public CampaignNewPilotScreen(Campaign campaign, CampaignHomeScreen parent, CampaignCoopAdminScreen alternateParent)
    {
        super("");
        this.setLayout(new BorderLayout());
        this.setOpaque(false);
        
        this.campaign = campaign;
        this.campaignHome = parent;
        this.alternateParent = alternateParent;        
    }

    public void makePanels() throws PWCGException 
    {
        try
        {
            String imagePath = UiImageResolver.getImage(ScreenIdentifier.CampaignNewPilotScreen);
            this.setImage(imagePath);

            this.add(BorderLayout.WEST, makeButtonPanel());
            this.add(BorderLayout.CENTER, SpacerPanelFactory.makeSpacerPercentPanel(20));
            this.add(BorderLayout.EAST, makeServicePanel());
        }
        catch (Throwable e)
        {
            PWCGLogger.logException(e);
            ErrorDialog.internalError(e.getMessage());
        }
    }

    private JPanel makeServicePanel() throws PWCGException
    {        
        JPanel servicesPanel = new JPanel(new BorderLayout());
        servicesPanel.setLayout(new BorderLayout());
        servicesPanel.setOpaque(false);

        PilotGenerationInfoGUI campaignChooseServiceGUI = new PilotGenerationInfoGUI(this, campaign);
        campaignChooseServiceGUI.makeServiceSelectionPanel();

        servicesPanel.add(campaignChooseServiceGUI, BorderLayout.NORTH);
     
        return servicesPanel;
    }

    private JPanel makeButtonPanel() throws PWCGException
    {
        JPanel configPanel = new JPanel(new BorderLayout());
        configPanel.setLayout(new BorderLayout());
        configPanel.setOpaque(false);
        
        JPanel buttonPanel = new JPanel(new GridLayout(6,1));
        buttonPanel.setOpaque(false);

        newPilotCreateButton = PWCGButtonFactory.makeTranslucentMenuButton("Create Pilot", "Create Pilot", "Add this pilot to the campaign", this);
        buttonPanel.add(newPilotCreateButton);
        newPilotCreateButton.setEnabled(false);
        
        JLabel dummyLabel3 = new JLabel("     ");       
        dummyLabel3.setOpaque(false);
        buttonPanel.add(dummyLabel3);
        
        JButton cancelChanges = PWCGButtonFactory.makeTranslucentMenuButton("Cancel", "Cancel", "Cancel the addition of a new pilot to this campaign", this);
        buttonPanel.add(cancelChanges);

        configPanel.add(buttonPanel, BorderLayout.NORTH);
     
        return configPanel;
    }

    @Override
    public void actionPerformed(ActionEvent ae)
    {        
        try
        {
            String action = ae.getActionCommand();
            
            if (action.equalsIgnoreCase("Cancel"))
            {
                CampaignGuiContextManager.getInstance().popFromContextStack();
            }
            else if (action.equalsIgnoreCase("Create Pilot"))
            {
                createPilot();
                if (alternateParent != null)
                {
                    CampaignCoopAdminScreen.redisplayUpdatedCoopAdminScreen(campaignHome);
                }
                else if (campaignHome != null)
                {
                    campaignHome.createCampaignHomeContext();
                    CampaignGuiContextManager.getInstance().popFromContextStack();
                }
            }
        }
        catch (Exception e)
        {
            PWCGLogger.logException(e);
            ErrorDialog.internalError(e.getMessage());
        }
    }

    private void createPilot() throws PWCGUserException, Exception
    {
        String playerName = newPilotGeneratorDO.getPlayerPilotName();
        String squadronName = newPilotGeneratorDO.getSquadName();
        String rank = newPilotGeneratorDO.getRank();
        String coopuser = newPilotGeneratorDO.getCoopUser();

        ISquadronMemberReplacer squadronMemberReplacer = CampaignModeFactory.makeSquadronMemberReplacer(campaign);
        SquadronMember newSquadronMember = squadronMemberReplacer.createPersona(playerName, rank, squadronName, coopuser);
        
        campaign.write();        
        campaign.open(campaign.getCampaignData().getName());
        PWCGContext.getInstance().setCampaign(campaign);
        if (campaign.getCampaignData().getCampaignMode() != CampaignMode.CAMPAIGN_MODE_SINGLE)
        {
            newPilotGeneratorDO.createCoopUserAndPersona(campaign, newSquadronMember);
        }
    }

    public void changeService(ArmedService service) throws PWCGException
    {
        newPilotGeneratorDO.setService(service);
        
        if (dataEntry != null)
        {
            this.remove(dataEntry);
        }
        
        dataEntry = new NewPilotDataEntryGUI(campaign, this);
        dataEntry.makePanels();
        dataEntry.evaluateUI();
        
        this.add(dataEntry, BorderLayout.CENTER);
        
        CampaignGuiContextManager.getInstance().refreshCurrentContext(this);
    }

    public NewPilotGeneratorDO getNewPilotGeneratorDO()
    {
        return newPilotGeneratorDO;
    }

    public NewPilotState getNewPilotState()
    {
        return newPilotState;
    }

    public void evaluateCompletionState() throws PWCGException
    {
        newPilotCreateButton.setEnabled(false);
        createNewPilotState();
        if (newPilotState.isComplete())
        {
            newPilotCreateButton.setEnabled(true);
        }
    }

    private void createNewPilotState() throws PWCGException
    {
        if (newPilotState == null)
        {
            newPilotState = new NewPilotState(campaign, newPilotGeneratorDO);
            newPilotState.buildStateStack();
        }
    }
}
