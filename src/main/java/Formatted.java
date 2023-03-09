public interface Formatted {
    public Class<? extends Enum> getFormatEnum();

    public Enum<?> getFormatString(String format);
}
