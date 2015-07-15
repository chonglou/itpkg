package com.itpkg.core.oauth2;

import org.springframework.social.connect.ConnectionFactoryLocator;
import org.springframework.social.connect.mem.InMemoryUsersConnectionRepository;

/**
 * Created by flamen on 15-7-15.
 */
public class TemporaryConnectionRepository extends InMemoryUsersConnectionRepository {
    public TemporaryConnectionRepository(ConnectionFactoryLocator connectionFactoryLocator) {
        super(connectionFactoryLocator);
    }
}
