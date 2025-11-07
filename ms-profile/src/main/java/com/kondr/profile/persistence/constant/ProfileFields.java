package com.kondr.profile.persistence.constant;

import com.kondr.profile.persistence.domain.ProfileDocument;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * Имена полей в документах профилей ({@link ProfileDocument}).
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ProfileFields {

  public static final String USERNAME_LOWER = "usernameLower";
  public static final String FULL_NAME_LOWER = "fullNameLower";

}
