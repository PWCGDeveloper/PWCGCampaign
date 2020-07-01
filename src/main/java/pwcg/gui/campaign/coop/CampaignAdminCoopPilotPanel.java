package pwcg.gui.campaign.coop;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ButtonGroup;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;

import pwcg.campaign.Campaign;
import pwcg.campaign.squadmember.SquadronMemberStatus;
import pwcg.coop.CoopPersonaDataBuilder;
import pwcg.coop.model.CoopDisplayRecord;
import pwcg.core.exception.PWCGException;
import pwcg.core.exception.PWCGUserException;
import pwcg.core.utils.PWCGLogger;
import pwcg.gui.ScreenIdentifier;
import pwcg.gui.UiImageResolver;
import pwcg.gui.colors.ColorMap;
import pwcg.gui.dialogs.ErrorDialog;
import pwcg.gui.dialogs.PWCGMonitorFonts;
import pwcg.gui.utils.ImageResizingPanel;
import pwcg.gui.utils.PwcgBorderFactory;
import pwcg.gui.utils.ScrollBarWrapper;

public class CampaignAdminCoopPilotPanel extends ImageResizingPanel implements ActionListener
{
    private static final long serialVersionUID = 1L;
    private List<CoopDisplayRecord> coopDisplayRecords = new ArrayList<>();
    private ButtonGroup buttonGroup = new ButtonGroup();
    private int selectedPilotSerialNumber;
    private Campaign campaign;

    public CampaignAdminCoopPilotPanel(Campaign campaign)
    {
        super("");
        this.setLayout(new BorderLayout());

        this.campaign = campaign;
    }
    
    public void makePanels() 
    {
        try
        {
            String imagePath = UiImageResolver.getImage(ScreenIdentifier.Document);
            this.setImage(imagePath);
            this.setBorder(PwcgBorderFactory.createStandardDocumentBorder());

            JPanel centerPanel = makeDisplay();
            this.add(centerPanel, BorderLayout.NORTH);
        }
        catch (Throwable e)
        {
            PWCGLogger.logException(e);
            ErrorDialog.internalError(e.getMessage());
        }
    }

    private JPanel makeDisplay() throws PWCGException
    {
        loadCoopRecords();

        JPanel recordListPanel = new JPanel(new GridLayout(0, 4));
        recordListPanel.setOpaque(false);

        JPanel recordListHolderPanel = new JPanel();
        recordListHolderPanel.setOpaque(false);
        recordListHolderPanel.add(recordListPanel, BorderLayout.NORTH);

        JScrollPane planeListScroll = ScrollBarWrapper.makeScrollPane(recordListHolderPanel);

        for (CoopDisplayRecord coopDisplayRecord : coopDisplayRecords)
        {
            JRadioButton pilotSelector = makeRadioButton(coopDisplayRecord);
            JLabel usernameLabel = makeDataLabel(coopDisplayRecord.getUsername());
            JLabel campaignNameLabel = makeDataLabel(coopDisplayRecord.getCampaignName());
            JLabel squadronNameLabel = makeDataLabel(coopDisplayRecord.getSquadronName());
            recordListPanel.add(pilotSelector);
            recordListPanel.add(campaignNameLabel);
            recordListPanel.add(usernameLabel);
            recordListPanel.add(squadronNameLabel);
        }

        JPanel centerPanel = new JPanel();
        centerPanel.setOpaque(false);
        centerPanel.add(planeListScroll, BorderLayout.CENTER);
        return centerPanel;
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
        
        buttonGroup.add(button);

        return button;
    }

    private void loadCoopRecords() throws PWCGUserException, PWCGException
    {
        coopDisplayRecords.clear();
        CoopPersonaDataBuilder coopPersonaDataBuilder = new CoopPersonaDataBuilder();
        List<CoopDisplayRecord> coopDisplayRecordsForCampaign = coopPersonaDataBuilder.getPlayerSquadronMembersForUser(campaign);
        for (CoopDisplayRecord coopDisplayRecord : coopDisplayRecordsForCampaign)
        {
            if (coopDisplayRecord.getPilotStatus() > SquadronMemberStatus.STATUS_CAPTURED)
            {
                coopDisplayRecords.add(coopDisplayRecord);
            }
        }
    }
    
    public JLabel makeDataLabel(String labelText) throws PWCGException  
    {
        Font font = PWCGMonitorFonts.getPrimaryFont();
        Color bg = ColorMap.PAPER_BACKGROUND;
        Color fg = ColorMap.PAPER_FOREGROUND;

        JLabel label = new JLabel(labelText, JLabel.LEFT);
        label.setBackground(bg);
        label.setForeground(fg);
        label.setOpaque(false);
        label.setFont(font);

       return label;
    }
    
    public void actionPerformed(ActionEvent ae)
    {
        try
        {
            selectedPilotSerialNumber = Integer.valueOf(ae.getActionCommand());
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
}
