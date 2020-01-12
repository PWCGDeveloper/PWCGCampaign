package pwcg.mission.ground.builder;

import pwcg.campaign.Campaign;
import pwcg.core.exception.PWCGException;
import pwcg.mission.ground.GroundUnitInformation;
import pwcg.mission.ground.GroundUnitInformationFactory;
import pwcg.mission.ground.org.GroundUnitCollection;
import pwcg.mission.ground.org.GroundUnitCollectionData;
import pwcg.mission.ground.org.GroundUnitCollectionType;
import pwcg.mission.ground.org.IGroundUnit;
import pwcg.mission.ground.org.IGroundUnitCollection;
import pwcg.mission.ground.unittypes.staticunits.AirfieldTargetGroup;
import pwcg.mission.mcu.Coalition;
import pwcg.mission.target.TargetType;
import pwcg.mission.target.TargetDefinition;

public class AirfieldUnitBuilder
{
    private Campaign campaign;
    private TargetDefinition targetDefinition;
    
    public AirfieldUnitBuilder (Campaign campaign, TargetDefinition targetDefinition)
    {
        this.campaign  = campaign;
        this.targetDefinition  = targetDefinition;
    }

    public IGroundUnitCollection createAirfieldUnit () throws PWCGException
    {
        GroundUnitInformation groundUnitInformation = GroundUnitInformationFactory.buildGroundUnitInformation(campaign, targetDefinition);
        IGroundUnit airfieldGroup = new AirfieldTargetGroup(campaign, groundUnitInformation);
        airfieldGroup.createGroundUnit();
        
        GroundUnitCollectionData groundUnitCollectionData = new GroundUnitCollectionData(
                GroundUnitCollectionType.TRANSPORT_GROUND_UNIT_COLLECTION, 
                "Airfield Trucks", 
                TargetType.TARGET_AIRFIELD,
                Coalition.getCoalitionsForSide(groundUnitInformation.getCountry().getSide().getOppositeSide()));

        IGroundUnitCollection groundUnitCollection = new GroundUnitCollection (groundUnitCollectionData);
        groundUnitCollection.addGroundUnit(airfieldGroup);
        groundUnitCollection.setPrimaryGroundUnit(airfieldGroup);
        groundUnitCollection.finishGroundUnitCollection();

        return groundUnitCollection;
    }   
}
