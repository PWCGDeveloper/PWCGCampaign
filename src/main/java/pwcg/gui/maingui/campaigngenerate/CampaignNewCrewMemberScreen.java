package pwcg.gui.maingui.campaigngenerate;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;

import pwcg.campaign.ArmedService;
import pwcg.campaign.Campaign;
import pwcg.campaign.crewmember.PlayerCrewMemberBuilder;
import pwcg.core.exception.PWCGException;
import pwcg.core.exception.PWCGUserException;
import pwcg.core.utils.PWCGLogger;
import pwcg.gui.CampaignGuiContextManager;
import pwcg.gui.IRefreshableParentUI;
import pwcg.gui.ScreenIdentifier;
import pwcg.gui.UiImageResolver;
import pwcg.gui.dialogs.ErrorDialog;
import pwcg.gui.utils.ImageResizingPanel;
import pwcg.gui.utils.PWCGButtonFactory;
import pwcg.gui.utils.PWCGLabelFactory;
import pwcg.gui.utils.SpacerPanelFactory;

public class CampaignNewCrewMemberScreen extends ImageResizingPanel implements ActionListener
{    
    private static final long serialVersionUID = 1L;

    private Campaign campaign;
    private JButton newCrewMemberCreateButton;
    private NewCrewMemberDataEntryGUI dataEntry;
    
    private NewCrewMemberGeneratorDO newCrewMemberGeneratorDO = new NewCrewMemberGeneratorDO();
    private NewCrewMemberState newCrewMemberState;
    private IRefreshableParentUI parentScreen;

    public CampaignNewCrewMemberScreen(Campaign campaign, IRefreshableParentUI parentScreen)
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
            String imagePath = UiImageResolver.getImage(ScreenIdentifier.CampaignNewCrewMemberScreen);
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

    private JPanel makeServicePanel() throws PWCGException
    {        
        JPanel servicesPanel = new JPanel(new BorderLayout());
        servicesPanel.setLayout(new BorderLayout());
        servicesPanel.setOpaque(false);

        CrewMemberGenerationInfoGUI campaignChooseServiceGUI = new CrewMemberGenerationInfoGUI(this, campaign);
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

        newCrewMemberCreateButton = PWCGButtonFactory.makeTranslucentMenuButton("Create CrewMember", "Create CrewMember", "Add this crewMember to the campaign", this);
        buttonPanel.add(newCrewMemberCreateButton);
        newCrewMemberCreateButton.setEnabled(false);
        
        buttonPanel.add(PWCGLabelFactory.makeDummyLabel());
        
        JButton cancelChanges = PWCGButtonFactory.makeTranslucentMenuButton("Cancel", "Cancel", "Cancel the addition of a new crewMember to this campaign", this);
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
            else if (action.equalsIgnoreCase("Create CrewMember"))
            {
                createCrewMember();
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

    private void createCrewMember() throws PWCGUserException, Exception
    {
        String playerName = newCrewMemberGeneratorDO.getPlayerCrewMemberName();
        String squadronName = newCrewMemberGeneratorDO.getSquadName();
        String rank = newCrewMemberGeneratorDO.getRank();
        String coopuser = newCrewMemberGeneratorDO.getCoopUser();

        PlayerCrewMemberBuilder playerCrewMemberBuilder = new PlayerCrewMemberBuilder(campaign);
        playerCrewMemberBuilder.buildPlayerCrewMember(playerName, squadronName, rank, coopuser);
    }

    public void changeService(ArmedService service) throws PWCGException
    {
        newCrewMemberGeneratorDO.setService(service);
        
        if (dataEntry != null)
        {
            this.remove(dataEntry);
        }
        
        dataEntry = new NewCrewMemberDataEntryGUI(campaign, this);
        dataEntry.makePanels();
        dataEntry.evaluateUI();
        
        this.add(dataEntry, BorderLayout.CENTER);
        
        CampaignGuiContextManager.getInstance().refreshCurrentContext(this);
    }

    public NewCrewMemberGeneratorDO getNewCrewMemberGeneratorDO()
    {
        return newCrewMemberGeneratorDO;
    }

    public NewCrewMemberState getNewCrewMemberState()
    {
        return newCrewMemberState;
    }

    public void evaluateCompletionState() throws PWCGException
    {
        newCrewMemberCreateButton.setEnabled(false);
        createNewCrewMemberState();
        if (newCrewMemberState.isComplete())
        {
            newCrewMemberCreateButton.setEnabled(true);
        }
    }

    private void createNewCrewMemberState() throws PWCGException
    {
        if (newCrewMemberState == null)
        {
            newCrewMemberState = new NewCrewMemberState(campaign, newCrewMemberGeneratorDO);
            newCrewMemberState.buildStateStack();
        }
    }
}
