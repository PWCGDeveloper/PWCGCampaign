package pwcg.mission.data;

import pwcg.campaign.Campaign;
import pwcg.campaign.context.PWCGContextManager;
import pwcg.campaign.io.mission.MissionFileWriter;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.DateUtils;
import pwcg.mission.Mission;
import pwcg.mission.flight.Flight;

public class MissionHeaderGenerator
{

    public MissionHeader generateMissionHeader(Campaign campaign, Mission mission) throws PWCGException
    {
        Flight myFlight = mission.getMissionFlightBuilder().getPlayerFlight();
        
        MissionHeader missionHeader = new MissionHeader();
        
        String missionFileName = MissionFileWriter.getMissionFileName(campaign) ;
        missionHeader.setMissionFileName(missionFileName);
        
        missionHeader.setAirfield(campaign.getAirfieldName());
        missionHeader.setDate(DateUtils.getDateStringYYYYMMDD(campaign.getDate()));
        missionHeader.setSquadron(campaign.determineSquadron().determineDisplayName(campaign.getDate()));
        missionHeader.setAircraftType(myFlight.getPlanes().get(0).getDisplayName());

        
        missionHeader.setDuty("" + myFlight.getFlightType());  // String to enum and back
        missionHeader.setAltitude(myFlight.getMaximumFlightAltitude()); 
        
        missionHeader.setMapName(PWCGContextManager.getInstance().getCurrentMap().getMapName()); 

        String formattedTime = DateUtils.getDateAsMissionFileFormat(campaign.getDate());
        missionHeader.setTime(formattedTime);
        
        return missionHeader;
    }
}
