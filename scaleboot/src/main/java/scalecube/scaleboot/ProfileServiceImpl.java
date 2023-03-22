package scalecube.scaleboot;

import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Greeting is an act of communication in which human beings intentionally make their presence known
 * to each other, to show attention to, and to suggest a type of relationship (usually cordial) or
 * social status (formal or informal) between individuals or groups of people coming in contact with
 * each other.
 */
@Service("profileService")
public class ProfileServiceImpl implements ProfileService {

    ProfileRepository profileRepository;

    public ProfileServiceImpl(ProfileRepository profileRepository) {
        this.profileRepository = profileRepository;
    }

    /**
     * Call this method to be greeted by the this ScaleCube service.
     *
     * @param requestStream incoming stream of names to greet.
     * @return service greeting
     */
    @Override
    public Flux<String> greeting(Flux<String> requestStream) {
        return requestStream.map(next -> "greeting: " + next);
    }

    @Override
    public Mono<String> greet(String name) {
        return Mono.just("Hello +" + name);
    }
    @Override
    public Flux<Profile> data(Data data) {
        return profileRepository.findAll();
    }
}