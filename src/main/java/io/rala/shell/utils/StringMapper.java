package io.rala.shell.utils;

public class StringMapper {
    private final String string;

    public StringMapper(String string) {
        this.string = string;
    }

    public String getString() {
        return string;
    }

    public Object map(Class<?> type) {
        if (!type.isPrimitive() && getString().equals("null")) return null;
        if (boolean.class.isAssignableFrom(type) || Boolean.class.isAssignableFrom(type)) {
            return Boolean.parseBoolean(getString());
        }
        if (byte.class.isAssignableFrom(type) || Byte.class.isAssignableFrom(type)) {
            return Byte.parseByte(getString());
        }
        if (char.class.isAssignableFrom(type) || Character.class.isAssignableFrom(type)) {
            return getString().length() == 1 ? getString().charAt(0) : null;
        }
        if (short.class.isAssignableFrom(type) || Short.class.isAssignableFrom(type)) {
            return Short.parseShort(getString());
        }
        if (int.class.isAssignableFrom(type) || Integer.class.isAssignableFrom(type)) {
            return Integer.parseInt(getString());
        }
        if (long.class.isAssignableFrom(type) || Long.class.isAssignableFrom(type)) {
            return Long.parseLong(getString());
        }
        if (float.class.isAssignableFrom(type) || Float.class.isAssignableFrom(type)) {
            return Float.parseFloat(getString());
        }
        if (double.class.isAssignableFrom(type) || Double.class.isAssignableFrom(type)) {
            return Double.parseDouble(getString());
        }
        return getString();
    }

    @Override
    public String toString() {
        return getString();
    }
}
