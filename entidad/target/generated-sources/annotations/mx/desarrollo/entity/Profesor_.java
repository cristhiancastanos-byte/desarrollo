package mx.desarrollo.entity;

import jakarta.annotation.Generated;
import jakarta.persistence.metamodel.EntityType;
import jakarta.persistence.metamodel.SingularAttribute;
import jakarta.persistence.metamodel.StaticMetamodel;

@StaticMetamodel(Profesor.class)
@Generated("org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
public abstract class Profesor_ {

	
	/**
	 * @see mx.desarrollo.entity.Profesor#apellidoPaterno
	 **/
	public static volatile SingularAttribute<Profesor, String> apellidoPaterno;
	
	/**
	 * @see mx.desarrollo.entity.Profesor#usuario
	 **/
	public static volatile SingularAttribute<Profesor, Usuario> usuario;
	
	/**
	 * @see mx.desarrollo.entity.Profesor#id
	 **/
	public static volatile SingularAttribute<Profesor, Integer> id;
	
	/**
	 * @see mx.desarrollo.entity.Profesor
	 **/
	public static volatile EntityType<Profesor> class_;
	
	/**
	 * @see mx.desarrollo.entity.Profesor#nombre
	 **/
	public static volatile SingularAttribute<Profesor, String> nombre;
	
	/**
	 * @see mx.desarrollo.entity.Profesor#rfc
	 **/
	public static volatile SingularAttribute<Profesor, String> rfc;
	
	/**
	 * @see mx.desarrollo.entity.Profesor#apellidoMaterno
	 **/
	public static volatile SingularAttribute<Profesor, String> apellidoMaterno;

	public static final String APELLIDO_PATERNO = "apellidoPaterno";
	public static final String USUARIO = "usuario";
	public static final String ID = "id";
	public static final String NOMBRE = "nombre";
	public static final String RFC = "rfc";
	public static final String APELLIDO_MATERNO = "apellidoMaterno";

}

