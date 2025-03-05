package org.projeti.entites;

public class Wallet {
    private int userId;
    private float score;

    public Wallet(int userId, float score) {
        this.userId = userId;
        this.score = score;
    }

    public int getUserId() {
        return userId;
    }

    public float getScore() {
        return score;
    }

    public void setScore(float score) {
        this.score = score;
    }

    public void addScore(float amount) {
        this.score += amount;
    }

    public void deductScore(float amount) {
        if (amount <= score) {
            this.score -= amount;
        }
    }

    @Override
    public String toString() {
        return "Wallet{" +
                "userId=" + userId +
                ", score=" + score +
                '}';
    }
}