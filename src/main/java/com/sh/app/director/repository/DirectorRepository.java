package com.sh.app.director.repository;

import com.sh.app.director.entity.Director;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public interface DirectorRepository extends JpaRepository<Director, Long> {
    List<Director> findByIdIn(ArrayList<Long> longs);

    Optional<Director> findByDirectorName(String directorName);
}
