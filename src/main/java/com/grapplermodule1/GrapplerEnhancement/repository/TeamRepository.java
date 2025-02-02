package com.grapplermodule1.GrapplerEnhancement.repository;

import com.grapplermodule1.GrapplerEnhancement.dtos.TeamDTO;
import com.grapplermodule1.GrapplerEnhancement.entities.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TeamRepository extends JpaRepository<Team, Long> {

    @Query("SELECT NEW com.grapplermodule1.GrapplerEnhancement.dtos.TeamDTO(t.id, t.name) FROM Team t")
    List<TeamDTO> findListOfTeams();

    @Query("SELECT NEW com.grapplermodule1.GrapplerEnhancement.dtos.TeamDTO(t.id, t.name) FROM Team t WHERE t.id = :teamId")
    Optional<TeamDTO> findTeamById(Long teamId);

}
