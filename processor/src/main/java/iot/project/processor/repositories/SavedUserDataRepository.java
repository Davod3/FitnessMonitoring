package iot.project.processor.repositories;

import iot.project.processor.entities.ProcessedUserData;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface SavedUserDataRepository extends MongoRepository<ProcessedUserData, String> {

    @Query("{'date' : { $gte: ?0, $lte: ?1 } }")
    public List<ProcessedUserData> findBetweenStartEnd(LocalDate startDate, LocalDate endDate);

    public ProcessedUserData findByDate(LocalDate date);

}
