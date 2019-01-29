package com.mobiquityinc.packer;

import lombok.*;

import java.util.List;

@Builder
@ToString
@EqualsAndHashCode
@Getter
@Setter
class Package {
    private double weightCapacity;
    private List<Item> items;
    private double itemsCost;

    double getWeightCapacity() {
        return weightCapacity;
    }

    public double getItemsCost() {
        return itemsCost;
    }

    List<Item> getItems() {
        return items;
    }

    Item getItem(final int index) {
        return items.get(index);
    }

    int getItemsCount() {
        return items.size();
    }

    void slice(final int begin, final int end) {
        items = items.subList(begin, end);
    }
}
