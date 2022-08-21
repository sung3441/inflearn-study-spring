package hello.itemservice.web.basic;

import hello.itemservice.domain.item.Item;
import hello.itemservice.domain.item.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.annotation.PostConstruct;
import java.util.List;

@Controller
@RequestMapping("/basic/items")
@RequiredArgsConstructor
public class BasicItemController {

    private final ItemRepository itemRepository;


    @GetMapping
    public String items(Model model) {
        List<Item> items = itemRepository.findAll();
        model.addAttribute("items", items);
        return "basic/items";
    }

    @GetMapping("/{itemId}")
    public String item(@PathVariable long itemId, Model model) {
        Item findItem = itemRepository.findById(itemId);
        model.addAttribute("item", findItem);
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

        Item item = new Item(itemName, price, quantity);

        itemRepository.save(item);
        model.addAttribute("item", item);

        return "basic/item";
    }

    /**
     * @ModelAttribute를 사용하면 객체에 값을 set 해주는 것과 model에 addAttribute 까지 해줌
     * model에 데이터를 넣을 때는 이름이 필요하다. 이름은 @ModelAttribute에 지정한 name(value) 속성을 사용
     *
     * @ModelAttribute("item") Item item    => 이름을 "item"으로 지정
     * model.addAttribute("item", item)     => 모델에 "item" 이름으로 저장
     */
//    @PostMapping("/add")
    public String addItemV2(@ModelAttribute("item") Item item, Model model) {

        itemRepository.save(item);
//        model.addAttribute("item", item); 자동 추가, 생략 가능

        return "basic/item";
    }

    /**
     * @ModelAttribute 에 name을 지정하지 않을 시 Class의 첫 글자만 바꾼 값을 name으로 설정해준다.
     * ex) Item => item, HelloData => helloData
     */
//    @PostMapping("/add")
    public String addItemV3(@ModelAttribute Item item, Model model) {

        itemRepository.save(item);

        return "basic/item";
    }

    /**
     * String, int 등 단순 타입이 오면 @RequestParam이 적용되고,
     * 커스텀 클래스가 파라미터로 오면 @ModelAttribute가 자동 적용되기 때문에 생략 가능하다.
     * 이때도 커스텀 클래스의 첫 글자만 소문자로 바꾼 값이 model의 name으로 설정된다.
     */
//    @PostMapping("/add")
    public String addItemV4(Item item, Model model) {

        itemRepository.save(item);

        return "basic/item";
    }

    /**
     * post로 작업을 한 후 forward를 하게 되면 새로 고침을 했을 때
     * 마지막 실행이었던 post 작업을 계속 실행하게 된다.
     * 이를 방지하기 위해 PRG(Post/Redirect/Get) 패턴을 사용한다.
     *
     * Post 요청 -> 요청 처리 & Redirect 반환 -> Redirect 넘어온 url으로 Get 요청 -> 요청 처리
     */
//    @PostMapping("/add")
    public String addItemV5(Item item, Model model) {

        itemRepository.save(item);

        return "redirect:/basic/items/" + item.getId();
    }

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
        return "basic/editForm";
    }

    @PostMapping("/{itemId}/edit")
    public String edit(@PathVariable Long itemId, Item item) {
        itemRepository.update(itemId, item);
        return "redirect:/basic/items/{itemId}";
    }

    /**
     * 테스트용 데이터 추가
     */
    @PostConstruct
    public void init() {

        itemRepository.save(new Item("itemA", 10_000, 10));
        itemRepository.save(new Item("itemB", 30_000, 30));
    }
}
