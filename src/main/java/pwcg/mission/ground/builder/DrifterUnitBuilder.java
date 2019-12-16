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
import pwcg.mission.ground.unittypes.infantry.DrifterAAAUnit;
import pwcg.mission.ground.unittypes.infantry.DrifterUnit;
import pwcg.mission.mcu.Coalition;
import pwcg.mission.target.TacticalTarget;
import pwcg.mission.target.TargetDefinition;

public class DrifterUnitBuilder
{
    private Campaign campaign;
    private TargetDefinition targetDefinition;
    
    public DrifterUnitBuilder (Campaign campaign, TargetDefinition targetDefinition)
    {
        this.campaign = campaign;
        this.targetDefinition  = targetDefinition;
    }

    public IGroundUnitCollection createDrifterUnit () throws PWCGException
    {
        GroundUnitInformation groundUnitInformation = GroundUnitInformationFactory.buildGroundUnitInformation(campaign, targetDefinition);
        IGroundUnit drifterUnit = new DrifterUnit(groundUnitInformation);
        drifterUnit.createGroundUnit();

        IGroundUnit aaDrifterUnit = new DrifterAAAUnit(groundUnitInformation);
        aaDrifterUnit.createGroundUnit();
        
        GroundUnitCollectionData groundUnitCollectionData = new GroundUnitCollectionData(
                GroundUnitCollectionType.TRANSPORT_GROUND_UNIT_COLLECTION, 
                "Drifters", 
                TacticalTarget.TARGET_DRIFTER,
                Coalition.getCoalitionsForSide(groundUnitInformation.getCountry().getSide().getOppositeSide()));

        IGroundUnitCollection groundUnitCollection = new GroundUnitCollection (groundUnitCollectionData);
        groundUnitCollection.addGroundUnit(drifterUnit);
        groundUnitCollection.addGroundUnit(aaDrifterUnit);
        groundUnitCollection.setPrimaryGroundUnit(drifterUnit);
        groundUnitCollection.finishGroundUnitCollection();
        return groundUnitCollection;
    }
}
