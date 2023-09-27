package com.lighthouse.lingoswap.member.dto;

import com.lighthouse.lingoswap.member.entity.UsedLanguage;

public record UsedLanguageDto(String code, String name, int level) {

    public static UsedLanguageDto from(UsedLanguage usedLanguage) {
        return new UsedLanguageDto(usedLanguage.getLanguage().getCode(), usedLanguage.getLanguage().getName(), usedLanguage.getLevel());
    }
}