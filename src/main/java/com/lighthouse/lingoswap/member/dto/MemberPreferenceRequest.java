package com.lighthouse.lingoswap.member.dto;

import java.util.List;

public record MemberPreferenceRequest(List<String> preferredCountries,
                                      List<UsedLanguageInfoDto> usedLanguages,
                                      List<PreferredInterestsInfoDto> preferredInterests) {

}
