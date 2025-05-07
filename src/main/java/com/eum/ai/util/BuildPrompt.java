package com.eum.ai.util;

import com.eum.ai.model.dto.request.CultureFitRequest;

import static com.eum.ai.util.PromptConstants.*;

public class BuildPrompt {

    public static String buildPrompt(CultureFitRequest request) {
        return String.join("\n",
                "다음은 사용자의 컬쳐핏 성향 질문에 대한 응답입니다. 아래 JSON 응답을 바탕으로 아래 7가지 유형 중 가장 어울리는 1개만 선택해 주세요.\n",
                String.format(Q1, request.collaborationStyle()),
                String.format(Q2, request.deadlineAttitude()),
                String.format(Q3, request.problemSolvingApproach()),
                String.format(Q4, request.communicationStyle()),
                String.format(Q5, request.conflictResolution()),
                String.format(Q6, request.tackDistribution()),
                INSTRUCTION
        );
    }
}
