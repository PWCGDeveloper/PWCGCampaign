package pwcg.mission.ground.factory;

import java.util.Date;

import pwcg.campaign.Campaign;
import pwcg.campaign.api.ICountry;
import pwcg.campaign.context.PWCGContextManager;
import pwcg.campaign.group.Block;
import pwcg.campaign.group.GroupManager;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.mission.MissionBeginUnitCheckZone;
import pwcg.mission.ground.unittypes.transport.GroundTrainUnit;
import pwcg.mission.mcu.Coalition;

public class TrainUnitFactory
{
    private Campaign campaign;
    private Coordinate location;
    private ICountry country;
    private Date date;

    public TrainUnitFactory (Campaign campaign, Coordinate location, ICountry country, Date date)
    {
        this.campaign  = campaign;
        this.location  = location.copy();
        this.country  = country;
        this.date  = date;
    }

    public GroundTrainUnit createTrainUnit () throws PWCGException
    {
        GroundTrainUnit trainTarget = null;
        Coordinate destination = getTrainDestination();
        Coalition playerCoalition = Coalition.getFriendlyCoalition(campaign.determineCountry());
        
        MissionBeginUnitCheckZone missionBeginUnit = new MissionBeginUnitCheckZone();
        missionBeginUnit.initialize(location, 50000, playerCoalition);
        
        trainTarget = new GroundTrainUnit(campaign);
        trainTarget.initialize (missionBeginUnit, location, destination, country);
        trainTarget.createUnitMission();
        
        return trainTarget;
    }

    private Coordinate getTrainDestination() throws PWCGException
    {
        GroupManager groupData =  PWCGContextManager.getInstance().getCurrentMap().getGroupManager();
        Block destinationStation = groupData.getRailroadStationFinder().getDestinationTrainPosition(location, country, date);
        Coordinate destination = destinationStation.getPosition();
        if (destinationStation != null)
        {
            destination = destinationStation.getPosition();
        }
        return destination;
    }

}
