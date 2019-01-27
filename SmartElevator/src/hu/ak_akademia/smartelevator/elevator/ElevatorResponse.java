package hu.ak_akademia.smartelevator.elevator;

import java.util.Objects;

public class ElevatorResponse {

    private final ElevatorResponseCode code;
    private final String responseMessage;
    private final boolean operationSuccessful;

    public ElevatorResponse(ElevatorResponseCode code, String responseMessage, boolean operationSuccessful) {
        this.code = code;
        this.responseMessage = responseMessage;
        this.operationSuccessful = operationSuccessful;
    }

    public ElevatorResponseCode getCode() {
        return code;
    }

    public String getResponseMessage() {
        return responseMessage;
    }

    public boolean isOperationSuccessful() {
        return operationSuccessful;
    }

    @Override
    public int hashCode() {
        return Objects.hash(code, operationSuccessful, responseMessage);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof ElevatorResponse)) {
            return false;
        }
        ElevatorResponse other = (ElevatorResponse) obj;
        return code == other.code && operationSuccessful == other.operationSuccessful && Objects.equals(responseMessage, other.responseMessage);
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("ElevatorResponse [code=");
        builder.append(code);
        builder.append(", responseMessage=");
        builder.append(responseMessage);
        builder.append(", operationSuccessful=");
        builder.append(operationSuccessful);
        builder.append("]");
        return builder.toString();
    }

}