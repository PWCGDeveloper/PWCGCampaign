package pwcg.campaign.target;

import pwcg.core.exception.PWCGException;
import pwcg.core.exception.PWCGMissionGenerationException;
import pwcg.mission.ground.GroundUnitAAAFactory;
import pwcg.mission.ground.GroundUnitCollection;
import pwcg.mission.ground.unittypes.artillery.GroundAAABattery;

public class GroundUnitBuilderAAA
{
    private GroundUnitCollection groundUnitCollection = new GroundUnitCollection(GroundUnitCollectionType.INFANTRY_GROUND_UNIT_COLLECTION);

    public GroundUnitBuilderAAA()
    {
    }
    
    public GroundUnitCollection createAAABattery(TargetDefinition targetDefinition, int numAAAMg, int numAAAArtillery) throws PWCGException 
    {
        
        addAAAMg(targetDefinition, numAAAMg);
        addAAAArty(targetDefinition, numAAAArtillery);
        return groundUnitCollection;
    }

    private void addAAAMg(TargetDefinition targetDefinition, int numAAAMg) throws PWCGException, PWCGMissionGenerationException
    {
        GroundUnitAAAFactory groundUnitFactory =  new GroundUnitAAAFactory(targetDefinition.getTargetCountry(), targetDefinition.getTargetLocation());
        GroundAAABattery aaaMg = groundUnitFactory.createAAAMGBattery(numAAAMg);
        groundUnitCollection.addGroundUnit(GroundUnitType.AAA_MG_UNIT, aaaMg);
    }

    private void addAAAArty(TargetDefinition targetDefinition, int numAAAArtillery) throws PWCGException, PWCGMissionGenerationException
    {
        GroundUnitAAAFactory groundUnitFactory =  new GroundUnitAAAFactory(targetDefinition.getTargetCountry(), targetDefinition.getTargetLocation());
        GroundAAABattery aaaArty = groundUnitFactory.createAAAArtilleryBattery(numAAAArtillery);
        groundUnitCollection.addGroundUnit(GroundUnitType.AAA_ARTY_UNIT, aaaArty);
    }

    public GroundUnitCollection getGroundUnits()
    {
        return groundUnitCollection;
    }
}
