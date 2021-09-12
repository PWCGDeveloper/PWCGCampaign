package pwcg.gui.campaign.personnel;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Map;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javax.swing.JOptionPane;
import javafx.scene.layout.Pane;

import pwcg.campaign.Campaign;
import pwcg.campaign.squadmember.SquadronMember;
import pwcg.campaign.squadmember.SquadronMemberStatus;
import pwcg.coop.CoopUserCampaignMassUpdate;
import pwcg.coop.model.CoopDisplayRecord;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.PWCGLogger;
import pwcg.gui.CampaignGuiContextManager;
import pwcg.gui.IRefreshableParentUI;
import pwcg.gui.ScreenIdentifier;
import pwcg.gui.UiImageResolver;
import pwcg.gui.campaign.activity.CampaignTransferScreen;
import pwcg.gui.dialogs.ConfirmDialog;
import pwcg.gui.dialogs.ErrorDialog;
import pwcg.gui.maingui.campaigngenerate.CampaignNewPilotScreen;
import pwcg.gui.sound.SoundManager;
import pwcg.gui.utils.ImageResizingPanel;
import pwcg.gui.utils.ButtonFactory;

public class CampaignPlayerAdminScreen extends ImageResizingPanel implements ActionListener, IRefreshableParentUI
{
    private static final long serialVersionUID = 1L;
    private Campaign campaign;
    private CampaignAdminPilotPanel personaInfoPanel;
    private Pane personaActionsPanel;

    public CampaignPlayerAdminScreen(Campaign campaign)
    {
        super("");
        this.setLayout(new BorderLayout());
        this.setOpaque(false);

        this.campaign = campaign;
    }

    public void makePanels()
    {
        try
        {
            String imagePath = UiImageResolver.getImage(ScreenIdentifier.CampaignCoopAdminScreen);
            this.setImageFromName(imagePath);

            this.add(BorderLayout.WEST, makeNavigatePanel());
            this.add(BorderLayout.CENTER, makeCenterPanel());
            this.add(BorderLayout.EAST, makeRightPanel());
        }
        catch (Throwable e)
        {
            PWCGLogger.logException(e);
            ErrorDialog.internalError(e.getMessage());
        }
    }

    public Pane makeCenterPanel()
    {
        personaInfoPanel = new CampaignAdminPilotPanel(campaign, this);
        personaInfoPanel.makePanels();
        return personaInfoPanel;
    }

    public Pane makeNavigatePanel() throws PWCGException
    {
        Pane buttonPanel = new Pane(new GridLayout(0, 1));
        buttonPanel.setOpaque(false);

        Button finishedAndSave = ButtonFactory.makeTranslucentMenuButton("Finished", "Finished", "Finished save results of coop administration", this);
        buttonPanel.add(finishedAndSave);

        Button finishedAndCancel = ButtonFactory.makeTranslucentMenuButton("Cancel", "Cancel", "Finished do not save results of coop administration", this);
        buttonPanel.add(finishedAndCancel);

        Pane navPanel = new Pane(new BorderLayout());
        navPanel.setOpaque(false);
        navPanel.add(buttonPanel, BorderLayout.NORTH);

        return navPanel;
    }

    public Pane makeRightPanel() throws PWCGException
    {

        Pane buttonPanel = new Pane(new GridLayout(0, 1));
        buttonPanel.setOpaque(false);

        Label label = ButtonFactory.makeMenuLabelLarge("Select Admin Action:");
        buttonPanel.add(label);

        buttonPanel.add(makeActionButton("Add Pilot", "Add a coop persona to the campaign"));
        if (getSquadronMemberForSelectedPilot() != null)
        {
            if (getSquadronMemberForSelectedPilot().getPilotActiveStatus() == SquadronMemberStatus.STATUS_ACTIVE)
            {
                buttonPanel.add(makeActionButton("Transfer Pilot", "Transfer a coop persona to a new squadron"));
                buttonPanel.add(makeActionButton("Retire Pilot", "Retire a coop persona. Cannot be undone"));
            }
            else
            {
                buttonPanel.add(makeActionButton("Activate Pilot", "Activate a disabled pilot"));
            }
        }
        

        personaActionsPanel = new Pane(new BorderLayout());
        personaActionsPanel.setOpaque(false);
        personaActionsPanel.add(buttonPanel, BorderLayout.NORTH);
        return personaActionsPanel;
    }

    private Button makeActionButton(String buttonText, String tooltip) throws PWCGException
    {
        Button button = ButtonFactory.makeTranslucentMenuButton(buttonText, buttonText, tooltip, this);
        return button;
    }

    public void actionPerformed(ActionEvent ae)
    {
        try
        {
            String action = ae.getActionCommand();
            if (action.equalsIgnoreCase("Finished"))
            {
                updateCoopUserRecordsForUserSelection();
                CampaignGuiContextManager.getInstance().popFromContextStack();
                return;
            }

            if (action.equalsIgnoreCase("Cancel"))
            {
                CampaignGuiContextManager.getInstance().popFromContextStack();
                return;
            }

            if (action.equalsIgnoreCase("Add Pilot"))
            {
                addPilot();
            }
            else if (action.equalsIgnoreCase("Transfer Pilot"))
            {
                transferPilot();
            }
            else if (action.contains("Retire Pilot"))
            {
                retirePilot();
            }
            else if (action.contains("Activate Pilot"))
            {
                activatePilot();
            }
        }
        catch (Throwable e)
        {
            PWCGLogger.logException(e);
            ErrorDialog.internalError(e.getMessage());
        }
    }

    private void addPilot() throws PWCGException
    {
        SoundManager.getInstance().playSound("Typewriter.WAV");
        CampaignNewPilotScreen addPilotDisplay = new CampaignNewPilotScreen(campaign, this);
        addPilotDisplay.makePanels();
        CampaignGuiContextManager.getInstance().pushToContextStack(addPilotDisplay);
    }

    private void transferPilot() throws PWCGException
    {
        SquadronMember pilot = getSquadronMemberForSelectedPilot();
        if (pilot != null)
        {
            SoundManager.getInstance().playSound("Typewriter.WAV");
            boolean passTime = false;
            CampaignTransferScreen transferDisplay = new CampaignTransferScreen(campaign, pilot, this, passTime);
            transferDisplay.makePanels();
            CampaignGuiContextManager.getInstance().pushToContextStack(transferDisplay);
        }
    }

    private void retirePilot() throws PWCGException
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

                refreshInformation();
            }
        }
    }

    private void activatePilot() throws PWCGException
    {
        SquadronMember pilot = getSquadronMemberForSelectedPilot();
        if (pilot != null)
        {
            int result = ConfirmDialog.areYouSure("Confirm Reactivation of " + pilot.getNameAndRank() + ".  Cannot be reversed");
            if (result == JOptionPane.YES_OPTION)
            {
                SoundManager.getInstance().playSound("Typewriter.WAV");
                pilot.setPilotActiveStatus(SquadronMemberStatus.STATUS_ACTIVE, campaign.getDate(), null);
                campaign.write();

                refreshInformation();
            }
        }
    }

    private void updateCoopUserRecordsForUserSelection() throws PWCGException
    {
        Map<String, List<Integer>> personaByUser = personaInfoPanel.getUsersForPersonas();
        CoopUserCampaignMassUpdate.updateCoopUserRecordsForUserSelectionMakeANewClassAndTestIt(campaign, personaByUser);
    }

    public void refreshInformation() throws PWCGException
    {
        this.remove(personaActionsPanel);
        this.add(BorderLayout.EAST, makeRightPanel());

        personaInfoPanel.refreshInformation();
        
        refresh();
    }

    private SquadronMember getSquadronMemberForSelectedPilot() throws PWCGException
    {
        CoopDisplayRecord coopDisplayRecord = personaInfoPanel.getSelectedPilot();
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

    @Override
    public Pane getScreen()
    {
        return this;
    }
}
