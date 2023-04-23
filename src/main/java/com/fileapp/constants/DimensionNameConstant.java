package com.fileapp.constants;

public enum DimensionNameConstant {

    CRICKETER(1),
    ACTOR(2);

    public Integer getDimensionId() {
        return dimensionId;
    }

    DimensionNameConstant(Integer dimensionId) {
        this.dimensionId = dimensionId;
    }

    private final Integer dimensionId;

}
