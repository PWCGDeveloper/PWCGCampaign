package pwcg.campaign.ww1.io;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import pwcg.campaign.api.IAirfield;
import pwcg.campaign.context.PWCGContextManager;
import pwcg.campaign.group.FixedPosition;
import pwcg.core.exception.PWCGException;
import pwcg.core.exception.PWCGIOException;
import pwcg.core.utils.DateUtils;
import pwcg.core.utils.Logger;
import pwcg.mission.Mission;
import pwcg.mission.flight.balloondefense.BalloonDefenseGroup;
import pwcg.mission.io.MissionFileWriter;
import pwcg.mission.mcu.effect.FirePotSeries;
import pwcg.mission.options.MapSeasonalParameters;
import pwcg.mission.options.MapWeather;
import pwcg.mission.options.MapWeather.WindLayer;
import pwcg.mission.options.MissionOptions;

public class RoFMissionFile  extends MissionFileWriter
{	
	public RoFMissionFile (Mission mission)
	{
        super(mission);
	}
    
    @Override
    protected List<FixedPosition> adjustBlockDamage(List<FixedPosition> fixedPositions) throws PWCGException
    {
        return new ArrayList<FixedPosition>();
    }
    
    @Override
    protected void adjustBlockSmoke(List<FixedPosition> fixedPositions) throws PWCGException
    {
    }

    protected void writeProductSpecific(BufferedWriter writer) throws PWCGException
	{
        FirePotSeries firePotSeries = mission.getMissionEffects().getFirePotSeries();
        firePotSeries.write(writer);

        List<IAirfield>  fieldsForPatrol = mission.getFieldsForPatrol();
        for (IAirfield field : fieldsForPatrol)
        {
            if (!field.isGroup())
            {
                field.write(writer);
            }
        }

        List<BalloonDefenseGroup> ambientBalloons = mission.getMissionBalloons().getAmbientBalloons();
        for (BalloonDefenseGroup ambientBalloon : ambientBalloons)
        {
            ambientBalloon.write(writer);
        }       
	}

    @Override
    protected void writeMissionOptions(BufferedWriter writer) throws PWCGException
	{
		try
        {
            MissionOptions missionOptions = PWCGContextManager.getInstance().getCurrentMap().getMissionOptions();
            MapWeather mapWeather = missionOptions.getMissionWeather();

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
            writer.write("  GuiMap = \"" + mapSeasonalParameters.getGuiMap() + "\";");
            writer.newLine();
            writer.write("  SeasonPrefix = \"" + mapSeasonalParameters.getSeasonAbbreviation() + "\";");
            writer.newLine();
            writer.write("  MissionType = " + missionOptions.getMissionType() + ";");
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
            writer.write("    102 : 1;");
            writer.newLine();
            writer.write("    103 : 1;");
            writer.newLine();
            writer.write("    104 : 1;");
            writer.newLine();
            writer.write("    105 : 1;");
            writer.newLine();
            writer.write("    501 : 2;");
            writer.newLine();
            writer.write("    502 : 2;");
            writer.newLine();
            writer.write("    600 : 3;");
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

    @Override
    public void writeMission() throws PWCGException
    {
        super.writeMission();
    }
}
