package pwcg.mission;

import java.util.ArrayList;
import java.util.List;

import pwcg.campaign.Campaign;
import pwcg.campaign.api.Side;
import pwcg.campaign.context.FrontLinePoint;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.group.TownFinder;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.location.PWCGLocation;
import pwcg.core.utils.MathUtils;
import pwcg.mission.ground.builder.RadarUnitBuilder;
import pwcg.mission.ground.org.GroundUnitCollection;

public class MissionRadarBuilder
{
    private Mission mission;
    private Campaign campaign;

    private List<GroundUnitCollection> missionRadars = new ArrayList<>();

    public MissionRadarBuilder(Mission mission)
    {
        this.mission = mission;
        this.campaign = mission.getCampaign();
    }

    public List<GroundUnitCollection> buildRadarsForMission() throws PWCGException
    {
        buildRadarsForSide(Side.ALLIED);
        buildRadarsForSide(Side.AXIS);
        return missionRadars;
    }
    
    public void buildRadarsForSide(Side side) throws PWCGException
    {
        List <Coordinate> frontLineReferences = getReferenceFrontLinePositions(side);
        List<Coordinate> radarPositions = getRadarPositions(side, frontLineReferences);
        makeRadars(side, radarPositions);
    }

    private List<Coordinate> getReferenceFrontLinePositions(Side side) throws PWCGException
    {
        List<Coordinate> frontLineReferences = new ArrayList<>();
        List<FrontLinePoint> frontLinesForSide = PWCGContext.getInstance().getMap(campaign.getCampaignMap()).getFrontLinesForMap(mission.getCampaign().getDate()).getFrontLines(side);
        FrontLinePoint referencePoint = frontLinesForSide.get(0);
        for (FrontLinePoint frontLinePoint :  frontLinesForSide)
        {
            double distance = MathUtils.calcDist(referencePoint.getPosition(), frontLinePoint.getPosition());
            if (distance > 30000)
            {
                frontLineReferences.add(frontLinePoint.getPosition().copy());
                referencePoint = frontLinePoint;
            }
        }
        return frontLineReferences;
    }

    private List<Coordinate> getRadarPositions(Side side, List<Coordinate> frontLineReferences) throws PWCGException
    {
        List<Coordinate> radarPositions = new ArrayList<>();
        for (Coordinate frontLineReference :  frontLineReferences)
        {
            TownFinder townFinder = PWCGContext.getInstance().getMap(campaign.getCampaignMap()).getGroupManager().getTownFinder();
            PWCGLocation town = townFinder.findClosestTownForSide(campaign.getCampaignMap(), side, mission.getCampaign().getDate(), frontLineReference);
            double angle = MathUtils.calcAngle(frontLineReference, town.getPosition());
            Coordinate radarPosition = MathUtils.calcNextCoord(campaign.getCampaignMap(), town.getPosition(), angle, 5000);
            radarPositions.add(radarPosition);
        }
        return radarPositions;
    }

    private void makeRadars(Side side, List<Coordinate> radarPositions) throws PWCGException
    {
        for (Coordinate radarPosition :  radarPositions)
        {
            RadarUnitBuilder radarFactory = new RadarUnitBuilder(mission.getCampaign(), radarPosition, side);
            GroundUnitCollection radar = radarFactory.createRadarForSide();
            missionRadars.add(radar);
        }
    }

}
