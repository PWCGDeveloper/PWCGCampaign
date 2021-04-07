package pwcg.mission.flight;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import pwcg.campaign.Campaign;
import pwcg.campaign.api.Side;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGProduct;
import pwcg.campaign.group.airfield.Airfield;
import pwcg.campaign.squadron.Squadron;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.DateUtils;
import pwcg.core.utils.PWCGLogger;
import pwcg.core.utils.PWCGLogger.LogLevel;
import pwcg.mission.Mission;
import pwcg.mission.MissionGenerator;
import pwcg.mission.target.TargetType;
import pwcg.testutils.CampaignCache;
import pwcg.testutils.SquadronTestProfile;
import pwcg.testutils.TestMissionBuilderUtility;

public class BodenplatteFlightTest
{
    @Before
    public void fighterFlightTests() throws PWCGException
    {
        PWCGContext.setProduct(PWCGProduct.BOS);
        PWCGLogger.setActiveLogLevel(LogLevel.DEBUG);
    }

    @Test
    public void hasSkirmishAndAirfieldAttackTest() throws PWCGException
    {        
        Campaign campaign = CampaignCache.makeCampaign(SquadronTestProfile.JG_26_PROFILE_WEST);
        campaign.setDate(DateUtils.getDateYYYYMMDD("19450101"));
        MissionGenerator missionGenerator = new MissionGenerator(campaign);
        Mission mission = missionGenerator.makeMission(TestMissionBuilderUtility.buildTestParticipatingHumans(campaign));

        for (IFlight flight : mission.getMissionFlightBuilder().getAiFlightsForSide(Side.AXIS))
        {
            assert(flight.getFlightInformation().getFlightType() == FlightTypes.GROUND_ATTACK);
            assert(flight.getTargetDefinition().getTargetType() == TargetType.TARGET_AIRFIELD);
        }

        boolean scrambleFound = false;
        for (IFlight flight : mission.getMissionFlightBuilder().getAiFlightsForSide(Side.ALLIED))
        {
            if (flight.getFlightInformation().getFlightType() == FlightTypes.SCRAMBLE)
            {
                assert(!flight.getFlightInformation().isVirtual());
                scrambleFound = true;
            }
        }
        assert(scrambleFound);

        mission.finalizeMission();
        
        Map<Integer, Squadron> includedSquadrons = new HashMap<>();
        for (IFlight flight : mission.getMissionFlightBuilder().getAllAerialFlights())
        {
            assert(!includedSquadrons.containsKey(flight.getSquadron().getSquadronId()));
            includedSquadrons.put(flight.getSquadron().getSquadronId(), flight.getSquadron());
        }
        
        Map<String, Airfield> includedAirfields = new HashMap<>();
        for (IFlight flight : mission.getMissionFlightBuilder().getAllAerialFlights())
        {
            Airfield airfield = flight.getSquadron().determineCurrentAirfieldAnyMap(campaign.getDate());
            assert(!includedAirfields.containsKey(airfield.getName()));
            includedAirfields.put(airfield.getName(), airfield);
        }
        
        campaign.write();
        mission.write();
    }
}
