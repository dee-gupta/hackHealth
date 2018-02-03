package com.youcare;

/**
 * Created by Deepak Gupta on 2/3/18.
 */

class Person {
    private String name;
    private float negative;
    private float positive;
    private float score;

    public Person(String name, float negative, float positive, float score) {
        this.name = name;
        this.negative = negative;
        this.positive = positive;
        this.score = score;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getNegative() {
        return negative;
    }

    public void setNegative(String negative) {
        this.negative = Float.parseFloat(negative);
    }

    public float getPositive() {
        return positive;
    }

    public void setPositive(String positive) {
        this.positive = Float.parseFloat(positive);
    }

    public float getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = Float.parseFloat(score);
    }
}
