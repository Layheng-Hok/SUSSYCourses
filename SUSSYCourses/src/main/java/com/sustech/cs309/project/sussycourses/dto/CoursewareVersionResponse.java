package com.sustech.cs309.project.sussycourses.dto;

import java.time.LocalDateTime;

public record CoursewareVersionResponse(
        Long coursewareId,
        String category,
        String url,
        Boolean downloadable,
        Integer chapter,
        Integer coursewareOrder,
        Long variantOf,
        Integer version,
        Boolean displayVersion,
        LocalDateTime createTime
) {
}
