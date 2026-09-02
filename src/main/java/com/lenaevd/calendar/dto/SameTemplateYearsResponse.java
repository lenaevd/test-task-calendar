package com.lenaevd.calendar.dto;

import java.util.List;

public record SameTemplateYearsResponse(int fromYear, int toYear, List<Integer> years) {
}
