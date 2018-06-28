package pwcg.mission.ground;

import pwcg.campaign.Campaign;
import pwcg.campaign.api.ICountry;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.mission.MissionBeginUnitCheckZone;
import pwcg.mission.ground.unittypes.infantry.GroundTroopConcentration;
import pwcg.mission.mcu.Coalition;

public class GrountUnitTroopConcentrationFactory
{
    private Campaign campaign;
    private Coordinate location;
    private ICountry country;

    public GrountUnitTroopConcentrationFactory (Campaign campaign, Coordinate location, ICountry country)
    {
        this.campaign = campaign;
        this.location = location.copy();
        this.country = country;
    }

    public GroundTroopConcentration createTroopConcentration () throws PWCGException
    {
        Coalition playerCoalition = Coalition.getFriendlyCoalition(campaign.determineCountry());

        MissionBeginUnitCheckZone missionBeginUnit = new MissionBeginUnitCheckZone();
        missionBeginUnit.initialize(location, 10000, playerCoalition);

        GroundTroopConcentration troopConcentration = new GroundTroopConcentration(campaign);
        troopConcentration.initialize(missionBeginUnit, country, location, campaign.getDate());
        troopConcentration.createUnitMission();

        return troopConcentration;
    }

}
