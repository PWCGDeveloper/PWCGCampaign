package pwcg.mission.unit.tank;

import java.util.ArrayList;
import java.util.List;

import pwcg.campaign.Campaign;
import pwcg.campaign.api.ICountry;
import pwcg.campaign.crewmember.CrewMember;
import pwcg.campaign.crewmember.TankAce;
import pwcg.campaign.tank.Equipment;
import pwcg.campaign.tank.EquippedTank;
import pwcg.core.constants.AiSkillLevel;
import pwcg.core.constants.Callsign;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.PWCGLogger;
import pwcg.core.utils.PWCGLogger.LogLevel;
import pwcg.mission.playerunit.PlayerUnitInformation;
import pwcg.mission.playerunit.TankMcu;
import pwcg.mission.playerunit.crew.UnitCrewBuilder;

public class TankMcuFactory
{    
    private PlayerUnitInformation unitInformation;
    
    public TankMcuFactory(PlayerUnitInformation unitInformation)
    {
        this.unitInformation = unitInformation;
    }

    public List<TankMcu> createTanksForFlight(int numTanks) throws PWCGException
    {
        List<CrewMember> crewsForFlight = buildUnitCrews(numTanks);
        if (crewsForFlight.size() < numTanks)
        {
            numTanks = crewsForFlight.size();
        }
        List<EquippedTank> tanksTypesForFlight = buildEquipmentForFllght(numTanks);
        List<TankMcu> tanksForFlight = createTanks(tanksTypesForFlight, crewsForFlight);
        
        return tanksForFlight;
    }
    
    public static TankMcu createTankMcuByTankType (Campaign campaign, EquippedTank equippedTank, ICountry country, CrewMember tankCommander) throws PWCGException
    {
        TankMcu tank = new TankMcu(campaign, tankCommander);
        tank.buildTank(equippedTank, country);
        return tank;
    }

    private List<EquippedTank> buildEquipmentForFllght(int numTanks) throws PWCGException 
    {
        Equipment equipmentForCompany = unitInformation.getCampaign().getEquipmentManager().getEquipmentForCompany(unitInformation.getCompany().getCompanyId());
        UnitTankTypeBuilder tankTypeBuilder = new UnitTankTypeBuilder(equipmentForCompany, numTanks);
        List<EquippedTank> tanksTypesForFlight =tankTypeBuilder.getTankListForFlight();
        return tanksTypesForFlight;
    }

    private List<CrewMember> buildUnitCrews(int numTanks) throws PWCGException 
    {
        UnitCrewBuilder unitCrewBuilder = new UnitCrewBuilder(unitInformation);
        List<CrewMember> crewsForFlight = unitCrewBuilder.createCrewAssignmentsForFlight(numTanks);
        return crewsForFlight;
    }

    private List<TankMcu> createTanks(List<EquippedTank> tanksTypesForFlight, List<CrewMember> crewsForFlight) throws PWCGException
    {        
        List<TankMcu> tanksForFlight = new ArrayList<>();
        for (int index = 0; index < tanksTypesForFlight.size(); ++index)
        {
            try
            {
                EquippedTank equippedTank = tanksTypesForFlight.get(index);
                CrewMember tankCommander = crewsForFlight.get(index);            
                TankMcu tank = createTankMcuByTankType(unitInformation.getCampaign(), equippedTank, unitInformation.getCompany().getCountry(), tankCommander);
                if (index > 0)
                {
                    TankMcu leadTank = tanksForFlight.get(0);
                    tank.setTarget(leadTank.getLinkTrId());
                }
                tanksForFlight.add(tank);
            }
            catch (Exception e)
            {
                e.printStackTrace();
                PWCGLogger.log(LogLevel.ERROR, e.getMessage());
            }
        }
        
        initializeTankParameters(tanksForFlight);
        return tanksForFlight;
    }

    private void initializeTankParameters(List<TankMcu> tanksForFlight) throws PWCGException
    {
        int numInFormation = 1;
        for (TankMcu tank : tanksForFlight)
        {
            setPlaceInFormation(numInFormation, tank);
            setTankDescription(tank);
            setTankCallsign(numInFormation, tank);
            setAiSkillLevelForTank(tank);
            ++numInFormation;
        }
    }

    private void setPlaceInFormation(int numInFormation, TankMcu aiTank)
    {
        aiTank.setNumberInFormation(numInFormation);
    }

    private void setTankDescription(TankMcu tank) throws PWCGException
    {
        tank.setDesc(tank.getCommander().getNameAndRank());
    }

    private void setTankCallsign(int numInFormation, TankMcu tank)
    {
        Callsign callsign = unitInformation.getCompany().determineCurrentCallsign(unitInformation.getCampaign().getDate());

        tank.setCallsign(callsign);
        tank.setCallnum(numInFormation);
    }

    private void setAiSkillLevelForTank(TankMcu tank) throws PWCGException
    {
        AiSkillLevel aiLevel = AiSkillLevel.COMMON;
        if (tank.getCommander().isPlayer())
        {
            aiLevel = AiSkillLevel.PLAYER;

        }
        else if (tank.getCommander() instanceof TankAce)
        {
            aiLevel = AiSkillLevel.ACE;

        }
        else
        {
            aiLevel = assignAiSkillLevel(tank);
        }

        tank.setAiLevel(aiLevel);
    }

    private AiSkillLevel assignAiSkillLevel(TankMcu tank) throws PWCGException
    {
        AiSkillLevel aiLevel = tank.getCommander().getAiSkillLevel();
        return aiLevel;
    }
 }
