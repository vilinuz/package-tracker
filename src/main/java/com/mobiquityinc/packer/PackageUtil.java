package com.mobiquityinc.packer;

import com.mobiquityinc.packer.exception.APIException;
import org.apache.commons.lang3.StringUtils;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.lang.Double.parseDouble;

/**
 * Bunch of utility methods around a package and the package items.
 */
class PackageUtil {
    static final String COMMA = ",";
    private static final String EURO_SIGN = "€";
    private static final int MAX_PACKAGE_WEIGHT = 100;
    private static final int MAX_ITEM_WEIGHT = 100;
    private static final int MAX_ITEM_COST = 100;
    private static final int MAX_ITEMS_COUNT = 15;
    private static final Pattern REGEX_PATTERN = Pattern.compile("(\\d)+\\s+:\\s+(\\(\\d+,\\d+\\.\\d+,€\\d+\\)[\\s+]{0,1})+");


    /**
     * Validates if the given file path is correct.
     * @param file - te file path
     */
    static void validateFile(final Path file) {
        if (file == null || !Files.exists(file)) {
            String filePathString = file == null ? "" : file.toString();
            if (StringUtils.isEmpty(filePathString)) {
                throw new APIException("Specified file path " + filePathString + " doesn't exist");
            }
            throw new APIException("Specified file is null");
        }

    }

    /**
     * Method to strip the opneing and closing braces from a string.
     * @param item - the string representation of an item
     * @return the stripped version of an item without any braces
     */
    static String stripBraces(final String item) {
        return item.replace("(", "").replace(")", "");
    }

    /**
     * Removes euro sign from the cost and converts it in a double.
     * @param cost - the string representation of cost
     * @return the double representation of the cost
     */
    static double getCostAsNumber(final String cost) {
        return parseDouble(cost.replace(EURO_SIGN, ""));
    }

    /**
     * Check if line is a valid one using regex.
     *
     * @param line - the line to validate
     * @return true if the line is valid, else false
     */
    static boolean isValidLine(final String line) {
        Matcher matcher = REGEX_PATTERN.matcher(line);
        return matcher.matches();
    }

    /**
     * Validates if all of the following constraints are met:
     * 1: items count < 15
     * 2: item cost < 100
     * 3: item weight < 100
     * 4: package weight < 100
     * @param aPackage - the {@link Package} instance to be validated
     * @return true if the package is valid, else false
     */
    static boolean isValidPackage(final Package aPackage) {
        return
                isValidItemsCount(aPackage.getItemsCount()) &&
                isEachItemWithValidCost(aPackage) &&
                isEachItemWithValidWeight(aPackage) &&
                isValidPackageWeight(aPackage.getWeightCapacity());
    }

    /**
     * Validates if the items count constraint is met.
     * @param itemsCount - the items count
     * @return true if met, else false
     */
    private static boolean isValidItemsCount(final int itemsCount) {
        return itemsCount <= MAX_ITEMS_COUNT;
    }

    /**
     * Validates if all items meet the max weight constraint.
     * @param aPackage - the package
     * @return true if met, else false
     */
    private static boolean isEachItemWithValidWeight(final Package aPackage) {
        return aPackage.getItems().stream().
                allMatch(item -> item.getWeight() <= MAX_ITEM_WEIGHT);
    }

    /**
     * Validates if the items count constraint is met.
     * @param aPackage - the package
     * @return true if met, else false
     */
    private static boolean isEachItemWithValidCost(final Package aPackage) {
        return aPackage.getItems().stream().
                allMatch(item -> item.getWeight() <= MAX_ITEM_COST);
    }

    /**
     * Validates if the package weight constraint is met.
     * @param packageWeight - the package weight
     * @return true if met, else false
     */
    private static boolean isValidPackageWeight(final double packageWeight) {
        return packageWeight <= MAX_PACKAGE_WEIGHT;
    }

}
