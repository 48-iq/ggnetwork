package ru.ivanov.ggnetwork.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.ivanov.ggnetwork.entities.UserPostGrade;

@Repository
public interface UserPostGradeRepository extends JpaRepository<UserPostGrade, Integer> {
}
