package telegram.impl;

import feign.Param;

public class EnumExpander implements Param.Expander {
    @Override
    public String expand(Object value) {

        if (value instanceof Enum) {
            Enum enumValue = (Enum) value;
            return enumValue.name().toLowerCase();
        }
        return value.toString();
    }
}
