package pwcg.gui.campaign.personnel;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import pwcg.campaign.Campaign;
import pwcg.campaign.squadmember.SquadronMember;
import pwcg.campaign.squadmember.SquadronMemberStatus;
import pwcg.coop.CoopPersonaDataBuilder;
import pwcg.coop.CoopUserCampaignMassUpdate;
import pwcg.coop.model.CoopDisplayRecord;
import pwcg.core.exception.PWCGException;
import pwcg.core.exception.PWCGUserException;
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
import pwcg.gui.utils.ImageToDisplaySizer;
import pwcg.gui.utils.PWCGButtonFactory;
import pwcg.gui.utils.PWCGLabelFactory;

public class CampaignPlayerAdminScreen extends ImageResizingPanel implements ActionListener, IRefreshableParentUI
{
    private static final long serialVersionUID = 1L;
    private Campaign campaign;
    private int selectedPilotPanel  = 0;
    private List<CampaignPlayerAdminPilotPanel> personaInfoPanels = new ArrayList<>();
    private CampaignPlayerAdminPilotPanel selectedPersonaInfoPanel = null;
    private TreeMap<String, CoopDisplayRecord> coopDisplayRecords = new TreeMap<>(Collections.reverseOrder());
    private JPanel personaActionsPanel;
    private int pilotsPerPanel  = 12;
    private JButton nextPage;
    private JButton previousPage;

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
            loadCoopRecords();

            String imagePath = UiImageResolver.getImage(ScreenIdentifier.CampaignCoopAdminScreen);
            this.setImageFromName(imagePath);
            
            JPanel navPanel  = makeNavigatePanel();
            this.add(navPanel, BorderLayout.WEST);
            
            makeCenterPanels();

            makeRightPanel();
            this.add(personaActionsPanel, BorderLayout.EAST);
        }
        catch (Throwable e)
        {
            PWCGLogger.logException(e);
            ErrorDialog.internalError(e.getMessage());
        }
    }

    private void makeCenterPanels() throws PWCGException
    {
        personaInfoPanels.clear();
        int pages = calculateNumberOfPages();
        for (int page = 0; page <pages; ++page)
        {
            List<CoopDisplayRecord> coopDisplayRecordsForPage = getRecordsForPage(page);
            CampaignPlayerAdminPilotPanel personaInfoPanel = new CampaignPlayerAdminPilotPanel(campaign, this, coopDisplayRecordsForPage);
            personaInfoPanel.makePanels();
            ImageToDisplaySizer.setDocumentSizePlusExtra(personaInfoPanel, 300);
            personaInfoPanels.add(personaInfoPanel);
        }

        setCoopRecordPage();
        enablePageTurners();
    }

    private void setCoopRecordPage()
    {
        if (selectedPersonaInfoPanel != null)
        {
            this.remove(selectedPersonaInfoPanel);
        }

        selectedPersonaInfoPanel = personaInfoPanels.get(selectedPilotPanel);
        this.add(selectedPersonaInfoPanel, BorderLayout.CENTER);
    }

    private int calculateNumberOfPages()
    {
        int pages = coopDisplayRecords.size() / pilotsPerPanel;
        if ((coopDisplayRecords.size() % pilotsPerPanel) > 0)
        {
            ++pages;
        }
        if (pages == 0)
        {
            pages = 1;
        }
        return pages;
    }

    private List<CoopDisplayRecord> getRecordsForPage(int page)
    {
        int startRecord = pilotsPerPanel * page;
        int endRecord = pilotsPerPanel * (page + 1);
        if (endRecord > coopDisplayRecords.size())
        {
            endRecord = coopDisplayRecords.size();
        }
        
        List<CoopDisplayRecord> allCoopDisplayRecords = new ArrayList<>();
        allCoopDisplayRecords.addAll(coopDisplayRecords.values());
        
        List<CoopDisplayRecord> coopDisplayRecordsForPage = new ArrayList<>();
        for (int i = startRecord; i < endRecord;++i)
        {
            coopDisplayRecordsForPage.add(allCoopDisplayRecords.get(i));
        }
        return coopDisplayRecordsForPage;
    }

    public JPanel makeNavigatePanel() throws PWCGException
    {
        JPanel buttonPanel = new JPanel(new GridLayout(0, 1));
        buttonPanel.setOpaque(false);

        buttonPanel.add(PWCGLabelFactory.makeDummyLabel());
        buttonPanel.add(PWCGLabelFactory.makeDummyLabel());
        
        JButton finished = PWCGButtonFactory.makeTranslucentMenuButton("Finished", "Finished", "Finished save results of coop administration", this);
        buttonPanel.add(finished);

        buttonPanel.add(PWCGLabelFactory.makeDummyLabel());
        
        nextPage = PWCGButtonFactory.makeTranslucentMenuButton("Next Page", "NextPage", "Next page of pilots", this);
        buttonPanel.add(nextPage);

        previousPage = PWCGButtonFactory.makeTranslucentMenuButton("Previous Page", "PreviousPage", "Previous page of pilots", this);
        buttonPanel.add(previousPage);

        JPanel navPanel = new JPanel(new BorderLayout());
        navPanel.setOpaque(false);
        navPanel.add(buttonPanel, BorderLayout.NORTH);

        return navPanel;
    }

    public JPanel makeRightPanel() throws PWCGException
    {
        if (personaActionsPanel != null)
        {
            this.remove(personaActionsPanel);
        }
        
        JPanel buttonPanel = new JPanel(new GridLayout(0, 1));
        buttonPanel.setOpaque(false);

        buttonPanel.add(PWCGLabelFactory.makeDummyLabel());

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

        personaActionsPanel = new JPanel(new BorderLayout());
        personaActionsPanel.setOpaque(false);
        personaActionsPanel.add(buttonPanel, BorderLayout.NORTH);
        return personaActionsPanel;
    }
    
    private void enablePageTurners()
    {
        nextPage.setEnabled(false);
        previousPage.setEnabled(false);

        if (selectedPilotPanel > 0)
        {
            previousPage.setEnabled(true);
        }

        if (selectedPilotPanel < (personaInfoPanels.size() -1))
        {
            nextPage.setEnabled(true);
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
                saveAndFinish();
            }
            else if (action.equalsIgnoreCase("NextPage"))
            {
                nextPage();
            }
            else if (action.equalsIgnoreCase("PreviousPage"))
            {
                previousPage();
            }
            else if (action.equalsIgnoreCase("Add Pilot"))
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

    private void saveAndFinish() throws PWCGException
    {
        updateCoopUserRecordsForUserSelection();
        CampaignGuiContextManager.getInstance().popFromContextStack();
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
            int result = ConfirmDialog.areYouSure("Confirm Retire " + pilot.getNameAndRank());
            if (result == JOptionPane.YES_OPTION)
            {
                ChangePilotStatus(pilot, SquadronMemberStatus.STATUS_RETIRED);
            }
        }
    }

    private void activatePilot() throws PWCGException
    {
        SquadronMember pilot = getSquadronMemberForSelectedPilot();
        if (pilot != null)
        {
            int result = ConfirmDialog.areYouSure("Confirm Reactivation of " + pilot.getNameAndRank());
            if (result == JOptionPane.YES_OPTION)
            {
                ChangePilotStatus(pilot, SquadronMemberStatus.STATUS_ACTIVE);
            }
        }
    }

    private void ChangePilotStatus(SquadronMember pilot, int status) throws PWCGException, PWCGUserException
    {
        SoundManager.getInstance().playSound("Typewriter.WAV");
        pilot.setPilotActiveStatus(status, campaign.getDate(), null);
        campaign.write();

        loadCoopRecords();
        updateCoopUserRecordsForUserSelection();
        refreshInformation();
    }
    
    private void nextPage() throws PWCGException
    {
        if (selectedPilotPanel < (personaInfoPanels.size()-1))
        {
            ++selectedPilotPanel;
            updateCoopUserRecordsForUserSelection();
        }
    }
    
    private void previousPage() throws PWCGException
    {
        if (selectedPilotPanel > 0)
        {
            --selectedPilotPanel;
            updateCoopUserRecordsForUserSelection();
        }
    }

    private void updateCoopUserRecordsForUserSelection() throws PWCGException
    {
        CampaignPlayerAdminPilotPanel personaInfoPanel = personaInfoPanels.get(selectedPilotPanel);
        Map<String, List<Integer>> personaByUser = personaInfoPanel.getUsersForPersonas();
        CoopUserCampaignMassUpdate.updateCoopUserRecordsForUserSelectionMakeANewClassAndTestIt(campaign, personaByUser);
        makeCenterPanels();
        refresh();
    }

    public void refreshInformation() throws PWCGException
    {
        CampaignPlayerAdminPilotPanel personaInfoPanel = personaInfoPanels.get(selectedPilotPanel);                
        personaInfoPanel.refreshInformation();
        
        makeRightPanel();
        this.add(personaActionsPanel, BorderLayout.EAST);

        refresh();
    }

    private SquadronMember getSquadronMemberForSelectedPilot() throws PWCGException
    {
         CampaignPlayerAdminPilotPanel personaInfoPanel = personaInfoPanels.get(selectedPilotPanel);
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
    public JPanel getScreen()
    {
        return this;
    }

    private void loadCoopRecords() throws PWCGUserException, PWCGException
    {
        coopDisplayRecords.clear();
        CoopPersonaDataBuilder coopPersonaDataBuilder = new CoopPersonaDataBuilder();
        List<CoopDisplayRecord> coopDisplayRecordsForCampaign = coopPersonaDataBuilder.getPlayerSquadronMembersForUser(campaign);
        for (CoopDisplayRecord coopDisplayRecord : coopDisplayRecordsForCampaign)
        {
            String key = coopDisplayRecord.getPilotStatus() + coopDisplayRecord.getPilotNameAndRank();
            coopDisplayRecords.put(key, coopDisplayRecord);
        }
    }
}
