package pwcg.mission.ground.builder;

import pwcg.campaign.Campaign;
import pwcg.campaign.api.ICountry;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.PWCGLocation;
import pwcg.mission.ground.GroundUnitInformation;
import pwcg.mission.ground.GroundUnitInformationFactory;
import pwcg.mission.ground.org.GroundUnitCollection;
import pwcg.mission.ground.org.GroundUnitCollectionData;
import pwcg.mission.ground.org.GroundUnitCollectionType;
import pwcg.mission.ground.org.IGroundUnit;
import pwcg.mission.ground.unittypes.infantry.DrifterAAAUnit;
import pwcg.mission.ground.unittypes.infantry.DrifterUnit;
import pwcg.mission.mcu.Coalition;
import pwcg.mission.target.TargetType;

public class DrifterUnitBuilder
{
    private Campaign campaign;
    private PWCGLocation position;
    private ICountry country;
    
    public DrifterUnitBuilder (Campaign campaign, PWCGLocation drifterPosition, ICountry country)
    {
        this.campaign = campaign;
        this.position  = drifterPosition;
        this.country  = country;
    }

    public GroundUnitCollection createDrifterUnit () throws PWCGException
    {
        GroundUnitInformation groundUnitInformation = GroundUnitInformationFactory.buildGroundUnitInformation(
                campaign, 
                country, 
                TargetType.TARGET_DRIFTER,
                position.getPosition(), 
                position.getPosition(),
                position.getOrientation());

        IGroundUnit drifterUnit = new DrifterUnit(groundUnitInformation);
        drifterUnit.createGroundUnit();

        IGroundUnit aaDrifterUnit = new DrifterAAAUnit(groundUnitInformation);
        aaDrifterUnit.createGroundUnit();
        
        GroundUnitCollectionData groundUnitCollectionData = new GroundUnitCollectionData(
                GroundUnitCollectionType.TRANSPORT_GROUND_UNIT_COLLECTION, 
                "Drifters", 
                TargetType.TARGET_DRIFTER,
                Coalition.getCoalitionsForSide(groundUnitInformation.getCountry().getSide().getOppositeSide()));

        GroundUnitCollection groundUnitCollection = new GroundUnitCollection ("Drifter", groundUnitCollectionData);
        groundUnitCollection.addGroundUnit(drifterUnit);
        groundUnitCollection.addGroundUnit(aaDrifterUnit);
        groundUnitCollection.setPrimaryGroundUnit(drifterUnit);
        groundUnitCollection.finishGroundUnitCollection();
        return groundUnitCollection;
    }
}
