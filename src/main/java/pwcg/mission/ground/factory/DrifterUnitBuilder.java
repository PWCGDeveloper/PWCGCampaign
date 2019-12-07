package pwcg.mission.ground.factory;

import pwcg.campaign.Campaign;
import pwcg.core.exception.PWCGException;
import pwcg.mission.ground.GroundUnitInformation;
import pwcg.mission.ground.GroundUnitInformationFactory;
import pwcg.mission.ground.org.GroundUnitCollection;
import pwcg.mission.ground.org.GroundUnitCollectionType;
import pwcg.mission.ground.org.IGroundUnit;
import pwcg.mission.ground.org.IGroundUnitCollection;
import pwcg.mission.ground.unittypes.infantry.DrifterAAAUnit;
import pwcg.mission.ground.unittypes.infantry.DrifterUnit;
import pwcg.mission.mcu.Coalition;
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

        IGroundUnitCollection groundUnitCollection = new GroundUnitCollection(GroundUnitCollectionType.TRANSPORT_GROUND_UNIT_COLLECTION, "Drifters", Coalition.getCoalitionsForSide(groundUnitInformation.getCountry().getSide().getOppositeSide()));
        groundUnitCollection.addGroundUnit(drifterUnit);
        groundUnitCollection.addGroundUnit(aaDrifterUnit);
        groundUnitCollection.finishGroundUnitCollection();
        return groundUnitCollection;
    }
}
