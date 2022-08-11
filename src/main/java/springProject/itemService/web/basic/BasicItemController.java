package springProject.itemService.web.basic;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import springProject.itemService.domain.item.Item;
import springProject.itemService.domain.item.ItemRepository;

import java.util.List;

@Controller
@RequestMapping("/basic/items")
@RequiredArgsConstructor // final필드나 @NonNull이 붙은 필드의 생성자를 생성한다. DI편의성을 위해 사용
public class BasicItemController {
    private final ItemRepository itemRepository;


//    @RequiredArgsConstructor 어노테이션을 이용해 생성자 자동 생성
//    public BasicItemController(ItemRepository itemRepository) {
//        this.itemRepository = itemRepository;
//    }

    @GetMapping
    public String items(Model model) {
        List<Item> items = itemRepository.findAll();
        model.addAttribute("items", items);
        return "basic/items";
    }

    @GetMapping("/{itemId}")
    public String item(@PathVariable long itemId, Model model) {
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item", item);
        return "basic/item";

    }

    @GetMapping("/add")
    public String addForm() {
        return "basic/addForm";
    }

//    @PostMapping("/add")
    public String addItemV1(@RequestParam String itemName,
                       @RequestParam int price,
                       @RequestParam Integer quantity,
                       Model model) {
        Item item = new Item();
        item.setItemName(itemName);
        item.setPrice(price);
        item.setQuantity(quantity);

        itemRepository.save(item);

        model.addAttribute("item", item);
        return "basic/item";
    }

//    @PostMapping("/add")
    public String addItemV2(@ModelAttribute("item") Item item, Model model) {

        itemRepository.save(item);
//        model.addAttribute("item", item); // 자동추가 되기 때문에 생략
        
        return "basic/item";
    }

//    @PostMapping("/add")
    public String addItemV3(@ModelAttribute Item item) {
        // ModelAttribute의 이름 생략가능
        // 이때 클래스 이름의 첫 글자만 소문자로 변경되어 등록된다.
        // ex) Item -> item, HelloWorld -> helloWorld
        itemRepository.save(item);
        return "basic/item";
    }

//    @PostMapping("/add")
    public String addItemV4(Item item) {
        // @ModelAttribute 자체도 생략 가능
        itemRepository.save(item);
        return "basic/item";
    }

    /**
     * 제품 등록 후 해당 페이지 내에서 새로고침을 반복할 경우 동일상품이 누적해서 등록되는 문제가 있다.
     * 새로고침은 마지막에 서버에 전송한 데이터를 다시 전송하는 방식이므로
     * 마지막으로 POST 데이터 전송이 계속 반복돼 상품이 계속 쌓이는 것이다.
     * 이를 해결하기 위해 리다이렉트를 함으로써 해결할 수 있다.
     * Post/Redirect/Get => PRG라고 한다.
     * 상품 저장 후 뷰 템플릿으로 이동하는 것이 아니라 상품 상세 화면으로 리다이렉트를 호출해주면 된다.
     */

//    @PostMapping("/add")
    public String addItemV5(Item item) {
        itemRepository.save(item);
        return "redirect:/basic/items/" + item.getId();
    }

    /**
     * RedirectAttributes를 사용하면 URL인코딩뿐만 아니라 pathVarible, 쿼리 파라미터까지 처리해준다.
     * redirect:/basic/items/{itemId}
     *  - pathVariable 바인딩: {itemId}
     *  - 나머지는 쿼리 파라미터로 처리 ?status=true
     */

    @PostMapping("/add")
    public String addItemV6(Item item, RedirectAttributes redirectAttributes) {
        Item savedItem = itemRepository.save(item);
        redirectAttributes.addAttribute("itemId", savedItem.getId());
        redirectAttributes.addAttribute("status", true);
        return "redirect:/basic/items/{itemId}";
    }

    @GetMapping("/{itemId}/edit")
    public String editForm(@PathVariable Long itemId, Model model) {
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item", item);
        return "/basic/editForm";
    }

    @PostMapping("/{itemId}/edit")
    public String edit(@PathVariable Long itemId, @ModelAttribute Item item) {
        itemRepository.update(itemId, item);
        return "redirect:/basic/items/{itemId}";
    }

    // 테스트용 데이터 추가
    @PostMapping
    public void init() {

        itemRepository.save(new Item("itemA", 10000, 10));
        itemRepository.save(new Item("itemB", 20000, 20));
    }
}

