package pwcg.campaign.group.airfield.staticobject;

import java.util.ArrayList;
import java.util.List;

import pwcg.campaign.Campaign;
import pwcg.campaign.context.Country;
import pwcg.campaign.group.airfield.Airfield;
import pwcg.core.config.ConfigItemKeys;
import pwcg.core.config.ConfigManagerCampaign;
import pwcg.core.config.ConfigSimple;
import pwcg.core.constants.AiSkillLevel;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.location.PWCGLocation;
import pwcg.core.utils.MathUtils;
import pwcg.mission.Mission;
import pwcg.mission.ground.GroundUnitSize;
import pwcg.mission.ground.builder.AAAUnitBuilder;
import pwcg.mission.ground.org.GroundUnitCollection;
import pwcg.mission.ground.org.IGroundUnit;
import pwcg.mission.target.TargetDefinition;
import pwcg.mission.target.TargetType;

public class AirfieldApproachAABuilder
{
    private List<GroundUnitCollection> airfieldApproachAA = new ArrayList<>();
    private Campaign campaign;
    private Mission mission;
    private Airfield airfield;
    
    public AirfieldApproachAABuilder (Mission mission, Airfield airfield)
    {
        this.campaign = mission.getCampaign();
        this.mission = mission;
        this.airfield = airfield;
    }
    
    public List<GroundUnitCollection> addAirfieldApproachAA() throws PWCGException
    {
        if (airfield.createCountry(campaign.getDate()).getCountry() == Country.NEUTRAL)
        {
            return airfieldApproachAA;
        }
        
        PWCGLocation landingPosition = airfield.getLandingLocation(mission);
        double aaProgressionAngle = airfield.getTakeoffLocation(mission).getOrientation().getyOri();
        Coordinate aaStartPosition = MathUtils.calcNextCoord(landingPosition.getPosition(), aaProgressionAngle, 500.0);
        
        List<Coordinate> aaCoordinates = buildAirfieldAAPositions(aaProgressionAngle, aaProgressionAngle, aaStartPosition);
        buildFlightPathAA(aaCoordinates);

        return airfieldApproachAA;
    }

    private List<Coordinate> buildAirfieldAAPositions(double angleOut, double aaProgressionAngle, Coordinate aaStartPosition) throws PWCGException
    {
        double angleLeft = MathUtils.adjustAngle(aaProgressionAngle, 270);
        double angleRight = MathUtils.adjustAngle(aaProgressionAngle, 90);

        List<Coordinate> aaCoordinates = new ArrayList<>();
        int numPairs = getNumAAGunPairs();
        for (int i = 0; i < numPairs; ++i)
        {
            Coordinate aaCenterCoordinate = MathUtils.calcNextCoord(aaStartPosition, angleOut, (i * 1000));
            Coordinate leftAAPoint = MathUtils.calcNextCoord(aaCenterCoordinate, angleLeft, 500);
            Coordinate rightAAPoint = MathUtils.calcNextCoord(aaCenterCoordinate, angleRight, 500);
            
            aaCoordinates.add(leftAAPoint);
            aaCoordinates.add(rightAAPoint);
        }
        return aaCoordinates;
    }

    private int getNumAAGunPairs() throws PWCGException
    {
        int numAAGunPairs = 2;
        ConfigManagerCampaign configManager = campaign.getCampaignConfigManager();
        String currentAASetting = configManager.getStringConfigParam(ConfigItemKeys.SimpleConfigAAKey);
        if (currentAASetting.equals(ConfigSimple.CONFIG_LEVEL_LOW))
        {
            numAAGunPairs = 2;
        }
        else if (currentAASetting.equals(ConfigSimple.CONFIG_LEVEL_MED))
        {
            numAAGunPairs = 3;
        }
        else if (currentAASetting.equals(ConfigSimple.CONFIG_LEVEL_HIGH))
        {
            numAAGunPairs = 3;
        }
        return numAAGunPairs;
    }

    private void buildFlightPathAA(List<Coordinate> aaCoordinates) throws PWCGException
    {
        for (Coordinate aaPoint : aaCoordinates)
        {            
            TargetDefinition targetDefinition = new TargetDefinition(TargetType.TARGET_ARTILLERY, aaPoint, airfield.createCountry(campaign.getDate()));
            AAAUnitBuilder groundUnitFactory = new AAAUnitBuilder(campaign, targetDefinition);

            GroundUnitCollection aaa = groundUnitFactory.createAAAArtilleryBattery(GroundUnitSize.GROUND_UNIT_SIZE_TINY);
            if (aaa != null)
            {
                for (IGroundUnit aaGun : aaa.getGroundUnits())
                {
                    aaGun.setAiLevel(AiSkillLevel.COMMON);
                }
                airfieldApproachAA.add(aaa);
            }
        }
    }
}
