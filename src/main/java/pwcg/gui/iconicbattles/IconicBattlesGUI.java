package pwcg.gui.iconicbattles;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.SwingConstants;

import pwcg.campaign.ArmedService;
import pwcg.campaign.Campaign;
import pwcg.campaign.CampaignGenerator;
import pwcg.campaign.CampaignGeneratorModel;
import pwcg.campaign.CampaignMode;
import pwcg.campaign.api.IRankHelper;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.factory.RankFactory;
import pwcg.campaign.skirmish.IconicMissionsManager;
import pwcg.campaign.skirmish.IconicSingleMission;
import pwcg.campaign.squadmember.SquadronMember;
import pwcg.campaign.squadron.Squadron;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.DateUtils;
import pwcg.core.utils.PWCGLogger;
import pwcg.gui.CampaignGuiContextManager;
import pwcg.gui.ScreenIdentifier;
import pwcg.gui.UiImageResolver;
import pwcg.gui.colors.ColorMap;
import pwcg.gui.dialogs.ErrorDialog;
import pwcg.gui.dialogs.PWCGMonitorFonts;
import pwcg.gui.rofmap.brief.BriefingDescriptionScreen;
import pwcg.gui.rofmap.brief.CampaignHomeGuiBriefingWrapper;
import pwcg.gui.utils.ImageResizingPanel;
import pwcg.gui.utils.PWCGButtonFactory;
import pwcg.mission.Mission;
import pwcg.mission.MissionGenerator;
import pwcg.mission.MissionHumanParticipants;

public class IconicBattlesGUI extends ImageResizingPanel implements ActionListener
{
	private static final long serialVersionUID = 1L;
    private ButtonGroup buttonGroup = new ButtonGroup();
    private String iconicBattleKey;
    private int selectedSquadron = 0;

	public IconicBattlesGUI(String iconicBattleKey) 
	{		
        super("");
        
        this.iconicBattleKey = iconicBattleKey;
        
        setLayout(new BorderLayout());
	}

	public void makeGUI() throws PWCGException 
	{
        String imagePath = UiImageResolver.getImage(ScreenIdentifier.Document);
        this.setImageFromName(imagePath);
        this.setBorder(BorderFactory.createEmptyBorder(50,50,50,100));
        
        JPanel squadronPanel = makeSquadronPanel();
        this.add(squadronPanel, BorderLayout.CENTER);
	}

    public JPanel makeSquadronPanel() throws PWCGException  
    {
        JPanel configPanel = new JPanel(new BorderLayout());
        configPanel.setOpaque(false);

        JPanel buttonPanel = new JPanel(new GridLayout(0,1));
        buttonPanel.setOpaque(false);
        
        buttonPanel.add(PWCGButtonFactory.makeMenuLabelLarge("Iconic Mission Squadrons:"));
        buttonPanel.add(PWCGButtonFactory.makeMenuLabelLarge("   "));

        IconicSingleMission iconicMission = IconicMissionsManager.getInstance().getSelectedMissionProfile(iconicBattleKey);
        for (Integer squadronId : iconicMission.getIconicBattleParticipants())
        {
            String description = formDescription(squadronId);
            buttonPanel.add(makeCategoryRadioButton(description, squadronId));
        }

        buttonPanel.add(PWCGButtonFactory.makeMenuLabelLarge("   "));
        buttonPanel.add(PWCGButtonFactory.makeMenuLabelLarge("   "));
        buttonPanel.add(PWCGButtonFactory.makePaperButtonWithBorder("Generate Mission", "Generate Mission", this));
        
        add (buttonPanel);

        configPanel.add(buttonPanel, BorderLayout.NORTH);
        
        return configPanel;
    }

    private String formDescription(Integer squadronId) throws PWCGException
    {
        Squadron squadron = PWCGContext.getInstance().getSquadronManager().getSquadron(squadronId);
        Date iconicBattleDate = DateUtils.getDateYYYYMMDD(iconicBattleKey);
        String description = squadron.determineDisplayName(iconicBattleDate) + " flying " + squadron.determineBestPlane(iconicBattleDate).getDisplayName();
        return description;
    }

    private JRadioButton makeCategoryRadioButton(String buttonText, Integer squadronId) throws PWCGException 
    {
        Color fgColor = ColorMap.PAPER_FOREGROUND;

        Font font = PWCGMonitorFonts.getPrimaryFont();

        JRadioButton button = new JRadioButton(buttonText);
        button.setActionCommand("" + squadronId);
        button.setHorizontalAlignment(SwingConstants.LEFT );
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.addActionListener(this);
        button.setOpaque(false);
        button.setForeground(fgColor);
        button.setFont(font);

        buttonGroup.add(button);

        return button;
    }


    @Override
    public void actionPerformed(ActionEvent ae)
    {
        try
        {
            String action = ae.getActionCommand();
            if (action.equalsIgnoreCase("Generate Mission"))
            {
                if (selectedSquadron > 0)
                {
                    Campaign campaign = generateCampaign();
                    Mission mission = generateMission(campaign);
                    CampaignHomeGuiBriefingWrapper campaignHomeGuiBriefingWrapper = new CampaignHomeGuiBriefingWrapper(null);
                    BriefingDescriptionScreen briefingMap = new BriefingDescriptionScreen(campaignHomeGuiBriefingWrapper, mission);
                    briefingMap.makePanels();
                    CampaignGuiContextManager.getInstance().pushToContextStack(briefingMap);
                }
                return;
            }
            else
            {
                selectedSquadron = Integer.valueOf(action);
            }
        }
        catch (Throwable e)
        {
            PWCGLogger.logException(e);
            ErrorDialog.internalError(e.getMessage());
        }
    }

    private Campaign generateCampaign() throws PWCGException
    {
        CampaignGeneratorModel model = makeCampaignModelForProfile();        
        CampaignGenerator generator = new CampaignGenerator(model);
        Campaign campaign = generator.generate();          
        PWCGContext.getInstance().setCampaign(campaign);
        return campaign;
    }

    private CampaignGeneratorModel makeCampaignModelForProfile() throws PWCGException
    {
        IconicSingleMission iconicMission = IconicMissionsManager.getInstance().getSelectedMissionProfile(iconicBattleKey);
        Squadron squadron = PWCGContext.getInstance().getSquadronManager().getSquadron(selectedSquadron);
        Date campaignDate = DateUtils.getDateYYYYMMDD(iconicBattleKey);

        ArmedService service = squadron.determineServiceForSquadron(campaignDate);
        String squadronName = squadron.determineDisplayName(campaignDate);
        
        IRankHelper rank = RankFactory.createRankHelper();
        String rankName = rank.getRankByService(2, service);
    
        CampaignGeneratorModel generatorModel = new CampaignGeneratorModel();
        generatorModel.setCampaignDate(campaignDate);
        generatorModel.setCampaignName(iconicMission.getMapName() + " " + iconicMission.getCampaignName());
        generatorModel.setPlayerName("Iconic Player");
        generatorModel.setPlayerRank(rankName);
        generatorModel.setPlayerRegion("");
        generatorModel.setService(service);
        generatorModel.setSquadronName(squadronName);
        generatorModel.setCampaignMode(CampaignMode.CAMPAIGN_MODE_SINGLE);

        return generatorModel;
    }

    private MissionHumanParticipants buildTestParticipatingHumans(Campaign campaign) throws PWCGException
    {
        MissionHumanParticipants participatingPlayers = new MissionHumanParticipants();
        for (SquadronMember player: campaign.getPersonnelManager().getAllActivePlayers().getSquadronMemberList())
        {
            participatingPlayers.addSquadronMember(player);
        }
        return participatingPlayers;
    }

    private Mission generateMission(Campaign campaign) throws PWCGException
    {
        MissionGenerator missionGenerator = new MissionGenerator(campaign);
        Mission mission = missionGenerator.makeMission(buildTestParticipatingHumans(campaign));
        return mission;
        
        // mission.finalizeMission();
        // mission.writeGameMissionFiles();
    }

}
