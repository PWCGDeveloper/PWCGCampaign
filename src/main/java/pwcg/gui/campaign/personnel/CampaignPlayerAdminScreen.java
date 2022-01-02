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
import pwcg.campaign.crewmember.CrewMember;
import pwcg.campaign.crewmember.CrewMemberStatus;
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
import pwcg.gui.maingui.campaigngenerate.CampaignNewCrewMemberScreen;
import pwcg.gui.sound.SoundManager;
import pwcg.gui.utils.ImageResizingPanel;
import pwcg.gui.utils.ImageToDisplaySizer;
import pwcg.gui.utils.PWCGButtonFactory;
import pwcg.gui.utils.PWCGLabelFactory;

public class CampaignPlayerAdminScreen extends ImageResizingPanel implements ActionListener, IRefreshableParentUI
{
    private static final long serialVersionUID = 1L;
    private Campaign campaign;
    private int selectedCrewMemberPanel  = 0;
    private List<CampaignPlayerAdminCrewMemberPanel> personaInfoPanels = new ArrayList<>();
    private CampaignPlayerAdminCrewMemberPanel selectedPersonaInfoPanel = null;
    private TreeMap<String, CoopDisplayRecord> coopDisplayRecords = new TreeMap<>(Collections.reverseOrder());
    private JPanel personaActionsPanel;
    private int crewMembersPerPanel  = 12;
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
            CampaignPlayerAdminCrewMemberPanel personaInfoPanel = new CampaignPlayerAdminCrewMemberPanel(campaign, this, coopDisplayRecordsForPage);
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

        selectedPersonaInfoPanel = personaInfoPanels.get(selectedCrewMemberPanel);
        this.add(selectedPersonaInfoPanel, BorderLayout.CENTER);
    }

    private int calculateNumberOfPages()
    {
        int pages = coopDisplayRecords.size() / crewMembersPerPanel;
        if ((coopDisplayRecords.size() % crewMembersPerPanel) > 0)
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
        int startRecord = crewMembersPerPanel * page;
        int endRecord = crewMembersPerPanel * (page + 1);
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
        
        nextPage = PWCGButtonFactory.makeTranslucentMenuButton("Next Page", "NextPage", "Next page of crewMembers", this);
        buttonPanel.add(nextPage);

        previousPage = PWCGButtonFactory.makeTranslucentMenuButton("Previous Page", "PreviousPage", "Previous page of crewMembers", this);
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

        buttonPanel.add(makeActionButton("Add CrewMember", "Add a coop persona to the campaign"));
        if (getCrewMemberForSelectedCrewMember() != null)
        {
            if (getCrewMemberForSelectedCrewMember().getCrewMemberActiveStatus() == CrewMemberStatus.STATUS_ACTIVE)
            {
                buttonPanel.add(makeActionButton("Transfer CrewMember", "Transfer a coop persona to a new squadron"));
                buttonPanel.add(makeActionButton("Retire CrewMember", "Retire a coop persona. Cannot be undone"));
            }
            else
            {
                buttonPanel.add(makeActionButton("Activate CrewMember", "Activate a disabled crewMember"));
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

        if (selectedCrewMemberPanel > 0)
        {
            previousPage.setEnabled(true);
        }

        if (selectedCrewMemberPanel < (personaInfoPanels.size() -1))
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
            else if (action.equalsIgnoreCase("Add CrewMember"))
            {
                addCrewMember();
            }
            else if (action.equalsIgnoreCase("Transfer CrewMember"))
            {
                transferCrewMember();
            }
            else if (action.contains("Retire CrewMember"))
            {
                retireCrewMember();
            }
            else if (action.contains("Activate CrewMember"))
            {
                activateCrewMember();
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

    private void addCrewMember() throws PWCGException
    {
        SoundManager.getInstance().playSound("Typewriter.WAV");
        CampaignNewCrewMemberScreen addCrewMemberDisplay = new CampaignNewCrewMemberScreen(campaign, this);
        addCrewMemberDisplay.makePanels();
        CampaignGuiContextManager.getInstance().pushToContextStack(addCrewMemberDisplay);
    }

    private void transferCrewMember() throws PWCGException
    {
        CrewMember crewMember = getCrewMemberForSelectedCrewMember();
        if (crewMember != null)
        {
            SoundManager.getInstance().playSound("Typewriter.WAV");
            boolean passTime = false;
            CampaignTransferScreen transferDisplay = new CampaignTransferScreen(campaign, crewMember, this, passTime);
            transferDisplay.makePanels();
            CampaignGuiContextManager.getInstance().pushToContextStack(transferDisplay);
        }
    }

    private void retireCrewMember() throws PWCGException
    {
        CrewMember crewMember = getCrewMemberForSelectedCrewMember();
        if (crewMember != null)
        {
            int result = ConfirmDialog.areYouSure("Confirm Retire " + crewMember.getNameAndRank());
            if (result == JOptionPane.YES_OPTION)
            {
                ChangeCrewMemberStatus(crewMember, CrewMemberStatus.STATUS_RETIRED);
            }
        }
    }

    private void activateCrewMember() throws PWCGException
    {
        CrewMember crewMember = getCrewMemberForSelectedCrewMember();
        if (crewMember != null)
        {
            int result = ConfirmDialog.areYouSure("Confirm Reactivation of " + crewMember.getNameAndRank());
            if (result == JOptionPane.YES_OPTION)
            {
                ChangeCrewMemberStatus(crewMember, CrewMemberStatus.STATUS_ACTIVE);
            }
        }
    }

    private void ChangeCrewMemberStatus(CrewMember crewMember, int status) throws PWCGException, PWCGUserException
    {
        SoundManager.getInstance().playSound("Typewriter.WAV");
        crewMember.setCrewMemberActiveStatus(status, campaign.getDate(), null);
        campaign.write();

        loadCoopRecords();
        updateCoopUserRecordsForUserSelection();
        refreshInformation();
    }
    
    private void nextPage() throws PWCGException
    {
        if (selectedCrewMemberPanel < (personaInfoPanels.size()-1))
        {
            ++selectedCrewMemberPanel;
            updateCoopUserRecordsForUserSelection();
        }
    }
    
    private void previousPage() throws PWCGException
    {
        if (selectedCrewMemberPanel > 0)
        {
            --selectedCrewMemberPanel;
            updateCoopUserRecordsForUserSelection();
        }
    }

    private void updateCoopUserRecordsForUserSelection() throws PWCGException
    {
        CampaignPlayerAdminCrewMemberPanel personaInfoPanel = personaInfoPanels.get(selectedCrewMemberPanel);
        Map<String, List<Integer>> personaByUser = personaInfoPanel.getUsersForPersonas();
        CoopUserCampaignMassUpdate.updateCoopUserRecordsForUserSelectionMakeANewClassAndTestIt(campaign, personaByUser);
        makeCenterPanels();
        refresh();
    }

    public void refreshInformation() throws PWCGException
    {
        CampaignPlayerAdminCrewMemberPanel personaInfoPanel = personaInfoPanels.get(selectedCrewMemberPanel);                
        personaInfoPanel.refreshInformation();
        
        makeRightPanel();
        this.add(personaActionsPanel, BorderLayout.EAST);

        refresh();
    }

    private CrewMember getCrewMemberForSelectedCrewMember() throws PWCGException
    {
         CampaignPlayerAdminCrewMemberPanel personaInfoPanel = personaInfoPanels.get(selectedCrewMemberPanel);
        CoopDisplayRecord coopDisplayRecord = personaInfoPanel.getSelectedCrewMember();
        if (coopDisplayRecord != null)
        {
            CrewMember crewMember = campaign.getPersonnelManager().getAnyCampaignMember(coopDisplayRecord.getCrewMemberSerialNumber());
            return crewMember;
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
        List<CoopDisplayRecord> coopDisplayRecordsForCampaign = coopPersonaDataBuilder.getPlayerCrewMembersForUser(campaign);
        for (CoopDisplayRecord coopDisplayRecord : coopDisplayRecordsForCampaign)
        {
            String key = coopDisplayRecord.getCrewMemberStatus() + coopDisplayRecord.getCrewMemberNameAndRank();
            coopDisplayRecords.put(key, coopDisplayRecord);
        }
    }
}
