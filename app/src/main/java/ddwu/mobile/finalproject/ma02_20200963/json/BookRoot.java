package ddwu.mobile.finalproject.ma02_20200963.json;

import java.util.List;

public class BookRoot {
    public List<NaverBook> getItems() {
        return items;
    }

    public void setItems(List<NaverBook> items) {
        this.items = items;
    }

    private List<NaverBook> items;


}
