package mx.desarrollo.entity;

import jakarta.annotation.Generated;
import jakarta.persistence.metamodel.EntityType;
import jakarta.persistence.metamodel.SingularAttribute;
import jakarta.persistence.metamodel.StaticMetamodel;

@StaticMetamodel(Unidad.class)
@Generated("org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
public abstract class Unidad_ {

	
	/**
	 * @see mx.desarrollo.entity.Unidad#horas
	 **/
	public static volatile SingularAttribute<Unidad, Integer> horas;
	
	/**
	 * @see mx.desarrollo.entity.Unidad#tipo
	 **/
	public static volatile SingularAttribute<Unidad, String> tipo;
	
	/**
	 * @see mx.desarrollo.entity.Unidad#id
	 **/
	public static volatile SingularAttribute<Unidad, Integer> id;
	
	/**
	 * @see mx.desarrollo.entity.Unidad
	 **/
	public static volatile EntityType<Unidad> class_;
	
	/**
	 * @see mx.desarrollo.entity.Unidad#nombre
	 **/
	public static volatile SingularAttribute<Unidad, String> nombre;

	public static final String HORAS = "horas";
	public static final String TIPO = "tipo";
	public static final String ID = "id";
	public static final String NOMBRE = "nombre";

}

