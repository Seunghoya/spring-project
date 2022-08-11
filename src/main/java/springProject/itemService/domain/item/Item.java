package springProject.itemService.domain.item;

import lombok.Getter;
import lombok.Setter;

//@Data -> @RequiredArgsConstructor + @Getter + @Setter + @ToString + @EqualsAndHashCode 등등 편리한 기능을 대부분 포함하고 있지만
//         각 어노테이션의 주의사항도 함께 포함하고 있다. 따라서 @Data 대신 @Getter, @Setter로 직접 명시하는 것을 권장
@Getter @Setter
public class Item {

    private Long id;
    private String itemName;
    private Integer price;  // null 이 올 수 있게 설계하기 위해 Int 대신 Integer로 설계
    private Integer quantity;

    public Item() {
    }

    public Item(String itemName, Integer price, Integer quantity) {
        this.itemName = itemName;
        this.price = price;
        this.quantity = quantity;
    }
}
