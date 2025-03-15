package com.purepigeon.test.utils.setup;

import lombok.Builder;
import lombok.extern.jackson.Jacksonized;

@Builder
@Jacksonized
public record TestData(
    String id,
    String content
) {
    public static final String ID = "abe159f5-016d-4e91-b078-430605f0e776";
    public static final String CONTENT = "testing content";

    public static TestData create() {
        return TestData.builder()
            .id(ID)
            .content(CONTENT)
            .build();
    }
}
