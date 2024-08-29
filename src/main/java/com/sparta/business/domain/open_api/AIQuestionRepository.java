package com.sparta.business.domain.open_api;

import com.sparta.business.entity.AIQuestion;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AIQuestionRepository extends JpaRepository<AIQuestion, UUID> {

}
