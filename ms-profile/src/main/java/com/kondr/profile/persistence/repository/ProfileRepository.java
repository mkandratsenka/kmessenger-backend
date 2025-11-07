package com.kondr.profile.persistence.repository;

import com.kondr.profile.persistence.domain.ProfileDocument;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Mono;

/**
 * Репозиторий для работы с документами профилей в MongoDB.
 */
public interface ProfileRepository extends ReactiveMongoRepository<ProfileDocument, String> {

  Mono<Boolean> existsByUsernameLower(String usernameLower);

}