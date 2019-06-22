package pwcg.testutils;

import java.util.Date;

import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;

import pwcg.campaign.Campaign;
import pwcg.campaign.CampaignEquipmentManager;
import pwcg.campaign.CampaignPersonnelManager;
import pwcg.campaign.api.ICountry;
import pwcg.campaign.context.Country;
import pwcg.campaign.context.PWCGContextManager;
import pwcg.campaign.context.PWCGMap.FrontMapIdentifier;
import pwcg.campaign.factory.CountryFactory;
import pwcg.campaign.personnel.SquadronPersonnel;
import pwcg.campaign.plane.Equipment;
import pwcg.campaign.squadron.Squadron;
import pwcg.core.config.ConfigItemKeys;
import pwcg.core.config.ConfigManagerCampaign;
import pwcg.core.config.ConfigSimple;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.location.CoordinateBox;
import pwcg.core.utils.DateUtils;
import pwcg.mission.Mission;
import pwcg.mission.MissionBeginUnitCheckZone;
import pwcg.mission.MissionFlightBuilder;
import pwcg.mission.MissionGroundUnitResourceManager;
import pwcg.mission.mcu.Coalition;

public class KubanAttackMockCampaign
{
    @Mock protected Campaign campaign;
    @Mock protected Mission mission;
    @Mock protected MissionFlightBuilder missionFlightBuilder;
    @Mock protected ConfigManagerCampaign configManager;
    @Mock protected CampaignPersonnelManager personnelManager;
    @Mock protected SquadronPersonnel squadronPersonnel;
    @Mock protected CampaignEquipmentManager equipmentManager;
    @Mock protected Equipment squadronEquipment;

    protected ICountry country = CountryFactory.makeCountryByCountry(Country.GERMANY);
    protected MissionBeginUnitCheckZone missionBeginUnit;
    protected Coordinate myTestPosition = new Coordinate (100000, 0, 100000);
    protected Coordinate mytargetLocation = new Coordinate (100000, 0, 150000);
    
    protected Date date;
    protected MissionGroundUnitResourceManager missionGroundUnitResourceManager = new MissionGroundUnitResourceManager();
    protected CoordinateBox missionBorders;
    protected Squadron squadron;

    public void mockCampaignSetup() throws PWCGException
    {
        PWCGContextManager.setRoF(false);
        PWCGContextManager.getInstance().changeContext(FrontMapIdentifier.KUBAN_MAP);

        date = DateUtils.getDateYYYYMMDD("19430401");
        
        squadron = PWCGContextManager.getInstance().getSquadronManager().getSquadron(20121002); // I./St.G.2

        Mockito.when(campaign.getCampaignConfigManager()).thenReturn(configManager);
        Mockito.when(campaign.getDate()).thenReturn(date);

        Mockito.when(campaign.getPersonnelManager()).thenReturn(personnelManager);
        Mockito.when(personnelManager.getSquadronPersonnel(Mockito.any())).thenReturn(squadronPersonnel);
        Mockito.when(squadronPersonnel.isSquadronPersonnelViable()).thenReturn(true);
        
        Mockito.when(campaign.getEquipmentManager()).thenReturn(equipmentManager);
        Mockito.when(equipmentManager.getEquipmentForSquadron(Mockito.any())).thenReturn(squadronEquipment);
        Mockito.when(squadronEquipment.isSquadronEquipmentViable()).thenReturn(true);

        Mockito.when(configManager.getIntConfigParam(ConfigItemKeys.MaxGroundTargetDistanceKey)).thenReturn(50000);
        Mockito.when(configManager.getStringConfigParam(ConfigItemKeys.SimpleConfigGroundKey)).thenReturn(ConfigSimple.CONFIG_LEVEL_MED);
        Mockito.when(mission.getMissionGroundUnitManager()).thenReturn(missionGroundUnitResourceManager);
        
        
        missionBorders = CoordinateBox.coordinateBoxFromCenter(myTestPosition, 100000);
        Mockito.when(mission.getMissionFlightBuilder()).thenReturn(missionFlightBuilder);
        Mockito.when(missionFlightBuilder.isInFlightPath(Matchers.any())).thenReturn(true);
        Mockito.when(mission.getMissionBorders()).thenReturn(missionBorders);


        missionBeginUnit = new MissionBeginUnitCheckZone(myTestPosition, 10000);
        missionBeginUnit.getSelfDeactivatingCheckZone().getCheckZone().triggerCheckZoneByPlaneCoalition(Coalition.COALITION_ALLIED);        
    }

}
