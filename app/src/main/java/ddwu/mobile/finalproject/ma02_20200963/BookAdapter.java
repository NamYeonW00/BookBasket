package ddwu.mobile.finalproject.ma02_20200963;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import ddwu.mobile.finalproject.ma02_20200963.json.NaverBook;

public class BookAdapter extends BaseAdapter {
    public static final String TAG = "BookAdapter";

    private LayoutInflater inflater;
    private Context context;
    private int layout;
    private List<NaverBook> list;



    public BookAdapter(Context context, int resource, List<NaverBook> list) {
        this.context = context;
        this.layout = resource;
        this.list = list;
        inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    @Override
    public int getCount() {
        return list.size();
    }


    @Override
    public NaverBook getItem(int position) {
        return list.get(position);
    }


    @Override
    public long getItemId(int position) {
        return list.get(position).getId();
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Log.d(TAG, "getView with position : " + position);
        View view = convertView;
        BookAdapter.ViewHolder viewHolder = null;

        if (view == null) {
            view = inflater.inflate(layout, parent, false);
            viewHolder = new BookAdapter.ViewHolder();
            viewHolder.tvTitle = view.findViewById(R.id.tvTitle);
            viewHolder.tvAuthor = view.findViewById(R.id.tvAuthor);
            viewHolder.tvPublisher = view.findViewById(R.id.tvPublisher);
            viewHolder.ivImage = view.findViewById(R.id.ivImage);
            view.setTag(viewHolder);
        } else {
            viewHolder = (BookAdapter.ViewHolder)view.getTag();
        }

        NaverBook dto = list.get(position);

        viewHolder.tvTitle.setText(dto.getTitle());
        viewHolder.tvAuthor.setText(dto.getAuthor());
        viewHolder.tvPublisher.setText(dto.getPublisher());

//        Glide 를 사용하여 웹이미지 로딩
        Glide.with(context)
                .load(dto.getImage())
                .into(viewHolder.ivImage);

        return view;
    }


    public void setList(List<NaverBook> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    //    ※ findViewById() 호출 감소를 위해 필수로 사용할 것
    static class ViewHolder {
        public TextView tvTitle = null;
        public TextView tvAuthor = null;
        public TextView tvPublisher = null;
        public ImageView ivImage = null;
    }
}
