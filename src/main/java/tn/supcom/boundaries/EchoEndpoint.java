package tn.supcom.boundaries;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriBuilder;
import tn.supcom.controllers.EchoController;
import tn.supcom.entities.Todo;

@Path("/echoes")
public class EchoEndpoint {
    @GET
    @Path("/{name}")
    public Response getClichedMessage(@PathParam("name") String name){
        return Response.ok().entity("{\"message\":\"Hello " + name + "!\"}").build();
    }

    @Inject
    private EchoController todoManager;

    @GET
    @Path("")
    @Produces(MediaType.APPLICATION_JSON)
    public Response all() {
        return Response.ok(todoManager.getAll()).build();
    }

    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response get(@PathParam("id") String id) {
        Todo todo = todoManager.get(id);
        return Response.ok(todo).build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response add(Todo todo) {
        Todo savedTodo = todoManager.add(todo);
        System.out.println(savedTodo.id);
        return Response.created(
                        UriBuilder.fromResource(this.getClass()).path(String.valueOf(savedTodo.id)).build())
                .build();
    }


}
