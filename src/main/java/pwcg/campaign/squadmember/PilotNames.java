package pwcg.campaign.squadmember;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import pwcg.campaign.ArmedService;
import pwcg.campaign.api.ICountry;
import pwcg.campaign.context.PWCGContext;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.PWCGLogger;
import pwcg.core.utils.RandomNumberGenerator;

public class PilotNames {
    private static final Pattern filePatter = Pattern.compile("(FirstNames|LastNames)(.+)\\.txt");
    private static PilotNames instance = null;

    private final Map<String, NationalityNames> namesByNationality;

    private static class NationalityNames {
        private final List<String> firstNames = new ArrayList<>();
        private final List<String> lastNames = new ArrayList<>();
    }

    private PilotNames(Map<String, NationalityNames> namesByNationality) {
        this.namesByNationality = namesByNationality;
    }

    public static PilotNames getInstance() throws PWCGException {
        if (instance == null) {
            Map<String, NationalityNames> namesByNationality = new LinkedHashMap<>();

            File[] files = new File(PWCGContext.getInstance().getDirectoryManager().getPwcgNamesDir()).listFiles();
            if (files == null)
                throw new PWCGException("No name list files at " + PWCGContext.getInstance().getDirectoryManager().getPwcgNamesDir());

            for (File f : files) {
                Matcher matcher = filePatter.matcher(f.getName());
                if (!matcher.find())
                    continue;
                String nameType = matcher.group(1);
                String nationality = matcher.group(2);
                NationalityNames names = namesByNationality.computeIfAbsent(nationality, n -> new NationalityNames());
                read(f.getAbsolutePath(), "FirstNames".equals(nameType) ? names.firstNames : names.lastNames);
            }
            instance = new PilotNames(namesByNationality);
        }

        return instance;
    }

    private static void read(String filename, Collection<String> list) throws PWCGException {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(Files.newInputStream(Paths.get(filename)), StandardCharsets.UTF_8));
            String line;
            while ((line = reader.readLine()) != null) {
                String name = line.trim();
                if (name.length() != 0) {
                    list.add(name);
                }
            }

            reader.close();
        } catch (IOException e) {
            PWCGLogger.logException(e);
            throw new PWCGException(e.getMessage());
        }
    }

    public String getNameForNationality(String nationality, Map<String, String> namesUsed) {
        NationalityNames nationalityNames = namesByNationality.get(nationality);
        return makeName(namesUsed, nationalityNames.firstNames, nationalityNames.lastNames);
    }

    public String getName(ArmedService armedService, Map<String, String> namesUsed) {
        ICountry country = armedService.getNameCountry();

        String nationality = country.getCountry().pickNationality();
        return getNameForNationality(nationality, namesUsed);
    }

    public String getFemaleName(Map<String, String> namesUsed) {
        return makeName(namesUsed, namesByNationality.get("RussiaFemale").firstNames, namesByNationality.get("Russia").lastNames);
    }

    private String makeName(Map<String, String> namesUsed, List<String> firstNameList, List<String> lastNameList) {
        String name;
        int firstNameIndex = RandomNumberGenerator.getRandom(firstNameList.size());
        String firstName = firstNameList.get(firstNameIndex);

        int lastNameIndex = RandomNumberGenerator.getRandom(lastNameList.size());
        String lastName = lastNameList.get(lastNameIndex);

        while (namesUsed.containsKey(lastName)) {
            lastNameIndex = RandomNumberGenerator.getRandom(lastNameList.size());
            lastName = lastNameList.get(lastNameIndex);
        }

        name = firstName + " " + lastName;
        return name;
    }
}
