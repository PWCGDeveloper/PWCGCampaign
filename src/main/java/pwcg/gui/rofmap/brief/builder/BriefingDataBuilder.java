package pwcg.gui.rofmap.brief.builder;

import java.util.Map;

import pwcg.campaign.squadmember.SquadronMember;
import pwcg.core.exception.PWCGException;
import pwcg.gui.rofmap.brief.BriefingMissionFlightSetBuilder;
import pwcg.gui.rofmap.brief.model.BriefingData;
import pwcg.gui.rofmap.brief.model.BriefingFlight;
import pwcg.mission.Mission;

public class BriefingDataBuilder
{
    private Mission mission;
    
    public BriefingDataBuilder(Mission mission)
    {
        this.mission = mission;
    }
    
    public BriefingData buildBriefingData() throws PWCGException
    {
        Map<Integer, BriefingFlight> briefingMissionFlights = buildBriefingMissions();

        BriefingData briefingData = new BriefingData(mission, briefingMissionFlights);

        int initialSquadronId = getInitialSelectedSquadron();
        briefingData.changeSelectedFlight(initialSquadronId);
        
        String missionTIme = getTime();
        briefingData.setMissionTime(missionTIme);
        
        return briefingData;
    }
    
    private String getTime() throws PWCGException
    {
        return mission.getMissionOptions().getMissionTime().getMissionTime();
    }

    private Map<Integer, BriefingFlight> buildBriefingMissions() throws PWCGException
    {
        Map<Integer, BriefingFlight> briefingMissionFlights = BriefingMissionFlightSetBuilder.buildBriefingMissions(mission);
        return briefingMissionFlights;
    }

    private int getInitialSelectedSquadron() throws PWCGException
    {
        SquadronMember referencePlayer = mission.getCampaign().findReferencePlayer();
        return referencePlayer.getSquadronId();
    }
}
