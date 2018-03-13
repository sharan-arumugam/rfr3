package com.lti.rfr.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lti.rfr.domain.Rfr;

public interface RfrRepository extends JpaRepository<Rfr, Long> {

    List<Rfr> findByImtIn(List<String> imts);

}
