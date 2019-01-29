package com.mobiquityinc.packer;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.Comparator;

@Builder
@ToString
@EqualsAndHashCode
class Item {
    private int id;
    private double weight;
    private double cost;

    int getId() {
        return id;
    }

    double getWeight() {
        return weight;
    }

    double getCost() {
        return cost;
    }

    private double getRatio() {
        return cost / weight;
    }

    static Comparator<Item> ratioComparator() {
        return (item1, item2) -> Double.compare(item2.getRatio(), item1.getRatio());
    }
}
