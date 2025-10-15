package model;

import java.util.ArrayList;
import java.util.List;

public class HealthAnalytics {
    private String patientId;
    private List<String> recommendations = new ArrayList<>();
    private String riskLevel;

    public HealthAnalytics(String patientId) {
        this.patientId = patientId;
    }

    public String generateTrendsReport() {
        return "Health trend report for patient " + patientId;
    }

    public double calculateRiskScore() {
        return Math.random() * 100;
    }

    public List<String> generateRecommendations() {
        recommendations.add("Drink more water");
        recommendations.add("Exercise regularly");
        return recommendations;
    }

    @Override
    public String toString() {
        return "HealthAnalytics{" + patientId + ", riskLevel=" + riskLevel + "}";
    }
}
