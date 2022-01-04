package pwcg.mission.data;

import pwcg.campaign.Campaign;
import pwcg.campaign.company.Company;
import pwcg.campaign.context.PWCGContext;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.DateUtils;
import pwcg.mission.Mission;
import pwcg.mission.flight.IFlight;
import pwcg.mission.io.MissionFileNameBuilder;

public class MissionHeaderGenerator
{

    public MissionHeader generateMissionHeader(Campaign campaign, Mission mission) throws PWCGException
    {
        // Even for Coop flights we have to set the header.  Doesn't really matter which flight 
        // as long as it is a player flight
        IFlight myFlight = mission.getFlights().getReferencePlayerFlight();
        Company mySquadron =myFlight.getSquadron();
        
        MissionHeader missionHeader = new MissionHeader();
        
        String missionFileName = MissionFileNameBuilder.buildMissionFileName(campaign) ;
        missionHeader.setMissionFileName(missionFileName);
        
        missionHeader.setAirfield(mySquadron.determineCurrentAirfieldName(campaign.getDate()));
        missionHeader.setDate(DateUtils.getDateStringYYYYMMDD(campaign.getDate()));
        missionHeader.setSquadron(mySquadron.determineDisplayName(campaign.getDate()));
        missionHeader.setAircraftType(myFlight.getFlightPlanes().getFlightLeader().getDisplayName());

        
        missionHeader.setDuty("" + myFlight.getFlightType());  // String to enum and back
        missionHeader.setAltitude(myFlight.getFlightInformation().getAltitude()); 
        
        missionHeader.setMapName(PWCGContext.getInstance().getCurrentMap().getMapName()); 

        String formattedTime = DateUtils.getDateAsMissionFileFormat(campaign.getDate());
        missionHeader.setTime(formattedTime);
        
        return missionHeader;
    }
}
