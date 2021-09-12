package pwcg.gui.maingui.campaigngenerate;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;

import pwcg.campaign.ArmedService;
import pwcg.campaign.Campaign;
import pwcg.campaign.squadmember.PlayerPilotBuilder;
import pwcg.core.exception.PWCGException;
import pwcg.core.exception.PWCGUserException;
import pwcg.core.utils.PWCGLogger;
import pwcg.gui.CampaignGuiContextManager;
import pwcg.gui.IRefreshableParentUI;
import pwcg.gui.ScreenIdentifier;
import pwcg.gui.UiImageResolver;
import pwcg.gui.dialogs.ErrorDialog;
import pwcg.gui.utils.ImageResizingPanel;
import pwcg.gui.utils.ButtonFactory;
import pwcg.gui.utils.SpacerPanelFactory;

public class CampaignNewPilotScreen extends ImageResizingPanel implements ActionListener
{    
    private static final long serialVersionUID = 1L;

    private Campaign campaign;
    private Button newPilotCreateButton;
    private NewPilotDataEntryGUI dataEntry;
    
    private NewPilotGeneratorDO newPilotGeneratorDO = new NewPilotGeneratorDO();
    private NewPilotState newPilotState;
    private IRefreshableParentUI parentScreen;

    public CampaignNewPilotScreen(Campaign campaign, IRefreshableParentUI parentScreen)
    {
        super("");
        this.setLayout(new BorderLayout());
        this.setOpaque(false);
        
        this.campaign = campaign;
        this.parentScreen = parentScreen;        
    }

    public void makePanels() throws PWCGException 
    {
        try
        {
            String imagePath = UiImageResolver.getImage(ScreenIdentifier.CampaignNewPilotScreen);
            this.setImageFromName(imagePath);

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

    private Pane makeServicePanel() throws PWCGException
    {        
        Pane servicesPanel = new Pane(new BorderLayout());
        servicesPanel.setLayout(new BorderLayout());
        servicesPanel.setOpaque(false);

        PilotGenerationInfoGUI campaignChooseServiceGUI = new PilotGenerationInfoGUI(this, campaign);
        campaignChooseServiceGUI.makeServiceSelectionPanel();

        servicesPanel.add(campaignChooseServiceGUI, BorderLayout.NORTH);
     
        return servicesPanel;
    }

    private Pane makeButtonPanel() throws PWCGException
    {
        Pane configPanel = new Pane(new BorderLayout());
        configPanel.setLayout(new BorderLayout());
        configPanel.setOpaque(false);
        
        Pane buttonPanel = new Pane(new GridLayout(6,1));
        buttonPanel.setOpaque(false);

        newPilotCreateButton = ButtonFactory.makeTranslucentMenuButton("Create Pilot", "Create Pilot", "Add this pilot to the campaign", this);
        buttonPanel.add(newPilotCreateButton);
        newPilotCreateButton.setEnabled(false);
        
        Label dummyLabel3 = new Label("     ");       
        dummyLabel3.setOpaque(false);
        buttonPanel.add(dummyLabel3);
        
        Button cancelChanges = ButtonFactory.makeTranslucentMenuButton("Cancel", "Cancel", "Cancel the addition of a new pilot to this campaign", this);
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
                parentScreen.refreshInformation();
                CampaignGuiContextManager.getInstance().popFromContextStack();
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

        PlayerPilotBuilder playerPilotBuilder = new PlayerPilotBuilder(campaign);
        playerPilotBuilder.buildPlayerPilot(playerName, squadronName, rank, coopuser);
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
