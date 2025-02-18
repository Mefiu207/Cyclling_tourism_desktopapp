package com.project.springbootjavafx.models;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Objects;

/**
 * The {@code ListyHoteliKey} class represents a composite primary key for the {@link ListyHoteli} entity.
 *
 * <p>
 * This key consists of three fields:
 * </p>
 * <ul>
 *   <li>{@code pokoj} - an {@code Integer} representing the room identifier.</li>
 *   <li>{@code miastoWycieczki} - an {@code Integer} representing the identifier of the trip night city.</li>
 *   <li>{@code hotelKod} - a {@code String} representing the hotel code.</li>
 * </ul>
 *
 * <p>
 * The class is annotated with {@link Embeddable} to indicate that it can be embedded in an entity as a composite key.
 * It implements {@link Serializable} as required for primary key classes in JPA.
 * Lombok annotations are used to generate boilerplate code such as constructors, getters, and setters.
 * </p>
 *
 * <p>
 * The {@code equals()} and {@code hashCode()} methods are overridden to ensure proper key comparison and hashing.
 * </p>
 *
 * @see ListyHoteli
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Embeddable
public class ListyHoteliKey implements Serializable {

    /**
     * The room identifier.
     */
    @Column(name = "pokoj")
    Integer pokoj;

    /**
     * The identifier for the trip night city.
     */
    @Column(name = "miasto_wycieczki")
    Integer miastoWycieczki;

    /**
     * The hotel code.
     */
    @Column(name = "hotel")
    String hotelKod;

    /**
     * Checks if this key is equal to another object.
     *
     * @param o the object to compare with this key
     * @return {@code true} if the specified object is equal to this key; {@code false} otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ListyHoteliKey that = (ListyHoteliKey) o;
        return Objects.equals(pokoj, that.pokoj) &&
                Objects.equals(miastoWycieczki, that.miastoWycieczki) &&
                Objects.equals(hotelKod, that.hotelKod);
    }

    /**
     * Returns a hash code value for this key.
     *
     * @return the hash code value for this key
     */
    @Override
    public int hashCode() {
        return Objects.hash(pokoj, miastoWycieczki, hotelKod);
    }
}
