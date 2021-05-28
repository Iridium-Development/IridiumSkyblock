package com.iridium.iridiumskyblock.utils;

import com.iridium.iridiumskyblock.IridiumSkyblock;
import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Utility class which formats numbers.
 */
public class NumberFormatter {

    private static final BigDecimal ONE_THOUSAND = new BigDecimal(1000);
    private static final BigDecimal ONE_MILLION = new BigDecimal(1000000);
    private static final BigDecimal ONE_BILLION = new BigDecimal(1000000000);

    /**
     * Formats a provided number as configured by the user.
     * Automatically rounds it and adds a suffix if possible.
     *
     * @param number The number which should be formatted
     * @return The formatted version of the number. May not be parsed by {@link Double#valueOf(String)}
     */
    public static String format(double number) {
        if (IridiumSkyblock.getInstance().getConfiguration().displayNumberAbbreviations) {
            return formatNumber(BigDecimal.valueOf(number));
        } else {
            return String.format("%.2f", number);
        }
    }

    /**
     * Formats a provided number as configured by the user.
     * Automatically shortens it and adds a suffix.
     *
     * @param bigDecimal The BigDecimal that should be formatted
     * @return The formatted version of the number. Cannot be parsed by {@link Double#valueOf(String)}
     */
    private static String formatNumber(BigDecimal bigDecimal) {
        bigDecimal = bigDecimal.setScale(IridiumSkyblock.getInstance().getConfiguration().numberAbbreviationDecimalPlaces, RoundingMode.HALF_DOWN);
        StringBuilder outputStringBuilder = new StringBuilder();

        if (bigDecimal.compareTo(BigDecimal.ZERO) < 0) {
            outputStringBuilder
                .append("-")
                .append(formatNumber(bigDecimal.negate()));
        } else if (bigDecimal.compareTo(ONE_THOUSAND) < 0) {
            outputStringBuilder
                .append(bigDecimal.stripTrailingZeros().toPlainString());
        } else if (bigDecimal.compareTo(ONE_MILLION) < 0) {
            outputStringBuilder
                .append(bigDecimal.divide(ONE_THOUSAND, RoundingMode.HALF_DOWN).stripTrailingZeros().toPlainString())
                .append(IridiumSkyblock.getInstance().getConfiguration().thousandAbbreviation);
        } else if (bigDecimal.compareTo(ONE_BILLION) < 0) {
            outputStringBuilder
                .append(bigDecimal.divide(ONE_MILLION, RoundingMode.HALF_DOWN).stripTrailingZeros().toPlainString())
                .append(IridiumSkyblock.getInstance().getConfiguration().millionAbbreviation);
        } else {
            outputStringBuilder
                .append(bigDecimal.divide(ONE_BILLION, RoundingMode.HALF_DOWN).stripTrailingZeros().toPlainString())
                .append(IridiumSkyblock.getInstance().getConfiguration().billionAbbreviation);
        }

        return outputStringBuilder.toString();
    }

}