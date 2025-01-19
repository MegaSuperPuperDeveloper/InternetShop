package com.example.service;

import com.example.model.User;
import lombok.AllArgsConstructor;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.ws.rs.core.Response;
import java.util.Collections;
@Service
@AllArgsConstructor
public class KeycloakService {

    private final Keycloak keycloak;
    private final UserService userService;

    public void createUser(User user, String password, String realm) {
        UserRepresentation userRepresentation = new UserRepresentation();
        userRepresentation.setUsername(user.getUsername());
        userRepresentation.setEnabled(true);

        CredentialRepresentation credential = new CredentialRepresentation();
        credential.setType(CredentialRepresentation.PASSWORD);
        credential.setValue(password);
        credential.setTemporary(false);

        userRepresentation.setCredentials(Collections.singletonList(credential));
        userRepresentation.setRealmRoles(Collections.singletonList("ROLE_USER"));

        Response response = keycloak.realm(realm).users().create(userRepresentation);

        String locationHeader = response.getHeaderString("Location");
        String userId = locationHeader.substring(locationHeader.lastIndexOf('/') + 1);
        userService.updateKeycloakIdById(user.getId(), userId);
        user.setKeycloakId(userId);
    }

    public void deleteUserByKeycloakId(String keycloakId, String realm) {
        RealmResource realmResource = keycloak.realm(realm);

        UsersResource usersResource = realmResource.users();

        usersResource.get(keycloakId).remove();
    }

    public void updateUsername(String userId, String username, String realm) {
        UserResource userResource = keycloak.realm(realm).users().get(userId);
        UserRepresentation user = userResource.toRepresentation();
        user.setUsername(username);
        userResource.update(user);
    }

    public void updatePassword(String userId, String password, String realm) {
        UserResource userResource = keycloak.realm(realm).users().get(userId);
        UserRepresentation user = userResource.toRepresentation();

        CredentialRepresentation credential = new CredentialRepresentation();
        credential.setType(CredentialRepresentation.PASSWORD);
        credential.setValue(password);
        credential.setTemporary(false);

        user.setCredentials(Collections.singletonList(credential));

        userResource.update(user);

    }

    public void updateRole(String userId, String newRole, String realm) {
        UserResource userResource = keycloak.realm(realm).users().get(userId);

        UserRepresentation user = userResource.toRepresentation();

        user.setRealmRoles(Collections.singletonList(newRole));

        userResource.update(user);
    }


}