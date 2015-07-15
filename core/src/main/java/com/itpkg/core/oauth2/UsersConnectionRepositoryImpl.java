package com.itpkg.core.oauth2;

import com.itpkg.core.models.User;
import com.itpkg.core.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.AuthenticationException;
import org.springframework.social.connect.*;
import org.springframework.social.connect.mem.InMemoryUsersConnectionRepository;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by flamen on 15-7-15.
 */
public class UsersConnectionRepositoryImpl implements UsersConnectionRepository {
    private final Logger logger = LoggerFactory.getLogger(UsersConnectionRepositoryImpl.class);

    @Override
    public List<String> findUserIdsWithConnection(Connection<?> connection) {
        try {
            User user = socialUserService.loadUserByConnectionKey(connection.getKey());
            String token = connection.createData().getAccessToken();
            if (!token.equals(user.getAccessToken())) {
                userService.setAccessToken(user.getId(), token);
            }
            String[] ids = {user.getAccessToken()};
            return Arrays.asList(ids);

        } catch (AuthenticationException ae) {
            String[] ids = {connectionSignUp.execute(connection)};
            return Arrays.asList(ids);
        }

    }

    @Override
    public Set<String> findUserIdsConnectedTo(String providerId, Set<String> providerUserIds) {
        Set<String> keys = new HashSet<>();
        for(String userId : providerUserIds){
            ConnectionKey ck = new ConnectionKey(providerId, userId);
            try{
                keys.add(socialUserService.loadUserByConnectionKey(ck).getId());
            }catch (AuthenticationException ae){
                logger.error("error on oauth2",ae);
            }
        }
        return keys;
    }

    @Override
    public ConnectionRepository createConnectionRepository(String userId) {
        ConnectionRepository cr = new TemporaryConnectionRepository(connectionFactoryLocator);
        User user = userService.findById(userId);
        ConnectionData cd = new ConnectionData(
                user.getProviderId(),               user.getProviderUserId(),
                null, null, null,
                user.getAccessToken(),
                null, null, null);
        Connection conn = connectionFactoryLocator.getConnectionFactory(user.getProviderId()).createConnection(cd);
        conn.add

        return null;
    }

    SocialUserService socialUserService;
    ConnectionFactoryLocator connectionFactoryLocator;
    ConnectionSignUp connectionSignUp;
    UserService userService;
}
