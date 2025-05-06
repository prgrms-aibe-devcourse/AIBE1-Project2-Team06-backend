package com.eum.ai.model.dto.request;

public record CultureFitRequest(
        String collaborationStyle,  //질문 1
        String deadlineAttitude,    // 질문 2
        String problemSolvingApproach,  // 질문 3
        String communicationStyle,  // 질문 4
        String conflictResolution,  // 질문 5
        String tackDistribution     // 질문 6
) {
}
