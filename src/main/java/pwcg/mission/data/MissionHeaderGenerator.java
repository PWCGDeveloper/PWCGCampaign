package pwcg.mission.data;

import pwcg.campaign.Campaign;
import pwcg.campaign.company.Company;
import pwcg.campaign.context.PWCGContext;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.DateUtils;
import pwcg.mission.Mission;
import pwcg.mission.io.MissionFileNameBuilder;
import pwcg.mission.playerunit.PlayerUnit;

public class MissionHeaderGenerator
{

    public MissionHeader generateMissionHeader(Campaign campaign, Mission mission) throws PWCGException
    {
        // Even for Coop flights we have to set the header.  Doesn't really matter which flight 
        // as long as it is a player flight
        PlayerUnit myUnit = mission.getUnits().getReferencePlayerUnit();
        Company mySquadron = myUnit.getCompany();
        
        MissionHeader missionHeader = new MissionHeader();
        
        String missionFileName = MissionFileNameBuilder.buildMissionFileName(campaign) ;
        missionHeader.setMissionFileName(missionFileName);
        
        missionHeader.setBase(mySquadron.determineCurrentAirfieldName(campaign.getDate()));
        missionHeader.setDate(DateUtils.getDateStringYYYYMMDD(campaign.getDate()));
        missionHeader.setSquadron(mySquadron.determineDisplayName(campaign.getDate()));
        missionHeader.setVehicleType(myUnit.getLeadVehicle().getDisplayName());

        
        missionHeader.setDuty("" + myUnit.getUnitInformation().getUnitMissionType()); 
        
        missionHeader.setMapName(PWCGContext.getInstance().getCurrentMap().getMapName()); 

        String formattedTime = DateUtils.getDateAsMissionFileFormat(campaign.getDate());
        missionHeader.setTime(formattedTime);
        
        return missionHeader;
    }
}
