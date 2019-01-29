package com.mobiquityinc.packer;

import com.google.common.collect.Lists;
import lombok.extern.log4j.Log4j;

import java.nio.file.Path;
import java.util.List;
import java.util.Scanner;
import java.util.StringTokenizer;

import static com.mobiquityinc.packer.PackageUtil.*;
import static java.lang.Double.parseDouble;
import static java.lang.Integer.parseInt;

/**
 * The main goal of this class is to parse a file with package items and to create {@link List} of {@link Package}s.
 */
@Log4j
class PackageMapper {

    /**
     * This method parses the file specified and transforms each line into package.
     * @param file - the file to parse into {@link Package}s
     * @return list of {@link Package}s
     */
    static List<Package> parse(final Path file) {
        List<Package> packages = Lists.newArrayList();
        Scanner scanner;

        try {
            scanner = new Scanner(file, "Windows-1252");

            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                if (isValidLine(line)) {
                    Package aPackage = transformLineToPackage(line);
                    if (isValidPackage(aPackage)) {
                        packages.add(aPackage);
                    } else {
                        log.warn("Package " + aPackage + " doesn't seem to be valid.");
                    }
                } else {
                    log.warn("Failed to validate line: " + line);
                }
            }
        } catch (Exception e) {
            log.error("Failed to parse the specified file: " + file.toString(), e);
        }
        return packages;
    }

    /**
     * Method to transform line into package. The line has the following format:
     * <capacity> : (<index1>,<weight1>,<cost1>) (<index2>,<weight2>,<cost2>) ... (<indexN>,<weightN>,<costN>)
     * @param line - a line from the file to transform
     * @return a {@link Package} representation
     */
    private static Package transformLineToPackage(final String line) {
        StringTokenizer tokenizer = new StringTokenizer(line, ":");
        int capacity = parseInt(tokenizer.nextToken().trim());
        List<Item> items = extractItems(tokenizer.nextToken().split("\\s+"));
        return Package.builder().
                weightCapacity(capacity).
                items(items).
                build();
    }

    /**
     * Creates list of {@link Item}s from a string array.
     * @param itemsAsString - array of string items
     * @return {@link List} of items
     */
    private static List<Item> extractItems(String[] itemsAsString) {
        List<Item> items = Lists.newArrayList();
        for (String itemAsString : itemsAsString) {
            StringTokenizer tokenizer = new StringTokenizer(itemAsString.trim(), "\\s+", false);
            while (tokenizer.hasMoreElements()) {
                String nextItem = tokenizer.nextToken();
                Item item = createItem(stripBraces(nextItem));
                items.add(item);
            }
        }
        return items;
    }

    /**
     * Created an {@link Item} object from string representing an item.
     * @param itemAsString - the string representation of item
     * @return the {@link Item} representation
     */
    private static Item createItem(final String itemAsString) {
        StringTokenizer tokenizer = new StringTokenizer(itemAsString, COMMA, false);
        int index = parseInt(tokenizer.nextToken());
        double weight = parseDouble(tokenizer.nextToken());
        double cost = getCostAsNumber(tokenizer.nextToken());

        return Item.builder().
                id(index).
                weight(weight).
                cost(cost).
                build();
    }

}
