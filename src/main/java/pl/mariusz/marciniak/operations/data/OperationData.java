package pl.mariusz.marciniak.operations.data;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class OperationData<V> {
    private final String name;
    private V valueObject;

    public OperationData(String name) {
        this.name = name;
    }
    
    public String getName() {
        return name;
    }
    
    public V getValueObject() {
        return valueObject;
    }
    
    public void setValueObject(V v) {
        valueObject = v;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append(name).append(valueObject).toString();
    }
}
