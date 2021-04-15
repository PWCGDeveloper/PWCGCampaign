package pwcg.mission.ground.builder;

import java.util.ArrayList;
import java.util.List;

import pwcg.campaign.api.ICountry;
import pwcg.campaign.battle.AmphibiousAssault;
import pwcg.campaign.battle.AmphibiousAssaultShipDefinition;
import pwcg.campaign.factory.CountryFactory;
import pwcg.core.exception.PWCGException;
import pwcg.mission.Mission;
import pwcg.mission.ground.GroundUnitSize;
import pwcg.mission.ground.org.GroundUnitCollection;

public class AmphibiousAssaultBuilder
{
    private Mission mission;
    private AmphibiousAssault amphibiousAssault;
    private List<GroundUnitCollection> amphibiousAssaultUnits = new ArrayList<>();
    
    public AmphibiousAssaultBuilder(Mission mission, AmphibiousAssault amphibiousAssault)
    {
        this.mission = mission;
        this.amphibiousAssault = amphibiousAssault;
    }
    
    public List<GroundUnitCollection> generateAmphibiousAssault() throws PWCGException
    {
        List<AmphibiousAssaultShipDefinition> shipsForMission = getLandingCraftForAssault();

        makeLandingCraft(shipsForMission);
        makeLanding(shipsForMission);
        makeDefense(shipsForMission);
        
        return amphibiousAssaultUnits;
    }

    private void makeLandingCraft(List<AmphibiousAssaultShipDefinition> shipsForMission) throws PWCGException
    {
        ICountry shipCountry = CountryFactory.makeCountryByCountry(amphibiousAssault.getAggressorCountry());
        AmphibiousAssaultShipBuilder amphibiousAssaultShipBuilder = new AmphibiousAssaultShipBuilder(mission, shipsForMission, shipCountry);
        GroundUnitCollection ships = amphibiousAssaultShipBuilder.generateAmphibiousAssautShips();
        amphibiousAssaultUnits.add(ships);
    }

    private void makeLanding(List<AmphibiousAssaultShipDefinition> shipsForMission) throws PWCGException
    {
        for (AmphibiousAssaultShipDefinition shipDefinition :shipsForMission)
        {
            if (shipDefinition.getShipType().startsWith("land"))
            {
                AmphibiousAssaultAttackBuilder amphibiousAssaultShipBuilder = new AmphibiousAssaultAttackBuilder(mission, amphibiousAssault, shipDefinition);
                GroundUnitCollection assault = amphibiousAssaultShipBuilder.generateAmphibiousAssaultAttack();
                amphibiousAssaultUnits.add(assault);
            }
        }
    }

    private void makeDefense(List<AmphibiousAssaultShipDefinition> shipsForMission) throws PWCGException
    {
        for (AmphibiousAssaultShipDefinition shipDefinition : shipsForMission)
        {
            if (shipDefinition.getShipType().startsWith("land"))
            {
                AmphibiousDefenseBuilder amphibiousDefenseBuilder = new AmphibiousDefenseBuilder(mission, amphibiousAssault, shipDefinition);
                GroundUnitCollection defense = amphibiousDefenseBuilder.generateAmphibiousAssaultDefense();
                amphibiousAssaultUnits.add(defense);
            }
        }
    }

    private List<AmphibiousAssaultShipDefinition> getLandingCraftForAssault() throws PWCGException
    {
        GroundUnitSize groundUnitSizeConfig = GroundUnitSize.calcNumUnitsByConfig(mission.getCampaign());
        int numLandingCraft = 3;
        if (groundUnitSizeConfig == GroundUnitSize.GROUND_UNIT_SIZE_MEDIUM)
        {
            numLandingCraft = 6;
        }
        else if (groundUnitSizeConfig == GroundUnitSize.GROUND_UNIT_SIZE_HIGH)
        {
            numLandingCraft = 9;
        }
        
        amphibiousAssault.shuffleLandingCraft();;
        List<AmphibiousAssaultShipDefinition> shipsForMission = new ArrayList<>();
        for (int i = 0; i < numLandingCraft; ++ i)
        {
            if (i < (amphibiousAssault.getShipDefinitions().size()-1))
            {
                shipsForMission.add(amphibiousAssault.getShipDefinitions().get(i));
            }
        }
        return shipsForMission;
    }

 }
