package pwcg.mission.io;

import java.io.BufferedWriter;
import java.io.IOException;

import pwcg.campaign.context.PWCGContext;
import pwcg.core.exception.PWCGException;
import pwcg.core.exception.PWCGIOException;
import pwcg.core.utils.DateUtils;
import pwcg.core.utils.PWCGLogger;
import pwcg.mission.Mission;
import pwcg.mission.options.MapSeasonalParameters;
import pwcg.mission.options.MissionOptions;
import pwcg.mission.options.MissionWeather;
import pwcg.mission.options.WindLayer;

public class MissionFileOptionWriter
{
    private Mission mission;
    
	public MissionFileOptionWriter (Mission mission)
	{
		this.mission = mission;
	}
	
	public void writeMissionOptions(BufferedWriter writer) throws PWCGException 
    {
        try
        {
            MissionOptions missionOptions = mission.getMissionOptions();
            MissionWeather mapWeather = mission.getWeather();

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
            
            MapSeasonalParameters mapSeasonalParameters =PWCGContext.getInstance().getCurrentMap().getMapSeason().getSeasonBasedParameters(mission.getCampaign().getDate());
            
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
            writer.write("  MissionType = " + missionOptions.getMissionType().getMissionTypeCode() + ";");
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
            writer.write("  SeaState = " + mapWeather.getSeaState() + ";");
            writer.newLine();
            writer.write("  Turbulence = " + mapWeather.getTurbulence() + ";");
            writer.newLine();

            writer.write("  TempPressLevel = " + mapWeather.getTempPressLevel() + ";");
            writer.newLine();
            writer.write("  Temperature = " + mapWeather.getTemperature() + ";");
            writer.newLine();
            writer.write("  Pressure = " + mapWeather.getPressure() + ";");
            writer.newLine();
            writer.write("  Haze = " + mapWeather.getHaze() + ";");
            writer.newLine();

            writer.write("  WindLayers");
            writer.newLine();
            writer.write("  {");
            writer.newLine();
            
            for (WindLayer layer : mapWeather.getWindLayers())
            {
                writer.write("    " + layer.getLayer() + " :     " + layer.getDirection() + " :     " + layer.getSpeed() + ";");
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
            writer.write("    102 : 1;");
            writer.newLine();
            writer.write("    103 : 1;");
            writer.newLine();
            writer.write("    201 : 2;");
            writer.newLine();
            writer.write("    202 : 2;");
            writer.newLine();
            writer.write("    203 : 2;");
            writer.newLine();
            writer.write("    301 : 3;");
            writer.newLine();
            writer.write("    302 : 3;");
            writer.newLine();
            writer.write("    303 : 3;");
            writer.newLine();
            writer.write("    304 : 3;");
            writer.newLine();
            writer.write("    305 : 3;");
            writer.newLine();
            writer.write("    401 : 4;");
            writer.newLine();
            writer.write("    402 : 4;");
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
            PWCGLogger.logException(e);
            throw new PWCGIOException(e.getMessage());
        }
    }
}
