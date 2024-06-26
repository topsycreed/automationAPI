package com.productStar.server;

import com.endpoints.examples.bookstore.BookAuthorRequest;
import com.endpoints.examples.bookstore.BookResponse;
import com.endpoints.examples.bookstore.BookServiceGrpc;
import com.endpoints.examples.bookstore.GetBookRequest;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.stub.StreamObserver;

import java.io.IOException;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

public class BookStoreServer {
    private static final Logger logger = Logger.getLogger(BookStoreServer.class.getName());

    private Server server;

    public static void main(String[] args) throws IOException, InterruptedException {
        final BookStoreServer server = new BookStoreServer();
        server.start();
        server.blockUntilShutdown();
    }

    public void start() throws IOException {
        int port = 50055;
        server = ServerBuilder.forPort(port)
                .addService(new BookStoreServiceImpl())
                .build()
                .start();
        logger.info("Server started, listening on " + port);
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                try {
                    BookStoreServer.this.stop();
                } catch (InterruptedException e) {
                    e.printStackTrace(System.err);
                }
            }
        });
    }

    public void stop() throws InterruptedException {
        if (server != null) {
            server.shutdown().awaitTermination(30, TimeUnit.SECONDS);
        }
    }

    /**
     * Await termination on the main thread since the grpc library uses daemon threads.
     */
    private void blockUntilShutdown() throws InterruptedException {
        if (server != null) {
            server.awaitTermination();
        }
    }

    static class BookStoreServiceImpl extends BookServiceGrpc.BookServiceImplBase {

        @Override
        public void getBook(GetBookRequest request, StreamObserver<BookResponse> responseObserver) {

            BookResponse.Builder response = BookResponse.newBuilder();

            int isbn = request.getIsbn();

            if (isbn == 1) {
                response.setResponseCode("200").setMessage("It: A Novel: King, Stephen").build();
            } else {
                response.setResponseCode("200").setMessage("Failed").build();
            }

            responseObserver.onNext(response.build());

            responseObserver.onCompleted();
        }

        @Override
        public void getBooksViaAuthor(BookAuthorRequest request, StreamObserver<BookResponse> responseObserver) {
            BookResponse.Builder response = BookResponse.newBuilder();

            String author = request.getAuthor();

            logger.info("Author name " + author);

            if (author.equals("King, Stephen")) {
                response.setResponseCode("200").setMessage("It: A Novel: King, Stephen").build();
            } else
                response.setResponseCode("200").setMessage("Failed").build();

            responseObserver.onNext(response.build());

            responseObserver.onCompleted();
        }
    }
}
