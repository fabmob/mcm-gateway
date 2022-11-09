package com.gateway.database.model;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorColumn(name="list_type",
        discriminatorType = DiscriminatorType.STRING)
@DiscriminatorValue("duration")
public class Duration extends PriceListItem{

    @Override
    public String toString() {
        return "";
    }
}
