package com.kondr.profile.persistence.dao.impl;


import static com.kondr.mongo.utils.CriteriaUtils.notEqualTo;
import static com.kondr.mongo.utils.CriteriaUtils.startsWithIgnoreCase;

import com.kondr.mongo.constant.DocumentFields;
import com.kondr.profile.persistence.constant.ProfileFields;
import com.kondr.profile.persistence.dao.ProfileDocumentDAO;
import com.kondr.profile.persistence.domain.ProfileDocument;
import com.kondr.profile.persistence.handler.ProfileDocumentHandler;
import com.kondr.profile.persistence.repository.ProfileRepository;
import com.kondr.shared.utils.AppStringUtils;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
@RequiredArgsConstructor
public class ProfileDocumentDAOImpl implements ProfileDocumentDAO {

  public static final int MAX_NAME_TOKENS_FOR_SEARCH = 2;

  private final ProfileRepository profileRepository;

  private final ReactiveMongoTemplate mongoTemplate;

  private final ProfileDocumentHandler profileDocumentHandler;

  @Override
  public Mono<Boolean> existsByUsername(String username) {
    return StringUtils.isBlank(username)
        ? Mono.just(Boolean.FALSE)
        : profileRepository.existsByUsernameLower(username.toLowerCase());
  }

  @Override
  public Mono<ProfileDocument> findById(String id) {
    return StringUtils.isBlank(id)
        ? Mono.empty()
        : profileRepository.findById(id);
  }

  @Override
  public Flux<ProfileDocument> findByIds(List<String> ids) {
    return AppStringUtils.isAllBlank(ids)
        ? Flux.empty()
        : profileRepository.findAllById(ids);
  }

  @Override
  public Mono<ProfileDocument> create(ProfileDocument profile) {
    return profileDocumentHandler.handle(profile)
        .flatMap(profileRepository::insert);
  }

  @Override
  public Flux<ProfileDocument> findProfiles(String username, List<String> nameTokens,
      String ignoredUserId, int limit) {

    Query query = new Query();
    query.limit(limit);

    Optional.ofNullable(getProfileSearchCriteria(username, nameTokens, ignoredUserId))
        .ifPresent(query::addCriteria);

    return mongoTemplate.find(query, ProfileDocument.class);
  }

  private Criteria getProfileSearchCriteria(String username, List<String> nameTokens,
      String ignoredUserId) {

    List<Criteria> filters = Stream
        .of(
            startsWithIgnoreCase(ProfileFields.USERNAME_LOWER, username),
            notEqualTo(DocumentFields.ID, ignoredUserId),
            getFullNameCriteria(nameTokens)
        )
        .filter(Objects::nonNull)
        .toList();

    return filters.isEmpty()
        ? null
        : new Criteria().andOperator(filters);
  }

  private Criteria getFullNameCriteria(List<String> nameTokens) {
    List<String> validNameTokens = getValidNameTokens(nameTokens);
    if (CollectionUtils.isEmpty(validNameTokens)) {
      return null;
    }

    if (validNameTokens.size() == MAX_NAME_TOKENS_FOR_SEARCH) {
      String nameCombo = validNameTokens.get(0) + " " + validNameTokens.get(1);
      String swappedNameCombo = validNameTokens.get(1) + " " + validNameTokens.get(0);

      return new Criteria().orOperator(
          startsWithIgnoreCase(ProfileFields.FULL_NAME_LOWER, nameCombo),
          startsWithIgnoreCase(ProfileFields.FULL_NAME_LOWER, swappedNameCombo)
      );
    }

    return startsWithIgnoreCase(ProfileFields.FULL_NAME_LOWER, validNameTokens.get(0));
  }

  private List<String> getValidNameTokens(List<String> nameTokens) {
    return nameTokens == null
        ? Collections.emptyList()
        : nameTokens.stream()
            .filter(StringUtils::isNotBlank)
            .limit(MAX_NAME_TOKENS_FOR_SEARCH)
            .toList();
  }

}