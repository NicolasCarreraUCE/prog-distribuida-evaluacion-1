package com.distribuida;

import com.distribuida.model.Book;
import com.distribuida.service.IBookService;
import com.google.gson.Gson;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import com.squareup.moshi.Types;
import jakarta.enterprise.inject.se.SeContainer;
import jakarta.enterprise.inject.se.SeContainerInitializer;
import spark.Request;
import spark.Response;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

import static spark.Spark.*;

public class Principal {
    static SeContainer container;
    static List<Book> listarTodo(Request req, Response res) {
        var servicio = container.select(IBookService.class).get();
        res.type("application/json");

        return servicio.findAll();
    }

    static boolean crear(Request req, Response res) throws IOException {
        res.type("application/json");

        Moshi moshi = new Moshi.Builder().build();
        JsonAdapter<Book> jsonAdapter = moshi.adapter(Book.class);
        Book newBook = jsonAdapter.fromJson(req.body());

        var servicio = container.select(IBookService.class).get();
        servicio.insert(newBook);

        return true;
    }

    static Object buscar(Request req, Response res) {
        var servicio = container.select(IBookService.class).get();
        res.type("application/json");
        String _id = req.params(":id");

        var book =  servicio.findById(Integer.valueOf(_id));

        if(book==null) {
            // 404
            halt(404, "Persona no encontrada");
        }

        return book;
    }

    static boolean actualizar(Request req, Response res) throws IOException {
        var servicio=container.select(IBookService.class).get();
        res.type("application/json");
        String _id = req.params(":id");
        String requestBody=req.body();

        Moshi moshi = new Moshi.Builder().build();
        JsonAdapter<Book> jsonAdapter = moshi.adapter(Book.class);
        Book book = jsonAdapter.fromJson(req.body());

        book.setId(Integer.valueOf(_id));
        servicio.update(book);

        if(book==null){
            halt(404, "Persona no encontrada");
        }
        return  true;
    }

    static boolean eliminar(Request req, Response res) {
        res.type("application/json");

        String _id = req.params(":id");
        Integer id = Integer.valueOf(_id);

        var servicio = container.select(IBookService.class).get();

        boolean eliminado = servicio.deleteById(id);

        if (eliminado) {
            return true;
        } else {
            res.status(404);
            return false;
        }
    }

    public static void main(String[] args) {
        container = SeContainerInitializer.newInstance().initialize();

        IBookService service = container.select(IBookService.class).get();

        port(8080);

        // Crea una instancia de Moshi
        Moshi moshi = new Moshi.Builder().build();

        // Crea un objeto JSON a partir de un objeto Java
        Type type = Types.newParameterizedType(Book.class);
        JsonAdapter<Book> jsonAdapter = moshi.adapter(type);

        get("/books/:id", (req, res) -> Principal.buscar(req, res), jsonAdapter::toJson);
        get("/books", (req, res) -> Principal.listarTodo(req, res), jsonAdapter::toJson);
        post("/book", (req, res) -> Principal.crear(req, res), jsonAdapter::toJson);
        put("/books/:id", (req, res) -> Principal.actualizar(req, res), jsonAdapter::toJson);
        delete("/books/:id", (req, res) -> Principal.eliminar(req, res), jsonAdapter::toJson);

    }
}
