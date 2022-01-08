package pwcg.gui.rofmap.brief;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import pwcg.campaign.Campaign;
import pwcg.campaign.context.PWCGContext;
import pwcg.core.exception.PWCGException;
import pwcg.gui.colors.ColorMap;
import pwcg.gui.dialogs.PWCGMonitorBorders;
import pwcg.gui.dialogs.PWCGMonitorFonts;
import pwcg.gui.rofmap.brief.model.BriefingData;
import pwcg.gui.rofmap.brief.model.BriefingUnit;
import pwcg.gui.utils.ContextSpecificImages;
import pwcg.gui.utils.ImageResizingPanel;
import pwcg.gui.utils.ScrollBarWrapper;
import pwcg.mission.IMissionDescription;
import pwcg.mission.Mission;
import pwcg.mission.MissionDescriptionFactory;
import pwcg.mission.playerunit.crew.CrewTankPayloadPairing;

public class BriefingDescriptionChalkboard extends ImageResizingPanel
{

    private static final long serialVersionUID = 1L;

    private JTextArea missionTextArea = new JTextArea();
    private Mission mission;
    private BriefingData briefingContext;

    public BriefingDescriptionChalkboard(Mission mission, BriefingData briefingContext)
    {
        super("");
        this.setLayout(new BorderLayout());
        this.setOpaque(false);
        
        this.mission = mission;
        this.briefingContext = briefingContext;
    }
    
    public void makePanel() throws PWCGException 
    {
        String imagePath = ContextSpecificImages.imagesMisc() + "Chalkboard.png";
        JPanel briefingPanel = new ImageResizingPanel(imagePath);
        briefingPanel.setLayout(new BorderLayout());
        briefingPanel.setOpaque(false);
        briefingPanel.setBorder(BorderFactory.createEmptyBorder(75,100,50,50));

        JPanel missionTextPanel = makeMissionText();
        
        JScrollPane missionScrollPane = ScrollBarWrapper.makeScrollPane(missionTextPanel);

        briefingPanel.add(missionScrollPane, BorderLayout.CENTER);
        
        this.add(briefingPanel, BorderLayout.CENTER);

        setMissionText();
    }

    private JPanel makeMissionText() throws PWCGException 
    {
        JPanel missionTextPanel = new JPanel(new BorderLayout());
        missionTextPanel.setOpaque(false);
        
        Font font = PWCGMonitorFonts.getBriefingChalkboardFont();

        missionTextArea.setFont(font);
        missionTextArea.setOpaque(false);
        missionTextArea.setLineWrap(true);
        missionTextArea.setWrapStyleWord(true);
        missionTextArea.setForeground(ColorMap.CHALK_FOREGROUND);
        
        // Calculate the writable area of the text and generate margins scaled to screen size
        Insets margins = PWCGMonitorBorders.calculateBorderMargins(50, 35, 65, 35);
        missionTextArea.setMargin(margins);
        
        missionTextPanel.add(missionTextArea, BorderLayout.CENTER);

        return missionTextPanel;
    }

    public void setMissionText() throws PWCGException 
    {
        Campaign campaign = PWCGContext.getInstance().getCampaign();

        String missionPrefix = getMissionPrefix();
        
        

        IMissionDescription missionDescription = MissionDescriptionFactory.buildMissionDescription(campaign, mission, briefingContext.getSelectedUnit());
        String missionDescriptionText = missionDescription.createDescription();
        
        StringBuffer missionDescriptionBuffer = new StringBuffer("");
        missionDescriptionBuffer.append(missionPrefix);
        missionDescriptionBuffer.append(missionDescriptionText);

        String crewMemberList = makeCrewMemberList();
        missionDescriptionBuffer.append(crewMemberList.toString());
        
        missionTextArea.setText(missionDescriptionBuffer.toString());
    }

    private String getMissionPrefix()
    {
        String missionPrefix = "Mission: \n";
        return missionPrefix;
    }

    private String makeCrewMemberList() throws PWCGException 
    {
        BriefingUnit activeMissionHandler = briefingContext.getActiveBriefingUnit();
        StringBuffer assignedCrewMembersBuffer = new StringBuffer ("Assigned CrewMembers:\n");
        for (CrewTankPayloadPairing crewPlane : activeMissionHandler.getCrews())
        {
            assignedCrewMembersBuffer.append("    " + crewPlane.getCrewMember().getNameAndRank() + "\n");
        }
        
        return assignedCrewMembersBuffer.toString();
    }

}
