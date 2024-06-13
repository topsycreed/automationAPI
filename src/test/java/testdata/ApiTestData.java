package testdata;

import models.Item;
import models.Items;
import models.User;

import java.util.List;

public class ApiTestData {
    public static final User DEFAULT_USER = new User(0, "ProductStar_user_1", "firstName", "lastName", "email@gmail.com", "qwerty", "12345678", 0);

    public static final Items JEANS_ITEM = Items.builder()
            .items(List.of(new Item("0040716474", 1, null)))
            .build();
}
