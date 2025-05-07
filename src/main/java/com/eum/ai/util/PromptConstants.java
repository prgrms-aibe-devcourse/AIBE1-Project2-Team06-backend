package com.eum.ai.util;

public final class PromptConstants {

    private PromptConstants() {
        // 유틸 클래스이므로 인스턴스화 방지
    }

    public static final String Q1 = "1. 당신은 어떤 협업 방식을 선호하나요? %s";
    public static final String Q2 = "2. 마감일이 다가올 때 당신의 작업 스타일은 어떤가요? %s";
    public static final String Q3 = "3. 프로젝트 중 문제가 생기면 어떤 방식으로 해결하려 하나요? %s";
    public static final String Q4 = "4. 팀 내 소통에서 당신의 성향은? %s";
    public static final String Q5 = "5. 팀원과 의견이 다를 때 어떤 편인가요? %s";
    public static final String Q6 = "6. 당신은 어떤 방식의 업무 분배를 선호하나요? %s";

    public static final String INSTRUCTION = """
            
            가능한 컬쳐핏 유형:
            자율형 (자기 주도 및 개인 중심), 계획형 (체계적·신중한 스타일), 소통 협업형 (협력·의사소통 중시),
            실용 협업형 (효율성과 실행력 중심), 조화 중시형 (관계 중심), 지시 기반형 (명확한 체계 선호)

            컬쳐핏 유형의 응답은 아래 중 하나를 골라 정해진 영어 단어로 답해주세요.
            자율형 : AUTONOMOUS, 계획형 : PLANNER, 소통 협업형 : COMMUNICATIVE,
            실용 협업형 : PRACTICAL, 조화 중시형 : HARMONY, 지시 기반형 : DIRECTIVE

            형식은 다음과 같이 응답해주세요:
            {
               "cultureFitType": "..."
            }
            """;
}
