package com.example.my_automation.expected;

import java.util.List;

public class Investigations {
    public static final String A = "!123";
    private static final String B = "1243";
    private static final String C = "1st Case";
    private static final String D = "!Abc";
    private static final String E = "Alma";
    private static final String F = "alma";
    private static final String G = "berries-test";
    private static final String H = "BigTeens";
    private static final String I = "SI: Automation";
    private static final String J = "SI: automnation";
    private static final String K = "The testing for @ new";
    private static final String L = "tiktoker_profile";
    private static final String M = "Twitter[Automation]";
    private static final String N = "още един кейс";

    public static final List<String> EXPECTED_INVESTIGATIONS_NAMES_ASC = List.of(
            "!123",
            "1243",
            "1st Case",
            "!Abc",
            "Alma",
            "alma",
            "berries-test",
            "BigTeens",
            "SI: Automation",
            "SI: automation",
            "The testing for @ new",
            "tiktoker_profile",
            "Twitter[Automation]",
            "още един кейс"
    );

    public static final List<String> EXPECTED_INVESTIGATIONS_NAMES_DESC = List.of(
            "още един кейс",
            "Twitter[Automation]",
            "tiktoker_profile",
            "The testing for @ new",
            "SI: automation",
            "SI: Automation",
            "BigTeens",
            "berries-test",
            "alma",
            "Alma",
            "!Abc",
            "1st Case",
            "1243",
            "!123"
    );
}
