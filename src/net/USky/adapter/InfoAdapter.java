package net.USky.adapter;

import java.util.List;

import com.example.lasttest.R;
import net.USky.entity.InfoEntity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class InfoAdapter extends BaseAdapter {
	List<InfoEntity> list;
	Context context;

	public InfoAdapter(List<InfoEntity> list, Context context) {
		super();
		this.list = list;
		this.context = context;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public InfoEntity getItem(int position) {
		// TODO Auto-generated method stub
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View view, ViewGroup parent) {
		// TODO Auto-generated method stub
		Helper helper;
		if (view == null) {
			helper = new Helper();
			view = LayoutInflater.from(context).inflate(
					R.layout.show_list_item, null);
			helper.show_text = (TextView) view
					.findViewById(R.id.list_item_text);
			view.setTag(helper);
		} else {
			helper = (Helper) view.getTag();
		}
		helper.show_text.setText(list.get(position).getData());
		return view;
	}

	class Helper {
		TextView show_text;
	}

}
