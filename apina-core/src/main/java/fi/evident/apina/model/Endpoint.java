package fi.evident.apina.model;

import fi.evident.apina.model.parameters.EndpointParameter;
import fi.evident.apina.model.parameters.EndpointPathVariableParameter;
import fi.evident.apina.model.parameters.EndpointRequestBodyParameter;
import fi.evident.apina.model.parameters.EndpointRequestParamParameter;
import fi.evident.apina.model.type.ApiType;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static fi.evident.apina.utils.CollectionUtils.filterByType;
import static fi.evident.apina.utils.CollectionUtils.join;
import static java.util.Collections.unmodifiableList;
import static java.util.Objects.requireNonNull;

/**
 * API endpoint reachable at an URL using given method and parameters. An example
 * of an endpoint is a single method on a controller.
 * Represents an single endpoint (e.g. a method in a controller)
 */
public final class Endpoint {

    /** Name of the original source element that specifies this endpoint */
    private final String name;

    /** URI template for the endpoint */
    private final URITemplate uriTemplate;

    private final List<EndpointParameter> parameters = new ArrayList<>();

    private final Optional<ApiType> responseBody;

    /** HTTP method for accessing the endpoint */
    private HTTPMethod method = HTTPMethod.GET;

    public Endpoint(String name, URITemplate uriTemplate, Optional<ApiType> responseBody) {
        this.name = requireNonNull(name);
        this.uriTemplate = requireNonNull(uriTemplate);
        this.responseBody = requireNonNull(responseBody);
    }

    public void addParameter(EndpointParameter parameter) {
        parameters.add(requireNonNull(parameter));
    }


    public Optional<EndpointRequestBodyParameter> getRequestBody() {
        for (EndpointParameter parameter : parameters)
            if (parameter instanceof EndpointRequestBodyParameter)
                return Optional.of((EndpointRequestBodyParameter) parameter);

        return Optional.empty();
    }

    public List<EndpointPathVariableParameter> getPathVariables() {
        return filterByType(parameters, EndpointPathVariableParameter.class);
    }

    public List<EndpointRequestParamParameter> getRequestParameters() {
        return filterByType(parameters, EndpointRequestParamParameter.class);
    }

    public List<EndpointParameter> getParameters() {
        return unmodifiableList(parameters);
    }

    public Optional<ApiType> getResponseBody() {
        return responseBody;
    }

    public URITemplate getUriTemplate() {
        return uriTemplate;
    }

    public String getName() {
        return name;
    }

    public HTTPMethod getMethod() {
        return method;
    }

    public void setMethod(HTTPMethod method) {
        this.method = requireNonNull(method);
    }

    @Override
    public String toString() {
        return String.format("%s %s(%s): %s %s",
                responseBody.map(ApiType::toString).orElse("void"),
                name,
                join(parameters, ", "),
                method,
                uriTemplate);
    }
}