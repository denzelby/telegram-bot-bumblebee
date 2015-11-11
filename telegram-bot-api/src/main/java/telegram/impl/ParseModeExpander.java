package telegram.impl;

import feign.Param;
import telegram.domain.request.ParseMode;

public class ParseModeExpander implements Param.Expander {
    @Override
    public String expand(Object value) {

        if (value instanceof ParseMode) {
            ParseMode mode = (ParseMode) value;
            return (mode == ParseMode.DEFAULT) ? null : mode.getValue();
        }
        return value.toString();
    }
}
