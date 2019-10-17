package pwcg.product.fc.io;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import pwcg.campaign.api.IAirfield;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.group.FakeAirfield;
import pwcg.campaign.group.FixedPosition;
import pwcg.core.exception.PWCGException;
import pwcg.core.exception.PWCGIOException;
import pwcg.core.utils.DateUtils;
import pwcg.core.utils.Logger;
import pwcg.mission.Mission;
import pwcg.mission.MissionBlockDamage;
import pwcg.mission.MissionBlockSmoke;
import pwcg.mission.io.MissionFileWriter;
import pwcg.mission.mcu.group.SmokeGroup;
import pwcg.mission.options.MapSeasonalParameters;
import pwcg.mission.options.MapWeather;
import pwcg.mission.options.MapWeather.WindLayer;
import pwcg.mission.options.MissionOptions;


public class FCMissionFile extends MissionFileWriter
{	
	public FCMissionFile (Mission mission)
	{
		super(mission);
	}
    
    @Override
    protected List<FixedPosition> adjustBlockDamage(List<FixedPosition> fixedPositions) throws PWCGException
    {
        MissionBlockDamage missionBlockDamage = new MissionBlockDamage(mission);      
        return missionBlockDamage.setDamageToFixedPositions(fixedPositions);
    }
    
    @Override
    protected void adjustBlockSmoke(List<FixedPosition> fixedPositions) throws PWCGException
    {
        MissionBlockSmoke missionBlockSmoke = new MissionBlockSmoke(mission);      
        missionBlockSmoke.addSmokeToDamagedAreas(fixedPositions);
    }

    @Override
    protected void writeProductSpecific(BufferedWriter writer) throws PWCGException
    {
        writeFieldsInMission(writer);
        writeFakeAirfieldForAiReturnToBase(writer);
        writeSmoke(writer);

    }

    private void writeFieldsInMission(BufferedWriter writer) throws PWCGException
    {
        List<IAirfield>  fieldsForPatrol = mission.getFieldsForPatrol();
        for (IAirfield field : fieldsForPatrol)
        {
            if (!field.isGroup())
            {
                field.write(writer);
            }
        }
    }

    private void writeFakeAirfieldForAiReturnToBase(BufferedWriter writer) throws PWCGException
    {
        Map<String, IAirfield> allAirFields =  PWCGContext.getInstance().getCurrentMap().getAirfieldManager().getAllAirfields();
        for (IAirfield airfield : allAirFields.values())
        {
            FakeAirfield fakeAirfiield = new FakeAirfield(airfield, mission.getCampaign().getDate());
            fakeAirfiield.write(writer);
        }
    }

    private void writeSmoke(BufferedWriter writer) throws PWCGException
    {
        for (SmokeGroup smokeGroup : mission.getMissionEffects().getSmokeGroups())
        {
            smokeGroup.write(writer);
        }
    }

    protected void writeMissionOptions(BufferedWriter writer) throws PWCGException 
    {
        try
        {
            MissionOptions missionOptions = PWCGContext.getInstance().getCurrentMap().getMissionOptions();
            MapWeather mapWeather = PWCGContext.getInstance().getCurrentMap().getMapWeather();

            writer.write("Options");
            writer.newLine();
            writer.write("{");
            writer.newLine();
            
            writer.write("  LCName = " + missionOptions.getlCName() + ";");
            writer.newLine();
            writer.write("  LCDesc = " + missionOptions.getlCDesc() + ";");
            writer.newLine();
            writer.write("  LCAuthor = " + missionOptions.getlCAuthor() + ";");
            writer.newLine();
            writer.write("  PlayerConfig = \"" + missionOptions.getPlayerConfig() + "\";");
            writer.newLine();
            
            String missionTime = missionOptions.getMissionTime().getMissionTime();
            writer.write("  Time = " + missionTime + ";");
            writer.newLine();
            
            String missionDate = DateUtils.getDateAsMissionFileFormat(mission.getCampaign().getDate());
            writer.write("  Date = " + missionDate + ";");
            writer.newLine();
            
            MapSeasonalParameters mapSeasonalParameters = missionOptions.getSeasonBasedParameters(mission.getCampaign().getDate());
            
            writer.write("  HMap = \"" + mapSeasonalParameters.getHeightMap() + "\";");
            writer.newLine();
            writer.write("  Textures = \"" + mapSeasonalParameters.getTextureMap() + "\";");
            writer.newLine();
            writer.write("  Forests = \"" + mapSeasonalParameters.getForrestMap() + "\";");
            writer.newLine();
            writer.write("  Layers = \"" + missionOptions.getLayers() + "\";");
            writer.newLine();
            writer.write("  GuiMap = \"" + mapSeasonalParameters.getGuiMap() + "\";");
            writer.newLine();
            writer.write("  SeasonPrefix = \"" + mapSeasonalParameters.getSeasonAbbreviation() + "\";");
            writer.newLine();
            writer.write("  MissionType = " + missionOptions.getMissionType() + ";");
            writer.newLine();
            writer.write("  AqmId = " + missionOptions.getAqmId() + ";");
            writer.newLine();
            writer.write("  CloudLevel = " + mapWeather.getCloudLevel() + ";");
            writer.newLine();
            writer.write("  CloudHeight = " + mapWeather.getCloudHeight() + ";");
            writer.newLine();
            writer.write("  PrecLevel = " + mapWeather.getPrecLevel() + ";");
            writer.newLine();
            writer.write("  PrecType = " + mapWeather.getPrecType() + ";");
            writer.newLine();
            writer.write("  CloudConfig = \"" + mapWeather.getCloudConfig() + "\";");
            writer.newLine();
            writer.write("  SeaState = " + missionOptions.getSeaState() + ";");
            writer.newLine();
            writer.write("  Turbulence = " + mapWeather.getTurbulence() + ";");
            writer.newLine();

            writer.write("  TempPressLevel = " + mapWeather.getTempPressLevel() + ";");
            writer.newLine();
            writer.write("  Temperature = " + mapWeather.getTemperature() + ";");
            writer.newLine();
            writer.write("  Pressure = " + mapWeather.getPressure() + ";");
            writer.newLine();

            writer.write("  WindLayers");
            writer.newLine();
            writer.write("  {");
            writer.newLine();
            
            for (WindLayer layer : mapWeather.getWindLayers())
            {
                writer.write("    " + layer.layer + " :     " + layer.direction + " :     " + layer.speed + ";");
                writer.newLine();           
            }
            
            writer.write("  }");
            writer.newLine();

            writer.write("  Countries");
            writer.newLine();
            writer.write("  {");
            writer.newLine();
            writer.write("    0 : 0;");
            writer.newLine();
            writer.write("    101 : 1;");
            writer.newLine();
            writer.write("    201 : 2;");
            writer.newLine();
            writer.write("  }");
            writer.newLine();

            writer.write("}");
            writer.newLine();
            writer.newLine();
            writer.newLine();
        }
        catch (IOException e)
        {
            Logger.logException(e);
            throw new PWCGIOException(e.getMessage());
        }
    }

}
