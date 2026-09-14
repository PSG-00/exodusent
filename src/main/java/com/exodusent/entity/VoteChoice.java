package com.exodusent.entity;

/**
 * 투표 가능한 메뉴.
 */
public enum VoteChoice {
    JAJANG("jajang"),
    JJAMPPONG("jjamppong");

    private final String value;

    VoteChoice(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static VoteChoice fromValue(String value) {
        for (VoteChoice choice : values()) {
            if (choice.value.equals(value)) {
                return choice;
            }
        }
        throw new IllegalArgumentException("지원하지 않는 투표 선택지입니다: " + value);
    }
}
