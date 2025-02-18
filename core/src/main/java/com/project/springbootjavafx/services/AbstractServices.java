package com.project.springbootjavafx.services;

import java.lang.reflect.Field;
import java.util.List;
import java.util.ArrayList;

import com.project.springbootjavafx.utils.Pair;
import lombok.Getter;
import org.springframework.data.jpa.repository.JpaRepository;

import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.ManyToMany;

/**
 * The {@code AbstractServices} class is an abstract base class for service implementations.
 *
 * <p>
 * It provides common functionality for all service classes, such as adding records, retrieving field information,
 * and basic CRUD operations using a {@link JpaRepository}. This class is intended to be extended by concrete service
 * classes that implement domain-specific behavior.
 * </p>
 *
 * @param <T>  the type of the entity managed by the service
 * @param <ID> the type of the entity's identifier
 */
public abstract class AbstractServices<T, ID> {

    /**
     * The JPA repository used for CRUD operations on the entity.
     */
    protected final JpaRepository<T, ID> repository;

    /**
     * The domain class of the entity managed by this service.
     */
    protected final Class<T> domainClass;

    /**
     * The class type of the entity's identifier.
     */
    @Getter
    protected final Class<ID> idClass;

    /**
     * Constructs a new {@code AbstractServices} instance.
     *
     * @param repository  the repository that this service will use for data access
     * @param domainClass the class object representing the entity type managed by this service
     * @param idClass     the class object representing the type of the entity's identifier
     */
    public AbstractServices(JpaRepository<T, ID> repository, Class<T> domainClass, Class<ID> idClass) {
        this.repository = repository;
        this.domainClass = domainClass;
        this.idClass = idClass;
    }

    /**
     * Adds a new record to the database.
     *
     * <p>
     * This method is abstract and must be implemented by subclasses to provide domain-specific
     * behavior for adding entities.
     * </p>
     *
     * @param model the entity to add
     * @return the added entity
     */
    public abstract T add(T model);

    /**
     * Retrieves a list of fields and their types for the managed entity.
     *
     * <p>
     * This method uses reflection to inspect the declared fields of the domain class.
     * Fields annotated with {@link OneToMany}, {@link OneToOne}, or {@link ManyToMany} are excluded.
     * Each field is represented as a {@link Pair} of the field name and its simple type name.
     * </p>
     *
     * @return an {@link ArrayList} of {@link Pair} objects, where each pair contains the field name and its type
     */
    public ArrayList<Pair<String, String>> getFieldsTypes() {
        ArrayList<Pair<String, String>> fieldInfo = new ArrayList<>();
        Field[] fields = domainClass.getDeclaredFields();

        for (Field field : fields) {
            // Skip fields annotated with relationship annotations.
            if (field.isAnnotationPresent(OneToMany.class) ||
                    field.isAnnotationPresent(OneToOne.class) ||
                    field.isAnnotationPresent(ManyToMany.class)) {
                continue;
            }
            String fieldName = field.getName();
            String fieldType = field.getType().getSimpleName();
            fieldInfo.add(new Pair<>(fieldName, fieldType));
        }
        return fieldInfo;
    }

    /**
     * Retrieves all records of the managed entity.
     *
     * @return a {@link List} of all entities
     */
    public List<T> getAll() {
        return repository.findAll();
    }

    /**
     * Retrieves a record by its identifier.
     *
     * @param id the identifier of the entity
     * @return the entity with the specified id
     */
    public T getById(ID id) {
        return repository.findById(id).get();
    }

    /**
     * Checks whether an entity with the specified identifier exists.
     *
     * @param id the identifier to check
     * @return {@code true} if an entity with the given id exists, {@code false} otherwise
     */
    public boolean existsById(ID id) {
        return repository.existsById(id);
    }

    /**
     * Deletes the entity with the specified identifier.
     *
     * @param id the identifier of the entity to delete
     */
    public void delete(ID id) {
        repository.deleteById(id);
    }
}
