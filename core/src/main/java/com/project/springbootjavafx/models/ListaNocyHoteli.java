package com.project.springbootjavafx.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Immutable;
import java.time.LocalDate;

/**
 * The {@code ListaNocyHoteli} class represents an immutable view of hotel night list data.
 *
 * <p>
 * This entity is mapped to the database view <em>v_lista_nocy_hoteli</em> and is marked as immutable,
 * indicating that its data is read-only. It contains information about a specific hotel night entry,
 * including the room identifier, night number, date, city, and hotel name.
 * </p>
 *
 * <p>
 * Note: Since this entity is immutable, any changes to the data should be made at the source level (i.e., in the database view).
 * </p>
 *
 * @see LocalDate
 */
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Entity
@Immutable
@Table(name = "v_lista_nocy_hoteli")
public class ListaNocyHoteli {

    /**
     * The unique identifier for the hotel night list record.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false, insertable = false)
    private Integer id;

    /**
     * The identifier of the associated room.
     */
    @Column(name = "pokoj_id")
    private Integer pokojId;

    /**
     * The night number.
     */
    @Column(name = "noc")
    private Integer noc;

    /**
     * The date corresponding to this night entry.
     */
    @Column(name = "data")
    private LocalDate data;

    /**
     * The city associated with this record.
     */
    @Column(name = "miasto")
    private String miasto;

    /**
     * The name of the hotel for this night entry.
     */
    @Column(name = "hotel")
    private String hotel;
}
