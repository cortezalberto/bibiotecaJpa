package org.example;

import org.example.entidades.*;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class Main {
    public static void main(String[] args) {
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("example-unit");

        EntityManager entityManager = entityManagerFactory.createEntityManager();
        System.out.println("en marcha Alberto");

        try {
            // Persistir una nueva entidad Person
            entityManager.getTransaction().begin();

            Localidad loc1 = Localidad.builder()

                    .denominacion("Godoy Cruz")
                    .build();


            Domicilio dom1 = Domicilio.builder()
                    .calle("suipacha")
                    .numero(239)
                    .localidad(loc1)
                    .build();

            Persona per1 = Persona.builder()
                    .nombre("Alberto")
                    .apellido("Cortez")
                    .dni(12)
                    .domicilio(dom1)
                    .build();


            entityManager.persist(loc1); // Persistir Localidad primero
            entityManager.persist(dom1); // Luego Domicilio
            entityManager.persist(per1); // Finalmente Persona

            Autor autor1 = Autor.builder()
                    .nombre("Gabriel García Márquez")
                    .build();

            Autor autor2 = Autor.builder()
                    .nombre("Luis Borges")
                    .build();

            // Crear un Libro y asociarlo al Autor
            Libro libro1 = Libro.builder()
                    .titulo("Cien años de soledad")
                    .fecha(1967)
                    .genero("Realismo mágico")
                    .paginas(496)
                    .build();

            libro1.getAutores().add(autor1);


            Libro libro2 = Libro.builder()
                    .titulo("Doc años de soledad")
                    .fecha(1947)
                    .genero("Realismo ")
                    .paginas(300)
                    .build();

            libro2.getAutores().add(autor1);
            libro2.getAutores().add(autor2);


            // Persistir el Libro (y el Autor asociado debido a la cascada)
            entityManager.persist(libro1);
            entityManager.persist(libro2);

            System.out.println("Grabé--------------------------");

            entityManager.flush();

//   Busco un libro por su Id y veo quines son sus autores
            Long libroId = 2L; // Cambia esto al ID del libro que deseas recuperar
            Libro libro = entityManager.find(Libro.class, libroId);

            if (libro != null) {
                System.out.println("Libro recuperado: " + libro.getTitulo());
                System.out.println("Autores del libro:");
                for (Autor autor : libro.getAutores()) {
                    System.out.println("- " + autor.getNombre());
                }
            } else {
                System.out.println("No se encontró ningún libro con el ID " + libroId);
            }


   // VOY A PRESTAR EL LIBRO

            Persona persona = entityManager.find(Persona.class, per1.getId());
            System.out.println(" Se lo presto a :" + persona);

            persona.getLibros().add(libro);
            entityManager.merge(persona);
            System.out.println("Persona" + persona);


            entityManager.getTransaction().commit();



        }catch (Exception e){

            entityManager.getTransaction().rollback();
            System.out.println(e.getMessage());
            System.out.println("No se pudo grabar ");}

        // Cerrar el EntityManager y el EntityManagerFactory
        entityManager.close();
        entityManagerFactory.close();
    }
}
