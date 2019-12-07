package pwcg.mission.ground.factory;

import pwcg.campaign.Campaign;
import pwcg.campaign.api.ICountry;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.mission.ground.GroundUnitInformation;
import pwcg.mission.ground.GroundUnitInformationFactory;
import pwcg.mission.ground.GroundUnitSize;
import pwcg.mission.ground.org.GroundUnitCollection;
import pwcg.mission.ground.org.GroundUnitCollectionType;
import pwcg.mission.ground.org.IGroundUnit;
import pwcg.mission.ground.org.IGroundUnitCollection;
import pwcg.mission.ground.unittypes.artillery.SpotlightUnit;
import pwcg.mission.mcu.Coalition;
import pwcg.mission.target.TacticalTarget;
import pwcg.mission.target.TargetDefinition;

public class SpotLightBuilder
{
    private Campaign campaign;

    public SpotLightBuilder (Campaign campaign)
    {
        this.campaign = campaign;
    }

    public IGroundUnitCollection createSpotLightGroup (TargetDefinition targetDefinition) throws PWCGException
    {
        GroundUnitInformation groundUnitInformation = GroundUnitInformationFactory.buildGroundUnitInformation(campaign, targetDefinition);
        IGroundUnit spotlightUnit = new SpotlightUnit(groundUnitInformation);
        spotlightUnit.createGroundUnit();
        
        IGroundUnitCollection groundUnitCollection = makeSearchLightGroundUnit(groundUnitInformation, spotlightUnit);

        return groundUnitCollection;
    }
    
    public IGroundUnitCollection createOneSpotLight (ICountry country, Coordinate position) throws PWCGException
    {
        GroundUnitInformation groundUnitInformation = createAAGroundUnitInformation(country, position);
        IGroundUnit spotlightUnit = new SpotlightUnit(groundUnitInformation);
        spotlightUnit.createGroundUnit();

        IGroundUnitCollection groundUnitCollection = makeSearchLightGroundUnit(groundUnitInformation, spotlightUnit);
        return groundUnitCollection;
    }

    private IGroundUnitCollection makeSearchLightGroundUnit(GroundUnitInformation groundUnitInformation, IGroundUnit spotlightUnit)
    {
        IGroundUnitCollection groundUnitCollection = new GroundUnitCollection(GroundUnitCollectionType.STATIC_GROUND_UNIT_COLLECTION, "Search Lights", Coalition.getCoalitionsForSide(groundUnitInformation.getCountry().getSide().getOppositeSide()));
        groundUnitCollection.addGroundUnit(spotlightUnit);
        groundUnitCollection.finishGroundUnitCollection();
        return groundUnitCollection;
    }

    private GroundUnitInformation createAAGroundUnitInformation(ICountry country, Coordinate position) throws PWCGException
    {
        String nationality = country.getNationality();
        String name = nationality + " Search Light";

        boolean isPlayerTarget = false;
        GroundUnitInformation groundUnitInformation = GroundUnitInformationFactory.buildGroundUnitInformation(
                campaign, country, name, TacticalTarget.TARGET_AIRFIELD, position, position, null, isPlayerTarget);
        
        groundUnitInformation.setUnitSize(GroundUnitSize.GROUND_UNIT_SIZE_TINY);
        return groundUnitInformation;
    }

}
