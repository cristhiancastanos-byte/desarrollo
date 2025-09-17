package mx.desarrollo.entity;

import jakarta.annotation.Generated;
import jakarta.persistence.metamodel.EntityType;
import jakarta.persistence.metamodel.SingularAttribute;
import jakarta.persistence.metamodel.StaticMetamodel;

@StaticMetamodel(Asignacion.class)
@Generated("org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
public abstract class Asignacion_ {

	
	/**
	 * @see mx.desarrollo.entity.Asignacion#unidad
	 **/
	public static volatile SingularAttribute<Asignacion, Unidad> unidad;
	
	/**
	 * @see mx.desarrollo.entity.Asignacion#horario
	 **/
	public static volatile SingularAttribute<Asignacion, String> horario;
	
	/**
	 * @see mx.desarrollo.entity.Asignacion#profesor
	 **/
	public static volatile SingularAttribute<Asignacion, Profesor> profesor;
	
	/**
	 * @see mx.desarrollo.entity.Asignacion#id
	 **/
	public static volatile SingularAttribute<Asignacion, Integer> id;
	
	/**
	 * @see mx.desarrollo.entity.Asignacion
	 **/
	public static volatile EntityType<Asignacion> class_;

	public static final String UNIDAD = "unidad";
	public static final String HORARIO = "horario";
	public static final String PROFESOR = "profesor";
	public static final String ID = "id";

}

