package telegram.polling;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CommandParser {

    private static final Pattern COMMAND_PATTERN = Pattern.compile("^/[\\w]+");

    public String parse(String message) {
        Matcher matcher = COMMAND_PATTERN.matcher(message);
        if (matcher.find()) {
            return matcher.group();
        }
        return null;
    }
}
