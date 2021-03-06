package com.github.throyer.common.springboot.domain.user.service;

import com.github.throyer.common.springboot.domain.user.repository.UserRepository;
import static com.github.throyer.common.springboot.domain.session.service.SessionService.authorized;
import com.github.throyer.common.springboot.domain.user.model.UpdateUserProps;
import com.github.throyer.common.springboot.domain.user.model.UserDetails;
import static com.github.throyer.common.springboot.domain.mail.exceptions.EmailValidations.validateEmailUniquenessOnModify;
import static com.github.throyer.common.springboot.utils.Responses.notFound;
import static com.github.throyer.common.springboot.utils.Responses.unauthorized;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UpdateUserService {
    
    @Autowired
    UserRepository repository;

    public UserDetails update(Long id, UpdateUserProps body) {

        authorized()
            .filter(authorized -> authorized.itsMeOrSessionIsADM(id))
                .orElseThrow(() -> unauthorized("Permission invalidates resource update"));

        var actual = repository
            .findOptionalByIdFetchRoles(id)
                .orElseThrow(() -> notFound("User not found"));
        
        validateEmailUniquenessOnModify(body, actual);

        actual.merge(body);

        return new UserDetails(repository.save(actual));
    }
}
