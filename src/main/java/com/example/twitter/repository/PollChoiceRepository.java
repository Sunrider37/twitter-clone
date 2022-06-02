package com.example.twitter.repository;

import com.example.twitter.model.PollChoice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PollChoiceRepository extends JpaRepository<PollChoice,Long> {
}
