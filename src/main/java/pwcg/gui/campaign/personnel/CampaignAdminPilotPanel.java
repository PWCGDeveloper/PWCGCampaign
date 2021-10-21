package pwcg.gui.campaign.personnel;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
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
import pwcg.gui.utils.PwcgBorderFactory;
import pwcg.gui.utils.ScrollBarWrapper;

public class CampaignAdminPilotPanel extends ImageResizingPanel implements ActionListener, IRefreshableParentUI
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

    public CampaignAdminPilotPanel(Campaign campaign, IRefreshableParentUI parentScreen)
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
            String imagePath = UiImageResolver.getImage(ScreenIdentifier.Document);
            this.setImageFromName(imagePath);
            this.setBorder(PwcgBorderFactory.createStandardDocumentBorder());

            this.add(makeDisplay(), BorderLayout.NORTH);
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

        JPanel recordListPanel = new JPanel(new GridLayout(0, 4));
        if (campaign.isCoop())
        {
            recordListPanel = new JPanel(new GridLayout(0, 5));
        }
        
        recordListPanel.setOpaque(false);

        for (CoopDisplayRecord coopDisplayRecord : coopDisplayRecords)
        {
            JRadioButton pilotSelector = makeRadioButton(coopDisplayRecord);
            JComboBox<String> userSelector = makeUserSelector(coopDisplayRecord.getUsername());
            
            JLabel campaignNameLabel = PWCGLabelFactory.makeLabel(
                    coopDisplayRecord.getCampaignName(), ColorMap.PAPER_BACKGROUND, ColorMap.PAPER_FOREGROUND, PWCGMonitorFonts.getPrimaryFont(), SwingConstants.LEFT);
            String pilotStatusText = "     " + SquadronMemberStatus.pilotStatusToStatusDescription(coopDisplayRecord.getPilotStatus());
            JLabel pilotStatusLabel = PWCGLabelFactory.makeLabel(
                    pilotStatusText, ColorMap.PAPER_BACKGROUND, ColorMap.PAPER_FOREGROUND, PWCGMonitorFonts.getPrimaryFont(), SwingConstants.LEFT);
            JLabel squadronNameLabel = PWCGLabelFactory.makeLabel(
                    "     " + coopDisplayRecord.getSquadronName(), ColorMap.PAPER_BACKGROUND, ColorMap.PAPER_FOREGROUND, PWCGMonitorFonts.getPrimaryFont(), SwingConstants.LEFT);

            recordListPanel.add(pilotSelector);
            recordListPanel.add(campaignNameLabel);
            
            
            if (campaign.isCoop())
            {
                recordListPanel.add(userSelector);
            }
            
            recordListPanel.add(pilotStatusLabel);
            recordListPanel.add(squadronNameLabel);
            
            pilotButtonModels.put(coopDisplayRecord.getPilotSerialNumber(), pilotSelector.getModel());
            userSelectors.put(coopDisplayRecord.getPilotSerialNumber(), userSelector);
        }


        JScrollPane planeListScroll = ScrollBarWrapper.makeScrollPane(recordListPanel);
        
        centerPanel = new JPanel();
        centerPanel.setOpaque(false);
        centerPanel.add(planeListScroll, BorderLayout.NORTH);

        return centerPanel;
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
        this.add(makeDisplay(), BorderLayout.NORTH);
        
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
