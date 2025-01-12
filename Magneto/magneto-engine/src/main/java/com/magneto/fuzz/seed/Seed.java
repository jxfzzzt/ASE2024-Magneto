package com.magneto.fuzz.seed;

import com.magneto.staticanalysis.MethodCall;
import com.magneto.util.object.CopyObjectResult;
import com.magneto.util.object.ObjectUtil;

public class Seed implements Comparable<Seed>, Cloneable {

    private static final Integer MAX_ENERGY_FACTOR = 100;

    private static final Double DISTANCE_COEFF = 1D;

    private static final Double COVERAGE_COEFF = 0.0D;

    private static final Double SIMILARITY_COEFF = 0.0D;

    private int selectTimes;

    private Object object;

    private MethodCall targetMethodCall;

    private Object[] paramValues;

    private Boolean evaluateStatus;

    private Double distanceScore;

    private Double coverageScore;

    private Double similarityScore;

    public Seed() {
        this.coverageScore = .0;
        this.distanceScore = .0;
        this.similarityScore = .0;
        this.evaluateStatus = false;
        this.selectTimes = 1;
    }

    public Seed(MethodCall targetMethodCall, Object object, Object[] paramValues) {
        this();
        this.targetMethodCall = targetMethodCall;
        this.object = object;
        this.paramValues = paramValues;
    }

    public void select() {
        selectTimes += 1;
    }

    public int getEnergy() {
        return Math.max((int) (getSeedScore() * MAX_ENERGY_FACTOR) + 1, 1);
    }

    public Double getSeedScore() {
        return (SIMILARITY_COEFF * similarityScore + DISTANCE_COEFF * distanceScore + COVERAGE_COEFF * coverageScore)
                * calcTemperature(selectTimes);
    }

    private Double calcTemperature(int selectTime) {
        return Math.pow(20.0, -(selectTime / 500.0));
    }

    public Double getCoverageScore() {
        return coverageScore;
    }

    public void setCoverageScore(Double coverageScore) {
        this.coverageScore = coverageScore;
    }

    public Double getDistanceScore() {
        return distanceScore;
    }

    public void setDistanceScore(Double distanceScore) {
        this.distanceScore = distanceScore;
    }

    public Double getSimilarityScore() {
        return similarityScore;
    }

    public void setSimilarityScore(Double similarityScore) {
        this.similarityScore = similarityScore;
    }

    public Object getObject() {
        return object;
    }

    public void setObject(Object object) {
        this.object = object;
    }

    public MethodCall getTargetMethodCall() {
        return targetMethodCall;
    }

    public Object[] getParamValues() {
        return paramValues;
    }

    public void setParamValues(Object[] paramValues) {
        this.paramValues = paramValues;
    }

    public void setEvaluateStatus(Boolean evaluateStatus) {
        this.evaluateStatus = evaluateStatus;
    }

    public Boolean getEvaluateStatus() {
        return evaluateStatus;
    }

    public int getSelectTimes() {
        return selectTimes;
    }

    public void setTargetMethodCall(MethodCall targetMethodCall) {
        this.targetMethodCall = targetMethodCall;
    }

    @Override
    public int compareTo(Seed that) {
        // It must be written this way; Java defaults to a min-heap.
        return Double.compare(that.getSeedScore(), this.getSeedScore());
    }

    public Seed copy() {
        Seed seed = new Seed();
        seed.setTargetMethodCall(this.targetMethodCall);

        Object[] copyParamValues = new Object[this.paramValues.length];
        for (int i = 0; i < copyParamValues.length; i++) {
            CopyObjectResult<Object> result = ObjectUtil.copyObject(this.paramValues[i]);
            Object copyValue = result.getCopyValue();
            copyParamValues[i] = copyValue;
        }
        seed.setParamValues(copyParamValues);

        CopyObjectResult<Object> result = ObjectUtil.copyObject(this.object);
        seed.setObject(result.getCopyValue());

        return seed;
    }
}
