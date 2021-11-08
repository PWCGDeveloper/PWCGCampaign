package pwcg.gui.campaign.personnel;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.ButtonGroup;
import javax.swing.ButtonModel;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;

import pwcg.campaign.Campaign;
import pwcg.campaign.squadmember.SquadronMemberStatus;
import pwcg.coop.CoopPersonaDataBuilder;
import pwcg.coop.CoopUserManager;
import pwcg.coop.model.CoopDisplayRecord;
import pwcg.coop.model.CoopUser;
import pwcg.core.exception.PWCGException;
import pwcg.core.exception.PWCGUserException;
import pwcg.core.utils.PWCGLogger;
import pwcg.gui.IRefreshableParentUI;
import pwcg.gui.ScreenIdentifier;
import pwcg.gui.UiImageResolver;
import pwcg.gui.colors.ColorMap;
import pwcg.gui.dialogs.ErrorDialog;
import pwcg.gui.dialogs.PWCGMonitorFonts;
import pwcg.gui.utils.ImageResizingPanel;
import pwcg.gui.utils.PWCGLabelFactory;
import pwcg.gui.utils.ScrollBarWrapper;

public class CampaignPlayerAdminPilotPanel extends ImageResizingPanel implements ActionListener, IRefreshableParentUI
{
    private static final long serialVersionUID = 1L;
    private List<CoopDisplayRecord> coopDisplayRecords = new ArrayList<>();
    private Map<Integer, JComboBox<String>> userSelectors = new HashMap<>();
    private ButtonGroup pilotButtonGroup = new ButtonGroup();
    private Map<Integer, ButtonModel> pilotButtonModels = new HashMap<>();

    private int selectedPilotSerialNumber;
    private JPanel centerPanel;
    
    private IRefreshableParentUI parentScreen;
    private Campaign campaign;

    public CampaignPlayerAdminPilotPanel(Campaign campaign, IRefreshableParentUI parentScreen)
    {
        super("");
        this.setLayout(new BorderLayout());

        this.campaign = campaign;
        this.parentScreen = parentScreen;
    }
    
    public void makePanels() 
    {
        try
        {
            String imagePath = UiImageResolver.getImage(ScreenIdentifier.BlankDocument);
            this.setImageFromName(imagePath);
            this.add(makeDisplay(), BorderLayout.CENTER);
        }
        catch (Throwable e)
        {
            PWCGLogger.logException(e);
            ErrorDialog.internalError(e.getMessage());
        }
    }

    private JPanel makeDisplay() throws PWCGException
    {
        pilotButtonModels.clear();
        
        loadCoopRecords();

        JPanel recordListPanel = new JPanel(new GridBagLayout());
        
        recordListPanel.setOpaque(false);

        GridBagConstraints constraints = initializeGridbagConstraints();

        constraints.weightx = 0.05;
        constraints.weighty = 0.1;
        constraints.gridx = 1;
        constraints.gridy = 1;
        constraints.gridheight = 1;
        recordListPanel.add(PWCGLabelFactory.makeDummyLabel(), constraints);

        int rowNumber = 2;
        for (CoopDisplayRecord coopDisplayRecord : coopDisplayRecords)
        {
            JRadioButton pilotSelector = makeRadioButton(coopDisplayRecord);
            JComboBox<String> userSelector = makeUserSelector(coopDisplayRecord.getUsername());
            
            JLabel campaignNameLabel = PWCGLabelFactory.makeTransparentLabel(
                    coopDisplayRecord.getCampaignName(), ColorMap.PAPER_FOREGROUND, PWCGMonitorFonts.getPrimaryFont(), SwingConstants.LEFT);
            String pilotStatusText = "     " + SquadronMemberStatus.pilotStatusToStatusDescription(coopDisplayRecord.getPilotStatus());
            JLabel pilotStatusLabel = PWCGLabelFactory.makeTransparentLabel(
                    pilotStatusText, ColorMap.PAPER_FOREGROUND, PWCGMonitorFonts.getPrimaryFont(), SwingConstants.LEFT);
            JLabel squadronNameLabel = PWCGLabelFactory.makeTransparentLabel(
                    "     " + coopDisplayRecord.getSquadronName(), ColorMap.PAPER_FOREGROUND, PWCGMonitorFonts.getPrimaryFont(), SwingConstants.LEFT);

            pilotButtonModels.put(coopDisplayRecord.getPilotSerialNumber(), pilotSelector.getModel());
            userSelectors.put(coopDisplayRecord.getPilotSerialNumber(), userSelector);
            
            constraints.weightx = 0.05;
            constraints.weighty = 0.1;
            constraints.gridx = 1;
            constraints.gridy = rowNumber;
            constraints.gridheight = 1;
            recordListPanel.add(pilotSelector, constraints);

            constraints.weightx = 0.01;
            constraints.weighty = 0.1;
            constraints.gridx = 2;
            constraints.gridy = rowNumber;
            constraints.gridheight = 1;
            recordListPanel.add(campaignNameLabel, constraints);
            
            constraints.weightx = 0.01;
            constraints.weighty = 0.1;
            constraints.gridx = 3;
            constraints.gridy = rowNumber;
            constraints.gridheight = 1;
            recordListPanel.add(pilotStatusLabel, constraints);
            
            constraints.weightx = 0.01;
            constraints.weighty = 0.1;
            constraints.gridx = 4;
            constraints.gridy = rowNumber;
            constraints.gridheight = 1;
            recordListPanel.add(squadronNameLabel, constraints);
            
            if (campaign.isCoop())
            {
                constraints.weightx = 0.01;
                constraints.weighty = 0.1;
                constraints.gridx = 5;
                constraints.gridy = rowNumber;
                constraints.gridheight = 1;
                recordListPanel.add(userSelector, constraints);
            }

            ++rowNumber;
        }


        JScrollPane planeListScroll = ScrollBarWrapper.makeScrollPane(recordListPanel);
        
        centerPanel = new JPanel();
        centerPanel.setOpaque(false);
        centerPanel.add(planeListScroll, BorderLayout.CENTER);

        return centerPanel;
    }

    private GridBagConstraints initializeGridbagConstraints()
    {
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.ipadx = 3;
        constraints.ipady = 3;
        constraints.anchor = GridBagConstraints.NORTHWEST;
        Insets margins = new Insets(0, 5, 5, 0);
        constraints.insets = margins;
        return constraints;
    }

    private JComboBox<String> makeUserSelector(String username) throws PWCGException
    {
        JComboBox<String> userSelector = new JComboBox<>();
        userSelector.setOpaque(false);
        userSelector.setBackground(ColorMap.PAPER_BACKGROUND);
        userSelector.setFont(PWCGMonitorFonts.getPrimaryFontLarge());

        userSelector.addItem("Unassigned");
        for (CoopUser coopUser : CoopUserManager.getIntance().getAllCoopUsers())
        {
            userSelector.addItem(coopUser.getUsername());
            userSelector.setSelectedItem(username);
        }
        return userSelector;
    }

    private JRadioButton makeRadioButton(CoopDisplayRecord coopDisplayRecord) throws PWCGException
    {
        Font font = PWCGMonitorFonts.getPrimaryFont();
        Color bg = ColorMap.PAPER_BACKGROUND;
        Color fg = ColorMap.PAPER_FOREGROUND;

        JRadioButton button = new JRadioButton(coopDisplayRecord.getPilotNameAndRank());
        button.setActionCommand("" + coopDisplayRecord.getPilotSerialNumber());
        button.setHorizontalAlignment(SwingConstants.LEFT );
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.addActionListener(this);
        button.setOpaque(false);
        button.setForeground(fg);
        button.setBackground(bg);
        button.setFont(font);
        
        pilotButtonGroup.add(button);

        return button;
    }

    private void loadCoopRecords() throws PWCGUserException, PWCGException
    {
        coopDisplayRecords.clear();
        CoopPersonaDataBuilder coopPersonaDataBuilder = new CoopPersonaDataBuilder();
        List<CoopDisplayRecord> coopDisplayRecordsForCampaign = coopPersonaDataBuilder.getPlayerSquadronMembersForUser(campaign);
        for (CoopDisplayRecord coopDisplayRecord : coopDisplayRecordsForCampaign)
        {
            coopDisplayRecords.add(coopDisplayRecord);
        }
    }
    
    public void actionPerformed(ActionEvent ae)
    {
        try
        {
            selectedPilotSerialNumber = Integer.valueOf(ae.getActionCommand());
            parentScreen.refreshInformation();
        }
        catch (Throwable e)
        {
            PWCGLogger.logException(e);
            ErrorDialog.internalError(e.getMessage());
        }
    }

    public CoopDisplayRecord getSelectedPilot()
    {
        for (CoopDisplayRecord coopDisplayRecord : coopDisplayRecords)
        {
            if (coopDisplayRecord.getPilotSerialNumber() == selectedPilotSerialNumber)
            {
                return coopDisplayRecord;
            }
        }
        return null;
    }
    
    public Map<String, List<Integer>> getUsersForPersonas()
    {
        Map<String, List<Integer>> personaByUser = new HashMap<>();
        for (int serialNumber : userSelectors.keySet())
        {
            JComboBox<String> userSelector = userSelectors.get(serialNumber);
            String userName = (String)userSelector.getSelectedItem();
            if (!personaByUser.containsKey(userName))
            {
                List<Integer> personasForUser = new ArrayList<>();
                personaByUser.put(userName, personasForUser);
            }
            List<Integer> personasForUser = personaByUser.get(userName);
            personasForUser.add(serialNumber);
        }
        return personaByUser;            
    }

    @Override
    public void refreshInformation() throws PWCGException
    {
        this.remove(centerPanel);
        this.add(makeDisplay(), BorderLayout.CENTER);
        
        CoopDisplayRecord coopDisplayRecord = getSelectedPilot();
        if (coopDisplayRecord != null)
        {
            if (pilotButtonModels.containsKey(selectedPilotSerialNumber))
            {
                ButtonModel pilotButtonModel = pilotButtonModels.get(selectedPilotSerialNumber);
                pilotButtonGroup.setSelected(pilotButtonModel, true);
            }
        }
    }

    @Override
    public JPanel getScreen()
    {
        return this;
    }
}
