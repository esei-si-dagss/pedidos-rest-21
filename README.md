

# Ejemplo Spring REST  (SI-2021, semana 4)

Ejemplo de endpoints REST con Spring-MVC, Spring HATEOAS y  SpringDoc OAS

Construido sobre proyecto [pedidos-spring-21](https://github.com/esei-si-dagss/pedidos-spring-21)

## Añadidos

* En paquete `daos`: `ClienteDAO`, `AlmacenDAO` y `ArticuloAlmacenDAO` 
* Creado paquete `services` con:
	* `ClienteService` y `ClienteServiceImpl.java` 	
	* `ArticuloService` y `ArtculoServiceImpl.java` 	
	* `AlmacenService` y `AlmacenServiceImpl.java`  	
* Creado paquete `controllers` con:
   * `ClienteController`: API REST para clientes (con anotaciones OAS)
   * `ArticuloController`: API REST para artículos
   * `FamiliaController`: API REST para familias