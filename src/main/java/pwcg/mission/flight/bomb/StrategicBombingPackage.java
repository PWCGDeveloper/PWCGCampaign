package pwcg.mission.flight.bomb;

import java.util.ArrayList;
import java.util.List;

import pwcg.campaign.Campaign;
import pwcg.campaign.api.IAirfield;
import pwcg.campaign.api.Side;
import pwcg.campaign.context.PWCGContextManager;
import pwcg.campaign.plane.Role;
import pwcg.campaign.squadron.Squadron;
import pwcg.campaign.target.StrategicTargetBuilder;
import pwcg.campaign.target.TargetDefinition;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.utils.MathUtils;
import pwcg.core.utils.RandomNumberGenerator;
import pwcg.mission.Mission;
import pwcg.mission.MissionBeginUnit;
import pwcg.mission.flight.Flight;
import pwcg.mission.flight.FlightInformation;
import pwcg.mission.flight.FlightInformationFactory;
import pwcg.mission.flight.FlightPackage;
import pwcg.mission.flight.FlightTypes;
import pwcg.mission.flight.bomb.BombingWaypoints.BombingAltitudeLevel;
import pwcg.mission.flight.intercept.InterceptFlight;
import pwcg.mission.ground.GroundUnitFactory;
import pwcg.mission.ground.factory.AAAUnitFactory;
import pwcg.mission.ground.unittypes.GroundUnitSpawning;
import pwcg.mission.ground.unittypes.SpotLightGroup;

public class StrategicBombingPackage extends FlightPackage
{
    public StrategicBombingPackage(Mission mission, Campaign campaign, Squadron squadron, boolean isPlayerFlight)
    {
        super(mission, campaign, squadron, isPlayerFlight);
        this.flightType = FlightTypes.STRATEGIC_BOMB;
    }


    public Flight createPackage() throws PWCGException
    {
        StrategicTargetBuilder strategicTargetBuilder = new StrategicTargetBuilder();
        TargetDefinition targetDefinition = strategicTargetBuilder.getStrategicTarget(campaign, mission, squadron);

        StrategicBombingFlight strategicBombingFlight = createStrategicBombingFlight(squadron, isPlayerFlight, targetDefinition);

        if (isPlayerFlight)
        {
            mission.getMissionFlightBuilder().setPlayerFlight(strategicBombingFlight);
        }

        createAAA(targetDefinition, strategicBombingFlight);
        createSpotlight(targetDefinition, strategicBombingFlight);

        createOtherFlightsInStream(squadron, targetDefinition.getTargetPosition(), strategicBombingFlight);

        createHomeDefenseFlights(squadron, isPlayerFlight, strategicBombingFlight);

        return strategicBombingFlight;
    }

    private StrategicBombingFlight createStrategicBombingFlight(Squadron squadron, boolean isPlayerFlight, TargetDefinition targetDefinition) throws PWCGException
    {
        Coordinate startCoords = squadron.determineCurrentPosition(campaign.getDate());
        MissionBeginUnit missionBeginUnit = new MissionBeginUnit();
        missionBeginUnit.initialize(startCoords.copy());

        FlightInformation flightInformation = createFlightInformation(targetDefinition.getTargetPosition());
        StrategicBombingFlight strategicBombingFlight = new StrategicBombingFlight(flightInformation, missionBeginUnit);
        strategicBombingFlight.setBombingAltitudeLevel(BombingAltitudeLevel.HIGH);
        strategicBombingFlight.setTargetDefinition(targetDefinition);
        if (squadron.determineIsNightSquadron())
        {
            strategicBombingFlight.setNightFlight(true);
        }
        else
        {
            strategicBombingFlight.setNightFlight(false);
        }
        strategicBombingFlight.createUnitMission();

        return strategicBombingFlight;
    }

    private void createOtherFlightsInStream(Squadron squadron, Coordinate targetPosition, StrategicBombingFlight strategicBombingFlight)
            throws PWCGException
    {
        if (squadron.determineIsNightSquadron())
        {
            IAirfield airfield = campaign.getPlayerAirfield();
            List<StrategicSupportingFlight> supportingFlights = getSupportingFlights(mission, targetPosition, airfield.getPosition(), squadron);
            for (Flight supportingFlight : supportingFlights)
            {
                strategicBombingFlight.addLinkedUnit(supportingFlight);
            }
        }
    }

    private void createAAA(TargetDefinition targetDefinition, StrategicBombingFlight strategicBombingFlight) throws PWCGException
    {
        AAAUnitFactory groundUnitFactory = new AAAUnitFactory(campaign, targetDefinition.getTargetCountry(), targetDefinition.getTargetPosition());
        GroundUnitSpawning aaaArty = groundUnitFactory.createAAAArtilleryBattery(3, 3);
        strategicBombingFlight.addLinkedUnit(aaaArty);
    }

    private void createHomeDefenseFlights(Squadron squadron, boolean isPlayerFlight, StrategicBombingFlight strategicBombingFlight)
            throws PWCGException
    {
        List<Flight> homeDefenseFlights = getOpposingFlight(mission, strategicBombingFlight, squadron, isPlayerFlight);
        if (homeDefenseFlights != null && homeDefenseFlights.size() > 0)
        {
            for (Flight homeDefenseFlight : homeDefenseFlights)
            {
                strategicBombingFlight.addLinkedUnit(homeDefenseFlight);
            }
        }
    }

    private GroundUnitFactory createSpotlight(TargetDefinition targetDefinition, StrategicBombingFlight strategicBombingFlight) throws PWCGException
    {
        GroundUnitFactory groundUnitFactory =  new GroundUnitFactory(campaign, targetDefinition.getTargetPosition(), targetDefinition.getTargetCountry());
        if (squadron.determineIsNightSquadron())
        {
            if (squadron.determineIsNightSquadron())
            {
                SpotLightGroup spotLightGroup = groundUnitFactory.createSpotLightGroup();
                strategicBombingFlight.addLinkedUnit(spotLightGroup);
            }
        }
        return groundUnitFactory;
    }

    private List<Flight> getOpposingFlight(Mission mission, BombingFlight bombing, Squadron squadron, boolean isPlayerFlight) throws PWCGException
    {
        List<Flight> homeDefenseFlights = new ArrayList<Flight>();

        if (isPlayerFlight)
        {
            List<Role> acceptableRoles = new ArrayList<Role>();
            acceptableRoles.add(Role.ROLE_FIGHTER);

            List<Squadron> opposingSquads = getOpposingSquadrons(bombing.getCoordinatesToIntersectWithPlayer().copy(), squadron.determineIsNightSquadron());

            if (opposingSquads != null && opposingSquads.size() != 0)
            {
                int numHomeDefenseFlights = 1;
                for (int i = 0; i < numHomeDefenseFlights; ++i)
                {
                    int index = RandomNumberGenerator.getRandom(opposingSquads.size());
                    Squadron opposingSquad = opposingSquads.get(index);

                    // And the opposing mission
                    // Target is the start of the opposing flight while the
                    // player field
                    // is the target for the opposing flight.
                    Coordinate homeDefenseCoords = opposingSquad.determineCurrentPosition(campaign.getDate());
                    MissionBeginUnit missionBeginUnit = new MissionBeginUnit();
                    missionBeginUnit.initialize(homeDefenseCoords.copy());

                    
                    FlightInformation opposingFlightInformation = FlightInformationFactory.buildAiFlightInformation(opposingSquad, mission, FlightTypes.INTERCEPT, bombing.getCoordinatesToIntersectWithPlayer().copy());
                    InterceptFlight homeDefenseFlight = new InterceptFlight(opposingFlightInformation, missionBeginUnit);
                    homeDefenseFlight.createUnitMission();

                    homeDefenseFlights.add(homeDefenseFlight);
                }
            }
        }

        return homeDefenseFlights;
    }

    private List<Squadron> getOpposingSquadrons(Coordinate targetCoordinates, boolean isNightMission) throws PWCGException
    {
        List<Role> acceptableRoles = new ArrayList<Role>();
        acceptableRoles.add(Role.ROLE_FIGHTER);
        List<Squadron> opposingSquadrons = new ArrayList<Squadron>();

        // First get anything near the target area
        List<Squadron> allOpposingSquads = null;
        Side enemySide = campaign.determineCountry().getSide().getOppositeSide();
        allOpposingSquads = PWCGContextManager.getInstance().getSquadronManager().getNearestSquadronsByRole(targetCoordinates.copy(), 1, 75000.0, acceptableRoles, enemySide,
                campaign.getDate());

        // Include day or night squadrons based on whether the mission is day or
        // night
        if (allOpposingSquads != null)
        {
            // Night
            if (isNightMission)
            {
                for (Squadron squadron : allOpposingSquads)
                {
                    if (squadron.determineIsNightSquadron())
                    {
                        opposingSquadrons.add(squadron);
                    }
                }
            }
            // Day
            else
            {
                for (Squadron squadron : allOpposingSquads)
                {
                    if (!squadron.determineIsNightSquadron())
                    {
                        opposingSquadrons.add(squadron);
                    }
                }
            }
        }

        return opposingSquadrons;
    }

    private List<StrategicSupportingFlight> getSupportingFlights(Mission mission, Coordinate targetPosition, Coordinate rootStartPosition, Squadron squadron)
            throws PWCGException
    {
        List<StrategicSupportingFlight> supportingBombingFlights = new ArrayList<StrategicSupportingFlight>();

        // Number of other flights on this raid
        int numSupportingFlights = 1 + RandomNumberGenerator.getRandom(3);

        double targetToStartAngle = MathUtils.calcAngle(rootStartPosition, targetPosition);
        for (int i = 0; i < numSupportingFlights; ++i)
        {
            // Send out supporting flights before the player flight
            Coordinate startPosition = MathUtils.calcNextCoord(rootStartPosition, targetToStartAngle, (i + 1) * 4000.0);

            MissionBeginUnit missionBeginUnit = new MissionBeginUnit();
            missionBeginUnit.initialize(startPosition.copy());

            FlightInformation opposingFlightInformation = FlightInformationFactory.buildAiFlightInformation(squadron, mission, FlightTypes.STRATEGIC_BOMB, targetPosition.copy());
            StrategicSupportingFlight bombingFlight = new StrategicSupportingFlight(opposingFlightInformation, missionBeginUnit, startPosition.copy());
            bombingFlight.createUnitMission();

            // Start the bombers right away
            bombingFlight.getMissionBeginUnit().setStartTime(2);

            supportingBombingFlights.add(bombingFlight);
        }

        return supportingBombingFlights;
    }

}
