package pwcg.campaign.target;

import pwcg.campaign.Campaign;
import pwcg.campaign.api.ICountry;
import pwcg.core.exception.PWCGException;
import pwcg.core.exception.PWCGMissionGenerationException;
import pwcg.core.location.Coordinate;
import pwcg.mission.Mission;
import pwcg.mission.flight.FlightTypes;
import pwcg.mission.flight.artySpot.ArtillerySpotArtilleryGroup;
import pwcg.mission.ground.GroundUnitCollection;
import pwcg.mission.ground.GroundUnitFactory;

public class TargetBuilder
{
    private GroundUnitCollection groundUnitCollection;

    private Campaign campaign;
    private Mission mission;
    private TargetDefinition targetDefinition;    

    public TargetBuilder(
            Campaign campaign, 
            Mission mission, 
            TargetDefinition targetDefinition) throws PWCGException 
    { 
        this.campaign = campaign;
        this.mission = mission;
        this.targetDefinition = targetDefinition;
    }

	public void buildTarget(FlightTypes flightType) throws PWCGException 
	{
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
        GroundUnitBuilderAttack groundUnitBuilderAttack = new GroundUnitBuilderAttack(campaign, mission, targetDefinition);
        groundUnitCollection = groundUnitBuilderAttack.createTargetGroundUnits();
        
        Coordinate targetCoordinates = groundUnitCollection.getTargetCoordinatesFromGroundUnits(targetDefinition.getTargetCountry().getSide());
        if (targetCoordinates != null)
        {
            targetDefinition.setTargetLocation(targetCoordinates);
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
        GroundUnitBuilderBalloonDefense groundUnitBuilderBalloonDefense = new GroundUnitBuilderBalloonDefense(campaign, targetDefinition);
        groundUnitCollection = groundUnitBuilderBalloonDefense.createBalloonDefenseUnits();
    }

    private void createAssaultGroundUnits() throws PWCGException
    {
        groundUnitCollection = GroundUnitBuilderAssault.createAssault(campaign, mission, targetDefinition);
    }

    private void createNightDropGroundUnits() throws PWCGException
    {
        GroundUnitBuilderSpyDrop groundUnitBuilderSpyDrop = new GroundUnitBuilderSpyDrop(campaign, targetDefinition);
        groundUnitCollection = groundUnitBuilderSpyDrop.createSpyDropTargets();

        Coordinate targetCoordinates = groundUnitCollection.getTargetCoordinatesFromGroundUnits(targetDefinition.getTargetCountry().getSide());
        addAAABattery(targetDefinition.getTargetCountry(), targetCoordinates, 2, 1);
    }

    private void addAAABattery(ICountry country, Coordinate targetPosition, int numAAAMG, int numAAAArtillery) throws PWCGException 
    {
        GroundUnitBuilderAAA groundUnitBuilderAAA = new GroundUnitBuilderAAA();
        GroundUnitCollection groundUnitCollectionAAA = groundUnitBuilderAAA.createAAABattery(targetDefinition, numAAAMG, numAAAArtillery);
        groundUnitCollection.mergeGroundUnitCollection(groundUnitCollectionAAA);
    }

    private void addFriendlyArtillery() throws PWCGException, PWCGMissionGenerationException
    {
        GroundUnitFactory groundUnitFactory =  new GroundUnitFactory(campaign, targetDefinition.getTargetLocation(), targetDefinition.getAttackingCountry());
        ArtillerySpotArtilleryGroup friendlyArtilleryGroup = groundUnitFactory.createFriendlyArtilleryBattery();
        groundUnitCollection.addGroundUnit(GroundUnitType.ARTILLERY_UNIT, friendlyArtilleryGroup);
    }

    public GroundUnitCollection getGroundUnits()
    {
        groundUnitCollection.setTargetDefinition(targetDefinition);
        return this.groundUnitCollection;
    }
 }
