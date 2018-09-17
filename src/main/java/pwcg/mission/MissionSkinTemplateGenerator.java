package pwcg.mission;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pwcg.campaign.Campaign;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGMap;
import pwcg.campaign.context.PWCGMap.FrontMapIdentifier;
import pwcg.campaign.factory.RankFactory;
import pwcg.campaign.skin.Skin;
import pwcg.campaign.skin.SkinTemplate;
import pwcg.campaign.skin.SkinsForPlane;
import pwcg.campaign.squadmember.SquadronMember;
import pwcg.campaign.skin.SkinTemplate.SkinTemplateInstance;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.AsyncJobRunner;
import pwcg.core.utils.DateUtils;
import pwcg.mission.flight.IFlight;
import pwcg.mission.flight.plane.PlaneMcu;

import pwcg.mission.options.MapSeasonalParameters.Season;

public class MissionSkinTemplateGenerator
{
    public static List<SkinTemplateInstance> instantiateTemplates(Campaign campaign, List<IFlight> list) throws PWCGException
    {
        List<SkinTemplateInstance> skinsToGenerate = new ArrayList<>();

        for (IFlight flight : list)
        {
            for (PlaneMcu plane : flight.getFlightPlanes().getPlanes())
            {
                SkinsForPlane skinsForPlane = PWCGContext.getInstance().getSkinManager().getSkinsForPlane(plane.getType());
                Skin skin = plane.getPlaneSkin();
                if (skin != null && skin.getTemplate() != null)
                {
                    SkinTemplate template = skinsForPlane.getTemplate(plane.getPlaneSkin().getTemplate());
                    if (template == null)
                        continue;

                    Map<String, Object> params = new HashMap<>();
                    Date date = campaign.getDate();
                    PWCGMap currentMap = PWCGContext.getInstance().getCurrentMap();
                    params.put("WINTER", (currentMap.getMapWeather().getSeason(date) == Season.WINTER) ? 1 : 0);
                    params.put("WESTERN_FRONT", (currentMap.getMapIdentifier() == FrontMapIdentifier.BODENPLATTE_MAP) ? 1 : 0);

                    SquadronMember pilot = plane.getPilot();
                    int rank_pos = RankFactory.createRankHelper().getRankPosByService(pilot.getRank(), pilot.determineService(date));
                    params.put("PILOT_RANK", rank_pos);
                    switch (rank_pos)
                    {
                        case 0: params.put("RANK_FLAG", "Insignia\\raf_sqn_ldr.png");
                                params.put("FLAG_NUM", squadron.determineDisplayName(date).split(" ")[0]);
                                break;
                    }
                    params.put("PILOT_NAME_RANK", pilot.getNameAndRank());
                    params.put("PILOT_NAME_RANK_UC", pilot.getNameAndRank().toUpperCase());
                    String[] name_words = pilot.getName().split(" ");
                    String name_init = pilot.determineRankAbbrev() + " ";
                    for (int i = 0; i < name_words.length - 1; i++)
                    {
                        name_init += name_words[i].charAt(0) + ". ";
                    }
                    name_init += name_words[name_words.length - 1];
                    params.put("PILOT_NAME_RANK_INIT", name_init);
                    params.put("PILOT_NAME_RANK_INIT_UC", name_init.toUpperCase());

                    int stripes = date.before(DateUtils.getDateYYYYMMDD("19440606")) ? 0 :
                                  date.before(DateUtils.getDateYYYYMMDD("19440706")) ? 2 :
                                  date.before(DateUtils.getDateYYYYMMDD("19450101")) ? 1 :
                                                                                       0;
                    params.put("STRIPES", stripes);

                    params.putAll(skin.getOverrides());
                    SkinTemplateInstance instance = template.instantiate(params);
                    if (!instance.skinExists()) {
                        skinsToGenerate.add(instance);
                    }

                    Skin renderedSkin = new Skin();
                    renderedSkin.setSkinName(instance.getFilename());
                    plane.setPlaneSkin(renderedSkin);
                }
            }
        }
        return skinsToGenerate;
    }

    public static void generateSkins(List<SkinTemplateInstance> skinsToGenerate, AsyncJobRunner runner)
    {
        if (!skinsToGenerate.isEmpty())
        {
            List<AsyncJobRunner.Job> jobs = new ArrayList<>();

            for (SkinTemplateInstance instance : skinsToGenerate)
            {
                jobs.add(() -> instance.generate());
            }

            runner.add("Generating skins...", jobs);
        }
    }
}
