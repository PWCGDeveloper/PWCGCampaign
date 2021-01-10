package pwcg.mission.ground.builder;

import pwcg.campaign.Campaign;
import pwcg.campaign.api.IAirfield;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGMap;
import pwcg.campaign.group.AirfieldFinder;
import pwcg.campaign.group.AirfieldManager;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Orientation;
import pwcg.mission.ground.GroundUnitInformation;
import pwcg.mission.ground.GroundUnitInformationFactory;
import pwcg.mission.ground.org.GroundUnitCollection;
import pwcg.mission.ground.org.GroundUnitCollectionData;
import pwcg.mission.ground.org.GroundUnitCollectionType;
import pwcg.mission.ground.org.IGroundUnit;
import pwcg.mission.ground.unittypes.staticunits.AirfieldTargetGroup;
import pwcg.mission.mcu.Coalition;
import pwcg.mission.target.TargetDefinition;
import pwcg.mission.target.TargetType;

public class AirfieldUnitBuilder
{
    private Campaign campaign;
    private TargetDefinition targetDefinition;
    
    public AirfieldUnitBuilder (Campaign campaign, TargetDefinition targetDefinition)
    {
        this.campaign  = campaign;
        this.targetDefinition  = targetDefinition;
    }

    public GroundUnitCollection createAirfieldUnit () throws PWCGException
    {
        GroundUnitInformation groundUnitInformation = GroundUnitInformationFactory.buildGroundUnitInformation(
                campaign, 
                targetDefinition.getCountry(), 
                TargetType.TARGET_AIRFIELD, 
                targetDefinition.getPosition(), 
                targetDefinition.getPosition(), 
                Orientation.createRandomOrientation());
        
        IAirfield targetAirfield = findTargetAirfield();
        IGroundUnit airfieldGroup = new AirfieldTargetGroup(campaign, targetAirfield, groundUnitInformation);
        airfieldGroup.createGroundUnit();
        
        GroundUnitCollectionData groundUnitCollectionData = new GroundUnitCollectionData(
                GroundUnitCollectionType.TRANSPORT_GROUND_UNIT_COLLECTION, 
                "Airfield Trucks", 
                TargetType.TARGET_AIRFIELD,
                Coalition.getCoalitionsForSide(groundUnitInformation.getCountry().getSide().getOppositeSide()));

        GroundUnitCollection groundUnitCollection = new GroundUnitCollection ("Airfield Unit", groundUnitCollectionData);
        groundUnitCollection.addGroundUnit(airfieldGroup);
        groundUnitCollection.setPrimaryGroundUnit(airfieldGroup);
        groundUnitCollection.finishGroundUnitCollection();

        return groundUnitCollection;
    }

    private IAirfield findTargetAirfield() throws PWCGException
    {
        PWCGMap map = PWCGContext.getInstance().getCurrentMap();
        AirfieldManager airfieldManager = map.getAirfieldManager();
        AirfieldFinder airfieldFinder = airfieldManager.getAirfieldFinder();
        IAirfield targetAirfield = airfieldFinder.findClosestAirfieldForSide(targetDefinition.getPosition(), campaign.getDate(), targetDefinition.getCountry().getSide().getOppositeSide());
        return targetAirfield;
    }   
}
