package edu.refactor.demo;

/**
 * Base class for response.
 *
 * @param <V> type of data value
 * @author sofronov
 * Created: 25.09.2019
 */
public class ResponseWrapper<V> {

    private String error;

    private V data;

    public ResponseWrapper() {
    }

    public ResponseWrapper(String error) {
        this(null, error);
    }

    public ResponseWrapper(V data) {
        this(data, null);
    }

    public ResponseWrapper(V data, String error) {
        this.error = error;
        this.data = data;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public V getData() {
        return data;
    }

    public void setData(V data) {
        this.data = data;
    }
}