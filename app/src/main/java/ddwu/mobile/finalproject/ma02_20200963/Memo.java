package ddwu.mobile.finalproject.ma02_20200963;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "memo_table")
public class Memo implements Serializable {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String book;
    private String memo_content;
    private int state;

    public Memo() {
        this.state = R.id.rb_before;
    }

    public Memo(int id, String book, String memo_content, int state) {
        this.id = id;
        this.book = book;
        this.memo_content = memo_content;
    }

    public Memo(String book, String memo_content, int state) {
        this.book = book;
        this.memo_content = memo_content;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getBook() {
        return book;
    }

    public void setBook(String book) {
        this.book = book;
    }

    public String getMemo_content() {
        return memo_content;
    }

    public void setMemo_content(String memo_content) {
        this.memo_content = memo_content;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    @Override
    public String toString() {
        return "Memo{" +
                "id=" + id +
                ", book='" + book + '\'' +
                ", memo_content='" + memo_content + '\'' +
                ", state=" + state +
                '}';
    }
}
