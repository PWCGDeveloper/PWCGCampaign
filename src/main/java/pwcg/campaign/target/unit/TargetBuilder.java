package pwcg.campaign.target.unit;

import pwcg.campaign.api.ICountry;
import pwcg.campaign.target.TargetDefinition;
import pwcg.core.exception.PWCGException;
import pwcg.core.exception.PWCGMissionGenerationException;
import pwcg.core.location.Coordinate;
import pwcg.mission.flight.FlightInformation;
import pwcg.mission.flight.FlightTypes;
import pwcg.mission.flight.artySpot.ArtillerySpotArtilleryGroup;
import pwcg.mission.ground.ArtillerySpotBatteryFactory;
import pwcg.mission.ground.GroundUnitCollection;
import pwcg.mission.ground.factory.AAAUnitFactory;
import pwcg.mission.ground.unittypes.GroundUnit;

public class TargetBuilder
{
    private GroundUnitCollection groundUnitCollection;

    private FlightInformation flightInformation;
    private TargetDefinition targetDefinition;    

    public TargetBuilder(FlightInformation flightInformation) throws PWCGException 
    { 
        this.flightInformation = flightInformation;
        this.targetDefinition = flightInformation.getTargetDefinition();
    }

	public void buildTarget() throws PWCGException 
	{
	    FlightTypes flightType = flightInformation.getFlightType();
        if (flightType == FlightTypes.ARTILLERY_SPOT)
        {
            createArtillerySpotGroundUnits();
        }
        else if (flightType == FlightTypes.BALLOON_DEFENSE)
        {
            getBalloonDefenseUnits();
        }
        else if (flightType == FlightTypes.CONTACT_PATROL)
        {
            createAssaultGroundUnits();        
        }
        else if (flightType == FlightTypes.CARGO_DROP)
        {
            createAssaultGroundUnits();        
        }
        else if (flightType == FlightTypes.PARATROOP_DROP)
        {
            createNightDropGroundUnits();        
        }   
        else if (flightType == FlightTypes.SPY_EXTRACT)
        {
            createNightDropGroundUnits();        
        }   
        else
        {
            createTargetGroundUnits();
        }
	}

    private void createTargetGroundUnits() throws PWCGException
    {
        GroundUnitBuilderAttack groundUnitBuilderAttack = new GroundUnitBuilderAttack(flightInformation.getCampaign(), flightInformation.getMission(), targetDefinition);
        groundUnitCollection = groundUnitBuilderAttack.createTargetGroundUnits();
        
        Coordinate targetCoordinates = groundUnitCollection.getTargetCoordinatesFromGroundUnits(targetDefinition.getTargetCountry().getSide());
        if (targetCoordinates != null)
        {
            targetDefinition.setTargetPosition(targetCoordinates);
        }
    }

    private void createArtillerySpotGroundUnits() throws PWCGException, PWCGMissionGenerationException
    {
        createTargetGroundUnits();
        addFriendlyArtillery();               
        Coordinate targetCoordinates = groundUnitCollection.getTargetCoordinatesFromGroundUnits(targetDefinition.getTargetCountry().getSide());
        addAAABattery(targetDefinition.getTargetCountry(), targetCoordinates, 2, 1);
    }

    private void getBalloonDefenseUnits() throws PWCGException 
    {        
        GroundUnitBuilderBalloonDefense groundUnitBuilderBalloonDefense = new GroundUnitBuilderBalloonDefense(flightInformation.getCampaign(), targetDefinition);
        groundUnitCollection = groundUnitBuilderBalloonDefense.createBalloonDefenseUnits();
    }

    private void createAssaultGroundUnits() throws PWCGException
    {
        groundUnitCollection = GroundUnitBuilderAssault.createAssault(flightInformation.getCampaign(), flightInformation.getMission(), targetDefinition);
    }

    private void createNightDropGroundUnits() throws PWCGException
    {
        GroundUnitBuilderSpyDrop groundUnitBuilderSpyDrop = new GroundUnitBuilderSpyDrop(flightInformation.getCampaign(), targetDefinition);
        groundUnitCollection = groundUnitBuilderSpyDrop.createSpyDropTargets();

        Coordinate targetCoordinates = groundUnitCollection.getTargetCoordinatesFromGroundUnits(targetDefinition.getTargetCountry().getSide());
        addAAABattery(targetDefinition.getTargetCountry(), targetCoordinates, 2, 1);
    }

    private void addAAABattery(ICountry country, Coordinate targetPosition, int numAAAMG, int numAAAArtillery) throws PWCGException 
    {
        AAAUnitFactory groundUnitFactory =  new AAAUnitFactory(flightInformation.getCampaign(), targetDefinition.getTargetCountry(), targetDefinition.getTargetPosition());
        
        GroundUnit assaultAAAMgUnit = groundUnitFactory.createAAAMGBattery(1, numAAAMG);
        groundUnitCollection.addGroundUnit(GroundUnitType.AAA_ARTY_UNIT, assaultAAAMgUnit);

        GroundUnit assaultAAAArtilleryUnit = groundUnitFactory.createAAAArtilleryBattery(1, numAAAArtillery);
        groundUnitCollection.addGroundUnit(GroundUnitType.AAA_ARTY_UNIT, assaultAAAArtilleryUnit);
    }

    private void addFriendlyArtillery() throws PWCGException, PWCGMissionGenerationException
    {
        ArtillerySpotBatteryFactory artillerySpotBatteryFactory = new ArtillerySpotBatteryFactory(
        		flightInformation.getCampaign(), 
        		flightInformation.getSquadron(), 
        		targetDefinition.getTargetPosition(), 
        		targetDefinition.getTargetPosition(), 
        		targetDefinition.getAttackingCountry());
        ArtillerySpotArtilleryGroup friendlyArtilleryGroup = artillerySpotBatteryFactory.createFriendlyArtilleryBattery(targetDefinition.isPlayerTarget());
        friendlyArtilleryGroup.createUnitMission();
        groundUnitCollection.addGroundUnit(GroundUnitType.ARTILLERY_UNIT, friendlyArtilleryGroup);
    }

    public GroundUnitCollection getGroundUnits()
    {
        groundUnitCollection.setTargetDefinition(targetDefinition);
        return this.groundUnitCollection;
    }
 }
