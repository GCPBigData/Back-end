package br.com.clearinvest.clivserver.web.rest.errors;

import org.zalando.problem.AbstractThrowableProblem;
import org.zalando.problem.Status;
import org.zalando.problem.StatusType;

import javax.annotation.Nullable;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;

public class BusinessException extends AbstractThrowableProblem {

    private static final long serialVersionUID = 1L;

    private final String entityName;

    private final String errorKey;

    public BusinessException(String defaultMessage) {
        this(ErrorConstants.DEFAULT_TYPE, defaultMessage, null, null, Status.BAD_REQUEST);
    }

    public BusinessException(String defaultMessage, @Nullable final StatusType status) {
        this(ErrorConstants.DEFAULT_TYPE, defaultMessage, null, null, status);
    }

    public BusinessException(String defaultMessage, String entityName, String errorKey, @Nullable final StatusType status) {
        this(ErrorConstants.DEFAULT_TYPE, defaultMessage, entityName, errorKey, status);
    }

    public BusinessException(URI type, String defaultMessage, String entityName, String errorKey, @Nullable final StatusType status) {
        super(type, defaultMessage, status, null, null, null, getAlertParameters(entityName, errorKey));
        this.entityName = entityName;
        this.errorKey = errorKey;
    }

    public String getEntityName() {
        return entityName;
    }

    public String getErrorKey() {
        return errorKey;
    }

    private static Map<String, Object> getAlertParameters(String entityName, String errorKey) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("message", "error." + errorKey);
        parameters.put("params", entityName);
        return parameters;
    }
}
