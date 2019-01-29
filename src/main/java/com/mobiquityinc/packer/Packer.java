package com.mobiquityinc.packer;

import com.google.common.base.Functions;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.extern.log4j.Log4j;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

import static com.mobiquityinc.packer.PackageUtil.validateFile;

/**
 * This class creates a package with filtered items.
 * It uses a greedy algorithm in order to calculate
 * which set of items fit the best in the package.
 */
@ToString
@EqualsAndHashCode
@Log4j
public class Packer {

    /**
     * Method to pack the items in packages according to the given criteria.
     * @param file - the file to get the packages from
     * @return {@link List} of comma-separated item ids
     */
    public static List<String> pack(final Path file) {
        validateFile(file);

        List<Package> packages = PackageMapper.parse(file);

        packages.stream().
            forEach(Packer::transformItems);

        return createItemIDsList(packages);
    }

    /**
     * Method to create a list with string representation of the item ids.
     * @param packages - the package
     * @return {@link List} of comma-separated item ids
     */
    private static List<String> createItemIDsList(final List<Package> packages) {
        return packages.stream().
                map(Packer::extractItemIDs).
                collect(Collectors.toList());
    }

    /**
     * Method to extract comma-separated string representation of the item ids.
     * @param aPackage - the package
     * @return string representation of the item ids
     */
    private static String extractItemIDs(final Package aPackage) {
        List<Integer> ids = aPackage.getItems().stream().
                mapToInt(Item::getId).
                boxed().
                collect(Collectors.toList());

        return convertIdsListToString(ids);
    }

    /**
     * Method to convert list of {@link Integer}s to comma separated string.
     * @param ids - the list of {@link Integer}s
     * @return comma separated string
     */
    private static String convertIdsListToString(final List<Integer> ids) {
        return ids.isEmpty() ? "-" : String.join(",", ids.stream().map(Functions.toStringFunction()::apply).collect(Collectors.toList()));
    }

    /**
     * This is the actual method that filters the items in a package.
     * @param aPackage - a package
     */
    private static void transformItems(final Package aPackage) {
        aPackage.getItems().sort(Item.ratioComparator());

        double usedCapacity = 0;
        double cost = 0;
        int i;

        for (i = 0; i < aPackage.getItemsCount(); i++) {
            Item item = aPackage.getItem(i);
            if (usedCapacity + item.getWeight() > aPackage.getWeightCapacity())
                break;

            usedCapacity += item.getWeight();
            cost += item.getCost();
        }

        aPackage.setWeightCapacity(usedCapacity);
        aPackage.setItemsCost(cost);
        aPackage.slice(0, i);
    }

    public static void main(String[] args) {
        pack(Paths.get("c:\\work\\dev\\mobiquity\\packages.conf")).forEach(log::info);
    }
}
