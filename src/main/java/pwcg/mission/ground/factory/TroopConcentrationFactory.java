package pwcg.mission.ground.factory;

import pwcg.campaign.Campaign;
import pwcg.campaign.api.ICountry;
import pwcg.campaign.context.FrontLinesForMap;
import pwcg.campaign.context.PWCGContextManager;
import pwcg.campaign.target.TacticalTarget;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.location.Orientation;
import pwcg.mission.ground.GroundUnitInformation;
import pwcg.mission.MissionBeginUnitCheckZone;
import pwcg.mission.ground.GroundUnitInformationFactory;
import pwcg.mission.ground.unittypes.infantry.GroundTroopConcentration;
import pwcg.mission.mcu.Coalition;

public class TroopConcentrationFactory
{
    private Campaign campaign;
    private Coordinate position;
    private ICountry country;

    public TroopConcentrationFactory (Campaign campaign, Coordinate location, ICountry country)
    {
        this.campaign = campaign;
        this.position = location.copy();
        this.country = country;
    }

    public GroundTroopConcentration createTroopConcentration () throws PWCGException
    {
        Coalition playerCoalition = Coalition.getFriendlyCoalition(campaign.determineCountry());

        MissionBeginUnitCheckZone missionBeginUnit = new MissionBeginUnitCheckZone();
        missionBeginUnit.initialize(position, 10000, playerCoalition);

        Orientation orientation = createTroopConcentrationOrientation();
        String countryName = country.getNationality();
        String name = countryName + " Troop Concentration";
        
        GroundUnitInformation groundUnitInformation = GroundUnitInformationFactory.buildGroundUnitInformation(
                missionBeginUnit, country, name, TacticalTarget.TARGET_TROOP_CONCENTRATION, position, position, orientation);
        
        GroundTroopConcentration troopConcentration = new GroundTroopConcentration(campaign, groundUnitInformation);
        troopConcentration.createUnitMission();

        return troopConcentration;
    }

    private Orientation createTroopConcentrationOrientation() throws PWCGException
    {
        FrontLinesForMap frontLineMarker =  PWCGContextManager.getInstance().getCurrentMap().getFrontLinesForMap(campaign.getDate());
        int indexIntoFrontArray = frontLineMarker.findIndexForClosestPosition(position, country.getSide());
        double facing = frontLineMarker.getOrientation(indexIntoFrontArray, country.getSide(), campaign.getDate());
        Orientation orientation = new Orientation(facing);
        return orientation;
    }

}
