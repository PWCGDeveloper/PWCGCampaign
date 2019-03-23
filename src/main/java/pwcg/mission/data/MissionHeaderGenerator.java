package pwcg.mission.data;

import pwcg.campaign.Campaign;
import pwcg.campaign.context.PWCGContextManager;
import pwcg.campaign.squadron.Squadron;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.DateUtils;
import pwcg.mission.Mission;
import pwcg.mission.flight.Flight;
import pwcg.mission.io.MissionFileWriter;

public class MissionHeaderGenerator
{

    public MissionHeader generateMissionHeader(Campaign campaign, Mission mission) throws PWCGException
    {
        // Even for Coop flights we have to set the header.  Doesn't really matter which flight 
        // as long as itis a player flight
        Flight myFlight = mission.getMissionFlightBuilder().getReferencePlayerFlight();
        Squadron mySquadron =myFlight.getSquadron();
        
        MissionHeader missionHeader = new MissionHeader();
        
        String missionFileName = MissionFileWriter.getMissionFileName(campaign) ;
        missionHeader.setMissionFileName(missionFileName);
        
        missionHeader.setAirfield(mySquadron.determineCurrentAirfieldName(campaign.getDate()));
        missionHeader.setDate(DateUtils.getDateStringYYYYMMDD(campaign.getDate()));
        missionHeader.setSquadron(mySquadron.determineDisplayName(campaign.getDate()));
        missionHeader.setAircraftType(myFlight.getPlanes().get(0).getDisplayName());

        
        missionHeader.setDuty("" + myFlight.getFlightType());  // String to enum and back
        missionHeader.setAltitude(myFlight.getMaximumFlightAltitude()); 
        
        missionHeader.setMapName(PWCGContextManager.getInstance().getCurrentMap().getMapName()); 

        String formattedTime = DateUtils.getDateAsMissionFileFormat(campaign.getDate());
        missionHeader.setTime(formattedTime);
        
        return missionHeader;
    }
}
