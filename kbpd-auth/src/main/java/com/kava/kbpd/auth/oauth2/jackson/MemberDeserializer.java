package com.kava.kbpd.auth.oauth2.jackson;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.MissingNode;
import com.kava.kbpd.auth.model.MemberDetails;
import org.springframework.security.core.userdetails.User;

import java.io.IOException;

/**
 * Custom Deserializer for {@link User} class. This is already registered with
 * {@link SysUserMixin}. You can also use it directly with your mixin class.
 *
 * @author Jitendra Singh
 * @see SysUserMixin
 * @since 4.2
 */
class MemberDeserializer extends JsonDeserializer<MemberDetails> {

    /**
     * This method will create {@link User} object. It will ensure successful object
     * creation even if password key is null in serialized json, because credentials may
     * be removed from the {@link User} by invoking {@link User#eraseCredentials()}. In
     * that case there won't be any password key in serialized json.
     *
     * @param jp   the JsonParser
     * @param ctxt the DeserializationContext
     * @return the user
     * @throws IOException if a exception during IO occurs
     */
    @Override
    public MemberDetails deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
        ObjectMapper mapper = (ObjectMapper) jp.getCodec();
        JsonNode jsonNode = mapper.readTree(jp);
        Long userId = readJsonNode(jsonNode, "id").asLong();
        String username = readJsonNode(jsonNode, "username").asText();
        boolean enabled = readJsonNode(jsonNode, "enabled").asBoolean();
        return new MemberDetails(userId, username, enabled);
    }

    private JsonNode readJsonNode(JsonNode jsonNode, String field) {
        return jsonNode.has(field) ? jsonNode.get(field) : MissingNode.getInstance();
    }

}
