package pwcg.gui.campaign.activity;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import pwcg.aar.ui.events.model.LeaveEvent;
import pwcg.campaign.Campaign;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.crewmember.CrewMember;
import pwcg.core.config.InternationalizationManager;
import pwcg.core.exception.PWCGException;
import pwcg.core.exception.PWCGUserException;
import pwcg.core.utils.DateUtils;
import pwcg.core.utils.PWCGLogger;
import pwcg.gui.CampaignGuiContextManager;
import pwcg.gui.ScreenIdentifier;
import pwcg.gui.UiImageResolver;
import pwcg.gui.campaign.home.CampaignHomeScreen;
import pwcg.gui.colors.ColorMap;
import pwcg.gui.dialogs.ErrorDialog;
import pwcg.gui.dialogs.HelpDialog;
import pwcg.gui.dialogs.PWCGMonitorFonts;
import pwcg.gui.sound.SoundManager;
import pwcg.gui.utils.ImageResizingPanel;
import pwcg.gui.utils.ImageResizingPanelBuilder;
import pwcg.gui.utils.ImageToDisplaySizer;
import pwcg.gui.utils.PWCGButtonFactory;
import pwcg.gui.utils.PWCGLabelFactory;
import pwcg.gui.utils.PwcgBorderFactory;
import pwcg.gui.utils.SpacerPanelFactory;

public class CampaignLeaveScreen extends ImageResizingPanel implements ActionListener
{
    private static final long serialVersionUID = 1L;

    private CampaignHomeScreen parent = null;
    private Campaign campaign = null;
    private JTextField tLeaveTime;
    private int gridRow = 1;

    public CampaignLeaveScreen(CampaignHomeScreen parent)
    {
        super("");
        this.setLayout(new GridBagLayout());
        this.setOpaque(false);

        this.parent = parent;
        this.campaign = PWCGContext.getInstance().getCampaign();
    }

    public void makeVisible(boolean visible)
    {
    }

    public void makePanels() throws PWCGException
    {
        String imagePath = UiImageResolver.getImage(ScreenIdentifier.CampaignLeaveScreen);
        this.setImageFromName(imagePath);

        GridBagConstraints constraints = initializeGridbagConstraints();

        constraints.weightx = 0.1;
        constraints.gridx = 0;
        constraints.gridy = 0;
        this.add(makeNavPanel(), constraints);

        constraints.weightx = 0.1;
        constraints.gridx = 1;
        constraints.gridy = 0;
        this.add(makeLeaveCenterPanel(), constraints);
        
        constraints.weightx = 0.5;
        constraints.gridx = 2;
        constraints.gridy = 0;
        this.add(SpacerPanelFactory.makeDocumentSpacerPanel(1400), constraints);

    }

    private JPanel makeNavPanel() throws PWCGException
    {
        JPanel navPanel = new JPanel(new BorderLayout());
        navPanel.setOpaque(false);

        JPanel leaveButtonPanel = new JPanel(new GridLayout(0, 1));
        leaveButtonPanel.setOpaque(false);

        JButton acceptButton = PWCGButtonFactory.makeTranslucentMenuButton("Accept Leave", "Accept Leave", "Take leave for entered number of days", this);
        leaveButtonPanel.add(acceptButton);

        JLabel spacer = PWCGLabelFactory.makeDummyLabel();
        leaveButtonPanel.add(spacer);

        JButton rejectButton = PWCGButtonFactory.makeTranslucentMenuButton("Reject Leave", "Reject Leave", "Do not ake leave", this);
        leaveButtonPanel.add(rejectButton);

        navPanel.add(leaveButtonPanel, BorderLayout.NORTH);

        return navPanel;
    }

    private JPanel makeLeaveCenterPanel() throws PWCGException
    {
        JPanel leaveCenterPanel = new JPanel();
        leaveCenterPanel.setOpaque(false);
        leaveCenterPanel.setLayout(new BorderLayout());

        JPanel leaveNotification = makeLeaveLetterPanel();
        leaveCenterPanel.add(leaveNotification, BorderLayout.CENTER);

        ImageToDisplaySizer.setDocumentSize(leaveCenterPanel);

        return leaveCenterPanel;
    }

    private JPanel makeLeaveLetterPanel() throws PWCGException
    {
        String imagePath = UiImageResolver.getImage(ScreenIdentifier.Document);
        ImageResizingPanel leaveLetterPanel = ImageResizingPanelBuilder.makeImageResizingPanel(imagePath);
        leaveLetterPanel.setBorder(PwcgBorderFactory.createDocumentBorderWithExtraSpaceFromTop());

        leaveLetterPanel.setLayout(new BorderLayout());
        leaveLetterPanel.setOpaque(false);

        JPanel leaveLetterGrid = new JPanel(new GridLayout(0, 1));
        leaveLetterGrid.setOpaque(false);

        JPanel leavePlayerWoundInfoPanel = makePlayerWoundHealTimePanel();
        leaveLetterGrid.add(leavePlayerWoundInfoPanel);

        JPanel leaveRequestPanel = makeLeaveRequestRow();
        leaveLetterGrid.add(leaveRequestPanel);

        leaveLetterPanel.add(leaveLetterGrid, BorderLayout.NORTH);

        return leaveLetterPanel;
    }

    private JPanel makePlayerWoundHealTimePanel() throws PWCGException
    {
        Font font = PWCGMonitorFonts.getPrimaryFontLarge();

        tLeaveTime = new JTextField(5);
        tLeaveTime.setOpaque(false);
        tLeaveTime.setFont(font);

        JPanel leaveTimeToHealPanel = new JPanel();
        GridBagLayout documentLayout = new GridBagLayout();
        leaveTimeToHealPanel.setLayout(documentLayout);

        leaveTimeToHealPanel.setOpaque(false);

        GridBagConstraints constraints = initializeGridbagConstraints();

        constraints.weightx = 0.5;
        constraints.weighty = 0.1;
        constraints.gridx = 1;
        constraints.gridheight = 1;

        for (CrewMember player : campaign.getPersonnelManager().getAllActivePlayers().getCrewMemberList())
        {
            if (player.getRecoveryDate() != null)
            {
                constraints.gridy = gridRow;
                ++gridRow;

                int daysToHeal = DateUtils.daysDifference(campaign.getDate(), player.getRecoveryDate()) + 1;
                String requires = " " + InternationalizationManager.getTranslation("requires") + " ";
                String daysToRecover = " " + InternationalizationManager.getTranslation("days to recover from his wounds");
                String playerWoundHealTimeDesc = player.getNameAndRank() + requires + daysToHeal + daysToRecover;
                playerWoundHealTimeDesc += ": ";
                JLabel playerWoundHealTimeLabel = PWCGLabelFactory.makeTransparentLabel(playerWoundHealTimeDesc, ColorMap.PAPER_FOREGROUND, font, SwingConstants.LEFT);                
                leaveTimeToHealPanel.add(playerWoundHealTimeLabel, constraints);
            }
        }

        return leaveTimeToHealPanel;
    }

    private JPanel makeLeaveRequestRow() throws PWCGException
    {
        Font font = PWCGMonitorFonts.getPrimaryFontLarge();
        Color buttonBG = ColorMap.PAPER_BACKGROUND;

        String leaveText = InternationalizationManager.getTranslation("Request Leave Time (days)");
        leaveText += ": ";
        JLabel lLeave = PWCGLabelFactory.makeTransparentLabel(leaveText, ColorMap.PAPER_FOREGROUND, font,SwingConstants.LEFT);

        tLeaveTime = new JTextField(5);
        tLeaveTime.setBackground(buttonBG);
        tLeaveTime.setOpaque(false);
        tLeaveTime.setFont(font);

        JPanel leaveRequestPanel = new JPanel();
        GridBagLayout documentLayout = new GridBagLayout();
        leaveRequestPanel.setLayout(documentLayout);

        leaveRequestPanel.setOpaque(false);

        GridBagConstraints constraints = initializeGridbagConstraints();

        constraints.gridy = gridRow;
        ++gridRow;

        constraints.weightx = 0.01;
        constraints.weighty = 0.1;
        constraints.gridx = 1;
        constraints.gridheight = 1;
        leaveRequestPanel.add(lLeave, constraints);

        constraints.weightx = 0.1;
        constraints.weighty = 0.1;
        constraints.gridx = 2;
        constraints.gridheight = 5;
        leaveRequestPanel.add(tLeaveTime, constraints);

        return leaveRequestPanel;
    }

    private GridBagConstraints initializeGridbagConstraints()
    {
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.ipadx = 3;
        constraints.ipady = 3;
        constraints.anchor = GridBagConstraints.NORTHWEST;
        Insets margins = new Insets(0, 50, 50, 0);
        constraints.insets = margins;
        return constraints;
    }

    public void actionPerformed(ActionEvent ae)
    {
        String action = ae.getActionCommand();

        try
        {
            if (action.equalsIgnoreCase("Accept Leave"))
            {
                crewMemberLeave();
            }
            else if (action.equalsIgnoreCase("Reject Leave"))
            {
                SoundManager.getInstance().playSound("Stapling.WAV");
                CampaignGuiContextManager.getInstance().popFromContextStack();
            }
        }
        catch (Exception e)
        {
            PWCGLogger.logException(e);
            ErrorDialog.internalError(e.getMessage());
        }
    }

    private void crewMemberLeave() throws PWCGUserException, Exception
    {
        SoundManager.getInstance().playSound("Stapling.WAV");

        int leaveTimeDays = getLeaveTime();
        if (leaveTimeDays > 0)
        {
            boolean isNewsWorthy = false;
            CrewMember referencePlayer = campaign.findReferencePlayer();
            LeaveEvent leaveEvent = new LeaveEvent(campaign, leaveTimeDays, referencePlayer.getCompanyId(), referencePlayer.getSerialNumber(), campaign.getDate(),
                    isNewsWorthy);
            parent.campaignTimePassedForLeave(leaveEvent.getLeaveTime());
        }
        else
        {
            new  HelpDialog("Enter leave in days continuing");
        }
    }

    public int getLeaveTime() throws PWCGUserException, Exception
    {
        if (tLeaveTime.getText() == null || tLeaveTime.getText().length() == 0)
        {
            return 0;
        }

        int leaveTime = Integer.valueOf(tLeaveTime.getText()).intValue();

        return leaveTime;
    }
}
