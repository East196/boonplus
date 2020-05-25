package cn.md.testmodel;
import static org.boon.Boon.atIndex;
import static org.boon.Boon.puts;
import static org.boon.Maps.fromMap;
import static org.boon.Sets.set;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.boon.Boon;
import org.boon.IO;
import org.boon.json.JsonParserAndMapper;
import org.boon.json.JsonParserFactory;

/**
 * Created by Richard on 5/6/14.
 */
public class PartialDataTreeExample {


    public static class TeamRoster {
        Set<String> teamNames;

        @Override
        public String toString() {
            return "TeamRoster{" +
                    "teamNames=" + teamNames +
                    '}';
        }
    }

    public static class TeamInfo {

        TeamRoster teamRoster;

        @Override
        public String toString() {
            return "TeamInfo{" +
                    "teamRoster=" + teamRoster +
                    '}';
        }
    }

    @SuppressWarnings("unchecked")
	public static void main (String... args) {
        String path = "json/teams.json";
        puts ("PATH", path);
        puts ("CONTENTS of PATH", IO.read("json/teams.json"));

        /* Using Boon style parser (fast). */
        JsonParserAndMapper boonMapper = new JsonParserFactory().create();
        Object jsonObject = boonMapper.parseFile(path);


        /* Using Boon path. */
        puts ("teamInfo", atIndex(jsonObject, "teamInfo"));
        puts("Team Roster", atIndex(jsonObject, "teamInfo.teamRoster"));
        puts("Team Names", atIndex(jsonObject, "teamInfo.teamRoster.teamNames"));



        /* Using Boon style (easy) 2 parser. */
        jsonObject = Boon.jsonResource(path);


        /* Using Boon path. */
        puts ("teamInfo", atIndex(jsonObject, "teamInfo"));
        puts("Team Roster", atIndex(jsonObject, "teamInfo.teamRoster"));
        puts("Team Names", atIndex(jsonObject, "teamInfo.teamRoster.teamNames"));

        //There is also a Groovy style and a GSON style.

        List<String> teamNames = (List<String>) atIndex(jsonObject, "teamInfo.teamRoster.teamNames");

        puts("Team Names", teamNames);

        Set<String> teamNameSet = set(teamNames);

        puts ("Converted to a set", teamNameSet);

        TeamInfo teamInfo = fromMap((Map<String, Object>) atIndex(jsonObject, "teamInfo"), TeamInfo.class);
        puts(teamInfo);


        TeamRoster teamRoster = fromMap((Map<String, Object>) atIndex(jsonObject, "teamInfo.teamRoster"), TeamRoster.class);
        puts(teamRoster);

    }

}