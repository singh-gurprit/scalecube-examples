package scalecube.scaleboot;

import io.scalecube.services.annotations.Service;
import io.scalecube.services.annotations.ServiceMethod;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service("ProfileService")
public interface ProfileService {

    @ServiceMethod()
    public Flux<String> greeting(Flux<String> request);

    @ServiceMethod
    public Mono<String> greet(String name);

    @ServiceMethod
    public Flux<Profile> data(Data data);
}