

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
   
## Añadidos (Semana 5, 2/12/2021)
* En paquete `entidades`: 
	* Añadido `orphanRemoval=true` en relación `@OneToMany` de `Pedido` hacia `LineaPedido` para que `cascade.MERGE` gestione el borrado de las líneas de pedido
	* Añadida anotación `@JsonIgnore` en relación `@ManyToOne` de `LineaPedido` haia `Pedido` para evitar ciclos en la serialziación JSON de los pedidos.
* En paquete `daos : añadido `PedidoDAO`
* En paquete `services`: añadidos `PedidoService` y `PedidoServiceImpl`
* En paquete `controllers`: añadidos `PedidoController` (API REST para pedidos) y `AlmacenController` (API REST para almacenes)   
* Correcciones de erratas

## Añadidos (Semana 6, 16/12/02021)

* Correcciones de erratas
* En paquete `controllers`: ampliado `AlmacenenController` para añadir los _endpoint_ que gestionan los artículos de un `Almacen