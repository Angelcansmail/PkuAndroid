package cn.edu.pku.ss.gzh.gojson.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import cn.edu.pku.ss.gzh.gojson.R;
import cn.edu.pku.ss.gzh.gojson.entity.Book;

public class BookListAdapter extends BaseAdapter {
	private Context c;
	private ArrayList<Book> list;

	public BookListAdapter(Context context, ArrayList<Book> books) {
		this.c = context;
		this.list = books;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return list.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			convertView = View.inflate(c, R.layout.xml_json_layout, null);
			holder = new ViewHolder();
			holder.tv = (TextView) convertView.findViewById(R.id.tv);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		Book b = list.get(position);
		holder.tv.setText(b.getTitle() + "\n" + b.getPublisher());
		return convertView;
	}

	class ViewHolder {
		TextView tv;
	}

}
