package iot.project.processor.repositories;

import iot.project.processor.documents.UserData;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

public interface UserDataRepository extends MongoRepository<UserData, String> {

    @Query("{'dateTime' : { $gte: ?0, $lte: ?1 } }")
    public List<UserData> findBetweenStartEnd(LocalDateTime startDate, LocalDateTime endDate);

}
