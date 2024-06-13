import com.endpoints.examples.bookstore.BookServiceGrpc;
import com.productStar.server.BookStoreServer;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.io.IOException;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class SimpleGrpcTests {
    private ManagedChannel channel;

    @BeforeAll
    void setup() throws IOException {
        channel = ManagedChannelBuilder.forAddress("localhost", 50055).usePlaintext().build();
        BookStoreServer grpcServer = new BookStoreServer();
        grpcServer.start();
    }

    @AfterAll
    void tearDown() throws InterruptedException {
        BookStoreServer grpcServer = new BookStoreServer();
        grpcServer.stop();
        channel.shutdown();
    }

    @Test
    void getBookByAuthorTest() {
        var request = com.endpoints.examples.bookstore.BookAuthorRequest.newBuilder()
                .setAuthor("King, Stephen")
                .build();
        var response = BookServiceGrpc.newBlockingStub(channel).getBooksViaAuthor(request);
        Assertions.assertEquals("200", response.getResponseCode());
        Assertions.assertEquals("It: A Novel: King, Stephen", response.getMessage());
    }

    @Test
    void getBookByIsbnTest() {
        var request = com.endpoints.examples.bookstore.GetBookRequest.newBuilder()
                .setIsbn(1)
                .build();
        var response = BookServiceGrpc.newBlockingStub(channel).getBook(request);
        Assertions.assertEquals("200", response.getResponseCode());
        Assertions.assertEquals("It: A Novel: King, Stephen", response.getMessage());
    }
}
