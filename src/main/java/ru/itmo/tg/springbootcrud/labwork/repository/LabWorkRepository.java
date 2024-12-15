package ru.itmo.tg.springbootcrud.labwork.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.itmo.tg.springbootcrud.labwork.model.LabWork;

import java.util.List;

@Repository
public interface LabWorkRepository extends JpaRepository<LabWork, Long> {

    @Query(nativeQuery = true, value = "select delete_lab_work_by_minimal_point(:p, :uId)")
    Boolean deleteLabWorkByMinimalPoint(Integer p, Long uId);

    @Query(nativeQuery = true, value = "select get_count_by_author_id(:authorId)")
    Integer getCountByAuthorId(Long authorId);

    @Query(nativeQuery = true,
            value = "select * from get_lab_works_with_description_containing(:substring, :page, :pageSize)")
    List<LabWork> getLabWorksWithDescriptionContaining(String substring, Integer page, Integer pageSize);

    @Query(nativeQuery = true, value = "select adjust_lab_work_difficulty_by(:labId, :steps, :diffVals)")
    String adjustDifficulty(long labId, int steps, String[] diffVals);

    @Query(nativeQuery = true, value = "select * from copy_lab_to_discipline(:labId, :disId, :uId)")
    LabWork copyLabWorkToDiscipline(long labId, long disId, long uId);

}
