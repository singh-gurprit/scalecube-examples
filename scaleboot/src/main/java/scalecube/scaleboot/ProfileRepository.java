package scalecube.scaleboot;

import org.springframework.data.cassandra.repository.ReactiveCassandraRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ProfileRepository extends ReactiveCassandraRepository<Profile, UUID> {

}