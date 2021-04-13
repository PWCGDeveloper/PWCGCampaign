package pwcg.mission.ground.builder;

import java.util.ArrayList;
import java.util.List;

import pwcg.campaign.battle.AmphibiousAssault;
import pwcg.campaign.battle.AmphibiousAssaultShip;
import pwcg.core.exception.PWCGException;
import pwcg.mission.Mission;
import pwcg.mission.ground.org.GroundUnitCollection;
import pwcg.mission.ground.org.IGroundUnit;

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
        makeLandingCraft();
        makeLanding();
        makeDefense();
        
        return amphibiousAssaultUnits;
    }

    private void makeLandingCraft() throws PWCGException
    {
        AmphibiousAssaultShipBuilder amphibiousAssaultShipBuilder = new AmphibiousAssaultShipBuilder(mission, amphibiousAssault);
        GroundUnitCollection ships = amphibiousAssaultShipBuilder.generateAmphibiousAssautShips();
        finishGroundUnitCollection(ships);
    }

    private void makeLanding() throws PWCGException
    {
        for (AmphibiousAssaultShip shipDefinition : amphibiousAssault.getShips())
        {
            if (shipDefinition.getShipType().startsWith("land"))
            {
                AmphibiousAssaultAttackBuilder amphibiousAssaultShipBuilder = new AmphibiousAssaultAttackBuilder(mission, amphibiousAssault, shipDefinition);
                GroundUnitCollection assault = amphibiousAssaultShipBuilder.generateAmphibiousAssaultAttack();
                finishGroundUnitCollection(assault);
            }
        }
    }

    private void makeDefense() throws PWCGException
    {
        AmphibiousDefenseBuilder amphibiousDefenseBuilder = new AmphibiousDefenseBuilder(mission, amphibiousAssault);
        GroundUnitCollection defense = amphibiousDefenseBuilder.generateAmphibiousAssaultDefense();
        finishGroundUnitCollection(defense);
    }

    private void finishGroundUnitCollection(GroundUnitCollection amphibiousGroundUnit) throws PWCGException
    {
        List<IGroundUnit> primaryAssaultSegmentGroundUnits = new ArrayList<>();
        primaryAssaultSegmentGroundUnits.add(amphibiousGroundUnit.getPrimaryGroundUnit());
        amphibiousGroundUnit.setPrimaryGroundUnit(primaryAssaultSegmentGroundUnits.get(0));
        amphibiousGroundUnit.finishGroundUnitCollection();
        amphibiousAssaultUnits.add(amphibiousGroundUnit);
    }
 }
