package pwcg.gui.rofmap.debrief;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import pwcg.aar.AARCoordinator;
import pwcg.campaign.Campaign;
import pwcg.campaign.crewmember.CrewMember;
import pwcg.core.exception.PWCGException;
import pwcg.gui.colors.ColorMap;
import pwcg.gui.dialogs.PWCGMonitorBorders;
import pwcg.gui.dialogs.PWCGMonitorFonts;
import pwcg.gui.utils.ContextSpecificImages;
import pwcg.gui.utils.ImageResizingPanel;
import pwcg.gui.utils.ScrollBarWrapper;

public class DebriefDescriptionChalkboard extends ImageResizingPanel
{

    private static final long serialVersionUID = 1L;

    private Campaign campaign;
    private JTextArea missionTextArea = new JTextArea();
    private AARCoordinator aarCoordinator;

    public DebriefDescriptionChalkboard(Campaign campaign, AARCoordinator aarCoordinator)
    {
        super("");
        this.setLayout(new BorderLayout());
        this.setOpaque(false);
        
        this.campaign = campaign;
        this.aarCoordinator = aarCoordinator;
    }
    
    public void makePanel() throws PWCGException 
    {
        String imagePath = ContextSpecificImages.imagesMisc() + "Chalkboard.png";
        JPanel briefingPanel = new ImageResizingPanel(imagePath);
        briefingPanel.setLayout(new BorderLayout());
        briefingPanel.setOpaque(false);
        briefingPanel.setBorder(BorderFactory.createEmptyBorder(75,100,50,50));

        JPanel missionTextPanel = makeMissionTextArea();
        
        JScrollPane missionScrollPane = ScrollBarWrapper.makeScrollPane(missionTextPanel);

        briefingPanel.add(missionScrollPane, BorderLayout.CENTER);
        
        this.add(briefingPanel, BorderLayout.CENTER);
    }

    private JPanel makeMissionTextArea() throws PWCGException 
    {
        JPanel missionTextPanel = new JPanel(new BorderLayout());
        missionTextPanel.setOpaque(false);
        
        Font font = PWCGMonitorFonts.getBriefingChalkboardFont();

        missionTextArea.setFont(font);
        missionTextArea.setOpaque(false);
        missionTextArea.setLineWrap(true);
        missionTextArea.setWrapStyleWord(true);
        missionTextArea.setForeground(ColorMap.CHALK_FOREGROUND);
        
        Insets margins = PWCGMonitorBorders.calculateBorderMargins(50, 35, 65, 35);
        missionTextArea.setMargin(margins);
        
        String missionText = makeMissionText();
        missionTextArea.setText(missionText);
        
        missionTextPanel.add(missionTextArea, BorderLayout.CENTER);

        return missionTextPanel;
    }

    private String makeMissionText() throws PWCGException
    {
        String missionText = "Assigned Personnel:\n";
        
        for (CrewMember crewMemberInMission : aarCoordinator.getAarContext().getPreliminaryData().getCampaignMembersInMission().getCrewMemberCollection().values())
        {            
            CrewMember referencePlayer = campaign.findReferencePlayer();
            if (crewMemberInMission.getCompanyId() == referencePlayer.getCompanyId())
            {
                missionText += "             " + crewMemberInMission.getNameAndRank();
                missionText += "\n";
            }
        }

        missionText += "\n";
        missionText += aarCoordinator.getAarContext().getPreliminaryData().getPwcgMissionData().getMissionDescription();
        
        return missionText;
        
    }
}
