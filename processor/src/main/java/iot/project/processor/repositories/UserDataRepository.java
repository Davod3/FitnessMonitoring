package iot.project.processor.repositories;

import iot.project.processor.documents.UserData;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserDataRepository extends MongoRepository<UserData, String> {
}
